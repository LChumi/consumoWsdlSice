package com.cumpleanos.consumowsdl.repository;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;


public interface ComprobElecGrandeRepository extends JpaRepository<ComprobElecGrande,String> {

    @Query(value = "SELECT V FROM ComprobElecGrande V ")
    List<ComprobElecGrande> findAll();

    @Query(value = "SELECT  V FROM ComprobElecGrande V WHERE V.cco_codigo=:id AND V.xmlf_empresa=:empresa ")
    ComprobElecGrande findByCco_codigoAndXmlf_empresa(BigInteger id,Long empresa);

    @Query(value = "SELECT  V FROM ComprobElecGrande V WHERE V.cco_codigo=:id")
    ComprobElecGrande findByCco_codigo(BigInteger id);

    @Query(value =  "SELECT V FROM ComprobElecGrande  V  WHERE V.xmlf_clave=:clave")
    ComprobElecGrande findByXmlf_clave(String clave);
}
