package plugin.gui;

import burp.IBurpExtenderCallbacks;


import javax.swing.*;

public class RIOSuiteTabView extends JTabbedPane {
    IBurpExtenderCallbacks callbacks;
    public RIOSuiteTabView(IBurpExtenderCallbacks callbacks) {
        this.callbacks=callbacks;
    initComponents();
    }
    private void initComponents() {

        this.addTab("Settings", new SettingsPanel(this.callbacks));
      //  this.addTab("Decoder", new JPanel());
        this.addTab("Help", new HelpPanel());
    }
}
