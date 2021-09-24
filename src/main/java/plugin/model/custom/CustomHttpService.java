package plugin.model.custom;


import burp.IHttpService;

public class CustomHttpService implements IHttpService {

    String host;

    public CustomHttpService(String host) {
        super();
        this.host = host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {

        return host;
    }

    public int getPort() {

        return 8080;
    }

    public String getProtocol() {

        return "http";
    }

}

