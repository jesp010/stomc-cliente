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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccionPuntoDeAtencionController implements IController {
    private String accion;
    private CatalogueAttentionPoint attentionPoint;
    private static List<String> tipos = new ArrayList<String>() {
        {
            add("Caja");
            add("Módulo");
            add("Caja/Módulo");
        }
    };


    @FXML
    public AnchorPane rootPane;

    @FXML
    public Label lblPuntoDeAtencionAccion;

    @FXML
    public Spinner<Integer> txtNumero;
    @FXML
    public ComboBox<String> cboxTipo;

    @FXML
    public Button btnAceptar;
    @FXML
    public Button btnCancelar;

    public void setAccion(String accion) {
        this.accion = accion;

        String separador = "\\s*-\\s*";
        switch (accion) {
            case "Añadir":
                lblPuntoDeAtencionAccion.setText("Añadir Punto de Atención");
                btnAceptar.setText("Añadir");
                break;
            case "Editar":
                lblPuntoDeAtencionAccion.setText("Editar Punto de Atención");
                btnAceptar.setText("Editar");

                for (int i = 0; i < cboxTipo.getItems().size(); i++) {
                    if (cboxTipo.getItems().get(i).equalsIgnoreCase(attentionPoint.getPoint().split(separador)[0])) {
                        cboxTipo.getSelectionModel().select(i);
                        break;
                    }
                }

                txtNumero.getValueFactory().setValue( Integer.valueOf(attentionPoint.getPoint().split(separador)[1]) );

                break;
        }
    }

    public void setAttentionPoint(CatalogueAttentionPoint attentionPoint) {
        this.attentionPoint = attentionPoint;
    }

    public void initialize(){
        System.out.println("AccionPuntoDeAtencionController did initialize");

        Conexion.getInstance().setController(this);

        // Solo acepta valores numericos
//        txtNumero.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                if (!newValue.matches("\\d*")) {
//                    txtNumero.setText(newValue.replaceAll("[^\\d]", ""));
//                }
//            }
//        });



        cboxTipo.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> tipoListView) {
                final ListCell<String> cell = new ListCell<String>(){

                    @Override
                    protected void updateItem(String tipo, boolean bln) {
                        super.updateItem(tipo, bln);

                        if(tipo != null){
                            setText(tipo);
                        }else{
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });

        cboxTipo.setItems(FXCollections.observableList(tipos));
    }

    @FXML
    public void didClickAceptarButton(){
        if (!camposVacios()) {
            switch (accion) {
                case "Añadir":
                    addAttentionPoint();
                    break;
                case "Editar":
                    editAttentionPoint();
                    break;
            }
        } else {
            makeToast("Llenar los campos vacios");
        }
    }

    private void editAttentionPoint() {
        CatalogueAttentionPoint catalogueAttentionPoint = this.attentionPoint;

        catalogueAttentionPoint.setPoint(cboxTipo.getValue() + " - " + txtNumero.getValue());

        Message msg = new Message(Message.MessageType.EDIT_CATALOGUE_ATTENTION_POINT, "test_user");
        msg.setObject(catalogueAttentionPoint);

        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAttentionPoint() {
        CatalogueAttentionPoint catalogueAttentionPoint = new CatalogueAttentionPoint();

        catalogueAttentionPoint.setPoint(cboxTipo.getValue() + " - " + txtNumero.getValue());

        Message msg = new Message(Message.MessageType.ADD_CATALOGUE_ATTENTION_POINT, "test_user");
        msg.setObject(catalogueAttentionPoint);

        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void didClickCancelarButton() {
        cargarMenuPuntoDeAtencion();
    }

    private void cargarMenuPuntoDeAtencion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPuntosDeAtencion.fxml"));
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

        if (txtNumero.getValue() == null) {
            txtNumero.setStyle("-fx-border-color: red");
            respuesta = true;
        }
        if (cboxTipo.getValue() == null) {
            cboxTipo.setStyle("-fx-border-color: red");
            respuesta = true;
        }

        return respuesta;
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case ADD_CATALOGUE_ATTENTION_POINT:
                ObservableList<CatalogueAttentionPoint> lista1 = FXCollections.observableList( (List<CatalogueAttentionPoint>) message.getObject() );
                if (lista1.isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("Ya existe un punto de atención con esos datos ingresados");
                            txtNumero.setStyle("-fx-border-color: red");
                            cboxTipo.setStyle("-fx-border-color: red");
                        }
                    });

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("El punto de atención ha sido creado correctamente");
                            cargarMenuPuntoDeAtencion();
                        }
                    });
                }
                break;

            case EDIT_CATALOGUE_ATTENTION_POINT:
                ObservableList<CatalogueAttentionPoint> lista2 = FXCollections.observableList( (List<CatalogueAttentionPoint>) message.getObject() );
                if (lista2.isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("Ya existe un punto de atención con esos datos ingresados");
                            txtNumero.setStyle("-fx-border-color: red");
                            cboxTipo.setStyle("-fx-border-color: red");
                        }
                    });

                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            makeToast("El punto de atención ha sido editado correctamente");
                            cargarMenuPuntoDeAtencion();
                        }
                    });
                }
                break;
        }
    }
}
