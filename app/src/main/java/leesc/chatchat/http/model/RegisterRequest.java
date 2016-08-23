package leesc.chatchat.http.model;

import com.google.api.client.util.Key;

/**
 * Created by hmlee on 2016-08-22.
 */
public class RegisterRequest {

    @Key
    String email;
    @Key
    String name;
    @Key
    String token;
    @Key
    String device_type;

    public RegisterRequest(String email, String name, String token, String device_type) {
        this.email = email;
        this.name = name;
        this.token = token;
        this.device_type = device_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
