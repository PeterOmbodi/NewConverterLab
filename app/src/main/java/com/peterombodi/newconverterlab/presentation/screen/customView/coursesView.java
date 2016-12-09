package com.peterombodi.newconverterlab.presentation.screen.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.peterombodi.newconverterlab.data.model.Currency;
import com.peterombodi.newconverterlab.data.model.OrganizationRV;
import com.peterombodi.newconverterlab.presentation.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Admin on 06.12.2016.
 */

public class CoursesView extends View {

    private static final String TAG = "coursesView";
    private static final float LEFT_X_TITLE = 50;
    private static final float LEFT_X_NAME = 80;
    private static final float LEFT_X_COURSE = 320;

    private Paint paintForeground;
    private ArrayList<Currency> courseList;
    private OrganizationRV bank;

    private int viewWidth = 0;
    private String leadingZero =  "0000000000";

    private int titleColor;
    private int subTitleColor;
    private int nameColor;
    private int valueColor;
    private int canvasColor;

    // for visual design in IDE
    private float titleSize = 40;
    private float subTitleSize = 24;
    private float nameSize = 24;
    private float valueSize = 22;

    private int titleStyle;
    private int subTitleStyle;
    private int nameStyle;
    private int valueStyle;

    private float currentX = 0;

    public CoursesView(Context context) {
        super(context);
        init(context, null);
    }

    public CoursesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CoursesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CoursesView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        Log.d(TAG, "____init");

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CoursesView,
                0, 0
        );
        try {

            titleColor = a.getInt(R.styleable.CoursesView_titleColor, ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            subTitleColor = a.getInt(R.styleable.CoursesView_subTitleColor, ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

            nameColor = a.getInt(R.styleable.CoursesView_nameColor, ContextCompat.getColor(getContext(), R.color.colorAccent));
            valueColor = a.getInt(R.styleable.CoursesView_valueColor, ContextCompat.getColor(getContext(), R.color.colorPrimary));
            canvasColor = a.getInt(R.styleable.CoursesView_canvasColor, ContextCompat.getColor(getContext(), R.color.colorMenuBar));

            titleSize = a.getDimensionPixelSize(R.styleable.CoursesView_titleSize, 20);
            subTitleSize = a.getDimensionPixelSize(R.styleable.CoursesView_subTitleSize, 20);
            nameSize = a.getDimensionPixelSize(R.styleable.CoursesView_nameSize, 20);
            valueSize = a.getDimensionPixelSize(R.styleable.CoursesView_valueSize, 20);

            titleStyle = a.getInt(R.styleable.CoursesView_titleStyle, Typeface.NORMAL);
            subTitleStyle = a.getInt(R.styleable.CoursesView_subTitleStyle, Typeface.NORMAL);
            nameStyle = a.getInt(R.styleable.CoursesView_nameStyle, Typeface.NORMAL);
            valueStyle = a.getInt(R.styleable.CoursesView_valueStyle, Typeface.NORMAL);


        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }

        setWillNotDraw(false);

        paintForeground = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintForeground.setStyle(Paint.Style.FILL);
        paintForeground.setAntiAlias(true);
        paintForeground.setColor(canvasColor);

        final ViewTreeObserver observer = this.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                viewWidth = getWidth();
            }
        });


    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "________________onDraw*" + bank.getTitle() + "/ titleStyle = " + titleStyle + "/ titleSize = " + titleSize);

        canvas.drawColor(canvasColor);
        drawText(canvas, titleSize, titleStyle, titleColor, bank.getTitle(), true, LEFT_X_TITLE);
        drawText(canvas, subTitleSize, subTitleStyle, subTitleColor, bank.getRegion(), true, LEFT_X_TITLE);
        drawText(canvas, subTitleSize, subTitleStyle, subTitleColor, bank.getCity(), true, LEFT_X_TITLE);

        for (Currency course : courseList) {
            drawText(canvas, nameSize, nameStyle, nameColor, course.getCurrencyId(), true, LEFT_X_NAME);
            drawText(canvas, valueSize, valueStyle, valueColor,
                    (leadingZero + course.getAsk()).substring(course.getAsk().length())
                            + " / "
                            + ("0000000" + course.getAsk()).substring(course.getBid().length()),
                    false, LEFT_X_COURSE);
        }
        currentX = 0;
    }

    private void drawText(Canvas _canvas, float _textSize, int _textStyle, int _textColor, String _text, boolean _newLine, float _y) {

        if (_newLine) currentX = currentX + _textSize + _textSize / 2;
        paintForeground.setTextSize(_textSize);
        paintForeground.setTypeface(Typeface.defaultFromStyle(_textStyle));
        paintForeground.setColor(_textColor);

        _canvas.drawText(_text, _y, currentX, paintForeground);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (viewWidth != 0) {
            int height = (int) (((courseList == null) ? 3 : (courseList.size()) * valueSize + titleSize * 2 + subTitleSize) * 1.5 + 60);
            setMeasuredDimension(viewWidth, height);

        } else {
            //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // for visual design in IDE
            setMeasuredDimension(1000, 1000);
        }

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d(TAG, "_______onSizeChanged w = " + w + " / h = " + h + " / oldw = " + oldw + " / oldh = " + oldh);
        super.onSizeChanged(w, h, oldw, oldh);
//        viewHeight = h;
        viewWidth = w;

        // for visual design in IDE
        if (this.bank == null) {
            this.bank = new OrganizationRV("1", "Title", "Region", "City", "", "", "", "", "");

            ArrayList<Currency> currencies = new ArrayList<>();
            Currency currency = new Currency();

            currency.setCurrencyId("USD");
            currency.setAsk("123.4567");
            currency.setBid("234.5678");
            currencies.add(currency);

            Currency currency1 = new Currency();
            currency1.setCurrencyId("EUR");
            currency1.setAsk("34.4567");
            currency1.setBid("67.5678");
            currencies.add(currency1);

            Currency currency2 = new Currency();
            currency2.setCurrencyId("RUB");
            currency2.setAsk("1.4567");
            currency2.setBid("2.5678");
            currencies.add(currency2);
            this.courseList = currencies;
        }

    }


    public void setData(OrganizationRV _organisation, ArrayList<Currency> _courses) {
        Log.d(TAG, "_________organisation = " + _organisation.toString() + "/_courses.size = " + _courses.size());
        this.bank = _organisation;
        this.courseList = _courses;

        int textLength = 0;
        for (Currency course : courseList) {
            int tmpLength = (course.getAsk().length() > course.getBid().length())
                    ? course.getAsk().length() : course.getBid().length();
            textLength = (tmpLength > textLength)?tmpLength:textLength;
        }

        leadingZero =  (leadingZero).substring(leadingZero.length()-textLength);
        requestLayout();
        invalidate();
    }


    public Bitmap saveSignature(){

        Bitmap  bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        return bitmap;
    }

}
