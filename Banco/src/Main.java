import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.control.PasswordField;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;
import java.util.Locale;
import java.sql.*;

public class Main extends Application{

    static String banco = "jdbc:postgresql://localhost:5432/banco";
    static String usuario = "postgres";
    static String senha = "@1233";

    public static Stage stage;
    
    @FXML private Pane tela;

    @FXML private AnchorPane sceneLogin;
    @FXML private TextField labelLogin;
    @FXML private PasswordField labelSenha;

    @FXML private Text textNome;
    @FXML private Text textConta;
    @FXML private AnchorPane sceneBotoes;
    @FXML private AnchorPane sceneSaldo;
    @FXML private AnchorPane sceneDepositar;
    @FXML private AnchorPane sceneRenda;

    @FXML private AnchorPane sceneSacar;
    @FXML private Label labelSaldo;
    @FXML private TextField fieldSaque;
    @FXML private TextField fieldDeposito;
    @FXML private Label labelRenda;

    @FXML
    void initialize(){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.##", symbols);

        fieldSaque.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            ParsePosition parsePosition = new ParsePosition(0);
            Number parsedValue = decimalFormat.parse(newText, parsePosition);

            int decimalIndex = newText.indexOf('.');
            if (newText.isEmpty() || parsedValue != null && parsePosition.getIndex() == newText.length()
                    && parsePosition.getErrorIndex() == -1 && (decimalIndex == -1 || newText.length() - decimalIndex <= 3)) {
                return change;
            }

            return null;
        }));

        fieldDeposito.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            ParsePosition parsePosition = new ParsePosition(0);
            Number parsedValue = decimalFormat.parse(newText, parsePosition);

            int decimalIndex = newText.indexOf('.');
            if (newText.isEmpty() || parsedValue != null && parsePosition.getIndex() == newText.length()
                    && parsePosition.getErrorIndex() == -1 && (decimalIndex == -1 || newText.length() - decimalIndex <= 3)) {
                return change;
            }

            return null;
        }));
    }

    @Override
    public void start(Stage primaryStage){

        try{
            stage = primaryStage;
            primaryStage.setTitle("Banco");
            Parent loader = FXMLLoader.load(getClass().getResource("/resources/tela.fxml"));
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
            tela.setDisable(true);
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);
        }
    }

    @FXML
    void btnLogar(ActionEvent event) {
        try {
            if (labelLogin.getText().isEmpty() || labelSenha.getText().isEmpty()){
                tela.setDisable(true);
                Alerta.titulo = "Alerta";
                Alerta.texto = "Preencha os campos corretamente!";
                Alerta alerta = new Alerta();
                alerta.aviso();
                tela.setDisable(false);
    
            } else {
                Connection conexao = DriverManager.getConnection(banco, usuario, senha);
                PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM usuario WHERE login = ? AND senha = ?");
                stmt.setString(1, labelLogin.getText().toString());
                stmt.setString(2, labelSenha.getText().toString());
                ResultSet rs = stmt.executeQuery();
    
                if (rs.next()){
                    textNome.setText(rs.getString("nome"));
                    
                    PreparedStatement stmtConta = conexao.prepareStatement("SELECT * FROM conta WHERE login = ?");
                    stmtConta.setString(1, labelLogin.getText().toString());
                    ResultSet rsConta = stmtConta.executeQuery();

                    while (rsConta.next()){
                        textConta.setText(Integer.valueOf(rsConta.getInt("id_conta")).toString());
                    }
                    
                    conexao.close();
                    stmt.close();
                    rs.close();
                    stmtConta.close();
                    rsConta.close();

                    labelLogin.clear();
                    labelSenha.clear();
                    
                    sceneLogin.setDisable(true);
                    sceneLogin.setVisible(false);
        
                    sceneBotoes.setDisable(false);
                    sceneBotoes.setVisible(true);
                    
                } else {
                    tela.setDisable(true);
                    Alerta.titulo = "Alerta";
                    Alerta.texto = "Login ou senha inválidos!";
                    Alerta alerta = new Alerta();
                    alerta.aviso();
                    tela.setDisable(false);
                }
            }
        } catch (Exception e) {
            tela.setDisable(true);
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);
        }
    }

    @FXML
    void btnSaldo(ActionEvent event) {
        Banco conta = new Banco();
        labelSaldo.setText(String.valueOf(conta.saldo(Integer.valueOf(textConta.getText()))));

        fieldSaque.clear();
        fieldDeposito.clear();

        sceneSaldo.setDisable(false);
        sceneSaldo.setVisible(true);

        sceneSacar.setDisable(true);
        sceneSacar.setVisible(false);

        sceneDepositar.setDisable(true);
        sceneDepositar.setVisible(false);

        sceneRenda.setDisable(true);
        sceneRenda.setVisible(false);
    }

    @FXML
    void btnSacar(ActionEvent event) {
        Banco conta = new Banco();
        labelSaldo.setText(String.valueOf(conta.saldo(Integer.valueOf(textConta.getText()))));

        fieldDeposito.clear();

        sceneSaldo.setDisable(false);
        sceneSaldo.setVisible(true);
        
        sceneSacar.setDisable(false);
        sceneSacar.setVisible(true);

        sceneDepositar.setDisable(true);
        sceneDepositar.setVisible(false);

        sceneRenda.setDisable(true);
        sceneRenda.setVisible(false);
    }

    @FXML
    void btnRealizarSaque(ActionEvent event) {
        if (fieldSaque.getText().isEmpty()){
            tela.setDisable(true);
            Alerta.titulo = "Alerta";
            Alerta.texto = "Informe um valor para sacar!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);
            
        } else {
            Banco conta = new Banco();
            conta.sacar(Integer.valueOf(textConta.getText()), Double.valueOf(fieldSaque.getText()));
            labelSaldo.setText(String.valueOf(conta.saldo(Integer.valueOf(textConta.getText()))));
            fieldSaque.clear();
        }
    }

    @FXML
    void btnDepositar(ActionEvent event) {
        Banco conta = new Banco();
        labelSaldo.setText(String.valueOf(conta.saldo(Integer.valueOf(textConta.getText()))));
        
        fieldSaque.clear();
        
        sceneSaldo.setDisable(false);
        sceneSaldo.setVisible(true);
        
        sceneSacar.setDisable(true);
        sceneSacar.setVisible(false);

        sceneDepositar.setDisable(false);
        sceneDepositar.setVisible(true);

        sceneRenda.setDisable(true);
        sceneRenda.setVisible(false);
    }

    @FXML
    void btnRealizarDeposito(ActionEvent event) {
        if (fieldDeposito.getText().isEmpty()){
            tela.setDisable(true);
            Alerta.titulo = "Alerta";
            Alerta.texto = "Informe um valor para depositar!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);

        } else {
            Banco conta = new Banco();
            conta.depositar(Integer.valueOf(textConta.getText()), Double.valueOf(fieldDeposito.getText()));
            labelSaldo.setText(String.valueOf(conta.saldo(Integer.valueOf(textConta.getText()))));
            fieldDeposito.clear();

        }
    }

    @FXML
    void btnRenda(ActionEvent event) {
        Banco conta = new Banco();
        labelRenda.setText(String.valueOf(conta.renda(Integer.valueOf(textConta.getText()))));

        sceneSaldo.setDisable(true);
        sceneSaldo.setVisible(false);
        
        sceneSacar.setDisable(true);
        sceneSacar.setVisible(false);

        sceneDepositar.setDisable(true);
        sceneDepositar.setVisible(false);

        sceneRenda.setDisable(false);
        sceneRenda.setVisible(true);
    }

    @FXML
    void btnDeslogar(ActionEvent event) {
        sceneLogin.setDisable(false);
        sceneLogin.setVisible(true);

        sceneSaldo.setDisable(true);
        sceneSaldo.setVisible(false);

        sceneSacar.setDisable(true);
        sceneSacar.setVisible(false);

        sceneDepositar.setDisable(true);
        sceneDepositar.setVisible(false);

        sceneRenda.setDisable(true);
        sceneRenda.setVisible(false);

        sceneBotoes.setDisable(true);
        sceneBotoes.setVisible(false);
    }
    
    public static void main (String[] args){
        launch();
    }
}