package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.BaseActivity;
import digimatic.shangcommerce.callback.CartItemCallback;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.CartItem;

/**
 * Created by USER on 3/7/2016.
 */
public class CartAdapter extends BaseSwipListAdapter {

    private List<CartItem> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private CartItemCallback cartItemCallback;
    private Font font;
    private Handler handler;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private int focus = -1;

    public CartAdapter(Activity activity, List<CartItem> listData, CartItemCallback cartItemCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.cartItemCallback = cartItemCallback;
        this.font = new Font(activity);
        handler = new Handler();

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
            convertView = this.layoutInflater.inflate(R.layout.item_cart, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.imvSubQuantity = (ImageView) convertView.findViewById(R.id.imvSubQuantity);
            holder.imvAddQuantity = (ImageView) convertView.findViewById(R.id.imvAddQuantity);
            holder.imvProduct = (ImageView) convertView.findViewById(R.id.imvProduct);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtBrand = (TextView) convertView.findViewById(R.id.txtBrand);
            holder.txtTotalPrice = (TextView) convertView.findViewById(R.id.txtTotalPrice);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            holder.txtQuantity = (EditText) convertView.findViewById(R.id.txtQuantity);
            holder.lnlListOption = (LinearLayout) convertView.findViewById(R.id.lnlListOption);

            font.overrideFontsBold(holder.txtName);
            font.overrideFontsBold(holder.txtTotalPrice);
            font.overrideFontsBold(holder.txtPrice);
            font.overrideFontsLight(holder.txtBrand);
            font.overrideFontsLight(holder.txtQuantity);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imvSubQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemCallback.subQuantity(position);
            }
        });

        holder.imvAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemCallback.addQuantity(position);
            }
        });

        holder.txtQuantity.setId(position);

        holder.txtQuantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        String qty_str = view.getText().toString().trim();
                        if (qty_str.length() == 0) {
                            ((BaseActivity) activity).showToastError(activity.getString(R.string.null_quantity));
                            notifyDataSetChanged();
                        } else if (qty_str.length() > 5) {
                            ((BaseActivity) activity).showToastError(activity.getString(R.string.long_quantity));
                            notifyDataSetChanged();
                        } else {
//                            ((BaseActivity) activity).showToastInfo(view.getText().toString().trim() + " " + view.getId());
                            int qty = Integer.parseInt(qty_str);
                            cartItemCallback.updateQuantity(qty, view.getId());
                        }
                        break;
                }
                return false;
            }
        });

        imageLoader.displayImage(listData.get(position).getImage_url(), holder.imvProduct, options);
        holder.txtName.setText(listData.get(position).getName());
        holder.txtQuantity.setText(listData.get(position).getQty() + "");
        BigDecimal b_total = new BigDecimal(listData.get(position).getRow_total()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtTotalPrice.setText("$" + StaticFunction.formatPrice(b_total));
        BigDecimal b_price = new BigDecimal(listData.get(position).getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtPrice.setText("$" + StaticFunction.formatPrice(b_price));

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
        ImageView imvSubQuantity;
        ImageView imvAddQuantity;
        ImageView imvProduct;
        TextView txtName;
        TextView txtBrand;
        TextView txtTotalPrice;
        TextView txtPrice;
        EditText txtQuantity;
        LinearLayout lnlListOption;
    }
}
