/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.services;

import com.cumpleanos.consumowsdl.models.CComfac;

import java.math.BigInteger;

public interface CComfacService {

    CComfac porCcoYEmpresa(BigInteger cco,Long empresa);
    void actualizarPorCcoEmpresa(BigInteger cco,Long empresa,String autorizacion);

}
