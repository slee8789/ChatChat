package leesc.chatchat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;

import leesc.chatchat.utils.CommonUtils;

public class MessageDB implements BaseColumns, DB_Constant {

    public static String DATABASE_NAME = "chat_chat.db";
    public static int DATABASE_VERSION = 2;

    private static final int THREADS = 100;
    private static final int CONVERSATION = 200;
    private static final int MESSAGES = 300;

    private static String THREADS_PROJECTION[] = { _ID, THREADS_FIELD_THREAD_EMAIL, THREADS_FIELD_THREAD_NAMES };

    private static String CONVERSATION_PROJECTION[] = { _ID, CONVERSATION_FIELD_THREAD_ID, CONVERSATION_FIELD_SNIPPET,
            CONVERSATION_FIELD_DATE, CONVERSATION_FIELD_STATUS, CONVERSATION_FIELD_MSG_COUNT,
            CONVERSATION_FIELD_UNREAD_COUNT, CONVERSATION_FIELD_FAVORITY, CONVERSATION_FIELD_BLOCKED };

    private static String MESSAGES_PROJECTION[] = { _ID, MESSAGES_FIELD_THREAD_ID, MESSAGES_FIELD_EMAIL,
            MESSAGES_FIELD_NAMES, MESSAGES_FIELD_BODY, MESSAGES_FIELD_DATE, MESSAGES_FIELD_TYPE, MESSAGES_FIELD_STATUS,
            MESSAGES_FIELD_READ };

    private static volatile MessageDB dbHandle;
    private static volatile DatabaseHelper dbHelper;

    static class MsgHolder {
        static final MessageDB holder = new MessageDB();
    }

    public MessageDB() {

    }

    public static MessageDB getInstance() {
        if (dbHandle == null) {
            synchronized (MessageDB.class) {
                if (dbHandle == null) {
                    dbHandle = MsgHolder.holder;
                }
            }
        }
        return dbHandle;
    }

    private static DatabaseHelper getDbHelper(Context context) {
        getInstance();
        if (dbHelper == null) {
            synchronized (dbHandle) {
                if (dbHelper == null) {
                    dbHelper = new DatabaseHelper(context);
                }
            }
        }
        return dbHelper;
    }

