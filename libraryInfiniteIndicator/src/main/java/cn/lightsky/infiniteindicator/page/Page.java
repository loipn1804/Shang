package cn.lightsky.infiniteindicator.page;

/**
 * Created by xushuai on 2014/12/25.
 */
public class Page {
    private String url;
    private boolean isImage;
    private String url_video;
    private OnPageClickListener onPageClickListener;

    public Page(String url, boolean isImage, String url_video, OnPageClickListener onPageClickListener) {
        this.url = url;
        this.isImage = isImage;
        this.url_video = url_video;
        this.onPageClickListener = onPageClickListener;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean isImage) {
        this.isImage = isImage;
    }

    public String getUrl_video() {
        return url_video;
    }

    public void setUrl_video(String url_video) {
        this.url_video = url_video;
    }

    public OnPageClickListener getOnPageClickListener() {
        return onPageClickListener;
    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }
}
