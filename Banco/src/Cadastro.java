import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Cadastro{
    public class DadosLogin{
        private Boolean status;
        private String nome;
        private String id_conta;
        private String tipo;

        public DadosLogin(Boolean status, String nome, String id_conta, String tipo) {
            this.status = status;
            this.nome = nome;
            this.id_conta = id_conta;
            this.tipo = tipo;
        }

        public Boolean getStatus() { return status; }

        public String getNome() { return nome; }

        public String getId_Conta() { return id_conta; }

        public String getTipo() { return tipo; }
    }

    String nome = "";
    String id_conta = "";
    String tipo = "";

    public DadosLogin login(String login, String senha){
        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM usuario WHERE login = ? AND senha = ?");
            stmt.setString(1, login);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                nome = rs.getString("nome");
                tipo = rs.getString("tipo");

                PreparedStatement stmtConta = conexao.prepareStatement("SELECT * FROM conta WHERE login = ?");
                stmtConta.setString(1, login);
                ResultSet rsConta = stmtConta.executeQuery();

                if (rsConta.next()){
                    id_conta = rsConta.getString("id_conta");
                }

                conexao.close();
                stmt.close();
                rs.close();
                stmtConta.close();
                rsConta.close();
                
                return new DadosLogin(true, nome, id_conta, tipo);

            } else {
                return new DadosLogin(false, "", "", "");
            }
        } catch (Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
            return new DadosLogin(null, "", "", "");
        }
    }

    public class Consulta{

        private String login;
        private String nome;
        private Double saldo;

        public Consulta(String login, String nome, Double saldo){
            this.login = login;
            this.nome = nome;
            this.saldo = saldo;

        }

        public String getLogin(){ return login; }
        public String getNome(){ return nome; }
        public Double getSaldo(){ return saldo; }

    }

    public Consulta dados(Integer id_conta){
        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("SELECT " +
            "usuario.login AS login, usuario.nome AS nome, saldo FROM conta JOIN usuario ON conta.login = usuario.login WHERE id_conta = ?");
            stmt.setInt(1, id_conta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                String login = rs.getString("login");
                String nome = rs.getString("nome");
                Double saldo = rs.getDouble("saldo");

                return new Consulta(login, nome, saldo);
            }

            conexao.close();
            stmt.close();
            rs.close();

        }catch(Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();

        }
        return null;
    }

    public Boolean cadastrar(String nome, String login, String senha, String tipo){
        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmtVerificar = conexao.prepareStatement("SELECT login FROM usuario WHERE login = ?");
            stmtVerificar.setString(1, login);
            ResultSet rs = stmtVerificar.executeQuery();

            if (rs.next()){
                return false;
            } else {
                PreparedStatement stmtUsuario = conexao.prepareStatement("INSERT INTO usuario (nome, login, senha, tipo) VALUES (?, ?, ?, ?)");
                stmtUsuario.setString(1, nome);
                stmtUsuario.setString(2, login);
                stmtUsuario.setString(3, senha);
                stmtUsuario.setString(4, tipo);
    
                stmtUsuario.executeUpdate();
                stmtUsuario.close();
    
                if (tipo == "comum"){
                    PreparedStatement stmtConta = conexao.prepareStatement("INSERT INTO conta (login, saldo) VALUES (?, ?)");
                    stmtConta.setString(1, login);
                    stmtConta.setDouble(2, 0.0);
        
                    stmtConta.executeUpdate();
                    stmtConta.close();
    
                }
                return true;
            }

        }catch(Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();

        }
        return false;
    }

    public class LoginsUsuarios{
        private String login;

        public LoginsUsuarios(String login){
            this.login = login;
        }

        public String getLogin() { return login; }
    }

    public List<LoginsUsuarios> dados(){
        List<LoginsUsuarios> dadosLogins = new ArrayList<>();

        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("SELECT login FROM usuario ORDER BY login");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                String login = rs.getString("login");

                LoginsUsuarios logins = new LoginsUsuarios(login);
                dadosLogins.add(logins);
            }

        }catch(Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
        }
        return dadosLogins;
    }

    public class DadosUsuario{
        String nome;
        String senha;
        String tipo;

        public DadosUsuario(String nome, String senha, String tipo){
            this.nome = nome;
            this.senha = senha;
            this.tipo = tipo;
        }

        public String getNome(){ return nome; }
        public String getSenha(){ return senha; }
        public String getTipo(){ return tipo; }

    }

    public DadosUsuario usuario(String login){

        try{Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("SELECT nome, senha, tipo FROM usuario WHERE login = ?");
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                String nome = rs.getString("nome");
                String senha = rs.getString("senha");
                String tipo = rs.getString("tipo");

                return new DadosUsuario(nome, senha, tipo);
            }

        }catch(Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
        }
        return null;
    }

    public Boolean alterar(String login, String nome, String senha){
        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("UPDATE usuario SET nome = ?, senha = ? WHERE login = ?");
            stmt.setString(1, nome);
            stmt.setString(2, senha);
            stmt.setString(3, login);

            stmt.executeUpdate();
            stmt.close();
            return true;

        }catch(Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();

        }
        return false;
    }

    public Boolean excluir(String login){
        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmtVerificar = conexao.prepareStatement("SELECT login FROM conta WHERE login = ?");
            stmtVerificar.setString(1, login);
            ResultSet rs = stmtVerificar.executeQuery();

            if (rs.next()){
                PreparedStatement stmtVerificarSaldo = conexao.prepareStatement("SELECT saldo FROM conta WHERE saldo = 0");
                ResultSet rsSaldo = stmtVerificarSaldo.executeQuery();

                if (rsSaldo.next()){
                    PreparedStatement stmtIdConta = conexao.prepareStatement("SELECT id_conta FROM conta WHERE login = ?");
                    stmtIdConta.setString(1, login);
                    ResultSet rsConta = stmtIdConta.executeQuery();

                    if (rsConta.next()){
                        PreparedStatement stmtHistorico = conexao.prepareStatement("DELETE FROM movimentacao WHERE id_conta = ?");
                        stmtHistorico.setInt(1, rsConta.getInt("id_conta"));
                        stmtHistorico.executeUpdate();
                    }
                    
                    PreparedStatement stmtConta = conexao.prepareStatement("DELETE FROM conta WHERE login = ?");
                    stmtConta.setString(1, login);
                    stmtConta.executeUpdate();
                    stmtConta.close();
                    
                    PreparedStatement stmtUsuario = conexao.prepareStatement("DELETE FROM usuario WHERE login = ?");
                    stmtUsuario.setString(1, login);
                    stmtUsuario.executeUpdate();
                    stmtUsuario.close();
                    stmtVerificar.close();
                    rs.close();
                    return true;

                } else {
                    return false;
                }

            } else {
                PreparedStatement stmtUsuario = conexao.prepareStatement("DELETE FROM usuario WHERE login = ?");
                stmtUsuario.setString(1, login);
                stmtUsuario.executeUpdate();
                stmtUsuario.close();

                stmtVerificar.close();
                rs.close();
                return true;

            }

        } catch(Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();

        }
        return false;
    }
}