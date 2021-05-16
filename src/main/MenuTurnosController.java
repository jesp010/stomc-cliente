package main;

import comm.Conexion;
import dominio.Message;
import dominio.Turn;
import gui_elements.Toast;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import reportes.ControllerReport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuTurnosController implements IController {

    private static List<Turn> turnos;

    private static final List<String> estados = new ArrayList<String>() {
        {
            add("Todos");
            add("Esperando");
            add("Atendiendo");
            add("Finalizado");
        }
    };

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Button btnConsultar;
    @FXML
    public Button btnGenerarReporte;

    @FXML
    public TableView<Turn> tblTurnos;

    @FXML
    public TableColumn colSucursal;
    @FXML
    public TableColumn colFecha;
    @FXML
    public TableColumn colNumeroTurno;
    @FXML
    public TableColumn colTipoTurno;
    @FXML
    public TableColumn colHoraInicio;
    @FXML
    public TableColumn colMinutosEsperando;
    @FXML
    public TableColumn colMinutosAtendiendo;
    @FXML
    public TableColumn colEstado;
    @FXML
    public TableColumn colNoEmpleado;

    @FXML
    public DatePicker dpFechaInicio;
    @FXML
    public DatePicker dpFechaFin;
    @FXML
    public ComboBox<String> cboxEstado;

    public void initialize() {
        System.out.println("MenuTurnosController did initialize");

        Conexion.getInstance().setController(this);

        colSucursal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Turn, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getIdEmployee() != null) {
                    if (cellDataFeatures.getValue().getIdEmployee().getIdBranch() != null) {
                        return new SimpleStringProperty(cellDataFeatures.getValue().getIdEmployee().getIdBranch().getBranchName());
                    }
                }
                return new SimpleStringProperty("-");
            }
        });

        colFecha.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Turn, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getDateTimeCreated() != null) {
                    return new SimpleStringProperty(new SimpleDateFormat("yyyy-MM-dd").format(cellDataFeatures.getValue().getDateTimeCreated()));
                }
                return new SimpleStringProperty("-");
            }
        });

        colHoraInicio.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Turn, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getDateTimeCreated() != null) {
                    return new SimpleStringProperty(new SimpleDateFormat("hh:mm a").format(cellDataFeatures.getValue().getDateTimeCreated()));
                }
                return new SimpleStringProperty("-");
            }
        });

        colMinutosEsperando.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Turn, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getDateTimeAssigned() != null) {

                    long millies = cellDataFeatures.getValue().getDateTimeAssigned().getTime() - cellDataFeatures.getValue().getDateTimeCreated().getTime();

                    return new SimpleStringProperty(new SimpleDateFormat("mm:ss").format(new Date(millies)));
                }
                return new SimpleStringProperty("-");
            }
        });

        colMinutosAtendiendo.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Turn, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getDateTimeFinished() != null) {
                    long millies = cellDataFeatures.getValue().getDateTimeFinished().getTime() - cellDataFeatures.getValue().getDateTimeAssigned().getTime();

                    return new SimpleStringProperty(new SimpleDateFormat("mm:ss").format(new Date(millies)));
                }
                return new SimpleStringProperty("-");
            }
        });

        colEstado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Turn, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getStatus() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getStatus());
                }
                return new SimpleStringProperty("-");
            }
        });

        colNoEmpleado.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Turn, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getIdEmployee() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getIdEmployee().getNoEmployee().toString());
                }
                return new SimpleStringProperty("-");
            }
        });

        colNumeroTurno.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Turn, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getTurnNumber() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getTurnNumber().toString());
                }
                return new SimpleStringProperty("-");
            }
        });

        colTipoTurno.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Turn, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Turn, String> cellDataFeatures) {
                if (cellDataFeatures.getValue().getType() != null) {
                    return new SimpleStringProperty(cellDataFeatures.getValue().getType().toString());
                }
                return new SimpleStringProperty("-");
            }
        });

        dpFechaInicio.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (dpFechaFin.getValue() != null) {
                    LocalDate limit = dpFechaFin.getValue();

                    setDisable(empty || date.compareTo(limit) > -1);
                }
            }
        });

        dpFechaFin.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (dpFechaInicio.getValue() != null) {
                    LocalDate limit = dpFechaInicio.getValue();

                    setDisable(empty || date.compareTo(limit) < 1);
                }
            }
        });


        cboxEstado.setItems(FXCollections.observableList(estados));
        cboxEstado.getSelectionModel().selectFirst();

        Message msg = new Message(Message.MessageType.GET_MANY_TURN, "test_user");
        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void didClickConsultarButton(ActionEvent actionEvent) {

        Date fechaInicio = null;
        Date fechaFin = null;
        String estado = null;

        Message msg = new Message();
        ArrayList<Object> contenido = new ArrayList<>();

        if (dpFechaInicio.getValue() != null) {
            fechaInicio = java.sql.Date.valueOf(dpFechaInicio.getValue());
        }

        if (dpFechaFin.getValue() != null) {
            fechaFin = java.sql.Date.valueOf(dpFechaFin.getValue());
        }

        if (!cboxEstado.getValue().equalsIgnoreCase("Todos")) {
            estado = cboxEstado.getValue();
        }

        contenido.add(fechaInicio);
        contenido.add(fechaFin);
        contenido.add(estado);

        msg.setObject(contenido);

        if (fechaInicio != null) {
            if (fechaFin != null) {
                if (estado != null) {
                    msg.setType(Message.MessageType.GET_TURNS_STATUS_START_AND_END_DATE);
                } else {
                    msg.setType(Message.MessageType.GET_TURNS_START_AND_END_DATE);
                }
            } else {
                if (estado != null) {
                    msg.setType(Message.MessageType.GET_TURNS_STATUS_START_DATE);
                } else {
                    msg.setType(Message.MessageType.GET_TURNS_START_DATE);
                }
            }
        } else {
            if (fechaFin != null) {
                if (estado != null) {
                    msg.setType(Message.MessageType.GET_TURNS_STATUS_END_DATE);
                } else {
                    msg.setType(Message.MessageType.GET_TURNS_END_DATE);
                }
            } else {
                if (estado != null) {
                    msg.setType(Message.MessageType.GET_TURNS_STATUS);
                } else {
                    msg.setType(Message.MessageType.GET_MANY_TURN);
                }
            }
        }

        msg.setRequestedBy("user");

        try {
            Conexion.getInstance().sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void didClickResetDatePickerInicioButton() {
        dpFechaInicio.setValue(null);
    }

    @FXML
    public void didClickResetDatePickerFinButton() {
        dpFechaFin.setValue(null);
    }

    @FXML
    public void didClickGenerarReporteButton(ActionEvent actionEvent) {
        if (turnos != null) {
            new ControllerReport().generarReporte(turnos);
        }
    }

    public void makeToast(String mensaje) {
        Toast.makeText(null, mensaje, 3500, 100, 300);
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.getType()) {
            case GET_TURNS_STATUS:
            case GET_TURNS_START_DATE:
            case GET_TURNS_END_DATE:
            case GET_TURNS_STATUS_START_DATE:
            case GET_TURNS_STATUS_END_DATE:
            case GET_TURNS_START_AND_END_DATE:
            case GET_TURNS_STATUS_START_AND_END_DATE:
            case GET_MANY_TURN:
                turnos = (List<Turn>) message.getObject();
                ObservableList<Turn> lista = FXCollections.observableList((List<Turn>) message.getObject());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tblTurnos.setItems(lista);
                    }
                });
                break;
        }
    }
}
