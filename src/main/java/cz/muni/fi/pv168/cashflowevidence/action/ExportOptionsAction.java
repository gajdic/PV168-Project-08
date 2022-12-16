package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class ExportOptionsAction extends AbstractAction{
    private final List<ExportAction> exportActions;

    public ExportOptionsAction(List<ExportAction> exportActions) {
        super("Export", UIManager.getIcon("FileChooser.fileIcon"));
        this.exportActions = exportActions;
        putValue(SHORT_DESCRIPTION, "Exports selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        int exportFormat = chooseExporter();

        for (int i = 0; i < exportActions.size(); i++) {
            if (exportFormat == i) {
                exportActions.get(i).actionPerformed(actionEvent);
            }
        }
    }

    private int chooseExporter() {
        Object[] options = exportActions.stream()
                .map(ExportAction::toString).toArray();
        return JOptionPane.showOptionDialog(
                null,
                "Choose export format", "Export",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]
        );
    }
}
