package plugin.model;

import burp.BurpExtender;
import plugin.helpers.log.PluginHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Results {

	static Logger logger = Logger.getLogger(Results.class.getName());

	String title="User Update - Foo > Bar > Done";
	String target ="";
	String reqHeaders="";
	String reqBody="";
	boolean reqBase64Encoded=false;
	boolean reqHeadersOnly=false;
	String reqFirstLine = "";
	String resHeaders="";
	String resBody="";
	boolean resBase64Encoded=false;
	boolean resHeadersOnly=false;




	String URL="";



	String host = "";
	String port = "";
	String protocol = "";
	String status="";
	String statusCode="";



	private LinkedHashMap<String, String[]> reqHeadersForTemplate = new LinkedHashMap<>();
	private LinkedHashMap<String, String[]> resHeadersForTemplate = new LinkedHashMap<>();
	private LinkedHashMap<String, String[]> cookiesForTemplate = new LinkedHashMap<>();

	String currentTemplate="";
	public Results() {
		logger.addHandler(new PluginHandler());
	}
	public String getCurrentTemplate() {
		return currentTemplate;
	}

	public void setCurrentTemplate(String currentTemplate) {
		this.currentTemplate = currentTemplate;
	}

	public LinkedHashMap<String, String[]> getReqHeadersForTemplate() {
		return reqHeadersForTemplate;
	}

	public void setReqHeadersForTemplate(LinkedHashMap<String, String[]> reqHeadersForTemplate) {
		this.reqHeadersForTemplate = reqHeadersForTemplate;
	}

	public LinkedHashMap<String, String[]> getResHeadersForTemplate() {
		return resHeadersForTemplate;
	}

	public void setResHeadersForTemplate(LinkedHashMap<String, String[]> resHeadersForTemplate) {
		this.resHeadersForTemplate = resHeadersForTemplate;
	}

	public LinkedHashMap<String, String[]> getCookiesForTemplate() {
		return cookiesForTemplate;
	}

	public void setCookiesForTemplate(LinkedHashMap<String, String[]> cookiesForTemplate) {
		this.cookiesForTemplate = cookiesForTemplate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getReqHeaders() {
		return reqHeaders;
	}
	public void setReqHeaders(String reqHeaders) {
		this.reqHeaders = reqHeaders;
	}
	public String getReqBody() {
		return reqBody;
	}
	public void setReqBody(String reqBody) {
		this.reqBody = reqBody;
	}


	public String getResHeaders() {
		return resHeaders;
	}
	public void setResHeaders(String resHeaders) {
		this.resHeaders = resHeaders;
	}
	public String getResBody() {
		return resBody;
	}
	public void setResBody(String resBody) {
		this.resBody = resBody;
	}

	
	public boolean isReqHeadersOnly() {
		return reqHeadersOnly;
	}
	public void setReqHeadersOnly(boolean reqHeadersOnly) {
		this.reqHeadersOnly = reqHeadersOnly;
	}
	public boolean isResHeadersOnly() {
		return resHeadersOnly;
	}
	public void setResHeadersOnly(boolean resHeadersOnly) {
		this.resHeadersOnly = resHeadersOnly;
	}
	
	
	public boolean isReqBase64Encoded() {
		return reqBase64Encoded;
	}
	public void setReqBase64Encoded(boolean reqBase64Encoded) {
		this.reqBase64Encoded = reqBase64Encoded;
	}
	public boolean isResBase64Encoded() {
		return resBase64Encoded;
	}
	public void setResBase64Encoded(boolean resBase64Encoded) {
		this.resBase64Encoded = resBase64Encoded;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getReqFirstLine() {
		return reqFirstLine;
	}

	public void setReqFirstLine(String reqFirstLine) {
		this.reqFirstLine = reqFirstLine;
	}

	@Override
	public String toString() {


		try {
			String content = getCurrentTemplate();
			content = content.replaceAll("_title_", getTitle());
			content = content.replaceAll("_target_", getTarget());

			content = content.replaceAll("_url_", getURL());
			content = content.replaceAll("_port_", getPort());
			content = content.replaceAll("_host_", getHost());
			content = content.replaceAll("_protocol_", getProtocol());

			content = content.replaceAll("_request_fl_", getReqFirstLine());
			content = content.replaceAll("_request_headers_", getReqHeaders());
			if(!isReqHeadersOnly()) {
				content = content.replaceAll("_request_content_", java.util.regex.Matcher.quoteReplacement(getReqBody()));
			}else{
				content = content.replaceAll("_request_content_","");
			}

			content = content.replaceAll("_response_headers_", getResHeaders());
			content = content.replaceAll("_response_status_", getStatus());
			content = content.replaceAll("_response_code_", getStatusCode());
			if(!isResHeadersOnly()) {
				content = content.replaceAll("_response_content_", java.util.regex.Matcher.quoteReplacement(getResBody()));

			}else{
				content = content.replaceAll("_response_content_","");
			}
			//Replace req headers
			content = processUserDefinedTemplates(getReqHeadersForTemplate(),content,"_request_header[%s]_");
			content = processUserDefinedTemplates(getResHeadersForTemplate(),content,"_response_header[%s]_");
			content = processUserDefinedTemplates(getCookiesForTemplate(),content,"_cookie[%s]_");
			 return content;
		}catch(Exception e){
		e.printStackTrace(BurpExtender.errStream);
		}


		return "Error during template parsing";
	}

	public String processUserDefinedTemplates(LinkedHashMap<String, String[]> map,String content,String pattern){
		//Replace req headers
		for (Map.Entry<String, String[]> set : map.entrySet()) {
			//
			String[] templateValues = set.getValue();
			//rebuild keys
			String templateKey;
			if(templateValues.length>1){
				//append * to handle i.e _request_header[%s]_ to _request_header[%s*]_
				String patternExtended = pattern.replace("]_","*]_");
				templateKey = String.format(patternExtended,set.getKey());
			}else{
				templateKey = String.format(pattern,set.getKey());
			}
			StringBuilder templateContent = new StringBuilder();
			for(String value : set.getValue()){
				templateContent.append(value).append("\r\n");

			}
			String templateCandidate = templateContent.toString();
			//remove last /r/n
			templateCandidate= templateCandidate.replaceAll("[\n\r]$", "");

			logger.fine(templateKey);
			String lookForTemplateKey = Pattern.quote(templateKey).replace("**", "*");
			logger.fine(lookForTemplateKey);
			content = content.replaceAll(lookForTemplateKey,templateCandidate);
		}
		return content;
	}


}
