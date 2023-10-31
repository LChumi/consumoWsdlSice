/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.models.CComfac;
import com.cumpleanos.consumowsdl.services.CComfacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/ccomfac")
@CrossOrigin("*")
public class CComfacController {

    private final static Logger LOG = LoggerFactory.getLogger(CComfacController.class);

    @Autowired
    private CComfacService service;

    @GetMapping("/porCco/{cco}/{empresa}/empresa")
    public ResponseEntity<CComfac> porCcoEmpresa(@PathVariable BigInteger cco,@PathVariable Long empresa){
        try {
            CComfac cfac=service.porCcoYEmpresa(cco, empresa);
            if (cfac==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(cfac,HttpStatus.OK);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/actualizarAutorizacion/{cco}/{empresa}/autorizacion/{autorizacion}")
    public ResponseEntity<?> actualizar(@PathVariable BigInteger cco,@PathVariable Long empresa,@PathVariable String autorizacion){
        try {
            CComfac cfac=service.porCcoYEmpresa(cco, empresa);
            if (cfac==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cfac no encontrado");
            }
            service.actualizarPorCcoEmpresa(cco, empresa, autorizacion);
            return ResponseEntity.ok("CComfac actualizado");
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>("Hubo un error al actualizar", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
