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
    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    @Scheduled(cron = "${cron.expression.30min}")
    public void gestionFactura() {
        log.info(" ------------- Iniciando proceso Envio Facturacion electronica------------------- ");
        try {
            List<ComprobElecGrande> comprobantes = comprobanteService.listar();
            if (!comprobantes.isEmpty()) {
                for (ComprobElecGrande c : comprobantes) {
                    log.info("{} cco: {} empresa: {} clave {}", c.getXmlf_comprobante(), c.getCco_codigo(), c.getXmlf_empresa(), c.getXmlf_clave());
                    Future<?> future = executor.submit(() -> {
                        try {
                            if (c.getXmlf_caracter() == null || c.getXmlf_caracter().isEmpty()) {
                                log.info("Comprobante sin informacion creando xml");
                                // No necesitamos esperar por esta tarea, ya que no hay llamadas asincrónicas
                                creaXmlEnvia(c);
                            } else {
                                // Ejecutar la tarea y esperar su resultado
                                String respuesta = enviarXml(c); // Esperar a que la tarea termine y obtener el resultado
                                if (validarClaveAcceso(respuesta)) {
                                    log.info("Autorizado {}, {} , respuesta -> {}",c.getXmlf_comprobante(), c.getCco_codigo() , respuesta);
                                    guardarAutorizacion(c.getCco_codigo(), c.getXmlf_empresa(), respuesta);
                                    guardarAutorizacionXmlFac(c.getCco_codigo(), c.getXmlf_empresa(), respuesta);
                                } else {
                                    log.error("Error {}, {} , respuesta -> {}",c.getXmlf_comprobante(), c.getCco_codigo() , respuesta);
                                    guardarErrorXmlFac(c.getCco_codigo(), c.getXmlf_empresa(), respuesta);
                                }
                            }
                        } catch (Exception e) {
                            log.error("Ocurrio un error en la tarea para el comprobante: " + c.getCco_codigo(), e);
                        }
                    });
                    // Esperar a que la tarea termine antes de pasar a la siguiente
                    future.get();
                }
            }
        } catch (Exception e) {
            log.error("Ocurrio un error a nivel general: " + e.getMessage(), e);
        }
    }

    private void creaXml(ComprobElecGrande c){
        try {
            log.info("Creando xml para el comprobante: {} cco: {} empresa: {}",c.getXmlf_comprobante(), c.getCco_codigo(), c.getXmlf_empresa());
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

    private boolean validarClaveAcceso(String claveAcceso){
        try {
            if (claveAcceso.length() != 49) {
                return false;
            }
            return claveAcceso.matches("\\d{49}");
        }catch (Exception e){
            return false;
        }
    }

    @PreDestroy
    public void onDestroy() {
        executor.shutdown();
    }

}
