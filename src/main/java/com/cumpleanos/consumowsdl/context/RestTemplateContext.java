package com.cumpleanos.consumowsdl.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateContext {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Crear una lista de convertidores de mensaje
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        // Convertidor para cadenas en UTF-8
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        messageConverters.add(stringConverter);

        // Convertidor para JSON
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(new ObjectMapper());
        messageConverters.add(jsonConverter);

        // Configurar los convertidores en RestTemplate
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }


}
