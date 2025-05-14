package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.services.ComprobElecGrandeService;
import com.cumpleanos.consumowsdl.services.SpringConsumoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/apiWsdl")
@CrossOrigin("*")
@Slf4j
public class SoapController {

    @Autowired
    private SpringConsumoService springConsumoService;
    @Autowired
    private ComprobElecGrandeService service;

    @PostMapping("/obtieneAutorizacion")
    public ResponseEntity<?> obtieneAutorizacion(@RequestParam String clave){
        try{
            String response= springConsumoService.obtenerAutoriizacion(clave);
            if (springConsumoService.validarClaveAcceso(response)){
                return ResponseEntity.ok("AUTORIZADO");
            }else{
                return ResponseEntity.ok("NO AUTORIZADO");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PostMapping("/reenviarComprobante")
    public ResponseEntity<?> reenviarComprobante(@RequestBody String xml, @RequestParam String email){
        log.info(xml);
        try {
            String respuesta =springConsumoService.firmarXml(xml, email);
            return ResponseEntity.ok(respuesta);
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping("/envioComprobante/{idEmpresa}/{cco}")
    public ResponseEntity<?> enviarComprobante(@PathVariable Long idEmpresa, @PathVariable BigInteger cco){
        try {
            ComprobElecGrande comprobante= service.porCcoYEmpresa(cco,idEmpresa);
            if (comprobante== null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            String respuesta =springConsumoService.firmarXml(comprobante.getXmlf_caracter(), comprobante.getCli_mail());
            return ResponseEntity.ok(respuesta);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
