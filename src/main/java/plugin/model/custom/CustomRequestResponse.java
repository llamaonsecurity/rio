package plugin.model.custom;


import burp.IHttpRequestResponse;
import burp.IHttpService;

public class CustomRequestResponse implements IHttpRequestResponse {

    public CustomRequestResponse(byte[] request, byte[] response, String comment) {
        super();
        this.request = request;
        this.response = response;
        this.comment = comment;
    }

    byte[] request;
    byte[] response;
    String comment;
    String highlight;
    CustomHttpService httpService;

    public byte[] getRequest() {

        return request;
    }

    public void setRequest(byte[] message) {

        this.request = message;
    }

    public byte[] getResponse() {

        return response;
    }

    public void setResponse(byte[] message) {

        this.response = message;
    }

    public String getComment() {

        return comment;
    }

    public void setComment(String comment) {

        this.comment = comment;
    }

    public String getHighlight() {

        return highlight;
    }

    public void setHighlight(String color) {

        this.highlight = color;
    }

    public IHttpService getHttpService() {
        return httpService;
    }

    public void setHttpService(IHttpService httpService) {
        this.httpService = (CustomHttpService) httpService;
    }

}
