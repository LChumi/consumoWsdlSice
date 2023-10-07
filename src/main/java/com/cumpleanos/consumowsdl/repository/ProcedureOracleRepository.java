package com.cumpleanos.consumowsdl.repository;

import com.cumpleanos.consumowsdl.models.enums.TipoDoc;
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

    public void  crearXml(Long empresa,String comprobante,int tipoDoc){
        String creaDoc="";

        switch (tipoDoc){
            case 1:
                creaDoc=TipoDoc.CREA_XML_FAC.toString();
                break;
            case 2:
                creaDoc=TipoDoc.CREA_XML_NCC.toString();
                break;
            case 3:
                creaDoc=TipoDoc.CREA_XML_RET.toString();
                break;
            case 4:
                creaDoc=TipoDoc.CREA_XML_GUI.toString();
                break;
            case 5:
                creaDoc=TipoDoc.CREA_XML_LIQ.toString();
        }


        StoredProcedureQuery procedureQuery = entityManager.createStoredProcedureQuery("PRG_USR.AST_SRI."+creaDoc);

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
