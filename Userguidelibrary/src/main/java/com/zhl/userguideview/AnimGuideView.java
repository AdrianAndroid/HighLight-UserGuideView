package com.zhl.userguideview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public class AnimGuideView extends View {
    public static final int VIEWSTYLE_RECT = 0;
    public static final int VIEWSTYLE_CIRCLE = 1;
    public static final int VIEWSTYLE_OVAL = 2;
    public static final int MASKBLURSTYLE_SOLID = 0; // 模糊
    public static final int MASKBLURSTYLE_NORMAL = 1; //模糊


    private int screenW, screenH;// 屏幕宽高
    private int maskblurstyle = MASKBLURSTYLE_SOLID;
    private int highLightStyle = VIEWSTYLE_RECT; //高亮的模式

    private Canvas mCanvas;// 给蒙版层的画布
    private Paint mPaint;//绘制蒙版层画笔
    private Bitmap fgBitmap; //前景
    private Bitmap tipBitmap; // 提示的bitmap
    private int maskColor = 0x99000000;// 蒙版层颜色

    private Bitmap bmHands;// 手指指向
    private Bitmap bmIknow;

    private float corner = 20;
    private int statusBarHeight; // 状态栏的高度

    private float handsOffset = 0;

    //private Bitmap jtUpLeft, jtUpRight, jtUpCenter, jtDownRight, jtDownLeft, jtDownCenter;// 指示箭头
    private View targetView;

    public AnimGuideView(Context context) {
        super(context);
        initialize(context, null);
    }

    public AnimGuideView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public AnimGuideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {

        if (attrs != null) {
//            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.UserGuideView);
//            highLightStyle = array.getInt(R.styleable.UserGuideView_HighlightViewStyle, VIEWSTYLE_RECT);
//            maskblurstyle = array.getInt(R.styleable.UserGuideView_MaskBlurStyle, MASKBLURSTYLE_SOLID);
//            BitmapDrawable drawable = (BitmapDrawable) array.getDrawable(R.styleable.UserGuideView_tipView);
//            maskColor = array.getColor(R.styleable.UserGuideView_maskColor, maskColor);
//            if (drawable != null) {
//                tipBitmap = drawable.getBitmap();
//            }

//            array.recycle();
            bmHands = getBitmap(R.drawable.hands, 36, 36);// BitmapFactory.decodeResource(getResources(), R.drawable.shouzhi);
            bmIknow = BitmapFactory.decodeResource(getResources(), R.mipmap.iknown);
        }


        cal(context);
        init(context);
    }


    public Bitmap getBitmap(@DrawableRes final int resId,
                            final int maxWidth,
                            final int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        final Resources resources = getResources();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * Return the sample size.
     *
     * @param options   The options.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return the sample size
     */
    private int calculateInSampleSize(final BitmapFactory.Options options,
                                      final int maxWidth,
                                      final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while ((width >>= 1) >= maxWidth && (height >>= 1) >= maxHeight) {
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }


    private void cal(Context context) {
        int[] screenSize = MeasureUtil.getScreenSize(context);
        screenW = screenSize[0];
        screenH = screenSize[1];
    }


    /**
     * 初始化对象
     */
    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        mPaint.setARGB(0, 255, 0, 0);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        BlurMaskFilter.Blur blurStyle = null;
        switch (maskblurstyle) {
            case MASKBLURSTYLE_SOLID:
                blurStyle = BlurMaskFilter.Blur.SOLID;
                break;
            case MASKBLURSTYLE_NORMAL:
                blurStyle = BlurMaskFilter.Blur.NORMAL;
                break;
        }
        mPaint.setMaskFilter(new BlurMaskFilter(15, blurStyle));

        fgBitmap = MeasureUtil.createBitmapSafely(screenW, screenH, Bitmap.Config.ARGB_8888, 2);
        if (fgBitmap == null) {
            throw new RuntimeException("out of memory cause fgbitmap create fail");
        }
        mCanvas = new Canvas(fgBitmap);
        mCanvas.drawColor(maskColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (targetView == null) {
            return;
        }

        // 画背景
        canvas.drawBitmap(fgBitmap, 0, 0, null);

        Rect rect = new Rect();
        targetView.getGlobalVisibleRect(rect);
        rect.offset(0, -statusBarHeight);

        //  绘制高亮
        switch (highLightStyle) {
            case VIEWSTYLE_RECT: // 正方形
                RectF rectF = new RectF(rect);
                mCanvas.drawRoundRect(rectF, corner, corner, mPaint);
                break;
            case VIEWSTYLE_CIRCLE: // 圆形
                break;
            case VIEWSTYLE_OVAL: //椭圆形
                break;
        }


        // rect.offset(0, statusBarHeight);
        drawHands(canvas, rect);
        drawHints(canvas, rect);
    }

    private void drawHints(Canvas canvas, Rect rect) {
        // "我知道了" 提示
        canvas.drawBitmap(bmIknow, getIKnowLeft(rect), getIKnowTop(rect), null);
    }


    private float getIKnowLeft(Rect rect) {
        return rect.left - bmIknow.getWidth() - 20;
    }

    private float getIKnowTop(Rect rect) {
        return rect.top - (Math.abs(rect.top - rect.bottom) >> 1 - bmIknow.getHeight() >> 1);
    }


    private void drawHands(Canvas canvas, Rect rect) {
        // 手指头
        canvas.drawBitmap(bmHands, getHandsLeft(rect), getHandsTop(rect), null);
    }


    private float getHandsLeft(Rect targetRect) {
        return targetRect.left + (Math.abs(targetRect.left - targetRect.right) >> 1) - (bmHands.getWidth() >> 1);
    }

    private float getHandsTop(Rect targetRect) {
        return targetRect.top - bmHands.getHeight() - handsOffset;
    }


    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    // 设置高亮的View
    public void setHighLightView(View targetView) {
        if (this.targetView != null && targetView != null && this.targetView != targetView) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // 清除之前的模式
            mCanvas.drawPaint(paint);
            mCanvas.drawColor(maskColor);
        }
        this.targetView = targetView;
        invalidate();
        setVisibility(VISIBLE);
    }


    public void setHandsOffset(float handsOffset) {
        this.handsOffset = handsOffset;
        invalidate();
    }

    public float getHandsOffset() {
        return handsOffset;
    }

    public void startHandsAnimate() {
        ObjectAnimator handsOffset = ObjectAnimator.ofFloat(this, "handsOffset", 0F, 1F, 2F, 3F, 10F, 15F, 17F, 18F, 19F, 20F);
        handsOffset.setInterpolator(new LinearInterpolator());
        handsOffset.setDuration(500);
        handsOffset.setRepeatCount(ObjectAnimator.INFINITE);
        handsOffset.setRepeatMode(ObjectAnimator.REVERSE);
        handsOffset.start();
    }
}
