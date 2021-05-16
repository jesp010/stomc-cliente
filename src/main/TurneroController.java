package main;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TurneroController {

    private static String separador = "\\s*-\\s*";

    @FXML
    public Label dateLabel;
    @FXML
    public Label timeLabel;
    @FXML
    public ImageView imgLogo;

    @FXML
    public Label lblCajaTurno;
    @FXML
    public Label lblModuloTurno;
    @FXML
    public Label lblCajaModuloTurno;

    @FXML
    public Label lblCaja;
    @FXML
    public Label lblModulo;
    @FXML
    public Label lblCajaModulo;

    public void initialize(){
        System.out.println("TurneroController did initialize");

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
    }

    public void siguienteTurnoCaja(int turno, String puntoDeAtencion) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblCajaTurno.setText("C" + turno);
                lblCaja.setText(puntoDeAtencion.split(separador)[1]);
            }
        });
    }

    public void siguienteTurnoModulo(int turno, String puntoDeAtencion) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblModuloTurno.setText("M" + turno);
                lblModulo.setText(puntoDeAtencion.split(separador)[1]);
            }
        });
    }

    public void siguienteTurnoCajaModulo(String tipo, int turno, String puntoDeAtencion) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lblCajaModuloTurno.setText(tipo + turno);
                lblCajaModulo.setText(puntoDeAtencion.split(separador)[1]);
            }
        });
    }
}
