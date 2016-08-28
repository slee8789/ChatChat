package leesc.chatchat.http.model;

import com.google.api.client.util.Key;

/**
 * Created by LeeSeongKyung on 2016-08-23.
 */
public class AddFriend {
    @Key
    String useremail;
    @Key
    String friendemail;

    public AddFriend(String useremail, String friendemail) {
        this.useremail = useremail;
        this.friendemail = friendemail;
    }
}
