package main;

import dominio.CatalogueAttentionPoint;
import dominio.CatalogueProfile;
import dominio.LysingInformation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.StringConverter;

import java.util.List;

public class RequisitosController {

    private List<LysingInformation> lysingInformations;

    @FXML
    public ImageView imgLogo;
    @FXML
    public Button imprimirTurnoButton;
    @FXML
    public ComboBox<LysingInformation> cboxTramite;
    @FXML
    public AnchorPane panTramite;
    @FXML
    public Text lblTitulo;
    @FXML
    public Text lblProceso;

    public List<LysingInformation> getLysingInformations() {
        return lysingInformations;
    }

    public void setLysingInformations(List<LysingInformation> lysingInformations) {
        this.lysingInformations = lysingInformations;

        cboxTramite.setItems(FXCollections.observableList(lysingInformations));
    }

    public void initialize(){
        System.out.println("RequisitosController did initialize");

        cboxTramite.setConverter(new StringConverter<LysingInformation>() {
            @Override
            public String toString(LysingInformation lysingInformation) {
                if (lysingInformation != null) {
                    return lysingInformation.getTitle();
                }
                return "Seleccione un trámite";
            }

            @Override
            public LysingInformation fromString(String string) {
                return null;
            }
        });

        imgLogo.setImage(new Image(getClass().getClassLoader().getResourceAsStream(("logo.png"))));
        imgLogo.setCache(true);
    }

    public void didClickImprimirButton(ActionEvent actionEvent) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            //PageLayout pageLayout = printerJob.getPrinter().createPageLayout(Paper.A5, PageOrientation.PORTRAIT, 0, 0, 0, 0);

            WritableImage view = panTramite.snapshot(null, null);
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

    public void seleccionarTramite(ActionEvent actionEvent) {
        if (cboxTramite.getValue() != null) {
            lblTitulo.setText(cboxTramite.getValue().getTitle());
            lblProceso.setText(cboxTramite.getValue().getProcess());
        }
    }
}
