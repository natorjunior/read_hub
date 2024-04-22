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

    public String processo(String busca) throws Exception {
        // Carregar o documento PDF
        File file = new File(
                "/home/eddev/Documentos/Faculdade/read_hub/api_readhub/src/main/resources/diarios/caderno2-Judiciario.pdf");
        PDDocument doc = Loader.loadPDF(file);

        // Remove a primeira pagina do documento
        doc.removePage(0);

        // transforma o documento em texto
        PDFTextStripper stripper = new PDFTextStripper();
        String texto = stripper.getText(doc);

        // quebra o documentos e cria um array com todas as linhas;
        String[] textCut = texto.split("\n");

        List<Integer> indexBusca = new ArrayList<Integer>();
        List<Integer> indexInicio = new ArrayList<Integer>();
        List<Integer> indexFinal = new ArrayList<Integer>();

        // acha aonde ocorreu a aparição da busca.
        for (int i = 0; i < textCut.length; i++) {
            if (textCut[i].contains(busca)) {
                indexBusca.add(i);
            }
        }

        // Cria o regex de numero de processo
        String regex = "\\d\\d\\d\\d\\d\\d\\d-\\d\\d\\.\\d\\d\\d\\d\\.\\d\\.\\d\\d\\.\\d\\d\\d\\d";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        // Faz a busca para cima e para baixo do inicio e fim do processo.
        for (int i = 0; i < indexBusca.size(); i++) {
            for (int j = indexBusca.get(i); j >= 0; j--) {
                Matcher matcher = pattern.matcher(textCut[j]);
                if (matcher.find()) {
                    indexInicio.add(j);
                    break;
                }
            }
            for (int y = indexBusca.get(i) + 1; y < textCut.length; y++) {
                Matcher matcher = pattern.matcher(textCut[y]);
                if (matcher.find()) {
                    indexFinal.add(y - 1);
                    break;
                }
            }
        }

        // Cria uma lista dos processos
        List<String> citacao = new ArrayList<String>();

        // Cria o texto do processo e adiciona na lista de processos.
        for (int i = 0; i < indexBusca.size(); i++) {
            String textoCitacao = "";
            for (int j = indexInicio.get(i); j <= indexFinal.get(i); j++) {
                textoCitacao += textCut[j];
            }
            citacao.add(textoCitacao);
        }

        System.out.println(indexBusca.toString());
        System.out.println(indexInicio.toString());
        System.out.println(indexFinal.toString());
        System.out.println("___________________________________________________");

        return citacao.toString();
    }
}
