package leesc.chatchat.data.config;

public class ConfigHeaderItem extends ConfigItem {

    public ConfigHeaderItem(int titleResourceId) {
        super(titleResourceId);
        mType = HEADER_TYPE;
        mEnabled = false;
    }

}
