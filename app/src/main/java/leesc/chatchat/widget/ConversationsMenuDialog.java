package leesc.chatchat.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConversationsMenuDialog extends DialogFragment {

    private static OnClickListener mListener;

    public static ConversationsMenuDialog newInstance(String title, int contentResourceId, OnClickListener listener) {

        mListener = listener;

        ConversationsMenuDialog frag = new ConversationsMenuDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("contentResourceId", contentResourceId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        int contentResourceId = getArguments().getInt("contentResourceId");
        return new AlertDialog.Builder(getActivity()).setTitle(title).setItems(contentResourceId, mListener).create();
    }
}
