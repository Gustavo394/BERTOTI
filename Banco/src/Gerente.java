import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.List;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class Gerente{
    Conta conta = new Conta();

    public static Stage stage;
    
    @FXML private Pane tela;

    @FXML private AnchorPane sceneBotoes;
    static String nome;
    @FXML private Text textNome;
    @FXML private JFXButton btnAlterar;
    @FXML private JFXButton btnCadastrar;
    @FXML private JFXButton btnConsultar;
    @FXML private JFXButton btnHistorico;
    @FXML private JFXButton btnSair;

    @FXML private AnchorPane sceneCadastrar;
    @FXML private TextField fieldCadastrarNome;
    @FXML private TextField fieldCadastrarLogin;
    @FXML private TextField fieldCadastrarSenha;
    @FXML private JFXComboBox<String> fieldCadastrarTipo;
    @FXML private JFXButton btnSalvarCadastro;

    @FXML private AnchorPane sceneAlterar;
    @FXML private JFXComboBox<String> fieldAlterarLogin;
    @FXML private TextField fieldAlterarNome;
    @FXML private TextField fieldAlterarSenha;
    @FXML private Text textAlterarTipo;
    @FXML private JFXButton btnExcluirUsuario;
    @FXML private JFXButton btnSalvarAlteracao;

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
        btnCadastrar.setBackground(corFundoBotao);
        btnSalvarCadastro.setBackground(corFundoBotao);
        btnAlterar.setBackground(corFundoBotao);
        btnExcluirUsuario.setBackground(corFundoBotao);
        btnSalvarAlteracao.setBackground(corFundoBotao);
        btnConsultar.setBackground(corFundoBotao);
        btnHistorico.setBackground(corFundoBotao);
        fieldCadastrarTipo.setBackground(corFundoCombobox);
        fieldAlterarLogin.setBackground(corFundoCombobox);
        ids_contas.setBackground(corFundoCombobox);
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

        ObservableList<String> tipos = FXCollections.observableArrayList("comum", "atendente", "gerente");
        fieldCadastrarTipo.setItems(tipos);

        if (fieldAlterarLogin.getValue() == null){
            List<Cadastro.LoginsUsuarios> dadosLogins = dadosConta.dados();
            ObservableList<String> loginLista = FXCollections.observableArrayList();
    
            dadosLogins.forEach(dados -> {
                loginLista.add(dados.getLogin());
            });
            
            fieldAlterarLogin.setItems(loginLista);
        }

        if (id_conta == null){
            List<Conta.Consulta> dadosIds = consultaIds.ids_contas();
            ObservableList<Integer> idLista = FXCollections.observableArrayList();
    
            dadosIds.forEach(dados -> {
                idLista.add(dados.getId_Conta());
            });
            
            ids_contas.setItems(idLista);

        } else if (id_conta != null && anoConsulta == null){
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
    void btnCadastrar(ActionEvent event) {
        sceneAlterar.setDisable(true);
        sceneAlterar.setVisible(false);

        sceneConsultar.setDisable(true);
        sceneConsultar.setVisible(false);
        ids_contas.setValue(null);

        sceneHistorico.setDisable(true);
        sceneHistorico.setVisible(false);

        chartHistorico.setDisable(true);
        chartHistorico.setVisible(false);

        fieldCadastrarNome.clear();
        fieldCadastrarLogin.clear();
        fieldCadastrarSenha.clear();
        fieldCadastrarTipo.setValue(null);

        sceneCadastrar.setDisable(false);
        sceneCadastrar.setVisible(true);

    }
    

    @FXML
    void salvarCadastro(ActionEvent event) {
        Cadastro salvar = new Cadastro();

        if (fieldCadastrarNome.getText().isEmpty() || fieldCadastrarLogin.getText().isEmpty() || fieldCadastrarSenha.getText().isEmpty() || fieldCadastrarTipo.getValue() == null){
            tela.setDisable(true);
            Alerta.titulo = "Alerta";
            Alerta.texto = "Preencha todos os campos corretamente!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);

        }else if (!salvar.cadastrar(fieldCadastrarNome.getText(), fieldCadastrarLogin.getText(), fieldCadastrarSenha.getText(), fieldCadastrarTipo.getValue())) {
            tela.setDisable(true);
            Alerta.titulo = "Alerta";
            Alerta.texto = "A login informado já existe!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);

        }else {
            fieldCadastrarNome.clear();
            fieldCadastrarLogin.clear();
            fieldCadastrarSenha.clear();
            fieldCadastrarTipo.setValue(null);

            tela.setDisable(true);
            Alerta.titulo = "Certo";
            Alerta.texto = "Cadastro realizado com sucesso!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);

        }

    }

    @FXML
    void btnAlterar(ActionEvent event) {
        sceneCadastrar.setDisable(true);
        sceneCadastrar.setVisible(false);

        sceneConsultar.setDisable(true);
        sceneConsultar.setVisible(false);
        ids_contas.setValue(null);
        
        sceneHistorico.setDisable(true);
        sceneHistorico.setVisible(false);

        chartHistorico.setDisable(true);
        chartHistorico.setVisible(false);

        fieldAlterarLogin.setValue(null);
        fieldAlterarNome.clear();
        fieldAlterarSenha.clear();
        textAlterarTipo.setText("tipo");

        sceneAlterar.setDisable(false);
        sceneAlterar.setVisible(true);

        atualizar(null, null, null);

    }

    @FXML
    void fieldAlterarLogin(ActionEvent event) {
        if (fieldAlterarLogin.getValue() != null){
            Cadastro dados = new Cadastro();
            Cadastro.DadosUsuario resultadoUsuario = dados.usuario(fieldAlterarLogin.getValue());
            fieldAlterarNome.setText(resultadoUsuario.getNome());
            fieldAlterarSenha.setText(resultadoUsuario.getSenha());
            textAlterarTipo.setText(resultadoUsuario.getTipo());

        }
    }

    @FXML
    void salvarAlteracao(ActionEvent event) {
        Cadastro alterar = new Cadastro();
        if (fieldAlterarLogin.getValue() == null || fieldAlterarNome.getText().isEmpty() || fieldAlterarSenha.getText().isEmpty()){
            tela.setDisable(true);
            Alerta.titulo = "Alerta";
            Alerta.texto = "Preencha todos os campos corretamente!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);

        }else if (fieldAlterarLogin.getValue() != null && !alterar.alterar(fieldAlterarLogin.getValue(), fieldAlterarNome.getText(), fieldAlterarSenha.getText())){
            tela.setDisable(true);
            Alerta.titulo = "Alerta";
            Alerta.texto = "Não é possível alterar o cadastro!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);
        } else {
            fieldAlterarLogin.setValue(null);
            fieldAlterarNome.clear();
            fieldAlterarSenha.clear();
            textAlterarTipo.setText("tipo");

            tela.setDisable(true);
            Alerta.titulo = "Certo";
            Alerta.texto = "Alteração realizada com sucesso!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);
        }

    }

    @FXML
    void excluirUsuario(ActionEvent event) {
        Cadastro excluir = new Cadastro();
        if (fieldAlterarLogin.getValue() != null && excluir.excluir(fieldAlterarLogin.getValue())){
            fieldAlterarLogin.setValue(null);
            fieldAlterarNome.clear();
            fieldAlterarSenha.clear();
            textAlterarTipo.setText("tipo");

            atualizar(null, null, null);

            tela.setDisable(true);
            Alerta.titulo = "Certo";
            Alerta.texto = "Usuario deletado com sucesso!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);

        }else{
            tela.setDisable(true);
            Alerta.titulo = "Alerta";
            Alerta.texto = "Não é possível deletar o cadastro!\nPode ser que haja saldo na conta do usuario!";
            Alerta alerta = new Alerta();
            alerta.aviso();
            tela.setDisable(false);
        }
    }

    @FXML
    void btnConsultar(ActionEvent event) {
        sceneCadastrar.setDisable(true);
        sceneCadastrar.setVisible(false);

        sceneAlterar.setDisable(true);
        sceneAlterar.setVisible(false);

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
        sceneCadastrar.setDisable(true);
        sceneCadastrar.setVisible(false);
        
        sceneAlterar.setDisable(true);
        sceneAlterar.setVisible(false);

        sceneConsultar.setDisable(true);
        sceneConsultar.setVisible(false);

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

    public void gerente(Stage stage){
        Main main = new Main();
        main.janela("/resources/gerente.fxml", stage);
    }
}
