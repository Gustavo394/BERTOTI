import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.PasswordField;
import javafx.scene.control.CheckBox;

public class Main extends Application{

    static String banco = "jdbc:postgresql://localhost:5432/banco";
    static String usuario = "postgres";
    static String senha = "@1233";

    public static Stage stage;
    
    @FXML private Pane tela;
    @FXML private CheckBox ver;
    @FXML private TextField labelLogin;
    @FXML private PasswordField labelSenha;

    @FXML private JFXButton btnEntrar;

    Conta conta = new Conta();

    @FXML
    void initialize(){
        BackgroundFill corTela = new BackgroundFill(Color.PURPLE, null, null);
        BackgroundFill corBotao = new BackgroundFill(Color.PERU, null, null);
        Background corFundoTela = new Background(corTela);
        Background corFundoBotao = new Background(corBotao);
        tela.setBackground(corFundoTela);
        btnEntrar.setBackground(corFundoBotao);
    }

    @Override
    public void start(Stage primaryStage){
        janela("/resources/login.fxml", primaryStage);
    }

    public void janela(String fxml, Stage primaryStage){
        try{
            stage = primaryStage;
            primaryStage.setTitle("Banco");
            Parent loader = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(loader);

            Image icon = new Image(getClass().getResourceAsStream("/resources/icone.png"));
            primaryStage.getIcons().add(icon);

            primaryStage.sizeToScene();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            double x = (screenBounds.getWidth() - loader.prefWidth(-1)) / 2;
            double y = (screenBounds.getHeight() - loader.prefHeight(-1)) / 2;
            primaryStage.setX(x);
            primaryStage.setY(y);

            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.out.println(e);
            tela.setDisable(true);
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);
        }
    }

    @FXML
    void btnEntrar(ActionEvent event) {
        try {
            if (ver.isSelected()){
                labelSenha.setText(labelSenha.getPromptText());
                labelSenha.setEditable(true);
                labelSenha.setPromptText(null);
                ver.setSelected(false);
            }
            if (labelLogin.getText().isEmpty() || labelSenha.getText().isEmpty()){
                tela.setDisable(true);
                Alerta.titulo = "Alerta";
                Alerta.texto = "Preencha os campos corretamente!";
                Alerta alerta = new Alerta();
                alerta.aviso();
                tela.setDisable(false);
    
            } else {
                Cadastro validar = new Cadastro();
                Cadastro.DadosLogin resultado = validar.login(labelLogin.getText().toString(), labelSenha.getText().toString());
                
                if (resultado.getStatus() != null){
                    if (resultado.getStatus()) {
                        if (resultado.getTipo().equals("comum")){
                            Comum tipo = new Comum();
                            Comum.nome = resultado.getNome();
                            Comum.id_conta = Integer.parseInt(resultado.getId_Conta());
                            tipo.comum(stage);

                        } else if (resultado.getTipo().equals("atendente")){
                            Atendente tipo = new Atendente();
                            Atendente.nome = resultado.getNome();
                            tipo.atendente(stage);

                        } else if (resultado.getTipo().equals("gerente")){
                            Gerente tipo = new Gerente();
                            Gerente.nome = resultado.getNome();
                            tipo.gerente(stage);

                        } else {
                            tela.setDisable(true);
                            Alerta.titulo = "Alerta";
                            Alerta.texto = "Cargo não identificado!";
                            Alerta alerta = new Alerta();
                            alerta.aviso();
                            tela.setDisable(false);
                        }
                        
                    } else {
                        tela.setDisable(true);
                        Alerta.titulo = "Alerta";
                        Alerta.texto = "Login ou senha inválidos!";
                        Alerta alerta = new Alerta();
                        alerta.aviso();
                        tela.setDisable(false);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            tela.setDisable(true);
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);
        }
    }

    @FXML
    void ver(ActionEvent event) {
        if (ver.isSelected() && !labelSenha.getText().isEmpty()){
            labelSenha.setPromptText(labelSenha.getText());
            labelSenha.setEditable(false);
            labelSenha.setText(null);

        } else if (!ver.isSelected() && !labelSenha.getPromptText().isEmpty()) {
            labelSenha.setText(labelSenha.getPromptText());
            labelSenha.setEditable(true);
            labelSenha.setPromptText(null);
            
        } else {
            ver.setSelected(false);
        }
    }

    public static void changeScreen(String scr){
        Main main = new Main();
        switch (scr){
            case "sair":
                main.janela("/resources/login.fxml", stage);
                break;
        }
    }

    public static void main (String[] args){
        launch();
    }
}