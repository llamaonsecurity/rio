package plugin.gui;

import burp.IBurpExtenderCallbacks;
import burp.ITab;
import plugin.helpers.Config;


import java.awt.*;

public class RepeaterTab implements ITab {
    IBurpExtenderCallbacks callbacks;

    public RepeaterTab(IBurpExtenderCallbacks callbacks) {
    this.callbacks=callbacks;
    }

    @Override
    public String getTabCaption() {
        return Config.EXTENSION_NAME;
    }

    @Override
    public Component getUiComponent() {
        RIOSuiteTabView view;
        view = new RIOSuiteTabView(callbacks);

        return view;
    }
}
