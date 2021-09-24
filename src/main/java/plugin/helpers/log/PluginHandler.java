package plugin.helpers.log;
import burp.BurpExtender;
import plugin.helpers.Config;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class PluginHandler extends StreamHandler {

    public PluginHandler(){
        //Log everything to burp output stream
        this.setOutputStream(BurpExtender.outStream);
        this.setFormatter(new PluginFormatter());
        this.setLevel(Config.LOG_LEVEL);
    }
    @Override
    public void publish(LogRecord record) {
        //add own logic to publish

        super.publish(record);
    }


    @Override
    public void flush() {
        super.flush();
    }


    @Override
    public void close() throws SecurityException {
        super.close();
    }

}
