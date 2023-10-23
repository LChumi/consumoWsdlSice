/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.services;

import com.cumpleanos.consumowsdl.models.CComfac;
import com.cumpleanos.consumowsdl.repository.CComfacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CComfacServiceImpl implements CComfacService{

    @Autowired
    private CComfacRepository cComfacRepository;

    @Override
    public CComfac porCcoYEmpresa(BigInteger cco, Long empresa) {
        return cComfacRepository.findByCco_comprobaAndEmpresa(cco, empresa);
    }

    @Override
    public void actualizarPorCcoEmpresa(BigInteger cco, Long empresa,String autorizacion) {
        cComfacRepository.updateCComfacByCco_comprobaAndEmpresa(cco, empresa,autorizacion);
    }
}
