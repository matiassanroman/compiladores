package accionesSemanticas;

public class AS10_No_Accion extends AccionSemantica{

	//No hace nada.
	
	@Override
	public int execute(StringBuffer buffer, char c) {
		return 0;
	}
	
	@Override
	public boolean acomodarLinea(){
		return false;
	}

}
