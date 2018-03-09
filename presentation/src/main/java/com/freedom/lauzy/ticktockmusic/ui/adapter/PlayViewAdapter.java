package com.freedom.lauzy.ticktockmusic.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.freedom.lauzy.ticktockmusic.R;
import com.freedom.lauzy.ticktockmusic.model.SongEntity;
import com.freedom.lauzy.ticktockmusic.utils.ThemeHelper;
import com.lauzy.freedom.librarys.imageload.ImageConfig;
import com.lauzy.freedom.librarys.imageload.ImageLoader;
import com.lauzy.freedom.librarys.view.util.PaletteColor;
import com.lauzy.freedom.librarys.view.util.ScrimUtil;

import java.util.List;

/**
 * Desc : PlayViewAdapter
 * Author : Lauzy
 * Date : 2018/2/5
 * Blog : http://www.jianshu.com/u/e76853f863a9
 * Email : freedompaladin@gmail.com
 */
public class PlayViewAdapter extends PagerAdapter {

    private List<SongEntity> mSongEntities;
    private SparseIntArray mColorArr = new SparseIntArray();

    public PlayViewAdapter(List<SongEntity> songEntities) {
        mSongEntities = songEntities;
    }

    public SparseIntArray getColorArr() {
        return mColorArr;
    }

    @Override
    public int getCount() {
        return mSongEntities.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Context context = container.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_play_view, container, false);
        ImageView ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.fl_play);
        ImageLoader.INSTANCE.display(context, new ImageConfig.Builder()
                .asBitmap(true)
                .url(mSongEntities.get(position).albumCover)
                .cacheStrategy(ImageConfig.CACHE_NONE)
                .skipMemoryCache(true)
                .isRound(false)
                .placeholder(R.drawable.ic_default)
                .intoTarget(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        PaletteColor.mainColorObservable(ThemeHelper.getThemeColorResId(context), resource)
                                .subscribe(integer -> {
                                    mColorArr.put(position, integer);
                                    //过渡效果比 LinearGradient 好
                                    Drawable drawable = ScrimUtil.makeCubicGradientScrimDrawable(integer, 8, Gravity.BOTTOM);
                                    frameLayout.setForeground(drawable);
//                                    Bitmap bitmap = GradientBitmap.getGradientBitmap(resource, integer);
                                    ivPlay.setImageBitmap(resource);
                                });
                    }
                })
                .build());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}