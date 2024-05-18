package com.cumpleanos.consumowsdl.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "COMPROB_ELEC_GRANDE_V")
@Data
public class ComprobElecGrande implements Comparable<ComprobElecGrande>{

    @Id
    @Column(name = "CCO_CODIGO")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigInteger cco_codigo;

    @Column(name = "XMLF_COMPROBANTE")
    private String xmlf_comprobante;

    @Column(name = "CCO_FECHA")
    private LocalDate cco_fecha;

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

    @Override
    public int compareTo(ComprobElecGrande o) {
        if (o == null){
            throw  new NullPointerException("No se puede comparar objeto nulo");
        }
        return this.cco_fecha.compareTo(o.cco_fecha);
    }
}
