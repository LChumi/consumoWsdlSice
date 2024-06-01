/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.services;

import com.cumpleanos.consumowsdl.models.XmlFac;

import java.math.BigInteger;

public interface XmlFacService {

    XmlFac guardar(XmlFac xmlFac);
    void actualizarPorComprobante(BigInteger id,String error,Long empresa);
    XmlFac porIdYEmpresa(BigInteger id, Long empresa);
    void actualizarAutorizacion(BigInteger id,String auth,Long empresa);

}
