package plugin.helpers;

import plugin.gui.actions.LoadTemplateActionListener;
import plugin.helpers.log.PluginHandler;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 public  class TemplateUtils {
     static Logger logger = Logger.getLogger(TemplateUtils.class.getName());

     public TemplateUtils() {
         logger.addHandler(new PluginHandler());
     }

     //ParseHeaders,Cookies in response and request
    public LinkedHashMap<String,String[]>  parseRequestHeaders(String template ){
        logger.info("parseRequestHeaders");
        Pattern p = Pattern.compile("_request_header\\[.*\\]_");   // the pattern to search for
        logger.info(p.pattern());
        Matcher m = p.matcher(template);

        // if we find a match, get the group
        LinkedHashMap<String,String[]> map = new LinkedHashMap<>();
        while (m.find())
        {
            // we're only looking for one group, so get it
            String theGroup = m.group();

            // print the group out for verification
            logger.info(theGroup);
            String headerName= theGroup.replace("_request_header[","").replace("]_","");
            logger.info(headerName);
            map.put(headerName,new String[]{});
        }
        return map;
    }

     public LinkedHashMap<String,String[]>  parseResponseHeaders(String template ){
         logger.info("parseResponseHeaders");
         Pattern p = Pattern.compile("_response_header\\[.*\\]_");   // the pattern to search for
         logger.info(p.pattern());
         Matcher m = p.matcher(template);

         // if we find a match, get the group
         LinkedHashMap<String,String[]> map = new LinkedHashMap<>();
         while (m.find())
         {
             // we're only looking for one group, so get it
             String theGroup = m.group();

             // print the group out for verification
             logger.info(theGroup);
             String headerName= theGroup.replace("_response_header[","").replace("]_","");
             logger.info(headerName);
             map.put(headerName,new String[]{});
         }
         return map;
     }

     public LinkedHashMap<String,String[]>  parseCookies(String template ){
         logger.info("parseCookies");
         Pattern p = Pattern.compile("_cookie\\[.*\\]_");   // the pattern to search for
         logger.info(p.pattern());
         Matcher m = p.matcher(template);

         // if we find a match, get the group
         LinkedHashMap<String,String[]> map = new LinkedHashMap<>();
         while (m.find())
         {
             // we're only looking for one group, so get it
             String theGroup = m.group();

             // print the group out for verification
             logger.info(theGroup);
             String headerName= theGroup.replace("_cookie[","").replace("]_","");
             logger.info(headerName);
             map.put(headerName,new String[]{});
         }
         return map;
     }
}
