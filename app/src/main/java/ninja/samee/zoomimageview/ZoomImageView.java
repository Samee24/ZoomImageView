package ninja.samee.zoomimageview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ZoomImageView extends ImageView {

    private static final int ANIM_DURATION = 300;

    private float scaleFactor = 1000f;
    private boolean isImageZoomed = false;
    private boolean isAnimating = false;
    private float adjustedWidth;
    private float adjustedHeight;
    private Activity mActivity;
    private ImageView mImageView;

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

    void translateImageAnim (float toWidth, float toHeight, View target) {
        target.animate()
                .translationX(toWidth)
                .translationY(toHeight)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isAnimating = false;
                    }
                })
                .start();
    }

    void scaleImageAnim(float to, View target) {
        target.animate()
                .scaleX(to)
                .scaleY(to)
                .setDuration(ANIM_DURATION)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isAnimating = false;
                    }
                })
                .start();
    }


    Screen calculateAdjustedViewBounds (Screen screenVals) {
        float width = Utils.getRawScreenWidth(mActivity);
        float height = Utils.getRawScreenHeight(mActivity);

        //This handles scaling when screen is in either landscape or portrait mode
        scaleFactor = (float) (0.7 * height)/getHeight();
        screenVals.width = width/2 - getWidth()/2 - getX();
        screenVals.height = (float) (height/2 - getHeight()/1.5) - getY();

        return screenVals;
    }

    private void init() {

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            if (!isAnimating) {
                isAnimating = true;
                if (!isImageZoomed) {
                    float width = Utils.getScreenWidth(mActivity);
                    float height = Utils.getScreenHeight(mActivity);
                    Screen adjustedScreen = calculateAdjustedViewBounds(new Screen(width, height));
                    adjustedWidth = adjustedScreen.width;
                    adjustedHeight = adjustedScreen.height;

                    translateImageAnim(adjustedWidth, adjustedHeight, mImageView);
                    scaleImageAnim(scaleFactor, mImageView);

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
                    translateImageAnim(0, 0, mImageView);
                    scaleImageAnim(1f, mImageView);
                    isImageZoomed = false;
                }
            }
            }
        });
    }
}
