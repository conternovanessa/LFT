public class Tenativo
{
  void move() {
        look = lex.lexical_scan(pbr);
        System.err.println("token = " + look);
    }
  void match(int t) {
	if (look.tag == t) {
	    if (look.tag != Tag.EOF) move();
	}
    }
  
  private void expr() { 
    switch (look.tag) {
       case '/':
				System.out.println("divisione");
                match('/');
                expr();
                expr(); 
                break;
    }
  }
  public static void main(String[] args) {
        String path = "diviso.txt"; // il percorso del file da leggere
            System.out.println("Input OK"); 
  
}
}