package plugin.helpers.log;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PluginFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
       /* return record.getThreadID()+"::"+record.getSourceClassName()+"::"
                +record.getSourceMethodName()+"::"
                +new Date(record.getMillis())+"::"
                +record.getMessage()+"\n";*/
        StringBuilder sb = new StringBuilder();
        String logSeparator = "::";
        sb.append(record.getSourceClassName()).append(logSeparator);
        sb.append(record.getSourceMethodName()).append(logSeparator);
        Date date = new Date(record.getMillis());

        sb.append(date.toGMTString()).append(": ");
        sb.append(record.getMessage()).append("\n");
        return sb.toString();
    }

}

