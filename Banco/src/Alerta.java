import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Alerta {
    @FXML private ImageView imagemAviso;
    @FXML private Text textoAviso;

    static String titulo = null;
    static String texto = null;
    
    private Stage telaAlerta = new Stage();


    public void setStage(Stage stage) {
        this.telaAlerta = stage;
    }

    @FXML
    public void initialize(){
        textoAviso.setText(texto);
        Image simbolo = new Image(getClass().getResourceAsStream("/resources/" + Alerta.titulo + ".png"));
        imagemAviso.setImage(simbolo);
    }

    public void aviso(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/alerta.fxml"));
            Parent root = loader.load();
            Alerta controlador = loader.getController();
            controlador.setStage(telaAlerta);
            Scene scene = new Scene(root);
            
            Image icone = new Image(getClass().getResourceAsStream("/resources/icone.png"));
            telaAlerta.getIcons().add(icone);

            telaAlerta.sizeToScene();
            telaAlerta.setTitle(titulo);
            
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            
            double x = (screenBounds.getWidth() - root.prefWidth(-1)) / 2;
            double y = (screenBounds.getHeight() - root.prefHeight(-1)) / 2;
            telaAlerta.setX(x);
            telaAlerta.setY(y);
    
            telaAlerta.setResizable(false);
            telaAlerta.setScene(scene);
            telaAlerta.showAndWait();

        }catch(IOException e){
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Erro");
            alerta.setHeaderText(null);
            alerta.setContentText("Erro " + e);

            Image icone = new Image(getClass().getResourceAsStream("/resources/erro.png"));

            ImageView imageView = new ImageView(icone);
            imageView.setFitHeight(32);
            imageView.setFitWidth(32);
            DialogPane dialogPane = alerta.getDialogPane();
            dialogPane.setGraphic(imageView);

            alerta.showAndWait();
        }
    }

    @FXML
    private void ok(ActionEvent event){
        telaAlerta.close();

    }
}