package compilador;

public class Token {
	int token;
	int linea;
	static String lexema = "";
	
	public Token ()	{}
	
	public Token (int t, int l)	{
		this.token = t;
		this.linea = l;
	}

	public int getToken()     { return token; }
	public int getLinea()     { return linea; }
	public String getLexema() { return Token.lexema; }
	
	public void setToken(int token) { this.token = token; }
	public void setLinea(int linea) { this.linea = linea; }
	public void setLexema(String lexema) { Token.lexema = lexema; }
	
	public String toString() {
		String a;
		if (Token.lexema == "") a = "[Token: " + this.token +  "]";
		else                    a = "[Token: " + this.token + " Lexema: " + Token.lexema + "]";
		return a;
	}

}
