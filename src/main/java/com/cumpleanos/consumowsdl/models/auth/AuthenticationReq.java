/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.models.auth;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationReq implements Serializable {

    private static final long serialVerionUID= 1L;

    private String usuario;
    private String clave;

}
