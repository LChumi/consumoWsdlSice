package com.cumpleanos.consumowsdl.services;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;

import java.util.List;

public interface ComprobElecGrandeService {

    public List<ComprobElecGrande> listar();
    public ComprobElecGrande porComprobante(String comprobante);

}
