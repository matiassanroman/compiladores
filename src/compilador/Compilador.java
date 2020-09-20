
// Clases central desde donde se invoca todo

package compilador;
import java.io.IOException;

public class Compilador {

	public static void main(String[] args) throws IOException {
		
		// Obtengo la ruta del archivo de los argumentos de programa
		String ruta = args[0];
		Archivo archivo = new Archivo();
		// Cargo el archivo para poder usarlo
		archivo.cargarArchivo(ruta);

	}

}
