package main;

import comm.Conexion;
import dominio.AttentionPoint;
import dominio.CatalogueAttentionPoint;
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

public class MenuPuntosDeAtencionController implements IController {

    AccionPuntoDeAtencionController accionPuntoDeAtencionController;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Button btnAniadirPuntoDeAtencion;
    @FXML
    public Button btnEditarPuntoDeAtencion;
    @FXML
    public Button btnEliminarPuntoDeAtencion;

    @FXML
    public TableView<AttentionPoint> tblPuntoDeAtencion;

    @FXML
    public TableColumn colFolioPuntoDeAtencion;
    @FXML
    public TableColumn colPuntoPuntoDeAtencion;
    @FXML
    public TableColumn colEmpleadoPuntoDeAtencion;
    @FXML
    public TableColumn colSucursalPuntoDeAtencion;

    @FXML
    public TableView<CatalogueAttentionPoint> tblCatalagoPuntoDeAtencion;
    @FXML
    public TableColumn colFolioCatalagoPuntoDeAtencion;
    @FXML
    public TableColumn colPuntoCatalagoPuntoDeAtencion;

    public void initialize(){
        System.out.println("AdministrarSistemaController did initialize");

        Conexion.getInstance().setController(this);

        colFolioPuntoDeAtencion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AttentionPoint, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AttentionPoint, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getFolio() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getFolio().toString());
                }
                return new SimpleStringProperty("");
            }
        });

        colPuntoPuntoDeAtencion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AttentionPoint, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AttentionPoint, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getPoint() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getPoint());
                }
                return new SimpleStringProperty("");
            }
        });

        colEmpleadoPuntoDeAtencion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AttentionPoint, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AttentionPoint, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getEmployee() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getEmployee().getName());
                }
                return new SimpleStringProperty("");
            }
        });

        colSucursalPuntoDeAtencion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<AttentionPoint, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<AttentionPoint, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getEmployee() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getEmployee().getIdBranch().getBranchName());
                }
                return new SimpleStringProperty("");
            }
        });

        // Columna de Catalago de Puntos de Atencion
        colFolioCatalagoPuntoDeAtencion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CatalogueAttentionPoint, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CatalogueAttentionPoint, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getFolio() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getFolio().toString());
                }
                return new SimpleStringProperty("");
            }
        });

        colPuntoCatalagoPuntoDeAtencion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CatalogueAttentionPoint, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CatalogueAttentionPoint, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getFolio() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getPoint());
                }
                return new SimpleStringProperty("");
            }
        });

        try {
            Conexion.getInstance().sendMessage(new Message(Message.MessageType.GET_MANY_ATTENTION_POINT, "test_user"));
            Conexion.getInstance().sendMessage(new Message(Message.MessageType.GET_MANY_CATALOGUE_ATTENTION_POINT, "test_user"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void didClickAniadirPuntoDeAtencionButton(ActionEvent actionEvent) {
        loadContent(actionEvent, "Añadir");
    }

    @FXML
    public void didClickEditarPuntoDeAtencionButton(ActionEvent actionEvent) {
        if (tblCatalagoPuntoDeAtencion.getSelectionModel().getSelectedItem() != null) {
            loadContent(actionEvent, "Editar");
        } else {
            makeToast("Seleccione un punto de atención del catalago para editarlo");
        }
    }

    @FXML
    public void didClickEliminarPuntoDeAtencionButton(ActionEvent actionEvent) {
        if (tblCatalagoPuntoDeAtencion.getSelectionModel().getSelectedItem() != null) {
            deleteCatalogueAttentionPoint();
        } else {
            makeToast("Selecciona un punto de atención del catalago  para eliminarlo");
        }
    }

    private void deleteCatalogueAttentionPoint() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estas seguro de eliminar este punto de atención del catalago?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {

            Message msg = new Message(Message.MessageType.DELETE_CATALOGUE_ATTENTION_POINT, "test_user");
            msg.setObject(tblCatalagoPuntoDeAtencion.getSelectionModel().getSelectedItem().getId());

            try {
                Conexion.getInstance().sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void loadContent(ActionEvent actionEvent, String accion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accionPuntoDeAtencion.fxml"));
            Parent root = loader.load();

            accionPuntoDeAtencionController = loader.getController();

            if (accion.equalsIgnoreCase("Editar")) {
                accionPuntoDeAtencionController.setAttentionPoint(tblCatalagoPuntoDeAtencion.getSelectionModel().getSelectedItem());
            }
            accionPuntoDeAtencionController.setAccion(accion);

            rootPane.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeToast(String mensaje) {
        Toast.makeText(null, mensaje, 3500,100,300);
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case GET_MANY_ATTENTION_POINT:
                ObservableList<AttentionPoint> lista1 = FXCollections.observableList((List<AttentionPoint>) message.getObject());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblPuntoDeAtencion.setItems(lista1);
                    }
                });
                break;

            case GET_MANY_CATALOGUE_ATTENTION_POINT:
                ObservableList<CatalogueAttentionPoint> lista2 = FXCollections.observableList((List<CatalogueAttentionPoint>) message.getObject());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblCatalagoPuntoDeAtencion.setItems(lista2);
                    }
                });
                break;

            case DELETE_CATALOGUE_ATTENTION_POINT:
                ObservableList<CatalogueAttentionPoint> lista3 = FXCollections.observableList((List<CatalogueAttentionPoint>) message.getObject());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblCatalagoPuntoDeAtencion.setItems(lista3);
                        makeToast("El punto de atención ha sido eliminado correctamente");
                    }
                });
                break;
        }
    }
}
