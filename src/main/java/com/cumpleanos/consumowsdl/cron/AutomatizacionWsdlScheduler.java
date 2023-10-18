package com.cumpleanos.consumowsdl.cron;

import com.cumpleanos.consumowsdl.client.SoapClient;
import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.models.modelsxml.Autorizaciones;
import com.cumpleanos.consumowsdl.models.modelsxml.Comprobante;
import com.cumpleanos.consumowsdl.models.modelsxml.Data;
import com.cumpleanos.consumowsdl.repository.ComprobElecGrandeRepository;
import com.cumpleanos.consumowsdl.repository.ProcedureOracleRepository;
import com.cumpleanos.consumowsdl.wsdl.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;


@Component
public class AutomatizacionWsdlScheduler {

    private final static Logger LOG = LoggerFactory.getLogger(AutomatizacionWsdlScheduler.class);
    private final SoapClient soapClient;
    private final ComprobElecGrandeRepository repository;
    private final ProcedureOracleRepository oracleRepository;

    @Autowired
    public AutomatizacionWsdlScheduler(SoapClient soapClient, ComprobElecGrandeRepository repository, ProcedureOracleRepository oracleRepository) {
        this.soapClient = soapClient;
        this.repository = repository;
        this.oracleRepository = oracleRepository;
    }

    @Scheduled(cron = "${cron.expression.30min}")
    public void gestionSise(){
        LOG.info("Iniciando proceso... ");
        try {
            List<ComprobElecGrande> comprobantes= repository.findAll();
            if (!comprobantes.isEmpty()){
                for (ComprobElecGrande c:comprobantes){
                    LOG.info(c.getXmlf_comprobante());
                    if (c.getXmlf_caracter()==null){
                        creaXmlEnvia(c);
                    } else {
                        String autorizacionEstado=obtieneAuth(c);
                        if (autorizacionEstado.contains("NO COMPROBANTE") || autorizacionEstado.contains("SIN AUTORIZACION")){
                            if (autorizacionEstado.contains("NO COMPROBANTE")) {
                                enviarXml(c);
                            }
                            verificarYProcesar(c);
                        }
                    }
                }
            }
        }catch (Exception e){
            LOG.error("Error: "+e.getMessage());
        }
    }

    protected void creaXml(ComprobElecGrande c){
        try {
            oracleRepository.crearXml(c.getXmlf_empresa(), c.getCco_codigo().toString(),c.getXml_tipoComprobante());
        }catch (Exception e){
            LOG.error("Error "+e.getMessage());
        }
    }

    protected String obtieneAuth(ComprobElecGrande c){
        try {
            ObtieneAutorizacionResponse autorizacion=soapClient.getObtieneAutorizacion(c.getXmlf_clave());
            System.out.println(autorizacion.getObtieneAutorizacionResult());
            return autorizacion.getObtieneAutorizacionResult();
        }catch (Exception e){
            LOG.error(c.getXmlf_comprobante()+" "+"Error "+e.getMessage());
            return "Error";
        }
    }

    protected String enviarXml(ComprobElecGrande c){
        try {
            RecibirComprobanteResponse recibe= soapClient.getRecibirComprobanteResponse(c.getXmlf_caracter(), c.getCli_mail(), c.getXml_tipoComprobante());
            return recibe.getRecibirComprobanteResult();
        }catch (Exception e){
            LOG.error(c.getXmlf_comprobante()+" "+"Error "+e.getMessage());
            return "Error";
        }
    }


    protected String verificarComprobante(ComprobElecGrande c){
        try {
            VerificarComprobanteResponse verifica= soapClient.getVerificarComprobanteResponse(c.getXmlf_clave());
            return verifica.getVerificarComprobanteResult();
        }catch (Exception e){
            LOG.error(c.getXmlf_comprobante()+" "+"Error "+e.getMessage());
            return "Error";
        }
    }

    protected String obtenerEstado(ComprobElecGrande c){
        try{
            GetComprobanteDataResponse response= soapClient.getComprobante(c.getXmlf_clave());

            try {
                String xmlResponse= response.getGetComprobanteDataResult();

                JAXBContext jaxbContext= JAXBContext.newInstance(Data.class);
                Unmarshaller unmarshaller= jaxbContext.createUnmarshaller();

                Data data= (Data) unmarshaller.unmarshal(new StringReader(xmlResponse));
                return data.getEstado();
            }catch (JAXBException e){
                LOG.error(c.getXmlf_comprobante()+" "+e.getMessage());
                return "Error";
            }
        }catch (Exception e){
            LOG.error(c.getXmlf_comprobante()+" "+e.getMessage());
            return "Error";
        }
    }

    protected String obtenerRespuesta(ComprobElecGrande c){
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
                LOG.error(c.getXmlf_comprobante()+" "+e.getMessage());
        }catch (Exception e){
            LOG.error(c.getXmlf_comprobante()+" "+"Error "+ e.getMessage());
        }
        return "Error";
    }

    protected void creaXmlEnvia(ComprobElecGrande c){
        try {
            creaXml(c);
            enviarXml(c);
        }catch (Exception e){
            LOG.error("Error: "+e.getMessage());
        }
    }

    protected  void verificarYProcesar(ComprobElecGrande c){
        try {
            verificarComprobante(c);
            String estado=obtenerEstado(c);
            if (estado.contains("Autorizado")){
                obtieneAuth(c);
            } else if (estado.contains("Enviado")) {
                obtenerRespuesta(c);
            }else{
                obtenerRespuesta(c);
                enviarXml(c);
            }
        }catch (Exception e){
            LOG.error(c.getXmlf_comprobante()+" Error: "+e.getMessage());
        }
    }
}
