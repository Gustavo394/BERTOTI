public class Teste {

    public static void main (String[] args){
        Banco conta = new Banco();
    
        System.out.println("Conta 1 saldo atual: " + conta.saldo(1));
        
        conta.depositar(1, 100.00);
        System.out.println("Conta 1 + depósito de 100: " + conta.saldo(1));
        
        conta.sacar(1, 100.00);
        System.out.println("Conta 1 - saque de 100: " + conta.saldo(1));
        System.out.println("Conta 1 renda mensal: " + conta.renda(1) + "\n");
        
        System.out.println("Conta 2 saldo atual: " + conta.saldo(2));

        conta.depositar(2, 150.00);
        System.out.println("Conta 2 + depósito de 150: " + conta.saldo(2));

        conta.sacar(2, 150.00);
        System.out.println("Conta 2 - saque de 150: " + conta.saldo(2));
        System.out.println("Conta 2 renda mensal: " + conta.renda(2) + "\n");
    }
}