    public long insertThreads(Context context, String email, String names) {
        ContentValues values = new ContentValues();
        values.put(THREADS_FIELD_THREAD_EMAIL, email);
        values.put(THREADS_FIELD_THREAD_NAMES, names);
        try {
            long id = getThreadId(context, email);
            if (id == -1) {
                id = insert(context, values, THREADS);
            } else {
                Cursor cursor = queryForConversation(context, id);
                if (cursor == null || cursor.getCount() == 0) {
                    insertConversation(context, id, "", 0, 0, 0, 0, 0);
                }
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public long getThreadId(Context context, String email) {
        long id = -1;
        Cursor cursor = null;
        try {
            StringBuilder where = new StringBuilder(THREADS_FIELD_THREAD_EMAIL);
            where.append('=');
            where.append('\'');
            where.append(email);
            where.append('\'');
            getThreadCursor(context);
            cursor = queryForThreads(context, where.toString());
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getLong(0);
            }
//            Log.e(getClass().getCanonicalName(), "getThreadId >> " + id + " / weher >> " + where.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }

        return id;
    }

    public Cursor getThreadCursor(Context context) {
        Cursor cursor = null;
        try {
            cursor = queryForThreads(context, null);
//            if (cursor.getCount() > 0) {
//                String columns[] = cursor.getColumnNames();
//                cursor.move(-1);
//                while (cursor.moveToNext()) {
//                    for (String column : columns) {
//                        Log.e(getClass().getCanonicalName(),
////                                "column >> " + column + "   /  index : " + cursor.getColumnIndex(column));
//                        if (cursor.getColumnIndex(column) != -1)
////                            Log.e(getClass().getCanonicalName(), "*"+cursor.getString(cursor.getColumnIndex(column))+"*");
//                    }
//                }
//                cursor.move(-1);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return cursor;
    }

    public void closeCursor(Cursor cur) {
        if (cur != null) {
            cur.close();
        }
    }

    public long insertConversation(Context context, long threadId, String snippet, int status, int msgCount,
            int unreadCount, int favority, int blocked) {
        long id = -1;
        ContentValues values = new ContentValues();
        values.put(CONVERSATION_FIELD_THREAD_ID, threadId);
        values.put(CONVERSATION_FIELD_SNIPPET, snippet);
        values.put(CONVERSATION_FIELD_DATE, System.currentTimeMillis());
        values.put(CONVERSATION_FIELD_STATUS, status);
        values.put(CONVERSATION_FIELD_MSG_COUNT, msgCount);
        values.put(CONVERSATION_FIELD_UNREAD_COUNT, unreadCount);
        values.put(CONVERSATION_FIELD_FAVORITY, favority);
        values.put(CONVERSATION_FIELD_BLOCKED, blocked);
        try {
            id = insert(context, values, CONVERSATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public long updateConversation(Context context, long threadId) {
        long id = -1;
        Cursor cursor = null;
        try {
            cursor = queryForMessages(context, threadId);
            ContentValues values = new ContentValues();
            if (cursor.getCount() > 0) {
                cursor.moveToLast();
                String type = cursor.getString(cursor.getColumnIndex(MESSAGES_FIELD_TYPE));
                String snippet = null;
                snippet = cursor.getString(cursor.getColumnIndex(MESSAGES_FIELD_BODY));
                long date = cursor.getLong(cursor.getColumnIndex(MESSAGES_FIELD_DATE));

                values.put(CONVERSATION_FIELD_SNIPPET, snippet);
                values.put(CONVERSATION_FIELD_DATE, date);
            } else {
                values.put(CONVERSATION_FIELD_SNIPPET, "");
                values.put(CONVERSATION_FIELD_DATE, 0);
            }

            id = update(context, threadId, values, CONVERSATION);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
        }

        return id;
    }

    public long storeMessage(Context context, String message_type, String address, String names, String body, int type) {
        long threadID = -1L;
        try {
            threadID = insertThreads(context, address, names);
            RecipientIdCache.fill(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        insertMessages(context, message_type, threadID, address, names, body, type, 0);
        onChange();
        return threadID;
    }

    private void onChange() {
        DataObserver.getInstance().onChange();
    }

    public long insertMessages(Context context, String message_type, long threadId, String address, String names,
            String body, int status, int read) {
        ContentValues values = new ContentValues();
        values.put(MESSAGES_FIELD_THREAD_ID, threadId);
        values.put(MESSAGES_FIELD_TYPE, message_type);
        values.put(MESSAGES_FIELD_EMAIL, address);
        values.put(MESSAGES_FIELD_NAMES, names);
        values.put(MESSAGES_FIELD_BODY, body);
        values.put(MESSAGES_FIELD_DATE, System.currentTimeMillis());
        values.put(MESSAGES_FIELD_STATUS, status);
        values.put(MESSAGES_FIELD_READ, read);
        try {
            long id = insert(context, values, MESSAGES);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private long insert(Context context, ContentValues values, int type) {
        DatabaseHelper helper = getDbHelper(context);
        Cursor cursor = null;
        long id = -1;
        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                if (type == THREADS) {
                    id = db.insert(THREADS_TABLE_NAME, "", values);
                    while (id == -1) {
                        id = db.insert(THREADS_TABLE_NAME, "", values);
                    }
                    RecipientIdCache.fill(context);
                    // ContentValues conversationValues = new ContentValues();
                    // conversationValues.put(CONVERSATION_FIELD_THREAD_ID, id);
                    // conversationValues.put(CONVERSATION_FIELD_SNIPPET, "");
                    // conversationValues.put(CONVERSATION_FIELD_DATE, System.currentTimeMillis());
                    // conversationValues.put(CONVERSATION_FIELD_STATUS, 0);
                    // conversationValues.put(CONVERSATION_FIELD_MSG_COUNT, 0);
                    // conversationValues.put(CONVERSATION_FIELD_UNREAD_COUNT, 0);
                    // conversationValues.put(CONVERSATION_FIELD_FAVORITY, 0);
                    // conversationValues.put(CONVERSATION_FIELD_BLOCKED, 0);
                    // db.insert(CONVERSATION_TABLE_NAME, "", conversationValues);
                    insertConversation(context, id, "", 0, 0, 0, 0, 0);
                } else if (type == CONVERSATION) {
                    id = db.insert(CONVERSATION_TABLE_NAME, "", values);
                } else if (type == MESSAGES) {
                    id = db.insert(MESSAGES_TABLE_NAME, "", values);
                    cursor = queryForConversation(context, values.getAsLong(MESSAGES_FIELD_THREAD_ID));
                    cursor.moveToFirst();
                    int unreadCount = 0;
                    if (cursor.getCount() > 0) {
                        unreadCount = cursor.getInt(cursor.getColumnIndex(CONVERSATION_FIELD_UNREAD_COUNT));
                    }
                    ContentValues conversationValues = new ContentValues();
                    conversationValues.put(CONVERSATION_FIELD_THREAD_ID, values.getAsLong(MESSAGES_FIELD_THREAD_ID));
                    String messageType = values.getAsString(MESSAGES_FIELD_TYPE);
                    conversationValues.put(CONVERSATION_FIELD_SNIPPET, values.getAsString(MESSAGES_FIELD_BODY));

                    conversationValues.put(CONVERSATION_FIELD_DATE, System.currentTimeMillis());
                    if (values.getAsInteger(MESSAGES_FIELD_STATUS) == DB_Constant.RECEIVE_TYPE) {
                        conversationValues.put(CONVERSATION_FIELD_UNREAD_COUNT, ++unreadCount);
                    }
                    update(context, values.getAsLong(MESSAGES_FIELD_THREAD_ID), conversationValues, CONVERSATION);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeCursor(cursor);
            }
        }
        onChange();
        return id;
    }

    private long update(Context context, long threadId, ContentValues values, int type) {
        DatabaseHelper helper = getDbHelper(context);
        int count = -1;
        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                if (type == THREADS) {
                    count = db.update(THREADS_TABLE_NAME, values, _ID + " = " + threadId, null);
                } else if (type == CONVERSATION) {
                    count = db.update(CONVERSATION_TABLE_NAME, values, CONVERSATION_FIELD_THREAD_ID + "=" + threadId,
                            null);
                } else if (type == MESSAGES) {
                    count = db.update(MESSAGES_TABLE_NAME, values, MESSAGES_FIELD_THREAD_ID + "=" + threadId, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (count > 0) {
            onChange();
        }

        return count;
    }

    public void deleteThreads(Context context, String[] ids) {
        DatabaseHelper helper = getDbHelper(context);

        String args = TextUtils.join(", ", ids);

        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.execSQL(String.format("DELETE FROM " + THREADS_TABLE_NAME + " WHERE " + _ID + " IN (%s);", args));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onChange();
    }

    public void deleteConversations(Context context, String[] threadIds) {
        DatabaseHelper helper = getDbHelper(context);

        String args = TextUtils.join(", ", threadIds);

        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.execSQL(String.format("DELETE FROM " + CONVERSATION_TABLE_NAME + " WHERE "
                        + CONVERSATION_FIELD_THREAD_ID + " IN (%s);", args));
                deleteMessages(context, threadIds);
                deleteThreads(context, threadIds);

                int unreadCount = MessageDB.getInstance().queryForUnreadCount(context);
                CommonUtils.updateIconBadge(context, unreadCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onChange();
    }

    public void deleteMessages(Context context, String[] threadIds) {
        DatabaseHelper helper = getDbHelper(context);

        String args = TextUtils.join(", ", threadIds);

        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                db.execSQL(String.format("DELETE FROM " + MESSAGES_TABLE_NAME + " WHERE " + MESSAGES_FIELD_THREAD_ID
                        + " IN (%s);", args));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onChange();
    }

    public long deleteMessage(Context context, long _id) {
        DatabaseHelper helper = getDbHelper(context);
        long id = -1;
        String where = _ID + " = ?";
        String[] args = new String[] { String.valueOf(_id) };

        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                id = db.delete(MESSAGES_TABLE_NAME, where, args);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onChange();
        return id;
    }

    public boolean initUnreadCount(Context context, long threadId) {
        Cursor cursor = queryForConversation(context, threadId);
        cursor.moveToFirst();
        int unreadCount = 0;
        if (cursor.getCount() == 0) {
            closeCursor(cursor);
            return false;
        }
        try {
            unreadCount = cursor.getInt(cursor.getColumnIndex(CONVERSATION_FIELD_UNREAD_COUNT));
        } catch (Exception e) {
            e.printStackTrace();
            unreadCount = 0;
        } finally {
            closeCursor(cursor);
        }

        if (unreadCount > 0) {
            ContentValues conversationValues = new ContentValues();
            conversationValues.put(CONVERSATION_FIELD_UNREAD_COUNT, 0);

            update(context, threadId, conversationValues, CONVERSATION);
            return true;
        } else return false;
    }

    public Cursor queryForThreads(Context context, String where) {
        String orderBy = "";

        DatabaseHelper helper = getDbHelper(context);
        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.query(THREADS_TABLE_NAME, THREADS_PROJECTION, where, null, null, null, orderBy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Cursor queryForConversation(Context context, long threadId) {
        String where = "";
        String orderBy = CONVERSATION_FIELD_DATE + " desc";

        where = MESSAGES_FIELD_THREAD_ID + "=" + threadId;

        DatabaseHelper helper = getDbHelper(context);
        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.query(CONVERSATION_TABLE_NAME, CONVERSATION_PROJECTION, where, null, null, null, orderBy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int queryForUnreadCount(Context context) {
        DatabaseHelper helper = getDbHelper(context);
        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = null;
            try {

                cursor = db.rawQuery("select sum(unreadcount) from conversation", null);
                cursor.moveToFirst();

                int unreadCount = cursor.getInt(0);

                return unreadCount;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeCursor(cursor);
            }
        }
        return 0;
    }

    public Cursor queryForAllConversation(Context context) {
        String where = null;
        String orderBy = "";

        orderBy = CONVERSATION_FIELD_DATE + " desc";

        DatabaseHelper helper = getDbHelper(context);
        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.query(CONVERSATION_TABLE_NAME, CONVERSATION_PROJECTION, where, null, null, null, orderBy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Cursor queryForMessages(Context context, long threadId) {
        String where = "";
        String orderBy = "";

        // switch (type) {
        // case QUERY_SEND_MESSAGE:
        // where = FIELD_TYPE+"="+SEND_TYPE;
        // break;
        // case QUERY_RECEIVED_MESSAGE:
        // where = FIELD_TYPE+"="+RECEIVE_TYPE;
        // break;
        // case QUERY_MESSAGE:
        // break;
        // }

        where = MESSAGES_FIELD_THREAD_ID + "=" + threadId;

        // switch (sort) {
        // case QUERY_SORT_ASC:
        // orderBy = FIELD_DATE+" ASC";
        // break;
        // case QUERY_SORT_DESC:
        // orderBy = FIELD_DATE+" DESC";
        // break;
        // }

        DatabaseHelper helper = getDbHelper(context);
        synchronized (dbHandle) {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                return db.query(MESSAGES_TABLE_NAME, MESSAGES_PROJECTION, where, null, null, null, orderBy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // threads 테이블
            db.execSQL("CREATE TABLE " + THREADS_TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY NOT NULL,"
                    + THREADS_FIELD_THREAD_EMAIL + " TEXT NOT NULL," + THREADS_FIELD_THREAD_NAMES + " TEXT NOT NULL"
                    + ");");

            // conversation 테이블
            db.execSQL("CREATE TABLE " + CONVERSATION_TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY NOT NULL,"
                    + CONVERSATION_FIELD_THREAD_ID + " INTEGER NOT NULL," + CONVERSATION_FIELD_SNIPPET + " TEXT,"
                    + CONVERSATION_FIELD_DATE + " INTEGER NOT NULL," + CONVERSATION_FIELD_STATUS + " INTEGER NOT NULL,"
                    + CONVERSATION_FIELD_MSG_COUNT + " INTEGER NOT NULL," + CONVERSATION_FIELD_UNREAD_COUNT
                    + " INTEGER NOT NULL," + CONVERSATION_FIELD_FAVORITY + " INTEGER," + CONVERSATION_FIELD_BLOCKED
                    + " INTEGER," + " FOREIGN KEY (" + CONVERSATION_FIELD_THREAD_ID + ") REFERENCES "
                    + THREADS_TABLE_NAME + " (" + _ID + "));");

            // messages 테이블
            db.execSQL("CREATE TABLE " + MESSAGES_TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY NOT NULL,"
                    + MESSAGES_FIELD_THREAD_ID + " INTEGER NOT NULL," + MESSAGES_FIELD_EMAIL + " TEXT NOT NULL,"
                    + MESSAGES_FIELD_NAMES + " TEXT," + MESSAGES_FIELD_BODY + " TEXT," + MESSAGES_FIELD_TYPE
                    + " TEXT NOT NULL," + MESSAGES_FIELD_DATE + " INTEGER NOT NULL," + MESSAGES_FIELD_STATUS
                    + " INTEGER NOT NULL," + MESSAGES_FIELD_READ + " INTEGER NOT NULL," + " FOREIGN KEY ("
                    + MESSAGES_FIELD_THREAD_ID + ") REFERENCES " + THREADS_TABLE_NAME + " (" + _ID + "));");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + THREADS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CONVERSATION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE_NAME);
            onCreate(db);
        }
    }
}
