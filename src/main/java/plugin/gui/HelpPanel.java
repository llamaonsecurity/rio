package plugin.gui;

import burp.BurpExtender;
import plugin.helpers.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HelpPanel extends JPanel {

    public HelpPanel(){
        //this.setLayout(new BorderLayout());
    //
        JPanel contentPanel= new JPanel();
        contentPanel.setLayout(new BorderLayout());
        //contentPanel.setBorder(new TitledBorder("Help:"));

        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel wikiPanel = new JPanel();
        wikiPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel helpText= new JLabel();
        helpText.setText("You can find usage guide at ");
        JLabel helpLink = new JLabel();
        helpLink.setText("<html><a href=\"" + Config.HELP_URL + "\">RIO wiki</a>");
        helpLink.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {

                    Desktop.getDesktop().browse(new URI(Config.HELP_URL));

                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace(BurpExtender.errStream);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // the mouse has entered the label
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // the mouse has exited the label
            }
        });
        this.add(contentPanel);
        wikiPanel.add(helpText);
        wikiPanel.add(helpLink);
        JLabel pluginName = new JLabel();
        pluginName.setText(Config.EXTENSION_NAME+" "+Config.VERSION);
        Font f = pluginName.getFont();

        pluginName.setFont(f.deriveFont(Font.BOLD));
        contentPanel.add(pluginName,BorderLayout.NORTH);
        contentPanel.add(wikiPanel,BorderLayout.CENTER);


    }
}
