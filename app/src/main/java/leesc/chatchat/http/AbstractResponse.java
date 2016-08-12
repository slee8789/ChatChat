package leesc.chatchat.http;

import com.google.api.client.util.Key;

public class AbstractResponse {
    @Key
    private Status status;

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus(){
        return status;
    }
}