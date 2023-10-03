package com.cumpleanos.consumowsdl.client;

import com.cumpleanos.consumowsdl.wsdl.RecibirComprobante;
import com.cumpleanos.consumowsdl.wsdl.RecibirComprobanteResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class SoapClient extends WebServiceGatewaySupport {

    /**
     * Metodo para enviar los comprobantes
     * @param xml
     * @param email
     * @param formato
     * @return
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

}
