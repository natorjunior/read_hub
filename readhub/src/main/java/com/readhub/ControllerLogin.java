package com.readhub;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.readhub.service.DotenvConf;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class ControllerLogin {

    DotenvConf dotenv = new DotenvConf();
    private String url = dotenv.get("DATABASE_URL");
    private String root = dotenv.get("DATABASE_ROOT");
    private String password_data = dotenv.get("DATABASE_PASSWORD");

    private Stage stage;
    private Scene scene;
    private App app = new App();

    @FXML
    TextField emailTextField;
    
    @FXML
    TextField passwordTextField;

    @FXML
    Text passwordInvalid;

    @FXML
    Text emailInvalid;

    @FXML
    public void login(@SuppressWarnings("exports") ActionEvent e) throws IOException, SQLException {

        emailInvalid.setVisible(false);
        passwordInvalid.setVisible(false);
        passwordInvalid.setText("Senha invalida.");
        
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if(password.length() < 6 || password.length() > 50) {
            passwordInvalid.setText("Senha deve está entra 6 e 50 caracteres.");
            passwordInvalid.setVisible(true);
            return;
        }

        boolean result = entrar(email, password);

        if (!result) return;

        // coloca o outro arquivo no root.
        app.load("Sys");
        // carrega o arquivo root no stage novamente.
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setResizable(false);
        scene = new Scene(app.getRoot());
        stage.setScene(scene);
        stage.show();
    }

    private boolean entrar(String email, String password) throws SQLException {
        Connection conn = DriverManager.getConnection(url,root,password_data);
        try {
            String query = "SELECT * FROM USUARIOS WHERE email = \"" + email + "\";";
            ResultSet rs = conn.createStatement().executeQuery(query);
            rs.next();
            String emailBase = rs.getString("email");
            String passwordBase = rs.getString("password_hash");
            BCrypt.Result resultado = BCrypt.verifyer().verify(password.toCharArray(), passwordBase);
            if(!resultado.verified) {
                passwordInvalid.setVisible(true);
            }
            System.out.println("conectado com sucesso");
            return resultado.verified;
        } catch(Exception e) {
            emailInvalid.setVisible(true);
            System.out.println("Deu ruim");
        } finally {
            conn.close();
        }
        return false;
    }

    public void criarConta() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o email: ");
        String email = sc.nextLine();
        System.out.println("Digite sua senha: ");
        String password = sc.nextLine();
        sc.close();
        String passwordHash =  BCrypt.withDefaults().hashToString(12, password.toCharArray());
        System.out.println(passwordHash);
        Connection conn = DriverManager.getConnection(url,root,password_data);
        try {
            
            String query = "INSERT INTO USUARIOS (email, password_hash) VALUES (?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, passwordHash);
            pstmt.executeUpdate();
            
            System.out.println("Conta criada com sucesso.");
        } catch(Exception e) {
            System.out.println("Deu ruim");
        } finally {
            conn.close();
        }
    }
}