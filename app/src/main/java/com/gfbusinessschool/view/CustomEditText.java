package com.gfbusinessschool.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.gfbusinessschool.R;

/**
 * Created by lenovo on 2018/5/5.
 */

@SuppressLint("AppCompatCustomView")
public class CustomEditText extends EditText {
     private EditText edit;
     private Paint mPaint;//画笔
     private Rect mBound;//文本绘制的范围
     private int editWidth;//输入框的宽高
     private int editHeight;
     private int numLength = 100;
     private String numStr = "0/"+numLength;

     private int numSize = 18;//字数统计的大小
           public CustomEditText(Context context) {
                this(context,null);
           }

           public CustomEditText(Context context, AttributeSet attrs) {
                super(context,attrs);
                edit = this;
                //设置最多输入的字数
                edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(numLength)});
                mPaint = new Paint();
                int colorGray = getResources().getColor(R.color.color_a9a8a8);
                mPaint.setColor(colorGray);
                mPaint.setTextSize(numSize);
                mBound = new Rect();
                //初始化时候获取统计str的最小矩形的长宽。
                mPaint.getTextBounds(numLength+"/"+numLength, 0, (numLength+"/"+numLength).length(), mBound);

                //因为在绘制之前就会得到控件的宽高，即0和0，所以要强制在绘制完成之后再拿一次宽高才有正确数值
                ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                edit.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                editWidth = edit.getWidth();
                editHeight = edit.getHeight();
             }
             });
           }
        @Override
        protected void onDraw(Canvas canvas) {
               super.onDraw(canvas);
           //绘制字数统计

                canvas.drawText(numStr, editWidth-mBound.width()-10, editHeight-mBound.height()-5, mPaint);
            }
        @Override
        protected void onTextChanged(CharSequence text, int start,
            int lengthBefore, int lengthAfter) {
        //      super.onTextChanged(text, start, lengthBefore, lengthAfter);
                editHeight = getHeight();//目的在edit输入到第二行的时候刷新一下edit的高度重绘字数统计的高度
                int num = text.length();
                numStr = num+"/"+numLength;
                /*刷新该界面
                 * invalidate()是用来刷新View的，必须是在UI线程中进行工作。
                 * invalidate()得在UI线程中被调动，在工作者线程中可以通过Handler来通知UI线程进行界面更新。
                 *而postInvalidate()在工作者线程中被调用
                 * */
                postInvalidate();
                //重绘
                //requestLayout(); 区别在于绘制时候会不会重新测量位置即onMeaure，这个会重新归整位置
                invalidate();
            }
        public void setEditHint(String str){
                edit.setHint(str);
                //重绘
                requestLayout();
        }
}
