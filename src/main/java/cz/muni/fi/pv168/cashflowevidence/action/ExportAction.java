package cz.muni.fi.pv168.cashflowevidence.action;

import cz.muni.fi.pv168.cashflowevidence.app.workers.AsyncExporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.DataManipulationException;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.Exporter;
import cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted.Format;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Objects;

public class ExportAction extends AbstractAction {
    private final JTable table;
    private final Exporter exporter;

    private final Format format;

    public ExportAction(JTable table, Exporter exporter, Format format, Runnable callback) {
        super("Export " + format.getName(), UIManager.getIcon("FileChooser.fileIcon"));
        this.table = Objects.requireNonNull(table);
        this.exporter = new AsyncExporter(Objects.requireNonNull(exporter),
                callback
        );
        this.format = format;
        putValue(SHORT_DESCRIPTION, "Exports selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        var fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter(getExtension(),getExtension()));
        fileChooser.setSelectedFile(new File("export." + getExtension()));

        int dialogResult = fileChooser.showSaveDialog(table);
        if (dialogResult == JFileChooser.APPROVE_OPTION) {
            String filePath = addFormatIfNotPresent(fileChooser.getSelectedFile().getAbsolutePath());

            try {
                exporter.exportData(filePath);
            } catch (DataManipulationException ex) {
                JOptionPane.showMessageDialog(
                        null, "Something went wrong with the export. Please try again.");
            }
        }
    }

    private String getExtension() {
        return format.getExtensions().stream().findFirst().orElse(format.getName());
    }

            
    private String addFormatIfNotPresent(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        if (!extension.equals(getExtension())) {
            filePath = filePath + "." + getExtension();
        }

        return filePath;
    }


    @Override public String toString() {
        return this.format.getName();
    }
}