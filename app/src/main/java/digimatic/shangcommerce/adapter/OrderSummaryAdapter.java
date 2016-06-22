package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.CartItem;

/**
 * Created by USER on 3/17/2016.
 */
public class    OrderSummaryAdapter extends BaseAdapter {

    private List<CartItem> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public OrderSummaryAdapter(Activity activity, List<CartItem> listData) {
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

    public void setListData(List<CartItem> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_order_summary, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.imvProduct = (ImageView) convertView.findViewById(R.id.imvProduct);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtBrand = (TextView) convertView.findViewById(R.id.txtBrand);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtTotalPrice = (TextView) convertView.findViewById(R.id.txtTotalPrice);
            holder.lnlListOption = (LinearLayout) convertView.findViewById(R.id.lnlListOption);

            font.overrideFontsBold(holder.txtName);
            font.overrideFontsLight(holder.txtPrice);
            font.overrideFontsBold(holder.txtTotalPrice);
            font.overrideFontsLight(holder.txtBrand);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(listData.get(position).getImage_url(), holder.imvProduct, options);
        holder.txtName.setText(listData.get(position).getName());
        BigDecimal b_price = new BigDecimal(listData.get(position).getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtPrice.setText(listData.get(position).getQty() + " X $" + StaticFunction.formatPrice(b_price));
        BigDecimal b_total = new BigDecimal(listData.get(position).getRow_total()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtTotalPrice.setText("$" + StaticFunction.formatPrice(b_total));

        try {
            holder.lnlListOption.removeAllViews();
            JSONArray array = new JSONArray(listData.get(position).getOptions());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_cart_option, null);
                TextView txtLabel = (TextView) view.findViewById(R.id.txtLabel);
                TextView txtValue = (TextView) view.findViewById(R.id.txtValue);
                txtLabel.setTypeface(font.bold);
                txtValue.setTypeface(font.light);
                txtLabel.setText(object.getString("label") + " : ");
                txtValue.setText(object.getString("value"));
                holder.lnlListOption.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        ImageView imvProduct;
        TextView txtName;
        TextView txtBrand;
        TextView txtPrice;
        TextView txtTotalPrice;
        LinearLayout lnlListOption;
    }
}
