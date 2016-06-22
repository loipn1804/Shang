package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
public class EVoucherAdapter extends BaseAdapter {

    public interface Callback {
        void check(int position);

        void unCheck(int position);
    }

    private List<VoucherItem> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;
    private Callback callback;

    private int margin;
    private int imv_margin;

    private int ic_check;
    private int ic_delete;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public EVoucherAdapter(Activity activity, List<VoucherItem> listData, Callback callback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.font = new Font(activity);
        this.callback = callback;

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();

        margin = (int) activity.getResources().getDimension(R.dimen.dm_5dp);
        int row_width = StaticFunction.getScreenWidth(activity) - margin * 2 - ((int) activity.getResources().getDimension(R.dimen.dm_20dp)) * 2;
        imv_margin = row_width * 35 / 180 - ((int) activity.getResources().getDimension(R.dimen.dm_30dp)) / 2;

        ic_check = R.drawable.ic_check;
        ic_delete = R.drawable.ic_delete_red;
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
            convertView = this.layoutInflater.inflate(R.layout.item_evoucher, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.rltRoot = (RelativeLayout) convertView.findViewById(R.id.rltRoot);
            holder.txtLabel = (TextView) convertView.findViewById(R.id.txtLabel);
            holder.txtEVoucher = (TextView) convertView.findViewById(R.id.txtEVoucher);
            holder.txtApplied = (TextView) convertView.findViewById(R.id.txtApplied);
            holder.imvCheck = (ImageView) convertView.findViewById(R.id.imvCheck);
            holder.txtValidDateFromLabel = (TextView) convertView.findViewById(R.id.txtValidDateFromLabel);
            holder.txtValidDateToLabel = (TextView) convertView.findViewById(R.id.txtValidDateToLabel);
            holder.txtValidDateFrom = (TextView) convertView.findViewById(R.id.txtValidDateFrom);
            holder.txtValidDateTo = (TextView) convertView.findViewById(R.id.txtValidDateTo);

            font.overrideFontsLight(holder.txtLabel);
            font.overrideFontsLight(holder.txtApplied);
            font.overrideFontsBold(holder.txtEVoucher);
            font.overrideFontsBold(holder.txtValidDateFromLabel);
            font.overrideFontsBold(holder.txtValidDateToLabel);
            font.overrideFontsBold(holder.txtValidDateFrom);
            font.overrideFontsBold(holder.txtValidDateTo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BigDecimal b_price = new BigDecimal(listData.get(position).getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtLabel.setText(listData.get(position).getVoucherName());
        holder.txtValidDateFrom.setText(StaticFunction.convertYYYYMMDDtoAppFormat(listData.get(position).getValidFrom()));
        holder.txtValidDateTo.setText(StaticFunction.convertYYYYMMDDtoAppFormat(listData.get(position).getValidTo()));

        if (!listData.get(position).isUsed()) {
            holder.txtEVoucher.setTextColor(activity.getResources().getColor(R.color.main_color));
            holder.txtEVoucher.setText("$" + StaticFunction.formatPrice(b_price));
            holder.txtApplied.setVisibility(View.GONE);
            if (listData.get(position).isCheck()) {
                holder.imvCheck.setImageResource(R.drawable.ic_check);
            } else {
                holder.imvCheck.setImageResource(R.drawable.ic_uncheck);
            }
        } else {
            holder.txtEVoucher.setTextColor(activity.getResources().getColor(R.color.txt_black_55));
            holder.txtEVoucher.setText("$" + StaticFunction.formatPrice(b_price));
            holder.imvCheck.setImageResource(ic_delete);
            holder.txtApplied.setVisibility(View.VISIBLE);
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.rltRoot.getLayoutParams();
        if (position == 0) {
            params.topMargin = margin;
            params.bottomMargin = 0;
        } else {
            params.topMargin = 0;
            params.bottomMargin = 0;
        }
        holder.rltRoot.setLayoutParams(params);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.imvCheck.getLayoutParams();
        layoutParams.rightMargin = imv_margin;
        holder.imvCheck.setLayoutParams(layoutParams);

        holder.imvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listData.get(position).isUsed()) {
                    callback.check(position);
                } else {
                    callback.unCheck(position);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        RelativeLayout rltRoot;
        TextView txtLabel;
        TextView txtEVoucher;
        TextView txtApplied;
        ImageView imvCheck;
        TextView txtValidDateFromLabel;
        TextView txtValidDateToLabel;
        TextView txtValidDateFrom;
        TextView txtValidDateTo;
    }
}
