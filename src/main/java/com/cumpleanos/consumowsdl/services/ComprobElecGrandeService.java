package com.cumpleanos.consumowsdl.services;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;

import java.math.BigInteger;
import java.util.List;

public interface ComprobElecGrandeService {

    List<ComprobElecGrande> listar();
    ComprobElecGrande porCco(BigInteger id);
    ComprobElecGrande porCcoYEmpresa(BigInteger id,Long empresa);
    ComprobElecGrande porClave(String clave);


}
