import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

public class Comum{
    Conta conta = new Conta();

    public static Stage stage;
    
    @FXML private Pane tela;

    @FXML private AnchorPane sceneBotoes;
    static String nome;
    @FXML private Text textNome;
    static Integer id_conta;
    @FXML private Text textIdConta;
    @FXML private JFXButton btnSaldo;
    @FXML private JFXButton btnSacar;
    @FXML private JFXButton btnDepositar;
    @FXML private JFXButton btnRenda;
    @FXML private JFXButton btnSair;
    @FXML private JFXButton btnHistorico;
    
    @FXML private AnchorPane sceneSaldo;
    @FXML private Label labelSaldo;

    @FXML private AnchorPane sceneSacar;
    @FXML private TextField fieldSaque;
    @FXML private JFXButton btnRealizarSaque;

    @FXML private TextField fieldDeposito;
    @FXML private AnchorPane sceneDepositar;
    @FXML private JFXButton btnRealizarDeposito;

    @FXML private AnchorPane sceneRenda;
    @FXML private Label labelRendaMensal;
    @FXML private Label labelRendaAnual;
    @FXML private LineChart<String, Double> chartRenda;

    @FXML private AnchorPane sceneHistorico;
    @FXML private JFXButton btnTabelaHistorico;
    @FXML private TableView<Movimentacao.Consulta> tabelaHistorico;
    @FXML private TableColumn<Movimentacao.Consulta, Integer> id_historico;
    @FXML private TableColumn<Movimentacao.Consulta, String> data_historico;
    @FXML private TableColumn<Movimentacao.Consulta, String> valor_historico;
    @FXML private TableColumn<Movimentacao.Consulta, String> tipo_historico;
    @FXML private JFXButton btnGraficoHistorico;
    @FXML private JFXComboBox<String> historicoAno;
    @FXML private JFXComboBox<String> historicoMes;
    @FXML private BarChart<String, Double> chartHistorico;
    @FXML private Text legendaChartHistorico;

    @FXML
    void initialize(){
        textIdConta.setText(id_conta.toString());
        BackgroundFill corTela = new BackgroundFill(Color.PURPLE, null, null);
        BackgroundFill corBotao = new BackgroundFill(Color.PERU, null, null);
        BackgroundFill corFundo = new BackgroundFill(Color.WHITE, null, null);
        Background corFundoTela = new Background(corTela);
        Background corFundoBotao = new Background(corBotao);
        Background corFundoCombobox = new Background(corFundo);
        tela.setBackground(corFundoTela);
        btnSaldo.setBackground(corFundoBotao);
        btnSacar.setBackground(corFundoBotao);
        btnRealizarSaque.setBackground(corFundoBotao);
        btnDepositar.setBackground(corFundoBotao);
        btnRealizarDeposito.setBackground(corFundoBotao);
        btnRenda.setBackground(corFundoBotao);
        btnHistorico.setBackground(corFundoBotao);
        btnTabelaHistorico.setBackground(corFundoBotao);
        historicoMes.setBackground(corFundoCombobox);
        historicoAno.setBackground(corFundoCombobox);
        btnGraficoHistorico.setBackground(corFundoBotao);
        
        btnSair.setBackground(corFundoBotao);

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
        atualizar(null, null);
    }

