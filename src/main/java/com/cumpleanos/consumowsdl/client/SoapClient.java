package com.cumpleanos.consumowsdl.client;

import com.cumpleanos.consumowsdl.wsdl.RecibirComprobante;
import com.cumpleanos.consumowsdl.wsdl.RecibirComprobanteResponse;
import com.cumpleanos.consumowsdl.wsdl.VerificarComprobante;
import com.cumpleanos.consumowsdl.wsdl.VerificarComprobanteResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class SoapClient extends WebServiceGatewaySupport {

    /**
     * Metodo comprobar el estado de los comprobantes
     * @param xml
     * @param email
     * @param formato
     * @return el estado del comprobante
     */
    public RecibirComprobanteResponse getRecibirComprobanteResponse(String xml,String email, int formato){

        RecibirComprobante recibirComprobante=new RecibirComprobante();
        recibirComprobante.setXml(xml);
        recibirComprobante.setMail(email);
        recibirComprobante.setFormato(formato);

        SoapActionCallback soapActionCallback=new SoapActionCallback("http://tempuri.org/RecibirComprobante");

        RecibirComprobanteResponse response= (RecibirComprobanteResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.siac.com.ec/SICE/ws/Metodos.asmx",recibirComprobante,soapActionCallback);

        return response;
    }

    /**
     * Metodo para verificar los comprobantes
     * @param clave
     * @return comprobante si esta o no verificado
     */
    public VerificarComprobanteResponse getVerificarComprobanteResponse(String clave){

        VerificarComprobante verificarComprobante=new VerificarComprobante();
        verificarComprobante.setClave(clave);

        SoapActionCallback soapActionCallback=new SoapActionCallback("http://tempuri.org/VerificarComprobante");

        VerificarComprobanteResponse response=(VerificarComprobanteResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.siac.com.ec/SICE/ws/Metodos.asmx",verificarComprobante,soapActionCallback);

        return response;
    }

}
