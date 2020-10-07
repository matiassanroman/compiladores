package accionesSemanticas;

public class AS12_Error extends AccionSemantica{

	
	// Accion semantica que detecta caracters invalidos
	// en ese caso retorna codigo de error -2
	public int execute(StringBuffer buffer, char c) {
		buffer = new StringBuffer();
		return -2;
	}

	// Al haber detectado el caracter invalido se debe 
	// acomodar la niea para seguir procesando
	public boolean acomodarLinea() {
		// TODO Auto-generated method stub
		return true;
	}

}
