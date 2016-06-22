package digimatic.shangcommerce.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by USER on 04/13/2016.
 */
public class Banner implements Parcelable{

    private String url;
    private boolean isImage;
    private String url_video;

    public Banner(String url, boolean isImage, String url_video) {
        this.url = url;
        this.isImage = isImage;
        this.url_video = url_video;
    }

    protected Banner(Parcel in) {
        url = in.readString();
        isImage = in.readByte() != 0;
        url_video = in.readString();
    }

    public static final Creator<Banner> CREATOR = new Creator<Banner>() {
        @Override
        public Banner createFromParcel(Parcel in) {
            return new Banner(in);
        }

        @Override
        public Banner[] newArray(int size) {
            return new Banner[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeByte((byte) (isImage ? 1 : 0));
        dest.writeString(url_video);
    }
}
