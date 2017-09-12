package fixmoney.fixshix.com.fixshixmoney.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import fixmoney.fixshix.com.fixshixmoney.R;

/**
 * Created by lenovo on 7/5/2017.
 */

public class FixshixFragment extends Fragment {
    View rootView;
    ImageView FooterImg1, FooterImg2,FooterImg3,FooterImg4 ;
   public FixshixFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fixshix, container, false);
     initialize();
        setUpComponent();
    return rootView;
    }

    private void setUpComponent() {

      //  FooterImg1 .setImageDrawable(getResources().getDrawable(R.drawable.fixshix2));
    }

    private void initialize() {

    }
}
