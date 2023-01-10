package cz.muni.fi.pv168.cashflowevidence.action;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class OptionsSelector {

    static void selectAction (List<Action> actionList, ActionEvent actionEvent,
                              String message, String title) {
        Object[] options = actionList.stream()
                .map(Action::toString).toArray();
        int selected =  JOptionPane.showOptionDialog(
                null,
                message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        for (int i = 0; i < options.length; i++) {
            if (selected == i) {
                actionList.get(i).actionPerformed(actionEvent);
            }
        }

    }
}
