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
import java.util.concurrent.ExecutionException;
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

    private volatile boolean shuttingDown = false;

    @Scheduled(cron = "${cron.expression.30min}")
    public void gestionFactura() {
        if (shuttingDown) {
            log.warn("Aplicacion detenida, omitiendo ejecucion del scheduler");
            return;
        }

        log.info("======================= Iniciando procesos Envio Facturacion Electronica =====================");
        long inicio = System.currentTimeMillis();

        try {
            List<ComprobElecGrande> comprobantes = comprobanteService.listar();
            int total = comprobantes.size();

            if (comprobantes.isEmpty()) {
                log.info("No hay comprobantes pendientes.");
                return;
            }

            log.info("Comprobantes a procesar: {}", total);
            int procesados = 0, errores = 0;

            for (ComprobElecGrande c : comprobantes) {
                if (shuttingDown || Thread.currentThread().isInterrupted()) {
                    log.warn("Proceso interrumpido en comprobantes {} de {}", procesados + 1, total);
                    break;
                }

                log.info("[{}/{}] Procesando: {} | cco: {} | empresa {}", procesados +1 , total, c.getXmlf_comprobante(), c.getCco_codigo(), c.getXmlf_empresa());

                boolean ok = procesarComprobante(c);
                if (ok) procesados++; else errores++;
            }

            long duracion = System.currentTimeMillis() - inicio;
            log.info("========== Proceso finalizado | OK: {} | Errores: {} | Tiempo: {}ms ==========",procesados, errores, duracion);
        } catch (Exception e){
            log.error("Error general en gestionFactura: {}", e.getMessage(), e);
        }
    }

    /**
     * Procesa un comprobante individual.
     * @return true si se procesó sin errores, false en caso contrario
     */
    private boolean procesarComprobante(ComprobElecGrande c) {
        try {
            if (sinXml(c)) {
                log.info("Sin XML → creando y enviando para cco: {}", c.getCco_codigo());
                creaXmlEnvia(c);
            } else {
                log.info("Con XML → enviando para cco: {}", c.getCco_codigo());
                String respuesta = enviarXml(c);

                if (validarClaveAcceso(respuesta)) {
                    log.info("AUTORIZADO: {} | cco: {}", c.getXmlf_comprobante(), c.getCco_codigo());
                    guardarAutorizacion(c.getCco_codigo(), c.getXmlf_empresa(), respuesta);
                    guardarAutorizacionXmlFac(c.getCco_codigo(), c.getXmlf_empresa(), respuesta);
                } else {
                    log.error("RECHAZADO: {} | cco: {} | respuesta: {}",
                            c.getXmlf_comprobante(), c.getCco_codigo(), respuesta);
                    guardarErrorXmlFac(c.getCco_codigo(), c.getXmlf_empresa(), respuesta);
                }
            }
            return true;

        } catch (Exception e) {
            log.error("Error procesando cco: {} | empresa: {} | {}",
                    c.getCco_codigo(), c.getXmlf_empresa(), e.getMessage(), e);
            return false;
        }
    }

    private boolean sinXml(ComprobElecGrande c) {
        return c.getXmlf_caracter() == null || c.getXmlf_caracter().isBlank();
    }

    private void creaXml(ComprobElecGrande c) {
        try {
            log.debug("Creando XML → cco: {} | empresa: {}", c.getCco_codigo(), c.getXmlf_empresa());
            oracleRepository.crearXml(
                    c.getXmlf_empresa(),
                    c.getCco_codigo().toString(),
                    c.getXml_tipoComprobante()
            );
        } catch (Exception e) {
            log.error("Error al crear XML → cco: {} | empresa: {} | {}",
                    c.getCco_codigo(), c.getXmlf_empresa(), e.getMessage(), e);
        }
    }

    private String enviarXml(ComprobElecGrande c) {
        try {
            return consumoService.firmarXml(c.getXmlf_caracter(), c.getCli_mail());
        } catch (Exception e) {
            log.error("Error al enviar XML → cco: {} | empresa: {} | {}",
                    c.getCco_codigo(), c.getXmlf_empresa(), e.getMessage(), e);
            return "Error";
        }
    }

    private void creaXmlEnvia(ComprobElecGrande c) {
        creaXml(c);
        // Refrescamos el comprobante para obtener el XML recién creado
        // Si el servicio lo soporta, recarga aquí. Si no, deja el envío para la siguiente ejecución.
        String caracter = c.getXmlf_caracter();
        if (caracter != null && !caracter.isBlank()) {
            enviarXml(c);
        } else {
            log.info("XML creado, se enviará en la próxima ejecución → cco: {}", c.getCco_codigo());
        }
    }

    private void guardarErrorXmlFac(BigInteger cco, Long empresa, String error) {
        try {
            XmlFac xml = xmlFacService.porIdYEmpresa(cco, empresa);
            if (xml != null) {
                xmlFacService.actualizarPorComprobante(xml.getXmlfCcoComproba(), error, xml.getXmlfEmpresa());
            }
        } catch (Exception e) {
            log.error("Error guardando error en XMLFAC → cco: {} | empresa: {} | {}", cco, empresa, e.getMessage(), e);
        }
    }

    private void guardarAutorizacionXmlFac(BigInteger cco, Long empresa, String auth) {
        try {
            XmlFac xml = xmlFacService.porIdYEmpresa(cco, empresa);
            if (xml != null) {
                xmlFacService.actualizarAutorizacion(xml.getXmlfCcoComproba(), auth, xml.getXmlfEmpresa());
            }
        } catch (Exception e) {
            log.error("Error guardando autorización en XMLFAC → cco: {} | empresa: {} | {}", cco, empresa, e.getMessage(), e);
        }
    }

    private void guardarAutorizacion(BigInteger cco, Long empresa, String autorizacion) {
        try {
            CComfac cfac = cComfacService.porCcoYEmpresa(cco, empresa);
            if (cfac != null) {
                cComfacService.actualizarPorCcoEmpresa(cco, empresa, autorizacion);
            }
        } catch (Exception e) {
            log.error("Error guardando autorización en CCOMFAC → cco: {} | empresa: {} | {}", cco, empresa, e.getMessage(), e);
        }
    }

    private boolean validarClaveAcceso(String claveAcceso) {
        if (claveAcceso == null || claveAcceso.length() != 49) return false;
        return claveAcceso.matches("\\d{49}");
    }

    @PreDestroy
    public void onDestroy() {
        log.info("Shutdown detectado → scheduler detenido limpiamente");
        shuttingDown = true;
    }

}