    public void atualizar(String anoConsulta, String mesConsulta){
        chartRenda.getData().clear();
        
        textNome.setText(nome);
        labelSaldo.setText(String.valueOf(conta.saldo(Integer.valueOf(textIdConta.getText()))));
        labelRendaMensal.setText(String.valueOf(conta.rendaMensal(Integer.valueOf(textIdConta.getText()))));
        labelRendaAnual.setText(String.valueOf(conta.rendaAnual(Integer.valueOf(textIdConta.getText()))));

        LocalDateTime dataAtual = LocalDateTime.now();
        LocalDate dataMovimentacao = dataAtual.toLocalDate();
        Integer mes = dataMovimentacao.getMonthValue();

        String mesNome = "";

        Double saldo = conta.saldo(Integer.valueOf(textIdConta.getText()));

        XYChart.Series<String, Double> dataSeriesRenda = new XYChart.Series<>();
        for (int x = 1; x <= 12; x++){
            switch (mes){
                case 1: mesNome = "Janeiro"; break;
                case 2: mesNome = "Fevereiro"; break;
                case 3: mesNome = "Março"; break;
                case 4: mesNome = "Abril"; break;
                case 5: mesNome = "Maio"; break;
                case 6: mesNome = "Junho"; break;
                case 7: mesNome = "Julho"; break;
                case 8: mesNome = "Agosto"; break;
                case 9: mesNome = "Setembro"; break;
                case 10: mesNome = "Outubro"; break;
                case 11: mesNome = "Novembro"; break;
                case 12: mesNome = "Dezembro"; mes = 0; break;
            }
            mes += 1;
            saldo += saldo*0.0117;
            dataSeriesRenda.getData().add(new XYChart.Data<>(mesNome, saldo));
        }

        chartRenda.getData().add(dataSeriesRenda);

        Movimentacao historico = new Movimentacao();
        List<Movimentacao.Consulta> resultado = historico.consulta(Comum.id_conta);
        ObservableList<Movimentacao.Consulta> arrayHistorico = FXCollections.observableArrayList();
        arrayHistorico.addAll(resultado);

        id_historico.setCellValueFactory(new PropertyValueFactory<>("id"));
        data_historico.setCellValueFactory(new PropertyValueFactory<>("data"));
        valor_historico.setCellValueFactory(new PropertyValueFactory<>("valor"));
        tipo_historico.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        tabelaHistorico.setItems(arrayHistorico);

        chartHistorico.getData().clear();

        List<Movimentacao.ConsultaMensal> dadosMensais = historico.mensal(Comum.id_conta, anoConsulta, mesConsulta);
        XYChart.Series<String, Double> dataSeriesSaque = new XYChart.Series<>();
        XYChart.Series<String, Double> dataSeriesDeposito = new XYChart.Series<>();
        ObservableList<String> anoLista = FXCollections.observableArrayList();
        ObservableList<String> mesLista = FXCollections.observableArrayList();

        dadosMensais.forEach(dados -> {
            
            if (historicoAno.getValue() == null){
                anoLista.add(dados.getAno().toString());
                dataSeriesSaque.getData().add(new XYChart.Data<>(dados.getAno(), dados.getValorSaque()));
                dataSeriesDeposito.getData().add(new XYChart.Data<>(dados.getAno(), dados.getValorDeposito()));

            } else if (historicoAno.getValue() != null && historicoMes.getValue() == null){
                String nomeMes = null;

                switch (dados.getMes()){
                    case 1: nomeMes = "Janeiro"; break;
                    case 2: nomeMes = "Fevereiro"; break;
                    case 3: nomeMes = "Março"; break;
                    case 4: nomeMes = "Abril"; break;
                    case 5: nomeMes = "Maio"; break;
                    case 6: nomeMes = "Junho"; break;
                    case 7: nomeMes = "Julho"; break;
                    case 8: nomeMes = "Agosto"; break;
                    case 9: nomeMes = "Setembro"; break;
                    case 10: nomeMes = "Outubro"; break;
                    case 11: nomeMes = "Novembro"; break;
                    case 12: nomeMes = "Dezembro"; break;
                }

                mesLista.add(nomeMes);

                dataSeriesSaque.getData().add(new XYChart.Data<>(nomeMes, dados.getValorSaque()));
                dataSeriesDeposito.getData().add(new XYChart.Data<>(nomeMes, dados.getValorDeposito()));

            } else if (historicoMes.getValue() != null){
                dataSeriesSaque.getData().add(new XYChart.Data<>(dados.getDia(), dados.getValorSaque()));
                dataSeriesDeposito.getData().add(new XYChart.Data<>(dados.getDia(), dados.getValorDeposito()));
            }
        });

        if (historicoAno.getValue() == null){
            historicoAno.setItems(anoLista);
        }
        
        if (historicoAno.getValue() != null && historicoMes.getValue() == null){
            historicoMes.setItems(mesLista);
        }

        chartHistorico.getData().add(dataSeriesSaque);
        chartHistorico.getData().add(dataSeriesDeposito);
    }
    
    @FXML
    void btnSaldo(ActionEvent event) {
        fieldSaque.clear();
        fieldDeposito.clear();

        sceneSacar.setDisable(true);
        sceneSacar.setVisible(false);

        sceneDepositar.setDisable(true);
        sceneDepositar.setVisible(false);

        sceneRenda.setDisable(true);
        sceneRenda.setVisible(false);
        
        sceneHistorico.setDisable(true);
        sceneHistorico.setVisible(false);

        sceneSaldo.setDisable(false);
        sceneSaldo.setVisible(true);

    }

