import java.io.*; 

public class Parser2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser2(Lexer l, BufferedReader br) {
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

    public void prog() {  
	   statlist();
	   match(Tag.EOF);
    }
    /*********************************************************************/
    public void statlist(){
    	stat();
    	statlistp();
    }
    /***********************************************************************/
    public void statlistp(){
    	if(look.tag == ';'){
    		move();
    		stat();
    		statlistp();
    	}
    }
    /*************************************************************************/
    public void stat(){
    	switch(look.tag){
			
    		case Tag.ASSIGN:
    			match(Tag.ASSIGN);
    			expr();
    			if(look.tag == Tag.TO){
    				match(Tag.TO);
    				idlist();
    			}
    		break;

    		case Tag.PRINT:
    			match(Tag.PRINT);
    			match('[');
    			exprlist();
    			match(']');
    		break;

    		case Tag.READ:
    			match(Tag.READ);
    			if(look.tag == '[')
    			match('[');
    			idlist();
    			if(look.tag == ']')
    			match(']');
    			else error("Errore");
    			
    		break;

    		case Tag.WHILE:
    			match(Tag.WHILE);
    			match('(');
    			bexpr();
    			match(')');
    			stat();
    		break;

    		case Tag.COND:
    			match(Tag.COND);
    			match('[');
    			optlist();
    			match(']');
    			if(look.tag == Tag.ELSE){
    				match(Tag.ELSE);
    				stat();
    			}	
    			match(Tag.END);
    		break;

    		case '{':
    			match('{');
    			statlist();
    			match('}');
		}
    }
/********************************************************************/
    public void idlist(){	
    	if(look.tag == Tag.ID){
    		match(Tag.ID);
    		idlistp();
    	}
    }
/**********************************************************************/
    public void idlistp(){
    	if(look.tag == ','){
    		match(',');
    		if(look.tag == Tag.ID){
    			match(Tag.ID);
    			idlistp();
    		}
    	}
    	if(look.tag == Tag.EOF){
    		match(Tag.EOF);
    	}
    }
/*********************************************************************/
    public void optlist(){
    	optitem();
    	optilistp();
    	if(look.tag == Tag.EOF){
    		match(Tag.EOF);
    	}
    }
/********************************************************************/
    public void optilistp(){
    	optitem();
    	//optilistp(); sarebbe infinito
    }
/********************************************************************/
    public void optitem(){
    	switch(look.tag){
    	case Tag.OPTION:
    		match(Tag.OPTION);
    		match('(');
    		bexpr();
    		match(')');
    		if(look.tag == Tag.DO){
    			match(Tag.DO);
    			stat();
    		}
    	break;
    	}
    }
/********************************************************************/
    private void bexpr(){
    	if(look.tag == Tag.RELOP){
    		match(Tag.RELOP);
    		expr();
    		expr();
    	}
    }
/******************************************************************/
    private void expr() {  
		switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;

            case '-':
                match('-');
                expr();
                expr();
                break;

             case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr(); 
                break; 

            case Tag.NUM:
            	match(Tag.NUM);
				
            	break;
            case Tag.ID:
            	match(Tag.ID);      
           		break;
		}
    }
/*********************************************************************/
    public void exprlist(){
    	expr();
    	exprlistp();
    }
/*******************************************************************/
    public void exprlistp(){
    	if(look.tag == ','){
    		match(',');
    		expr();
    		exprlistp();
    	}
    }
/*******************************************************************/
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "/home/vconterno/lft/es3/Provaes3.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser2 parser = new Parser2(lex, br); 
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }
}
