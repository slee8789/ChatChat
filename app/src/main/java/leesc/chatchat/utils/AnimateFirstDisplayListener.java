package leesc.chatchat.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

    private final List<String> mDisplayedImages = Collections.synchronizedList(new LinkedList<String>());

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        if (loadedImage != null && view != null) {
            boolean alreadyDisplayed = mDisplayedImages.contains(imageUri);
            if (!alreadyDisplayed) {
                view.requestLayout();
                animateBeating(view, 700);
                mDisplayedImages.add(imageUri);
            }
        }
    }

    private static void animateBeating(View imageView, int durationMillis) {
        if (imageView != null) {
            ScaleAnimation fadeImage = new ScaleAnimation(0.3f, 1.05f, 0.3f, 1.05f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            fadeImage.setDuration(durationMillis);
            fadeImage.setInterpolator(new AccelerateInterpolator(1.0f));
            imageView.startAnimation(fadeImage);
        }
    }
}
