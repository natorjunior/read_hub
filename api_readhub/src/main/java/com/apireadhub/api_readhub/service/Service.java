package com.apireadhub.api_readhub.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Service {

    // constructor
    public Service(String path) {
        setPath(path);
    }

    // Getters e Setters de path.
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // Getters e Setters de busca.
    private String busca;

    public String getBusca() {
        return busca;
    }

    public void setBusca(String busca) {
        this.busca = busca;
    }

    // Getters e Setters de linhas.
    private String[] linhas;

    public String[] getLinhas() {
        return linhas;
    }

    public void setLinhas(String[] linhas) {
        this.linhas = linhas;
    }

    // Carregar o documento PDF
    private PDDocument createDocument() throws Exception {
        File file = new File(this.path);
        PDDocument doc = Loader.loadPDF(file);

        // Remove a primeira pagina do documento
        doc.removePage(0);

        return doc;
    }

    // transforma o documento em texto e particiona em linhas em um array de Strings
    private void transformText() throws Exception {
        PDFTextStripper stripper = new PDFTextStripper();
        String texto = stripper.getText(this.createDocument());
        String[] linhas = texto.split("\n");
        setLinhas(linhas);
    }

    // acha aonde ocorreu a aparição da busca.
    private List<Integer> indexBusca() {
        List<Integer> indexBusca = new ArrayList<Integer>();
        for (int i = 0; i < linhas.length; i++) {
            if (linhas[i].contains(this.busca)) {
                indexBusca.add(i);
            }
        }
        return indexBusca;
    }

    private Pattern regex() {
        // Cria o regex de numero de processo
        String regex = "\\d\\d\\d\\d\\d\\d\\d-\\d\\d\\.\\d\\d\\d\\d\\.\\d\\.\\d\\d\\.\\d\\d\\d\\d";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        return pattern;
    }

    // Faz a busca para cima dos indices de inicio do processo.
    private List<Integer> indexInicio(List<Integer> indexBusca) {
        List<Integer> indexInicio = new ArrayList<Integer>();

        int indexAntX = 0;
        for (int i = 0; i < indexBusca.size(); i++) {
            // Busca o inicio
            for (int x = indexBusca.get(i); x >= 0; x--) {
                Matcher matcher = this.regex().matcher(linhas[x]);
                if (matcher.find() && indexAntX != x) {
                    indexInicio.add(x);
                    indexAntX = x;
                    break;
                }
            }
        }

        return indexInicio;
    }

    // Faz a busca para baixo dos indices de fim do processo.
    private List<Integer> indexFinal(List<Integer> indexBusca) {
        List<Integer> indexFinal = new ArrayList<Integer>();

        int indexAntY = 0;
        for (int i = 0; i < indexBusca.size(); i++) {
            // Busca o final
            for (int y = indexBusca.get(i) + 1; y < linhas.length; y++) {
                Matcher matcher = this.regex().matcher(linhas[y]);
                if (matcher.find() && indexAntY != y) {
                    indexFinal.add(y - 1);
                    indexAntY = y;
                    break;
                }
            }
        }

        return indexFinal;
    }

    private List<String> listarProcesos(List<Integer> indexInicio, List<Integer> indexFinal) {

        // Cria uma lista dos processos
        List<String> processos = new ArrayList<String>();

        // Cria o texto do processo e adiciona na lista de processos.
        for (int i = 0; i < indexInicio.size(); i++) {
            if (indexInicio.get(i) != -1 || indexFinal.get(i) != -1) {
                String textoCitacao = "";
                for (int j = indexInicio.get(i); j <= indexFinal.get(i); j++) {
                    textoCitacao += linhas[j];
                }
                processos.add(textoCitacao);
            }
        }

        return processos;

    }

    public List<String> processo(String busca) throws Exception {

        setBusca(busca);
        transformText();
        List<Integer> indexBusca = indexBusca();
        List<Integer> indexInicio = indexInicio(indexBusca);
        List<Integer> indexFinal = indexFinal(indexBusca);

        return listarProcesos(indexInicio, indexFinal);
    }
}
