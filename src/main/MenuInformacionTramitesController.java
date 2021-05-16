package main;

import comm.Conexion;
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

public class MenuInformacionTramitesController implements IController {

    AccionInformacionTramiteController accionInformacionTramiteController;

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Button btnAniadir;
    @FXML
    public Button btnEditar;
    @FXML
    public Button btnEliminar;

    @FXML
    public TableView<LysingInformation> tblInformacionTramites;
    @FXML
    public TableColumn colFolioInformacionTramites;
    @FXML
    public TableColumn colTituloInformacionTramites;
    @FXML
    public TableColumn<LysingInformation, String> colProcesoInformacionTramites;

    public void initialize(){
        System.out.println("MenuInformacionTramitesController did initialize");

        Conexion.getInstance().setController(this);
        
        colFolioInformacionTramites.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LysingInformation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LysingInformation, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getFolio() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getFolio().toString());
                }
                return new SimpleStringProperty("");
            }
        });

        colTituloInformacionTramites.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LysingInformation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LysingInformation, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getTitle() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getTitle());
                }
                return new SimpleStringProperty("");
            }
        });

        colProcesoInformacionTramites.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LysingInformation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<LysingInformation, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getProcess() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getProcess());
                }
                return new SimpleStringProperty("");
            }
        });

        // Wrap text en la columna de los procesos.
        colProcesoInformacionTramites.setCellFactory(tc -> {
            TableCell<LysingInformation, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(colProcesoInformacionTramites.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });

        try {
            Conexion.getInstance().sendMessage(new Message(Message.MessageType.GET_MANY_LYSING_INFORMATION, "test_user"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void didClickAniadirInformationTramiteButton(ActionEvent actionEvent) {
        loadContent(actionEvent, "Añadir");
    }

    @FXML
    public void didClickEditarInformationTramiteButton(ActionEvent actionEvent) {
        if (tblInformacionTramites.getSelectionModel().getSelectedItem() != null) {
            loadContent(actionEvent, "Editar");
        } else {
            makeToast("Seleccione un trámite para editarlo");
        }
    }

    @FXML
    public void didClickEliminarInformationTramiteButton(ActionEvent actionEvent) {
        if (tblInformacionTramites.getSelectionModel().getSelectedItem() != null) {
            deleteLysingInformation();
        } else {
            makeToast("Selecciona un trámite para eliminarlo");
        }
    }

    private void deleteLysingInformation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirmación");
        alert.setContentText("¿Estas seguro de eliminar este trámite?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.get() == ButtonType.OK) {

            Message msg = new Message(Message.MessageType.DELETE_LYSING_INFORMATION, "test_user");
            msg.setObject(tblInformacionTramites.getSelectionModel().getSelectedItem().getId());

            try {
                Conexion.getInstance().sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void loadContent(ActionEvent actionEvent, String accion) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("accionInformacionTramite.fxml"));
            Parent root = loader.load();

            accionInformacionTramiteController = loader.getController();

            if (accion.equalsIgnoreCase("Editar")) {
                accionInformacionTramiteController.setInformacionTramite(tblInformacionTramites.getSelectionModel().getSelectedItem());
            }
            accionInformacionTramiteController.setAccion(accion);

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
            case GET_MANY_LYSING_INFORMATION:
                ObservableList<LysingInformation> lista1 = FXCollections.observableList((List<LysingInformation>) message.getObject());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblInformacionTramites.setItems(lista1);
                    }
                });
                break;

            case DELETE_LYSING_INFORMATION:
                ObservableList<LysingInformation> lista2 = FXCollections.observableList((List<LysingInformation>) message.getObject());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblInformacionTramites.setItems(lista2);
                        makeToast("El trámite ha sido eliminado correctamente");
                    }
                });
                break;
        }
    }
}
