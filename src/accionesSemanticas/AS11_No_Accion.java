package accionesSemanticas;

public class AS11_No_Accion extends AccionSemantica{

	// Ante la apacion de un determinado simbolo, el cual
	// no requiere realizar ninguna accion, no se realiza nada
	
	public int execute(StringBuffer buffer, char c) {
		System.out.println("NoActionBuffer " + buffer + " Char: " + c);
		return 0;
	}
	
	public boolean acomodarLinea(){
		return false;
	}

}
