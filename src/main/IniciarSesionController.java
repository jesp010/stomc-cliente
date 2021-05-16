package main;

import comm.Conexion;
import dominio.Employee;
import dominio.Message;
import dominio.Turn;
import gui_elements.Toast;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class IniciarSesionController implements IController {

    AdministrarSistemaController administrarSistemaController;
    ClientFrontController clientFrontController;
    TurneroController turneroController;

    @FXML
    public ImageView imgLogo;
    @FXML
    public Label dateLabel;
    @FXML
    public Label timeLabel;
    @FXML
    public TextField txtUsuario;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public Button btnIniciarSesion;

    public void initialize(){
        System.out.println("IniciarSesionController did initialize");

        Conexion.getInstance().setController(this);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            timeLabel.setText(currentTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy")));

        addTextLimiter(txtUsuario, 250);
        addTextLimiter(txtPassword, 250);

        imgLogo.setImage(new Image(getClass().getClassLoader().getResourceAsStream(("logo.png"))));
        imgLogo.setCache(true);
    }

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    @FXML
    public void didClickIniciarSesionButton(){
        if (!camposVacios()) {
            System.out.println("Nuevo Iniciar Sesion Button Clicked");
            Message msg = new Message(Message.MessageType.LOGIN, "test_user");

            String[] userAndPassword = {txtUsuario.getText(), txtPassword.getText()};

            msg.setObject(userAndPassword);

            try {
                Conexion.getInstance().sendMessage(msg);
            } catch (IOException e) {
                System.out.println("Could not connect to server.");
            }
        } else {
            makeToast("Llenar los campos vacios.");
        }
    }

    public void makeToast(String mensaje) {
        Toast.makeText(null, mensaje, 3500,100,300);
    }

    private boolean camposVacios() {
        boolean respuesta = false;

        if (txtUsuario.getText().isEmpty()) {
            txtUsuario.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (txtPassword.getText().isEmpty()) {
            txtPassword.setStyle("-fx-border-color: red");
            respuesta = true;
        }

        return respuesta;
    }

    public void loadAdministrador(Employee empleado) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("administrarSistema.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root,  1060, 700);
        Stage appStage = (Stage) btnIniciarSesion.getScene().getWindow();
        appStage.setScene(scene);
        appStage.toFront();
        appStage.show();

        administrarSistemaController = loader.getController();
        administrarSistemaController.setEmpleado(empleado);

        appStage.setTitle("STOMC - Administración");
        appStage.setOnCloseRequest(windowEvent -> System.exit(0));
        appStage.show();
    }

    public void loadClientFront(Employee empleado) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clientFront.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root,  1060, 700);
        Stage appStage = (Stage) btnIniciarSesion.getScene().getWindow();
        appStage.setScene(scene);
        appStage.toFront();
        appStage.show();

        clientFrontController = loader.getController();
        clientFrontController.setEmpleado(empleado);

        appStage.setTitle("STOMC Client - Front");
        appStage.setOnCloseRequest(windowEvent -> System.exit(0));
        appStage.show();

        // Abre ventana del turnero
        FXMLLoader loaderTurnero = new FXMLLoader(getClass().getResource("turnero.fxml"));
        Pane ventanaDos = (Pane) loaderTurnero.load();
        Stage ventanaTurnero = new Stage();
        ventanaTurnero.setOnCloseRequest(windowEvent -> windowEvent.consume());
        ventanaTurnero.setTitle("TURNERO OOMAPASC");
        //ventanaTurnero.initOwner(appStage);
        Scene sceneTurnero = new Scene(ventanaDos);
        ventanaTurnero.setScene(sceneTurnero);
        turneroController = loaderTurnero.getController();

        clientFrontController.setTurneroController(turneroController);

        ventanaTurnero.show();

        letterbox(sceneTurnero, ventanaDos);
    }

    @Override
    public void handleMessage(Message message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                switch (message.getType()) {
                    case LOGIN:
                        if (message.getObject() != null) {
                            Employee empleado = (Employee) message.getObject();

                            if (empleado.getIdProfile().getProfileName().equalsIgnoreCase("Administrador")) {
                                try {
                                    loadAdministrador(empleado);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    makeToast("Error al cargar la pagina. Vuelva a intentarlo.");
                                }
                            } else {
                                try {
                                    loadClientFront(empleado);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    makeToast("Error al cargar la pagina. Vuelva a intentarlo.");
                                }
                            }

                        } else {
                            makeToast("Datos incorrectos. Vuelva intentarlo.");
                        }

                        break;
                }
            }
        });
    }

    private void letterbox(final Scene scene, final Pane contentPane) {
        final double initWidth  = scene.getWidth();
        final double initHeight = scene.getHeight();
        final double ratio      = initWidth / initHeight;

        SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth, contentPane);
        scene.widthProperty().addListener(sizeListener);
        scene.heightProperty().addListener(sizeListener);
    }

    private static class SceneSizeChangeListener implements ChangeListener<Number> {
        private final Scene scene;
        private final double ratio;
        private final double initHeight;
        private final double initWidth;
        private final Pane contentPane;

        public SceneSizeChangeListener(Scene scene, double ratio, double initHeight, double initWidth, Pane contentPane) {
            this.scene = scene;
            this.ratio = ratio;
            this.initHeight = initHeight;
            this.initWidth = initWidth;
            this.contentPane = contentPane;
        }

        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
            final double newWidth = scene.getWidth();
            final double newHeight = scene.getHeight();

            double scaleFactor =
                    newWidth / newHeight > ratio
                            ? newHeight / initHeight
                            : newWidth / initWidth;

            if (scaleFactor >= 1) {
                Scale scale = new Scale(scaleFactor, scaleFactor);
                scale.setPivotX(0);
                scale.setPivotY(0);
                scene.getRoot().getTransforms().setAll(scale);

                contentPane.setPrefWidth(newWidth / scaleFactor);
                contentPane.setPrefHeight(newHeight / scaleFactor);
            } else {
                contentPane.setPrefWidth(Math.max(initWidth, newWidth));
                contentPane.setPrefHeight(Math.max(initHeight, newHeight));
            }
        }
    }
}
