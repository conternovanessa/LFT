import java.io.*; 

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    
    void move() {
        look = lex.lexical_scan(pbr);
        System.err.println("token = " + look);
    }

    void error(String s) { 
	throw new Error("near line " + lex.line + ": " + s); 
    }

    void match(int t) {
	if (look.tag == t) {
	    if (look.tag != Tag.EOF) move();
	} else error("syntax error");
    }

    public void start() {  
	   expr();
	   match(Tag.EOF);
    }

    private void expr() {  
        if(look.tag == Tag.NUM || look.tag == Token.lpt.tag){
            term();
            if(look.tag == Token.plus.tag || look.tag == Token.minus.tag || look.tag == Token.rpt.tag || look.tag == Tag.EOF){
                exprp();
            }
            else error("L'espressione non contiene l'operatore '+' o '-' richiesto");
        }
        else error("L'espressione non contiene '(' o il numero richiesto");
       
        
    }

    private void exprp() {
	switch (look.tag) {
            case '+':
                match('+');
                term();
                exprp();
                break;
            case '-':
                match('-');
                term();
                exprp();
                break;
           
                
	}
    }

    private void term() { 
        if(look.tag == Tag.NUM || look.tag == Token.lpt.tag){
            fact();
            if(look.tag == Token.mult.tag || look.tag == Token.div.tag || look.tag == Token.plus.tag || look.tag == Token.minus.tag || look.tag == Token.rpt.tag || look.tag == Tag.EOF){
                termp();
            }
            else error("L'espressione non contiene l'operatore '*,/,+,-' richiesto");
        }
        else error("L'espressione non contiene '(' o il numero richiesto");
        
        
        
    
    }
    
    private void termp() { 
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;
            case '/':
                match('/');
                fact();
                termp();
                break;   
	}
        
    }
    
    private void fact() { 
        
        if(look.tag == Tag.NUM){
            match(Tag.NUM);
        }
        else{
            if (look.tag != Token.lpt.tag){
                error("non è presente la parentesi");
            }
            match('(');
            expr();
            if (look.tag != Token.rpt.tag){
                error("non è stata chiusa la parentesi");
            }
            else match(')');
            
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/vconterno/lft/es3/Provaes31.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br); 
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }
}
