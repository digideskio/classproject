/*
 * Copyright 2013 Adam Saturna
 *  Copyright 2013 Brian Trinh
 *  Copyright 2013 Ethan Mykytiuk
 *  Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cmput301.recipebot;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.integralblue.httpresponsecache.HttpResponseCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import roboguice.RoboGuice;

import java.io.File;
import java.io.IOException;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.FROYO;
import static com.cmput301.recipebot.util.LogUtils.makeLogTag;

/**
 * RecipeBot application
 */
public class RecipeBotApplication extends Application {

    private static final String LOGTAG = makeLogTag(RecipeBotApplication.class);

    /**
     * Create main application
     */
    public RecipeBotApplication() {
        // Disable http.keepAlive on Froyo and below
        if (SDK_INT <= FROYO)
            HttpRequest.keepAlive(false);
    }

    /**
     * Create main application
     *
     * @param context
     */
    public RecipeBotApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setApplicationInjector(this);

        buildHttpCache();
        buildImageCache();
    }

    /**
     * Build the {@link HttpResponseCache}.
     */
    private void buildHttpCache() {
        File httpCacheDir = new File(getCacheDir(), "http");
        long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
        try {
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.e(LOGTAG, "HTTP response cache installation failed:" + e);
        }
    }

    /**
     * Build the {@link UnlimitedDiscCache} for images.
     */
    private void buildImageCache() {
        File cacheDir = new File(getCacheDir(), "uil");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .showImageForEmptyUri(android.R.drawable.ic_menu_report_image)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCache(new UnlimitedDiscCache(cacheDir))
                .memoryCacheExtraOptions(250, 300)
                .build();

        ImageLoader.getInstance().init(config);
    }

    /**
     * Create main application
     *
     * @param instrumentation
     */
    public RecipeBotApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }

    /**
     * Sets the application injector. Using the {@link RoboGuice#newDefaultRoboModule} as well as a
     * custom binding module {@link RecipeBotModule} to set up your application module
     *
     * @param application
     * @return
     */
    public static Injector setApplicationInjector(Application application) {
        return RoboGuice.setBaseApplicationInjector(application, Stage.DEVELOPMENT, RoboGuice.newDefaultRoboModule
                (application), new RecipeBotModule());
    }
}
