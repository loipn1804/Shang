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

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.callback.ItemClickCallback;
import digimatic.shangcommerce.model.Category;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 4/5/2016.
 */
public class CategoryAdapter extends BaseAdapter {

    private List<Category> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;
    private ItemClickCallback itemClickCallback;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public CategoryAdapter(Activity activity, List<Category> listData, ItemClickCallback itemClickCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.font = new Font(activity);
        this.itemClickCallback = itemClickCallback;

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();
    }

    public void setListData(List<Category> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_category, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.lnlFollowing = (LinearLayout) convertView.findViewById(R.id.lnlFollowing);
            holder.txtFollowing = (TextView) convertView.findViewById(R.id.txtFollowing);

            font.overrideFontsLight(holder.txtName);
            font.overrideFontsLight(holder.txtFollowing);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(listData.get(position).getImage_url(), holder.imageView, options);
        holder.txtName.setText(listData.get(position).getName());

        if (listData.get(position).isChosen()) {
            holder.lnlFollowing.setVisibility(View.VISIBLE);
        } else {
            holder.lnlFollowing.setVisibility(View.INVISIBLE);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickCallback.onItemLick(position);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        ImageView imageView;
        TextView txtName;
        LinearLayout lnlFollowing;
        TextView txtFollowing;
    }
}
