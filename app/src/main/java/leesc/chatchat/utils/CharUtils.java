package leesc.chatchat.utils;

import android.text.TextUtils;

import com.mogua.localization.KoreanChar;

public class CharUtils {

    public static String getInitialChar(String str) {
    	if (TextUtils.isEmpty(str))
    	{
    		return "";
    	}
        char initialChar = Character.toUpperCase(str.charAt(0));
        if (KoreanChar.isSyllable(initialChar)) {
            initialChar = KoreanChar.getCompatibilityChoseong(initialChar);
        }
        return Character.toString(initialChar);
    }
}
