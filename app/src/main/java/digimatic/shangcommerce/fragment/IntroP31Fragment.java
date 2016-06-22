package digimatic.shangcommerce.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import digimatic.shangcommerce.R;
import digimatic.shangcommerce.staticfunction.Font;

/**
 * Created by USER on 05/10/2016.
 */
public class IntroP31Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_p3_1, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        Font font = new Font(getActivity());
        font.overrideFontsBold(view.findViewById(R.id.txtTitle));
        font.overrideFontsLight(view.findViewById(R.id.txtDesc));
    }

    private void initData() {

    }
}
