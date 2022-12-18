package cz.muni.fi.pv168.cashflowevidence.UI;

import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class CustomComboBoxUI extends BasicComboBoxUI {

    @Override
    protected JButton createArrowButton() {
        return new BasicArrowButton(BasicArrowButton.SOUTH,
                UIManager.getColor("ComboBox.buttonShadow"),
                UIManager.getColor("ComboBox.buttonShadow"),
                UIManager.getColor("ComboBox.buttonDarkShadow"),
                UIManager.getColor("ComboBox.buttonHighlight"));
    }
}
