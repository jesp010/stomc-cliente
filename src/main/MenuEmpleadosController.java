package main;

import comm.Conexion;
import dominio.Employee;
import dominio.Message;
import gui_elements.Toast;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MenuEmpleadosController implements IController {

    AccionEmpleadoController accionEmpleadoController;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Button btnAniadirEmpleado;
    @FXML
    public Button btnEditarEmpleado;
    @FXML
    public Button btnEliminarEmpleado;

    @FXML
    public TableView<Employee> tblEmpleados;

    @FXML
    public TableColumn colFolioEmpleado;
    @FXML
    public TableColumn colNoEmpleado;
    @FXML
    public TableColumn colNombreEmpleado;
    @FXML
    public TableColumn colDireccionEmpleado;
    @FXML
    public TableColumn colDepartamentoEmpleado;
    @FXML
    public TableColumn colPuntoDeAtencionEmpleado;
    @FXML
    public TableColumn colSucursalEmpleado;
    @FXML
    public TableColumn colPerfilEmpleado;


    public void initialize() {
        System.out.println("MenuEmpleadosController did initialize");

        Conexion.getInstance().setController(this);

        colFolioEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Employee, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getFolio() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getFolio().toString());
                }
                return new SimpleStringProperty("");
            }
        });

        colNoEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Employee, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getNoEmployee() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getNoEmployee().toString());
                }
                return new SimpleStringProperty("");
            }
        });

        colNombreEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Employee, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getName() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getName());
                }
                return new SimpleStringProperty("");
            }
        });

        colDireccionEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Employee, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getName() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getAddress());
                }
                return new SimpleStringProperty("");
            }
        });

        colDepartamentoEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Employee, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getDepartment() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getDepartment());
                }
                return new SimpleStringProperty("");
            }
        });

        colPuntoDeAtencionEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Employee, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getIdAttentionPoint() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getIdAttentionPoint().getPoint());
                }
                return new SimpleStringProperty("");
            }
        });

        colSucursalEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Employee, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getIdBranch() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getIdBranch().getBranchName());
                }
                return new SimpleStringProperty("");
            }
        });

        colPerfilEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Employee, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Employee, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getIdProfile() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getIdProfile().getProfileName());
                }
                return new SimpleStringProperty("");
            }
        });


        Message msg = new Message(Message.MessageType.GET_MANY_EMPLOYEE, "test_user");
        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void didClickAniadirEmpleadoButton(ActionEvent actionEvent) {
        loadContent(actionEvent, "Añadir");
    }

    @FXML
    public void didClickEditarEmpleadoButton(ActionEvent actionEvent) {
        if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
            loadContent(actionEvent, "Editar");
        } else {
            makeToast("Selecciona un Empleado para editarlo");
        }
    }

    @FXML
    public void didClickEliminarEmpleadoButton(ActionEvent actionEvent) {
        if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
            deleteEmployee();
        } else {
            makeToast("Selecciona un Empleado para eliminarlo");
        }
    }

    private void deleteEmployee() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estas seguro de eliminar este empleado?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {

            Message msg = new Message(Message.MessageType.DELETE_EMPLOYEE, "test_user");
            msg.setObject(tblEmpleados.getSelectionModel().getSelectedItem().getId());

            try {
                Conexion.getInstance().sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void loadContent(ActionEvent actionEvent, String accion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accionEmpleado.fxml"));
            Parent root = loader.load();

            accionEmpleadoController = loader.getController();

            if (accion.equalsIgnoreCase("Editar")) {
                accionEmpleadoController.setEmployee(tblEmpleados.getSelectionModel().getSelectedItem());
            }
            accionEmpleadoController.setAccion(accion);

            rootPane.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeToast(String mensaje) {
        Toast.makeText(null, mensaje, 3500, 100, 300);
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case GET_MANY_EMPLOYEE:
                ObservableList<Employee> lista1 = FXCollections.observableList((List<Employee>) message.getObject());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblEmpleados.setItems(lista1);
                    }
                });
                break;

            case DELETE_EMPLOYEE:
                ObservableList<Employee> lista2 = FXCollections.observableList((List<Employee>) message.getObject());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblEmpleados.setItems(lista2);
                        makeToast("El empleado ha sido eliminado correctamente");
                    }
                });
                break;
        }
    }
}
