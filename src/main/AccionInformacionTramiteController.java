package main;

import comm.Conexion;
import dominio.LysingInformation;
import dominio.Message;
import gui_elements.Toast;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class AccionInformacionTramiteController implements IController {
    private String accion;
    private LysingInformation lysingInformation;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Label lblInformacionTramiteAccion;

    @FXML
    public TextField txtTitulo;
    @FXML
    public TextArea txtProceso;

    @FXML
    public Button btnAceptar;
    @FXML
    public Button btnCancelar;

    public void setAccion(String accion) {
        this.accion = accion;

        switch (accion) {
            case "Añadir":
                lblInformacionTramiteAccion.setText("Añadir Información de Trámite");
                btnAceptar.setText("Añadir");
                break;
            case "Editar":
                lblInformacionTramiteAccion.setText("Editar Información de Trámite");
                btnAceptar.setText("Editar");

                txtTitulo.setText(lysingInformation.getTitle());
                txtProceso.setText(lysingInformation.getProcess());

                break;
        }
    }

    public void setInformacionTramite(LysingInformation lysingInformation) {
        this.lysingInformation = lysingInformation;
    }

    public void initialize(){
        System.out.println("AccionInformacionTramiteController did initialize");

        Conexion.getInstance().setController(this);
    }

    @FXML
    public void didClickAceptarButton(){
        if (!camposVacios()) {
            switch (accion) {
                case "Añadir":
                    addLysingInformation();
                    break;
                case "Editar":
                    editLysingInformation();
                    break;
            }
        } else {
            makeToast("Llenar los campos vacios");
        }
    }

    private void editLysingInformation() {
        LysingInformation lysingInformation = this.lysingInformation;

        lysingInformation.setTitle(txtTitulo.getText());
        lysingInformation.setProcess(txtProceso.getText());

        Message msg = new Message(Message.MessageType.EDIT_LYSING_INFORMATION, "test_user");
        msg.setObject(lysingInformation);

        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addLysingInformation() {
        LysingInformation lysingInformation = new LysingInformation();

        lysingInformation.setTitle(txtTitulo.getText());
        lysingInformation.setProcess(txtProceso.getText());

        Message msg = new Message(Message.MessageType.ADD_LYSING_INFORMATION, "test_user");
        msg.setObject(lysingInformation);

        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void didClickCancelarButton() {
        cargarMenuInformacionTramites();
    }

    private void cargarMenuInformacionTramites() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuInformacionTramites.fxml"));
            Parent root = loader.load();

            rootPane.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetField(MouseEvent event) {
        ( (Node) event.getSource() ).setStyle("");
    }

    public void makeToast(String mensaje) {
        Toast.makeText(null, mensaje, 3500,100,300);
    }

    private boolean camposVacios() {
        boolean respuesta = false;

        if (txtProceso.getText().isEmpty()) {
            txtProceso.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (txtTitulo.getText().isEmpty()) {
            txtTitulo.setStyle("-fx-border-color: red");
            respuesta = true;
        }

        return respuesta;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case ADD_LYSING_INFORMATION:
                ObservableList<LysingInformation> lista1 = FXCollections.observableList( (List<LysingInformation>) message.getObject() );
                if (lista1.isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("Ya existe un trámite con los mismos datos");
                            txtProceso.setStyle("-fx-border-color: red");
                        }
                    });

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("El trámite ha sido creado correctamente");
                            cargarMenuInformacionTramites();
                        }
                    });
                }
                break;

            case EDIT_LYSING_INFORMATION:
                ObservableList<LysingInformation> lista2 = FXCollections.observableList( (List<LysingInformation>) message.getObject() );
                if (lista2.isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("Ya existe un trámite con los mismos datos");
                            txtProceso.setStyle("-fx-border-color: red");
                        }
                    });

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("El trámite ha sido editado correctamente");
                            cargarMenuInformacionTramites();
                        }
                    });
                }
                break;
        }
    }
}
