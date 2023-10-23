/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "CCOMFAC")
@Data
public class CComfac {

    @Id
    @Column(name = "cfac_cco_comproba")
    private BigInteger cco_comproba;

    @Column(name = "cfac_empresa")
    private Long empresa;

    @Column(name = "cfac_cco_pedido")
    private BigInteger cco_pedido;

    @Column(name = "cfac_vigencia")
    private Date vigencia;

    @Column(name = "cfac_autoriza")
    private Long autoriza;

    @Column(name = "cfac_nombre")
    private String nombre;

    @Column(name = "cfac_direccion")
    private String direccion;

    @Column(name = "cfac_telefono")
    private String telefono;

    @Column(name = "cfac_ced_ruc")
    private String ced_ruc;

    @Column(name = "cfac_ciudad")
    private Long ciudad;

    @Column(name = "cfac_tipopago")
    private Long tipopago;

    @Column(name = "cfac_acl_nroautoriza")
    private String acl_nroautoriza;

}
