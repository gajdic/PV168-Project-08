package cz.muni.fi.pv168.cashflowevidence.action;

import com.google.common.io.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HelpAction extends AbstractAction {

    public HelpAction() {
        super("Information");
        putValue(SHORT_DESCRIPTION, "Shows information about the app");
        putValue(MNEMONIC_KEY, KeyEvent.VK_N);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl H"));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFrame frame = new JFrame("Information");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(400, 500));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(400, 500));

        JTextPane textArea = new JTextPane();
        textArea.setMargin(new Insets(10, 10, 10, 10));

        URL url = Resources.getResource("help_text");
        try {
            String text = Resources.toString(url, StandardCharsets.UTF_8);
            textArea.setText(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        textArea.setEditable(false);

        frame.add(textArea);

        frame.setVisible(true);
    }
}