module com.readhub {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.apache.pdfbox;
    requires bcrypt;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens com.readhub to javafx.fxml;
    exports com.readhub;
}


