package com.cumpleanos.consumowsdl.cron;

import com.cumpleanos.consumowsdl.models.CComfac;
import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.models.XmlFac;
import com.cumpleanos.consumowsdl.repository.ProcedureOracleRepository;
import com.cumpleanos.consumowsdl.services.CComfacService;
import com.cumpleanos.consumowsdl.services.ComprobElecGrandeService;
import com.cumpleanos.consumowsdl.services.SpringConsumoService;
import com.cumpleanos.consumowsdl.services.XmlFacService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
@Slf4j
public class AutomatizacionFacElectronica {

    private final ComprobElecGrandeService comprobanteService;
    private final ProcedureOracleRepository oracleRepository;
    private final CComfacService cComfacService;
    private final XmlFacService xmlFacService;
    private final SpringConsumoService consumoService;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Scheduled(cron = "${cron.expression.30min}")
    private void gestionFactura(){
        log.info(" ------------- Iniciando proceso Envio Facturacion electronica------------------- ");
        try {
            List<ComprobElecGrande> comprobantes = comprobanteService.listar();
            if (!comprobantes.isEmpty()) {
                for (ComprobElecGrande c : comprobantes) {
                    log.info(c.getXmlf_comprobante() + " cco: " + c.getCco_codigo() + " empresa: " + c.getXmlf_empresa() + " clave " + c.getXmlf_clave());
                    if (c.getXmlf_caracter() == null) {
                        // No necesitamos esperar por esta tarea, ya que no hay llamadas asincrónicas
                        creaXmlEnvia(c);
                    } else {
                        // Ejecutar la tarea en el executor y esperar su resultado
                        Future<String> future = executor.submit(() -> enviarXml(c));
                        String respuesta = future.get(); // Esperar a que la tarea termine y obtener el resultado
                        if (validarClaveAcceso(respuesta)) {
                            guardarAutorizacion(c.getCco_codigo(), c.getXmlf_empresa(), respuesta);
                            guardarAutorizacionXmlFac(c.getCco_codigo(), c.getXmlf_empresa(), respuesta);
                        } else {
                            guardarErrorXmlFac(c.getCco_codigo(), c.getXmlf_empresa(), respuesta);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Ocurrio un error a nivel general: " + e.getMessage());
        } finally {
            executor.shutdown(); // Cerrar el executor cuando hayamos terminado de usarlo
        }
    }

    private void creaXml(ComprobElecGrande c){
        try {
            oracleRepository.crearXml(c.getXmlf_empresa(), c.getCco_codigo().toString(),c.getXml_tipoComprobante());
        }catch (Exception e){
            log.error("Ocurrio un problema al crear xml: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
        }
    }

    private String enviarXml(ComprobElecGrande c){
        try {
            return consumoService.firmarXml(c.getXmlf_caracter(), c.getCli_mail());
        }catch (Exception e){
            log.error("Ocurrio un problema al enviar Xml: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
            return "Error";
        }
    }

    private void creaXmlEnvia(ComprobElecGrande c){
        try {
            creaXml(c);
            enviarXml(c);
        }catch (Exception e){
            log.error("Ocurrio un problema al crear y enviar xml: "+" cco: "+c.getCco_codigo()+" en la empresa: "+c.getXmlf_empresa()+" Error "+e.getMessage());
        }
    }

    private void guardarErrorXmlFac(BigInteger cco, Long empresa, String error){
        try{
            XmlFac xml=xmlFacService.porIdYEmpresa(cco,empresa);
            if (xml!=null){
                xmlFacService.actualizarPorComprobante(xml.getXmlfCcoComproba(),error,xml.getXmlfEmpresa());
            }
        }catch (Exception e){
            log.error("Ocurrio un problema al guardar XMLFAC: "+" cco: "+cco+" en la empresa: "+empresa+" Error "+e.getMessage());
        }
    }

    private void guardarAutorizacionXmlFac(BigInteger cco, Long empresa, String auth){
        try{
            XmlFac xml=xmlFacService.porIdYEmpresa(cco,empresa);
            if (xml!=null){
                xmlFacService.actualizarAutorizacion(xml.getXmlfCcoComproba(),auth,xml.getXmlfEmpresa());
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

    private static boolean validarClaveAcceso(String claveAcceso){
        if (claveAcceso.length() != 49){
            return false;
        }
        return claveAcceso.matches("\\d{49}");
    }

    @PreDestroy
    public void onDestroy() {
        executor.shutdown();
    }

}
