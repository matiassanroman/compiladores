package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CrearAssembler {

	public static void crearTxtSalida(String assembler) {
		try {
			String ruta = "Assembler.txt";
			BufferedWriter salida = new BufferedWriter(new FileWriter(ruta));
			salida.write(assembler);
			salida.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
}
