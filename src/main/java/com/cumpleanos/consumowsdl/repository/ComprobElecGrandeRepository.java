package com.cumpleanos.consumowsdl.repository;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ComprobElecGrandeRepository extends JpaRepository<ComprobElecGrande,String> {

    @Query(value = "SELECT V FROM ComprobElecGrande V WHERE V.xmlf_comprobante = :comprobante")
    public ComprobElecGrande findByXmlf_comprobante(String comprobante);

    @Query(value = "SELECT V FROM ComprobElecGrande V ")
    public List<ComprobElecGrande> findAll();
}
