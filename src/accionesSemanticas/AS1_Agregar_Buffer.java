package accionesSemanticas;

public class AS1_Agregar_Buffer extends AccionSemantica{

	@Override
	public int execute(StringBuffer buffer, char c) {
		buffer.append(c);
		return 0;
	}

	@Override
	public boolean acomodarLinea() {
		// TODO Auto-generated method stub
		return false;
	}

}
