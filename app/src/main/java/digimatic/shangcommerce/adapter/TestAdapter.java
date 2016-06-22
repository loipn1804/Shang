package digimatic.shangcommerce.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.activity.BaseActivity;

/**
 * Created by USER on 3/4/2016.
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.SimpleViewHolder> {

    private Activity activity;
    private List<String> listData;

    public TestAdapter(Activity activity, List<String> listData) {
        this.activity = activity;
        this.listData = new ArrayList<>();
        this.listData.addAll(listData);
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTest;

        public SimpleViewHolder(View view) {
            super(view);
            txtTest = (TextView) view.findViewById(R.id.txtTest);
        }
    }

    @Override
    public TestAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_test, viewGroup, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestAdapter.SimpleViewHolder holder, final int position) {
        holder.txtTest.setText(listData.get(position));
        holder.txtTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) activity).showToastInfo(position + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
