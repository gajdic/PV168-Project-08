package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ExitApplication extends AbstractAction {
    @Override
    public void actionPerformed(ActionEvent e) {
        putValue(SHORT_DESCRIPTION, "Exits the application");
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
        System.exit(0);
    }
}
