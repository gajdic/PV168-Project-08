package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class ExportOptionsAction extends AbstractAction{
    private final List<Action> exportActions;

    public ExportOptionsAction(List<Action> exportActions) {
        super("Export", UIManager.getIcon("FileChooser.fileIcon"));
        this.exportActions = exportActions;
        putValue(SHORT_DESCRIPTION, "Exports selected record");
        putValue(MNEMONIC_KEY, KeyEvent.VK_X);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        OptionsSelector.selectAction(exportActions, actionEvent,
                "Choose export format", "Export");
    }

}
