package main;

import comm.Conexion;
import dominio.Employee;
import dominio.LysingInformation;
import dominio.Message;
import dominio.Turn;
import gui_elements.Toast;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.Server;
import server.TurnManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClientFrontController implements IController {

    static Server server = new Server();

    private TurneroController turneroController;

    private Turn turn;

    private Employee empleado;

    private TurnManager turnManager = new TurnManager();

    @FXML
    public Button btnCerrarSesion;
    @FXML
    public Label txtEmpleado;

    @FXML
    public Label dateLabel;
    @FXML
    public Label timeLabel;

    @FXML
    public Button newTurnCajaButton;
    @FXML
    public Button newTurnModuloButton;
    @FXML
    public Button preguntasFrecuentesButton;

    @FXML
    public Label txtTurnoCreado;
    @FXML
    public Label txtTipoCreado;
    @FXML
    public Label txtFechaCreado;
    @FXML
    public Label txtTiempoCreado;
    @FXML
    public Button imprimirTurnoButton;
    @FXML
    public AnchorPane panTicket;
    @FXML
    public ImageView imgLogo;
    @FXML
    public ImageView imgLogoVentana;
    @FXML
    public Label lblTurnosAdelante;

    @FXML
    public Label txtTurnosTotalCaja;
    @FXML
    public Label txtTurnoActualCaja;
    @FXML
    public Label txtTurnosTotalModulo;
    @FXML
    public Label txtTurnoActualModulo;

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public void setEmpleado(Employee empleado) {
        this.empleado = empleado;

        if (empleado != null) {
            txtEmpleado.setText(empleado.getName());
        }
    }

    public TurneroController getTurneroController() {
        return turneroController;
    }

    public void setTurneroController(TurneroController turneroController) {
        this.turneroController = turneroController;
        TurnManager.setTurneroController(turneroController);
        TurnManager.setClientFrontController(this);
    }

    public Employee getEmpleado() {
        return empleado;
    }

    public void initialize() {
        System.out.println("HomeController did initialize");

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

        imgLogo.setImage(new Image(getClass().getClassLoader().getResourceAsStream(("logo.png"))));
        imgLogo.setCache(true);

        imgLogoVentana.setImage(new Image(getClass().getClassLoader().getResourceAsStream(("logo.png"))));
        imgLogoVentana.setCache(true);

        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    @FXML
    public void didClickNewTurnCajaButton() {
        System.out.println("Nuevo Turno Caja Button Clicked");
        Message msg = new Message(Message.MessageType.NEW_TURN_CAJA, "test_user");
        msg.setObject(empleado);
        msg = TurnManager.createNewCajaTurn(msg);

        setTurn((Turn) msg.getObject());
        updateCreatedTurnLabelWithText();
        makeToast("Turno " + ((Turn) msg.getObject()).getType().toString() + " creado exitosamente.");
        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            System.out.println("Could not connect to server.");
            makeToast("EL TURNO " + Turn.Type.CAJA.toString() + " NO HA SIDO CREADO.");
        }
    }

    @FXML
    public void didClickNewTurnModuloButton() {
        System.out.println("Nuevo Turno Modulo Button Clicked");
        Message msg = new Message(Message.MessageType.NEW_TURN_MODULO, "test_user");
        msg.setObject(empleado);
        msg = TurnManager.createNewModuloTurn(msg);

        setTurn((Turn) msg.getObject());
        updateCreatedTurnLabelWithText();
        makeToast("Turno " + ((Turn) msg.getObject()).getType().toString() + " creado exitosamente.");
        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            System.out.println("Could not connect to server.");
            makeToast("EL TURNO " + Turn.Type.MODULO.toString() + " NO HA SIDO CREADO.");
        }
    }

    /*@FXML
    public void didClickNewTurnCajaModuloButton(){
        System.out.println("Nuevo Turno Caja/Modulo Button Clicked");
        Message msg = new Message(Message.MessageType.NEW_TURN_GENERIC, "test_user");
        msg = turnManager.createNewGenericTurn(msg);

        setTurn( (Turn)msg.getObject() );
        updateCreatedTurnLabelWithText();
        makeToast("Turno " + ( (Turn)msg.getObject() ).getType().toString() + " creado exitosamente.");
        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            System.out.println("Could not connect to server.");
            makeToast("EL TURNO " + Turn.Type.GENERIC.toString() + " NO HA SIDO CREADO.");
        }
    }*/

    @FXML
    public void didClickImprimirButton(ActionEvent actionEvent) {

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            //PageLayout pageLayout = printerJob.getPrinter().createPageLayout(Paper.A5, PageOrientation.PORTRAIT, 0, 0, 0, 0);

            WritableImage view = panTicket.snapshot(null, null);
            ImageView ticket = new ImageView(view);

            final PageLayout pageLayout = printerJob.getJobSettings().getPageLayout();
            final double scaleX = pageLayout.getPrintableWidth() /*216*/ / ticket.getImage().getWidth();
            final double scaleY = pageLayout.getPrintableHeight() /*216*/ / ticket.getImage().getHeight();
            final double scale = Math.min(scaleX, scaleY);
            // scale the calendar image only when it's too big for the selected page
            if (scale < 1.0) {
                ticket.getTransforms().add(new Scale(scale, scale));
            }

            boolean success = printerJob.printPage(ticket);
            if (success) {
                printerJob.endJob();
            }
        }
    }

    public void makeToast(String mensaje) {
        Toast.makeText(null, mensaje, 3500, 100, 300);
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case NEW_TURN_CAJA:
            case NEW_TURN_MODULO:
//            case NEW_TURN_GENERIC:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        setTurn( (Turn)message.getObject() );
//                        updateCreatedTurnLabelWithText();
//                        makeToast("Turno " + ( (Turn)message.getObject() ).getType().toString() + " creado exitosamente.");
                    }
                });
                break;

            case GET_MANY_LYSING_INFORMATION:

                List<LysingInformation> lista = (List<LysingInformation>) message.getObject();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FXMLLoader loaderTurnero = new FXMLLoader(getClass().getResource("requisitos.fxml"));
                            Pane ventanaDos = null;

                            ventanaDos = (Pane) loaderTurnero.load();

                            Stage ventanaTurnero = new Stage();
                            ventanaTurnero.setTitle("Requisitos de tramites");

                            Scene sceneTurnero = new Scene(ventanaDos);
                            ventanaTurnero.setScene(sceneTurnero);

                            RequisitosController requisitosController = loaderTurnero.getController();
                            requisitosController.setLysingInformations(lista);

                            ventanaTurnero.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }

    public void updateTurns() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtTurnosTotalCaja.setText(TurnManager.getCajaTurnNumberFloor() + "");
                txtTurnoActualCaja.setText(TurnManager.getCurrentCajaTurnNumber() + "");
                txtTurnosTotalModulo.setText(TurnManager.getModuloTurnNumberFloor() + "");
                txtTurnoActualModulo.setText(TurnManager.getCurrentModuloTurnNumber() + "");
            }
        });
    }

    public void updateCreatedTurnLabelWithText() {
        String letra = "";
        String tipo = "";
        switch (getTurn().getType()) {

            case CAJA:
                letra = "C";
                tipo = "CAJAS";
                lblTurnosAdelante.setText("Turnos adelante: " + TurnManager.getCajaTurnList().size());
                break;
            case MODULO:
                letra = "M";
                tipo = "MODULOS";
                lblTurnosAdelante.setText("Turnos adelante: " + TurnManager.getModuloTurnList().size());
                break;
            /*case GENERIC:
                letra = "G";
                tipo = "CAJA/MODULO";
                lblTurnosAdelante.setText("Turnos adelante: " + turnManager.getGenericTurnList().size());
                break;*/
        }

        txtTurnoCreado.setText(letra + getTurn().getTurnNumber());
        txtTipoCreado.setText(tipo);
        txtFechaCreado.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'del' yyyy")));
        txtTiempoCreado.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
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

            appStage.setTitle("STOMC Client");
            appStage.setOnCloseRequest(windowEvent -> System.exit(0));
            appStage.show();
        } catch (IOException e) {
            makeToast("Error al cerrar Sesión.");
        }
    }

    public void didClickRequisitosButton(ActionEvent actionEvent) {
        try {
            Conexion.getInstance().sendMessage(new Message(Message.MessageType.GET_MANY_LYSING_INFORMATION, "test_user"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
