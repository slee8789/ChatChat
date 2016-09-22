package leesc.chatchat.http.model;

import com.google.api.client.util.Key;

/**
 * Created by LeeSeongKyung on 2016-08-23.
 */
public class AddFriend {
    @Key
    String userEmail;
    @Key
    String friendEmail;

    public AddFriend(String userEmail, String friendEmail) {
        this.userEmail = userEmail;
        this.friendEmail = friendEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

}
