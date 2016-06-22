package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.callback.DeliveryItemCallback;
import digimatic.shangcommerce.model.DeliveryType;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 3/7/2016.
 */
public class DeliveryTypeAdapter extends BaseAdapter {

    private List<DeliveryType> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private DeliveryItemCallback deliveryItemCallback;
    private int maxPosition;
    private Font font;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public DeliveryTypeAdapter(Activity activity, List<DeliveryType> listData, DeliveryItemCallback deliveryItemCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.deliveryItemCallback = deliveryItemCallback;
        maxPosition = this.listData.size() - 1;
        this.font = new Font(activity);

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.color.transparent)
                .showImageOnLoading(R.color.transparent)
                .cacheOnDisk(true).build();
    }

    public void setListData(List<DeliveryType> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        maxPosition = this.listData.size() - 1;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = (listData == null) ? 0 : listData.size();

        return count;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.item_delivery_type, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.rltCover = (LinearLayout) convertView.findViewById(R.id.rltCover);
            holder.viewBottom = convertView.findViewById(R.id.viewBottom);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtFee = (TextView) convertView.findViewById(R.id.txtFee);
            holder.imvDelivery = (ImageView) convertView.findViewById(R.id.imvDelivery);

            font.overrideFontsBold(holder.txtName);
            font.overrideFontsLight(holder.txtFee);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(listData.get(position).getLogo(), holder.imvDelivery, options);
        holder.txtName.setText(listData.get(position).getTitle());
        BigDecimal price = new BigDecimal(listData.get(position).getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtFee.setText("$" + StaticFunction.formatPrice(price));

        if (listData.get(position).isChosen) {
            holder.rltCover.setBackgroundColor(activity.getResources().getColor(R.color.main_color));
            holder.imvDelivery.setBackgroundColor(activity.getResources().getColor(R.color.main_color));
            holder.txtName.setTextColor(activity.getResources().getColor(R.color.main_color));
            holder.txtFee.setTextColor(activity.getResources().getColor(R.color.main_color));
        } else {
            holder.rltCover.setBackgroundColor(activity.getResources().getColor(R.color.txt_black_55));
            holder.imvDelivery.setBackgroundColor(activity.getResources().getColor(R.color.txt_black_55));
            holder.txtName.setTextColor(activity.getResources().getColor(R.color.txt_black_55));
            holder.txtFee.setTextColor(activity.getResources().getColor(R.color.txt_black_55));
        }

        holder.rltCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryItemCallback.onItemClick(position);
            }
        });

        if (position < maxPosition) {
            holder.viewBottom.setVisibility(View.GONE);
        } else {
            holder.viewBottom.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        LinearLayout rltCover;
        View viewBottom;
        TextView txtName;
        TextView txtFee;
        ImageView imvDelivery;
    }
}
