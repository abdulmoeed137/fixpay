package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.Adapter.DashboardPagerAdapter;
import io.fabric.sdk.android.Fabric;

/**
 * Created by lenovo on 7/5/2017.
 */

public class DashboardActivity extends AppCompatActivity {
    private ViewPager homePager;
    private TabLayout tabLayout;

       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initialize();
        setUpComponent();

    }
    private void setUpComponent() {

        homePager.setAdapter(new DashboardPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(homePager);
        LayoutInflater inflater = LayoutInflater.from(this);

//
//        View view1 = inflater.inflate(R.layout.tab_text_layout, null);
//        TextView text = (TextView) view1.findViewById(R.id.tab_text);
//        ImageView imageView = (ImageView)view1.findViewById(R.id.tab_img);
//        text.setText("Fixshix");
//        imageView.setImageDrawable(getResources().getDrawable(R.drawable.fixshix));
//        tabLayout.getTabAt(0).setCustomView(view1);


        View view2 = inflater.inflate(R.layout.tab_text_layout, null);
        TextView text2 = (TextView) view2.findViewById(R.id.tab_text);
        ImageView imageView2 = (ImageView)view2.findViewById(R.id.tab_img);
        text2.setText("Fixwallet");
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.fixwallet));
        tabLayout.getTabAt(0).setCustomView(view2);


        View view3 = inflater.inflate(R.layout.tab_text_layout, null);
        TextView text3 = (TextView) view3.findViewById(R.id.tab_text);
        ImageView imageView3 = (ImageView)view3.findViewById(R.id.tab_img);
        text3.setText("Notifications");
        imageView3.setImageDrawable(getResources().getDrawable(R.drawable.notification));

      tabLayout.getTabAt(1).setCustomView(view3);



        View view4 = inflater.inflate(R.layout.tab_text_layout, null);
        TextView text4= (TextView) view4.findViewById(R.id.tab_text);
        ImageView imageView4 = (ImageView)view4.findViewById(R.id.tab_img);
        text4.setText("Settings");
        imageView4.setImageDrawable(getResources().getDrawable(R.drawable.setting));


       tabLayout.getTabAt(2).setCustomView(view4);

    }

    private void initialize() {
        homePager = (ViewPager) findViewById(R.id.container);
        tabLayout=(TabLayout)findViewById(R.id.footer);

    }
}
