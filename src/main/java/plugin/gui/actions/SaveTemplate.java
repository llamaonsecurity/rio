package plugin.gui.actions;

import plugin.gui.SettingsPanel;
import plugin.helpers.Config;
import plugin.helpers.log.PluginHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class SaveTemplate implements ActionListener {
    static Logger logger = Logger.getLogger(SaveTemplate.class.getName());

    int mode ;
    SettingsPanel settingsPanel;

    public SaveTemplate( SettingsPanel settingsPanel,int mode) {
        this.settingsPanel=settingsPanel;
        this.mode = mode;
        logger.addHandler(new PluginHandler());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //this.settingsPanel.utils.saveSettings(Config.EXTENSION_TEMPLATE_MODE_KEY,"test");
        String templateContent = this.settingsPanel.getTemplateContent();
        logger.info("saving mode - "+this.mode);
        if(mode==0) {

            this.settingsPanel.utils.saveExtensionSettings(Config.EXTENSION_SAVED_TEMPLATE_KEY, templateContent);
            this.settingsPanel.utils.saveSettings(Config.EXTENSION_SAVED_TEMPLATE_KEY,"");

        }else {
            this.settingsPanel.utils.saveSettings(Config.EXTENSION_SAVED_TEMPLATE_KEY, templateContent);
        }


    }
}
