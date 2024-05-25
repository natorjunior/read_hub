package com.apireadhub.api_readhub.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Download {

    // ATRIBUTOS
    private String date;

    // GETTERS E SETTERS
    public String getDate() {
        return date;
    }

    public boolean setDate(String date) {
        // Cria o regex de numero de processo
        try {

            String regex = "\\d\\d/\\d\\d/\\d\\d\\d\\d";

            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(date);

            if (matcher.find()) {
                this.date = date;
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getCause());
        }
        return false;
    }
    
    // METODOS PRINCIPAIS
    public void downloadDocument(String data) throws MalformedURLException {

        // valida se a data e valida.
        if(!setDate(data)) return;
        
        // nome de pasta e diretorio.
        String nomeDoArquivo = "diario_tjce_" + data.replace("/", "") + ".pdf";

        String pastaDoArquivo = "/home/eddev/Documentos/read_hub/api_readhub/src/main/resources/diarios/";

        File arquivoLocal = new File(pastaDoArquivo, nomeDoArquivo);

        if (!arquivoLocal.getParentFile().exists()) {
            // Cria a pasta de destino se não existir
            arquivoLocal.getParentFile().mkdirs();
        }

        // Cria o objeto URL.
        String url = "https://esaj.tjce.jus.br/cdje/downloadCaderno.do?dtDiario=" + data
                + "&cdCaderno=2&tpDownload=D";
        System.out.println(url);
        @SuppressWarnings("deprecation")
        URL arquivoURL = new URL(url);

        // Faz o download do Arquivo e grava no File criado arquivoLocal.
        try {
            System.out.println("opening connection");
            InputStream in = arquivoURL.openStream();
            FileOutputStream fos = new FileOutputStream(arquivoLocal);

            System.out.println("reading from resource and writing to file...");
            int length = -1;
            byte[] buffer = new byte[1024];// buffer for portion of data from connection
            System.out.println(in.read(buffer));
            while ((length = in.read(buffer)) > -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            in.close();
            System.out.println("File downloaded");
        } catch (MalformedURLException e) {
            System.out.println("Erro ao criar URL. Formato inválido.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
