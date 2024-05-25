package com.apireadhub.api_readhub.models;

import java.util.List;

@SuppressWarnings("unused")
public class Diario {
    
    private String diario_date;
    private String[] linhas;
    private List<Integer> indexBusca;

    public void diario(String diario_date, String[] linhas, List<Integer> indexBusca) {
        this.diario_date = diario_date;
        this.linhas = linhas;
        this.indexBusca = indexBusca;
    }
}
