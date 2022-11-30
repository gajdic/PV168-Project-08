package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.TransactionImporter;
import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.exceptions.InvalidImportException;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionTableModel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Collection;
import java.util.Objects;

import static cz.muni.fi.pv168.cashflowevidence.app.CashFlowEvidence.frame;

public class ImportAction extends AbstractAction {

    private final JTable table;
    private final TransactionImporter importer;
    private final Repository<Transaction> transactionRepository;

    private final Runnable callback;

    public ImportAction(JTable table,
                        TransactionImporter importer,
                        Repository<Transaction> transactionRepository,
                        Runnable callback)
    {
        super("Import " + importer.getFormat(), UIManager.getIcon("FileChooser.fileIcon"));
        this.table = Objects.requireNonNull(table);
        this.importer = Objects.requireNonNull(importer);
        this.transactionRepository = transactionRepository;
        this.callback = callback;
        putValue(SHORT_DESCRIPTION, "Imports selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter(importer.getFormat().toString(),
                importer.getFormat().toString()));

        int dialogResult = fileChooser.showOpenDialog(table);
        if (dialogResult != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File importFile = fileChooser.getSelectedFile();
        Collection<Transaction> importedTransactions;

        try {
            importedTransactions = importer.importRecords(importFile.getAbsolutePath());
        } catch (InvalidImportException ex) {
            JOptionPane.showMessageDialog(frame,
                    "There has been an error with the import, the chosen file is invalid. Nothing was imported," +
                            "please try again");
            ex.printStackTrace();
            return;
        }

        importedTransactions.forEach(transactionRepository::create);
        callback.run();
    }
}