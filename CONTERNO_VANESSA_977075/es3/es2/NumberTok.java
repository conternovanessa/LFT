public class NumberTok extends Token {
	int num = 0;
	public NumberTok(int n) { super(Tag.NUM) ; num = n;}
	public String toString() { return "<" + tag + ", " + num + ">"; }
		
}