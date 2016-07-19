package leesc.chatchat.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import leesc.chatchat.R;

public class OkCancelDialog extends DialogFragment {

    private static OnClickListener mListener;

    public static OkCancelDialog newInstance(int titleResourceId, int messageResourceId, OnClickListener listener) {
        mListener = listener;

        OkCancelDialog frag = new OkCancelDialog();
        Bundle args = new Bundle();
        args.putInt("titleResourceId", titleResourceId);
        args.putInt("messageResourceId", messageResourceId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int titleResourceId = getArguments().getInt("titleResourceId");
        int messageResourceId = getArguments().getInt("messageResourceId");
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_ok_cancel, null, false);
        TextView message = (TextView) view.findViewById(R.id.ok_cancel_dialog_message);
        message.setText(messageResourceId);
        return new AlertDialog.Builder(getActivity()).setView(view).setTitle(titleResourceId)
            .setPositiveButton(R.string.ok, mListener).setNegativeButton(R.string.cancel, mListener).create();
    }
}
