package arquivos;

import java.util.Scanner;

public class Pessoa {

    String nome = "Mario Almeida";
    String login = "malmeida";
    String senha = "1234";

    public boolean logar(String validar_login, String validar_senha) {
        if (validar_login.equals(login) && validar_senha.equals(senha)){
            System.out.println("Boas vindas " + nome);
            return true;

        }
        else {
            return false;

        }
    }

    public static void main(String[] args) {
        
        Pessoa pessoa = new Pessoa();
		Scanner scan = new Scanner(System.in);

        System.out.println("Login: ");
        String login = scan.next();
        System.out.println("Senha: ");
        String senha = scan.next();

        try{
            if (pessoa.logar(login, senha) == true){
                System.out.println("Selecione a opção desejada \n" +
                "1 - Saldo\n" +
                "2 - Sacar\n" +
                "3 - Depositar\n" +
                "4 - Calcular rendimento\n");
                int escolha = scan.nextInt();
                Banco banco = new Banco();

                switch(escolha){
                    case 1:
                        System.out.println("Saldo atual da conta: " + banco.saldo());
                        break;
                    case 2:
                        System.out.println("Informe o valor do saque: ");
                        double saque = scan.nextInt();
                        System.out.println("Saque realizado com sucesso\n" +
                        "Novo saldo: " + banco.saque(saque));
                        break;
                    case 3:
                        System.out.println("Informe o valor do deposito: ");
                        double deposito = scan.nextInt();
                        System.out.println("Deposito realizado com sucesso\n" +
                        "Novo saldo: " + banco.deposito(deposito));
                        break;
                    case 4:
                        System.out.println("O rendimento anual da sua conta atualmente é " + banco.rendimento());
                        break;

                }
                
            }
            else{
                System.out.println("Usuario ou senha incorretos!");

            }
        }
        catch (Exception e){
            System.out.println("Erro " + e);

        }
        scan.close();
    }

}
