package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ExitApplicationAction extends AbstractAction {
    public ExitApplicationAction() {
        super("Exit");
        putValue(SHORT_DESCRIPTION, "Exits the application");
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Q"));

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
