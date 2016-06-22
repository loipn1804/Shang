package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.callback.DeliveryItemCallback;
import digimatic.shangcommerce.callback.FaqItemCallback;

/**
 * Created by USER on 3/8/2016.
 */
public class FaqAdapter extends BaseAdapter {

    private List<Boolean> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private FaqItemCallback faqItemCallback;

    public FaqAdapter(Activity activity, List<Boolean> listData, FaqItemCallback faqItemCallback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.faqItemCallback = faqItemCallback;
    }

    public void setListData(List<Boolean> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_faq, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtQuestion = (TextView) convertView.findViewById(R.id.txtQuestion);
            holder.txtAnswer = (TextView) convertView.findViewById(R.id.txtAnswer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!listData.get(position)) {
            holder.txtQuestion.setTextColor(activity.getResources().getColor(R.color.txt_black_33));
            holder.txtAnswer.setVisibility(View.GONE);
        } else {
            holder.txtQuestion.setTextColor(activity.getResources().getColor(R.color.main_color));
            holder.txtAnswer.setVisibility(View.VISIBLE);
        }

        holder.txtQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faqItemCallback.onItemClick(position, listData.get(position));
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtQuestion;
        TextView txtAnswer;
    }
}
