package leesc.chatchat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import leesc.chatchat.utils.ConfigSettingPreferences;

public class ConfigPreviewActivity extends Activity {

    public static final String INTENT_EXTRA_PREVIEW_TYPE = "preview_type";
    public static final String INTENT_EXTRA_SELECTED_THEME_INDEX = "selected_theme_index";
    public static final String INTENT_EXTRA_SELECTED_THEME_NAME = "selected_theme_name";
    public static final String INTENT_EXTRA_SELECTED_BG_INDEX = "selected_bg_index";
    public static final String INTENT_EXTRA_SELECTED_BG_NAME = "selected_bg_name";
    public static final String INTENT_EXTRA_SELECTED_BG_VALUE = "selected_bg_value";
    public static final int PREVIEW_TYPE_THEME = 0;
    public static final int PREVIEW_TYPE_BG = 1;
    public static final int PREVIEW_TYPE_PHOTO_TAKING = 2;
    public static final int PREVIEW_TYPE_PHOTO_SELECT = 3;

    private ImageView mPreviewImage;
    private Button mApplyButton;

    private SparseIntArray mThemePortraitPreviewImages;
    private SparseIntArray mThemeLandscapePreviewImages;
    private SparseIntArray mBackgroundPreviewImages;

    private DisplayImageOptions mOptions;
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    
    private String mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_preview);
        findViews();
        initViews();
    }

    private void findViews() {
        mPreviewImage = (ImageView) findViewById(R.id.config_preview_image);
        mApplyButton = (Button) findViewById(R.id.config_preview_apply_button);
        mOptions = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.skin_preview_00)
            .showImageOnFail(R.mipmap.skin_preview_00)
            .cacheInMemory(false)
            .cacheOnDisc(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .resetViewBeforeLoading(true)
            .considerExifParams(true)
            .displayer(new SimpleBitmapDisplayer())
            .build();
    }

    private void initViews() {
        initActionBar();
        initPreviewImage();
        initPreviewType();
        initEvent();
    }

    private void initActionBar() {
//        mActionBar.setDisplayHomeAsUpEnabled(true);
//        mActionBar.setHomeButtonEnabled(true);
    }

    private void initPreviewType() {
        Intent intent = getIntent();
        int previewType = intent.getIntExtra(INTENT_EXTRA_PREVIEW_TYPE, PREVIEW_TYPE_THEME);
        switch (previewType) {
        case PREVIEW_TYPE_THEME:
            setThemePreview();
            break;
        case PREVIEW_TYPE_BG:
            int selectedBackgroundIndex = getIntent().getIntExtra(INTENT_EXTRA_SELECTED_BG_INDEX, 0);
            mPreviewImage.setImageResource(mBackgroundPreviewImages.get(selectedBackgroundIndex));
            break;
        case PREVIEW_TYPE_PHOTO_TAKING:
        case PREVIEW_TYPE_PHOTO_SELECT:
            mUri = getIntent().getData().toString();
            mImageLoader.displayImage(mUri, mPreviewImage, mOptions);
            break;
        default:
            throw new IllegalStateException("previewType: " + previewType);
        }
    }
    
    private void initPreviewImage() {
//        setThemePortraitResArray();
//        setThemeLandscapeResArray();
//        setBackgroundResArray();
    }

//    private void setThemePortraitResArray() {
//        mThemePortraitPreviewImages = new SparseIntArray();
//        mThemePortraitPreviewImages.append(0, R.drawable.theme_color_skin_preview_00);
//        mThemePortraitPreviewImages.append(1, R.drawable.theme_color_skin_preview_01);
//        mThemePortraitPreviewImages.append(2, R.drawable.theme_color_skin_preview_02);
//        mThemePortraitPreviewImages.append(3, R.drawable.theme_color_skin_preview_03);
//        mThemePortraitPreviewImages.append(4, R.drawable.theme_color_skin_preview_04);
//        mThemePortraitPreviewImages.append(5, R.drawable.theme_color_skin_preview_05);
//        mThemePortraitPreviewImages.append(6, R.drawable.theme_color_skin_preview_06);
//        mThemePortraitPreviewImages.append(7, R.drawable.theme_color_skin_preview_07);
//        mThemePortraitPreviewImages.append(8, R.drawable.theme_color_skin_preview_08);
//        mThemePortraitPreviewImages.append(9, R.drawable.theme_color_skin_preview_09);
//    }

//    private void setThemeLandscapeResArray() {
//        mThemeLandscapePreviewImages = new SparseIntArray();
//        mThemeLandscapePreviewImages.append(0, R.drawable.landscape_theme_color_skin_preview_00);
//        mThemeLandscapePreviewImages.append(1, R.drawable.landscape_theme_color_skin_preview_01);
//        mThemeLandscapePreviewImages.append(2, R.drawable.landscape_theme_color_skin_preview_02);
//        mThemeLandscapePreviewImages.append(3, R.drawable.landscape_theme_color_skin_preview_03);
//        mThemeLandscapePreviewImages.append(4, R.drawable.landscape_theme_color_skin_preview_04);
//        mThemeLandscapePreviewImages.append(5, R.drawable.landscape_theme_color_skin_preview_05);
//        mThemeLandscapePreviewImages.append(6, R.drawable.landscape_theme_color_skin_preview_06);
//        mThemeLandscapePreviewImages.append(7, R.drawable.landscape_theme_color_skin_preview_07);
//        mThemeLandscapePreviewImages.append(8, R.drawable.landscape_theme_color_skin_preview_08);
//        mThemeLandscapePreviewImages.append(9, R.drawable.landscape_theme_color_skin_preview_09);
//    }

//    private void setBackgroundResArray() {
//        mBackgroundPreviewImages = new SparseIntArray();
//        mBackgroundPreviewImages.append(0, R.drawable.skin_preview_00);
//        mBackgroundPreviewImages.append(1, R.drawable.skin_preview_01);
//        mBackgroundPreviewImages.append(2, R.drawable.skin_preview_02);
//        mBackgroundPreviewImages.append(3, R.drawable.skin_preview_03);
//        mBackgroundPreviewImages.append(4, R.drawable.skin_preview_04);
//        mBackgroundPreviewImages.append(5, R.drawable.skin_preview_05);
//        mBackgroundPreviewImages.append(6, R.drawable.skin_preview_06);
//    }

    private void setThemePreview() {
        int selectedThemeIndex = getIntent().getIntExtra(INTENT_EXTRA_SELECTED_THEME_INDEX, 0);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPreviewImage.setImageResource(mThemeLandscapePreviewImages.get(selectedThemeIndex));
        } else {
            mPreviewImage.setImageResource(mThemePortraitPreviewImages.get(selectedThemeIndex));
        }
    }

    private void initEvent() {
        mApplyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigSettingPreferences.getInstance(ConfigPreviewActivity.this).setPrefBackgroundUri(mUri);
                
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Intent intent = getIntent();
        int previewType = intent.getIntExtra(INTENT_EXTRA_PREVIEW_TYPE, PREVIEW_TYPE_THEME);
        if (previewType == PREVIEW_TYPE_THEME) {
            setThemePreview();
        }
        super.onConfigurationChanged(newConfig);
    }
}
