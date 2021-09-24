package plugin.gui;

import burp.*;
import plugin.helpers.Config;
import plugin.helpers.TemplateUtils;
import plugin.helpers.Utils;
import plugin.helpers.log.PluginHandler;
import plugin.model.Results;

import java.util.logging.Level;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class RepeaterMessageEditorTab implements IMessageEditorTab {

    static Logger logger = Logger.getLogger(RepeaterMessageEditorTab.class.getName());

	private final ITextEditor txtInput;
    IBurpExtenderCallbacks callbacks;
    IExtensionHelpers helpers;
    PrintStream errorPS;
    Utils utils;
    public JPanel mainPanel = new JPanel();
	private byte[] currentMessage;
    IMessageEditorController controller;

    Results results;
    JTextField titleField = new JTextField();

    private String lastUsedTemplate = "";
    private LinkedHashMap<String, String[]> reqHeaders = new LinkedHashMap<>();
    private LinkedHashMap<String, String[]> resHeaders = new LinkedHashMap<>();
    private LinkedHashMap<String, String[]> cookies = new LinkedHashMap<>();

    List<String> reqHeadersTemp = new ArrayList<>();
    List<String> resHeadersTemp = new ArrayList<>();
    List<String> cookiesTemp = new ArrayList<>();

    public RepeaterMessageEditorTab(IMessageEditorController controller, boolean editable, IBurpExtenderCallbacks callbacks) {

		this.callbacks = callbacks;
        this.utils = new Utils(callbacks);
        this.results = new Results();
        this.helpers = this.callbacks.getHelpers();
        if (controller != null) {
            logger.info("Controller - " + controller.getClass().getCanonicalName());
        } else {
            logger.info("Controller is empty");
        }


        //logger.setLevel(Level.INFO);
        logger.addHandler(new PluginHandler());

        setupTemplate();
        logger.info("Editable: " + editable);
        logger.info("Info log");
        logger.warning("warning log");
        logger.severe("severe log");

        this.controller = controller;

        txtInput = this.callbacks.createTextEditor();
        errorPS = new PrintStream(this.callbacks.getStderr());
        txtInput.setEditable(true);
        mainPanel.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        /* UI - Title - start */
        titleField.setBorder(new TitledBorder("Title"));
        titleField.setText("User Update - Foo > Bar > Done");

        topPanel.setLayout(new BorderLayout());
        topPanel.add(titleField, BorderLayout.CENTER);

        titleField.getDocument().addDocumentListener(new DocumentListener() {

            public void removeUpdate(DocumentEvent e) {
                String text = titleField.getText();
                results.setTitle(text);
                updateContent();

            }

            public void insertUpdate(DocumentEvent e) {
                String text = titleField.getText();
                results.setTitle(text);
                updateContent();

            }

            public void changedUpdate(DocumentEvent e) {
                String text = titleField.getText();
                results.setTitle(text);
                updateContent();

            }
        });
        /* UI - Title - end */

        /* UI - Options - start */
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BorderLayout());
        optionsPanel.setBorder(new TitledBorder("Options:"));

        /* UI - Options - Request - start */
        JPanel requestOptions = new JPanel(new BorderLayout());
        requestOptions.setBorder(new TitledBorder("Request:"));
        JCheckBox reqHeadersCheckBox = new JCheckBox("Headers only");
        JCheckBox reqEncodeCheckBox = new JCheckBox("Encode body with bas64");

        reqHeadersCheckBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                results.setReqHeadersOnly(e.getStateChange() == ItemEvent.SELECTED);
                updateContent();

            }
        });

        reqEncodeCheckBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                boolean state = e.getStateChange() == ItemEvent.SELECTED;
                results.setReqBase64Encoded(state);
                if (state) {
                    String base64Encode = helpers.base64Encode(results.getReqBody());
                    results.setReqBody(base64Encode);
                } else {
                    byte[] base64Decode = helpers.base64Decode(results.getReqBody());
                    results.setReqBody(helpers.bytesToString(base64Decode));
                }
                updateContent();

            }
        });


        requestOptions.add(reqHeadersCheckBox, BorderLayout.CENTER);
        requestOptions.add(reqEncodeCheckBox, BorderLayout.SOUTH);
        /* UI - Options - Request - end */

        /* UI - Options - Response - start */
        JPanel responseOptions = new JPanel(new BorderLayout());
        responseOptions.setBorder(new TitledBorder("Response:"));
        JCheckBox resHeadersCheckBox = new JCheckBox("Headers only");
        JCheckBox resEncodeCheckBox = new JCheckBox("Encode body with bas64");

        responseOptions.add(resHeadersCheckBox, BorderLayout.NORTH);
        responseOptions.add(resEncodeCheckBox, BorderLayout.SOUTH);


        resHeadersCheckBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                results.setResHeadersOnly(e.getStateChange() == ItemEvent.SELECTED);
                updateContent();

            }
        });

        resEncodeCheckBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                boolean state = e.getStateChange() == ItemEvent.SELECTED;
                results.setResBase64Encoded(state);
                if (state) {
                    String base64Encode = helpers.base64Encode(results.getResBody());
                    results.setResBody(base64Encode);
                } else {
                    byte[] base64Decode = helpers.base64Decode(results.getResBody());
                    results.setResBody(helpers.bytesToString(base64Decode));
                }
                updateContent();

            }
        });
        /* UI - Options - Response - end */

        optionsPanel.add(requestOptions, BorderLayout.NORTH);

        optionsPanel.add(responseOptions, BorderLayout.SOUTH);
        topPanel.add(optionsPanel, BorderLayout.SOUTH);
        /* UI - Options - end */

        mainPanel.add(topPanel, BorderLayout.NORTH);

        mainPanel.add(txtInput.getComponent(), BorderLayout.CENTER);

        txtInput.getComponent().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger())
                    showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger())
                    showPopup(e);
            }
        });

        callbacks.customizeUiComponent(mainPanel);
    }

    void showPopup(MouseEvent me) {
        String selectedText = selectedText();
        if (me.isPopupTrigger() && selectedText.length() != 0) {

            results.setResBody(selectedText);
            updateContent();
        }
    }


    private String selectedText() {
        byte[] selectedData = getSelectedData();
        String bytesToString = "";
        if (selectedData != null) {
            bytesToString = helpers.bytesToString(selectedData);
            logger.fine(bytesToString);
        }
        return bytesToString;
    }


    public String getTabCaption() {
        return Config.EXTENSION_NAME;
    }

    public void updateContent() {
        logger.info("updateContent !");

        if (txtInput != null) {
            setupTemplate();
            txtInput.setText(helpers.stringToBytes(this.results.toString()));
        }


    }



    public Component getUiComponent() {

        return mainPanel;
    }

    public boolean isEnabled(byte[] content, boolean isRequest) {
//TODO: DISABLE in Scanner, Target Issue section ?

        return isRequest & content.length != 0;
    }

    public void setupTemplate() {
        String currentTemplate = this.utils.getCurrentTemplate();
        TemplateUtils templateUtils = new TemplateUtils();

        if (reqHeaders.size() == 0 && resHeaders.size() == 0 && cookies.size() == 0) {
            //Compare maps
            logger.info("reqHeaders size " + reqHeaders.size());
            logger.info("resHeaders size " + resHeaders.size());
            logger.info("cookies size " + cookies.size());
            this.results.setReqHeadersForTemplate(reqHeaders);
            this.results.setResHeadersForTemplate(resHeaders);
            this.results.setCookiesForTemplate(cookies);
        }
        boolean templateChanged = currentTemplate.equals(lastUsedTemplate);
        logger.fine("templateChanged " + !templateChanged);
        logger.fine(String.format("comparing %s with %s", currentTemplate, lastUsedTemplate));
        if (!templateChanged) {
            //First run or template change - load new headers
            //Do not update reqHeaders if the template hasn't changed it will prevent the override of values
            this.reqHeaders = templateUtils.parseRequestHeaders(currentTemplate);
            this.resHeaders = templateUtils.parseResponseHeaders(currentTemplate);
            this.cookies = templateUtils.parseCookies(currentTemplate);
            lastUsedTemplate = currentTemplate;

            this.results.setReqHeadersForTemplate(reqHeaders);
            this.results.setResHeadersForTemplate(resHeaders);
            this.results.setCookiesForTemplate(cookies);

            //parseTemp in case of template change after setMessage()
            for (String header : reqHeadersTemp) {
                parseUserHeadersOrCookies(header, this.reqHeaders, false);
            }
            for (String header : resHeadersTemp) {
                parseUserHeadersOrCookies(header, this.resHeaders, false);
            }
            for (String cookie : cookiesTemp) {
                parseUserHeadersOrCookies(cookie, this.cookies, true);
            }

        }
        //We can't use userDefines template values without re-initialization

        this.results.setCurrentTemplate(currentTemplate);

    }

    private void debugReqHeaders() {
        for (Map.Entry<String, String[]> set : reqHeaders.entrySet()) {
            logger.warning(set.getKey() + " values : size - " + set.getValue().length);
            for (String value : set.getValue()) {
                logger.warning("value: " + value);
            }

        }
    }


    public void setMessage(byte[] content, boolean isRequest) {

        IRequestInfo requestInfo;
        IResponseInfo responseInfo;
        logger.fine("SetMessage !");
        //Check template

        //Do nothing on null editor
        if (txtInput != null) {
            setupTemplate();

            if (content == null) {
                // clear our display
                txtInput.setText("none".getBytes());
                txtInput.setEditable(false);
            } else {
                currentMessage = content;

                if (isRequest) {
                    try {
                        if (controller != null) {
                            IHttpService httpService = controller.getHttpService();
                            requestInfo = helpers.analyzeRequest(httpService, content);
                            int bodyOffset = requestInfo.getBodyOffset();

                            byte[] requestBody = Arrays.copyOfRange(content, bodyOffset, content.length);
                            parseRequest(requestInfo, requestBody, this.results.isReqHeadersOnly(),
                                    this.results.isReqBase64Encoded());

                            byte[] response = controller.getResponse();

                            if (response != null && response.length != 0) {
                                responseInfo = helpers.analyzeResponse(response);
                                int responseBodyOffset = responseInfo.getBodyOffset();


                                    byte[] responseBody = Arrays.copyOfRange(response, responseBodyOffset, response.length);
                                    parseResponse(responseInfo, responseBody, this.results.isResHeadersOnly(),
                                            this.results.isResBase64Encoded());

                            }
                        } else {
                            logger.log(Level.WARNING, "Empty controller");
                        }
                    } catch (Exception e) {

                        e.printStackTrace(errorPS);

                    }

                }
                updateContent();
            }
        }
    }

    public void parseRequest(IRequestInfo requestInfo, byte[] body, boolean headersOnly, boolean encode) {
        // Request description
        try {
            StringBuilder sb = new StringBuilder();

            results.setURL(requestInfo.getUrl().toExternalForm());
            String protocol = requestInfo.getUrl().getProtocol();
            results.setProtocol(protocol);
            // sb.append(protocol).append(";");
            String host = requestInfo.getUrl().getHost();
            results.setHost(host);
            sb.append(host).append(":");
            String port = String.valueOf(requestInfo.getUrl().getPort());
            results.setPort(port);
            sb.append(port);
            sb.append(" (").append(protocol).append(")");

            results.setTarget(sb.toString());

        } catch (Exception e) {

            e.printStackTrace(errorPS);
        }
        // Request headers
        // 1. Get the list of headers in template

        try {

            List<String> headers = requestInfo.getHeaders();
            reqHeadersTemp = headers;
            if (!headers.isEmpty()) {
                results.setReqFirstLine(headers.get(0));
            }
            StringBuilder sb = new StringBuilder();
            logger.info("Headers in request : " + headers.size());
            //resetValues of the map
            resetHeadersOrCookies(reqHeaders);
            resetHeadersOrCookies(cookies);

            for (String header : headers) {
                logger.info(header);

                sb.append(header).append("\r\n");
                // Minimalistic

                //compareMap if not equal generate new
                //Parse user defined headers

                parseUserHeadersOrCookies(header, this.reqHeaders, false);
                //Parse request cookies
                if (header.startsWith("Cookie:")) {
                    logger.info("CookiesHeader " + header);
                    String clearCookies = header.replaceAll("^Cookie: ", "");
                    String[] cookiesTab = clearCookies.split("; ");
                    cookiesTemp = Arrays.asList(cookiesTab);
                    for (String cookie : cookiesTab) {
                        logger.info("Cookie " + cookie);
                        parseUserHeadersOrCookies(cookie, this.cookies, true);
                    }
                }
            }
            debugReqHeaders();
            results.setReqHeaders(sb.toString());


        } catch (Exception e) {

            e.printStackTrace(errorPS);
        }
        if (!headersOnly) {
            try {

                String requestBody;

                if (encode) {
                    requestBody = helpers.base64Encode(body);
                } else {

                    requestBody = helpers.bytesToString(body);
                }
                results.setReqBody(requestBody);

            } catch (Exception e) {

                e.printStackTrace(errorPS);
            }
        }
    }

    private void resetHeadersOrCookies(LinkedHashMap<String, String[]> map) {
        for (Map.Entry<String, String[]> set : map.entrySet()) {
            set.setValue(new String[]{});
        }
    }

    private void parseUserHeadersOrCookies(String headerOrCookie, LinkedHashMap<String, String[]> map, boolean parsingCookies) {
        for (Map.Entry<String, String[]> set : map.entrySet()) {
            String[] candidatesValues = set.getValue();
            logger.info("Looking for header - " + set.getKey());
            String headerCandidate = set.getKey();
            if (headerCandidate.endsWith("*")) {

                String newHeaderValue = headerCandidate.replaceAll("\\*$", "");
                logger.info("StartsWitch " + newHeaderValue);
                if (headerOrCookie.startsWith(newHeaderValue)) { //append the value to new array
                    String[] newValue = new String[]{headerOrCookie};
                    if (candidatesValues.length == 0) {//first matching element
                        set.setValue(newValue);
                    } else {
                        String[] both = Stream.concat(Arrays.stream(candidatesValues), Arrays.stream(newValue))
                                .toArray(String[]::new);
                        set.setValue(both);
                    }
                }
            } else { //add single value
                //append :
                if (parsingCookies) {
                    headerCandidate = headerCandidate.concat("=");
                } else {
                    headerCandidate = headerCandidate.concat(":");
                }

                boolean b = headerOrCookie.startsWith(headerCandidate);
                if (b) {
                    set.setValue(new String[]{headerOrCookie});
                }
                logger.info(String.format("Comparing %s with %s with result %s ", headerCandidate, headerOrCookie, b));

            }
        }
    }

    public void parseResponse(IResponseInfo responseInfo, byte[] body, boolean headersOnly, boolean encode) {
        // Response description
        try {


            results.setStatusCode(String.valueOf(responseInfo.getStatusCode()));
        } catch (Exception e) {

            e.printStackTrace(errorPS);
        }
        // Response headers
        try {

            List<String> headers = responseInfo.getHeaders();

            resHeadersTemp = headers;
            if (!headers.isEmpty()) {
                String responseStatus = headers.get(0);

                results.setStatus(responseStatus);
            }

            StringBuilder sb = new StringBuilder();
            resetHeadersOrCookies(resHeaders);
            for (String header : headers) {
                //debugPrint(header);
                sb.append(header).append("\r\n");
                //Parse user defined headers
                parseUserHeadersOrCookies(header, this.resHeaders, false);
            }
            results.setResHeaders(sb.toString());
        } catch (Exception e) {

            e.printStackTrace(errorPS);
        }

        if (!headersOnly) {
            try {

                String responseBody;
                if (encode) {
                    responseBody = helpers.base64Encode(body);
                } else {

                    responseBody = helpers.bytesToString(body);
                }
                results.setResBody(responseBody);

            } catch (Exception e) {

                e.printStackTrace(errorPS);
            }
        }
    }

    public byte[] getMessage() {

        return currentMessage;
    }

    public boolean isModified() {
        if (txtInput != null) {
            return txtInput.isTextModified();
        } else {
            return false;
        }
    }

    public byte[] getSelectedData() {
        if (txtInput != null) {
            return txtInput.getSelectedText();
        } else {
            return "".getBytes();
        }

    }

}
