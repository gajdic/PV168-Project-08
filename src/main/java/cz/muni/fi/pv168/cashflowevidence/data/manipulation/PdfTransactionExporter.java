package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cz.muni.fi.pv168.cashflowevidence.exceptions.ExportFailedException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Stream;

import org.tinylog.Logger;

public class PdfTransactionExporter implements TransactionExporter{

    Font titleFont = new Font(Font.FontFamily.HELVETICA, 18,
            Font.BOLD);
    Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    Font contentFont = new Font(Font.FontFamily.HELVETICA, 10);

    @Override
    public void exportTransactions(Collection<Transaction> transactions, String filePath) throws ExportFailedException {
        Logger.info("Exporting transactions to {}", filePath);

        Document document = new Document();
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
            PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            document.addTitle("Transactions export from " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

            document.add(getHeader());
            document.add(getPdfTable(transactions));

            document.close();

        } catch (FileNotFoundException e) {
            Logger.error("Error while exporting transactions, unable to create the file with the path" + filePath);
            throw new ExportFailedException(e);
        } catch (IOException | DocumentException e) {
            Logger.error("Error while exporting transactions", e);
            throw new ExportFailedException(e);
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
    public ExportFormat getFormat() {
        return ExportFormat.pdf;
    }
}
