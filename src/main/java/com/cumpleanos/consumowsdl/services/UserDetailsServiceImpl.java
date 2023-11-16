/*
 * Copyright (c) 2023. Luis Chumi
 * Este programa es software libre: usted puede redistribuirlo y/o modificarlo bajo los términos de la Licencia Pública General GNU
 */

package com.cumpleanos.consumowsdl.services;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario=getById(username);

        if (usuario == null){
            throw new UsernameNotFoundException(username);
        }
        return User
                .withUsername(username)
                .password(usuario.password())
                .roles(usuario.roles().toArray(new String[0]))
                .build();
    }

    public record Usuario(String username,String password , Set<String> roles){};

    public static Usuario getById(String username){
        var password= "$2y$10$eRu9W1n22pIBesJWno7XEOsMppBDAgurVBJ2qa8yyb5Wtk7zJQdv2";
        Usuario Admin= new Usuario(
                "Admin",
                password, Set.of("ADMIN")
        );
        Usuario User=new Usuario(
                "User",
                password,
                Set.of("USER")
        );
        var usuarios= List.of(Admin,User);

        return usuarios
                .stream()
                .filter(e -> e.username.equals(username))
                .findFirst()
                .orElse(null);
    }
}
