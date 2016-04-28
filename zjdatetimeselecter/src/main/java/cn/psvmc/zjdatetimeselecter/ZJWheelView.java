package cn.psvmc.zjdatetimeselecter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PSVMC on 16/4/27.
 */
public class ZJWheelView extends ScrollView {

    List<ZJWheelItem> itemsOriginal;
    List<ZJWheelItem> itemsNew;


    int offset = 2; // 偏移量（需要在最前面和最后面补全）

    int selectedIndex = offset;//选中的项的索引

    private int displayItemCount = offset * 2 + 1; // 每页显示的数量

    int itemHeight = 0;//行高

    public static class OnWheelViewListener {
        public void onSelected(int selectedIndex, ZJWheelItem item) {
        }
    }


    private Context context;

    private LinearLayout views;

    public ZJWheelView(Context context) {
        super(context);
        init(context);
    }

    public ZJWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZJWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    public List<ZJWheelItem> getItemsOriginal() {
        return itemsOriginal;
    }

    public List<ZJWheelItem> getItemsNew() {
        return itemsNew;
    }

    public void setItemsNew(List<ZJWheelItem> list) {
        if (null == itemsNew) {
            itemsOriginal = new ArrayList<>();
            itemsNew = new ArrayList<>();
        }

        itemsOriginal.clear();
        itemsOriginal.addAll(list);

        itemsNew.clear();
        itemsNew.addAll(list);

        // 前面和后面补全
        for (int i = 0; i < offset; i++) {
            itemsNew.add(0, new ZJWheelItem("", ""));
            itemsNew.add(new ZJWheelItem("", ""));
        }

        initData();
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    private void init(Context context) {
        this.context = context;
        this.setVerticalScrollBarEnabled(false);

        views = new LinearLayout(context);
        views.setOrientation(LinearLayout.VERTICAL);
        this.addView(views);

        scrollerTask = new Runnable() {

            public void run() {
                int newY = getScrollY();
                if (initialY - newY == 0) {
                    final int remainder = initialY % itemHeight;
                    final int divided = initialY / itemHeight;
                    if (remainder == 0) {
                        selectedIndex = divided + offset;

                        onSeletedCallBack();
                    } else {
                        if (remainder > itemHeight / 2) {
                            ZJWheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    selectedIndex = divided + offset + 1;
                                    onSeletedCallBack();
                                    ZJWheelView.this.smoothScrollTo(0, initialY - remainder + itemHeight);
                                }
                            });
                        } else {
                            ZJWheelView.this.post(new Runnable() {
                                @Override
                                public void run() {
                                    ZJWheelView.this.smoothScrollTo(0, initialY - remainder);
                                    selectedIndex = divided + offset;
                                    onSeletedCallBack();
                                }
                            });
                        }

                    }

                } else {
                    initialY = getScrollY();
                    ZJWheelView.this.postDelayed(scrollerTask, newCheck);
                }
            }
        };
    }

    private int initialY;

    Runnable scrollerTask;
    int newCheck = 50;

    public void startScrollerTask() {

        initialY = getScrollY();
        this.postDelayed(scrollerTask, newCheck);
    }

    private void initData() {
        displayItemCount = offset * 2 + 1;
        views.removeAllViews();
        for (ZJWheelItem item : itemsNew) {
            views.addView(createView(item.text));
        }

        //第一次初始化
        if (selectedIndex <= offset) {
            selectedIndex = offset;
        } else if (selectedIndex > (itemsNew.size() - offset - 1)) {
            selectedIndex = itemsNew.size() - offset - 1;
        }
        refreshTextColor();
    }


    private TextView createView(String item) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setSingleLine(true);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setText(item);
        tv.setGravity(Gravity.CENTER);
        int padding = dip2px(10);
        tv.setPadding(padding, padding, padding, padding);
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv);
            views.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight * displayItemCount));
            this.getLayoutParams().height = itemHeight * displayItemCount;

        }
        return tv;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        refreshItemView(t);

        if (t > oldt) {
            scrollDirection = SCROLL_DIRECTION_DOWN;
        } else {
            scrollDirection = SCROLL_DIRECTION_UP;

        }
    }

    private void refreshItemView(int y) {
        if (y > itemHeight * itemsNew.size()) {
            return;
        }
        int position = y / itemHeight + offset;
        int remainder = y % itemHeight;
        int divided = y / itemHeight;

        if (remainder != 0) {
            if (remainder > itemHeight / 1.5) {
                position = divided + offset + 1;
            }
        }

        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (position == i) {
                itemView.setTextColor(Color.parseColor("#0288ce"));
            } else {
                itemView.setTextColor(Color.parseColor("#bbbbbb"));
            }
        }
    }

    /**
     * 获取选中区域的边界
     */
    int[] selectedAreaBorder;

    private int[] obtainSelectedAreaBorder() {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = new int[2];
            selectedAreaBorder[0] = itemHeight * offset;
            selectedAreaBorder[1] = itemHeight * (offset + 1);
        }
        return selectedAreaBorder;
    }


    private int scrollDirection = -1;
    private static final int SCROLL_DIRECTION_UP = 0;
    private static final int SCROLL_DIRECTION_DOWN = 1;

    Paint paint;
    int viewWidth;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void setBackground(Drawable background) {

        if (viewWidth == 0) {
            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        }

        if (null == paint) {
            paint = new Paint();
            paint.setColor(Color.parseColor("#83cde6"));
            paint.setStrokeWidth(dip2px(1.5f));
        }

        background = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[0], viewWidth * 5 / 6, obtainSelectedAreaBorder()[0], paint);
                canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[1], viewWidth * 5 / 6, obtainSelectedAreaBorder()[1], paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };


        super.setBackground(background);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        setBackground(null);
    }

    /**
     * 选中回调
     */
    private void onSeletedCallBack() {
        if (null != onWheelViewListener) {
            onWheelViewListener.onSelected(selectedIndex-offset, itemsNew.get(selectedIndex));
        }
        refreshTextColor();
    }

    /**
     * 刷新选中项的颜色
     */
    private void refreshTextColor() {
        int childSize = views.getChildCount();
        for (int i = 0; i < childSize; i++) {
            TextView itemView = (TextView) views.getChildAt(i);
            if (null == itemView) {
                return;
            }
            if (selectedIndex == i) {
                itemView.setTextColor(Color.parseColor("#0288ce"));
            } else {
                itemView.setTextColor(Color.parseColor("#bbbbbb"));
            }
        }
    }

    /**
     * 设置选中项的索引
     *
     * @param position
     * @param animation
     */
    private void setSelectIndexFromNewList(int position, boolean animation) {
        final int p = position;
        final boolean anim = animation;
        if (selectedIndex != p) {
            selectedIndex = p;

            this.post(new Runnable() {
                @Override
                public void run() {
                    if (anim) {
                        ZJWheelView.this.smoothScrollTo(0, (p - offset) * itemHeight);
                    } else {
                        ZJWheelView.this.scrollTo(0, (p - offset) * itemHeight);
                    }
                }
            });
        }

        refreshTextColor();
    }

    /**
     * 设置选中项的索引
     *
     * @param position
     * @param animation
     */
    public void setSelectIndex(int position, boolean animation) {
        position += offset;
        setSelectIndexFromNewList(position,animation);

    }

    /**
     * 设置选中的值
     *
     * @param value
     * @param animation
     */
    public void setSelectValue(String value, boolean animation) {
        int position = getIndexFromValue(value);
        if (position == -1) {
            return;
        }
        setSelectIndexFromNewList(position, animation);
    }

    /**
     * 设置选中的文本
     *
     * @param text
     * @param animation
     */
    public void setSelectText(String text, boolean animation) {
        int position = getIndexFromText(text);
        if (position == -1) {
            return;
        }
        setSelectIndexFromNewList(position, animation);
    }

    private int getIndexFromText(String text) {
        if (this.itemsNew != null) {
            for (int i = 0; i < this.itemsNew.size(); i++) {
                if (this.itemsNew.get(i).text.equals(text)) {
                    return i;
                }
            }

        }
        return -1;
    }

    private int getIndexFromValue(String value) {
        if (this.itemsNew != null) {
            for (int i = 0; i < this.itemsNew.size(); i++) {
                if (this.itemsNew.get(i).value.equals(value)) {
                    return i;
                }
            }

        }
        return -1;
    }


    public ZJWheelItem getSeletedItem() {
        if (selectedIndex <= offset) {
            selectedIndex = offset;
        } else if (selectedIndex > (itemsNew.size() - offset - 1)) {
            selectedIndex = itemsNew.size() - offset - 1;
        }
        return itemsNew.get(selectedIndex);
    }

    public int getSeletedIndex() {
        return selectedIndex - offset;
    }


    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            startScrollerTask();
        }
        return super.onTouchEvent(ev);
    }

    private OnWheelViewListener onWheelViewListener;

    public OnWheelViewListener getOnWheelViewListener() {
        return onWheelViewListener;
    }

    public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
        this.onWheelViewListener = onWheelViewListener;
    }

    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getViewMeasuredHeight(View view) {
        int width = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
        return view.getMeasuredHeight();
    }

}
