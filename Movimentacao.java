import java.text.SimpleDateFormat;
import java.util.Date;

public class Movimentacao {

    String dia;

    public void dia() {
        Date date = new Date();
        SimpleDateFormat dateForm = new SimpleDateFormat("dd/MMMM/Y");
        dia = dateForm.format(date);
        
    }
}