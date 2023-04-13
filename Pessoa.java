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
}