package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.model.VoucherItem;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 05/05/2016.
 */
public class MyRedemptionEVoucherAdapter extends BaseAdapter {

    private List<VoucherItem> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;

    private int margin;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public MyRedemptionEVoucherAdapter(Activity activity, List<VoucherItem> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.font = new Font(activity);

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();

        margin = (int) activity.getResources().getDimension(R.dimen.dm_5dp);
    }

    public void setListData(List<VoucherItem> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_my_redemption_evoucher, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.rltRoot = (RelativeLayout) convertView.findViewById(R.id.rltRoot);
            holder.txtLabel = (TextView) convertView.findViewById(R.id.txtLabel);
            holder.txtEVoucher = (TextView) convertView.findViewById(R.id.txtEVoucher);

            font.overrideFontsLight(holder.txtLabel);
            font.overrideFontsBold(holder.txtEVoucher);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BigDecimal b_price = new BigDecimal(listData.get(position).getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtEVoucher.setText("$" + StaticFunction.formatPrice(b_price));
        holder.txtLabel.setText(listData.get(position).getVoucherName());

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.rltRoot.getLayoutParams();
        if (position == 0) {
            params.topMargin = margin;
            params.bottomMargin = 0;
        } else {
            params.topMargin = 0;
            params.bottomMargin = 0;
        }
        holder.rltRoot.setLayoutParams(params);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        RelativeLayout rltRoot;
        TextView txtLabel;
        TextView txtEVoucher;
    }
}
