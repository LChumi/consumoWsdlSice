package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.client.SoapClient;
import com.cumpleanos.consumowsdl.wsdl.RecibirComprobanteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apiWsdl")
public class SoapController {

    @Autowired
    private SoapClient soapClient;

    @PostMapping("/enviarComprobante/")
    public ResponseEntity<?> prueba(@RequestBody String xml, @RequestParam String email, @RequestParam int tipo){
        try {
            RecibirComprobanteResponse response=soapClient.getRecibirComprobanteResponse(xml, email, tipo);

            return ResponseEntity.ok(response.getRecibirComprobanteResult());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
