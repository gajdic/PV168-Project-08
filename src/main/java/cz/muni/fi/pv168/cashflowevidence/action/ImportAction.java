package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.app.workers.AsyncImporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Importer;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.BatchImporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ImportAction extends AbstractAction {

    private final JTable table;
    private final Importer importer;

    private final Format format;

    private final Runnable callback;

    public ImportAction(JTable table,
                        Importer importer,
                        Format format,
                        Repository<Transaction> transactionRepository,
                        Runnable callback,
                        Consumer<Boolean> busyReporter)
    {
        super("Import " + format.getName(), UIManager.getIcon("FileChooser.fileIcon"));
        this.table = Objects.requireNonNull(table);
        this.importer = new AsyncImporter(Objects.requireNonNull(importer), callback, busyReporter);
        this.transactionRepository = transactionRepository;
        this.callback = callback;
        this.format = format;
        putValue(SHORT_DESCRIPTION, "Imports selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        String fileFormat = format.getExtensions().stream().findFirst().orElse(format.getName());
        fileChooser.setFileFilter(new FileNameExtensionFilter(fileFormat, fileFormat));

        int dialogResult = fileChooser.showOpenDialog(table);
        if (dialogResult != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File importFile = fileChooser.getSelectedFile();

        try {
            String fileName = importFile.getAbsolutePath();

            importer.importData(fileName);
        } catch (InvalidImportException ex) {
            JOptionPane.showMessageDialog(
                    null, "Nothing was imported. \n\nThe file that you provided is invalid, please try again.");
            return;
        }

        callback.run();
    }
}