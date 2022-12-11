package cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Batch;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchExporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import org.tinylog.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class PdfExporter implements BatchExporter {

    private static final Format FORMAT = new Format("PDF", List.of("pdf"));
    private final Font titleFont = new Font(Font.FontFamily.HELVETICA, 18,
            Font.BOLD);
    private final Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private final Font contentFont = new Font(Font.FontFamily.HELVETICA, 10);

    @Override
    public void exportBatch(Batch batch, String filePath) {
        Logger.info("Exporting transactions to {}", filePath);

        Document document = new Document();
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
            PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            document.addTitle("Transactions export from " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

            document.add(getHeader());
            document.add(getPdfTable(batch.getTransactions()));

            document.close();

        } catch (FileNotFoundException e) {
            Logger.error("Error while exporting transactions, unable to create the file with the path " + filePath);
            throw new DataManipulationException("Unable to create file with the path " + filePath, e);
        } catch (IOException | DocumentException e) {
            Logger.error("Error while exporting transactions. ", e);
            throw new DataManipulationException("Error while exporting transactions. ", e);
        }
    }

    private Paragraph getHeader() {
        Paragraph header = new Paragraph();

        header.add(new Paragraph(" "));
        Paragraph title = new Paragraph("Transactions export from "
                + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE), titleFont);
        title.setAlignment(0);
        header.add(title);
        header.add(new Paragraph(" "));

        return header;
    }

    private PdfPTable getPdfTable(Collection<Transaction> transactions) {
        PdfPTable table = new PdfPTable(6);

        table.setWidthPercentage(100);

        Stream.of("Transaction name", "Description", "Amount (€)",
                        "Created at", "Category", "Transaction type")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle, headerFont));
                    header.setPadding(5);
                    table.addCell(header);
                });

        transactions.forEach(transaction -> {
            table.addCell(new Phrase(transaction.getName(), contentFont));
            table.addCell(new Phrase(transaction.getDescription(), contentFont));
            table.addCell(new Phrase(String.valueOf(transaction.getAmount()), contentFont));
            table.addCell(new Phrase(transaction.getDateTime().toString(), contentFont));
            table.addCell(new Phrase(transaction.getCategory() != null ? transaction.getCategory().getName() : null, contentFont));
            table.addCell(new Phrase(transaction.getTransactionType().toString(), contentFont));
        });


        return table;
    }

    @Override
    public Format getFormat() {
        return FORMAT;
    }
}
