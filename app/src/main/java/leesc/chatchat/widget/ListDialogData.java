package leesc.chatchat.widget;

public class ListDialogData {

    private String mTitleText;
    private int mTitleTextSize;
    private boolean mSelected;

    public ListDialogData(String title, boolean selected) {
        mTitleText = title;
        mSelected = selected;
    }

    public ListDialogData(String title, int textSize, boolean selected) {
        mTitleText = title;
        mTitleTextSize = textSize;
        mSelected = selected;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public void setTitleText(String titleText) {
        mTitleText = titleText;
    }

    public int getTitleTextSize() {
        return mTitleTextSize;
    }

    public void setTitleTextSize(int titleTextSize) {
        mTitleTextSize = titleTextSize;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

}
