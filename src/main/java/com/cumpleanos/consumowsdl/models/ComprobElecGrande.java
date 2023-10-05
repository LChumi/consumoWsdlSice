package com.cumpleanos.consumowsdl.models;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Immutable
@Subselect("SELECT XMLF_COMPROBANTE, CCO_FECHA, XMLF_EMPRESA, XMLF_MENSAJE, XMLF_CARACTER, XMLF_ERROR, XMLF_AUTORIZACION, XMLF_CLAVE, XML_TIPO_COMPROBANTE, CLI_MAIL, XMLF_SIZE FROM COMPROB_ELEC_GRANDE_V")
@Data
public class ComprobElecGrande {

    @Id
    @Column(name = "XMLF_COMPROBANTE")
    private String xmlf_comprobante;

    @Column(name = "CCO_FECHA")
    private Date cco_fecha;

    @Column(name = "XMLF_EMPRESA")
    private Long xmlf_empresa;

    @Column(name = "XMLF_MENSAJE")
    private String xmlf_mensaje;

    @Column(name = "XMLF_ERROR")
    private String xmlf_error;

    @Column(name = "XMLF_AUTORIZACION")
    private String xmlf_autorizacion;

    @Column(name = "XMLF_CLAVE")
    private String xmlf_clave;

    @Column(name = "XML_TIPO_COMPROBANTE")
    private int xml_tipoComprobante;

    @Column(name = "CLI_MAIL")
    private String cli_mail;

    @Column(name = "XMLF_SIZE")
    private Long xmlf_size;

    @Column(name = "XMLF_CARACTER")
    private String xmlf_caracter;

}
