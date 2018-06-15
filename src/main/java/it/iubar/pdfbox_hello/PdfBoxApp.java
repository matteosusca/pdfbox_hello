package it.iubar.pdfbox_hello;

import java.io.File;
import java.io.IOException; 
import org.apache.pdfbox.pdmodel.*;


public class PdfBoxApp {
	public void run() throws IOException{
		File file = new File("C:\\Users\\Scuola - Lavoro\\Desktop\\hellociao.pdf");
		PDDocument document = PDDocument.load(file);
		PDPage my_page = new PDPage();
		document.addPage(my_page);
		try {
			document.save("C:\\Users\\Scuola - Lavoro\\Desktop\\hellociao.pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("addio");
		}
		try {
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("addio2");
		}
		
	}

}
