package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by user on 3/24/2016.
 */
public class OptionConfigAdapter extends BaseAdapter {

    private List<String> listData;
    private List<Boolean> listBool;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;

    public OptionConfigAdapter(Activity activity, List<String> listData, List<Boolean> listBool) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.listBool = new ArrayList<>();
        this.listBool.addAll(listBool);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.font = new Font(activity);
    }

    public void setListData(List<String> listData, List<Boolean> listBool) {
        this.listData.clear();
        this.listData.addAll(listData);
        this.listBool.clear();
        this.listBool.addAll(listBool);
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
            convertView = this.layoutInflater.inflate(R.layout.item_simple_string, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtString = (TextView) convertView.findViewById(R.id.txtString);

            font.overrideFontsBold(holder.txtString);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtString.setText(listData.get(position));

        if (listBool.get(position)) {
            holder.txtString.setTextColor(activity.getResources().getColor(R.color.txt_black_33));
            holder.txtString.setPaintFlags(holder.txtString.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        } else {
            holder.txtString.setTextColor(activity.getResources().getColor(R.color.txt_black_77));
            holder.txtString.setPaintFlags(holder.txtString.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtString;
    }
}
