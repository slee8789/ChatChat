package leesc.chatchat.data;

/**
 * Created by HM on 2016. 7. 20..
 */
public class Contact {

    private int id;
    private String name;
    private String phoneNumber;
    private String email;
    private String registrationToken;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Contact() {
    }

    Contact(String name, String phoneNumber, String email, String registrationToken) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.registrationToken = registrationToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }
}