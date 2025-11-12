public class sette
{

    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length()) {
	    final char ch = s.charAt(i++);

	    switch (state) {
	    case 0:
		if (ch == 'V')
		    state = 8 ;
		else
		    state = 1;
		break;

	    case 1:
		if (ch == 'a')
		    state = 2 ;
		break;

		case 2:
		if (ch == 'n')
		    state = 3 ;
		break;

		case 3:
		if (ch == 'e')
		    state = 4;
		break;

		case 4:
		if (ch == 's')
		    state = 5;
		break;

		case 5:
		if (ch == 's')
		    state = 6;
		break;

		case 6:
		if (ch == 'a')
		    state = 7;
		break;

		case 7:
		break;

		case 8:
		if (ch == 'a')
		    state = 9 ;
		else
		    state = 2;
		break;

		case 9:
		if (ch == 'n')
		    state = 10;
		else
		    state = 3;
		break;

		case 10:
		if (ch == 'e')
		    state = 11;
		else
		    state = 4;
		break;

		case 11:
		if (ch == 's')
		    state = 12;
		else
		    state = 5;
		break;

		case 12:
		if (ch == 's')
		    state = 13;
		else
		    state = 6;
		break;

		case 13:
		if (ch == 'a')
		    state = 14;
		else
		    state = 7;
		break;

		case 14:
		break;

	    }
	}
	if(state == 7 || state == 14)
		return true;
	else
		return false;
    }

    public static void main(String[] args)
    {
	System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}