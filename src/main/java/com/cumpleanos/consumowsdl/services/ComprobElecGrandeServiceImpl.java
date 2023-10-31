package com.cumpleanos.consumowsdl.services;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.repository.ComprobElecGrandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ComprobElecGrandeServiceImpl implements ComprobElecGrandeService {

    @Autowired
    private ComprobElecGrandeRepository repository;

    @Override
    public List<ComprobElecGrande> listar() {
        return repository.findAll();
    }

    @Override
    public ComprobElecGrande porCco(BigInteger id) {
        return repository.findByCco_codigo(id);
    }

    @Override
    public ComprobElecGrande porCcoYEmpresa(BigInteger id, Long empresa) {
        return repository.findByCco_codigoAndXmlf_empresa(id, empresa);
    }

    @Override
    public ComprobElecGrande porClave(String clave) {
        return repository.findByXmlf_clave(clave);
    }

    @Override
    public List<ComprobElecGrande> listaPorEmpresa(Long empresa) {
        return repository.findAllByXmlf_empresa(empresa);
    }

}
