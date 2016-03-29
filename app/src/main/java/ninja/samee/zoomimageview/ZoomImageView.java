package ninja.samee.zoomimageview;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by samee on 3/28/16.
 */
public class ZoomImageView extends ImageView {

    float scaleFactor = 2.5f;
    boolean isImageZoomed = false;
    float adjustedWidth;
    float adjustedHeight;
    Activity mActivity;
    ImageView mImageView;

    public ZoomImageView(Context context) {
        super(context);
        mActivity = (Activity) context;
        mImageView = this;
        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        mImageView = this;
        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        mImageView = this;
        init();
    }

    void translateImageAnim (float fromWidth, float fromHeight, float toWidth, float toHeight, View target) {
        ObjectAnimator animTransNewX = ObjectAnimator.ofFloat(target, "translationX", fromWidth, toWidth);
        ObjectAnimator animTransNewY = ObjectAnimator.ofFloat(target, "translationY", fromHeight, toHeight);
        animTransNewX.setDuration(350);
        animTransNewY.setDuration(350);
        animTransNewX.start();
        animTransNewY.start();
    }

    void scaleImageAnim(float from, float to, View target) {
        ObjectAnimator animScaleNewX = ObjectAnimator.ofFloat(target, "scaleX", from, to);
        ObjectAnimator animScaleNewY = ObjectAnimator.ofFloat(target, "scaleY", from, to);
        animScaleNewX.setDuration(350);
        animScaleNewY.setDuration(350);
        animScaleNewX.start();
        animScaleNewY.start();

    }


    Screen calculateAdjustedViewBounds (Screen screenVals) {
        float width = Utils.getRawScreenWidth(mActivity);
        float height = Utils.getRawScreenHeight(mActivity);

        //This Handles when screen is in either landscape or portrait mode
        scaleFactor = (float) (0.7 * height)/getHeight();
        screenVals.width = width/2 - getWidth()/2 - getX();
        screenVals.height = (float) (height/2 - getHeight()/1.5) - getY();

        return screenVals;
    }

    private void init() {

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isImageZoomed) {
                    float width = Utils.getScreenWidth(mActivity);
                    float height = Utils.getScreenHeight(mActivity);
                    Screen adjustedScreen = calculateAdjustedViewBounds(new Screen(width, height));
                    adjustedWidth = adjustedScreen.width;
                    adjustedHeight = adjustedScreen.height;

                    translateImageAnim(0,0, adjustedWidth, adjustedHeight, mImageView);
                    scaleImageAnim(1f, scaleFactor, mImageView);

                    //Animation for "dulling" the screen behind the image
//                    Animation animDarken = new AlphaAnimation(0.0f, 0.8f);
//                    animDarken.setFillAfter(true);
//                    animDarken.setDuration(350);


//                    rect.setVisibility(View.VISIBLE);
//                    rect.startAnimation(animDarken);
//                    rect.bringToFront();
                    bringToFront();
                    isImageZoomed = true;
                } else {
                    translateImageAnim(adjustedWidth, adjustedHeight, 0 ,0 , mImageView);
                    scaleImageAnim(scaleFactor, 1f, mImageView);
                    isImageZoomed = false;


                }

            }
        });
    }
}
