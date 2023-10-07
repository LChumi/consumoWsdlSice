package com.cumpleanos.consumowsdl.cron;

import com.cumpleanos.consumowsdl.client.SoapClient;
import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.repository.ComprobElecGrandeRepository;
import com.cumpleanos.consumowsdl.repository.ProcedureOracleRepository;
import com.cumpleanos.consumowsdl.wsdl.ObtieneAutorizacionResponse;
import com.cumpleanos.consumowsdl.wsdl.RecibirComprobanteResponse;

import com.cumpleanos.consumowsdl.wsdl.VerificarComprobanteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    public void enviarComprobanteHora(){
        LOG.info("Iniciando proceso.... ");
        try {
            List<ComprobElecGrande> comprobantes=repository.findAll();
            if (!comprobantes.isEmpty()){
                for (ComprobElecGrande c:comprobantes){
                    if (c.getXmlf_caracter()==null){
                        try {
                            oracleRepository.crearXml(c.getXmlf_empresa(), c.getCco_codigo().toString(),c.getXml_tipoComprobante());
                        }catch (Exception e){
                            LOG.error("Error "+e.getMessage());
                            continue;
                        }
                    }
                }
                for (ComprobElecGrande c:comprobantes){
                    try {
                        ObtieneAutorizacionResponse autorizacion=soapClient.getObtieneAutorizacion(c.getXmlf_clave());
                        System.out.println(autorizacion.getObtieneAutorizacionResult());
                    }catch (Exception e){
                        LOG.error("Error "+e.getMessage());
                        continue;
                    }
                }

                for (ComprobElecGrande c:comprobantes){
                    try {
                        RecibirComprobanteResponse recibe= soapClient.getRecibirComprobanteResponse(c.getXmlf_caracter(), c.getCli_mail(), c.getXml_tipoComprobante());
                        System.out.println(recibe.getRecibirComprobanteResult());
                    }catch (Exception e){
                        LOG.error("Error "+e.getMessage());
                        continue;
                    }
                }

                for (ComprobElecGrande c:comprobantes){
                    try {
                        VerificarComprobanteResponse verifica= soapClient.getVerificarComprobanteResponse(c.getXmlf_clave());
                        System.out.println(verifica.getVerificarComprobanteResult());
                    }catch (Exception e){
                        LOG.error("Error "+e.getMessage());
                        continue;
                    }
                }


                System.out.println(comprobantes.size());
            }

        }catch (Exception e){
            LOG.error("Error "+e.getMessage());
        }
    }



}
