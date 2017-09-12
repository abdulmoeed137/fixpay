package fixmoney.fixshix.com.fixshixmoney.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import fixmoney.fixshix.com.fixshixmoney.Fragments.FixMoneyFragment;
import fixmoney.fixshix.com.fixshixmoney.Fragments.NotificationFragment;
import fixmoney.fixshix.com.fixshixmoney.Fragments.SettingFragment;

/**
 * Created by lenovo on 7/5/2017.
 */

public class DashboardPagerAdapter extends FragmentStatePagerAdapter {

    public DashboardPagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("yoyoyoyo",position+"");
//        if(position ==0)
//        {
//
//            return new FixshixFragment();
//        }
        if(position ==0)
        {
            return new FixMoneyFragment();
        }
        if(position ==1)
        {
            return new NotificationFragment();
        }
        else if (position == 2){
            return new SettingFragment();}
        else
            return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
