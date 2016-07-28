package leesc.chatchat;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import leesc.chatchat.db.DataChange;
import leesc.chatchat.db.DataObserver;
import leesc.chatchat.db.MessageDB;
import leesc.chatchat.utils.CommonUtils;
import leesc.chatchat.utils.ConfigSettingPreferences;
import leesc.chatchat.widget.MonitoringEditText;

public class MessageFragment extends Fragment implements DataChange, MonitoringEditText.OnPasteListener {

    public static final String RESPONSE_MSG = "response_msg";
    public static final String API_ID_PUSH_REQUEST = "push_request";

    private Activity mActivity;
    private MessageAdapter mAdapter;
    private long mThreadId;
    private ImageView mBackgroundImage;
    private ImageView mImgProgress;
    private ListView mListView;

    private MonitoringEditText mInputMessage;
    private Button mSendButton;

    private DisplayImageOptions mOptions;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    
    private int mSendType = 0; // 0 normal 1 attendance 2 etc
    
    final Handler responseHandler = new Handler() {
        public void handleMessage(Message msg) {
            mSendButton.setVisibility(View.VISIBLE);
            mImgProgress.setVisibility(View.GONE);

            // TODO :: Handler 구현
            String response = msg.getData().getString(RESPONSE_MSG);
            if (response.equals(API_ID_PUSH_REQUEST)) {
                int success = msg.getData().getInt("success");
                int failure = msg.getData().getInt("failure");

                if (success >= 1) {
//                    Toast.makeText(mActivity, "푸시 전송에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                } else if (success == 0 && failure >= 1) {
                    Toast.makeText(mActivity, "푸시 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }

            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        mActivity = getActivity();
        mThreadId = mActivity.getIntent().getLongExtra(MessageActivity.THREAD_ID, -1);

        findViews(rootView);
        initViews();

        if (mThreadId > 0) {
            queryMessages(mThreadId);
        }
        DataObserver.getInstance().addObserver(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUnreadCount();
        setBackground();
    }

    private void findViews(View rootView) {
        mBackgroundImage = (ImageView) rootView.findViewById(R.id.message_background);
        mListView = (ListView) rootView.findViewById(R.id.messages_listview);
        mInputMessage = (MonitoringEditText) rootView.findViewById(R.id.message_text_editor);
        mSendButton = (Button) rootView.findViewById(R.id.message_send_btn);
        mImgProgress = (ImageView) rootView.findViewById(R.id.message_img_progress);
    }

    private void initViews() {
        mAdapter = new MessageAdapter(mActivity, mListView, null, true, mThreadId);

        mListView.setAdapter(mAdapter);
        mListView.setScrollingCacheEnabled(false);
        mSendButton.setOnClickListener(mSendButtonClickListener);

        mOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.skin_preview_00)
                .showImageOnFail(R.mipmap.skin_preview_00).cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true).considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer()).build();
        mInputMessage.setOnPasteListener(this);
    }

    @Override
    public void onTextPaste(boolean flag) {
        refreshView();
    }

    private void refreshView() {
        // Logger.e(TAG, "refreshView");
        mInputMessage.postDelayed(new Runnable() {

            @Override
            public void run() {
                String str = mInputMessage.getEditableText().toString();
                int index = mInputMessage.getSelectionStart();
                mInputMessage.setText("");
                mInputMessage.setSelection(index);
            }
        }, 50);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void setBackground() {
        String uri = ConfigSettingPreferences.getInstance(mActivity).getPrefBackgroundUri();
        mImageLoader.displayImage(uri, mBackgroundImage, mOptions);
    }

    private void queryMessages(final long threadId) {
        new Thread() {
            public void run() {
                Message message = Message.obtain();
                message.obj = MessageDB.getInstance().queryForMessages(mActivity, mThreadId);
                mMessageHandler.sendMessage(message);
            }
        }.start();
    }

    private Handler mMessageHandler = new Handler() {
        public void handleMessage(Message msg) {
            Cursor cursor = (Cursor) msg.obj;
            mAdapter.changeCursor(null);
            mAdapter.changeCursor(cursor);
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(mAdapter.getCount() - 1);
        };
    };

    @Override
    public void onDestroy() {
        mAdapter.changeCursor(null);
        DataObserver.getInstance().removeObserver(this);
        super.onDestroy();
    }

    private void updateUnreadCount() {
        new Thread() {
            public void run() {
                MessageDB.getInstance().initUnreadCount(mActivity, mThreadId);

                int unreadCount = MessageDB.getInstance().queryForUnreadCount(mActivity);
                CommonUtils.updateIconBadge(mActivity, unreadCount);
            }
        }.start();

    }

    @Override
    public void onChange() {
        if (mThreadId > 0) {
            queryMessages(mThreadId);
            updateUnreadCount();
        }
    }

    private OnClickListener mSendButtonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            prePareSendPushMessage(v);
        }
    };

    private void prePareSendPushMessage(View v) {
        String message = mInputMessage.getText().toString();
        if (message.length() > 0) {
            v.setVisibility(View.GONE);
            mImgProgress.setVisibility(View.VISIBLE);
            AnimationDrawable frameAnimation = (AnimationDrawable) mImgProgress.getBackground();
            frameAnimation.start();

            sendPushMessage(message, mSendType);
        } else {
            Toast.makeText(mActivity, "메시지를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendPushMessage(final String message, int type) {
        // TODO :: send push 기능 구현
        mInputMessage.setText("");

        mThreadId = MessageDB.getInstance().storeMessage(mActivity, CommonUtils.MESSAGE, "01047323972",
                        "hmlee", message, MessageDB.SEND_TYPE);

        Message msg = responseHandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString(RESPONSE_MSG, API_ID_PUSH_REQUEST);
        bundle.putInt("success", 1);
        bundle.putInt("failure", 0);
        msg.setData(bundle);
        responseHandler.sendMessage(msg);
    }


}
