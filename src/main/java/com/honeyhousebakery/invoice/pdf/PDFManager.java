package com.honeyhousebakery.invoice.pdf;

import com.honeyhousebakery.invoice.domain.Client;
import com.honeyhousebakery.invoice.domain.Order;
import com.honeyhousebakery.invoice.domain.Purchase;
import com.honeyhousebakery.invoice.utils.DateUtil;
import com.honeyhousebakery.invoice.utils.FilePathUtil;
import com.honeyhousebakery.invoice.utils.OrderType;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author danielwind
 */
public class PDFManager {
    
    private static final String DEFAULT_EXTENSION = ".pdf";
    private static final String DEFAULT_FILE_HEADER = "invoice-";
    private static final String DEFAULT_SEPARATOR = "_";
    private static final String DEFAULT_HEADER_IMAGE = "/hhb_logo.jpg";
    private static final String DEFAULT_HEADER_INVOICE_TEXT = "INVOICE #";
    private static final String DEFAULT_HEADER_CUSTOMER_TEXT = "To: ";
    private static final String DEFAULT_THANK_YOU_MESSAGE = "THANK YOU FOR YOUR BUSINESS!!";
    
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font tableHeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    private static Font tableValueFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
    
    private static BaseColor LIGHT_BLUE = new BaseColor(228, 234, 245);
    
    //--- Global Styles ---//
    
