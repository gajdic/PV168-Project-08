package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.data.manipulation.TransactionImporter;
import cz.muni.fi.pv168.cashflowevidence.model.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.TransactionTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Collection;
import java.util.Objects;

public class ImportAction extends AbstractAction {

    private final JTable table;
    private final TransactionImporter importer;

    public ImportAction(JTable table, TransactionImporter importer) {
        super("Import", UIManager.getIcon("FileChooser.fileIcon"));
        this.table = Objects.requireNonNull(table);
        this.importer = Objects.requireNonNull(importer);
        putValue(SHORT_DESCRIPTION, "Imports selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int dialogResult = fileChooser.showOpenDialog(table);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            File importFile = fileChooser.getSelectedFile();
            //TODO: switch between which import option e.g json or csv
            Collection<Transaction> importedTransactions = importer.importJson(importFile.getAbsolutePath());

            var transactionTableModel = (TransactionTableModel) table.getModel();
            importedTransactions.forEach(transactionTableModel::addRow);
        }
    }
}