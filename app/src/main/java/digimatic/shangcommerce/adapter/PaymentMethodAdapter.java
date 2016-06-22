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

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.callback.PaymentMethodCallback;
import digimatic.shangcommerce.model.PaymentMethod;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by user on 3/16/2016.
 */
public class PaymentMethodAdapter extends BaseAdapter {

    private List<PaymentMethod> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private PaymentMethodCallback paymentMethodCallback;
    private int maxPosition;
    private Font font;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public PaymentMethodAdapter(Activity activity, List<PaymentMethod> listData, PaymentMethodCallback paymentMethodCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.paymentMethodCallback = paymentMethodCallback;
        maxPosition = this.listData.size() - 1;
        this.font = new Font(activity);

        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.noimg)
                .showImageOnLoading(R.drawable.noimg)
                .cacheOnDisk(true).build();
    }

    public void setListData(List<PaymentMethod> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
        maxPosition = this.listData.size() - 1;
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
            convertView = this.layoutInflater.inflate(R.layout.item_payment_method, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.rltCover = (LinearLayout) convertView.findViewById(R.id.rltCover);
            holder.viewBottom = convertView.findViewById(R.id.viewBottom);
            holder.txtLabel = (TextView) convertView.findViewById(R.id.txtLabel);
            holder.imv = (ImageView) convertView.findViewById(R.id.imv);

            font.overrideFontsBold(holder.txtLabel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (listData.get(position).getCode().equals("cashondelivery")) {
            imageLoader.displayImage("drawable://" + R.drawable.cash_sample, holder.imv, options);
        } else if (listData.get(position).getCode().equals("paypalmobile")) {
            imageLoader.displayImage("drawable://" + R.drawable.paypal_sample, holder.imv, options);
        } else {

        }

        holder.txtLabel.setText(listData.get(position).getLabel());

        if (listData.get(position).isChosen) {
            holder.rltCover.setBackgroundColor(activity.getResources().getColor(R.color.main_color));
            holder.imv.setBackgroundColor(activity.getResources().getColor(R.color.main_color));
            holder.txtLabel.setTextColor(activity.getResources().getColor(R.color.main_color));
        } else {
            holder.rltCover.setBackgroundColor(activity.getResources().getColor(R.color.txt_black_55));
            holder.imv.setBackgroundColor(activity.getResources().getColor(R.color.txt_black_55));
            holder.txtLabel.setTextColor(activity.getResources().getColor(R.color.txt_black_55));
        }

        holder.rltCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodCallback.onItemClick(position);
            }
        });

        if (position < maxPosition) {
            holder.viewBottom.setVisibility(View.GONE);
        } else {
            holder.viewBottom.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        LinearLayout rltCover;
        View viewBottom;
        TextView txtLabel;
        ImageView imv;
    }
}