    public static void generatePDFInvoice(Order order) {
        
        String invoiceID = order.getInvoiceId();
        
        //get today's date
        String customDate = order.getDate();
        
        if(customDate == null || customDate.isEmpty()){
            customDate = DateUtil.invoiceGUIFormattedDate();
        }
            
        try {
            
            Document document = new Document();
            
            String path = "";
            
            //determine which directory should the PDF be saved!
            if(order.getType() == OrderType.RETAIL){
                path = FilePathUtil.getClientSubdirectoryPath("Retail");
            } else if(order.getType() == OrderType.WHOLESALE) {
                path = FilePathUtil.getClientSubdirectoryPath(order.getClient().getName());
            }
            
            //append file name parameters
            path += DEFAULT_FILE_HEADER + invoiceID + DEFAULT_SEPARATOR + DateUtil.invoiceFormattedDate() + DEFAULT_EXTENSION ;
            
            PdfWriter.getInstance(document, new FileOutputStream(path));
            
            document.open();
            
            addMetaData(document ,order);
            addCompanyInfo(document, order, customDate);
            addInvoiceDetails(document, order);
            addFooterThankYou(document);
            
            document.close();
            
            //open file with default associated program
            Desktop.getDesktop().open(new File(path));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void addMetaData(Document document, Order order) {
        document.addTitle("Honey House Bakery Invoice PDF");
        document.addSubject("Invoice #" + order.getInvoiceId());
        document.addKeywords("Invoice, Honey House Bakery, Bill");
        document.addAuthor("HHB Invoice System");
        document.addCreator("Daniel Wind");
    }
    
    private static void addCompanyInfo(Document document, Order order, String customDate) throws DocumentException, BadElementException, MalformedURLException, IOException {
        
        String invoiceNumber = order.getInvoiceId();
        Client client = order.getClient();
        
        Paragraph preface = new Paragraph();
        document.add( Chunk.NEWLINE );
        
        Paragraph invoiceParagraph = new Paragraph(DEFAULT_HEADER_INVOICE_TEXT + invoiceNumber, catFont);
        invoiceParagraph.setAlignment(Element.ALIGN_RIGHT);
        
        preface.add(invoiceParagraph);
        
        Image image1 = Image.getInstance(PDFManager.class.getResource(DEFAULT_HEADER_IMAGE));
        image1.setAbsolutePosition(50f, 690f);
        image1.scaleAbsolute(150f, 100f);
        //writer.getDirectContent().addImage(image1, true);
        
        Paragraph customerParagraph = new Paragraph(DEFAULT_HEADER_CUSTOMER_TEXT + client.getName() + "\n " + client.getAddress() + "\n " + (client.getState() != null ? client.getState() : "")  + " " + (client.getZipCode() != null ? client.getZipCode() : ""));
        customerParagraph.setAlignment(Element.ALIGN_RIGHT);
        
        preface.add(customerParagraph);
        
        //Date 
        Paragraph dateParagraph = new Paragraph(customDate);
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        dateParagraph.setSpacingBefore(40.0f);
        
        preface.add(dateParagraph);
        
        LineSeparator ls = new LineSeparator();
        ls.setLineWidth(0.5f);
        
        PdfPTable table = new PdfPTable(7); // 7 columns.
        
        PdfPCell headerCell1 = new PdfPCell(new Paragraph("SALESPERSON", tableHeaderFont));
        PdfPCell headerCell2 = new PdfPCell(new Paragraph("JOB", tableHeaderFont));
        PdfPCell headerCell3 = new PdfPCell(new Paragraph("SHIPPING\n METHOD", tableHeaderFont));
        PdfPCell headerCell4 = new PdfPCell(new Paragraph("SHIPPING\n TERMS", tableHeaderFont));
        PdfPCell headerCell5 = new PdfPCell(new Paragraph("DELIVERY\n DATE", tableHeaderFont));
        PdfPCell headerCell6 = new PdfPCell(new Paragraph("PAYMENT\n TERMS", tableHeaderFont));
        PdfPCell headerCell7 = new PdfPCell(new Paragraph("DUE DATE", tableHeaderFont));
        
        headerCell1.setBackgroundColor(LIGHT_BLUE);
        headerCell2.setBackgroundColor(LIGHT_BLUE);
        headerCell3.setBackgroundColor(LIGHT_BLUE);
        headerCell4.setBackgroundColor(LIGHT_BLUE);
        headerCell5.setBackgroundColor(LIGHT_BLUE);
        headerCell6.setBackgroundColor(LIGHT_BLUE);
        headerCell7.setBackgroundColor(LIGHT_BLUE);
        
        PdfPCell contentCell1 = new PdfPCell(new Paragraph(" "));
        PdfPCell contentCell2 = new PdfPCell(new Paragraph(" "));
        PdfPCell contentCell3 = new PdfPCell(new Paragraph(" "));
        PdfPCell contentCell4 = new PdfPCell(new Paragraph(" "));
        PdfPCell contentCell5 = new PdfPCell(new Paragraph(" "));
        PdfPCell contentCell6 = new PdfPCell(new Paragraph(" "));
        PdfPCell contentCell7 = new PdfPCell(new Paragraph(" "));
        
        table.addCell(headerCell1);
        table.addCell(headerCell2);
        table.addCell(headerCell3);
        table.addCell(headerCell4);
        table.addCell(headerCell5);
        table.addCell(headerCell6);
        table.addCell(headerCell7);
        
        table.addCell(contentCell1);
        table.addCell(contentCell2);
        table.addCell(contentCell3);
        table.addCell(contentCell4);
        table.addCell(contentCell5);
        table.addCell(contentCell6);
        table.addCell(contentCell7);
        
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        
        
        document.add(preface);
        
        document.add(image1);
        
        // add some blank lines
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        
        document.add(new Chunk(ls));
        
        document.add(table);
        
    }
    
    private static void addInvoiceDetails(Document document, Order order) throws DocumentException {
        
        PdfPTable table = new PdfPTable(6); // 6 columns.
        
        PdfPCell headerCell1 = new PdfPCell(new Paragraph("QTY", tableHeaderFont));
        PdfPCell headerCell2 = new PdfPCell(new Paragraph("ITEM #", tableHeaderFont));
        PdfPCell headerCell3 = new PdfPCell(new Paragraph("DESCRIPTION", tableHeaderFont));
        PdfPCell headerCell4 = new PdfPCell(new Paragraph("UNIT PRICE", tableHeaderFont));
        PdfPCell headerCell5 = new PdfPCell(new Paragraph("DISCOUNT", tableHeaderFont));
        PdfPCell headerCell6 = new PdfPCell(new Paragraph("LINE TOTAL", tableHeaderFont));
        
        headerCell1.setBackgroundColor(LIGHT_BLUE);
        headerCell2.setBackgroundColor(LIGHT_BLUE);
        headerCell3.setBackgroundColor(LIGHT_BLUE);
        headerCell4.setBackgroundColor(LIGHT_BLUE);
        headerCell5.setBackgroundColor(LIGHT_BLUE);
        headerCell6.setBackgroundColor(LIGHT_BLUE);
        
        table.addCell(headerCell1);
        table.addCell(headerCell2);
        table.addCell(headerCell3);
        table.addCell(headerCell4);
        table.addCell(headerCell5);
        table.addCell(headerCell6);
        
        //add purchases lines
        for (Purchase purchase : order.getPurchases()) {
            PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(purchase.getQuantity()), tableValueFont));
            PdfPCell cell2 = new PdfPCell(new Paragraph("", tableValueFont)); //nothing to add yet!
            PdfPCell cell3 = new PdfPCell(new Paragraph(purchase.getItemDescription(), tableValueFont));
            PdfPCell cell4 = new PdfPCell(new Paragraph(String.valueOf(purchase.getItemPrice()), tableValueFont));
            PdfPCell cell5 = new PdfPCell(new Paragraph("", tableValueFont)); //nothing to add yet!
            PdfPCell cell6 = new PdfPCell(new Paragraph(String.valueOf(purchase.getLinetotal()), tableValueFont));
            
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell6.setHorizontalAlignment(Element.ALIGN_RIGHT);
            
            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
        }
        
        //add three blank lines
        addInvoiceBlankLines(table);
        
        //add subtotal line
        addSubtotalLines(table, order);
        
        //add discount line
        addDiscountLines(table, order);
        
        //add total line
        addTotalLines(table, order);
                
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        
        float[] columnWidths = {1f, 1f, 2f, 1f, 1f, 1f};
        table.setWidths(columnWidths);
        
        document.add(table);
    }
    
