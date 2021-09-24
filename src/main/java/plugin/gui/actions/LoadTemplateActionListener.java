package plugin.gui.actions;


import plugin.gui.SettingsPanel;
import plugin.helpers.Config;
import plugin.helpers.log.PluginHandler;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class LoadTemplateActionListener implements ActionListener {
    static Logger logger = Logger.getLogger(LoadTemplateActionListener.class.getName());
    SettingsPanel settingsPanel;
    public LoadTemplateActionListener(SettingsPanel settingsPanel) {
        this.settingsPanel=settingsPanel;
        logger.addHandler(new PluginHandler());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File startPath= FileSystemView.getFileSystemView().getHomeDirectory();
        String loadedLastPath = this.settingsPanel.utils.loadExtensionSettings(Config.EXTENSION_SETTINGS_LAST_DIRECTORY_KEY);
        if(loadedLastPath!=null){
            startPath=new File(loadedLastPath);
        }

        JFileChooser jfc = new JFileChooser(startPath);
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = jfc.showOpenDialog(this.settingsPanel);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {

            File file =   jfc.getSelectedFile();

            try {
                logger.info(file.getAbsolutePath());
                String lastDirPath=file.getPath();
                logger.info(lastDirPath);
                this.settingsPanel.utils.saveExtensionSettings(Config.EXTENSION_SETTINGS_LAST_DIRECTORY_KEY, lastDirPath);
                Path template =  Paths.get(lastDirPath);
                Charset charset = StandardCharsets.UTF_8;

                String content = new String(Files.readAllBytes(template), charset);

                //Update only the text area

                this.settingsPanel.updateContent(content);

            } catch (Exception exception) {

                logger.warning(exception.getMessage());
                }
            }


        }
    }

