package leesc.chatchat.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class RecipientIdCache {
    private static final String TAG = "Chat/Cache";
    private static final boolean DEBUG = false;
    private static RecipientIdCache sInstance;

    static RecipientIdCache getInstance() {
        return sInstance;
    }

    private final Map<Long, Entry> mCache;
    private final ArrayList<Entry> mInfo;
    private final Context mContext;

    public static class Entry {
        public long id;
        public String names;
        public String emails;

        public Entry(long id, String names, String emails) {
            this.id = id;
            this.names = names;
            this.emails = emails;
        }

    };

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new RecipientIdCache(context);
        }

        new Thread(new Runnable() { // $codepro.audit.disable disallowUnnamedThreadUsage
                    public void run() {
                        fill(sInstance.mContext);
                    }
                }).start();
    }

    RecipientIdCache(Context context) {
        mCache = new HashMap<Long, Entry>();
        mInfo = new ArrayList<Entry>();
        mContext = context;
    }

    public static void fill(Context context) {
        if (DEBUG) {
            Log.d(TAG, "[RecipientIdCache] fill: begin");
        }

        Cursor c = MessageDB.getInstance().getThreadCursor(context);
        if (c == null) {
            Log.w(TAG, "null Cursor in fill()");
            return;
        }

        try {
            synchronized (sInstance) {
                // Technically we don't have to clear this because the stupid
                // canonical_addresses table is never GC'ed.
                sInstance.mCache.clear();
                sInstance.mInfo.clear();
                c.move(-1);
                while (c.moveToNext()) {
                    // TODO: don't hardcode the column indices
                    long id = c.getLong(0);
                    String names = c.getString(2);
                    String emails = c.getString(1);
//                    Log.e(TAG, "names " + names);
//                    Log.e(TAG, "numbers " + emails);
                    Entry info = new Entry(id, names, emails);
                    sInstance.mCache.put(id, info);
                }
            }
        } finally {
            c.close();
        }

        if (DEBUG) {
            Log.d(TAG, "[RecipientIdCache] fill: finished");
            dump();
        }
    }

    public static Entry getEmail(String threadId) {
        synchronized (sInstance) {
            Entry email = null;
//            Log.e(TAG, "getEmail " + threadId);
            String[] ids = threadId.split(" ");
            for (String id : ids) {
                long longId;

                try {
                    longId = Long.parseLong(id);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    continue;
                }

                if (sInstance.mCache != null) {
                    email = sInstance.mCache.get(longId);
                }
                if (email == null) {
                    fill(sInstance.mContext);
                    email = sInstance.mCache.get(longId);
                }
            }
           
            return email;
        }
    }

    // public static ArrayList<String> getAddressList() {
    // ArrayList<String> address = new ArrayList<String>();
    // if (sInstance.mCache != null ) {
    // Iterator it = sInstance.mCache.keySet().iterator();
    // while (it.hasNext()) {
    // Long key = (Long) it.next();
    // address.add(sInstance.mCache.get(key));
    // }
    // }
    // return address;
    // }

    public static void dump() {
        // Only dump user private data if we're in special debug mode
        synchronized (sInstance) {
            Log.d(TAG, "*** Recipient ID cache dump ***");
            for (Long id : sInstance.mCache.keySet()) {
                Log.d(TAG, id + ": " + sInstance.mCache.get(id));
            }
        }
    }

}
