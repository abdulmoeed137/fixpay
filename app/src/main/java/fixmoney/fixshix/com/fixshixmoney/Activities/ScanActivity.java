package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


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
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //this code will be executed on devices running ICS or later

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                     {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            } else   openCameraForBarCode();
        }else
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_GRANTED) {
                        openCameraForBarCode();
                    }

                }  else
                {
                    fixmoney.fixshix.com.fixshixmoney.Toast.Toast.makeCustomErrorToast(ScanActivity.this,"Permission Failed, Please Open Permission From Settings");
                    finish();
                }
            }
        }
    }
}