import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
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
        switch(look.tag){
            case Tag.READ:
            case Tag.ASSIGN:
            case Tag.WHILE:
            case Tag.PRINT:
            case Tag.COND:
            case '{':
        int lnext_prog = code.newLabel();
        statlist(lnext_prog);
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
            code.toJasmin();
        }
            catch(java.io.IOException e) {
            System.out.println("IO error\n");
        };
        }
        }
    public void statlist(int next){
        switch(look.tag){
            case Tag.READ:
            case Tag.ASSIGN:
            case Tag.WHILE:
            case Tag.PRINT:
            case Tag.COND:
            case '{':
    	stat(next);
    	statlistp(next);
        }
    }

    public void statlistp(int next){
        switch(look.tag){
        case ';':
    	    if(look.tag == ';'){
    		    move();
    		    stat(next);
    		    statlistp(next);
    	    }
        }
    }

    public void stat(int next) {
        switch(look.tag) {
            case Tag.ASSIGN:
    			match(Tag.ASSIGN);
    			expr(); 
                match(Tag.TO);
                int i = 0;
                idlist(i);
                next = code.newLabel();
                code.emit(OpCode.GOto, next);
                code.emitLabel(next);
    		break;

    		case Tag.PRINT:
    			match(Tag.PRINT);
    			match('[');
                int p = 2;
    			exprlist(p); //aggiungere la virgola
    			match(']'); 
                code.emit(OpCode.invokestatic, 1);
                next = code.newLabel();
                code.emit(OpCode.GOto, next);
                code.emitLabel(next);
    		break;

            case Tag.READ:
                match(Tag.READ);
                match('[');
                i = 1;
                idlist(i); 
                match(']');
                next = code.newLabel();
                code.emit(OpCode.GOto, next);
                code.emitLabel(next);
            break;

            case Tag.WHILE:
                match(Tag.WHILE);
				match('(');
                int label_while = code.newLabel();
                int first_while = code.newLabel();
				int end_while = code.newLabel();
                code.emit (OpCode.GOto, label_while);
                code.emitLabel(label_while);
                bexpr(first_while, end_while);
                match(')');
                code.emitLabel(first_while);
                stat(label_while);
				code.emit (OpCode.GOto, label_while);
                code.emitLabel(end_while);
				
   
            break;

            case Tag.COND:
                match(Tag.COND);
                int label_true = code.newLabel();
                int label_false = code.newLabel();
                int cnext = code.newLabel();
                match('[');
                optlist(label_true,label_false,cnext);
                match(']');
                if(look.tag == Tag.ELSE){
                    match(Tag.ELSE);
                    stat(label_false);
                    code.emit(OpCode.GOto,cnext);
                }
                code.emitLabel(cnext);
                match(Tag.END);
            break;

    	    case '{':
    		    match('{');
    		    statlist(next);
    		    match('}');
                next = code.newLabel();
                code.emit(OpCode.GOto, next);
                code.emitLabel(next);
            break;

		}
    }
        
    
    
    private void idlist(int i) {
        
        switch(look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                match(Tag.ID);
                if(i == 0){                                     // assign
                    code.emit(OpCode.istore, id_addr);
                    if (look.tag == ',') {
                        code.emit(OpCode.iload, id_addr);
                    }
                }
                else if(i == 1){                                // read
                    code.emit(OpCode.invokestatic, 0);
                    code.emit(OpCode.istore, id_addr);
                }
                idlistp(i);
                break;
                }
        }

    private void idlistp(int j) {   
        switch(look.tag){
        case ',':
            if(look.tag == ','){
                match(',');
                if(look.tag == Tag.ID) {
                    int id_add = st.lookupAddress(((Word)look).lexeme);
                    if (id_add==-1) {
                        id_add = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                    
                    match(Tag.ID);
                    if(j == 0){                             // assign 
                        code.emit(OpCode.istore, id_add);
                        if (j == 0 && look.tag == ',') {
                            code.emit(OpCode.iload, id_add);
                    }
                    }
                    else if(j == 1){                        // read
                        code.emit(OpCode.invokestatic, 0);
                        code.emit(OpCode.istore, id_add);
                    }
                }
                idlistp(j);
                if(look.tag == Tag.EOF){
                    match(Tag.EOF);
                }
            }
        }
    }
     public void optlist(int ltrue, int lfalse, int next){
        switch(look.tag){
        case Tag.OPTION:
            optitem(ltrue,lfalse, next);
            optilistp(ltrue,lfalse, next);
            if(look.tag == Tag.EOF){
                match(Tag.EOF);
            }
        }
    }
    
    public void optilistp(int otrue, int ofalse,int next){
        switch(look.tag){
        case Tag.OPTION:
            optitem(otrue,ofalse,next);
        break;
        }
    }

     public void optitem(int otrue,int ofalse, int next){            
    	switch(look.tag){
    	case Tag.OPTION:
    		match(Tag.OPTION);
    		match('(');
    		bexpr(otrue,ofalse);
    		match(')');
            code.emitLabel(otrue);
    		if(look.tag == Tag.DO){
    			match(Tag.DO);
    			stat(otrue);
    		}
            code.emit(OpCode.GOto,next);
            code.emitLabel(ofalse);
    	break;
    	}
    }
    private void expr() {
        switch(look.tag) {
            case '+':
                int p = 0;
                match('+');
                match('(');
                exprlist(p);
                match(')');
                //code.emit(OpCode.iadd);
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
             case '*':
                p = 1;
                match('*');
                match('(');
                exprlist(p);
                match(')');
                //code.emit(OpCode.imul);
                break;
            case '/':
                match('/');
                expr();
                expr(); 
                code.emit(OpCode.idiv);
                break; 

            case Tag.NUM:
                NumberTok x = (NumberTok) look; 
                int num = x.num;
                code.emit(OpCode.ldc, num);
                match(Tag.NUM);
            	break;
            case Tag.ID:
                int id_add = st.lookupAddress(((Word)look).lexeme);
                if (id_add==-1) {
                    id_add = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                code.emit(OpCode.iload, id_add);
                match(Tag.ID);
                break;
        }
    }
    

    public void exprlist(int p){
        switch(look.tag){
        case '+':
        case '-':
        case '*':
        case '/':
        case Tag.ID:
        case Tag.NUM:
    	expr();
    	exprlistp(p);
        }
    }

     public void bexpr(int atrue, int afalse){
        switch(look.tag){
        case Tag.RELOP:
            if (look == Word.eq) {
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpeq, atrue);

            } else if (look == Word.le) {
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmple, atrue);
            } else if (look == Word.lt) {
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmplt, atrue);
            } else if (look == Word.ne) {
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpne, atrue);
            } else if (look == Word.ge) {
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpge, atrue);
            } else if (look == Word.gt) {
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.if_icmpgt, atrue);
            } else if (look == Word.not) {
                match(Tag.RELOP);
                expr();
                expr();
                code.emit(OpCode.ifne, atrue);
            }
            code.emit(OpCode.GOto, afalse);
        }
    }

    public void exprlistp(int p){
        switch(look.tag){
        case ',':
            if(look.tag == ','){
                match(',');
                if (p==2) {
                code.emit(OpCode.invokestatic, 1);
                }
                expr();
                if (p == 0) {
                    code.emit(OpCode.iadd);
                } else if (p == 1) {
                    code.emit(OpCode.imul);
                }
                exprlistp(p);
            } 
        }
    }

    

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "prova.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br); 
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }
}

