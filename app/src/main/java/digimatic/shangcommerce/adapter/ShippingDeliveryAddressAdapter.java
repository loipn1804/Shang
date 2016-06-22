package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.callback.ShippingDeliveryAddressItemCallback;
import digimatic.shangcommerce.daocontroller.CountryController;
import digimatic.shangcommerce.model.Address;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 3/16/2016.
 */
public class ShippingDeliveryAddressAdapter extends BaseAdapter {

    private int REQUEST_UPDATE = 111;

    private List<Address> listData;
    private List<Boolean> listChosen;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private ViewHolder holder;
    private Font font;

    private ShippingDeliveryAddressItemCallback callback;

    public ShippingDeliveryAddressAdapter(Activity activity, List<Address> listData, List<Boolean> listChosen, ShippingDeliveryAddressItemCallback callback) {
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
        this.layoutInflater = LayoutInflater.from(activity);
        this.activity = activity;
        this.font = new Font(activity);
        this.callback = callback;
        this.listChosen = new ArrayList<>();
        this.listChosen.addAll(listChosen);
    }

    public void setListData(List<Address> listData, List<Boolean> listChosen) {
        this.listData.clear();
        this.listData.addAll(listData);
        this.listChosen.clear();
        this.listChosen.addAll(listChosen);
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
            convertView = this.layoutInflater.inflate(R.layout.item_shipping_delivery_address, null);
            holder = new ViewHolder();
            holder.root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
            holder.imvCheck = (ImageView) convertView.findViewById(R.id.imvCheck);

            font.overrideFontsLight(holder.txtAddress);
            font.overrideFontsBold(holder.txtName);

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

        if (listChosen.get(position)) {
            holder.imvCheck.setBackgroundResource(R.drawable.circle_main);
        } else {
            holder.imvCheck.setBackgroundResource(R.drawable.circle_gray);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onChosen(position);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        LinearLayout root;
        TextView txtName;
        TextView txtAddress;
        ImageView imvCheck;
    }
}
