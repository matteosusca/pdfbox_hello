package it.iubar.pdfbox_hello;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

public final class pdfbox_hello {
	private pdfbox_hello() {
	}

	private static final String FORM_PDF = "Modulo_registrazione_nuovo_dipendente.pdfa";
	private static final String FILLED_PDF = "Form_compilato(filled).pdfa";
	private static final String FINAL_PDF = "Scheda_dipendente_finale.pdfa";
	private static final String MERGED_PDF = "Merged.pdfa";

	public static void main(String[] args) throws IOException {
		//
		File formSample = createForm(FORM_PDF);
		// ..
		//Map<String, String> names = getFieldsNames(formSample);
		// ...
		File formFilled = fillForm(formSample, FILLED_PDF);
		
		File flattenPdf = flatPdf(formFilled, FINAL_PDF);
		// ....
		File merged = pdfMerge(formFilled, formSample, MERGED_PDF);
	}

	public static File createForm(String filename) throws IOException {
		
		List<String> data = new ArrayList<String>();
		
		data.add("Nome");
		data.add("Cognome");
		data.add("Sesso");
		data.add("Data nascita");
		data.add("Luogo nascita");
		
		File file = null;

		PDColor iubarColor = new PDColor(new float[] { 0.94f, 0.59f, 0.0f }, PDDeviceRGB.INSTANCE);
		PDColor lightOrange = new PDColor(new float[] { 0.99f, 0.851f, 0.597f }, PDDeviceRGB.INSTANCE);

		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			//Imposto le caratteristiche grafiche del form
			PDFont font = PDType1Font.TIMES_BOLD;
			PDResources resources = new PDResources();
			resources.put(COSName.getPDFName("Times_bold"), font);

			PDAcroForm acroForm = new PDAcroForm(document);
			document.getDocumentCatalog().setAcroForm(acroForm);

			acroForm.setDefaultResources(resources);

			String defaultAppearanceString = "/Helv 0 Tf 0 g";
			acroForm.setDefaultAppearance(defaultAppearanceString);
			
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
	 	    contentStream.showText("Scheda Dipendente");
		    contentStream.endText();
		    
		    contentStream.setNonStrokingColor(Color.LIGHT_GRAY);
		    contentStream.addRect(130, 625, 300, 1);
		    contentStream.fill();
		    
		    contentStream.beginText(); 
		    contentStream.newLineAtOffset(40, 585);
		    contentStream.setLeading(20.0f);
		    
		    //Ciclo per creare tutti i campi, inseriti nell' ArrayList "data"
		    for (int i = 0; i < data.size(); i++) {
		    	
		    	//Creo la targhetta
		    	contentStream.setNonStrokingColor(Color.DARK_GRAY);
		 	    contentStream.setFont(PDType1Font.TIMES_ITALIC, 16);
		 	    contentStream.showText(data.get(i) + ": ");
			    contentStream.newLine();
			    contentStream.newLine();
			    contentStream.newLine();
			    
			    //Creo il box field da compilare
			    PDTextField field = new PDTextField(acroForm);
			    field.setPartialName(data.get(i));
			    
			    
				defaultAppearanceString = "/Times_bold 20 Tf 0.94 0.59 0.0 rg";
				//defaultAppearanceString = "/TIMES_BOLD 20 Tf 0.94 0.59 0.0 rg";
				field.setDefaultAppearance(defaultAppearanceString);
				acroForm.getFields().add(field);
				PDAnnotationWidget nameWidget = field.getWidgets().get(0);
				PDRectangle nameRect = new PDRectangle(40, 550-(i*60), 300, 30);
				nameWidget.setRectangle(nameRect);
				nameWidget.setPage(page);
				PDAppearanceCharacteristicsDictionary nameFieldAppearance = new PDAppearanceCharacteristicsDictionary(
						new COSDictionary());
				nameWidget.setAppearanceCharacteristics(nameFieldAppearance);
				nameWidget.setPrinted(true);
				page.getAnnotations().add(nameWidget);
				field.setValue("Inserisci il " + data.get(i));
		    }

		    contentStream.endText();
		    
		    //Scrivo la firma a fondo pagina
		    contentStream.beginText(); 
		    contentStream.newLineAtOffset(400, 80);
		    contentStream.setLeading(19.0f);
		    contentStream.setNonStrokingColor(Color.DARK_GRAY);
	 	    contentStream.setFont(PDType1Font.HELVETICA, 14);
	 	    contentStream.showText("Il Titolare dell'azienda");
		    contentStream.endText();
		    
		    PDImageXObject sign = PDImageXObject.createFromFile("C:\\Users\\iubar\\Desktop\\sign.png",document);  
		    contentStream.drawImage(sign, 390, 30);	



		    //Closing the content stream
		    contentStream.close();
			file = new File(filename);
			document.save(filename);
			
		}
		return file;
	}

	public static File fillForm(File file, String outFilename) throws IOException {
		File outFile = null;

		try (PDDocument pdfDocument = PDDocument.load(file)) {
			PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

			if (acroForm != null) {
				List fields = acroForm.getFields();
				Iterator fieldsIter = fields.iterator();
				while (fieldsIter.hasNext()) {
					PDField field = (PDField) fieldsIter.next();
					field.setValue("Testo bla bla");
				}
			}
			pdfDocument.save(outFilename);
			outFile = new File(outFilename);
		}
		return outFile;

	}

	public static Map<String, String> getFieldsNames(File file) throws IOException {

		Map<String, String> boxs = new LinkedHashMap<String, String>();

		try (PDDocument pdfDocument = PDDocument.load(file)) {
			PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

			if (acroForm != null) {
				List<PDField> fields = acroForm.getFields();
				Iterator<PDField> fieldsIter = fields.iterator();
				while (fieldsIter.hasNext()) {
					PDField field = (PDField) fieldsIter.next();
					boxs.put(field.getPartialName(), field.getValueAsString());
				}
			}
		}
		return boxs;
	}
	
	public static File flatPdf(File form, String filename) throws IOException
	{
		PDDocument pDDocument = PDDocument.load(form);    
		PDAcroForm pDAcroForm = pDDocument.getDocumentCatalog().getAcroForm();
		
		pDAcroForm.flatten();

		pDDocument.save(filename);
		File flatten = new File(filename);
		pDDocument.close();
		return flatten;
	}

	public static File pdfMerge(File file1, File file2, String filename) throws IOException {

		PDDocument doc1 = PDDocument.load(file1);
		PDDocument doc2 = PDDocument.load(file2);

		PDFMergerUtility pdfMerger = new PDFMergerUtility();

		pdfMerger.setDestinationFileName(filename);
		PDDocumentInformation info = new PDDocumentInformation();
		info.setTitle("PDF di esempio");
		pdfMerger.setDestinationDocumentInformation(info);
		File file = new File(filename);
		pdfMerger.addSource(file1);
		pdfMerger.addSource(file2);
		pdfMerger.mergeDocuments(null);

		doc1.close();
		doc2.close();
		return file;
	}

}