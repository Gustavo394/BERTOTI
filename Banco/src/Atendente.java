import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

public class Atendente{
    Conta conta = new Conta();

    public static Stage stage;
    
    @FXML private Pane tela;

    @FXML private AnchorPane sceneBotoes;
    static String nome;
    @FXML private Text textNome;
    @FXML private JFXButton btnConsultar;
    @FXML private JFXButton btnHistorico;
    @FXML private JFXButton btnSair;

    @FXML private AnchorPane sceneConsultar;
    @FXML private JFXComboBox<Integer> ids_contas;
    @FXML private Text contaLogin;
    @FXML private Text contaNome;
    @FXML private Text contaSaldo;

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
        BackgroundFill corTela = new BackgroundFill(Color.PURPLE, null, null);
        BackgroundFill corBotao = new BackgroundFill(Color.PERU, null, null);
        BackgroundFill corFundo = new BackgroundFill(Color.WHITE, null, null);
        Background corFundoTela = new Background(corTela);
        Background corFundoBotao = new Background(corBotao);
        Background corFundoCombobox = new Background(corFundo);
        tela.setBackground(corFundoTela);
        btnConsultar.setBackground(corFundoBotao);
        ids_contas.setBackground(corFundoCombobox);
        btnHistorico.setBackground(corFundoBotao);
        btnHistorico.setBackground(corFundoBotao);
        btnTabelaHistorico.setBackground(corFundoBotao);
        historicoMes.setBackground(corFundoCombobox);
        historicoAno.setBackground(corFundoCombobox);
        btnGraficoHistorico.setBackground(corFundoBotao);
        
        btnSair.setBackground(corFundoBotao);

        atualizar(null, null, null);
    }

    public void atualizar(Integer id_conta,String anoConsulta, String mesConsulta){
        
        textNome.setText(nome);
        Conta consultaIds = new Conta();
        Cadastro dadosConta = new Cadastro();
        Movimentacao historico = new Movimentacao();

        if (ids_contas.getValue() == null){
            List<Conta.Consulta> dadosIds = consultaIds.ids_contas();
            ObservableList<Integer> idLista = FXCollections.observableArrayList();
    
            dadosIds.forEach(dados -> {
                idLista.add(dados.getId_Conta());
            });
            
            ids_contas.setItems(idLista);

        } else if (ids_contas.getValue() != null && historicoAno.getValue() == null){
            Cadastro.Consulta resultadoIds = dadosConta.dados(id_conta);
            contaLogin.setText(resultadoIds.getLogin());
            contaNome.setText(resultadoIds.getNome());
            contaSaldo.setText(resultadoIds.getSaldo().toString());
        }

        if (id_conta != null){
            List<Movimentacao.Consulta> resultado = historico.consulta(id_conta);
            ObservableList<Movimentacao.Consulta> arrayHistorico = FXCollections.observableArrayList();
            arrayHistorico.addAll(resultado);
    
            id_historico.setCellValueFactory(new PropertyValueFactory<>("id"));
            data_historico.setCellValueFactory(new PropertyValueFactory<>("data"));
            valor_historico.setCellValueFactory(new PropertyValueFactory<>("valor"));
            tipo_historico.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    
            tabelaHistorico.setItems(arrayHistorico);
    
            chartHistorico.getData().clear();
    
            List<Movimentacao.ConsultaMensal> dadosMensais = historico.mensal(id_conta, anoConsulta, mesConsulta);
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
    }

    @FXML
    void btnConsultar(ActionEvent event) {
        sceneHistorico.setDisable(true);
        sceneHistorico.setVisible(false);

        sceneConsultar.setDisable(false);
        sceneConsultar.setVisible(true);

        historicoAno.setValue(null);
        historicoAno.setValue(null);

    }

    @FXML
    void ids_contas(ActionEvent event) {
        if (ids_contas.getValue() != null){
            atualizar(ids_contas.getValue(), null, null);

        }
    }

    @FXML
    void btnHistorico(ActionEvent event) {
        sceneConsultar.setDisable(true);
        sceneConsultar.setVisible(false);

        chartHistorico.setDisable(true);
        chartHistorico.setVisible(false);

        historicoAno.setDisable(true);
        historicoAno.setVisible(false);
        historicoAno.setValue(null);

        historicoMes.setDisable(true);
        historicoMes.setVisible(false);
        historicoAno.setValue(null);

        legendaChartHistorico.setDisable(true);
        legendaChartHistorico.setVisible(false);

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
            atualizar(ids_contas.getValue(), historicoAno.getValue(), null);
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
            atualizar(ids_contas.getValue(), historicoAno.getValue(), mes);
        }
    }

    @FXML
    void btnSair(ActionEvent event) {
        Main.changeScreen("sair");

    }

    public void atendente(Stage stage){
        Main main = new Main();
        main.janela("/resources/atendente.fxml", stage);
    }
}
