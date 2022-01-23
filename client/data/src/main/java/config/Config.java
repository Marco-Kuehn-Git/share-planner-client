package config;

public class Config {
    private boolean saveLogin;
    private long id;
    private String token;

    public Config(){

    }

    public Config(boolean saveLogin, long id, String token) {
        this.saveLogin = saveLogin;
        this.id = id;
        this.token = token;
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
}
