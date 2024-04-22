package com.api_readhub.readhub.Controllers;

import org.springframework.web.bind.annotation.RestController;

// Anotação para dizer que a classe e responsavel pelo controle.
@RestController
public class Processo {
    
    public String processos() {
        return "Hello World";
    }

}
