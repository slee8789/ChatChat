package leesc.chatchat.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import leesc.chatchat.R;

public class ListDialogFragment extends DialogFragment implements OnItemClickListener {

    private Context mContext;
    private String mTitle;
    private View mView;
    private String[] mListArrays;
    private int[] mSizeArrays;
    private ArrayList<ListDialogData> mDialogDatas;
    private ListDialogAdapter mListDialogAdapter;
    private ListView mListView;
    private TextView mTextView;
    private Button mNegativeButton;
    private Button mPositiveButton;
    private OnClickListener mNegativeButtonClickListener;
    private OnClickListener mPositiveButtonClickListener;

    private int mSelectedPosition = 0;
    private int mPrevPosition = 0;

    public ListDialogFragment(Context context, String title, String[] listArrays, int selectedPosition) {
        mContext = context;
        mTitle = title;
        mListArrays = listArrays;
        mDialogDatas = new ArrayList<ListDialogData>();
        mSelectedPosition = selectedPosition;
    }

    public ListDialogFragment(Context context, String title, String[] listArrays, int[] sizeArrays, int selectedPosition) {
        mContext = context;
        mTitle = title;
        mListArrays = listArrays;
        mSizeArrays = sizeArrays;
        mDialogDatas = new ArrayList<ListDialogData>();
        mSelectedPosition = selectedPosition;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        findViews();
        initViews();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView);
        return builder.create();
    }
    
    private void findViews() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mView = inflater.inflate(R.layout.dialog_radio_button_list, null);
        mListView = (ListView) mView.findViewById(R.id.dialog_radio_button_list);
        mTextView = (TextView) mView.findViewById(R.id.dialog_radio_button_title);
        mNegativeButton = (Button) mView.findViewById(R.id.dialog_radio_button_cancel);
        mPositiveButton = (Button) mView.findViewById(R.id.dialog_radio_button_ok);
    }

    private void initViews() {
        mTextView.setText(mTitle);
        if (mSizeArrays == null) {
            for (int i = 0; i < mListArrays.length; i++) {
                mDialogDatas.add(new ListDialogData(mListArrays[i], false));
            }
        } else {
            for (int i = 0; i < mListArrays.length; i++) {
                mDialogDatas.add(new ListDialogData(mListArrays[i], mSizeArrays[i], false));
            }
        }
        initAdapter();
        mListView.setOnItemClickListener(this);
        setSelectedPosition(mSelectedPosition);
        mNegativeButton.setOnClickListener(mNegativeButtonClickListener);
        mPositiveButton.setOnClickListener(mPositiveButtonClickListener);
        
    }

    private void initAdapter() {
        mListDialogAdapter = new ListDialogAdapter(mContext, R.layout.row_dialog_radio_button,
            mDialogDatas);
        mListView.setAdapter(mListDialogAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setListPosition(position);
    }

    public void setSelectedPosition(int position) {
        setListPosition(position);
        mListView.smoothScrollToPosition(position);
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    private void setListPosition(int position) {
        mPrevPosition = mSelectedPosition;
        mSelectedPosition = position;
        mDialogDatas.get(mPrevPosition).setSelected(false);
        mDialogDatas.get(mSelectedPosition).setSelected(true);
        mListDialogAdapter.notifyDataSetChanged();
    }

    public void setNegativeButton(OnClickListener listener) {
        mNegativeButtonClickListener = listener;
    }

    public void setPositiveButton(OnClickListener listener) {
        mPositiveButtonClickListener = listener;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSelectedPosition(mSelectedPosition);
    }

}
