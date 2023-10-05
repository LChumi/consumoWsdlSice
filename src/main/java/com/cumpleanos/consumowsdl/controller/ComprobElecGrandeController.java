package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.services.ComprobElecGrandeService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ComprobantesVista")
public class ComprobElecGrandeController {

    @Autowired
    private ComprobElecGrandeService service;

    @GetMapping("/listar")
    public ResponseEntity<List<ComprobElecGrande>> listar(){
        try{
            List<ComprobElecGrande> listaComprobantes=service.listar();
            if (listaComprobantes.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(listaComprobantes,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscarComprobante/{comprobante}")
    public ResponseEntity<ComprobElecGrande> buscar(@PathVariable String comprobante){
        try{
            ComprobElecGrande compr= service.porComprobante(comprobante);
            if (compr==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(compr,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
