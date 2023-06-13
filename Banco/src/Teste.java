import org.junit.Assert;
import org.junit.Test;

public class Teste {
    Conta conta = new Conta();
    Cadastro usuario = new Cadastro();
    Movimentacao movimento = new Movimentacao();

    // Testes de usuarios
    @Test
    public void testeLogar() {
        Cadastro.DadosLogin resultado = usuario.login("emanuel", "4321");
        Assert.assertEquals(usuario.login(null, null).toString(), resultado.getStatus(), true);
    }

    @Test
    public void testeCadastrar() {
        Boolean verificar = usuario.cadastrar("teste", "teste", "teste", "teste");
        Assert.assertEquals(usuario.cadastrar("teste", "teste", "teste", "teste").toString(), verificar, true);
        
    }

    @Test
    public void testeAlterar() {
        Boolean verificar = usuario.alterar("teste", "teste", "teste");
        Assert.assertEquals(usuario.alterar("teste", "teste", "teste").toString(), verificar, true);
        
    }

    @Test
    public void testeExcluir() {
        Boolean verificar = usuario.excluir("teste");
        Assert.assertEquals(usuario.excluir("teste").toString(), verificar, true);
        
    }

    // Testes de conta bancária

    @Test
    public void testeConsultar() {
        double saldoInicialConta1 = conta.saldo(1);
        Assert.assertEquals(500.0, saldoInicialConta1, 500.0);
    }

    @Test
    public void testeDepositar() {
        conta.depositar(2, 250.0);
        double saldoConta1 = conta.saldo(2);
        Assert.assertEquals(500.0, saldoConta1, 500.0);
    }

    @Test
    public void testeSacar() {
        conta.sacar(2, 250.0);
        double saldoConta1 = conta.saldo(2);
        Assert.assertEquals(250.0, saldoConta1, 250.0);
    }

    @Test
    public void testeCalcular() {
        double rendaMensalConta1 = conta.rendaMensal(1);
        Assert.assertEquals(conta.saldo(1)*0.0117, rendaMensalConta1, conta.saldo(1)*0.0117);
    }

    // Testes de movimentaõçes

    @Test
    public void testExaminar() {
    }
}