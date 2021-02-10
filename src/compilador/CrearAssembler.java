package compilador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class CrearAssembler {

	public static void crearTxtSalida(String assembler, String nombre) {
		try {
			String separator = "\\";
			String[] nombre_arr = nombre.replaceAll(Pattern.quote(separator), "\\\\").split("\\\\");
			String[] nombre_final = nombre_arr[nombre_arr.length-1].split("\\.");
			String ruta = nombre_final[0] + ".asm";
			BufferedWriter salida = new BufferedWriter(new FileWriter(ruta));
			salida.write(assembler);
			salida.close();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
}
