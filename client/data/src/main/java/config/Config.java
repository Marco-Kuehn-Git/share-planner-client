package config;

public class Config {
    private boolean saveLogin;
    private long id;
    private String token;
    private String connectionMethod;
    private String hostAddress;
    private int port;

    public Config(){
        saveLogin = false;
        id = -1;
        token = "";
        connectionMethod = "http";
        hostAddress = "localhost";
        port = 8080;
    }

    public Config(boolean saveLogin, long id, String token) {
        this.saveLogin = saveLogin;
        this.id = id;
        this.token = token;
        connectionMethod = "http";
        hostAddress = "localhost";
        port = 8080;
    }

    public boolean isSaveLogin() {
        return saveLogin;
    }

    public void setSaveLogin(boolean saveLogin) {
        this.saveLogin = saveLogin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConnectionMethod() {
        return connectionMethod;
    }

    public void setConnectionMethod(String connectionMethod) {
        this.connectionMethod = connectionMethod;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String toServerUrl(){
        return getConnectionMethod() + "://" + getHostAddress() + ":" + getPort();
    }
}
