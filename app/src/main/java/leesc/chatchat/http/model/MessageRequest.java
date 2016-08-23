package leesc.chatchat.http.model;

import com.google.api.client.util.Key;

/**
 * Created by HM on 2016. 8. 23..
 */
public class MessageRequest {

    @Key
    String senderEmail;
    @Key
    String receiverEmail;
    @Key
    String message;

    public MessageRequest(String senderEmail, String receiverEmail, String message) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.message = message;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
