package com.nomad.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomad.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tompee.circularviewpager.CircularViewPager;

import java.util.ArrayList;
import java.util.List;

public class CoverImagesPager extends FrameLayout {
    private List<String> coverImages = new ArrayList<>();
    private boolean isZoomable=false;

    public CoverImagesPager(@NonNull Context context) {
        super(context);
        init();
    }

    public CoverImagesPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CoverImagesPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CoverImagesPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private ViewPager viewPager;
    private TextView coverImagesCounter;
    private CoverImagesPagerAdapter coverImagesPagerAdapter;

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cover_images_pager, this, false);
        addView(view);
        viewPager = view.findViewById(R.id.pager_view);
        coverImagesCounter = view.findViewById(R.id.cover_image_counter);
        coverImagesPagerAdapter = new CoverImagesPagerAdapter();
        viewPager.setAdapter(coverImagesPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*if (position == 1)
                    coverImagesCounter.setText(String.format("%d/%d", coverImagesPagerAdapter.getCount(), coverImagesPagerAdapter.getCount()));
                else if (position == coverImagesPagerAdapter.getCoverImageCount())
                    coverImagesCounter.setText(String.format("%d/%d", 1, coverImagesPagerAdapter.getCount()));
                else
                    coverImagesCounter.setText(String.format("%d/%d", position, coverImagesPagerAdapter.getCoverImageCount()));*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class CoverImagesPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return coverImages.size();
        }

        public int getCoverImageCount(){
            return coverImages.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View view = LayoutInflater.from(getContext()).inflate(
                    isZoomable ? R.layout.cover_images_zoom_pager_item :
                    R.layout.cover_images_pager_item, container, false);
            ImageView coverImage = view.findViewById(R.id.cover_image);
            coverImage.setTransitionName("cover_image_"+position);
            coverImage.setImageDrawable(null);
            ImageLoader.getInstance().displayImage(coverImages.get(position), coverImage);
            container.addView(view);
            coverImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(onCoverImageClickListener!=null)onCoverImageClickListener.onCoverImageClick(v,position);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    public boolean isZoomable() {
        return isZoomable;
    }

    public void setZoomable(boolean zoomable) {
        isZoomable = zoomable;
    }

    public void setCoverImages(List<String> coverImages) {

        this.coverImages.clear();
        this.coverImages.addAll(getCircularList(coverImages));
        viewPager.setAdapter(coverImagesPagerAdapter);
    }

    private List<String> getCircularList(List<String> coverImages) {
        List<String> circularList = new ArrayList<>();
        circularList.addAll(coverImages);
        circularList.add(0, coverImages.get(coverImages.size() - 1));
        circularList.add(coverImages.get(0));
        return circularList;

    }

    public void setCurrentItem(int position)
    {
        viewPager.setCurrentItem(position);
    }
    public int getCurrentItem()
    {
        return viewPager.getCurrentItem();
    }
    private OnCoverImageListener onCoverImageClickListener=null;

    public OnCoverImageListener getOnCoverImageClickListener() {
        return onCoverImageClickListener;
    }

    public void setOnCoverImageClickListener(OnCoverImageListener onCoverImageClickListener) {
        this.onCoverImageClickListener = onCoverImageClickListener;
    }

    public interface OnCoverImageListener
    {
         void onCoverImageClick(View view,int position);

    }

}
