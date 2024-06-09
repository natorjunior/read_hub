package com.readhub.models;

public class Processo {

    private String numeroProcesso;
    private String classe;
    private String nomes;
    private String texto;
    
    public String getNumeroProcesso() {
        return numeroProcesso;
    }
    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }
    public String getClasse() {
        return classe;
    }
    public void setClasse(String classe) {
        this.classe = classe;
    }
    public String getNomes() {
        return nomes;
    }
    public void setNomes(String nomes) {
        this.nomes = nomes;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }

    //constructor
    public Processo(String numeroProcesso, String classe, String nomes, String texto) {
        setNumeroProcesso(numeroProcesso);
        setClasse(classe);
        setNomes(nomes);
        setTexto(texto);
    }

}
