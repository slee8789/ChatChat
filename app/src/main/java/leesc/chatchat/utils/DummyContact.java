package leesc.chatchat.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import leesc.chatchat.R;

public class DummyContact {

    private static final int[] PROFILE_ICONS = new int[] {
        R.mipmap.chat_icon_profile01,
        R.mipmap.chat_icon_profile01_2,
        R.mipmap.chat_icon_profile01_3,
        R.mipmap.chat_icon_profile01_4,
        R.mipmap.chat_icon_profile01_5,
        R.mipmap.chat_icon_profile02,
        R.mipmap.chat_icon_profile02_2,
        R.mipmap.chat_icon_profile02_3,
        R.mipmap.chat_icon_profile02_4,
        R.mipmap.chat_icon_profile02_5,
    };

    private static Uri sAllCanonical = Uri.parse("content://mms-sms/canonical-addresses");
    private static DummyContact sInstance;
    private final Map<Long, String> mCache;
    private Context mContext;
    private static String[] mTagImageUrls;

    private DummyContact(Context context) {
        mCache = new HashMap<Long, String>();
        mContext = context;
    }

    static DummyContact getInstance() {
        return sInstance;
    }

    public static void init(final Context context) {
        sInstance = new DummyContact(context);
        new Thread(new Runnable() {
            public void run() {
                initGaJJaTagImage();
            }
        },"initThread").start();
    }

    public static String[] getGaJJaTagImageUrls() {
        if (mTagImageUrls == null || mTagImageUrls.length < 1) {
            initGaJJaTagImage();
        }
        return mTagImageUrls;
    }

    private static void initGaJJaTagImage() {
        Context context = sInstance.mContext;
        try {
            String[] imgs = context.getAssets().list("subtag");
            for (int i = 0; i < imgs.length; i++) {
                imgs[i] = "assets://subtag/" + imgs[i];
            }
            mTagImageUrls = imgs;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNumber(String recipientIds) {
        if (recipientIds.contains(" ")) {
            return String.format("%s 외 %s 명",
                recipientIds.split(" ").length);
        }

        String number = sInstance.mCache.get(Long.parseLong(recipientIds));

        if (TextUtils.isEmpty(number)) {
            Context context = sInstance.mContext;
            return context.getString(R.string.common_no_number);
        }
        return "";
    }

    public static int getAvatar(String recipientIds) {
        if (recipientIds.contains(" ")) {
            return R.mipmap.chat_list_thumbnail_group_001;
        }
        if (recipientIds.length() == 0) {
            return PROFILE_ICONS[0];
        }
        char lastCharacter = recipientIds.charAt(recipientIds.length() - 1);
        if (Character.isDigit(lastCharacter)) {
            int lastValue = Character.getNumericValue(lastCharacter);
            if (lastValue == -1 || lastValue == -2) {
                return PROFILE_ICONS[0];
            }
            return PROFILE_ICONS[lastValue];
        } else {
            return PROFILE_ICONS[0];
        }
    }
}
