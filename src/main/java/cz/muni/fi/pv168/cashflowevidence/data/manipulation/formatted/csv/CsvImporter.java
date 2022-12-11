package cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.csv;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Batch;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchImporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidCSVFormatException;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.category.Category;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionType;
import org.tinylog.Logger;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class CsvImporter implements BatchImporter {

    private static final String SEPARATOR = ",";
    private static final Format FORMAT = new Format("CSV", List.of("csv"));

    private final Repository<Category> categoryRepository;

    public CsvImporter(Repository<Category> categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public Batch importBatch(String filePath) {
        Logger.info("Loading transactions from {}", filePath);
        try (var reader = Files.newBufferedReader(Path.of(filePath))) {
            var transactions = reader.lines()
                    .peek(Logger::trace)
                    .map(this::parseTransaction)
                    .peek(Logger::debug)
                    .collect(Collectors.toList());

            // TODO: @Martin Pilát please review this and explain, why was this different in csv and json
            // In prev implementaions, the DAO exporter failed, when some categories were null
            var categories = new HashMap<String, Category>();

            Logger.info("Loaded {} records", transactions.size());
            return new Batch(categories.values(), transactions);
        } catch (IOException ex) {
            Logger.error("Error while loading transactions.");
            throw new DataManipulationException("Unable to read file", ex);
        }
    }

    private Transaction parseTransaction(String line) {
        try {
            var fields = line.split(SEPARATOR);
            Category category = convertCategory(fields);

            return new Transaction(
                    TransactionType.valueOf(fields[6]),
                    fields[0],
                    fields[1],
                    Double.parseDouble(fields[2]),
                    LocalDateTime.parse(fields[3]),
                    category
            );
        } catch (Exception ex) {
            throw new InvalidImportException("Invalid file format.");
        }
    }

    private Category convertCategory(String[] fields) {
        if (fields[4].equals("null") || fields[5].equals("null")) {
            return null;
        }

        List<Category> categories = categoryRepository.findAll();
        String categoryName = fields[4];
        String categoryColor = fields[5];

        return categories.stream()
                .filter(c -> Objects.equals(c.getName(), categoryName) &&
                        Objects.equals(c.getColorString(), categoryColor))
                .findFirst()
                .orElse(null);
    }
}
