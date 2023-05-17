import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Banco {
    Movimentacao registrar = new Movimentacao();

    public double saldo(Integer conta){
        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM conta WHERE id_conta = ?");
            stmt.setInt(1, conta);
            ResultSet rs = stmt.executeQuery();

            double saldoFormatado = 0.0;
    
            while (rs.next()){
                double saldo = rs.getDouble("saldo");

                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                symbols.setDecimalSeparator('.');
                DecimalFormat df = new DecimalFormat("#.##", symbols);
                String rendaFormatadoStr = df.format(saldo);
    
                saldoFormatado = Double.parseDouble(rendaFormatadoStr);

            }

            conexao.close();
            stmt.close();
            rs.close();
            return (saldoFormatado);

        } catch (Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
            return (0);
        }
    }

    public void sacar(Integer conta, Double saque){
        try{
            Double saldo = saldo(conta);

            if (saldo == 0){
                Alerta.titulo = "Alerta";
                Alerta.texto = "Não existe saldo atualmente na conta!";
                Alerta alerta = new Alerta();
                alerta.aviso();

            } else if (saque > saldo){
                Alerta.titulo = "Alerta";
                Alerta.texto = "O saque não pode ser maior que o saldo atual da conta!";
                Alerta alerta = new Alerta();
                alerta.aviso();

            } else{
                Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
                PreparedStatement stmt = conexao.prepareStatement("UPDATE conta SET saldo = ? WHERE id_conta = ?");
                stmt.setDouble(1, (saldo-saque));
                stmt.setInt(2, conta);
                stmt.executeUpdate();

                registrar.movimentacao(conta, "saque", saque);
            }

        } catch (Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
        }
    }

    public void depositar(Integer conta, Double deposito){
        try{
            Double saldo = saldo(conta);

            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("UPDATE conta SET saldo = ? WHERE id_conta = ?");
            stmt.setDouble(1, (saldo+deposito));
            stmt.setInt(2, conta);
            stmt.executeUpdate();

            registrar.movimentacao(conta, "deposito", deposito);
            
        } catch (Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
        }
    }

    public Double renda(Integer conta){
        try{
            Connection conexao = DriverManager.getConnection(Main.banco, Main.usuario, Main.senha);
            PreparedStatement stmt = conexao.prepareStatement("SELECT * FROM conta WHERE id_conta = ?");
            stmt.setInt(1, conta);
            ResultSet rs = stmt.executeQuery();

            double rendaFormatado = 0.0;
    
            while (rs.next()) {
                double renda = rs.getDouble("saldo") * 0.0117;
    
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
                symbols.setDecimalSeparator('.');
                DecimalFormat df = new DecimalFormat("#.##", symbols);
                String rendaFormatadoStr = df.format(renda);
    
                rendaFormatado = Double.parseDouble(rendaFormatadoStr);
            }

            conexao.close();
            stmt.close();
            rs.close();
            return (rendaFormatado);

        } catch (Exception e){
            Alerta.titulo = "Erro";
            Alerta.texto = e.toString();
            Alerta alerta = new Alerta();
            alerta.aviso();
            return (0.0);
        }
    }
}
