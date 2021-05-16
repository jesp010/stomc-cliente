package main;

import comm.Conexion;
import dominio.CatalogueBranch;
import dominio.Message;
import gui_elements.Toast;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class AccionSucursalController implements IController {
    private String accion;
    private CatalogueBranch branch;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Label lblSucursalAccion;

    @FXML
    public TextField txtNombre;
    @FXML
    public TextArea txtDireccion;

    @FXML
    public Button btnAceptar;
    @FXML
    public Button btnCancelar;

    public void setAccion(String accion) {
        this.accion = accion;

        switch (accion) {
            case "Añadir":
                lblSucursalAccion.setText("Añadir Sucursal");
                btnAceptar.setText("Añadir");
                break;
            case "Editar":
                lblSucursalAccion.setText("Editar Sucursal");
                btnAceptar.setText("Editar");

                txtNombre.setText(branch.getBranchName());
                txtDireccion.setText(branch.getAddress());

                break;
        }
    }

    public void setSucursal(CatalogueBranch branch) {
        this.branch = branch;
    }

    public void initialize(){
        System.out.println("AccionSucursalController did initialize");

        Conexion.getInstance().setController(this);

        addTextLimiter(txtNombre, 250);
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
    public void didClickAceptarButton(){
        if (!camposVacios()) {
            switch (accion) {
                case "Añadir":
                    addBranch();
                    break;
                case "Editar":
                    editBranch();
                    break;
            }
        } else {
            makeToast("Llenar los campos vacios");
        }
    }

    private void editBranch() {
        CatalogueBranch catalogueBranch = this.branch;

        catalogueBranch.setBranchName(txtNombre.getText());
        catalogueBranch.setAddress(txtDireccion.getText());

        Message msg = new Message(Message.MessageType.EDIT_CATALOGUE_BRANCH, "test_user");
        msg.setObject(catalogueBranch);

        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBranch() {
        CatalogueBranch catalogueBranch = new CatalogueBranch();

        catalogueBranch.setBranchName(txtNombre.getText());
        catalogueBranch.setAddress(txtDireccion.getText());

        Message msg = new Message(Message.MessageType.ADD_CATALOGUE_BRANCH, "test_user");
        msg.setObject(catalogueBranch);

        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void didClickCancelarButton() {
        cargarMenuSucursal();
    }

    private void cargarMenuSucursal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuSucursales.fxml"));
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

        if (txtNombre.getText().isEmpty()) {
            txtNombre.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (txtDireccion.getText().isEmpty()) {
            txtDireccion.setStyle("-fx-border-color: red");
            respuesta = true;
        }

        return respuesta;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case ADD_CATALOGUE_BRANCH:
                ObservableList<CatalogueBranch> lista1 = FXCollections.observableList( (List<CatalogueBranch>) message.getObject() );
                if (lista1.isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("Ya existe una sucursal con el mismo nombre");
                            txtNombre.setStyle("-fx-border-color: red");
                        }
                    });

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("La sucursal ha sido creado correctamente");
                            cargarMenuSucursal();
                        }
                    });
                }
                break;

            case EDIT_CATALOGUE_BRANCH:
                ObservableList<CatalogueBranch> lista2 = FXCollections.observableList( (List<CatalogueBranch>) message.getObject() );
                if (lista2.isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("Ya existe una sucursal con el mismo nombre");
                            txtNombre.setStyle("-fx-border-color: red");
                        }
                    });

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("La sucursal ha sido creado correctamente");
                            cargarMenuSucursal();
                        }
                    });
                }
                break;
        }
    }
}
