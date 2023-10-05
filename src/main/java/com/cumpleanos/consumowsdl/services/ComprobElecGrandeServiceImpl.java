package com.cumpleanos.consumowsdl.services;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import com.cumpleanos.consumowsdl.repository.ComprobElecGrandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ComprobElecGrande porComprobante(String comprobante) {
        return repository.findByXmlf_comprobante(comprobante);
    }


}
