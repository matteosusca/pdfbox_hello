package it.iubar.pdfbox_hello;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


public class PdfBoxApp {
	public void run() throws IOException{
		File file = new File("C:\\Users\\Scuola - Lavoro\\Desktop\\hellociao.pdf");
//		Create a pdf Document (Only here. not in a path)
		PDDocument document = PDDocument.load(file);
//		Create a page to add it later
		PDPage my_page = new PDPage();
//		Get Number of pages
		int noOfPages= document.getNumberOfPages();
		System.out.println(noOfPages);
		for (int i=0; i<noOfPages; i++) {
			document.removePage(0);
		}
//		Add page "my_page" to document "document"
		document.addPage(my_page);

		
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("Nome: ");
		String name = console.readLine();
		
		System.out.print("Cognome: ");
		String surname = console.readLine();
		
		String born[] = new String[3];
		System.out.print("Data di nascita:\n");
		System.out.print("	Giorno:");
		born[0] = console.readLine();
		System.out.print("	Mese:");
		born[1] = console.readLine();
		System.out.print("	Anno:");
		born[2] = console.readLine();
		
		document.addPage(my_page);
		
		PDPage page = document.getPage(0);
	    PDPageContentStream contentStream = new PDPageContentStream(document, page);
	     
	    //Insert name 
	    contentStream.beginText();   
	    contentStream.setFont(PDType1Font.TIMES_ITALIC, 15);
	    contentStream.newLineAtOffset(25, 750);
	    contentStream.showText("Nome: ");
	    contentStream.endText();
	    contentStream.beginText();   
	    contentStream.setFont(PDType1Font.TIMES_BOLD, 16);
	    contentStream.newLineAtOffset(70, 750);
	    contentStream.showText(name);
	    contentStream.endText();
	    
	    //Insert surname 
	    contentStream.beginText();   
	    contentStream.setFont(PDType1Font.TIMES_ITALIC, 15);
	    contentStream.newLineAtOffset(25, 725);
	    contentStream.showText("Cognome: ");
	    contentStream.endText();
	    contentStream.beginText();   
	    contentStream.setFont(PDType1Font.TIMES_BOLD, 16);
	    contentStream.newLineAtOffset(95, 725);
	    contentStream.showText(surname);
	    contentStream.endText();
	    
	    //Insert born date 
	    contentStream.beginText();   
	    contentStream.setFont(PDType1Font.TIMES_ITALIC, 15);
	    contentStream.newLineAtOffset(25, 700);
	    contentStream.showText("Data di nascita: ");
	    contentStream.endText();
	    contentStream.beginText();   
	    contentStream.setFont(PDType1Font.TIMES_BOLD, 16);
	    contentStream.newLineAtOffset(130, 700);
	    contentStream.showText(born[0] + "/" + born[1] + "/" + born[2]);
	    contentStream.endText();

	    System.out.println("PDF personle generato");

	    //Closing the content stream
	    contentStream.close();
		
		document.save("C:\\Users\\Scuola - Lavoro\\Desktop\\hellociao.pdf");		
	}

}
