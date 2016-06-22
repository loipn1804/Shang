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

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.model.RedeemableItem;
import digimatic.shangcommerce.staticfunction.Font;
import digimatic.shangcommerce.staticfunction.StaticFunction;

/**
 * Created by USER on 05/04/2016.
 */
public class RedeemableAdapter extends BaseAdapter {

    public interface Callback {
        void enterQuantity(int position);

        void unCheck(int position);
    }

    private List<RedeemableItem> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;
    private Callback callback;

    private int margin;
    private int check_width;
    private int width;
    private int height;

    public RedeemableAdapter(Activity activity, List<RedeemableItem> listData, Callback callback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.font = new Font(activity);
        this.callback = callback;

        margin = (int) activity.getResources().getDimension(R.dimen.dm_5dp);
        width = StaticFunction.getScreenWidth(activity) - margin * 2;
        height = width * 12 / 45;
        check_width = width * 14 / 45;
    }

    public void setListData(List<RedeemableItem> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_redeemable, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.rltRoot = (RelativeLayout) convertView.findViewById(R.id.rltRoot);
            holder.txtLabel = (TextView) convertView.findViewById(R.id.txtLabel);
            holder.txtEVoucher = (TextView) convertView.findViewById(R.id.txtEVoucher);
            holder.txtPoint = (TextView) convertView.findViewById(R.id.txtPoint);
            holder.imvCheck = (ImageView) convertView.findViewById(R.id.imvCheck);
            holder.lnlCheck = (LinearLayout) convertView.findViewById(R.id.lnlCheck);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);

            font.overrideFontsLight(holder.txtLabel);
            font.overrideFontsBold(holder.txtEVoucher);
            font.overrideFontsLight(holder.txtPoint);
            font.overrideFontsLight(holder.txtQuantity);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtEVoucher.setText("$" + listData.get(position).getAmount());
        holder.txtPoint.setText(" = " + listData.get(position).getRewardsRequired() + " pts");
        holder.txtQuantity.setText(listData.get(position).getMaxRedeems() + " eVoucher");

//        if (listData.get(position).isCheck()) {
//            holder.imvCheck.setImageResource(R.drawable.ic_check);
//            holder.txtQuantity.setVisibility(View.VISIBLE);
//        } else {
//            holder.imvCheck.setImageResource(R.drawable.ic_uncheck);
//            holder.txtQuantity.setVisibility(View.GONE);
//        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.rltRoot.getLayoutParams();
        params.height = height;
        params.width = width;
        if (position == 0) {
            params.topMargin = margin;
            params.bottomMargin = 0;
        } else {
            params.topMargin = 0;
            params.bottomMargin = 0;
        }
        holder.rltRoot.setLayoutParams(params);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.lnlCheck.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = check_width;
        holder.lnlCheck.setLayoutParams(layoutParams);

//        holder.imvCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isCheck = listData.get(position).isCheck();
//                if (isCheck) {
//                    callback.unCheck(position);
//                } else {
//                    callback.enterQuantity(position);
//                }
//            }
//        });

        holder.rltRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.enterQuantity(position);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        RelativeLayout rltRoot;
        TextView txtLabel;
        TextView txtEVoucher;
        TextView txtPoint;
        ImageView imvCheck;
        LinearLayout lnlCheck;
        TextView txtQuantity;
    }
}
