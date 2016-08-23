package leesc.chatchat.http.model;

import com.google.api.client.util.Key;

/**
 * Created by hmlee on 2016-08-22.
 */
public class IsRegisterUser {
    @Key
    String email;

    public IsRegisterUser(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
