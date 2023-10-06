package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.repository.ProcedureOracleRepository;
import com.cumpleanos.consumowsdl.services.ComprobElecGrandeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ComprobantesVista")
public class ComprobElecGrandeController {

    private static final Logger logger = LoggerFactory.getLogger(ComprobElecGrandeController.class);

    @Autowired
    private ComprobElecGrandeService service;

    @Autowired
    private ProcedureOracleRepository oracleRepository;


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

    @GetMapping("/creaXml/{comprobante}")
    public ResponseEntity<?> crearXml(@PathVariable String comprobante){
        try {
            ComprobElecGrande com=service.porComprobante(comprobante);
            System.out.println(com.getCco_codigo().toString());
            System.out.println(com.getXmlf_empresa());
            oracleRepository.crearXml(com.getXmlf_empresa(), com.getCco_codigo().toString());

            return ResponseEntity.ok("Procedimiento ejecutado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error "+e);
        }
    }




}
