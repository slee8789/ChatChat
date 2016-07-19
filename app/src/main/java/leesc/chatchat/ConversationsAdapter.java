package leesc.chatchat;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import leesc.chatchat.db.ConversationItemData;
import leesc.chatchat.db.MessageDB;
import leesc.chatchat.db.RecipientIdCache;
import leesc.chatchat.utils.AnimateFirstDisplayListener;
import leesc.chatchat.utils.DateUtils;
import leesc.chatchat.utils.DummyContact;

public class ConversationsAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mImageUrls;
    private DisplayImageOptions mOptions;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private ImageLoadingListener mAnimateFirstListener = new AnimateFirstDisplayListener();
    
    private LayoutInflater mInflater;
    private SparseBooleanArray mCheckedItems;
    private String mStringNoTitle;
    
    private ArrayList<ConversationItemData> mConversationList;

    public ConversationsAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.chat_icon_profile02)
            .showImageOnFail(R.mipmap.chat_icon_profile02).cacheInMemory(true).cacheOnDisc(true)
            .resetViewBeforeLoading(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(15, 0)).build();

        // TODO :: 이미지 작업
//        mImageUrls = DummyContact.getGaJJaTagImageUrls();
        mStringNoTitle = mContext.getString(R.string.no_content);
    }
    
    public void setListItems(ArrayList<ConversationItemData> list) {
        mConversationList = list;
    }
    
    public void removeItem(int[] indexs) {
    	for (int index : indexs) {
    		removeItem(index);
    	}
    }
    
    public void removeItem(int index) {
    	ConversationItemData item = mConversationList.remove(index);
    	String threads[] = new String[1];
    	threads[0] = "" +item.getThreadId();
    	MessageDB.getInstance().deleteConversations(mContext, threads);
    }
    
    public void setCursor(Cursor cursor) {
    	try {
    		if (cursor == null) {
    			return;
    		}
    		ArrayList<ConversationItemData> temp = new ArrayList<ConversationItemData>();
    		cursor.move(-1);
    		while(cursor.moveToNext()) {
    			ConversationItemData item = new ConversationItemData(cursor);
    			temp.add(item);
    		}
    		setListItems(temp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MessageDB.getInstance().closeCursor(cursor);
		}
    }

    public ArrayList<ConversationItemData> getListItems() {
        return mConversationList;
    }
    
    @Override
    public int getCount() {
        if (mConversationList == null)
            return 0;

        return mConversationList.size();
    }

    @Override
    public Object getItem(int position) {
        return mConversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_conversations, parent, false);
            holder = new ViewHolder();
            holder.conversationCheck = (CheckBox) convertView.findViewById(R.id.conversation_check);
            holder.thumbnailImage = (ImageView) convertView.findViewById(R.id.thumbnail_image);
            holder.userTitleText = (TextView) convertView.findViewById(R.id.user_title);
            holder.lastMessageDate = (TextView) convertView.findViewById(R.id.last_message_date);
            holder.lastMessage = (TextView) convertView.findViewById(R.id.last_message);
            holder.feedCount = (TextView) convertView.findViewById(R.id.feed_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.conversationCheck.setTag(position);
        if (!isDeleteMode()) {
            holder.conversationCheck.setVisibility(View.GONE);
        } else {
            holder.conversationCheck.setVisibility(View.VISIBLE);
            holder.conversationCheck.setChecked(mConversationList.get(position).isChecked());
        }
        
        String tempNames = RecipientIdCache.getAddress(""+mConversationList.get(position).getThreadId()).names == null ? "" :RecipientIdCache.getAddress(""+mConversationList.get(position).getThreadId()).names;
        holder.userTitleText.setText(tempNames);
        if (mConversationList.get(position).getDate() != 0) {
            holder.lastMessageDate.setText(DateUtils.formatListDate(mConversationList.get(position).getDate()));
            holder.lastMessageDate.setVisibility(View.VISIBLE);
        } else {
            holder.lastMessageDate.setVisibility(View.GONE);
        }
        
        
        String snippet = mConversationList.get(position).getSnippet();
        snippet = TextUtils.isEmpty(snippet) ? mStringNoTitle : snippet;
        holder.lastMessage.setText("");
        holder.lastMessage.setText(snippet);
        
        
        int unreadCount = mConversationList.get(position).getUnreadCount();
        if(unreadCount <= 0) {
            holder.feedCount.setVisibility(View.GONE);
            holder.lastMessage.setTypeface(null, Typeface.NORMAL);
        } else {
            holder.feedCount.setVisibility(View.VISIBLE);
            holder.feedCount.setText(String.valueOf(unreadCount));
            holder.lastMessage.setTypeface(null, Typeface.BOLD);
        }

        // TODO :: 이미지 작업

//        Uri photoUri = null;
//
//        mImageLoader.displayImage(
//            photoUri == null ? mImageUrls[position % mImageUrls.length] : photoUri.toString(),
//            holder.thumbnailImage,
//            mOptions, mAnimateFirstListener);

        mImageLoader.displayImage(
            null,
            holder.thumbnailImage,
            mOptions, mAnimateFirstListener);
        
        
        return convertView;
    }

    private class ViewHolder {
        public CheckBox conversationCheck;
        public ImageView thumbnailImage;
        public TextView userTitleText;
        public TextView lastMessageDate;
        public TextView lastMessage;
        public TextView feedCount;
    }

    public boolean isDeleteMode() {
        return mCheckedItems != null;
    }

    public void startDeleteMode() {
        mCheckedItems = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void finishDeleteMode() {
        mCheckedItems = null;
        notifyDataSetChanged();
    }
    
    public int getCheckedTotalCount() {
        int totalCount = 0;
        for (int i = 0; i < mConversationList.size(); i++) {
            if (mConversationList.get(i).isChecked()) {
                totalCount++;
            }
        }
        return totalCount;
    }
    
    public ArrayList<ConversationItemData> getCheckedItems() {
        ArrayList<ConversationItemData> conversationItems = new ArrayList<ConversationItemData>();
        for (int i = 0; i < mConversationList.size(); i++) {
            if (mConversationList.get(i).isChecked()) {
                conversationItems.add(mConversationList.get(i));
            }
        }
        return conversationItems;
    }

}
