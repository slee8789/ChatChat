package leesc.chatchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import leesc.chatchat.data.config.ConfigItem;

public class ConfigAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ConfigItem> mConfigList;

    public ConfigAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setListItems(ArrayList<ConfigItem> configList) {
        mConfigList = configList;
    }

    public ArrayList<ConfigItem> getListItems() {
        return mConfigList;
    }

    @Override
    public int getCount() {
        if (mConfigList == null)
            return 0;

        return mConfigList.size();
    }

    @Override
    public Object getItem(int position) {
        return mConfigList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mConfigList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return ConfigItem.TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int viewType = getItemViewType(position);
        if (convertView == null) {
            int resourcesId = 0;
            switch (viewType) {
            case ConfigItem.HEADER_TYPE:
                resourcesId = R.layout.row_config_header;
                break;
            case ConfigItem.NORMAL_TYPE:
                resourcesId = R.layout.row_config_normal;
                break;
            case ConfigItem.CHECK_TYPE:
                resourcesId = R.layout.row_config_check;
                break;
            default:
                throw new IllegalStateException("viewType: " + viewType);
            }

            convertView = mInflater.inflate(resourcesId, null);
            holder = new ViewHolder();

            holder.mHeaderListTextView = (TextView) convertView.findViewById(R.id.config_header_item_title);

            holder.mNormalListIcon = (ImageView) convertView.findViewById(R.id.config_normal_item_image);
            holder.mNormalListTitle = (TextView) convertView.findViewById(R.id.config_normal_item_title);
            holder.mNormalListInfo = (TextView) convertView.findViewById(R.id.config_normal_item_info);
            holder.mNormalListArrow = (ImageView) convertView.findViewById(R.id.config_normal_item_arrow);

            holder.mCheckListIcon = (ImageView) convertView.findViewById(R.id.config_check_item_image);
            holder.mCheckListTitle = (TextView) convertView.findViewById(R.id.config_check_item_title);
            holder.mCheckListCheckBox = (CheckBox) convertView.findViewById(R.id.config_check_item_checkbox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (viewType) {
        case ConfigItem.HEADER_TYPE:
            setHeaderList(holder, position);
            break;
        case ConfigItem.NORMAL_TYPE:
            setNormalList(holder, position);
            break;
        case ConfigItem.CHECK_TYPE:
            setCheckList(holder, position);
            break;
        default:
            throw new IllegalStateException("viewType: " + viewType);
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return mConfigList.get(position).isEnabled();
    }

    private void setHeaderList(ViewHolder holder, int position) {
        holder.mHeaderListTextView.setText(mConfigList.get(position).getTitleResourceId());
    }

    private void setNormalList(ViewHolder holder, int position) {
        holder.mNormalListIcon.setImageResource(mConfigList.get(position).getIconResourceId());
        holder.mNormalListTitle.setText(mConfigList.get(position).getTitleResourceId());
        holder.mNormalListInfo.setText(mConfigList.get(position).getInfoText());
        boolean isEnabled = mConfigList.get(position).isEnabled();
        if(isEnabled) {
            holder.mNormalListArrow.setVisibility(View.VISIBLE);
        } else {
            holder.mNormalListArrow.setVisibility(View.INVISIBLE);
        }
    }

    private void setCheckList(ViewHolder holder, int position) {
        holder.mCheckListIcon.setImageResource(mConfigList.get(position).getIconResourceId());
        holder.mCheckListTitle.setText(mConfigList.get(position).getTitleResourceId());

        if (mConfigList.get(position).isEnabled()) {
            holder.mCheckListTitle.setEnabled(true);
            holder.mCheckListCheckBox.setEnabled(true);
            if (mConfigList.get(position).isChecked()) {
                holder.mCheckListIcon.setBackgroundResource(R.mipmap.info_set_icon_bg_on);
                holder.mCheckListCheckBox.setChecked(true);
            } else {
                holder.mCheckListIcon.setBackgroundResource(R.mipmap.info_set_icon_bg_off);
                holder.mCheckListCheckBox.setChecked(false);
            }
        } else {
            holder.mCheckListIcon.setBackgroundResource(R.mipmap.info_set_icon_bg_inactive);
            holder.mCheckListTitle.setEnabled(false);
            holder.mCheckListCheckBox.setEnabled(false);
            holder.mCheckListCheckBox.setChecked(false);
        }
    }

    private class ViewHolder {
        TextView mHeaderListTextView;

        ImageView mNormalListIcon;
        TextView mNormalListTitle;
        TextView mNormalListInfo;
        ImageView mNormalListArrow;

        ImageView mCheckListIcon;
        TextView mCheckListTitle;
        CheckBox mCheckListCheckBox;
    }
}
