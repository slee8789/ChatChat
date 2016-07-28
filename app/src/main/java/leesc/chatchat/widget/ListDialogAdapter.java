package leesc.chatchat.widget;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import leesc.chatchat.R;

public class ListDialogAdapter extends ArrayAdapter<ListDialogData> {

    private Context mContext;
    private int mResourceId;
    private LayoutInflater mInflater;
    private ArrayList<ListDialogData> mDialogList;

    public ListDialogAdapter(Context context, int resourceId, ArrayList<ListDialogData> configList) {
        super(context, resourceId, configList);
        mContext = context;
        mResourceId = resourceId;
        mInflater = LayoutInflater.from(mContext);
        mDialogList = configList;
    }

    public void setListItems(ArrayList<ListDialogData> configList) {
        mDialogList = configList;
    }

    public ArrayList<ListDialogData> getListItems() {
        return mDialogList;
    }

    @Override
    public int getCount() {
        if (mDialogList == null)
            return 0;
        return mDialogList.size();
    }

    @Override
    public ListDialogData getItem(int position) {
        return mDialogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mResourceId, parent, false);
            holder = new ViewHolder();
            holder.mTitleText = (TextView) convertView.findViewById(R.id.dialog_radio_button_item_title);
            holder.mButtonImage = (ImageView) convertView.findViewById(R.id.dialog_radio_button_item_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTitleText.setText(mDialogList.get(position).getTitleText());
        if (mDialogList.get(position).getTitleTextSize() != 0) {
            holder.mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, mDialogList.get(position).getTitleTextSize());
        }
        holder.mButtonImage.setSelected(mDialogList.get(position).isSelected());

        return convertView;
    }

    private class ViewHolder {
        TextView mTitleText;
        ImageView mButtonImage;
    }
}
