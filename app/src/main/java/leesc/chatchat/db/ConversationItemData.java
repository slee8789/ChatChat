package leesc.chatchat.db;

import android.database.Cursor;

public class ConversationItemData {
    private int mId;
    private long mThreadId;
    private String mSnippet;
    private long mDate;
    private int mStatus;
    private int mMsgCount;
    private int mUnreadCount;
    private int mFavority;
    private int mBlocked;
    private boolean mChecked;

    public ConversationItemData(int id, int threadId, String snippet, long date, int status, int msgCount,
                                int unreadCount, int favority, int blocked) {
        mId = id;
        mThreadId = threadId;
        mSnippet = snippet;
        mDate = date;
        mStatus = status;
        mMsgCount = msgCount;
        mUnreadCount = unreadCount;
        mFavority = favority;
        mBlocked = blocked;
    }
    
    public ConversationItemData(Cursor cursor) {
    	mId = cursor.getInt(0);
        mThreadId = cursor.getLong(1);
        mSnippet = cursor.getString(2);
        mDate = cursor.getLong(3);
        mStatus = cursor.getInt(4);
        mMsgCount = cursor.getInt(5);
        mUnreadCount = cursor.getInt(6);
        mFavority = cursor.getInt(7);
        mBlocked = cursor.getInt(8);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public long getThreadId() {
        return mThreadId;
    }

    public void setThreadId(int threadId) {
        this.mThreadId = threadId;
    }

    public String getSnippet() {
        return mSnippet;
    }

    public void setSnippet(String snippet) {
        this.mSnippet = snippet;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public int getMsgCount() {
        return mMsgCount;
    }

    public void setMsgCount(int msgCount) {
        this.mMsgCount = msgCount;
    }

    public int getUnreadCount() {
        return mUnreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.mUnreadCount = unreadCount;
    }

    public int getFavority() {
        return mFavority;
    }

    public void setFavority(int favority) {
        this.mFavority = favority;
    }

    public int getBlocked() {
        return mBlocked;
    }

    public void setBlocked(int blocked) {
        this.mBlocked = blocked;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setCheck(boolean isChecked) {
        mChecked = isChecked;
    }

}
