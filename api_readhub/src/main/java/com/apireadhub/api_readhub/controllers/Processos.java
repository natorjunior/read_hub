package com.apireadhub.api_readhub.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.apireadhub.api_readhub.service.Service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Anotaçõa para dizer que e um controler.
@RestController
public class Processos {

    @GetMapping("/")
    public String home() {
        return new String("API - ReadHub");
    }
    
    @GetMapping("/busca")
    public Object home(@RequestParam String busca, @RequestParam String data) throws Exception {
        
        if(busca.length() < 3) {
            return "So se pode pesquisar apatir de 3 caracteres";
        }
        
        Service buscaProcesso = new Service("/home/eddev/Documentos/read_hub/api_readhub/src/main/resources/diarios/caderno2-Judiciario.pdf");
        
        return buscaProcesso.processo(busca);
    }
    
    @GetMapping("/Linhas")
    public String[] getMethodName() throws Exception {
        Service buscaProcesso = new Service("/home/eddev/Documentos/read_hub/api_readhub/src/main/resources/diarios/caderno2-Judiciario.pdf");
        buscaProcesso.processo("brisa");
        return buscaProcesso.getLinhas();
    }
    
}