    private static void addInvoiceBlankLines(PdfPTable table) {

        for (int i = 0; i < 3; i++) {
            
            for (int j = 0; j < 6; j++) {
                PdfPCell cell = new PdfPCell(new Paragraph(" "));
                table.addCell(cell);
            }
        }
    }
    
    private static void addSubtotalLines(PdfPTable table, Order order) {
            
            PdfPCell cell1 = new PdfPCell(new Paragraph("SUBTOTAL ($) ", tableHeaderFont));
            PdfPCell cell2 = new PdfPCell(new Paragraph(order.getOrderTotalAmount().toPlainString(), tableValueFont));
            
            //set column span to 5
            cell1.setColspan(5);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell1.setPaddingRight(5);
            
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setPaddingRight(5);
            
            table.addCell(cell1);
            table.addCell(cell2);
            
    }
    
    private static void addDiscountLines(PdfPTable table, Order order) {
            
            PdfPCell cell1 = new PdfPCell(new Paragraph("DISCOUNT ($) ", tableHeaderFont));
            PdfPCell cell2 = new PdfPCell(new Paragraph(order.getAppliedDiscount().toPlainString(), tableValueFont));
            
            //set column span to 5
            cell1.setColspan(5);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell1.setPaddingRight(5);
            
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setPaddingRight(5);
            
            table.addCell(cell1);
            table.addCell(cell2);
            
    }
    
    private static void addTotalLines(PdfPTable table, Order order) {
            
            PdfPCell cell1 = new PdfPCell(new Paragraph("TOTAL ($) ", tableHeaderFont));
            PdfPCell cell2 = new PdfPCell(new Paragraph(order.getOrderTotalWithDiscount(), tableValueFont));
            
            
            //set column span to 5
            cell1.setColspan(5);
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell1.setPaddingRight(5);
            
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setPaddingRight(5);
            
            table.addCell(cell1);
            table.addCell(cell2);
            
    }
    
    private static void addFooterThankYou(Document document) throws DocumentException {
        
        Paragraph thankYouParagraph = new Paragraph(DEFAULT_THANK_YOU_MESSAGE, tableHeaderFont);
        thankYouParagraph.setAlignment(Element.ALIGN_CENTER);
        
        // add some blank lines
        document.add( Chunk.NEWLINE );
        document.add( Chunk.NEWLINE );
        
        document.add(thankYouParagraph);
    }
    
}
