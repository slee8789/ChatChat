package leesc.chatchat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import leesc.chatchat.R;

public class HighlightTextView extends TextView {

    private CharSequence mHighlightText = "";
    private SpannableString mSpannedText;
    private ForegroundColorSpan mHighlightTextColorSpan = new ForegroundColorSpan(getResources().getColor(
        R.color.common_searched_text_color));

    public HighlightTextView(Context context) {
        super(context);
    }

    public HighlightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HighlightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CharSequence getHighlightText() {
        return mHighlightText;
    }

    public void setHighlightText(CharSequence highlightText) {
        mHighlightText = highlightText != null ? highlightText : "";
        if (mHighlightText.length() > 0) {
            mSpannedText = new SpannableString(getText());
        }
    }

    public int getHighlightTextColor() {
        return mHighlightTextColorSpan.getForegroundColor();
    }

    public void setHighlightTextColor(int color) {
        mHighlightTextColorSpan = new ForegroundColorSpan(color);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mHighlightText.length() > 0) {
            final int highlightTextIndex = getText().toString().indexOf(mHighlightText.toString());
            if (highlightTextIndex > -1) {
                final Layout layout = getLayout();
                final int ellipsisCount = layout.getEllipsisCount(0);
                final int ellipsisStart = layout.getEllipsisStart(0);
                final int start, end;
                if (ellipsisCount > 0 && highlightTextIndex >= ellipsisStart) {
                    start = ellipsisStart;
                    end = ellipsisStart + 1;
                } else {
                    start = highlightTextIndex;
                    end = highlightTextIndex + mHighlightText.length();
                }
                mSpannedText.setSpan(mHighlightTextColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                setText(mSpannedText);
            }
        }
        super.onDraw(canvas);
    }
}
