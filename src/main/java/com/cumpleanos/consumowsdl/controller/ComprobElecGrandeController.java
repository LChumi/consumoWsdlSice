package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.repository.ProcedureOracleRepository;
import com.cumpleanos.consumowsdl.services.ComprobElecGrandeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/comprobantesVista")
@CrossOrigin("*")
public class ComprobElecGrandeController {

    private final static Logger LOG = LoggerFactory.getLogger(ComprobElecGrandeController.class);

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
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listaEmpresa/{empresa}")
    public ResponseEntity<List<ComprobElecGrande>> listaEmpresa(@PathVariable Long empresa){
        try {
            List<ComprobElecGrande> listaComprobante=service.listaPorEmpresa(empresa);
            if (listaComprobante.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(listaComprobante,HttpStatus.OK);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscarPorCco/{cco}")
    public ResponseEntity<ComprobElecGrande> porCco(@PathVariable BigInteger cco){
        try{
            ComprobElecGrande comp= service.porCco(cco);
            if (comp==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(comp,HttpStatus.OK);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/creaXml/{cco}/empresa/{empresa}")
    public ResponseEntity<?> crearXml(@PathVariable BigInteger cco,@PathVariable Long empresa){
        try {
            ComprobElecGrande com=service.porCcoYEmpresa(cco,empresa);
            oracleRepository.crearXml(com.getXmlf_empresa(), com.getCco_codigo().toString(),com.getXml_tipoComprobante());

            return ResponseEntity.ok("Procedimiento ejecutado correctamente");
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error "+e);
        }
    }

    @GetMapping("/buscarPorCCoYEmpresa/{cco}/{empresa}")
    public ResponseEntity<ComprobElecGrande> porCcoEmpresa(@PathVariable BigInteger cco, @PathVariable Long empresa){
        try{
            ComprobElecGrande comp= service.porCcoYEmpresa(cco,empresa);
            if (comp==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(comp,HttpStatus.OK);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/verXml/{cco}/{empresa}",produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getXml(@PathVariable BigInteger cco, @PathVariable Long empresa){
        try{
            ComprobElecGrande comp= service.porCcoYEmpresa(cco,empresa);
            if (comp==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(comp.getXmlf_caracter(),HttpStatus.OK);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscarPorClave/{clave}")
    public ResponseEntity<ComprobElecGrande> porClave(@PathVariable String clave){
        try {
            ComprobElecGrande comp= service.porClave(clave);
            if (comp==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(comp,HttpStatus.OK);
        }catch (Exception e){
            LOG.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
