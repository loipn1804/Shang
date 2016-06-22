package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.model.RedeemHistoryItem;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 05/05/2016.
 */
public class MyRedemptionProductAdapter extends BaseAdapter {

    private List<RedeemHistoryItem> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public MyRedemptionProductAdapter(Activity activity, List<RedeemHistoryItem> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.font = new Font(activity);

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();
    }

    public void setListData(List<RedeemHistoryItem> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
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
            convertView = this.layoutInflater.inflate(R.layout.item_my_redemption_product, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.imvProduct = (ImageView) convertView.findViewById(R.id.imvProduct);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtPoint = (TextView) convertView.findViewById(R.id.txtPoint);

            font.overrideFontsBold(holder.txtName);
            font.overrideFontsBold(holder.txtPoint);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (listData.get(position).getRedeemType() == 1) {
            imageLoader.displayImage(listData.get(position).getItemImageURL(), holder.imvProduct, options);
        } else {
            imageLoader.displayImage("drawable://" + R.drawable.img_redemption, holder.imvProduct, options);
        }
        holder.txtName.setText(listData.get(position).getItemName());
        BigDecimal b_price = new BigDecimal(listData.get(position).getRewardDeduct()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtPoint.setText(StaticFunction.formatPrice(b_price) + " pts");

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        ImageView imvProduct;
        TextView txtName;
        TextView txtPoint;
    }
}
