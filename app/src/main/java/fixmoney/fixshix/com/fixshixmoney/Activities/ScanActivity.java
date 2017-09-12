package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;


import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.Fragments.BarCodeFragment;

public class ScanActivity extends FragmentActivity {

    FrameLayout container;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scan);
        initialize();
        openCameraForBarCode();

    }

    public void initialize(){
        container = (FrameLayout)findViewById(R.id.container);
        progressBar= (ProgressBar)findViewById(R.id.pbar);
    }
    public void openCameraForBarCode(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment mFragment = new BarCodeFragment();
        transaction.replace(R.id.container, mFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}