import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Movimentacao{
    LocalDateTime atual = LocalDateTime.now();

    public void movimentacao(Integer conta, String tipo, Double valor){
        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO movimentacao (data, tipo, valor, id_conta) VALUES (?, ?, ?, ?)");
            stmt.setTimestamp(1, Timestamp.valueOf(atual));
            stmt.setString(2, tipo);
            stmt.setDouble(3, valor);
            stmt.setInt(4, conta);
            stmt.executeUpdate();
            stmt.close();
            conexao.close();

        } catch (Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
        }
    }

    public class Consulta{
        private Integer id;
        private String data;
        private Double valor;
        private String tipo;
    
        public Consulta(Integer id, String data, Double valor, String tipo){
            this.id = id;
            this.data = data;
            this.valor = valor;
            this.tipo = tipo;
        }
    
        public Integer getId(){return id;}
    
        public String getData(){return data;}
    
        public Double getValor(){return valor;}
    
        public String getTipo(){return tipo;}

    }

    Integer id = null;
    String data = "";
    Double valor = null;
    String tipo = "";

    public List<Consulta> consulta(Integer id_conta) {
        List<Consulta> consultas = new ArrayList<>();
    
        try {
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("SELECT id, TO_CHAR(data, 'DD/MM/YY hh:mm') AS data, tipo, valor FROM movimentacao WHERE id_conta = ? ORDER BY id");
            stmt.setInt(1, id_conta);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Integer id = rs.getInt("id");
                String data = rs.getString("data");
                Double valor = rs.getDouble("valor");
                String tipo = rs.getString("tipo");
    
                Consulta consulta = new Consulta(id, data, valor, tipo);
                consultas.add(consulta);
            }
    
            conexao.close();
            stmt.close();
            rs.close();

        } catch (Exception e) {
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
        }
        return consultas;
    }

    public class ConsultaMensal {
        private String dia;
        private Integer mes;
        private String ano;
        private Double valorSaque;
        private Double valorDeposito;
    
        public ConsultaMensal(String dia, Integer mes, String ano, Double valorSaque, Double valorDeposito) {
            this.dia = dia;
            this.mes = mes;
            this.ano = ano;
            this.valorSaque = valorSaque;
            this.valorDeposito = valorDeposito;
        }

        public String getDia() { return dia; }
    
        public Integer getMes() { return mes; }

        public String getAno() { return ano; }
    
        public Double getValorSaque() { return valorSaque; }
    
        public Double getValorDeposito() { return valorDeposito; }
    }

    public List<ConsultaMensal> mensal(Integer id_conta, String anoConsulta, String mesConsulta) {
        List<ConsultaMensal> consultas = new ArrayList<>();
    
        try {
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            
            String dia = null;
            Integer mes = null;
            String ano = null;
            Double valorSaque = null;
            Double valorDeposito = null;

            if (anoConsulta == null && mesConsulta == null){
                PreparedStatement stmt = conexao.prepareStatement("SELECT " +
                "TO_CHAR(data, 'YYYY') AS ano, " +
                "SUM(CASE WHEN tipo = 'deposito' THEN valor ELSE 0 END) AS total_deposito, " +
                "SUM(CASE WHEN tipo = 'saque' THEN valor ELSE 0 END) AS total_saque " +
                "FROM movimentacao WHERE id_conta = ? GROUP BY ano ORDER BY ano");
                stmt.setInt(1, id_conta);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    ano = rs.getString("ano");
                    valorSaque = rs.getDouble("total_saque");
                    valorDeposito = rs.getDouble("total_deposito");
        
                    ConsultaMensal consulta = new ConsultaMensal(null, null, ano, valorSaque, valorDeposito);
                    consultas.add(consulta);
                }

            } else if (anoConsulta != null && mesConsulta == null){
                PreparedStatement stmt = conexao.prepareStatement("SELECT " +
                "TO_CHAR(data, 'MM') AS mes, " +
                "SUM(CASE WHEN tipo = 'deposito' THEN valor ELSE 0 END) AS total_deposito, " +
                "SUM(CASE WHEN tipo = 'saque' THEN valor ELSE 0 END) AS total_saque " +
                "FROM movimentacao WHERE id_conta = ? AND TO_CHAR(data, 'YYYY') = ? GROUP BY mes ORDER BY mes");
                stmt.setInt(1, id_conta);
                stmt.setString(2, anoConsulta);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()){
                    mes = rs.getInt("mes");
                    valorSaque = rs.getDouble("total_saque");
                    valorDeposito = rs.getDouble("total_deposito");
        
                    ConsultaMensal consulta = new ConsultaMensal(null, mes, null, valorSaque, valorDeposito);
                    consultas.add(consulta);
            }

            } else if (anoConsulta != null && mesConsulta != null){
                PreparedStatement stmt = conexao.prepareStatement("SELECT " +
                "TO_CHAR(data, 'DD') AS dia, " +
                "SUM(CASE WHEN tipo = 'deposito' THEN valor ELSE 0 END) AS total_deposito, " +
                "SUM(CASE WHEN tipo = 'saque' THEN valor ELSE 0 END) AS total_saque " +
                "FROM movimentacao WHERE id_conta = ? AND TO_CHAR(data, 'MM/YYYY') = ? GROUP BY dia");
                stmt.setInt(1, id_conta);
                stmt.setString(2, mesConsulta.toString() + "/" + anoConsulta.toString());
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()){
                    dia = rs.getString("dia");
                    valorSaque = rs.getDouble("total_saque");
                    valorDeposito = rs.getDouble("total_deposito");
        
                    ConsultaMensal consulta = new ConsultaMensal(dia, mes, ano, valorSaque, valorDeposito);
                    consultas.add(consulta);
                }

            }
            conexao.close();

        } catch (Exception e) {
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
        }
    
        return consultas;
    }
}
