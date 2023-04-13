public class Banco {
    
    Movimentacao movimentacao = new Movimentacao();
        
    double saldo = 100.00;

    public double saldo(){
        return(saldo);

    }
    public double saque(double valor_saque){
        saldo -= valor_saque;
        movimentacao.dia();
        return(saldo);

    }
    public double deposito(double valor_deposito){
        saldo += valor_deposito;
        movimentacao.dia();
        return(saldo);

    }
    public double rendimento(){
        return (saldo*0.20);
        
    }
}
