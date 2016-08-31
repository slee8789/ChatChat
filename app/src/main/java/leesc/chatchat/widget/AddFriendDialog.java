package leesc.chatchat.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import leesc.chatchat.R;

/**
 * Created by LeeSeongKyung on 2016-08-23.
 */
public class AddFriendDialog extends Dialog implements View.OnTouchListener {
    private EditText friendName, friendEmail;
    private Button addOk, addCancel;
    private String _friendName, _friendEmail;

    public addFriendDialog(Context context) {
        super(context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_addfriend);

        friendName = (EditText) findViewById(R.id.edit_name);
        friendEmail = (EditText)findViewById(R.id.edit_email);
        addOk = (Button)findViewById(R.id.addOK);
        addCancel = (Button)findViewById(R.id.addCancel);

        addOk.setOnTouchListener(this);
        addCancel.setOnTouchListener(this);
    }

    public String getFriendName() {
        return _friendName;
    }

    public String getFriendEmail() {
        return _friendEmail;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if(v == addOk) {
            _friendName = friendName.getText().toString();
            _friendEmail = friendEmail.getText().toString();
            dismiss();
        }
        else if(v == addCancel) {
            cancel();
        }
            return false;
    }
}
