package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class ImportOptionsAction extends AbstractAction{
    private final List<Action> importActions;

    public ImportOptionsAction(List<Action> importActions) {
        super("Import", UIManager.getIcon("FileChooser.fileIcon"));
        this.importActions = importActions;
        putValue(SHORT_DESCRIPTION, "Imports selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        OptionsSelector.selectAction(importActions, actionEvent,
                "Choose import format", "Import");
    }
}
