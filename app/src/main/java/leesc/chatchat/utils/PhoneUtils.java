package leesc.chatchat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;

import java.util.regex.Pattern;

public class PhoneUtils {

    private static Pattern PHONE_NUMBER_PATTERN = Pattern.compile("(#|\\*|\\+)?(#|\\*)?[0-9][0-9|-|#|\\*|\\+]*$");

    public enum Manufacturer {
        SAMSUNG, LG, PANTECH, SKTELESYS, GOOGLE, UNKNOWN
    }

    public static Manufacturer getManufacturer() {
        String manufacturer = Build.MANUFACTURER;

        if (manufacturer == null) {
            return Manufacturer.UNKNOWN;
        }

        if (manufacturer.toLowerCase().contains("samsung")) {
            return Manufacturer.SAMSUNG;
        } else if (manufacturer.toLowerCase().contains("lge")) {
            return Manufacturer.LG;
        } else if (manufacturer.toLowerCase().contains("pantech")) {
            return Manufacturer.PANTECH;
        } else if (manufacturer.toLowerCase().contains("sk-telesys")) {
            return Manufacturer.SKTELESYS;
        } else if (manufacturer.toLowerCase().contains("google")) {
            return Manufacturer.GOOGLE;
        }
        return Manufacturer.UNKNOWN;
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean isPhone(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean isEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean startVideoCall(Context context, String phoneNumber) {
        if (!isPhoneNumber(phoneNumber)) {
            return false;
        }

        Intent callIntent = null;
        try {
            switch (getManufacturer()) {
            case SAMSUNG:
                callIntent = new Intent("com.android.phone.videocall");
                callIntent.putExtra("videocall", true);
                callIntent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber)));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
                return true;
            case LG:
                callIntent = new Intent("android.intent.action.VTCALL_START");
                callIntent.putExtra("PhoneNumber", phoneNumber);
                context.sendBroadcast(callIntent);
                return true;
            case PANTECH:
                callIntent = new Intent("com.pantech.action.VT_CALL");
                callIntent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber)));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
                return true;
            case SKTELESYS:
                callIntent = new Intent("com.sktelesys.action.VTCALL");
                callIntent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber)));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(callIntent);
                return true;

            default:
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean startPhoneCall(Context context, String phoneNumber) {
        if (!isPhoneNumber(phoneNumber)) {
            return false;
        }

        try {
            Uri uri = Uri.parse("tel:" + Uri.encode(phoneNumber));
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean screenOn = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            screenOn = pm.isInteractive();
        } else {
            screenOn = pm.isScreenOn();
        }

        return screenOn;
    }
}
