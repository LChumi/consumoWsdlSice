/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.controller;

import com.cumpleanos.consumowsdl.models.auth.AuthenticationReq;
import com.cumpleanos.consumowsdl.models.auth.TokenInfo;
import com.cumpleanos.consumowsdl.security.JwtUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final static Logger LOG= LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;


    @PostMapping("/login/")
    public ResponseEntity<TokenInfo> login(@RequestBody AuthenticationReq usuario){
        LOG.info("Autenticando al usuario "+usuario.getUsuario());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getUsuario(),
                        usuario.getClave()));

        final UserDetails userDetails=userDetailsService.loadUserByUsername(usuario.getUsuario());

        final String jwt = jwtUtilService.generateToken(userDetails);

        return ResponseEntity.ok(new TokenInfo(jwt));
    }


}
