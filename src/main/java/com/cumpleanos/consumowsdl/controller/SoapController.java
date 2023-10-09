package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.client.SoapClient;
import com.cumpleanos.consumowsdl.wsdl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiWsdl")
public class SoapController {

    @Autowired
    private SoapClient soapClient;

    @PostMapping("/recibirComprobante/")
    public ResponseEntity<?> recibirComprobante(@RequestBody String xml, @RequestParam String email, @RequestParam int tipo){
        try {
            RecibirComprobanteResponse response=soapClient.getRecibirComprobanteResponse(xml, email, tipo);

            return ResponseEntity.ok(response.getRecibirComprobanteResult());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verificarComprobante")
    public ResponseEntity<?> verificarComprobante(@RequestParam String clave){
        try{
            VerificarComprobanteResponse response= soapClient.getVerificarComprobanteResponse(clave);

            return  ResponseEntity.ok(response.getVerificarComprobanteResult());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/obtieneAutorizacion")
    public ResponseEntity<?> obtieneAutorizacion(@RequestParam String clave){
        try{
            ObtieneAutorizacionResponse response= soapClient.getObtieneAutorizacion(clave);

            return  ResponseEntity.ok(response.getObtieneAutorizacionResult());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getComprobante")
    public ResponseEntity<?> obtieneComprobante(@RequestParam String clave){
        try{
            GetComprobanteDataResponse response= soapClient.getComprobante(clave);

            System.out.println(response.getGetComprobanteDataResult());
            return ResponseEntity.ok(response.getGetComprobanteDataResult());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getRespuesta")
    public ResponseEntity<?> obtieneRespuesta(@RequestParam String clave){
        try{
            GetRespuestaResponse response= soapClient.getRespuesta(clave);
            System.out.println(response.getGetRespuestaResult());
            return ResponseEntity.ok(response.getGetRespuestaResult());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getXml")
    public ResponseEntity<?> obtieneXml(@RequestParam String clave){
        try{
            GetXMLResponse response= soapClient.getXml(clave);
            System.out.println(response.getGetXMLResult());
            return ResponseEntity.ok(response.getGetXMLResult());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
