package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.repository.ProcedureOracleRepository;
import com.cumpleanos.consumowsdl.services.ComprobElecGrandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comprobantesVista")
public class ComprobElecGrandeController {

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
            oracleRepository.crearXml(com.getXmlf_empresa(), com.getCco_codigo().toString(),com.getXml_tipoComprobante());

            return ResponseEntity.ok("Procedimiento ejecutado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error "+e);
        }
    }




}
