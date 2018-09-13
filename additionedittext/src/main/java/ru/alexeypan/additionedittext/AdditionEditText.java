package ru.alexeypan.additionedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * EditText with suffix as independent text
 */
public class AdditionEditText extends AppCompatEditText {

  private String suffix = "";
  private SuffixGravity gravity = SuffixGravity.START;

  public AdditionEditText(Context context) {
    super(context);
  }

  public AdditionEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    getAttributes(context, attrs, 0);
  }

  public AdditionEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    getAttributes(context, attrs, defStyleAttr);
  }

  @Override
  public void onDraw(Canvas c) {
    super.onDraw(c);
    // https://stackoverflow.com/questions/23564633/custom-edittext-not-working-after-setting-its-input-type-through-setinputtype
    c.translate(getScrollX(), 0f);
    if (!TextUtils.isEmpty(getText())) {
      c.drawText(suffix, getSuffixXPosition(), getBaseline(), getPaint());
    }
  }

  public void setGravity(SuffixGravity gravity) {
    this.gravity = gravity;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  private void getAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
    TypedArray a = context.obtainStyledAttributes(
      attrs, R.styleable.AdditionEditText, defStyleAttr, 0
    );
    if (a != null) {
      if (a.hasValue(R.styleable.AdditionEditText_suffix)) {
        suffix = a.getString(R.styleable.AdditionEditText_suffix);
      }
      if (a.hasValue(R.styleable.AdditionEditText_suffix_gravity)) {
        gravity = SuffixGravity.fromId(a.getInt(R.styleable.AdditionEditText_suffix_gravity, 0));
      }
    }
    a.recycle();
  }

  private float getSuffixXPosition() {
    float suffixXPosition = 0;
    switch (gravity) {
      case START:
        float suffixPaddingLeft = getPaint().measureText(suffix);
        setPadding((int) suffixPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        break;
      case CENTER:
        suffixXPosition = getPaint().measureText(getText().toString()) / 2 + getWidth() / 2;
        break;
      case END:
        float suffixPaddingRight = getPaint().measureText(suffix);
        suffixXPosition = getWidth() - suffixPaddingRight;
        setPadding(getPaddingLeft(), getPaddingTop(), (int) suffixPaddingRight, getPaddingBottom());
        break;
    }
    return suffixXPosition;
  }

  enum SuffixGravity {
    START(0), CENTER(1), END(2);

    final int id;

    SuffixGravity(int id) {
      this.id = id;
    }

    static SuffixGravity fromId(int id) {
      for (SuffixGravity gravity : values()) {
        if (gravity.id == id) return gravity;
      }
      throw new IllegalArgumentException();
    }
  }
}
