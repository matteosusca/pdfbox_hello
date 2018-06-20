package it.iubar.pdfbox_hello;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

/**
 * An example of creating an AcroForm and a form field from scratch.
 * 
 * The form field is created with properties similar to creating a form with
 * default settings in Adobe Acrobat.
 * 
 */
public final class pdfbox_hello {
	private pdfbox_hello() {
	}

	private static final String FILENAME = "SimpleForm.pdf";
	private static final String FILLED_PDF = "FillFormField.pdf";

	public static void main(String[] args) throws IOException {
		//...
		File file = createForm(FILENAME);
		// ..
		List<String> names = getFieldsNames(file);
		// ...
		File outPdf = fillForm(file, FILLED_PDF);
		// ....
		File outPdf3 = pdfMerge(file, outPdf, "Merged.pdf");
	}


	

	public static File createForm(String filename2) throws IOException {
		File file = null;
		// Color iubarColor = new Color(243, 152, 0);
		PDColor iubarColor = new PDColor(new float[] { 243f, 152f, 0f }, PDDeviceRGB.INSTANCE);
		PDColor lightGray = new PDColor(new float[] { 224f, 224f, 224f }, PDDeviceRGB.INSTANCE);

		// Create a new document with an empty page.
		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage(PDRectangle.A4);
			document.addPage(page);

			// Adobe Acrobat uses Helvetica as a default font and
			// stores that under the name '/Helv' in the resources dictionary
			PDFont font = PDType1Font.HELVETICA;
			PDResources resources = new PDResources();
			resources.put(COSName.getPDFName("Helv"), font);

			// Add a new AcroForm and add that to the document
			PDAcroForm acroForm = new PDAcroForm(document);
			document.getDocumentCatalog().setAcroForm(acroForm);

			// Add and set the resources and default appearance at the form level
			acroForm.setDefaultResources(resources);

			// Acrobat sets the font size on the form level to be
			// auto sized as default. This is done by setting the font size to '0'
			String defaultAppearanceString = "/Helv 0 Tf 0 g";
			acroForm.setDefaultAppearance(defaultAppearanceString);

			PDTextField nameBox = new PDTextField(acroForm);
			nameBox.setPartialName("Nome");

			defaultAppearanceString = "/Helv 12 Tf 0 0 0 rg";
			nameBox.setDefaultAppearance(defaultAppearanceString);

			acroForm.getFields().add(nameBox);

			PDAnnotationWidget nameWidget = nameBox.getWidgets().get(0);
			PDRectangle nameRect = new PDRectangle(50, 700, 150, 30);
			nameWidget.setRectangle(nameRect);
			nameWidget.setPage(page);

			PDAppearanceCharacteristicsDictionary nameFieldAppearance = new PDAppearanceCharacteristicsDictionary(
					new COSDictionary());
			nameFieldAppearance.setBorderColour(iubarColor);
			nameFieldAppearance.setBackground(lightGray);
			nameWidget.setAppearanceCharacteristics(nameFieldAppearance);

			nameWidget.setPrinted(true);
			page.getAnnotations().add(nameWidget);
			nameBox.setValue("Nome");

			PDTextField surnameBox = new PDTextField(acroForm);
			surnameBox.setPartialName("Cognome");

			surnameBox.setDefaultAppearance(defaultAppearanceString);

			acroForm.getFields().add(surnameBox);

			PDAnnotationWidget surnameWidget = surnameBox.getWidgets().get(0);
			PDRectangle surnameRect = new PDRectangle(50, 650, 150, 30);
			surnameWidget.setRectangle(surnameRect);
			surnameWidget.setPage(page);

			PDAppearanceCharacteristicsDictionary surnameFieldAppearance = new PDAppearanceCharacteristicsDictionary(
					new COSDictionary());
			surnameFieldAppearance.setBorderColour(iubarColor);
			surnameFieldAppearance.setBackground(lightGray);
			surnameWidget.setAppearanceCharacteristics(surnameFieldAppearance);

			surnameWidget.setPrinted(true);
			page.getAnnotations().add(surnameWidget);
			surnameBox.setValue("Cognome");

			document.save(filename2);
			file = new File(filename2);
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
				while( fieldsIter.hasNext())
				{
				    PDField field = (PDField)fieldsIter.next();
				    field.setValue("Testo bla bla");

				}
			}
			pdfDocument.save(outFilename);
			outFile = new File(outFilename);
		}
		return outFile;	
		
	}
	
	public static List<String> getFieldsNames(File file) throws IOException{
		
		List<String> names = new ArrayList<String>();
		
		try (PDDocument pdfDocument = PDDocument.load(file)) {
			PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();
			if (acroForm != null) {
				List<PDField> fields = acroForm.getFields();
				Iterator<PDField> fieldsIter = fields.iterator();
				while( fieldsIter.hasNext())
				{
					PDField field = (PDField)fieldsIter.next();
				    names.add(field.getFullyQualifiedName());
				}
			}
		}
		return names;
	}
	
	public static File pdfMerge(File file1, File file2, String filename) throws IOException {

		  MemoryUsageSetting.setupMainMemoryOnly();
	      PDDocument doc1 = PDDocument.load(file1);
	      PDDocument doc2 = PDDocument.load(file2);
	      
	      PDFMergerUtility PDFmerger = new PDFMergerUtility();

	      PDFmerger.setDestinationFileName(filename);
	      File file = new File(filename);
	      
	      PDFmerger.addSource(file1);
	      PDFmerger.addSource(file2);
	      PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
	      
	      doc1.close();
	      doc2.close();
	      return file;
	}

}