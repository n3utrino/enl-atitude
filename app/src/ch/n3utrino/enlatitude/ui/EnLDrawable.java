package ch.n3utrino.enlatitude.ui;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;
import ch.n3utrino.enlatitude.R;
import ch.n3utrino.enlatitude.common.User;

/**
 * Created with IntelliJ IDEA.
 * User: n3utrino
 * Date: 22.01.13
 * Time: 05:44
 * To change this template use File | Settings | File Templates.
 */
public class EnLDrawable{


    private Paint textPaint = new Paint();
    private Paint smallTextPaint = new Paint();
    private final float textSize;

    private String name;
    private Bitmap mBitmap;
    private Canvas mCanvas;




    public EnLDrawable(User user, Resources res) {


        this.name = user.getName();
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, res.getDisplayMetrics());
        float smallTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, res.getDisplayMetrics());


        String justNow = res.getString(R.string.just_now);
        String vorMinuten = res.getString(R.string.minutes_ago);

        int mins = (int) (user.getLastUpdateSince() / 1000f / 60f);
        String ago = mins > 1 ? String.format(vorMinuten, mins) : justNow;

        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        smallTextPaint.setTextSize(smallTextSize);
        smallTextPaint.setColor(Color.BLACK);
        smallTextPaint.setStyle(Paint.Style.FILL);
        smallTextPaint.setAntiAlias(true);
        smallTextPaint.setTypeface(Typeface.DEFAULT);

        NinePatchDrawable ninePatchDrawableAnchor = (NinePatchDrawable) res.getDrawable(R.drawable.map_callout_bottom_anchorl);
        NinePatchDrawable ninePatchDrawableCapLeft = (NinePatchDrawable) res.getDrawable(R.drawable.map_callout_left_cap);
        NinePatchDrawable ninePatchDrawableCapRight = (NinePatchDrawable) res.getDrawable(R.drawable.map_callout_right_capl);


        int line_padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.5f, res.getDisplayMetrics());
        int bitmapHeight = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, res.getDisplayMetrics()));

        int bitmapWidth = (int)((int) Math.max((textPaint.measureText(name)), smallTextPaint.measureText(ago)))
                +ninePatchDrawableCapLeft.getMinimumWidth()
                +ninePatchDrawableCapRight.getMinimumWidth();


        ninePatchDrawableCapLeft.setBounds(0,0,ninePatchDrawableCapLeft.getMinimumWidth(),bitmapHeight);

        ninePatchDrawableAnchor.setBounds(ninePatchDrawableCapLeft.getMinimumWidth(),0,bitmapWidth-ninePatchDrawableCapRight.getMinimumWidth(),bitmapHeight);
        ninePatchDrawableCapRight.setBounds(ninePatchDrawableAnchor.getBounds().right,0,bitmapWidth,bitmapHeight);
        mBitmap = Bitmap.createBitmap(bitmapWidth,bitmapHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        ninePatchDrawableCapLeft.draw(mCanvas);
        ninePatchDrawableAnchor.draw(mCanvas);
        ninePatchDrawableCapRight.draw(mCanvas);

        mCanvas.drawText(name,ninePatchDrawableAnchor.getBounds().left, mBitmap.getHeight()-ninePatchDrawableAnchor.getIntrinsicHeight()+textSize,textPaint);
        mCanvas.drawText(ago,ninePatchDrawableAnchor.getBounds().left, mBitmap.getHeight()-ninePatchDrawableAnchor.getIntrinsicHeight()+textSize+line_padding+smallTextSize,smallTextPaint);
        mCanvas.save();

    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    public Bitmap getBitmap() {


        return mBitmap;

    }
}
