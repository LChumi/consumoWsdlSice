package com.cumpleanos.consumowsdl.cron;

import com.cumpleanos.consumowsdl.client.SoapClient;
import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.repository.ComprobElecGrandeRepository;
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

    @Autowired
    public AutomatizacionWsdlScheduler(SoapClient soapClient, ComprobElecGrandeRepository repository) {
        this.soapClient = soapClient;
        this.repository = repository;
    }

    @Scheduled(cron = "${cron.expression.5min}")
    public void enviarComprobanteHora(){
        int ok=0;
        int err=0;
        LOG.info("Ejecutando .................");
        try {
            List<ComprobElecGrande> listaOb=repository.findAll();
            if (!listaOb.isEmpty()){
                System.out.println(listaOb.size());
                for (ComprobElecGrande com:listaOb){
                    try{
                        RecibirComprobanteResponse respuesta= soapClient.getRecibirComprobanteResponse(com.getXmlf_caracter(), com.getCli_mail(), com.getXml_tipoComprobante());
                        System.out.println(respuesta.getRecibirComprobanteResult());
                        ok++;
                    }catch (Exception e){
                        System.out.println("Doc"+com.getXmlf_comprobante()+"Error "+e.getMessage());
                        System.out.println("Verificando Doc.............");
                        VerificarComprobanteResponse response=soapClient.getVerificarComprobanteResponse(com.getXmlf_clave());
                        System.out.println(response.getVerificarComprobanteResult());
                        err++;
                        continue;
                    }
                }
            }else {
                LOG.error("vacia");
            }
            System.out.println(ok +" "+ err);
        }catch (Exception e){
            LOG.error("Error "+e.getMessage());
        }
    }



}
