package compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Archivo {

	private FileReader archivo;
	private static BufferedReader bufferLectura;
	
	public Archivo() {
		this.archivo = null;
		Archivo.bufferLectura = null;
	}
	public FileReader getArchivo()                                    { return this.archivo; }
	public static BufferedReader getBufferLectura()                   { return bufferLectura;	}
	public int leerBuffer() throws IOException 						  { return Archivo.bufferLectura.read(); }
	public void setArchivo(FileReader archivo)                        { this.archivo = archivo; }
	public static void setBufferLectura(BufferedReader bufferLectura) { Archivo.bufferLectura = bufferLectura; }
	public void cargarArchivo(String ruta) throws IOException{
		
		File archivo = null;
	    @SuppressWarnings("unused")
		FileReader fr = null;
		
		System.out.println("hola: " + ruta);
		archivo = new File(ruta);
		System.out.println("archivo 111");
		//fr = new FileReader (archivo);
		System.out.println("archivo 2");
		Archivo.bufferLectura = new BufferedReader(new FileReader(archivo));
		System.out.println("archivo 3333");
	}
}
