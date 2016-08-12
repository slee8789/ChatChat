package leesc.chatchat.http;

public class Status {

    public final static int OK = 0;

    private int code;
    private String description;
    private String userMessage;
    private int activationCodeTC;
    private int activationCodeTL;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return description;
    }

    public void setMessage(String description) {
        this.description = description;
    }

    public int getActivationCodeTC() {
        return activationCodeTC;
    }

    public void setActivationCodeTC(int activationCodeTC) {
        this.activationCodeTC = activationCodeTC;
    }

    public int getActivationCodeTL() {
        return activationCodeTL;
    }

    public void setActivationCodeTL(int activationCodeTL) {
        this.activationCodeTL = activationCodeTL;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
}