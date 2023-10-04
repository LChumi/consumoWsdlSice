package com.cumpleanos.consumowsdl.models;

import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;

@Entity
@Immutable
@Subselect("SELECT V.* FROM COMPROB_ELEC_GRANDE_V V")
@Data
public class ComprobElecGrande {

    @Id
    @Column(name = "XMLF_COMPROBANTE")
    private String comprobante;

    @Column(name = "CCO_FECHA")
    private Date fecha;

    @Column(name = "XMLF_EMPRESA")
    private Long empresa;

    @Column(name = "XMLF_MENSAJE")
    private String mensaje;

    @Lob
    @Column(name = "XMLF_CARACTER")
    private String caracter;

    @Column(name = "XMLF_ERROR")
    private String error;

    @Column(name = "XMLF_AUTORIZACION")
    private String autorizacion;

    @Column(name = "XMLF_CLAVE")
    private String clave;

    @Column(name = "XML_TIPO_COMPROBANTE")
    private Long tipoComprobante;

    @Column(name = "CLI_MAIL")
    private String email;

    @Column(name = "XMLF_SIZE")
    private Long size;

}
