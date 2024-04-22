package com.apireadhub.api_readhub.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.apireadhub.api_readhub.service.Service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


// Anotaçõa para dizer que e um controler.
@RestController
public class Processos {
    
    @GetMapping("/busca/{nome}")
    public String home(@PathVariable String nome) throws Exception {
        Service busca = new Service();
        return busca.processo(nome);    
    }
    
    

}
