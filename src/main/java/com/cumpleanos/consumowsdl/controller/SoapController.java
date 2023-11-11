package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.client.SoapClient;
import com.cumpleanos.consumowsdl.models.modelsxml.Autorizaciones;
import com.cumpleanos.consumowsdl.models.modelsxml.Comprobante;
import com.cumpleanos.consumowsdl.models.modelsxml.Data;
import com.cumpleanos.consumowsdl.wsdl.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@RestController
@RequestMapping("/apiWsdl")
@CrossOrigin("*")
public class SoapController {

    private final static Logger LOG = LoggerFactory.getLogger(SoapController.class);

    @Autowired
    private SoapClient soapClient;

    @PostMapping("/recibirComprobante/")
    public ResponseEntity<?> recibirComprobante(@RequestBody String xml, @RequestParam String email, @RequestParam int tipo){
        try {
            RecibirComprobanteResponse response=soapClient.getRecibirComprobanteResponse(xml, email, tipo);
            String jsonResponse= new ObjectMapper().writeValueAsString(response.getRecibirComprobanteResult());//convertir a json
            return ResponseEntity.ok(jsonResponse);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verificarComprobante")
    public ResponseEntity<?> verificarComprobante(@RequestParam String clave){
        try{
            VerificarComprobanteResponse response= soapClient.getVerificarComprobanteResponse(clave);
            String jsonResponse= new ObjectMapper().writeValueAsString(response.getVerificarComprobanteResult());//convertir a json
            return  ResponseEntity.ok(jsonResponse);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/obtieneAutorizacion")
    public ResponseEntity<?> obtieneAutorizacion(@RequestParam String clave){
        try{
            ObtieneAutorizacionResponse response= soapClient.getObtieneAutorizacion(clave);
            String jsonResponse= new ObjectMapper().writeValueAsString(response.getObtieneAutorizacionResult());//convertir a json
            return  ResponseEntity.ok(jsonResponse);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getComprobante")
    public ResponseEntity<?> obtieneComprobante(@RequestParam String clave){
        try{
            GetComprobanteDataResponse response= soapClient.getComprobante(clave);

            String xmlResponse= response.getGetComprobanteDataResult();

            JAXBContext jaxbContext= JAXBContext.newInstance(Data.class);
            Unmarshaller unmarshaller= jaxbContext.createUnmarshaller();

            Data data= (Data) unmarshaller.unmarshal(new StringReader(xmlResponse));

            return ResponseEntity.ok(data);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/obtenerEstado")
    public ResponseEntity<String> obtenerEstado(@RequestParam String clave){
        try{
            GetComprobanteDataResponse response= soapClient.getComprobante(clave);

            String xmlResponse= response.getGetComprobanteDataResult();

            JAXBContext jaxbContext= JAXBContext.newInstance(Data.class);
            Unmarshaller unmarshaller= jaxbContext.createUnmarshaller();

            Data data= (Data) unmarshaller.unmarshal(new StringReader(xmlResponse));
            String jsonResponse= new ObjectMapper().writeValueAsString(data.getEstado());//convertir a json
            return ResponseEntity.ok(jsonResponse);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getRespuesta")
    public ResponseEntity<?> obtieneRespuesta(@RequestParam String clave){
        try{
            GetRespuestaResponse response= soapClient.getRespuesta(clave);
            String xmlResponse=response.getGetRespuestaResult();

            JAXBContext jaxbContext;
            Unmarshaller unmarshaller;
            Object responseObject=null;
            try {
                if (xmlResponse.contains("<autorizaciones>")) {
                    jaxbContext=JAXBContext.newInstance(Autorizaciones.class);
                    unmarshaller=jaxbContext.createUnmarshaller();
                    responseObject=(Autorizaciones) unmarshaller.unmarshal(new StringReader(xmlResponse));
                } else if (xmlResponse.contains("<comprobante>")){
                    jaxbContext=JAXBContext.newInstance(Comprobante.class);
                    unmarshaller=jaxbContext.createUnmarshaller();
                    responseObject=(Comprobante) unmarshaller.unmarshal(new StringReader(xmlResponse));
                }
            }catch (JAXBException e){
                LOG.error(e.getMessage());
            }

            if (responseObject!= null){
                return ResponseEntity.ok(responseObject);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verRespuesta")
    public ResponseEntity<String> verRespuesta(@RequestParam String clave){
        try{
            String respuesta="";

            GetRespuestaResponse response= soapClient.getRespuesta(clave);
            String xmlResponse=response.getGetRespuestaResult();

            JAXBContext jaxbContext;
            Unmarshaller unmarshaller;
            Object responseObject=null;
            try {
                if (xmlResponse.contains("<autorizaciones>")) {
                    jaxbContext=JAXBContext.newInstance(Autorizaciones.class);
                    unmarshaller=jaxbContext.createUnmarshaller();
                    responseObject=(Autorizaciones) unmarshaller.unmarshal(new StringReader(xmlResponse));
                    Autorizaciones auth=(Autorizaciones) unmarshaller.unmarshal(new StringReader(xmlResponse));
                    respuesta=auth.getAutorizacion().getEstado();
                } else if (xmlResponse.contains("<comprobante>")){
                    jaxbContext=JAXBContext.newInstance(Comprobante.class);
                    unmarshaller=jaxbContext.createUnmarshaller();
                    responseObject=(Comprobante) unmarshaller.unmarshal(new StringReader(xmlResponse));
                    Comprobante com=(Comprobante) unmarshaller.unmarshal(new StringReader(xmlResponse));
                    respuesta=com.getMensajes().getMensaje().getInformacionAdicional();                }
            }catch (JAXBException e){
                LOG.error(e.getMessage());
            }

            if (responseObject!= null){
                String jsonResponse= new ObjectMapper().writeValueAsString(respuesta);//convertir a json
                return ResponseEntity.ok(jsonResponse);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getXml")
    public ResponseEntity<?> obtieneXml(@RequestParam String clave){
        try{
            GetXMLResponse response= soapClient.getXml(clave);

            return ResponseEntity.ok(response.getGetXMLResult());
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
