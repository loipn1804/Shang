package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.callback.ItemClickCallback;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;
import greendao.Notification;

/**
 * Created by USER on 3/9/2016.
 */
public class AlertAdapter extends BaseAdapter {

    private List<Notification> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;
    private ItemClickCallback itemClickCallback;

    public AlertAdapter(Activity activity, List<Notification> listData, ItemClickCallback itemClickCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.font = new Font(activity);
        this.itemClickCallback = itemClickCallback;
    }

    public void setListData(List<Notification> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_alert, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.view = convertView.findViewById(R.id.view);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);

            font.overrideFontsBold(holder.txtTitle);
            font.overrideFontsLight(holder.txtDesc);
            font.overrideFontsLight(holder.txtTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(listData.get(position).getTitle());
        holder.txtDesc.setText(listData.get(position).getMessage());
        holder.txtTime.setText(StaticFunction.convertYYYYMMDDtoAppFormat(listData.get(position).getCreated_at()));

        if (listData.get(position).getIs_read() == 0) {
            holder.view.setBackgroundResource(R.drawable.circle_red);
        } else {
            holder.view.setBackgroundResource(R.drawable.circle_red_alert);
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
        View view;
        TextView txtTitle;
        TextView txtDesc;
        TextView txtTime;
    }
}
