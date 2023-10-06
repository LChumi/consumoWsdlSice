package com.cumpleanos.consumowsdl.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

@Repository
public class ProcedureOracleRepository {

    private final EntityManager entityManager;

    @Autowired
    public ProcedureOracleRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void  crearXml(Long empresa,String comprobante){
        StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery("PRG_USR.AST_SRI.CREA_XML_FAC");

        procedureQuery.registerStoredProcedureParameter("PN_EMPRESA", Long.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("PN_CCOMPROBA", String.class,ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("PN_ESTADO", Integer.class,ParameterMode.OUT);

        procedureQuery.setParameter("PN_EMPRESA",empresa);
        procedureQuery.setParameter("PN_CCOMPROBA",comprobante);

        procedureQuery.execute();

        Integer estado = (Integer) procedureQuery.getOutputParameterValue("PN_ESTADO");

        System.out.println("El estado del procedimiento es: "+ estado);

    }

}
