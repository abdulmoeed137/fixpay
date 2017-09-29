package fixmoney.fixshix.com.fixshixmoney.Activities;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.CustomBoldTextView;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;
import fixmoney.fixshix.com.fixshixmoney.Validity.Validity;

public class FundWithdrawActivity extends AppCompatActivity {
    TextView c_amount;
    Context context;
    ProgressBar progressBar;
    EditText amount;
    TextView bank_name, acc_title, acc_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_fund);

        initialize();
        setUpComponents();
    }

    private void setUpComponents() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("user_id",new SessionManager(context).getId());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                final JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.CheckUniversalBalance, hashMap, progressBar);


                if (response != null) {
                    try {

                        Log.d("yoyo",response+"");
                        if (response.names().get(0).equals("success")) {


                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        c_amount.setText(utils.double2decimal(Double.parseDouble(response.getString("success"))).toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });



                        } else if (response.names().get(0).equals("failed")) {

                            SnackBar.makeCustomErrorSnack(context, response.getString("failed"));

                        } else {

                            SnackBar.makeCustomErrorSnack(context, response.getString("failed"));
                        }
                    } catch (JSONException e) {

                        SnackBar.makeCustomErrorSnack(context, "System Maintenance on Progress. Try bit Later ");

                    }
                }

            }
        });


        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validity.isAmountTrue(amount.getText().toString(),context) && Validity.isTitleTrue(acc_title.getText().toString(),context)
                        && Validity.isAccNumberTrue(acc_number.getText().toString(),context)
                        && Validity.isBankNameTrue(bank_name.getText().toString(),context))
                {

                    if (Double.parseDouble(amount.getText().toString()) <= Double.parseDouble(c_amount.getText().toString()))
                    {

                        if ( Double.parseDouble(amount.getText().toString()) <= 5000 && Double.parseDouble(amount.getText().toString())>=1000 ){

                        final HashMap<String, String> hashMap = new HashMap<String, String>();

                            hashMap.put("user_id",new SessionManager(context).getId());
                            hashMap.put("amount",amount.getText().toString());
                            hashMap.put("bank_name",bank_name.getText().toString());
                            hashMap.put("title",acc_title.getText().toString());
                            hashMap.put("account_number",acc_number.getText().toString());


                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {

                                final JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.WithdrawRequest, hashMap, progressBar);


                                if (response != null) {
                                    try {

                                        Log.d("yoyo",response+"");
                                        if (response.names().get(0).equals("success")) {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                    SnackBar.makeCustomSnack(context, "Request Proceed.");

                                                    Handler handler = new Handler();

                                                    handler.postDelayed(new Runnable() {
                                                        public void run() {

                                                            finish();
                                                        }
                                                    }, 1000);
                                                }
                                            });

                                        } else if (response.names().get(0).equals("failed")) {

                                            SnackBar.makeCustomErrorSnack(context, response.getString("failed"));

                                        } else {

                                            SnackBar.makeCustomErrorSnack(context, response.getString("failed"));
                                        }
                                    } catch (JSONException e) {

                                        SnackBar.makeCustomErrorSnack(context, "System Maintenance on Progress. Try bit Later ");

                                    }
                                }

                            }
                        });



                    }
                    else {
                            utils.hideSoftKeyboard(FundWithdrawActivity.this);
                            SnackBar.makeCustomErrorSnack(context,"AMount should be between 1000-5000");
                        }
                    }
                    else
                    {
                        utils.hideSoftKeyboard(FundWithdrawActivity.this);
                        SnackBar.makeCustomErrorSnack(context,"Dont have enough amount in your account");
                    }
                }
            }
        });
    }

    private void initialize() {

        c_amount = (TextView)this.findViewById(R.id.c_amount);
        context = this;
        progressBar = (ProgressBar)this.findViewById(R.id.pbar);
        progressBar.bringToFront();

        amount = (EditText)this.findViewById(R.id.amount);

        bank_name = (EditText)this.findViewById(R.id.bank_name);

        acc_number = (TextView)this.findViewById(R.id.acc_number);

        acc_title= (TextView )this.findViewById( R.id.acc_title);

    }
}
