package main;

import comm.Conexion;
import dominio.CatalogueBranch;
import dominio.LysingInformation;
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
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MenuSucursalesController implements IController {

    AccionSucursalController accionSucursalController;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Button btnAniadir;
    @FXML
    public Button btnEditar;
    @FXML
    public Button btnEliminar;

    @FXML
    public TableView<CatalogueBranch> tblCatalagoSucursales;
    @FXML
    public TableColumn colFolioCatalagoSucursales;
    @FXML
    public TableColumn colNombreCatalagoSucursales;
    @FXML
    public TableColumn colDireccion;

    public void initialize(){
        System.out.println("MenuSucursalesController did initialize");

        Conexion.getInstance().setController(this);

        colFolioCatalagoSucursales.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CatalogueBranch, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CatalogueBranch, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getFolio() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getFolio().toString());
                }
                return new SimpleStringProperty("");
            }
        });

        colNombreCatalagoSucursales.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CatalogueBranch, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CatalogueBranch, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getBranchName() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getBranchName());
                }
                return new SimpleStringProperty("");
            }
        });

        colDireccion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<CatalogueBranch, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<CatalogueBranch, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getAddress() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getAddress());
                }
                return new SimpleStringProperty("");
            }
        });

        // Wrap text en la columna de los procesos.
        colDireccion.setCellFactory(tc -> {
            TableCell<LysingInformation, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(colDireccion.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });


        try {
            Conexion.getInstance().sendMessage(new Message(Message.MessageType.GET_MANY_CATALOGUE_BRANCH, "test_user"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void didClickAniadirSucursalButton(ActionEvent actionEvent) {
        loadContent(actionEvent, "Añadir");
    }

    @FXML
    public void didClickEditarSucursalButton(ActionEvent actionEvent) {
        if (tblCatalagoSucursales.getSelectionModel().getSelectedItem() != null) {
            loadContent(actionEvent, "Editar");
        } else {
            makeToast("Seleccione una sucursal para editarlo");
        }
    }

    @FXML
    public void didClickEliminarSucursalButton(ActionEvent actionEvent) {
        if (tblCatalagoSucursales.getSelectionModel().getSelectedItem() != null) {
            deleteCatalogueBranch();
        } else {
            makeToast("Selecciona una sucursal del catalago para eliminarlo");
        }
    }

    private void deleteCatalogueBranch() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estas seguro de eliminar esta sucursal del catalago?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {

            Message msg = new Message(Message.MessageType.DELETE_CATALOGUE_BRANCH, "test_user");
            msg.setObject(tblCatalagoSucursales.getSelectionModel().getSelectedItem().getId());

            try {
                Conexion.getInstance().sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void loadContent(ActionEvent actionEvent, String accion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accionSucursal.fxml"));
            Parent root = loader.load();

            accionSucursalController = loader.getController();

            if (accion.equalsIgnoreCase("Editar")) {
                accionSucursalController.setSucursal(tblCatalagoSucursales.getSelectionModel().getSelectedItem());
            }
            accionSucursalController.setAccion(accion);

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
            case GET_MANY_CATALOGUE_BRANCH:
                ObservableList<CatalogueBranch> lista1 = FXCollections.observableList((List<CatalogueBranch>) message.getObject());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblCatalagoSucursales.setItems(lista1);
                    }
                });
                break;

            case DELETE_CATALOGUE_BRANCH:
                ObservableList<CatalogueBranch> lista2 = FXCollections.observableList((List<CatalogueBranch>) message.getObject());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblCatalagoSucursales.setItems(lista2);
                        makeToast("La sucursal ha sido eliminado correctamente");
                    }
                });
                break;
        }
    }
}
