package leesc.chatchat.http.model;

import com.google.api.client.util.Key;

/**
 * Created by hmlee on 2016-08-22.
 */
public class CommonResponse {
    @Key
    private String resultCode;
    @Key
    private String resultMessage;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
