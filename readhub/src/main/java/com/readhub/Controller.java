package com.readhub;

import java.io.IOException;
import java.util.List;

import com.readhub.models.Processo;
import com.readhub.service.Gemini;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller {
    
    @FXML 
    TextField textoBusca;

    @FXML
    DatePicker dataBusca;

    @FXML
    FlowPane process;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Pane telaSys;
   
    private List<Processo> processos;
    int linha = 0;
    int coluna = 0;


    @FXML
    public void buscar(@SuppressWarnings("exports") ActionEvent e) throws Exception {
        Gemini busca = new Gemini("/home/eddev/Downloads/caderno2-Judiciario.pdf");
        String buscaString = textoBusca.getText();
        processos = busca.generateContent(buscaString);

        for(int i=0;i<processos.size();i++) {
            
            Processo novoProcesso = processos.get(i);
            Pane processo = createProcess(novoProcesso.getNumeroProcesso(), novoProcesso.getClasse(),novoProcesso.getNomes(), i);

            // cria o separador dos processos e separa por linhas e colunas.
            Separator separator = new Separator();
            separator.prefWidth(52);
            if(coluna != 0 && coluna != 3) {
                process.getChildren().add(separator);
            } else {
                Pane separatorV = new Pane();
                separatorV.setPrefHeight(47);
                separatorV.setPrefWidth(1268);
                process.getChildren().add(separatorV);
            }
    
            // logica das linhas e colunas
            if(coluna < 2) {
                coluna++;
            } else {
                coluna = 0;
                linha++;    
            }
    
            process.getChildren().add(processo);
        }

    }

    // cria o frame do processo.
    private Pane createProcess(String numeroProcesso, String classe, String nomes,int index) {
        Pane processo = new Pane();
        processo.setPrefWidth(331);
        processo.setPrefHeight(232);
        String paneCss = this.getClass().getResource("Css/Process.css").toExternalForm();
        processo.getStylesheets().add(paneCss);
        processo.setId("processo");
        Button mostraProcesso = createButton(index);
        Text textNumero = createText("Nº: " + numeroProcesso, 14, 38);
        Text textClasse;
        if(classe.length() >= 644) {
            textClasse = createText("Classe: " + classe.replace("\"", "").substring(0,644) + "\n", 0, 0);
        } else {
            textClasse = createText("Classe: " + classe.replace("\"", "") + "\n", 0, 0);
        }
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefSize(320, 150);
        textFlow.setMaxHeight(150);
        textFlow.setLayoutX(14);
        textFlow.setLayoutY(44);
        Text textNomes = createText("Nomes Relacionados: " + nomes, 0, 0);
        textFlow.getChildren().addAll(textClasse, textNomes);
        processo.getChildren().addAll(textNumero, textFlow, mostraProcesso);     
        return processo;
    }

    // cria botão para ver o processo.
    private Button createButton(int index) {
        Button mostraProcesso = new Button();
        mostraProcesso.setPrefSize(123, 30);
        mostraProcesso.setText("Ver Processo");
        mostraProcesso.setId("mostraProcesso");
        mostraProcesso.setLayoutX(201);
        mostraProcesso.setLayoutY(194);
        mostraProcesso.addEventHandler(ActionEvent.ACTION, event -> {
        try {
            mostraprocesso(new ActionEvent(), index);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        });
        return mostraProcesso;
    }

    private Button createButtonExit(int index) throws IOException {
        Button buttonExit = new Button();
        buttonExit.setPrefSize(123, 30);
        buttonExit.setText("Exit");
        buttonExit.setId("buttonExit");
        buttonExit.setLayoutX(560);
        buttonExit.setLayoutY(449);
        buttonExit.addEventHandler(ActionEvent.ACTION, event -> {
            exitProcesso(new ActionEvent(), index);
        });
        return buttonExit;
    }


    // cria os texto que vão aparecer no processo.
    private Text createText(String texto, int layX, int layY) {
        Text processo = new Text();
        processo.setText(texto);
        processo.setLayoutX(layX);
        processo.setLayoutY(layY);
        return processo;
    }

    // evento que vai mostrar os processos.
    
    Pane pane;
    public void mostraprocesso(@SuppressWarnings("exports") ActionEvent e, int index) throws IOException {
        telaSys.setVisible(true);
        telaSys.setDisable(false);
        String css = this.getClass().getResource("Css/telaProcess.css").toExternalForm();
        pane = new Pane();
        pane.setPrefWidth(700);
        pane.setPrefHeight(500);
        pane.setLayoutX(300);
        pane.setLayoutY(150);
        // Texto
        ScrollPane textProcess = new ScrollPane();
        textProcess.setPrefSize(700, 400);
        textProcess.setHmax(690);
        TextFlow textflow = new TextFlow();
        textflow.setId("textflow");
        Text texto = new Text();
        texto.setText(processos.get(index).getTexto());
        textflow.getChildren().add(texto);
        textflow.setPrefSize(670, 400);
        textProcess.setContent(textflow);
        // Buttão
        Button exit = createButtonExit(index);
        pane.setId("boxProcesso");
        pane.getStylesheets().add(css);
        pane.getChildren().addAll(textProcess, exit);
        telaSys.getChildren().add(pane);
    }

    @FXML
    Button exitProcesso;

    public void exitProcesso(@SuppressWarnings("exports") ActionEvent e, int index) {
        telaSys.setDisable(true);
        telaSys.setVisible(false);
    }
}
