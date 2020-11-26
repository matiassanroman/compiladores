package compilador;

public class Registros {

	public int reg_EAX = 0;
	public int reg_EBX = 0;
	public int reg_ECX = 0;
	public int reg_EDX = 0;
	
	
	public Registros () {
	
	}
	
	public void imprimir() {
		System.out.println("A: " + reg_EAX);
		System.out.println("B: " + reg_EBX);
		System.out.println("C: " + reg_ECX);
		System.out.println("D: " + reg_EDX);
	}
	
	public String getRegistro(int i, String tipo) {
	
		if (reg_EAX == i && tipo.contentEquals("FLOAT"))
			return "EAX";
		if (reg_EBX == i && tipo.contentEquals("FLOAT"))
			return "EBX";
		if (reg_ECX == i && tipo.contentEquals("FLOAT"))
			return "ECX";
		if (reg_EDX == i && tipo.contentEquals("FLOAT"))
			return "EDX";
		
		if (reg_EAX == i && tipo.contentEquals("INTEGER"))
			return "AX";
		if (reg_EBX == i && tipo.contentEquals("INTEGER"))
			return "BX";
		if (reg_ECX == i && tipo.contentEquals("INTEGER"))
			return "CX";
		if (reg_EDX == i && tipo.contentEquals("INTEGER"))
			return "DX";
		return "";
	}

	public void ocuparRegistro(String registro, int polaca) {
		if (registro == "EAX" || registro == "AX" ) {
			reg_EAX = polaca;
		}
		if (registro == "EBX" || registro == "BX" ) {
			reg_EBX = polaca;
		}
		if (registro == "ECX" || registro == "CX") {
			reg_ECX = polaca;
		}
		if (registro == "EDX" || registro == "DX") {
			reg_EDX = polaca;
		}
	}
	public String getPrimerRegistroLibre(String tipo) {
		if (tipo.equals("FLOAT")) {
			if (reg_EAX == 0)
				return "EAX";
			if (reg_EBX == 0)
				return "EBX";
			if (reg_ECX == 0)
				return "ECX";
			if (reg_EDX == 0)
				return "EDX";		
			
		}else if (tipo.equals("INTEGER")) {			
			if (reg_EAX == 0)
				return "AX";		
			if (reg_EBX == 0)
				return "BX";
			if (reg_ECX == 0)
				return "CX";
			if (reg_EDX == 0)
				return "DX";	
		}
		return "";
	
	}

	public boolean estaOcupado(String reg) {
		switch (reg) {
			case ("EAX"):
				if (reg_EAX == 0) return false;
				break;
			case ("EBX"):
				if (reg_EBX == 0) return false;
				break;
			case ("ECX"):
				if (reg_EAX == 0) return false;
				break;
			case ("EDX"):
				if (reg_EAX == 0) return false;
				break;
				
			case ("AX"):
				if (reg_EAX == 0) return false;
				break;
			case ("BX"):
				if (reg_EBX == 0) return false;
				break;
			case ("CX"):
				if (reg_EAX == 0) return false;
				break;
			case ("DX"):
				if (reg_EAX == 0) return false;
				break;
			
		
		}
		return true;
	}
	
}
