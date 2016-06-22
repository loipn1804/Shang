package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.FollowingDetailActivity;
import digimatic.shangcommerce.activity.ListProductActivity;
import digimatic.shangcommerce.callback.ItemClickCallback;
import digimatic.shangcommerce.model.AllFollowing;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 4/6/2016.
 */
public class AllFollowingAdapter extends RecyclerView.Adapter<AllFollowingAdapter.SimpleViewHolder> {

    private Activity activity;
    private List<AllFollowing> listData;
    private int size;
    private int margin;
    private int margin_x2;
    private int item_height;
    private Font font;

    private boolean isEdit;
    private ItemClickCallback itemClickCallback;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public AllFollowingAdapter(Activity activity, List<AllFollowing> listData, boolean isEdit, ItemClickCallback itemClickCallback) {
        this.activity = activity;
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.isEdit = isEdit;
        this.itemClickCallback = itemClickCallback;

        size = listData.size();
        margin = activity.getResources().getDimensionPixelOffset(R.dimen.dm_1dp);
        margin_x2 = margin * 2;
        item_height = StaticFunction.getScreenWidth(activity) / 2 - margin;

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();

        this.font = new Font(activity);
    }

    public void setListData(List<AllFollowing> listData, boolean isEdit) {
        this.listData.clear();
        this.listData.addAll(listData);
        this.isEdit = isEdit;

        size = listData.size();
        notifyDataSetChanged();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        private RelativeLayout rltCover;
        public TextView txtTitle;
        public LinearLayout lnlUnFollow;
        public TextView txtFollow;
        public ImageView imvFollow;

        public SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            rltCover = (RelativeLayout) view.findViewById(R.id.rltCover);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            lnlUnFollow = (LinearLayout) view.findViewById(R.id.lnlUnFollow);
            txtFollow = (TextView) view.findViewById(R.id.txtFollow);
            imvFollow = (ImageView) view.findViewById(R.id.imvFollow);
        }
    }

    @Override
    public AllFollowingAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_following, viewGroup, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllFollowingAdapter.SimpleViewHolder holder, final int position) {
        font.overrideFontsBold(holder.txtTitle);
        font.overrideFontsLight(holder.txtFollow);

        if (listData.get(position).isCategory()) {
            holder.txtTitle.setText(listData.get(position).getName_category());
            imageLoader.displayImage(listData.get(position).getThumbnail_url(), holder.imageView, options);
        } else {
            holder.txtTitle.setText(listData.get(position).getName_brand());
            imageLoader.displayImage(listData.get(position).getThumbnail_url(), holder.imageView, options);
        }

        if (isEdit) {
            if (listData.get(position).isFollow()) {
                holder.txtFollow.setText("Unfollow");
                holder.lnlUnFollow.setBackgroundResource(R.drawable.corner_large_red);
                holder.imvFollow.setImageResource(R.drawable.ic_delete_red);
            } else {
                holder.txtFollow.setText("Follow");
                holder.lnlUnFollow.setBackgroundResource(R.drawable.corner_large_green);
                holder.imvFollow.setImageResource(R.drawable.ic_check_green);
            }
            holder.lnlUnFollow.setVisibility(View.VISIBLE);
        } else {
            holder.lnlUnFollow.setVisibility(View.INVISIBLE);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.rltCover.getLayoutParams();

        if (position % 2 == 0) {
            // left
            params.leftMargin = 0;
            params.rightMargin = margin;
        } else {
            // right
            params.leftMargin = margin;
            params.rightMargin = 0;
        }

        if (size - 1 == position) {
            params.bottomMargin = margin_x2;
        } else {
            params.bottomMargin = 0;
        }
        params.topMargin = margin_x2;
        params.height = item_height;
        params.width = item_height;
        holder.rltCover.setLayoutParams(params);

        holder.rltCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    itemClickCallback.onItemLick(position);
                } else {
                    if (listData.get(position).isCategory()) {
                        Intent intent = new Intent(activity, ListProductActivity.class);
                        intent.putExtra("category_id", listData.get(position).getEntity_id());
                        intent.putExtra("title", listData.get(position).getName_category());
                        activity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(activity, FollowingDetailActivity.class);
                        intent.putExtra("brand_id", listData.get(position).getBrand_id());
                        intent.putExtra("title", listData.get(position).getName_brand());
                        activity.startActivity(intent);
                    }
                }
            }
        });

//        holder.lnlUnFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemClickCallback.onItemLick(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
