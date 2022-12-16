package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class ImportOptionsAction extends AbstractAction{
    private final List<ImportAction> importActions;

    public ImportOptionsAction(List<ImportAction> exportActions) {
        super("Import", UIManager.getIcon("FileChooser.fileIcon"));
        this.importActions = exportActions;
        putValue(SHORT_DESCRIPTION, "Imports selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        int exportFormat = chooseExporter();

        for (int i = 0; i < importActions.size(); i++) {
            if (exportFormat == i) {
                importActions.get(i).actionPerformed(actionEvent);
            }
        }
    }

    private int chooseExporter() {
        Object[] options = importActions.stream()
                .map(ImportAction::toString).toArray();
        return JOptionPane.showOptionDialog(
                null,
                "Choose import format", "Import",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]
        );
    }
}
