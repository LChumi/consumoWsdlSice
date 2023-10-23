/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.cron.AutomatizacionWsdlScheduler;
import com.cumpleanos.consumowsdl.models.XmlFac;
import com.cumpleanos.consumowsdl.services.XmlFacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/xmlFac")
public class XmlFacController {

    private final static Logger LOG = LoggerFactory.getLogger(XmlFacController.class);


    @Autowired
    private XmlFacService service;

    @GetMapping("/PorId/{id}/{empresa}/porEmpresa")
    public ResponseEntity<XmlFac> listaPorId(@PathVariable BigInteger id, @PathVariable Long empresa){
        try {
            XmlFac xml=service.porIdYEmpresa(id,empresa);
            if (xml==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(xml,HttpStatus.OK);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/actualizarError/{id}/{error}/{empresa}")
    public ResponseEntity<XmlFac> actualizarError(@PathVariable BigInteger id,@PathVariable String error,@PathVariable Long empresa){
        try{
            XmlFac xml=service.porIdYEmpresa(id,empresa);
            if (xml==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            xml.setXmlfError(error);
            //service.guardar(xml);
            service.actualizarPorComprobante(xml.getXmlfCcoComproba(), error,xml.getXmlfEmpresa());
            XmlFac xmlActualizado=service.porIdYEmpresa(id,empresa);
            return ResponseEntity.ok(xmlActualizado);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
