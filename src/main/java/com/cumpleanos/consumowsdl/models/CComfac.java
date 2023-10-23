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
@Table(name = "XML_FAC")
@Data
public class CComfac {

    @Column(name = "cfac_empresa")
    private Long empresa;

    @Id
    @Column(name = "cfac_cco_comproba")
    private BigInteger cco_comproba;

    @Column(name = "cfac_cco_pedido")
    private BigInteger cco_pedido;

    @Column(name = "cfac_otr_comproba")
    private BigInteger otr_comproba;

    @Column(name = "cfac_vigencia")

    private Date vigencia;

    @Column(name = "cfac_autoriza")
    private Long autoriza;

    @Column(name = "cfac_politica")
    private Long politica;

    @Column(name = "cfac_lista_precios")
    private Long lista_precios;

    @Column(name = "cfac_est_entrega")
    private Long est_entrega;

    @Column(name = "cfac_proc_fac")
    private Long proc_fac;

    @Column(name = "cfac_cco_recibo")
    private BigInteger cco_recibo;

    @Column(name = "cfac_proceso")
    private Long proceso;

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

    @Column(name = "cfac_prioridad")
    private Long prioridad;

    @Column(name = "cfac_empleado")
    private Long empleado;

    @Column(name = "cfac_con_codigo")
    private Long con_codigo;

    @Column(name = "cfac_area")
    private Long area;

    @Column(name = "cfac_departamento")
    private Long departamento;

    @Column(name = "cfac_observaciones")
    private String observaciones;

    @Column(name = "cfac_tipo_actpro")
    private Long tipo_actpro;

    @Column(name = "cfac_impuesto")
    private Long impuesto;

    @Column(name = "cfac_porc_impuesto")
    private Long porc_impuesto;

    @Column(name = "cfac_factura")
    private String factura;

    @Column(name = "cfac_facturatrans")
    private String facturatrans;

    @Column(name = "cfac_tipoevento")
    private Long tipoevento;

    @Column(name = "cfac_tipogasto")
    private Long tipogasto;

    @Column(name = "cfac_fecha_fac")
    private Date fecha_fac;

    @Column(name = "cfac_montorecibo")
    private Double montorecibo;

    @Column(name = "cfac_tipopago")
    private Long tipopago;

    @Column(name = "cfac_acl_nroautoriza")
    private String acl_nroautoriza;

    @Column(name = "cfac_acl_retdato")
    private Long acl_retdato;

    @Column(name = "cfac_acl_tablacoa")
    private Long acl_tablacoa;

    @Column(name = "cfac_devolucioncoa")
    private String devolucioncoa;

    @Column(name = "cfac_creditocoa")
    private String creditocoa;

    @Column(name = "cfac_secuenciacoa")
    private String secuenciacoa;

    @Column(name = "cfac_comision")
    private Long comision;

    @Column(name = "cfac_provision")
    private Long provision;

    @Column(name = "cfac_prorrateo")
    private Long prorrateo;

    @Column(name = "cfac_sisdistru")
    private Long sisdistru;

    @Column(name = "cfac_esevento")
    private Long esevento;

    @Column(name = "cfac_activo")
    private Long activo;

    @Column(name = "cfac_empresa_act")
    private Long empresa_act;

    @Column(name = "cfac_kilometraje_ini")
    private Double kilometraje_ini;

    @Column(name = "cfac_kilometraje_fin")
    private Double kilometraje_fin;

    @Column(name = "cfac_control_temp")
    private Long control_temp;

    @Column(name = "cfac_usr_liquida")
    private Long usr_liquida;

    @Column(name = "cfac_tipo_bono")
    private Long tipo_bono;

    @Column(name = "cfac_observaciones1")
    private String observaciones1;

    @Column(name = "cfac_trans_nombre")
    private String trans_nombre;

    @Column(name = "cfac_trans_ced_ruc")
    private String trans_ced_ruc;

    @Column(name = "cfac_emp_referencia")
    private Long emp_referencia;

    @Column(name = "cfac_cco_referemp")
    private BigInteger cco_referemp;

    @Column(name = "cfac_replica")
    private Long replica;

    @Column(name = "cfac_concepto_ret")
    private Long concepto_ret;

    @Column(name = "cfac_opr_ccomproba")
    private BigInteger opr_ccomproba;

    @Column(name = "cfac_est_com_elect")
    private Long est_com_elect;

    @Column(name = "cfac_cli_dir_ent")
    private Long cli_dir_ent;

    @Column(name = "cfac_acl_clave")
    private String acl_clave;

    @Column(name = "cfac_acl_mensaje")
    private String acl_mensaje;
}
