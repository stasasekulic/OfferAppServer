package offerapp.offer.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import offerapp.offer.OfferService;
import offerapp.offer.dto.OfferResponse;
import offerapp.product.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OfferGenerator {

    private final OfferService offerService;

    public OfferGenerator(OfferService offerService) {
        this.offerService = offerService;
    }

    public byte[] generatePdfForOffer(Long offerId) {
        OfferResponse offer = offerService.getOfferById(offerId);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            document.add(new Paragraph("Offer Details")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setFont(boldFont));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Offer ID: " + offer.getId()).setFont(regularFont));
            document.add(new Paragraph("Title: " + offer.getTitle()).setFont(regularFont));
            document.add(new Paragraph("Created By User: " + offer.getUserEmail() + " (ID: " + offer.getUserId() + ")").setFont(regularFont));
            document.add(new Paragraph("Status: " + offer.getStatus().name()).setFont(regularFont));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Products in Offer:")
                    .setFontSize(14)
                    .setFont(boldFont));

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 4, 2, 2}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell(new Paragraph("No.").setFont(boldFont));
            table.addHeaderCell(new Paragraph("Description").setFont(boldFont));
            table.addHeaderCell(new Paragraph("Type").setFont(boldFont));
            table.addHeaderCell(new Paragraph("Price").setFont(boldFont));

            AtomicInteger counter = new AtomicInteger(1);
            long totalOfferPrice = 0;

            for (ProductResponse product : offer.getProducts()) {
                table.addCell(new Paragraph(String.valueOf(counter.getAndIncrement())).setFont(regularFont));
                table.addCell(new Paragraph(product.getDescription()).setFont(regularFont));
                table.addCell(new Paragraph(product.getType().name()).setFont(regularFont));
                table.addCell(new Paragraph(String.valueOf(product.getPrice())).setFont(regularFont));
                totalOfferPrice += product.getPrice();
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Total Offer Price: " + totalOfferPrice + " RSD")
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(16)
                    .setFont(boldFont));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Generated On: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(10)
                    .setFont(regularFont));

        } catch (Exception e) {
            e.printStackTrace(); // dodatno za debug
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }
}
