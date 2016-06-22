package digimatic.shangcommerce.infiniteindicator;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import digimatic.shangcommerce.R;

/**
 * Created by lightsky on 16/1/28.
 */

public class UILoader implements cn.lightsky.infiniteindicator.loader.ImageLoader {

    private DisplayImageOptions options;

    private boolean isInited;

    public UILoader getImageLoader(Context context) {
        UILoader uilLoader = new UILoader();
        initLoader(context);
        return uilLoader;
    }

    @Override
    public void initLoader(Context context) {
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));

        //显示图片的配置
        options = new DisplayImageOptions.Builder().cacheInMemory(false)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();

        isInited = true;
    }

    @Override
    public void load(Context context, ImageView targetView, Object res) {

        if (!isInited)
            initLoader(context);

        if (res == null) {
            return;
        }

        if (res instanceof String) {
            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage((String) res, targetView, options);
        }
    }
}
