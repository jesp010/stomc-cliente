package main;

import comm.Conexion;
import dominio.*;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccionEmpleadoController implements IController {

    private String accion;
    private Employee employee;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Label lblEmpleadoAccion;

    @FXML
    public TextField txtNoEmpleado;
    @FXML
    public TextField txtNombreEmpleado;
    @FXML
    public TextField txtDireccionEmpleado;
    @FXML
    public TextField txtDepartamentoEmpleado;
    @FXML
    public ComboBox<CatalogueBranch> cboxSucursal;
    @FXML
    public ComboBox<CatalogueAttentionPoint> cboxPuntoDeAtencion;
    @FXML
    public TextField txtUsuarioEmpleado;
    @FXML
    public TextField txtPasswordEmpleado;
    @FXML
    public ComboBox<CatalogueProfile> cboxPerfil;

    @FXML
    public Button btnAceptar;
    @FXML
    public Button btnCancelar;

    public void setAccion(String accion) {
        this.accion = accion;

        switch (accion) {
            case "Añadir":
                lblEmpleadoAccion.setText("Añadir empleado");
                btnAceptar.setText("Añadir");
                break;
            case "Editar":
                lblEmpleadoAccion.setText("Editar empleado");
                btnAceptar.setText("Editar");
                txtNoEmpleado.setText(employee.getNoEmployee().toString());
                txtNoEmpleado.setEditable(false);
                txtNoEmpleado.setDisable(true);

                txtNombreEmpleado.setText(employee.getName());
                txtDireccionEmpleado.setText(employee.getAddress());
                txtDepartamentoEmpleado.setText(employee.getDepartment());
                txtUsuarioEmpleado.setText(employee.getAccount());
                txtPasswordEmpleado.setText(employee.getPassword());
                break;
        }
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void initialize() {
        System.out.println("AccionEmpleadoController did initialize");

        Conexion.getInstance().setController(this);

        txtNoEmpleado.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtNoEmpleado.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        cboxPerfil.setCellFactory(new Callback<ListView<CatalogueProfile>, ListCell<CatalogueProfile>>() {
            @Override
            public ListCell<CatalogueProfile> call(ListView<CatalogueProfile> catalogueProfileListView) {
                final ListCell<CatalogueProfile> cell = new ListCell<CatalogueProfile>() {

                    @Override
                    protected void updateItem(CatalogueProfile catalogueProfile, boolean bln) {
                        super.updateItem(catalogueProfile, bln);

                        if (catalogueProfile != null) {
                            setText(catalogueProfile.getProfileName());
                        } else {
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });

        cboxPuntoDeAtencion.setCellFactory(new Callback<ListView<CatalogueAttentionPoint>, ListCell<CatalogueAttentionPoint>>() {
            @Override
            public ListCell<CatalogueAttentionPoint> call(ListView<CatalogueAttentionPoint> catalogueAttentionPointListView) {
                final ListCell<CatalogueAttentionPoint> cell = new ListCell<CatalogueAttentionPoint>() {

                    @Override
                    protected void updateItem(CatalogueAttentionPoint catalogueAttentionPoint, boolean bln) {
                        super.updateItem(catalogueAttentionPoint, bln);

                        if (catalogueAttentionPoint != null) {
                            setText(catalogueAttentionPoint.getPoint());
                        } else {
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });

        cboxSucursal.setCellFactory(new Callback<ListView<CatalogueBranch>, ListCell<CatalogueBranch>>() {
            @Override
            public ListCell<CatalogueBranch> call(ListView<CatalogueBranch> catalogueBranchListView) {
                final ListCell<CatalogueBranch> cell = new ListCell<CatalogueBranch>() {

                    @Override
                    protected void updateItem(CatalogueBranch catalogueBranch, boolean bln) {
                        super.updateItem(catalogueBranch, bln);

                        if (catalogueBranch != null) {
                            setText(catalogueBranch.getBranchName());
                        } else {
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });

        cboxPerfil.setConverter(new StringConverter<CatalogueProfile>() {
            @Override
            public String toString(CatalogueProfile perfil) {
                if (perfil != null) {
                    return perfil.getProfileName();
                }
                return "Seleccione un perfil";
            }

            @Override
            public CatalogueProfile fromString(String string) {
                return null;
            }
        });

        cboxPuntoDeAtencion.setConverter(new StringConverter<CatalogueAttentionPoint>() {
            @Override
            public String toString(CatalogueAttentionPoint catalogueAttentionPoint) {
                if (catalogueAttentionPoint != null) {
                    return catalogueAttentionPoint.getPoint();
                }
                return "Seleccione un punto de atención";
            }

            @Override
            public CatalogueAttentionPoint fromString(String string) {
                return null;
            }
        });

        cboxSucursal.setConverter(new StringConverter<CatalogueBranch>() {
            @Override
            public String toString(CatalogueBranch catalogueBranch) {
                if (catalogueBranch != null) {
                    return catalogueBranch.getBranchName();
                }
                return "Seleccione una sucursal";
            }

            @Override
            public CatalogueBranch fromString(String string) {
                return null;
            }
        });

        addTextLimiter(txtNoEmpleado, 250);
        addTextLimiter(txtNombreEmpleado, 250);
        addTextLimiter(txtDepartamentoEmpleado, 250);
        addTextLimiter(txtDireccionEmpleado, 250);
        addTextLimiter(txtPasswordEmpleado, 250);
        addTextLimiter(txtUsuarioEmpleado, 250);

        try {
            Conexion.getInstance().sendMessage(new Message(Message.MessageType.GET_MANY_CATALOGUE_PROFILE, "usuario"));
            Conexion.getInstance().sendMessage(new Message(Message.MessageType.GET_MANY_CATALOGUE_ATTENTION_POINT, "usuario"));
            Conexion.getInstance().sendMessage(new Message(Message.MessageType.GET_MANY_CATALOGUE_BRANCH, "usuario"));
        } catch (IOException e) {
            makeToast("Falló conexión con el servidor.");
        }
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
    public void didClickAceptarButton() {
        if (!camposVacios()) {
            switch (accion) {
                case "Añadir":
                    addEmployee();
                    break;
                case "Editar":
                    editEmployee();
                    break;
            }
        } else {
            makeToast("Llenar los campos vacios.");
        }
    }

    private void editEmployee() {
        Employee employee = this.employee;

        employee.setNoEmployee(Long.valueOf(txtNoEmpleado.getText()));
        employee.setName(txtNombreEmpleado.getText());
        employee.setDepartment(txtDepartamentoEmpleado.getText());
        employee.setAddress(txtDireccionEmpleado.getText());
        employee.setAccount(txtUsuarioEmpleado.getText());
        employee.setPassword(txtPasswordEmpleado.getText());
        employee.getIdAttentionPoint().setPoint(cboxPuntoDeAtencion.getValue().getPoint());
        employee.getIdBranch().setBranchName(cboxSucursal.getValue().getBranchName());
        employee.getIdProfile().setProfileName(cboxPerfil.getValue().getProfileName());

        Message msg = new Message(Message.MessageType.EDIT_EMPLOYEE, "test_user");
        msg.setObject(employee);

        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addEmployee() {
        Employee employee = new Employee();

        employee.setNoEmployee(Long.valueOf(txtNoEmpleado.getText()));
        employee.setName(txtNombreEmpleado.getText());
        employee.setDepartment(txtDepartamentoEmpleado.getText());
        employee.setAddress(txtDireccionEmpleado.getText());
        employee.setAccount(txtUsuarioEmpleado.getText());
        employee.setPassword(txtPasswordEmpleado.getText());
        employee.setIdAttentionPoint(cboxPuntoDeAtencion.getValue().toCatalogueAttentionPoint());
        employee.setIdBranch(cboxSucursal.getValue().toCatalogueBranch());
        employee.setIdProfile(cboxPerfil.getValue().toProfile());

        Message msg = new Message(Message.MessageType.ADD_EMPLOYEE, "test_user");
        msg.setObject(employee);

        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void didClickCancelarButton() {
        cargarMenuEmpleados();
    }

    private void cargarMenuEmpleados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuEmpleados.fxml"));
            Parent root = loader.load();

            rootPane.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetField(MouseEvent event) {
        ((Node) event.getSource()).setStyle("");
    }

    public void makeToast(String mensaje) {
        Toast.makeText(null, mensaje, 3500, 100, 300);
    }

    private boolean camposVacios() {
        boolean respuesta = false;

        if (txtNoEmpleado.getText().isEmpty()) {
            txtNoEmpleado.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (txtNombreEmpleado.getText().isEmpty()) {
            txtNombreEmpleado.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (txtDireccionEmpleado.getText().isEmpty()) {
            txtDireccionEmpleado.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (txtDepartamentoEmpleado.getText().isEmpty()) {
            txtDepartamentoEmpleado.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (txtUsuarioEmpleado.getText().isEmpty()) {
            txtUsuarioEmpleado.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (txtPasswordEmpleado.getText().isEmpty()) {
            txtPasswordEmpleado.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (cboxSucursal.getValue() == null) {
            cboxSucursal.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (cboxPuntoDeAtencion.getValue() == null) {
            cboxPuntoDeAtencion.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (cboxPerfil.getValue() == null) {
            cboxPerfil.setStyle("-fx-border-color: red");
            respuesta = true;
        }

        return respuesta;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case GET_MANY_CATALOGUE_PROFILE:
                ObservableList<CatalogueProfile> listaP = FXCollections.observableList((List<CatalogueProfile>) message.getObject());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        cboxPerfil.setItems(listaP);

                        if (accion.equalsIgnoreCase("Editar")) {
                            for (int i = 0; i < cboxPerfil.getItems().size(); i++) {
                                if (cboxPerfil.getItems().get(i).getProfileName().equalsIgnoreCase(employee.getIdProfile().getProfileName())) {
                                    cboxPerfil.getSelectionModel().select(i);
                                    break;
                                }
                            }
                        }
                    }
                });

                break;
            case GET_MANY_CATALOGUE_ATTENTION_POINT:
                ObservableList<CatalogueAttentionPoint> listaA = FXCollections.observableList((List<CatalogueAttentionPoint>) message.getObject());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        cboxPuntoDeAtencion.setItems(listaA);

                        if (accion.equalsIgnoreCase("Editar")) {
                            for (int i = 0; i < cboxPuntoDeAtencion.getItems().size(); i++) {
                                if (cboxPuntoDeAtencion.getItems().get(i).getPoint().equalsIgnoreCase(employee.getIdAttentionPoint().getPoint())) {
                                    cboxPuntoDeAtencion.getSelectionModel().select(i);
                                    break;
                                }
                            }
                        }
                    }
                });

                break;
            case GET_MANY_CATALOGUE_BRANCH:
                ObservableList<CatalogueBranch> listaB = FXCollections.observableList((List<CatalogueBranch>) message.getObject());
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        cboxSucursal.setItems(listaB);

                        if (accion.equalsIgnoreCase("Editar")) {
                            for (int i = 0; i < cboxSucursal.getItems().size(); i++) {
                                if (cboxSucursal.getItems().get(i).getBranchName().equalsIgnoreCase(employee.getIdBranch().getBranchName())) {
                                    cboxSucursal.getSelectionModel().select(i);
                                    break;
                                }
                            }
                        }
                    }
                });
                break;
            case ADD_EMPLOYEE:
                ObservableList<Employee> listaE = FXCollections.observableList((List<Employee>) message.getObject());
                if (listaE.isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("Ya existe un empleado con el numero y el usuario ingresados.");
                            txtNoEmpleado.setStyle("-fx-border-color: red");
                            txtUsuarioEmpleado.setStyle("-fx-border-color: red");
                        }
                    });
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("El empleado ha sido creado correctamente");
                            cargarMenuEmpleados();
                        }
                    });
                }
                break;

            case EDIT_EMPLOYEE:
                ObservableList<Employee> listaE2 = FXCollections.observableList((List<Employee>) message.getObject());
                if (listaE2.isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("Ya existe un empleado con el usuario ingresado.");
                            txtUsuarioEmpleado.setStyle("-fx-border-color: red");
                        }
                    });
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("El empleado ha sido editado correctamente");
                            cargarMenuEmpleados();
                        }
                    });
                }
                break;
        }
    }

}
