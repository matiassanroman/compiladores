package accionesSemanticas;

public class AS12_Error extends AccionSemantica{

	
	// Accion semantica que detecta caracters invalidos
	// en ese caso retorna codigo de error -2
	public int execute(StringBuffer buffer, char c) {
		System.out.println("Error en la linea "+compilador.Compilador.nroLinea+": CARACTER invalido");
		buffer.delete(0, buffer.length());
		return 0;
	}

	// Al haber detectado el caracter invalido se debe 
	// acomodar la niea para seguir procesando
	public boolean acomodarLinea() {
		// TODO Auto-generated method stub
		return false;
	}

}
