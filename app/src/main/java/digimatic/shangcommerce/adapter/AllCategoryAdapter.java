package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.ListProductActivity;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.Category;

/**
 * Created by USER on 3/4/2016.
 */
public class AllCategoryAdapter extends RecyclerView.Adapter<AllCategoryAdapter.SimpleViewHolder> {

    private Activity activity;
    private List<Category> listData;
    private int size;
    private int margin;
    private int margin_x2;
    private int item_height;
    private Font font;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public AllCategoryAdapter(Activity activity, List<Category> listData) {
        this.activity = activity;
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);

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

    public void setListData(List<Category> listData) {
        this.listData.clear();
        this.listData.addAll(listData);

        size = listData.size();
        notifyDataSetChanged();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        private RelativeLayout rltCover;
        public TextView txtTitle;
        public TextView txtNumber;

        public SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            rltCover = (RelativeLayout) view.findViewById(R.id.rltCover);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtNumber = (TextView) view.findViewById(R.id.txtNumber);
        }
    }

    @Override
    public AllCategoryAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_all_category, viewGroup, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllCategoryAdapter.SimpleViewHolder holder, final int position) {
        font.overrideFontsLight(holder.txtTitle);
        font.overrideFontsLight(holder.txtNumber);

        holder.txtTitle.setText(listData.get(position).getName());
        imageLoader.displayImage(listData.get(position).getThumbnail_url(), holder.imageView, options);
        int product_count = listData.get(position).getProduct_count();
        if (product_count == 1) {
            holder.txtNumber.setText("1 item");
        } else {
            holder.txtNumber.setText(product_count + " items");
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
                Intent intent = new Intent(activity, ListProductActivity.class);
                intent.putExtra("category_id", listData.get(position).getEntity_id());
                intent.putExtra("title", listData.get(position).getName());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
