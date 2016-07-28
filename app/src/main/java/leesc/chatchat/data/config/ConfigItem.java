package leesc.chatchat.data.config;

public abstract class ConfigItem {

    public static final int TYPE_COUNT = 4;
    public static final int HEADER_TYPE = 0;
    public static final int NORMAL_TYPE = 1;
    public static final int CHECK_TYPE = 2;

    private int mIconResourceId;
    private int mTitleResourceId;
    private String mInfoText;
    protected int mType;
    protected boolean mChecked;
    protected boolean mEnabled;

    public ConfigItem(int titleResourceId) {
        mTitleResourceId = titleResourceId;
    }

    public ConfigItem(int iconResourceId, int titleResourceId) {
        mIconResourceId = iconResourceId;
        mTitleResourceId = titleResourceId;
    }

    public ConfigItem(int iconResourceId, int titleResourceId, String infoText) {
        mIconResourceId = iconResourceId;
        mTitleResourceId = titleResourceId;
        mInfoText = infoText;
    }

    public int getIconResourceId() {
        return mIconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        mIconResourceId = iconResourceId;
    }

    public int getTitleResourceId() {
        return mTitleResourceId;
    }

    public void setTitleResourceId(int titleResourceId) {
        mTitleResourceId = titleResourceId;
    }

    public String getInfoText() {
        return mInfoText;
    }

    public void setInfoText(String infoText) {
        mInfoText = infoText;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean isChecked) {
        mChecked = isChecked;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }
}
