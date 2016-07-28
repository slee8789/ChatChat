package leesc.chatchat.data.config;

public class ConfigNormalItem extends ConfigItem {

    public ConfigNormalItem(int iconResourceId, int titleResourceId) {
        super(iconResourceId, titleResourceId);
        mType = NORMAL_TYPE;
        mEnabled = true;
    }

    public ConfigNormalItem(int iconResourceId, int titleResourceId, boolean isEnabled) {
        super(iconResourceId, titleResourceId);
        mType = NORMAL_TYPE;
        mEnabled = isEnabled;
    }

    public ConfigNormalItem(int iconResourceId, int titleResourceId, String infoText) {
        super(iconResourceId, titleResourceId, infoText);
        mType = NORMAL_TYPE;
        mEnabled = true;
    }

    public ConfigNormalItem(int iconResourceId, int titleResourceId, String infoText, boolean isEnabled) {
        super(iconResourceId, titleResourceId, infoText);
        mType = NORMAL_TYPE;
        mEnabled = isEnabled;
    }
}
