/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.repository;

import com.cumpleanos.consumowsdl.models.CComfac;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface CComfacRepository extends JpaRepository<CComfac, BigInteger> {

}
