package leesc.chatchat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import leesc.chatchat.data.config.ConfigCheckItem;
import leesc.chatchat.data.config.ConfigHeaderItem;
import leesc.chatchat.data.config.ConfigItem;
import leesc.chatchat.data.config.ConfigNormalItem;
import leesc.chatchat.utils.ConfigSettingPreferences;
import leesc.chatchat.widget.ConversationsMenuDialog;
import leesc.chatchat.widget.ListDialogFragment;

public class ConfigFragment extends Fragment {
    
    private static final int BG_SETTING = 1;
    private static final int TEXT_SIZE_SETTING = 2;
    private static final int NOTICE_VIBRATE = 4;
    private static final int NOTICE_SOUND = 5;
    private static final int NOTICE_NOTIFICATION = 6;
    private static final int NOTICE_MESSAGE = 7;
    private static final int PROGRAM_INFO = 9;
    private static final int PINNUMBER_INFO = 11;
    
    private static final int BG_SETTING_COLOR = 101;
    private static final int BG_SETTING_ALBUM = 102;
    private static final int BG_SETTING_PICTURE = 103;
    private static final int BG_SETTING_PREVIEW = 104;

    private Activity mActivity;
    private ListView mConfigListView;
    private RelativeLayout mPinView;
    private ConfigAdapter mConfigAdapter;
    private ArrayList<ConfigItem> mConfigList;

    private ListDialogFragment mTextSizeDialog;
    
