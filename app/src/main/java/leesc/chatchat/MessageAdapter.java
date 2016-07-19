package leesc.chatchat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import leesc.chatchat.db.DB_Constant;
import leesc.chatchat.utils.CommonUtils;
import leesc.chatchat.utils.DateUtils;

public class MessageAdapter extends CursorAdapter {

    private static final int COUNT_OF_VIEW_TYPE = 2;

    private static final int VIEW_TYPE_OPPONENT = 0;
    private static final int VIEW_TYPE_USER = 1;

    private LayoutInflater mInflater;
    private ListView mListView;

    private int mThumbnailMargin;
    private long mThreadId;

    public MessageAdapter(Context context, ListView listView, Cursor cursor, boolean autoRequerye, long threadId) {
        super(context, cursor, autoRequerye);
        mInflater = LayoutInflater.from(context);
        mListView = listView;
        mThumbnailMargin = (int) mContext.getResources().getDimension(R.dimen.common_mms_thumbnail_margin);
        mThreadId = threadId;
    }

    @Override
    public int getItemViewType(int position) {
        Cursor cursor = (Cursor) getItem(position);
        int status = cursor.getInt(cursor.getColumnIndex(DB_Constant.MESSAGES_FIELD_STATUS));
        
        if (status == DB_Constant.SEND_TYPE) {
            return VIEW_TYPE_USER;
        } else if (status == DB_Constant.RECEIVE_TYPE) {
            return VIEW_TYPE_OPPONENT;
        } else {
            return VIEW_TYPE_USER;
        }
        
    }

