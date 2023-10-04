package com.cumpleanos.consumowsdl.repository;

import com.cumpleanos.consumowsdl.models.ComprobElecGrande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ComprobElecGrandeRepository extends JpaRepository<ComprobElecGrande,String> {

    @Query("SELECT V FROM ComprobElecGrande V WHERE V.comprobante LIKE: comprobante")
    public ComprobElecGrande findByComprobante (String comprobante);
}
