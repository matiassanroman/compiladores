package accionesSemanticas;

import compilador.Compilador;

public class AS9_count_NewLine_Comentario extends AccionSemantica{

	@Override
	public int execute(StringBuffer buffer, char c) {
		buffer = new StringBuffer();
		Compilador.limpiarBuffer();
		return 0;
	}

	@Override
	public boolean acomodarLinea() {
		// TODO Auto-generated method stub
		return false;
	}

}
