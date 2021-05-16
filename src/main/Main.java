package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    static IniciarSesionController iniciarSesionController;
    static ClientFrontController clientFrontController;
    static AdministrarSistemaController administrarSistemaController;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("inicioSesion.fxml"));
        Parent root = loader.load();

        iniciarSesionController = loader.getController();
        primaryStage.setTitle("STOMC Client - Inicio Sesión");
        primaryStage.setScene(new Scene(root, 1060, 700));
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("logo.png")));
        primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
        primaryStage.show();

        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("clientFront.fxml"));
        Parent root = loader.load();

        clientFrontController = loader.getController();
        primaryStage.setTitle("STOMC Client - Front");
        primaryStage.setScene(new Scene(root, 1060, 700));
        primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
        primaryStage.show();*/


        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("administrarSistema.fxml"));
        Parent root = loader.load();

        administrarSistemaController = loader.getController();
        primaryStage.setTitle("STOMC - Administración");
        primaryStage.setScene(new Scene(root, 1060, 700));
        primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
        primaryStage.show();*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