    private int mIndex = 0;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config, container, false);
        findViews(rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void findViews(View rootView) {
        mConfigListView = (ListView) rootView.findViewById(R.id.config_list_view);
    }

    private void initViews() {
        initAdapter();
        initEvent();
    }
    
    private void initAdapter() {
        initList();
        mConfigAdapter = new ConfigAdapter(mActivity);
        mConfigAdapter.setListItems(mConfigList);
        mConfigListView.setAdapter(mConfigAdapter);
    }
    
    @SuppressWarnings("serial")
    private void initList() {
        mConfigList = new ArrayList<ConfigItem>() {
            {
                // 디스플레이 설정
                add(new ConfigHeaderItem(R.string.display_info));
                add(new ConfigNormalItem(R.mipmap.setlist_album_icon, R.string.background_setting));
                add(new ConfigNormalItem(R.mipmap.setlist_text_icon, R.string.text_size,
                    ConfigSettingPreferences.getInstance(mActivity).getPrefTextSizeName()));
                
                // 알림 설정
                add(new ConfigHeaderItem(R.string.setting_notice));
                add(new ConfigCheckItem(R.mipmap.setlist_vibrate_icon, R.string.notice_vibrate,
                    ConfigSettingPreferences.getInstance(mActivity).getPrefNoticeVibrate()));
                add(new ConfigCheckItem(R.mipmap.setlist_alarmpopup_icon, R.string.notice_sound,
                    ConfigSettingPreferences.getInstance(mActivity).getPrefNotiSound()));
                add(new ConfigCheckItem(R.mipmap.setlist_alarm_icon, R.string.notice_noti,
                    ConfigSettingPreferences.getInstance(mActivity).getPrefNoticeNotification()));
                add(new ConfigCheckItem(R.mipmap.setlist_info_icon, R.string.notice_message,
                    ConfigSettingPreferences.getInstance(mActivity).getPrefNoticeMessage()));
                
                // 어플리케이션 정보
                add(new ConfigHeaderItem(R.string.application_info));
                add(new ConfigNormalItem(R.mipmap.setlist_category_icon, R.string.version_info, getVersionName(), false));

            }
        };
    }
    
    private String getVersionName() {
        PackageInfo pi = null;
        String versionName = "";

        try {
            pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }
    
    private void initEvent() {
        mConfigListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type = mConfigList.get(position).getType();
                switch (type) {
                case ConfigItem.NORMAL_TYPE:
                    clickedItemAction(position);
                    break;
                case ConfigItem.CHECK_TYPE:
                    checkedItemAction(position);
                    break;
                default:
                    throw new IllegalStateException("type: " + type);
                }

            }
        });
    }
    
    private void clickedItemAction(int position) {
        switch (position) {
        case BG_SETTING:
            showBackgroundSettingDialog();
            break;
        case TEXT_SIZE_SETTING:
            showTextSizeDialog();
            break;
        case PROGRAM_INFO:
            Toast.makeText(mActivity, "버전 정보", Toast.LENGTH_SHORT).show();
            break;
        case PINNUMBER_INFO:
            mPinView.setVisibility(View.VISIBLE);
            break;
        default:
            throw new IllegalStateException("position: " + position);
        }
    }
    
    private String getTextFromRadioButton(int id, View view) {
        String msg = "";
        RadioButton radioBtn = (RadioButton) view.findViewById(id);
        msg = radioBtn.getTag().toString();
        radioBtn.setChecked(false);
        return msg;
    }
    
    private void checkedItemAction(int position) {
        mConfigList.get(position).setChecked(!mConfigList.get(position).isChecked());
        switch (position) {
        case NOTICE_VIBRATE:
            ConfigSettingPreferences.getInstance(mActivity).setPrefNoticeVibrate(mConfigList.get(position).isChecked());
            break;
        case NOTICE_SOUND:
            ConfigSettingPreferences.getInstance(mActivity).setPrefNotiSound(mConfigList.get(position).isChecked());
            break;
        case NOTICE_NOTIFICATION:
            ConfigSettingPreferences.getInstance(mActivity).setPrefNoticeNotification(mConfigList.get(position).isChecked());
            break;
        case NOTICE_MESSAGE:
            ConfigSettingPreferences.getInstance(mActivity).setPrefNoticeMessage(mConfigList.get(position).isChecked());
            break;
        default:
            throw new IllegalStateException("position: " + position);
        }
        mConfigAdapter.notifyDataSetChanged();
    }
    
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI).setType("image/*");
        startActivityForResult(intent, BG_SETTING_ALBUM);
    }
    
    private void showBackgroundSettingDialog() {
        int resource = R.array.bg_setting_array;
        String title = mActivity.getString(R.string.background_setting);

        android.content.DialogInterface.OnClickListener listener = new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case 0:
                    pickImage();
                    break;

                case 1:
                    ConfigSettingPreferences.getInstance(mActivity).setPrefBackgroundUri(null);
                    Toast.makeText(mActivity, R.string.toast_msg_apply_to_basic_bg, Toast.LENGTH_SHORT).show();
                    break;

                }
            }
        };

        FragmentManager manager = ((FragmentActivity) mActivity).getSupportFragmentManager();
        ConversationsMenuDialog.newInstance(title, resource, listener).show(manager, "BackgroundSettingDialog");
    }
    
    private void showTextSizeDialog() {
        String title = getResources().getString(R.string.text_size);
        final String[] textSizeNameArray = getResources().getStringArray(R.array.text_size_title_array);
        final int[] textSizeArray = getResources().getIntArray(R.array.bubble_text_size_array);
        int selectedPosition = ConfigSettingPreferences.getInstance(mActivity).getPrefTextSizeIndex();
        mTextSizeDialog = new ListDialogFragment(mActivity, title, textSizeNameArray, textSizeArray, selectedPosition);
        mTextSizeDialog.show(getFragmentManager(), "TextSizeSetting");
        mTextSizeDialog.setNegativeButton(cancelButtonListener);
        mTextSizeDialog.setPositiveButton(okButtonListener);
    }
    
    private OnClickListener cancelButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mTextSizeDialog.dismiss();
        }
    };

    private OnClickListener okButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = mTextSizeDialog.getSelectedPosition();
            setTextSizePreferences(position);
            
            mTextSizeDialog.dismiss();
        }
    };
    
    private void setTextSizePreferences(int position) {
        final String[] textSizeNameArray = getResources().getStringArray(R.array.text_size_title_array);
        final int[] textSizeArray = getResources().getIntArray(R.array.bubble_text_size_array);

        ConfigSettingPreferences.getInstance(mActivity).setPrefTextSizeIndex(position);
        ConfigSettingPreferences.getInstance(mActivity).setPrefTextSizeName(textSizeNameArray[position]);
        ConfigSettingPreferences.getInstance(mActivity).setPrefTextSize(textSizeArray[position]);
        
        mConfigList.clear();
        initViews();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
        case BG_SETTING_COLOR:
            break;
        case BG_SETTING_ALBUM:
            Uri imgUri = data.getData();
            Intent intent = new Intent(new Intent(mActivity, ConfigPreviewActivity.class))
                .setData(imgUri)
                .putExtra(ConfigPreviewActivity.INTENT_EXTRA_PREVIEW_TYPE,
                    ConfigPreviewActivity.PREVIEW_TYPE_PHOTO_SELECT);
            startActivityForResult(intent, BG_SETTING_PREVIEW);
            break;
        case BG_SETTING_PICTURE:
            break;
        case BG_SETTING_PREVIEW:
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(mActivity, R.string.toast_msg_apply_to_bg, Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }    
    

}