    @FXML
    void btnSacar(ActionEvent event) {
        fieldSaque.clear();

        sceneDepositar.setDisable(true);
        sceneDepositar.setVisible(false);

        sceneRenda.setDisable(true);
        sceneRenda.setVisible(false);
        
        sceneHistorico.setDisable(true);
        sceneHistorico.setVisible(false);

        sceneSaldo.setDisable(false);
        sceneSaldo.setVisible(true);
        
        sceneSacar.setDisable(false);
        sceneSacar.setVisible(true);

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
            conta.sacar(Integer.valueOf(textIdConta.getText()), Double.valueOf(fieldSaque.getText()));
            labelSaldo.setText(String.valueOf(conta.saldo(Integer.valueOf(textIdConta.getText()))));
            fieldSaque.clear();
        }
        atualizar(null, null);
    }

    @FXML
    void btnDepositar(ActionEvent event) {
        fieldDeposito.clear();
        
        sceneSacar.setDisable(true);
        sceneSacar.setVisible(false);

        sceneRenda.setDisable(true);
        sceneRenda.setVisible(false);
        
        sceneHistorico.setDisable(true);
        sceneHistorico.setVisible(false);
        
        sceneSaldo.setDisable(false);
        sceneSaldo.setVisible(true);

        sceneDepositar.setDisable(false);
        sceneDepositar.setVisible(true);

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
            conta.depositar(Integer.valueOf(textIdConta.getText()), Double.valueOf(fieldDeposito.getText()));
            labelSaldo.setText(String.valueOf(conta.saldo(Integer.valueOf(textIdConta.getText()))));
            fieldDeposito.clear();
        }
        atualizar(null, null);
    }

    @FXML
    void btnRenda(ActionEvent event) {
        sceneSacar.setDisable(true);
        sceneSacar.setVisible(false);

        sceneDepositar.setDisable(true);
        sceneDepositar.setVisible(false);
        
        sceneHistorico.setDisable(true);
        sceneHistorico.setVisible(false);
        
        chartHistorico.setDisable(true);
        chartHistorico.setVisible(false);

        sceneSaldo.setDisable(false);
        sceneSaldo.setVisible(true);

        sceneRenda.setDisable(false);
        sceneRenda.setVisible(true);

    }
    
    @FXML
    void btnHistorico(ActionEvent event) {
        sceneSaldo.setDisable(true);
        sceneSaldo.setVisible(false);
        
        sceneSacar.setDisable(true);
        sceneSacar.setVisible(false);

        sceneDepositar.setDisable(true);
        sceneDepositar.setVisible(false);

        sceneRenda.setDisable(true);
        sceneRenda.setVisible(false);

        chartHistorico.setDisable(true);
        chartHistorico.setVisible(false);

        historicoAno.setDisable(true);
        historicoAno.setVisible(false);
        historicoAno.setValue(null);

        historicoMes.setDisable(true);
        historicoMes.setVisible(false);
        historicoMes.setValue(null);

        legendaChartHistorico.setDisable(true);
        legendaChartHistorico.setVisible(false);

        atualizar(null, null);

        sceneHistorico.setDisable(false);
        sceneHistorico.setVisible(true);

        tabelaHistorico.setDisable(false);
        tabelaHistorico.setVisible(true);

    }

    @FXML
    void btnTabelaHistorico(ActionEvent event) {
        chartHistorico.setDisable(true);
        chartHistorico.setVisible(false);

        historicoAno.setDisable(true);
        historicoAno.setVisible(false);
        historicoAno.setValue(null);

        historicoMes.setDisable(true);
        historicoMes.setVisible(false);
        historicoMes.setValue(null);

        legendaChartHistorico.setDisable(true);
        legendaChartHistorico.setVisible(false);

        atualizar(null, null);

        tabelaHistorico.setDisable(false);
        tabelaHistorico.setVisible(true);
        
    }

    @FXML
    void btnGraficoHistorico(ActionEvent event) {
        tabelaHistorico.setDisable(true);
        tabelaHistorico.setVisible(false);

        legendaChartHistorico.setDisable(false);
        legendaChartHistorico.setVisible(true);

        chartHistorico.setDisable(false);
        chartHistorico.setVisible(true);

        historicoAno.setDisable(false);
        historicoAno.setVisible(true);

        historicoMes.setVisible(true);

    }

    @FXML
    void historicoAno(ActionEvent event) {
        if (historicoAno.getValue() != null) {
            historicoMes.setValue(null);
            historicoMes.setDisable(false);
            atualizar(historicoAno.getValue(), null);
        }
    }

    @FXML
    void historicoMes(ActionEvent event) {
        if (historicoMes.getValue() != null) {
            String mes = null;
            switch (historicoMes.getValue()){
                case "Janeiro": mes = "01"; break;
                case "Fevereiro": mes = "02"; break;
                case "Março": mes = "03"; break;
                case "Abril": mes = "04"; break;
                case "Maio": mes = "05"; break;
                case "Junho": mes = "06"; break;
                case "Julho": mes = "07"; break;
                case "Agosto": mes = "08"; break;
                case "Setembro": mes = "09"; break;
                case "Outubro": mes = "10"; break;
                case "Novembro": mes = "11"; break;
                case "Dezembro": mes = "12"; break;
            }
            atualizar(historicoAno.getValue(), mes);
        }
    }

    @FXML
    void btnSair(ActionEvent event) {
        Main.changeScreen("sair");

    }

    public void comum(Stage stage){
        Main main = new Main();
        main.janela("/resources/comum.fxml", stage);
    }
}