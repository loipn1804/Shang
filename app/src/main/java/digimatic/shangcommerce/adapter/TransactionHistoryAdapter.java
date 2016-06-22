package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.TransactionDetailActivity;
import digimatic.shangcommerce.model.OrderHistory;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 3/7/2016.
 */
public class TransactionHistoryAdapter extends BaseAdapter {

    private List<OrderHistory> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private int maxPosition;
    private Font font;

    public TransactionHistoryAdapter(Activity activity, List<OrderHistory> listData) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        maxPosition = this.listData.size() - 1;
        this.font = new Font(activity);
    }

    public void setListData(List<OrderHistory> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_transaction_history, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.viewBottom = convertView.findViewById(R.id.viewBottom);
            holder.txtId = (TextView) convertView.findViewById(R.id.txtId);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);

            font.overrideFontsLight(holder.txtId);
            font.overrideFontsLight(holder.txtTime);
            font.overrideFontsBold(holder.txtPrice);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtId.setText("#" + listData.get(position).getIncrement_id());
        holder.txtTime.setText(StaticFunction.convertYYYYMMDDtoAppFormat(listData.get(position).getCreated_at()));
        BigDecimal b_total = new BigDecimal(listData.get(position).getGrand_total()).setScale(2, BigDecimal.ROUND_HALF_UP);
        holder.txtPrice.setText("$" + StaticFunction.formatPrice(b_total));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TransactionDetailActivity.class);
                intent.putExtra("entity_id", listData.get(position).getEntity_id());
                intent.putExtra("increment_id", listData.get(position).getIncrement_id());
                activity.startActivity(intent);
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
        View viewBottom;
        TextView txtId;
        TextView txtTime;
        TextView txtPrice;
    }
}