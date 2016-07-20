package leesc.chatchat.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import leesc.chatchat.R;

public class IndexBar extends View {
    private static Object[] mIndexChar;
    private SectionIndexer mSectionIndexer = null;
    private ListView mListView;
    private String mCurrentIndex;
    private TextView mIndexView;
    private float mTextSize;
    private float mCircleRadius;
    private int mCircleColor = 0xFF7D95CD;
    private Paint mPaint;

    public IndexBar(Context context) {
        super(context);
        init(context);
    }

    /**
     * set IndexBar for PhoneBook
     * 
     * @return null
     */
    public IndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * set the first character for Hangul.
     * 
     * @return null
     */
    private void init(Context context) {
        mIndexChar = new Object[] { "ㄱ", "ㄴ", "ㄷ", "ㄹ", "ㅁ", "ㅂ", "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ", "A", "F",
            "K", "P", "U", "Z", "#" };
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, context.getResources()
            .getDisplayMetrics());
        mCircleRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, context.getResources()
            .getDisplayMetrics()) / 2;
        mPaint = new Paint();
        getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                getRootView().getWindowVisibleDisplayFrame(r);

                int screenHeight = getRootView().getRootView().getHeight();
                int keyboardHeight = screenHeight - (r.bottom);

                if (keyboardHeight > 0) {
                    setVisibility(View.GONE);
                } else {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        setVisibility(View.GONE);
                    } else {
                        setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * set ListView and SectionIndexer.
     * 
     * @return null
     */
    public void setPinnedHeaderListView(ListView listView) {
        mListView = listView;
    }

    public void setSectionIndexer(SectionIndexer sectionIndexer) {
        mSectionIndexer = sectionIndexer;
    }

    public void setIndexView(TextView indexView) {
        mIndexView = indexView;
        mIndexView.setVisibility(View.GONE);
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        invalidate();
    }

    /**
     * According to motionEvent's position, set its selection.
     * 
     * @return list's proper location (selection)
     */
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (mIndexView != null) {
            setBackgroundResource(R.drawable.etc_contact_bg);
            mIndexView.setVisibility(View.GONE);
        }

        if (mListView == null)
            return false;
        int size = (getMeasuredHeight() / mIndexChar.length) - 1;
        int i = (int) event.getY();
        int idx = i / size;
        if (idx >= mIndexChar.length) {
            idx = mIndexChar.length - 1;
        } else if (idx < 0) {
            idx = 0;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mListView == null) {
                return false;
            }

            mCurrentIndex = mIndexChar[idx].toString();
            int position = mSectionIndexer.getPositionForSection(mCurrentIndex.charAt(0));

            if (mIndexView != null) {
                mIndexView.setText(mIndexChar[idx].toString());
                setBackgroundResource(R.drawable.etc_contact_bg);
                mIndexView.setVisibility(View.VISIBLE);
            }
            if (position == -1) {
                return true;
            } else {
                mListView.setSelection(position);
            }
        } else {
            mCurrentIndex = null;
        }
        invalidate();
        return true;
    }

    /**
     * drawing IndexBar for PhoneBook
     * 
     * @return null
     */
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        float widthCenter = getMeasuredWidth() / 2;
        float size = (getMeasuredHeight() / (mIndexChar.length)) - 1;
        for (int i = 0; i < mIndexChar.length; i++) {

            String ch = String.valueOf(mIndexChar[i]);

            if (mCurrentIndex != null && mCurrentIndex.equals(ch)) {
                // 글씨 배경
                float circleY = size + (i * size) - (mTextSize * 7 / 20);
                mPaint.setColor(mCircleColor);
                canvas.drawCircle(widthCenter, circleY, mCircleRadius, mPaint);
                // 글씨
                mPaint.setColor(0xFFFFFFFF);
                canvas.drawText(ch, widthCenter, size + (i * size), mPaint);
            } else {
                mPaint.setColor(0xFF9b9b96);
                canvas.drawText(ch, widthCenter, size + (i * size), mPaint);
            }
        }
        super.onDraw(canvas);
    }
}
