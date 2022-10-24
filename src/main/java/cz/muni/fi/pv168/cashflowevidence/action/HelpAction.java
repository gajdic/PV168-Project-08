package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class HelpAction extends AbstractAction {

    public HelpAction() {
        super("Help");
        putValue(SHORT_DESCRIPTION, "Shows help dialog");
        putValue(MNEMONIC_KEY, KeyEvent.VK_H);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl H"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}