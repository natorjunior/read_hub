package com.readhub.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.readhub.models.Processo;

public class Gemini extends Service {
    
    public Gemini(String path) {
      super(path);
      //TODO Auto-generated constructor stub
    }

    public List<Processo> generateContent(String busca) throws Exception {

        List<Processo> listaProcessos = new ArrayList<>();

        DotenvConf dotenv = new DotenvConf();
        // Substitua YOUR_API_KEY pela sua chave de API GCP
        String apiKey = dotenv.get("GEMINI_TOKEN");

        // Substitua https://vertex.ai/projects/{project_id}/locations/{location}/models/{model_id} pelo URL do seu modelo Gemini
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;

        int MAX_THREADS = 10;

        List<String> processos = this.processo(busca);
        List<String> numeroProcessos = this.numeroProcessos();


        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);

        for(int i=0;i<numeroProcessos.size();i++) {

          final int index = i;
          // Crie um objeto HttpURLConnection
          executor.submit(() -> {
            try {
              @SuppressWarnings("deprecation")
              URL obj = new URL(url);
              HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
    
              // Defina o método da solicitação para POST
              connection.setRequestMethod("POST");
    
              // Defina o cabeçalho de autorização para usar sua chave de API
              //connection.setRequestProperty("Authorization", "Bearer " + apiKey);
    
              // Defina o tipo de conteúdo para JSON
              connection.setRequestProperty("Content-Type", "application/json");
    
              // Defina o corpo da solicitação JSON
              String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \" Faça a leitura do trecho que foi tirado do Diario do TJCE e faça com 3 campos obrigatorios semparados pelo caracteres readhub sem nenhuma formatação no texto, primeiro sera com a classe do processo, segundo sera com todos os nomes relacionados ao processo se todos os nomes relacionados passarem de 250 caracteres coloque apenas os 250 caracteres e bote ..., terceiro vai ser o trecho do processo completo cortado. Numero do processo a ser cortado: " + numeroProcessos.get(index) + " Texto:" + processos.get(index) +  "\" }] }] }";
              connection.setDoOutput(true);
              connection.getOutputStream().write(requestBody.getBytes());
    
              // Envie a solicitação e obtenha a resposta
              Random random = new Random();
              int aleatorio = random.nextInt(30000);
              System.out.println(aleatorio);
              Thread.sleep(aleatorio);
              connection.connect();
              int responseCode = connection.getResponseCode();
    
              // Verifique se a solicitação foi bem-sucedida
              if (responseCode == HttpURLConnection.HTTP_OK) {
              // Leia a resposta JSON
                Processo novoProcesso = null;
                try {
                  String responseBody = new String(connection.getInputStream().readAllBytes());
                  String completa = responseBody.split("\"text\":")[1].split("}")[0];
                  String campos[] = completa.split("readhub");
                  novoProcesso = new Processo(numeroProcessos.get(index),campos[0],campos[1],campos[2]);
                } catch(Exception e) {
                  novoProcesso = new Processo(numeroProcessos.get(index),"","",processos.get(index));
                  System.out.println(e.getMessage());
                } finally {
                  synchronized (listaProcessos) { // Garante adição segura entre threads
                    listaProcessos.add(novoProcesso);
                  }
                }
              } else {
                System.out.println("Erro: " + responseCode);
                Processo novoProcesso = new Processo(numeroProcessos.get(index),"","",processos.get(index));
                synchronized (listaProcessos) { // Garante adição segura entre threads
                  listaProcessos.add(novoProcesso);
                }
              }
            } catch(Exception e) {

            }
          });

          System.out.println("OK");
      }

      executor.shutdown(); // Sinaliza que não há mais tarefas
      executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS); // Aguarda todas as tarefas terminarem

      return listaProcessos;
  }
}
