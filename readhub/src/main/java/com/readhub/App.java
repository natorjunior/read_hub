package com.readhub;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
/**
 * JavaFX App
 */
public class App extends Application {

    private Parent root;
    private Scene scene;

    @SuppressWarnings("exports")
    public Parent getRoot() {
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        try {
            primaryStage.setTitle("Read Hub");
            load("Home");
            // carrega o root na cena
            scene = new Scene(root);
                
            // cria a url para o css
            String css = this.getClass().getResource("Css/root.css").toExternalForm();
            
            // conecta o css na tela.
            scene.getStylesheets().add(css);
            
            // Seta a cena no state.
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            // tira da tela sem a opcao de alterar o tamanho.
            
            // Coloca o Tela como Visivel.
            primaryStage.show();
        } catch(Exception e) {
            System.out.println(e.getMessage()); 
        }
    }

    // faz o carregamento do fxml.
    public void load(String fxml) throws IOException {
        root = FXMLLoader.load(App.class.getResource("views/" + fxml + ".fxml"));
    }            
}