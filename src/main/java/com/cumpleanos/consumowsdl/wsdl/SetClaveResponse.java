//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.0 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2023.10.02 a las 10:21:14 AM ECT 
//


package com.cumpleanos.consumowsdl.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SetClaveResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "setClaveResult"
})
@XmlRootElement(name = "SetClaveResponse")
public class SetClaveResponse {

    @XmlElement(name = "SetClaveResult")
    protected String setClaveResult;

    /**
     * Obtiene el valor de la propiedad setClaveResult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetClaveResult() {
        return setClaveResult;
    }

    /**
     * Define el valor de la propiedad setClaveResult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetClaveResult(String value) {
        this.setClaveResult = value;
    }

}
