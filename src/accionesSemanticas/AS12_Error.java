package accionesSemanticas;

public class AS12_Error extends AccionSemantica{

	@Override
	public int execute(StringBuffer buffer, char c) {
		buffer = new StringBuffer();
		return -2;
	}

	@Override
	public boolean acomodarLinea() {
		// TODO Auto-generated method stub
		return true;
	}

}