    @Override
    public int getViewTypeCount() {
        return COUNT_OF_VIEW_TYPE;
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        mListView.setSelection(getCount());
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
        case VIEW_TYPE_OPPONENT:
            bindLeftView(view, cursor);
            break;
        case VIEW_TYPE_USER:
            bindRightView(view, cursor);
            break;

        default:
            throw new IllegalStateException("viewType=" + viewType);
        }
    }

    private void bindLeftView(View view, Cursor cursor) {
        ViewHolderLeft holder = (ViewHolderLeft) view.getTag(R.id.tag_holder_left);
        holder.multimediaLayout.removeAllViews();

        long date = cursor.getLong(cursor.getColumnIndex(DB_Constant.MESSAGES_FIELD_DATE));
        String body = (cursor.getString(cursor.getColumnIndex(DB_Constant.MESSAGES_FIELD_BODY)));
        // TODO :: size 변경 기능 구현
        int fontSize = 16;
//        int fontSize = ConfigSettingPreferences.getInstance(mContext).getPrefTextSize();
        
        String msgType = cursor.getString(cursor.getColumnIndex(DB_Constant.MESSAGES_FIELD_TYPE));

        if (msgType.equals(CommonUtils.MESSAGE)) {
            holder.bubbleText.setVisibility(View.VISIBLE);
//            holder.attendanceButton.setVisibility(View.GONE);
//            holder.bubbleText.setText("");
//            if (JLSSmileyParser.getInstance().checkSmilySpans(body)) {
//                holder.bubbleText.append(JLSSmileyParser.getInstance().addSmileySpans(body));
//            } else {
//                holder.bubbleText.setText(body);
//            }
            holder.bubbleText.setText(body);
            holder.bubbleText.setTextSize(fontSize);
        }

//        else if (msgType.equals(CommonUtilities.ATTENDANCE)) {
//            holder.bubbleText.setVisibility(View.GONE);
//            holder.attendanceButton.setClickable(true);
//            holder.attendanceButton.setVisibility(View.VISIBLE);
//            holder.attendanceButton.setTextSize(fontSize);
//            holder.attendanceButton.setOnClickListener(mLeftClickListener);
//            holder.attendanceButton.setTag(R.id.tag_position, cursor.getPosition());
//            holder.attendanceButton.setTag(R.id.tag_message_id, cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
//        } else if (msgType.equals(CommonUtilities.ATTENDANCE_COMPLETE)) {
//            holder.bubbleText.setVisibility(View.GONE);
//            holder.attendanceButton.setVisibility(View.VISIBLE);
//            holder.attendanceButton.setTextSize(fontSize);
//            holder.attendanceButton.setClickable(false);
//            holder.attendanceButton.setPressed(true);
//            holder.attendanceButton.setText(R.string.attendance_complete);
//            holder.attendanceButton.setTag(R.id.tag_position, cursor.getPosition());
//            holder.attendanceButton.setTag(R.id.tag_message_id, cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
//            holder.attendanceButton.setBackgroundResource(R.drawable.bottom_bt_press);
//        } else if (msgType.equals(CommonUtilities.ATTENDANCE_FAIL)) {
//            holder.bubbleText.setVisibility(View.GONE);
//            holder.attendanceButton.setVisibility(View.VISIBLE);
//            holder.attendanceButton.setTextSize(fontSize);
//            holder.attendanceButton.setOnClickListener(mLeftClickListener);
//            holder.attendanceButton.setTag(R.id.tag_message_id, cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
//            holder.attendanceButton.setTag(R.id.tag_position, cursor.getPosition());
//        }
//        holder.attendanceButton.setOnLongClickListener(mBubbleLongClickListener);
        
        if (isNewDay(cursor)) {
            holder.dateLayout.setVisibility(View.VISIBLE);
            holder.dateText.setText(DateUtils.formatMessagesDate(date));
        } else {
            holder.dateLayout.setVisibility(View.GONE);
        }

        holder.timeText.setText(DateUtils.formatAmPm(date) + " " + DateUtils.formatTime(date));
        holder.bubbleLayout.setTag(R.id.tag_position, cursor.getPosition());
        holder.bubbleLayout.setOnLongClickListener(mBubbleLongClickListener);
    }

    private void bindRightView(View view, Cursor cursor) {
        ViewHolderRight holder = (ViewHolderRight) view.getTag(R.id.tag_holder_right);
        holder.multimediaLayout.removeAllViews();

        long date = cursor.getLong(cursor.getColumnIndex(DB_Constant.MESSAGES_FIELD_DATE));
        String body = (cursor.getString(cursor.getColumnIndex(DB_Constant.MESSAGES_FIELD_BODY)));
        // TODO :: size 변경 기능 구현
        int fontSize = 16;
//        int fontSize = ConfigSettingPreferences.getInstance(mContext).getPrefTextSize();
        String msgType = cursor.getString(cursor.getColumnIndex(DB_Constant.MESSAGES_FIELD_TYPE));
        if (msgType.equals(CommonUtils.MESSAGE)) {
            holder.bubbleText.setVisibility(View.VISIBLE);
//
//            holder.bubbleText.setText("");
//            if (JLSSmileyParser.getInstance().checkSmilySpans(body)) {
//                holder.bubbleText.append(JLSSmileyParser.getInstance().addSmileySpans(body));
//            } else {
//                holder.bubbleText.setText(body);
//            }

            holder.bubbleText.setText(body);
            holder.bubbleText.setTextSize(fontSize);
        }

//        else if (msgType.equals(CommonUtilities.ATTENDANCE)) {
//            holder.bubbleText.setVisibility(View.GONE);
//            holder.attendanceButton.setClickable(true);
//            holder.attendanceButton.setVisibility(View.VISIBLE);
//            holder.attendanceButton.setTextSize(fontSize);
//            holder.attendanceButton.setOnClickListener(mRightClickListener);
//            holder.attendanceButton.setTag(R.id.tag_position, cursor.getPosition());
//            holder.attendanceButton.setTag(R.id.tag_message_id, cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
//        }
//        holder.attendanceButton.setOnLongClickListener(mBubbleLongClickListener);

        if (isNewDay(cursor)) {
            holder.dateLayout.setVisibility(View.VISIBLE);
            holder.dateText.setText(DateUtils.formatMessagesDate(date));
        } else {
            holder.dateLayout.setVisibility(View.GONE);
        }
        
        if (cursor.getInt(cursor.getColumnIndex(DB_Constant.MESSAGES_FIELD_STATUS)) == DB_Constant.SEND_TYPE_FAIL) {
        	holder.failMark.setVisibility(View.VISIBLE);
        } else {
        	holder.failMark.setVisibility(View.GONE);
        }

        holder.timeText.setText(DateUtils.formatAmPm(date) + " " + DateUtils.formatTime(date));
        holder.bubbleLayout.setTag(R.id.tag_position, cursor.getPosition());
        holder.bubbleLayout.setOnLongClickListener(mBubbleLongClickListener);
    }

    private void addImageView(LinearLayout parent, String uri, int position) {
        // Attachment attachment = Attachment.create(mContext, Uri.parse(uri));
        // ImageView imageView = createImageView(attachment);
        // imageView.setTag(R.id.tag_multimedia, attachment);
        // imageView.setTag(R.id.tag_position, Integer.valueOf(position));
        // LayoutParams params = createLayoutParams();
        // parent.addView(imageView, params);
    }

    private LayoutParams createLayoutParams() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(mThumbnailMargin, mThumbnailMargin, mThumbnailMargin, mThumbnailMargin);
        return params;
    }

    // private ImageView createImageView(Attachment attachment) {
    // ImageView imageView = new ImageView(mContext);
    // imageView.setScaleType(ScaleType.CENTER);
    // imageView.setAdjustViewBounds(true);
    // imageView.setImageBitmap(attachment.getThumbnail());
    // imageView.setOnLongClickListener(mMultimediaLongClickListener);
    // imageView.setOnClickListener(mMultimediaClickListener);
    // return imageView;
    // }

    private boolean isNewDay(Cursor cursor) {
        if (cursor.getPosition() == 0) {
            return true;
        } else {
            int dateIndex = cursor.getColumnIndex(DB_Constant.MESSAGES_FIELD_DATE);
            cursor.moveToPrevious();
            long prevMillis = cursor.getLong(dateIndex);
            cursor.moveToNext();
            long currMillis = cursor.getLong(dateIndex);
            return !DateUtils.isSameDay(prevMillis, currMillis);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view;
        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
        case VIEW_TYPE_OPPONENT:
            view = inflateLeftView(parent);
            break;
        case VIEW_TYPE_USER:
            view = inflateRightView(parent);
            break;

        default:
            throw new IllegalStateException("viewType=" + viewType);
        }
        return view;
    }

    private View inflateLeftView(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.row_message_left, parent, false);
        final ViewHolderLeft holder = new ViewHolderLeft();
        holder.bubbleLayout = view.findViewById(R.id.message_bubble_layout);
        holder.dateLayout =  view.findViewById(R.id.message_date_layout);
        holder.dateText = (TextView) view.findViewById(R.id.message_date_text);
        holder.multimediaLayout = (LinearLayout) view.findViewById(R.id.message_attachment_layout);
        holder.bubbleText = (TextView) view.findViewById(R.id.message_bubble_text);
        holder.timeText = (TextView) view.findViewById(R.id.message_time_text);
        view.setTag(R.id.tag_holder_left, holder);
        return view;
    }

    private View inflateRightView(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.row_message_right, parent, false);
        final ViewHolderRight holder = new ViewHolderRight();
        holder.bubbleLayout =  view.findViewById(R.id.message_bubble_layout);
        holder.dateLayout =  view.findViewById(R.id.message_date_layout);
        holder.dateText = (TextView) view.findViewById(R.id.message_date_text);
        holder.multimediaLayout = (LinearLayout) view.findViewById(R.id.message_attachment_layout);
        holder.bubbleText = (TextView) view.findViewById(R.id.message_bubble_text);
        holder.timeText = (TextView) view.findViewById(R.id.message_time_text);
        holder.failMark = (ImageView) view.findViewById(R.id.fail_mark);
        view.setTag(R.id.tag_holder_right, holder);
        return view;
    }

    private class ViewHolderLeft {
        public View bubbleLayout;
        public View dateLayout;
        public TextView dateText;
        public LinearLayout multimediaLayout;
        public TextView bubbleText;
        public TextView timeText;
    }

    private class ViewHolderRight {
        public View bubbleLayout;
        public View dateLayout;
        public TextView dateText;
        public LinearLayout multimediaLayout;
        public TextView bubbleText;
        public TextView timeText;
        public ImageView failMark;
    }
    
    private View.OnClickListener mLeftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag(R.id.tag_position);
            Cursor cursor = (Cursor) getItem(position);
            int id = (Integer) v.getTag(R.id.tag_message_id);
            String bodyString = cursor.getString(cursor.getColumnIndexOrThrow(DB_Constant.MESSAGES_FIELD_BODY));
        }
    };

    private View.OnClickListener mRightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag(R.id.tag_position);
            Cursor cursor = (Cursor) getItem(position);
            String bodyString = cursor.getString(cursor.getColumnIndexOrThrow(DB_Constant.MESSAGES_FIELD_BODY));
        }
    };

    private OnLongClickListener mBubbleLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Integer position = (Integer) v.getTag(R.id.tag_position);
            showTextMenuDialog(position);
            return false;
        }
    };

    protected void showTextMenuDialog(int position) {
        CommonUtils.makeToast(mContext, "Menu 기능 미구현");
        // TODO :: Menu 기능 구현
//        Cursor cursor = (Cursor) getItem(position);
//        int resource = R.array.messages_long_click_right;
//        String title = mContext.getString(R.string.message_option);
//        long _id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
//
//        MessagesMenuItemClickListener listener = new MessagesMenuItemClickListener(_id, position, null) {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                FragmentManager manager = ((FragmentActivity) mContext).getSupportFragmentManager();
//                switch (which) {
//                case 0:
//                    showMessageDeleteConfirmDialog(manager, getId());
//                    break;
//
//                case 1:
//                    setClipboardText(getPosition());
//                    Toast.makeText(mContext, R.string.toast_copy_completed, Toast.LENGTH_SHORT).show();
//                    break;
//
//                case 2:
//                    forwardMessage(getPosition());
//                    break;
//                }
//            }
//        };
//
//        FragmentManager manager = ((FragmentActivity) mContext).getSupportFragmentManager();
//        ConversationsMenuDialog.newInstance(title, resource, listener).show(manager, "TextMenuDialog");
    }

    private void showMessageDeleteConfirmDialog(FragmentManager manager, final long _id) {
        CommonUtils.makeToast(mContext, "Delete 기능 미구현");
        // TODO :: Delete 기능 구현
//        OkCancelDialog.newInstance(R.string.delete, R.string.delete_message, new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                case DialogInterface.BUTTON_POSITIVE:
//                    // delete _id thread
//                    long id = MessageDB.getInstance().deleteMessage(mContext, _id);
//
//                    if(id > 0) {
//                        notifyDataSetChanged();
//                        Toast.makeText(mContext, R.string.toast_msg_deleted, Toast.LENGTH_SHORT).show();
//                        MessageDB.getInstance().updateConversation(mContext, mThreadId);
//                    } else {
//                        Toast.makeText(mContext, R.string.toast_save_failed, Toast.LENGTH_SHORT).show();
//                    }
//
//                    break;
//                default:
//                    // ignore
//                    break;
//                }
//            }
//        }).show(manager, "MessageDeleteConfirmDialog");
    }

    public void setClipboardText(int position) {
        Cursor cursor = (Cursor) getItem(position);
        String clipText = cursor.getString(cursor.getColumnIndexOrThrow(DB_Constant.MESSAGES_FIELD_BODY));

        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(mContext.getString(R.string.app_name), clipText);
        clipboard.setPrimaryClip(clip);
    }

    protected void forwardMessage(int position) {
        CommonUtils.makeToast(mContext, "forwardMessage 기능 미구현, " + position);
        // TODO :: forwardMessage 기능 구현
//        Cursor cursor = (Cursor) getItem(position);
//        String bodyString = cursor.getString(cursor.getColumnIndexOrThrow(DB_Constant.MESSAGES_FIELD_BODY));
//
//        Intent intent = new Intent(mContext, ComposeActivity.class);
//        intent.putExtra(ComposeActivity.SEND_FROM, ComposeActivity.SEND_FROM_CONTACTS);
//        intent.putExtra(ComposeActivity.FORWARD_MSG, bodyString);
//        mContext.startActivity(intent);
    }
    
    abstract class MessagesMenuItemClickListener implements OnClickListener {

        private long mId;
        private int mPosition;
        private Uri mUri;

        public MessagesMenuItemClickListener(long id, int position, Uri uri) {
            setId(id);
            setPosition(position);
            setUri(uri);
        }

        public long getId() {
            return mId;
        }

        public void setId(long mId) {
            this.mId = mId;
        }

        public int getPosition() {
            return mPosition;
        }

        public void setPosition(int mPosition) {
            this.mPosition = mPosition;
        }

        public Uri getUri() {
            return mUri;
        }

        public void setUri(Uri uri) {
            this.mUri = uri;
        }
    }
}
