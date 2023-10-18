//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.0 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2023.10.18 a las 01:02:52 PM ECT 
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
 *         &lt;element name="OlvidoResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "olvidoResult"
})
@XmlRootElement(name = "OlvidoResponse")
public class OlvidoResponse {

    @XmlElement(name = "OlvidoResult")
    protected String olvidoResult;

    /**
     * Obtiene el valor de la propiedad olvidoResult.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOlvidoResult() {
        return olvidoResult;
    }

    /**
     * Define el valor de la propiedad olvidoResult.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOlvidoResult(String value) {
        this.olvidoResult = value;
    }

}
