package plugin.gui;

import burp.IBurpExtenderCallbacks;
import plugin.gui.actions.LoadTemplateActionListener;
import plugin.gui.actions.SaveTemplate;
import plugin.helpers.Utils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SettingsPanel extends JPanel {
    IBurpExtenderCallbacks callbacks;

    public Utils utils;

    JTextArea templateContent;

    public SettingsPanel(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.utils = new Utils(callbacks);
        this.setLayout(new BorderLayout());

        //TopSide
        JPanel top = new JPanel();
        JButton loadTemplate = new JButton("Load template");
        loadTemplate.addActionListener(new LoadTemplateActionListener(this));
        JButton savePerProject = new JButton("Save current template (Project)");
        savePerProject.addActionListener(new SaveTemplate(this, 1));
        JButton saveForPlugin = new JButton("Save current template (Plugin)");
        saveForPlugin.addActionListener(new SaveTemplate(this, 0));
        top.add(loadTemplate);
        top.add(savePerProject);
        top.add(saveForPlugin);
        this.add(top, BorderLayout.NORTH);

        //Center
        templateContent = new JTextArea();
        templateContent.setBorder(new TitledBorder("Current template"));
        templateContent.setText(this.utils.getCurrentTemplate());


        this.add(templateContent, BorderLayout.CENTER);

    }

    public void updateContent(String text) {
        templateContent.setText(text);
    }

    public String getTemplateContent() {
        return templateContent.getText();
    }
}
