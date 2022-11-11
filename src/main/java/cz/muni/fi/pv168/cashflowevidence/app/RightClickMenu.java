package cz.muni.fi.pv168.cashflowevidence.app;

import javax.swing.Action;
import javax.swing.JPopupMenu;
import java.util.List;

public class RightClickMenu  extends JPopupMenu {

    public RightClickMenu(List<Action> actions) {
        super();

        for (Action action: actions) {
            this.add(action);
        }
    }
}
