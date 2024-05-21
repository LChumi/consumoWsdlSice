package com.cumpleanos.consumowsdl.cron;

import com.cumpleanos.consumowsdl.client.SoapClient;
import com.cumpleanos.consumowsdl.models.CComfac;
import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.models.XmlFac;
import com.cumpleanos.consumowsdl.models.modelsxml.Autorizaciones;
import com.cumpleanos.consumowsdl.models.modelsxml.Comprobante;
import com.cumpleanos.consumowsdl.models.modelsxml.Data;
import com.cumpleanos.consumowsdl.repository.ProcedureOracleRepository;
import com.cumpleanos.consumowsdl.services.CComfacService;
import com.cumpleanos.consumowsdl.services.ComprobElecGrandeService;
import com.cumpleanos.consumowsdl.services.SpringConsumoService;
import com.cumpleanos.consumowsdl.services.XmlFacService;
import com.cumpleanos.consumowsdl.wsdl.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class AutomatizacionWsdlScheduler {

    private final SoapClient soapClient;
    private final ComprobElecGrandeService comprobanteService;
    private final ProcedureOracleRepository oracleRepository;
    private final CComfacService cComfacService;
    private final XmlFacService xmlFacService;
    private final SpringConsumoService consumoService;

    

    @Scheduled(cron = "${cron.expression.30min}")
    private void gestionSise(){
        log.info(" ------------- Iniciando proceso ------------------- ");
        try {
            List<ComprobElecGrande> comprobantes=comprobanteService.listar();
            if (!comprobantes.isEmpty()){
                for (ComprobElecGrande c:comprobantes){
                    log.info(c.getXmlf_comprobante()+" cco: "+c.getCco_codigo()+" empresa: "+c.getXmlf_empresa() +" clave "+c.getXmlf_clave());
                    if (c.getXmlf_caracter()==null){
                        creaXmlEnvia(c);
                    } else {
                        String autorizacionEstado=obtieneAuth(c);
                        String response = consumoService.firmarXml(c.getXmlf_caracter(),c.getCli_mail());
                        System.out.println("------------------------------------------------");
                        log.info(response);
                        System.out.println("------------------------------------------------");
                        if (autorizacionEstado.equals(c.getXmlf_clave())){
                            //guardar en BD -> ccomfac
                            guardarAutorizacion(c.getCco_codigo(),c.getXmlf_empresa(),autorizacionEstado);
                            log.warn("CCOMFAC actualizado: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa());
                        } else if (autorizacionEstado.contains("NO COMPROBANTE") || autorizacionEstado.contains("SIN AUTORIZACION")) {
                            if (autorizacionEstado.contains("NO COMPROBANTE")) {
                                enviarXml(c);
                            }
                            verificarYProcesar(c);
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("Ocurrio un error a nivel general: "+e.getMessage());
        }
    }

    private void creaXml(ComprobElecGrande c){
        try {
            oracleRepository.crearXml(c.getXmlf_empresa(), c.getCco_codigo().toString(),c.getXml_tipoComprobante());
        }catch (Exception e){
            log.error("Ocurrio un problema al crear xml: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
        }
    }

    private String obtieneAuth(ComprobElecGrande c){
        try {
            ObtieneAutorizacionResponse autorizacion=soapClient.getObtieneAutorizacion(c.getXmlf_clave());
            return autorizacion.getObtieneAutorizacionResult();
        }catch (Exception e){
            log.error("Ocurrio un problema al obtener autorizacion: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
            return "Error";
        }
    }

    private String enviarXml(ComprobElecGrande c){
        try {
            RecibirComprobanteResponse recibe= soapClient.getRecibirComprobanteResponse(c.getXmlf_caracter(), c.getCli_mail(), c.getXml_tipoComprobante());
            return recibe.getRecibirComprobanteResult();
        }catch (Exception e){
            log.error("Ocurrio un problema al enviar Xml: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
            return "Error";
        }
    }

    private String verificarComprobante(ComprobElecGrande c){
        try {
            VerificarComprobanteResponse verifica= soapClient.getVerificarComprobanteResponse(c.getXmlf_clave());
            return verifica.getVerificarComprobanteResult();
        }catch (Exception e){
            log.error("Ocurrio un problema al verificar comprobante: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
            return "Error";
        }
    }

    private String obtenerEstado(ComprobElecGrande c){
        try{
            GetComprobanteDataResponse response= soapClient.getComprobante(c.getXmlf_clave());

            try {
                String xmlResponse= response.getGetComprobanteDataResult();

                JAXBContext jaxbContext= JAXBContext.newInstance(Data.class);
                Unmarshaller unmarshaller= jaxbContext.createUnmarshaller();

                Data data= (Data) unmarshaller.unmarshal(new StringReader(xmlResponse));
                return data.getEstado();
            }catch (JAXBException e){
                log.error("Ocurrio un problema al obtener estado: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
                return "Error";
            }
        }catch (Exception e){
            log.error("Ocurrio un problema al obtener estado: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
            return "Error";
        }
    }

    private String obtenerRespuesta(ComprobElecGrande c){
        try{
            GetRespuestaResponse response= soapClient.getRespuesta(c.getXmlf_clave());
            String xmlResponse=response.getGetRespuestaResult();

            JAXBContext jaxbContext;
            Unmarshaller unmarshaller;

                if (xmlResponse.contains("<autorizaciones>")) {
                    jaxbContext=JAXBContext.newInstance(Autorizaciones.class);
                    unmarshaller=jaxbContext.createUnmarshaller();
                    Autorizaciones autorizaciones=(Autorizaciones) unmarshaller.unmarshal(new StringReader(xmlResponse));
                    return autorizaciones.getAutorizacion().getEstado();
                } else if (xmlResponse.contains("<comprobante>")){
                    jaxbContext=JAXBContext.newInstance(Comprobante.class);
                    unmarshaller=jaxbContext.createUnmarshaller();
                    Comprobante comprobante=(Comprobante) unmarshaller.unmarshal(new StringReader(xmlResponse));
                    return comprobante.getMensajes().getMensaje().getInformacionAdicional();
                }
        }catch (JAXBException e){
                log.error(c.getXmlf_comprobante()+" "+e.getMessage());
        }catch (Exception e){
            log.error("Ocurrio un problema al obtener respuesta: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
        }
        return "Error";
    }

    private void creaXmlEnvia(ComprobElecGrande c){
        try {
            creaXml(c);
            enviarXml(c);
        }catch (Exception e){
            log.error("Ocurrio un problema al crear y enviar xml: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
        }
    }

    private  void verificarYProcesar(ComprobElecGrande c){
        try {
            verificarComprobante(c);
            String estado=obtenerEstado(c);
            if (estado.equalsIgnoreCase("Autorizado")){
                obtieneAuth(c);
            } else if (estado.contains("Enviado")) {
                //guardar en BD -> xmlfac
                guardarErrorXmlFac(c.getCco_codigo(),c.getXmlf_empresa(),obtenerRespuesta(c));
                log.warn("XMLFAC actualizado: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa());
            }else{
                obtenerRespuesta(c);
                enviarXml(c);
            }
        }catch (Exception e){
            log.error("Ocurrio un problema al verificar y procesar  comprobante: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
        }
    }

    private void guardarErrorXmlFac(BigInteger cco,Long empresa,String error){
        try{
            XmlFac xml=xmlFacService.porIdYEmpresa(cco,empresa);
            if (xml!=null){
                xmlFacService.actualizarPorComprobante(xml.getXmlfCcoComproba(),error,xml.getXmlfEmpresa());
            }
        }catch (Exception e){
            log.error("Ocurrio un problema al guardar XMLFAC: "+" cco: "+cco+" en la empresa: "+empresa+" Error "+e.getMessage());
        }
    }

    private void guardarAutorizacion(BigInteger cco,Long empresa,String autorizacion){
        try {
            CComfac cfac=cComfacService.porCcoYEmpresa(cco, empresa);
            if (cfac!=null){
                cComfacService.actualizarPorCcoEmpresa(cco, empresa, autorizacion);
            }
        }catch (Exception e){
            log.error("Ocurrio un problema al guardar CCOMFAC: "+" cco: "+cco+" en la empresa: "+empresa+" Error "+e.getMessage());
        }
    }
}
