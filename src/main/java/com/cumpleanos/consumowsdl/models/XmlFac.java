/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "XML_FAC")
@Data
public class XmlFac {

    @Id
    @Column(name = "XMLF_CCO_COMPROBA")
    private BigInteger xmlfCcoComproba;

    @Column(name = "XMLF_EMPRESA")
    private Long xmlfEmpresa;

    @Column(name = "XMLF_VALOR")
    private String xmlfValor;

    @Column(name = "XMLF_VALOR_FIRMA")
    private String xmlfValorFirma;

    @Column(name = "XMLF_ESTADO")
    private Integer xmlfEstado;

    @Column(name = "XMLF_MENSAJE")
    private String xmlfMensaje;

    @Column(name = "XMLF_ERROR")
    private String xmlfError;

    @Column(name = "MOD_USR")
    private String modUsr;

    @Column(name = "MOD_FECHA")
    private Date modFecha;

    @Column(name = "XMLF_CARACTER")
    private String xmlfCaracter;

    @Column(name = "XMLF_CLAVE")
    private String xmlfClave;

    @Column(name = "XMLF_AUTORIZACION")
    private String xmlfAutorizacion;

    @Column(name = "XMLF_TIPO")
    private Integer xmlfTipo;
}