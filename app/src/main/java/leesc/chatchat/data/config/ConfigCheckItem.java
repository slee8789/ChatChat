package leesc.chatchat.data.config;

public class ConfigCheckItem extends ConfigItem {

    public ConfigCheckItem(int iconResourceId, int titleResourceId, boolean checked) {
        super(iconResourceId, titleResourceId);
        mType = CHECK_TYPE;
        mChecked = checked;
        mEnabled = true;
    }

    public ConfigCheckItem(int iconResourceId, int titleResourceId, boolean checked, boolean enabled) {
        super(iconResourceId, titleResourceId);
        mType = CHECK_TYPE;
        mChecked = checked;
        mEnabled = enabled;
    }

    public ConfigCheckItem(int iconResourceId, int titleResourceId, String infoText, boolean checked) {
        super(iconResourceId, titleResourceId, infoText);
        mType = CHECK_TYPE;
        mChecked = checked;
        mEnabled = true;
    }

    public ConfigCheckItem(int iconResourceId, int titleResourceId, String infoText, boolean checked, boolean enabled) {
        super(iconResourceId, titleResourceId, infoText);
        mType = CHECK_TYPE;
        mChecked = checked;
        mEnabled = enabled;
    }

}
