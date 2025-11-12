public class due
{

    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		if (Character.isLetter(ch))
		    state = 1;
		else if (ch == '_')
		    state = 2;
		else
		    state = -1;
		break;

	    case 1:
		if (Character.isLetter(ch) ||ch == '_'||Character.isDigit(ch))
		    state = 1;
		else
		    state = -1;
		break;

	    case 2:
		if (ch == '_')
		    state = 2;
		else if (Character.isDigit(ch)||Character.isLetter(ch))
		    state = 3;
		else
		    state = -1;
		break;

	    case 3:
		if (ch == '_'||Character.isDigit(ch)||Character.isLetter(ch))
			state = 3; 
		else
		    state = -1;
		break;
	    }
	}
	if(state==1 || state ==3)
		return true;
	else
		return false;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}



//per dire tutto alfabeto Character.isLetter()
//per dire tutte le cifre Character.isDigit()
