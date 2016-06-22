package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.content.Intent;
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
import digimatic.shangcommerce.activity.ProductDetailConfigurableActivity;
import digimatic.shangcommerce.activity.ProductDetailSimpleActivity;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.Wishlist;

/**
 * Created by USER on 3/8/2016.
 */
public class WishlistAdapter extends BaseAdapter {

    private List<Wishlist> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public WishlistAdapter(Activity activity, List<Wishlist> listData) {
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

    public void setListData(List<Wishlist> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_wishlist, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.imvProduct = (ImageView) convertView.findViewById(R.id.imvProduct);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtBrand = (TextView) convertView.findViewById(R.id.txtBrand);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);

            font.overrideFontsBold(holder.txtName);
            font.overrideFontsBold(holder.txtPrice);
            font.overrideFontsLight(holder.txtBrand);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(listData.get(position).getImage_url(), holder.imvProduct, options);
        holder.txtName.setText(listData.get(position).getName());
        holder.txtBrand.setText(listData.get(position).getSku());
        BigDecimal b_price = new BigDecimal(listData.get(position).getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtPrice.setText("$" + StaticFunction.formatPrice(b_price));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listData.get(position).getType_id().equalsIgnoreCase("simple")) {
                    Intent intent = new Intent(activity, ProductDetailSimpleActivity.class);
                    intent.putExtra("product_id", listData.get(position).getProduct_id());
                    activity.startActivity(intent);
                } else if (listData.get(position).getType_id().equalsIgnoreCase("configurable")) {
                    Intent intent = new Intent(activity, ProductDetailConfigurableActivity.class);
                    intent.putExtra("product_id", listData.get(position).getProduct_id());
                    activity.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        ImageView imvProduct;
        TextView txtName;
        TextView txtBrand;
        TextView txtPrice;
    }
}
