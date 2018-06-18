package it.iubar.pdfbox_hello;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.Color;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


public class PdfBoxApp {
	public void run() throws IOException{
			
		generatePDF(getInformation());
	}
	
	public void generatePDF(Map<String, String> information) throws IOException
	{
		Color iubarColor = new Color(243, 152, 0);
		
		PDDocument document = new PDDocument();
		/*
		AccessPermission ap = new AccessPermission();
		StandardProtectionPolicy spp = new StandardProtectionPolicy("1234", "1234", ap);
		spp.setPermissions(ap);
		document.protect(spp);
		*/
		PDPage my_page = new PDPage();
		document.addPage(my_page);
		
		PDPage page = document.getPage(0);
	    PDPageContentStream contentStream = new PDPageContentStream(document, page);
	    
	    PDImageXObject logo = PDImageXObject.createFromFile("C:\\Users\\iubar\\Desktop\\iubar_logo.png",document);  
	    contentStream.drawImage(logo, 200, 700);	    
	    
	    contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
	    contentStream.addRect(130, 665, 300, 1);
	    contentStream.fill();
	    
	    contentStream.beginText(); 
	    contentStream.newLineAtOffset(210, 640);
	    contentStream.setNonStrokingColor(Color.black);
 	    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 17);
 	    contentStream.showText("Scheda Personale");
	    contentStream.endText();
	    
	    contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
	    contentStream.addRect(130, 625, 300, 1);
	    contentStream.fill();
	    
	    contentStream.beginText(); 
	    contentStream.newLineAtOffset(40, 585);
	    contentStream.setLeading(19.0f);
	    
	    for (String name: information.keySet()){
	    	
	    	String key = name.toString();
	        String value = information.get(name).toString(); 
	        
	    	contentStream.setNonStrokingColor(Color.DARK_GRAY);
	 	    contentStream.setFont(PDType1Font.TIMES_ITALIC, 16);
	 	    contentStream.showText(key + ": ");
		    contentStream.newLine();
		    contentStream.setNonStrokingColor(iubarColor);
	 	    contentStream.setFont(PDType1Font.TIMES_BOLD, 20);
	 	    contentStream.showText(value);
		    contentStream.newLine();
		    contentStream.newLine();  
	    } 
	    
	    contentStream.endText();

	    System.out.println("PDF personle generato");

	    //Closing the content stream
	    contentStream.close();
		
		document.save("C:\\Users\\iubar\\Desktop\\Scheda_personale_iubar.pdf");
	}
	
	public Map<String, String> getInformation() throws IOException
	{
Map<String, String> data = new LinkedHashMap<String, String>();
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("!ATTENZIONE! Scrivere tutto sempre minuscolo.");
		
		System.out.print("Nome: ");
		data.put("Nome", console.readLine());
		
		System.out.print("Cognome: ");
		data.put("Cognome", console.readLine());
		 
		System.out.print("Sesso: ");
		data.put("Sesso", console.readLine().toUpperCase());
		
		System.out.print("Data di nascita: ");
		data.put("Nato il", console.readLine());
		
		System.out.print("Comune di nascita: ");
		String comune = console.readLine();
		System.out.print("Provincia: ");
		String provincia = console.readLine().toUpperCase();
		data.put("Nato a", comune + " (" + provincia + ")");
		
		System.out.print("Comune di residenza: ");
		comune = console.readLine();
		System.out.print("Provincia: ");
		provincia = console.readLine().toUpperCase();
		data.put("Residente a", comune + " (" + provincia + ")");
		
		return data;
	}
	

}
