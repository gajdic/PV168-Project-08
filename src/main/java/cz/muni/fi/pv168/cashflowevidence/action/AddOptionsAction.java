package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class AddOptionsAction extends AbstractAction{
    private final List<Action> addActions;

    public AddOptionsAction(List<Action> addActions) {
        super("Add", UIManager.getIcon("FileChooser.fileIcon"));
        this.addActions = addActions;
        putValue(SHORT_DESCRIPTION, "Add new");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        OptionsSelector.selectAction(addActions, actionEvent,
                "What do you want to add?", "Add");
    }

}
