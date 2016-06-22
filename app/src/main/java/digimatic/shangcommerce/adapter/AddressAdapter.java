package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.AddressDetailActivity;
import digimatic.shangcommerce.callback.AddressItemCallback;
import digimatic.shangcommerce.daocontroller.CountryController;
import digimatic.shangcommerce.model.Address;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 3/9/2016.
 */
public class AddressAdapter extends BaseSwipListAdapter {

    private int REQUEST_UPDATE = 111;

    private List<Address> listData;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;

    private AddressItemCallback callback;

    public AddressAdapter(Activity activity, List<Address> listData, AddressItemCallback callback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.font = new Font(activity);
        this.callback = callback;
    }

    public void setListData(List<Address> listData) {
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
            convertView = this.layoutInflater.inflate(R.layout.item_address, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
            holder.lnlSetDefault = (LinearLayout) convertView.findViewById(R.id.lnlSetDefault);
            holder.txtSetDefault = (TextView) convertView.findViewById(R.id.txtSetDefault);
            holder.imvShipping = (ImageView) convertView.findViewById(R.id.imvShipping);
            holder.imvBilling = (ImageView) convertView.findViewById(R.id.imvBilling);

            font.overrideFontsBold(holder.txtName);
            font.overrideFontsLight(holder.txtAddress);
            font.overrideFontsBold(holder.txtSetDefault);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Address addressObj = listData.get(position);
        String name = addressObj.getFirstname() + " " + (addressObj.getMiddlename().length() > 0 ? addressObj.getMiddlename() + " " : "") + addressObj.getLastname();
        holder.txtName.setText(name);
        String address = addressObj.getStreet() + (addressObj.getStreet_2().length() > 0 ? " " + addressObj.getStreet_2() : "");
        address += "\n" + addressObj.getCity() + " " + addressObj.getPostcode();
        address += "\n" + CountryController.getCountryLabel(activity, addressObj.getCountry_id());
        address += "\n" + "Phone: " + addressObj.getTelephone();
        holder.txtAddress.setText(address);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDetail = new Intent(activity, AddressDetailActivity.class);
                String data = new Gson().toJson(addressObj);
                intentDetail.putExtra("data", data);
                activity.startActivityForResult(intentDetail, REQUEST_UPDATE);
            }
        });

        holder.lnlSetDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.setDefault(position);
            }
        });

        if (addressObj.getIs_default_shipping() == 1) {
            holder.imvShipping.setVisibility(View.VISIBLE);
        } else {
            holder.imvShipping.setVisibility(View.GONE);
        }

        if (addressObj.getIs_default_billing() == 1) {
            holder.imvBilling.setVisibility(View.VISIBLE);
        } else {
            holder.imvBilling.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtName;
        TextView txtAddress;
        LinearLayout lnlSetDefault;
        TextView txtSetDefault;
        ImageView imvShipping;
        ImageView imvBilling;
    }
}
