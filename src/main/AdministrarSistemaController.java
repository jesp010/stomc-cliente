package main;

import comm.Conexion;
import dominio.Employee;
import dominio.Message;
import gui_elements.Toast;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AdministrarSistemaController implements IController {

    private IniciarSesionController iniciarSesionController;

    private Employee empleado;

    @FXML
    public Button btnCerrarSesion;
    @FXML
    public Label txtEmpleado;

    @FXML
    public Label dateLabel;
    @FXML
    public Label timeLabel;
    @FXML
    public BorderPane panContenido;
    @FXML
    ImageView imgLogo;

    @FXML
    public Button btnEmpleados;
    @FXML
    public Button btnPuntosDeAtencion;
    @FXML
    public Button btnSucursales;
    @FXML
    public Button btnTurnos;
    @FXML
    public Button btnTramites;

    public void setEmpleado(Employee empleado) {
        this.empleado = empleado;

        if (empleado != null) {
            txtEmpleado.setText(empleado.getName());
        }
    }

    public Employee getEmpleado() {
        return empleado;
    }

    public void initialize(){
        System.out.println("AdministrarSistemaController did initialize");

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            timeLabel.setText(currentTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy")));

        imgLogo.setImage(new Image(getClass().getClassLoader().getResourceAsStream(("logo.png"))));
        imgLogo.setCache(true);

        loadContent("menuEmpleados.fxml");
    }

    @FXML
    public void didClickEmpleadosButton() {
        loadContent("menuEmpleados.fxml");
    }

    @FXML
    public void didClickPuntosDeAtencionButton() {
        loadContent("menuPuntosDeAtencion.fxml");
    }

    @FXML
    public void didClickSucursalesButton() { loadContent("menuSucursales.fxml"); }

    @FXML
    public void didClickTurnosButton() {
        loadContent("menuTurnos.fxml");
    }

    @FXML
    public void didClickTramitesButton() {
        loadContent("menuInformacionTramites.fxml");
    }

    @FXML
    public void didClickCerrarSesionButton(ActionEvent actionEvent) {

        try {
            setEmpleado(null);

            try {
                Conexion.getInstance().close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("inicioSesion.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1060, 700);
            Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.toFront();
            appStage.show();

            iniciarSesionController = loader.getController();

            appStage.setTitle("STOMC Client");
            appStage.setOnCloseRequest(windowEvent -> System.exit(0));
            appStage.show();
        } catch(IOException e) {
            makeToast("Error al cerrar Sesión.");
        }
    }

    public void loadContent(String recurso) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(recurso));
            Parent root = loader.load();
            panContenido.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeToast(String mensaje) {
        Toast.makeText(null, mensaje, 3500,100,300);
    }

    @Override
    public void handleMessage(Message message) {

    }


}
