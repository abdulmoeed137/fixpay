package fixmoney.fixshix.com.fixshixmoney;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import fixmoney.fixshix.com.fixshixmoney.Activities.MerchantProfileActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.MerchantProfileIDActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.ScanActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.UserProfileIDActivity;

import static fixmoney.fixshix.com.fixshixmoney.Utilities.utils.scan_flow;


/**
 * Created by lenovo on 8/18/2017.
 */

public class DialogBox  {
    public void showDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog1);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(activity.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.3f;
        dialog.show();


        dialog.findViewById(R.id.o1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.startActivity(new Intent(activity,UserProfileIDActivity.class));

            }
        });
        dialog.findViewById(R.id.o2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_flow="share";
                activity.startActivity(new Intent(activity, ScanActivity.class));

            }
        });

    }
    public void showDialogPay(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog1);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(activity.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        lp.dimAmount = 0.3f;
        dialog.show();

        ((TextView)dialog.findViewById(R.id.o1)).setText("Pay via ID");
        ((TextView)dialog.findViewById(R.id.o2)).setText("Pay via QR");

        dialog.findViewById(R.id.o1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.startActivity(new Intent(activity,MerchantProfileIDActivity.class));

            }
        });
        dialog.findViewById(R.id.o2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.startActivity(new Intent(activity, ScanActivity.class));

            }
        });

    }
}
