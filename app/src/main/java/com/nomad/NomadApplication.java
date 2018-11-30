package com.nomad;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.facebook.FacebookSdk;
import com.nomad.util.AppCommons;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class NomadApplication extends Application {

    private static final int MAX_SIZE = 50 * 1024 * 1024;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
    }
    private void initImageLoader(Context context) {
        DisplayImageOptions  imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(MAX_SIZE))
                .defaultDisplayImageOptions(imageOptions)
                .diskCache(new UnlimitedDiskCache(new File(AppCommons.getAppDir(getApplicationContext()))))
                .build();
        ImageLoader.getInstance().init(config);
    }
}
