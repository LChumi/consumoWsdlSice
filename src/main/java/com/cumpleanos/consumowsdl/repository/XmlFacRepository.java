/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.repository;

import com.cumpleanos.consumowsdl.models.XmlFac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

public interface XmlFacRepository extends JpaRepository<XmlFac, BigInteger> {

    @Query(value = "SELECT V FROM XmlFac V WHERE V.xmlfCcoComproba=:id and V.xmlfEmpresa=:empresa")
    XmlFac findByXmlfCcoComprobaAndXmlfEmpresa(BigInteger id,Long empresa);

    @Query(value = "UPDATE XmlFac SET xmlfError=:xmlfError WHERE xmlfCcoComproba=:id AND xmlfEmpresa=:empresa")
    @Modifying
    @Transactional
    void updateXmlFacByXmlfCcoComproba(@Param("id") BigInteger id,@Param("xmlfError") String xmlfError,@Param("empresa") Long empresa);

    @Query(value = "UPDATE XmlFac SET xmlfAutorizacion=:xmlfAutorizacion WHERE xmlfCcoComproba=:id AND xmlfEmpresa=:empresa")
    @Modifying
    @Transactional
    void updateAuthXmlFacByXmlfCcoComproba(@Param("id") BigInteger id,@Param("xmlfAutorizacion") String xmlfAutorizacion,@Param("empresa") Long empresa);

}
