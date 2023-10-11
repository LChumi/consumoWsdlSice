/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.models.modelsxml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@lombok.Data
public class Data {
    private String clave;
    private String fecharecibe;
    private String fechaenvia;
    private String fechaRespuesta;
    private String fechaautoriza;
    private String estado;
    private String mensaje;
}
