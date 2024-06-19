package com.cumpleanos.consumowsdl.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;


@Service
@Slf4j
@RequiredArgsConstructor
public class SpringConsumoService {

    @Value("${spring.base-url}")
    private String baseSpringUrl;

    private final RestTemplate restTemplate;

    public String firmarXml(String xml,String correo){

        String url = baseSpringUrl+"procesarXml"  + "?correo=" + correo;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_XML));
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));

        HttpEntity<String> entity = new HttpEntity<>(xml, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            } else {
                throw new HttpServerErrorException(response.getStatusCode(), "Error en el servicio Spring Comprobantes: "+response.getBody());
            }
        }catch (HttpServerErrorException e){
            log.error("ERROR: al enviar Xml {}", e.getMessage(), e);
            return "Ocurrio un error";
        }
    }

    public String obtenerAutoriizacion(String claveAcceso){

        String url = baseSpringUrl+"obtenerRespuesta/" + claveAcceso;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setAccept(Collections.singletonList(MediaType.TEXT_PLAIN));
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            } else {
                log.error("ERROR: al enviar Xml {} codigo resultado{}", response.getBody(), response.getStatusCode());
                throw new HttpServerErrorException(response.getStatusCode(), "Error en el servicio Spring Comprobantes: "+response.getBody());
            }
        }catch (HttpServerErrorException e){
            log.error("ERROR: al enviar Xml {}", e.getMessage(), e);
            return "Ocurrio un error";
        }
    }

    public boolean validarClaveAcceso(String claveAcceso){
        try {
            if (claveAcceso.length() != 49) {
                return false;
            }
            return claveAcceso.matches("\\d{49}");
        }catch (Exception e){
            return false;
        }
    }



}
