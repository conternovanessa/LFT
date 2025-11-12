import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexer {
	public static int line = 1;
	private char peek = ' ';
	private void readch(BufferedReader br) {
		try {
			peek = (char) br.read();
			} catch (IOException exc) {
			peek = (char) -1; // ERROR
		}
	}


public Token lexical_scan(BufferedReader br) {
	while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
		if (peek == '\n') line++; 
		readch(br); 
		}
		if (peek == '/') {
		readch(br);
		if (peek == '*') {
			readch(br);
			while (peek == '*' || peek != '/') {
				readch(br);
			}
			readch(br);
			if (peek == '/') {
				readch(br);
			}

		} else if (peek == '/'){
			readch(br);
			boolean end = true;
			while(peek != '\n' || peek != (char) -1 ) {
				if(peek == (char) -1) break;
				readch(br);
			}
			readch(br);
		}else if (peek == ' '){
			 peek = '/';
		}
		
		}
	while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
		if (peek == '\n') line++; 
		readch(br); 
		}	
	switch (peek) {
		case '!':
			peek = ' ';
		return Token.not;
		case '(':
			peek = ' ';
		return Token.lpt;
		case ')':
			peek = ' ';
		return Token.rpt;
		case '[':
			peek = ' ';
		return Token.lpq;
		case ']':
			peek = ' ';
		return Token.rpq;
		case '{':
			peek = ' ';
		return Token.lpg;
		case '}':
			peek = ' ';
		return Token.rpg;
		case '+':
			peek = ' ';
		return Token.plus;
		case '-':
			peek = ' ';
		return Token.minus;
		case '*':
			peek = ' ';
		return Token.mult;
		case '/':
			peek = ' ';
		return Token.div;
		case ';':
			peek = ' ';
		return Token.semicolon;
		case ',':
			peek = ' ';
		return Token.comma;
		case '&':
			readch(br);
			if (peek == '&') {
				peek = ' ';
			return Word.and;
			} else {
				System.err.println("Erroneous character" + " after & : " + peek );
			return null;
			}
		case '|':
			readch(br);
			if (peek == '|') {
				peek = ' ';
			return Word.or;
			} else {
				System.err.println("Erroneous character" + " after | : " + peek );
			return null;
			}
		case '=':
			readch(br);
			if (peek == '=') {
				peek = ' ';
			return Word.eq;
			} else {
				System.err.println("Erroneous character" + " after = : " + peek );
			return null;
			}	
		case '<':
			readch(br);
				if (peek == '=') {
				peek = ' ';
				return Word.le;
				}
			else if (peek == ' '){
				return (Word.lt);
				} 
			else if(peek=='>'){
				readch(br);
				return (Word.ne);
			} else {
				System.err.println("Erroneous character" + " after < : " + peek );
			return null;
			}
		case '>':
			readch(br);
			if (peek == '=') {
				peek = ' ';
			return Word.ge;
			} 
			else if (peek == ' '){
				return (Word.gt);
			} else {
				System.err.println("Erroneous character" + " after > : " + peek );
			return null;
			}
	
		case (char)-1:
			return new Token(Tag.EOF);
			default:
			if (Character.isLetter(peek) || peek == '_') {							// ... gestire il caso degli identificatori e delle parole chiave //
					String p = new String();
					while (Character.isDigit(peek) || Character.isLetter(peek) || peek == '_'){
						p = p + peek;
						readch(br);
					}
					boolean tuttouguale = true;
					for (int i = 0; i < p.length(); i++) {
						if(p.charAt(i) != '_'){
							tuttouguale = false;
						}
					}
					if(tuttouguale){
						System.err.println("Erroneous identifier" );
						return null;
					}
				if (p.compareTo("assign")==0) {
					return Word.assign;
				}else if (p.compareTo("to")==0) {
					return Word.to;
				}else if (p.compareTo("option")==0){
					return Word.option;
				} else if (p.compareTo("conditional")==0) {
					return Word.conditional;
				} else if (p.compareTo("do")==0) {
					return Word.dotok;
				} else if (p.compareTo("else")==0) {
					return Word.elsetok;
				} else if (p.compareTo("while")==0) {
					return Word.whiletok;
				} else if (p.compareTo("begin")==0) {
					return Word.begin;
				} else if (p.compareTo("end")==0) {
					return Word.end;
				} else if (p.compareTo("print")==0) {
					return Word.print;
				} else if (p.compareTo("read")==0) {
					return Word.read;
				} else{
					return new Word (Tag.ID, p);}

			} else if (Character.isDigit(peek)) { // ... gestire il caso dei numeri ... //
				int sum=0;
			while(Character.isDigit(peek)){
				int c= Integer.parseInt(Character.toString(peek));;
				if(sum != 0 || c != 0){
					sum= sum*10+c;
					}
					readch(br);
			}
			if(peek == '_' || Character.isLetter(peek)){
				System.err.println("il numero non è seguito da uno spazio ma da: " + peek );
				return null;
			}else{
				return new NumberTok(sum);
			}
			} else {
					System.err.println("Erroneous character: " + peek );
			return null;
			}
	}
}


	public static void main(String[] args) {
		Lexer lex = new Lexer();
		String path = "Provaes3.txt"; // il percorso del file da leggere
			try {
				BufferedReader br = new BufferedReader(new FileReader(path));
				Token tok;
			do {
				tok = lex.lexical_scan(br);
				System.out.println("Scan: " + tok);
			}while (tok.tag != Tag.EOF);
				br.close();
			}catch (IOException e) {e.printStackTrace();}
	}
}
