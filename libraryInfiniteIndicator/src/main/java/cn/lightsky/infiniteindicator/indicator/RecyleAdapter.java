package cn.lightsky.infiniteindicator.indicator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.lightsky.infiniteindicator.R;
import cn.lightsky.infiniteindicator.jakewharton.salvage.RecyclingPagerAdapter;
import cn.lightsky.infiniteindicator.loader.ImageLoader;
import cn.lightsky.infiniteindicator.page.OnPageClickListener;
import cn.lightsky.infiniteindicator.page.Page;

public class RecyleAdapter extends RecyclingPagerAdapter {

    public interface RecyleAdapterCallback {
        void clickOnItem(int position,Page page);
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private OnPageClickListener mOnPageClickListener;
    private List<Page> pages = new ArrayList<>();
    private boolean isLoop = true;
    private RecyleAdapterCallback callback;

    public RecyleAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setCallback(RecyleAdapterCallback callback) {
        this.callback = callback;
    }

    public RecyleAdapter(Context context, OnPageClickListener onPageClickListener, RecyleAdapterCallback callback) {
        mContext = context;
        mOnPageClickListener = onPageClickListener;
        mInflater = LayoutInflater.from(context);
        this.callback = callback;
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    public int getPosition(int position) {
        return isLoop ? position % getRealCount() : position;
    }

    @Override
    public int getCount() {
        return isLoop ? getRealCount() * 100 : getRealCount();
    }

    public int getRealCount() {
        return pages.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {

        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_slider_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        final Page page = pages.get(getPosition(position));
        if (page.isImage()) {
            holder.imvPlay.setVisibility(View.GONE);
        } else {
            holder.imvPlay.setVisibility(View.VISIBLE);
        }

//        holder.imvPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(page.getUrl_video()));
//                intent.setDataAndType(Uri.parse(page.getUrl_video()), "video/*");
//                mContext.startActivity(intent);
//            }
//        });

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.clickOnItem(getPosition(position),page);
                }
            }
        });

//        if (page.getOnPageClickListener() != null) {
//            holder.target.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    page.getOnPageClickListener().onPageClick(getPosition(position), page);
//                }
//            });
//        }

        mImageLoader.load(mContext, holder.target, page.getUrl());
        return convertView;
    }

    private static class ViewHolder {
        final RelativeLayout root;
        final ImageView target;
        final ImageView imvPlay;

        public ViewHolder(View view) {
            root = (RelativeLayout) view.findViewById(R.id.root);
            target = (ImageView) view.findViewById(R.id.slider_image);
            imvPlay = (ImageView) view.findViewById(R.id.imvPlay);
        }
    }

    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public void addPage(Page page) {
        pages.add(page);
        notifyDataSetChanged();
    }

    public void removePage(Page page) {
        if (pages.contains(page)) {
            pages.remove(page);
            notifyDataSetChanged();
        }
    }

    /**
     * @return the is Loop
     */
    public boolean isLoop() {
        return isLoop;
    }

    /**
     * @param isLoop the is InfiniteLoop to set
     */
    public void setLoop(boolean isLoop) {
        this.isLoop = isLoop;
        notifyDataSetChanged();
    }

}
