package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import greendao.Product;

/**
 * Created by USER on 3/4/2016.
 */
public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.SimpleViewHolder> {

    private Activity activity;
    private List<Product> listData;
    private int size;
    private int margin;
    private int item_height;
    private Font font;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public ListProductAdapter(Activity activity, List<Product> listData) {
        this.activity = activity;
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);

        size = listData.size();
        margin = activity.getResources().getDimensionPixelOffset(R.dimen.dm_2dp);
        int total_padding = activity.getResources().getDimensionPixelOffset(R.dimen.dm_5dp) * 2;
        item_height = StaticFunction.getScreenWidth(activity) / 2 - margin * 3 - total_padding;

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();

        this.font = new Font(activity);
    }

    public void setListData(List<Product> listData) {
        this.listData.clear();
        this.listData.addAll(listData);

        size = listData.size();
        notifyDataSetChanged();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private RelativeLayout rltCover;
        private TextView txtName;
        private TextView txtOldPrice;
        private TextView txtNewPrice;
        private ImageView imvNew;
        private ImageView imvNewTop;
        private ImageView imvNewLeft;

        public SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            rltCover = (RelativeLayout) view.findViewById(R.id.rltCover);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtOldPrice = (TextView) view.findViewById(R.id.txtOldPrice);
            txtNewPrice = (TextView) view.findViewById(R.id.txtNewPrice);
            imvNew = (ImageView) view.findViewById(R.id.imvNew);
            imvNewTop = (ImageView) view.findViewById(R.id.imvNewTop);
            imvNewLeft = (ImageView) view.findViewById(R.id.imvNewLeft);
        }
    }

    @Override
    public ListProductAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_product, viewGroup, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListProductAdapter.SimpleViewHolder holder, final int position) {
        font.overrideFontsBold(holder.txtOldPrice);
        font.overrideFontsBold(holder.txtNewPrice);
        font.overrideFontsLight(holder.txtName);

        imageLoader.displayImage(listData.get(position).getSmall_image_url(), holder.imageView, options);
        holder.txtName.setText(listData.get(position).getName());
        if (listData.get(position).getSpecial_price() == 0) {
            holder.txtOldPrice.setVisibility(View.GONE);
            BigDecimal price = new BigDecimal(listData.get(position).getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
            holder.txtNewPrice.setText("$" + StaticFunction.formatPrice(price));
        } else {
            holder.txtOldPrice.setVisibility(View.VISIBLE);
            BigDecimal price = new BigDecimal(listData.get(position).getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP);
            holder.txtOldPrice.setText("$" + StaticFunction.formatPrice(price));
            BigDecimal special_price = new BigDecimal(listData.get(position).getSpecial_price()).setScale(2, BigDecimal.ROUND_HALF_UP);
            holder.txtNewPrice.setText("$" + StaticFunction.formatPrice(special_price));
        }
        if (listData.get(position).getIs_product_new() == 1) {
            holder.imvNew.setVisibility(View.VISIBLE);
            holder.imvNewTop.setVisibility(View.VISIBLE);
            holder.imvNewLeft.setVisibility(View.VISIBLE);
        } else {
            holder.imvNew.setVisibility(View.GONE);
            holder.imvNewTop.setVisibility(View.GONE);
            holder.imvNewLeft.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.rltCover.getLayoutParams();

        if (size - 1 == position) {
            params.topMargin = margin;
            params.bottomMargin = margin;
        } else {
            params.topMargin = margin;
            params.bottomMargin = 0;
        }
        holder.rltCover.setLayoutParams(params);

        RelativeLayout.LayoutParams params_imv = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
        params_imv.height = item_height;
        params_imv.width = item_height;
        holder.imageView.setLayoutParams(params_imv);

        holder.rltCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listData.get(position).getType_id().equalsIgnoreCase("simple")) {
                    Intent intent = new Intent(activity, ProductDetailSimpleActivity.class);
                    intent.putExtra("product_id", listData.get(position).getEntity_id());
                    activity.startActivity(intent);
                } else if (listData.get(position).getType_id().equalsIgnoreCase("configurable")) {
                    Intent intent = new Intent(activity, ProductDetailConfigurableActivity.class);
                    intent.putExtra("product_id", listData.get(position).getEntity_id());
                    activity.startActivity(intent);
                }
            }
        });

        holder.txtOldPrice.setPaintFlags(holder.txtOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
