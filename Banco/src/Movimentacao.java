import java.time.LocalDateTime;
import java.sql.*;

public class Movimentacao {
    LocalDateTime atual = LocalDateTime.now();

    public void movimentacao(Integer conta, String tipo, Double valor){
        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("INSERT INTO movimentacao (dia, tipo, valor, id_conta) VALUES (?, ?, ?, ?)");
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
}
