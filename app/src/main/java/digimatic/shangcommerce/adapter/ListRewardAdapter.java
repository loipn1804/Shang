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

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.CartActivity;
import digimatic.shangcommerce.activity.ProductDetailConfigurableActivity;
import digimatic.shangcommerce.activity.ProductDetailSimpleActivity;
import digimatic.shangcommerce.activity.RedeemDetailActivity;
import digimatic.shangcommerce.activity.RedemptionActivity;
import digimatic.shangcommerce.model.ItemListReward;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.Product;

/**
 * Created by USER on 4/4/2016.
 */
public class ListRewardAdapter extends RecyclerView.Adapter<ListRewardAdapter.SimpleViewHolder> {

    private Activity activity;
    private List<ItemListReward> listData;
    private int size;
    private int margin;
    private int item_height;
    private Font font;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public ListRewardAdapter(Activity activity, List<ItemListReward> listData) {
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

    public void setListData(List<ItemListReward> listData) {
        this.listData.clear();
        this.listData.addAll(listData);

        size = listData.size();
        notifyDataSetChanged();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private RelativeLayout rltCover;
        private TextView txtName;
        private TextView txtValue;

        public SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            rltCover = (RelativeLayout) view.findViewById(R.id.rltCover);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtValue = (TextView) view.findViewById(R.id.txtValue);
        }
    }

    @Override
    public ListRewardAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_reward, viewGroup, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListRewardAdapter.SimpleViewHolder holder, final int position) {
        font.overrideFontsBold(holder.txtValue);
        font.overrideFontsLight(holder.txtName);

        if (position == 0) {
            imageLoader.displayImage("drawable://" + R.drawable.img_redemption, holder.imageView, options);
            holder.txtName.setText("eVoucher Redemption");
            holder.txtValue.setText("");
        } else {
            imageLoader.displayImage(listData.get(position).getItemImageURL(), holder.imageView, options);
            holder.txtName.setText(listData.get(position).getItemName());
            holder.txtValue.setText(listData.get(position).getMinRewardtoDeduct() + " pts");
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
                if (position == 0) {
                    Intent intentRedemption = new Intent(activity, RedemptionActivity.class);
                    activity.startActivity(intentRedemption);
                } else {
                    JSONObject ItemList = new JSONObject();
                    try {
                        ItemList.put("RedeemRuleID", listData.get(position).getRedeemRuleID());
                        ItemList.put("ItemCode", listData.get(position).getItemCode());
                        ItemList.put("ItemName", listData.get(position).getItemName());
                        ItemList.put("ItemDescription", listData.get(position).getItemDescription());
                        ItemList.put("ItemURL", listData.get(position).getItemURL());
                        ItemList.put("ItemImageURL", listData.get(position).getItemImageURL());
                        ItemList.put("MinRewardtoDeduct", listData.get(position).getMinRewardtoDeduct());
                        ItemList.put("MaxRewardtoDeduct", listData.get(position).getMaxRewardtoDeduct());
                        ItemList.put("ActiveFrom", listData.get(position).getActiveFrom());
                        ItemList.put("ActiveTo", listData.get(position).getActiveTo());
                        ItemList.put("MaxRedeem", listData.get(position).getMaxRedeem());
                        Intent intent = new Intent(activity, RedeemDetailActivity.class);
                        intent.putExtra("ItemList", ItemList.toString());
                        activity.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
