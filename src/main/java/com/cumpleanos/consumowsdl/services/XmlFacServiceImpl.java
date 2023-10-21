/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.services;

import com.cumpleanos.consumowsdl.models.XmlFac;
import com.cumpleanos.consumowsdl.repository.XmlFacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class XmlFacServiceImpl implements XmlFacService{

    @Autowired
    private XmlFacRepository xmlFacRepository;

    @Override
    public XmlFac porId(BigInteger id) {
        return xmlFacRepository.findByXmlfCcoComproba(id);
    }

    @Override
    public XmlFac guardar(XmlFac xmlFac) {
        return xmlFacRepository.save(xmlFac);
    }

    @Override
    public void actualizarPorComprobante(BigInteger id, String error,Long empresa) {
         xmlFacRepository.updateXmlFacByXmlfCcoComproba(id,error,empresa);
    }


}
