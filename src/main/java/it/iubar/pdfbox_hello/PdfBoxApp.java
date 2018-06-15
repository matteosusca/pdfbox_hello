package it.iubar.pdfbox_hello;

import java.io.File;
import java.io.IOException; 
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
		PDDocumentInformation pdd = document.getDocumentInformation();
		//Setting the author of the document
	    pdd.setAuthor("Tutorialspoint");
	       
	    // Setting the title of the document
	    pdd.setTitle("Sample document"); 
	       
	    //Setting the creator of the document 
	    pdd.setCreator("PDF Examples"); 
	       
	    //Setting the subject of the document 
	    pdd.setSubject("Example document");
	    
	    PDPage page = document.getPage(0);
	      PDPageContentStream contentStream = new PDPageContentStream(document, page);
	      
	      //Begin the Content stream 
	      contentStream.beginText(); 
	       
	      //Setting the font to the Content stream  
	      contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

	      //Setting the position for the line 
	      contentStream.newLineAtOffset(25, 500);

	      String text = "This is the sample document and we are adding content to it.";

	      //Adding text in the form of string 
	      contentStream.showText(text);      

	      //Ending the content stream
	      contentStream.endText();
	      contentStream.close();
//		Save and create a project if it doesn't exist in a path
		document.save("C:\\Users\\Scuola - Lavoro\\Desktop\\hellociao.pdf");
//		Close open project
		document.close();		
	}

}
