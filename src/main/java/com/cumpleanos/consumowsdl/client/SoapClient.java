package com.cumpleanos.consumowsdl.client;

import com.cumpleanos.consumowsdl.wsdl.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class SoapClient extends WebServiceGatewaySupport {

    /**
     * Metodo comprobar el estado de los comprobantes
     * @param xml
     * @param email
     * @param formato
     * @return el estado del comprobante(OK, El comprobante ya esta en proceso)
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
     * @return comprobante si esta o no verificado(Verificando... o NO COMPROBANTE )
     */
    public VerificarComprobanteResponse getVerificarComprobanteResponse(String clave){

        VerificarComprobante verificarComprobante=new VerificarComprobante();
        verificarComprobante.setClave(clave);

        SoapActionCallback soapActionCallback=new SoapActionCallback("http://tempuri.org/VerificarComprobante");

        VerificarComprobanteResponse response=(VerificarComprobanteResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.siac.com.ec/SICE/ws/Metodos.asmx",verificarComprobante,soapActionCallback);

        return response;
    }

    /**
     * Metodo para obtener la autorizacion
     * @param clave
     * @return devuelve si el documento fue autorizado (SIN AUTORIZACION, NO COMPROBANTE O LA CLAVE 09110900...)
     */
    public ObtieneAutorizacionResponse getObtieneAutorizacion(String clave){

        ObtieneAutorizacion obtieneAutorizacion=new ObtieneAutorizacion();
        obtieneAutorizacion.setClave(clave);

        SoapActionCallback soapActionCallback=new SoapActionCallback("http://tempuri.org/ObtieneAutorizacion");

        ObtieneAutorizacionResponse response=(ObtieneAutorizacionResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.siac.com.ec/SICE/ws/Metodos.asmx",obtieneAutorizacion,soapActionCallback);

        return response;
    }

    /**
     * Metodo para obtener el estado del comprobante como devuelto
     * @param clave
     * @return devuelve un xml con la respuesta de la solicitud
     */
    public GetComprobanteDataResponse getComprobante(String clave){

        GetComprobanteData comprobanteData=new GetComprobanteData();
        comprobanteData.setClave(clave);

        SoapActionCallback soapActionCallback=new SoapActionCallback("http://tempuri.org/GetComprobanteData");

        GetComprobanteDataResponse response=(GetComprobanteDataResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.siac.com.ec/SICE/ws/Metodos.asmx",comprobanteData,soapActionCallback);

        return response;
    }

    /**
     * metodo para obtener la respuesta del comprobante
     * @param clave
     * @return un xml dento de el viene si tiene un error cual es el error
     */
    public GetRespuestaResponse getRespuesta(String clave){

        GetRespuesta respuesta=new GetRespuesta();
        respuesta.setClave(clave);

        SoapActionCallback soapActionCallback=new SoapActionCallback("http://tempuri.org/GetRespuesta");

        GetRespuestaResponse response=(GetRespuestaResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.siac.com.ec/SICE/ws/Metodos.asmx",respuesta,soapActionCallback);

        return response;
    }

    /**
     * Metodo para obtener el xml del comprobante
     * @param clave
     * @return el xml del comprobante
     */
    public GetXMLResponse getXml(String clave){

        GetXML xml =new GetXML();
        xml.setClave(clave);

        SoapActionCallback soapActionCallback=new SoapActionCallback("http://tempuri.org/GetXML");

        GetXMLResponse response=(GetXMLResponse) getWebServiceTemplate().marshalSendAndReceive("http://www.siac.com.ec/SICE/ws/Metodos.asmx",xml,soapActionCallback);

        return response;
    }

}
