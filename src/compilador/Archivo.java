package compilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Archivo {

	private FileReader archivo;
	private static BufferedReader bufferLectura;
		
	public Archivo()                                { }
	public FileReader getArchivo()                                    { return this.archivo; }
	public static BufferedReader getBufferLectura()                   { return bufferLectura;	}
	public void setArchivo(FileReader archivo)                        { this.archivo = archivo; }
	public static void setBufferLectura(BufferedReader bufferLectura) { Archivo.bufferLectura = bufferLectura; }
	public void cargarArchivo(String ruta) throws IOException{
		archivo = new FileReader(ruta);
	    bufferLectura = new BufferedReader(archivo);
	}
}
