package com.android.www.bannerviewpaper.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.www.bannerviewpaper.R;

public class BannerViewPaper extends FrameLayout implements ViewPager.OnPageChangeListener{

    /**
     * 所有的总数   为了无限滑动
     */
    private int totalCount = Integer.MAX_VALUE;

    /**
     * 显示的数目
     */
    private int showCount;

    /**
     * 当前显示为位置
     */
    private int currentPosition = 0;

    /**
     * 点的宽度
     */
    private int indicatorWidth;

    /**
     * 轮播间隔时间
     */
    private int intervalTime = 5000;

    private boolean isUserTouched = false;

    private int WHEEL = 100;

    private ViewPager viewPager;
    private LinearLayout indicatorLayout;

    private Context mContext;

    private BannerAdapter adapter;
    private OnItemClickListener itemClickListener;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == WHEEL && !isUserTouched){

                viewPager.setCurrentItem(currentPosition + 1,true);
            }
            handler.sendEmptyMessageDelayed(WHEEL,intervalTime);
        }
    };


    public BannerViewPaper(Context context) {
        super(context);
        this.mContext = context;

    }

    public BannerViewPaper(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public BannerViewPaper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View view = View.inflate(mContext, R.layout.banner_layout,this);

        viewPager = (ViewPager) view.findViewById(R.id.banner_view_paper);
        indicatorLayout = (LinearLayout) view.findViewById(R.id.banner_indicator_layout);
        indicatorWidth = 10;
        this.viewPager.addOnPageChangeListener(this);
//        this.viewPager.setOnTouchListener(this);

        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isUserTouched = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isUserTouched = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isUserTouched = false;
                        break;
                }
                return false;
            }
        });
    }

    public interface BannerAdapter{
        public int getCount();
        public View getView(int position);
        public boolean isEmpty();
    }

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    private void init(){
        viewPager.setAdapter(null);
        if (adapter == null){
            return;
        }
        int count = adapter.getCount();
        showCount = adapter.getCount();

        for (int i = 0; i < count;i++){
            View view = new View(mContext);
            //indicator
            if (currentPosition == i){
                view.setPressed(true);
                view.setSelected(true);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorWidth,indicatorWidth);
                params.setMargins(indicatorWidth,0,0,0);
                view.setLayoutParams(params);
            }else{
                view.setPressed(false);
                view.setSelected(false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorWidth,indicatorWidth);
                params.setMargins(indicatorWidth,0,0,0);
                view.setLayoutParams(params);
            }
            view.setBackgroundResource(R.drawable.indicator_layout_page);
            indicatorLayout.addView(view);
        }

        viewPager.setAdapter(new ViewPaperAdapter());
        viewPager.setCurrentItem(showCount * 100);

        handler.sendEmptyMessageDelayed(WHEEL,intervalTime);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        int count = indicatorLayout.getChildCount();
        for (int i = 0;i<count;i++){
            View view = indicatorLayout.getChildAt(i);
            if(position%showCount==i){
                view.setSelected(true);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorWidth,indicatorWidth);
                params.setMargins(indicatorWidth, 0, 0, 0);
                view.setLayoutParams(params);
            }else {
                view.setSelected(false);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(indicatorWidth,indicatorWidth );
                params.setMargins(indicatorWidth, 0, 0, 0);
                view.setLayoutParams(params);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class ViewPaperAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return totalCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= showCount;
            final View view = adapter.getView(position);
            container.addView(view);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null){
                        itemClickListener.onItemClick(view,currentPosition%showCount);
                    }

                }
            });
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

    }

    public void setBannerAdapter(BannerAdapter adapter){
        this.adapter = adapter;
        if (adapter != null){
            init();
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
