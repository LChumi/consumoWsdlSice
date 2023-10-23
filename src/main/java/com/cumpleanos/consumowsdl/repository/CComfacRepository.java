/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.repository;

import com.cumpleanos.consumowsdl.models.CComfac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

public interface CComfacRepository extends JpaRepository<CComfac, BigInteger> {

    @Query(value = "SELECT V FROM CComfac  V WHERE  V.cco_comproba=:cco AND V.empresa=:empresa")
    CComfac findByCco_comprobaAndEmpresa(BigInteger cco,Long empresa);

    @Query(value = "UPDATE CComfac SET acl_nroautoriza=:autorizacion WHERE cco_comproba=:cco AND empresa=:empresa")
    @Modifying
    @Transactional
    void updateCComfacByCco_comprobaAndEmpresa(@Param("cco") BigInteger cco,@Param("empresa") Long empresa, @Param("autorizacion") String autorizacion);
}
