package com.android.www.bannerviewpaper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.www.bannerviewpaper.view.BannerViewPaper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BannerViewPaper bannerViewPaper;

    private List<Integer> imageList = new ArrayList<>();
    private List<View> viewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bannerViewPaper = (BannerViewPaper) findViewById(R.id.view_paper);

        imageList.add(R.mipmap.img1);
        imageList.add(R.mipmap.img2);
        imageList.add(R.mipmap.img3);
        imageList.add(R.mipmap.img4);
        imageList.add(R.mipmap.img5);



        fillViewList();
        bannerViewPaper.setBannerAdapter(new BannerViewPaper.BannerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public View getView(int position) {
                return viewList.get(position);
            }

            @Override
            public boolean isEmpty() {
                return viewList.isEmpty();
            }
        });

        bannerViewPaper.setItemClickListener(new BannerViewPaper.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,"position ==" + position,Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void fillViewList(){
        for (int resId : imageList){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(resId);
            viewList.add(imageView);
        }
    }
}
