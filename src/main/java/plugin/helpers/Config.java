package plugin.helpers;

import java.util.logging.Level;

public class Config {
    public static java.util.logging.Level LOG_LEVEL = Level.ALL;
    public static String EXTENSION_NAME = "RIO";
    public static String EXTENSION_SETTINGS_URL = "rio-extension.local";
    public static String EXTENSION_SAVED_TEMPLATE_KEY = "rio-saved-template";
    public static String EXTENSION_SETTINGS_LAST_DIRECTORY_KEY = "rio-last-dir";
    public static String VERSION="0.7";
    public static String HELP_URL = "https://github.com/TheKalin/rio/docs";
    public static String DEFAULT_TEMPLATE ="# _title_\n" +
            "\n" +
            "URL: _url_\n" +
            "Burpsuite target settings: _target_\n" +
            "\n" +
            "```text\n" +
            "REQ:\n" +
            "_request_headers_\n" +
            "_request_content_\n" +
            "\n" +
            "RES:\n" +
            "_response_headers_\n" +
            "_response_content_\n" +
            "```\n";
}
