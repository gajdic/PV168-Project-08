package cz.muni.fi.pv168.cashflowevidence.app;

import javax.swing.*;


public class RightClickMenu  extends JPopupMenu {

    public RightClickMenu(String containerContextName) {
        super();
        // TODO: Maybe parametrize this construction a bit
        // TODO: And maybe consider joining with the default popup class
        // TODO: Handle how the event listeners will know which action to trigger based on pressed element

        JMenuItem add = new JMenuItem("Add" + containerContextName);
        add.addActionListener(e -> {});
        this.add(add);

        JMenuItem create = new JMenuItem("Create" + containerContextName);
        create.addActionListener(e -> {});
        this.add(create);

        JMenuItem edit = new JMenuItem("Edit" + containerContextName);
        edit.addActionListener(e -> {});
        this.add(edit);

    }
}
