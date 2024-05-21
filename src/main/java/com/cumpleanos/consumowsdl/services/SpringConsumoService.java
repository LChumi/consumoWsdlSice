package com.cumpleanos.consumowsdl.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Service
@Slf4j
@RequiredArgsConstructor
public class SpringConsumoService {

    @Value("${spring.base-url}")
    private String baseSpringUrl;

    private final RestTemplate restTemplate;

    public String firmarXml(String xml,String correo){

        String url = baseSpringUrl  + "?correo=" + correo;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        HttpEntity<String> entity = new HttpEntity<String>(xml, headers);

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



}
