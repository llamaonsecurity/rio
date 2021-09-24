package plugin.helpers;

import burp.IBurpExtenderCallbacks;
import burp.IHttpRequestResponse;
import plugin.helpers.log.PluginHandler;
import plugin.model.custom.CustomHttpService;
import plugin.model.custom.CustomRequestResponse;

import java.io.PrintWriter;
import java.util.logging.Logger;

import static plugin.helpers.Config.EXTENSION_SETTINGS_URL;


public class Utils {

    static Logger logger = Logger.getLogger(Utils.class.getName());

    IBurpExtenderCallbacks callbacks;
    private PrintWriter stdout;
    private PrintWriter stderr;
    public Utils(IBurpExtenderCallbacks callbacks) {
        logger.addHandler(new PluginHandler());
    this.callbacks = callbacks;
    }

    public void saveExtensionSettings(String key, String value) {
        this.callbacks.saveExtensionSetting(key, value);
    }

    public String loadExtensionSettings(String key) {
        return this.callbacks.loadExtensionSetting(key);
    }

    public void saveSettings(String key, String value) {
        // https://github.com/PortSwigger/response-clusterer/blob/master/ResponseClusterer.py#L336
        String request = "GET /" + key + " HTTP/1.0\r\n\r\n";
        request += "Ignore it, Extension generated request. It is a dirty hack to achive project based extansion settings";
        String response = "HTTP/1.1 200 OK\r\n" + value;
        CustomHttpService fakeService = new CustomHttpService(EXTENSION_SETTINGS_URL);
        CustomRequestResponse crr = new CustomRequestResponse(this.callbacks.getHelpers().stringToBytes(request),
                this.callbacks.getHelpers().stringToBytes(response), "Extension generated request - you can ignore it");
        crr.setHttpService(fakeService);
        callbacks.addToSiteMap(crr);
    }



    public String loadSettings(String key) {
       logger.fine("loading settings");
        String url = "http://" + EXTENSION_SETTINGS_URL + ":8080/" + key;
       logger.fine("url " + url);

        // Remove host from sitemap in case of null exception
        IHttpRequestResponse[] siteMap = callbacks.getSiteMap(url);
        String data = "";

        int length = siteMap.length;
        if (length != 0) {

            IHttpRequestResponse response = siteMap[0];

            data = this.callbacks.getHelpers().bytesToString(response.getResponse());
            if (data != null) {
                data = data.replace("HTTP/1.1 200 OK\r\n", "");
            }

        }
        return data;
    }

    public String getCurrentTemplate(){

        String savedTemplateFromProject = this.loadSettings(Config.EXTENSION_SAVED_TEMPLATE_KEY);
        String savedTemplate = this.loadExtensionSettings(Config.EXTENSION_SAVED_TEMPLATE_KEY);
        //Check for project template
        if (savedTemplateFromProject!=null && !savedTemplateFromProject.isEmpty()){
            return savedTemplateFromProject;
        }
        //Fallback to extension settings
        if (savedTemplate!=null && !savedTemplate.isEmpty()){
            return savedTemplate;
        }
        //If there is no extension and project-defined template - load default template

                return Config.DEFAULT_TEMPLATE;
    }

}
