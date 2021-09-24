package burp;

import plugin.gui.RepeaterMessageEditorTab;
import plugin.gui.RepeaterTab;
import plugin.helpers.Config;
import plugin.helpers.Utils;
import plugin.helpers.log.PluginHandler;

import java.io.PrintStream;
import java.util.logging.Logger;

//https://github.com/PortSwigger/json-web-tokens/blob/master/src/burp/BurpExtender.java
//https://github.com/RUB-NDS/JOSEPH/blob/master/src/main/java/burp/BurpExtender.java
//https://stackoverflow.com/questions/30292766/how-can-i-set-up-separate-streams-of-logging-for-log4j
public class BurpExtender implements IBurpExtender, IMessageEditorTabFactory {

	static Logger logger = Logger.getLogger(BurpExtender.class.getName());

	IExtensionHelpers helpers = null;
	IBurpExtenderCallbacks callbacks = null;
	Utils utils=null;
	public static PrintStream errStream;
	public static PrintStream outStream;



	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		this.callbacks = callbacks;


		this.errStream = new PrintStream(this.callbacks.getStderr());
		this.outStream = new PrintStream(this.callbacks.getStdout());
		this.utils = new Utils(callbacks);
		//logger.setLevel(Level.INFO);
		logger.addHandler(new PluginHandler());
		callbacks.setExtensionName(Config.EXTENSION_NAME);

		this.helpers = callbacks.getHelpers();
	
		   // register ourselves as a message editor tab factory
        callbacks.registerMessageEditorTabFactory(this);

		RepeaterTab RioTab = new RepeaterTab(callbacks);
		callbacks.addSuiteTab(RioTab);


	}



	public IMessageEditorTab createNewInstance(IMessageEditorController controller, boolean editable) {
		return new RepeaterMessageEditorTab(controller,editable,this.callbacks);
	}

}
