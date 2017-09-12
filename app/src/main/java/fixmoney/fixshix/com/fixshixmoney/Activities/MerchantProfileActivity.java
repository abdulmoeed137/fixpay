package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;
import fixmoney.fixshix.com.fixshixmoney.Validity.Validity;

public class MerchantProfileActivity extends FragmentActivity {
    LinearLayout container;


    TextView name,discounted_amount, universal_amount;
    RadioButton universal , discounted;
    ImageView merchant_icon;
    LinearLayout send;
    EditText amount;
    ProgressBar progressBar;
    TextView final_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        initialize();
        setUpComponent();
 }


    public void initialize(){
        container = (LinearLayout)findViewById(R.id.container);


        name = (TextView)findViewById(R.id.name);
        discounted_amount= (TextView)findViewById(R.id.discounted_amount);
        universal_amount = (TextView)findViewById(R.id.universal_amount);

        universal = (RadioButton)findViewById(R.id.universal);
        discounted= (RadioButton)findViewById(R.id.discount);

        merchant_icon = (ImageView)findViewById(R.id.merchant_icon);

        send=(LinearLayout)findViewById(R.id.send);

        amount=(EditText)findViewById(R.id.amount);

        progressBar = (ProgressBar)findViewById(R.id.pbar);
        progressBar.bringToFront();

        final_amount = (TextView)findViewById(R.id.final_amount);
    }


    private void setUpComponent() {
        Intent intent = getIntent();
         Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("merchant_id")) {
              String id = extras.getString("merchant_id");
                String name =  extras.getString("merchant_name");
                String image =  extras.getString("merchant_image");
                String universal =  extras.getString("universal");
                if (universal.trim().equals("0")) universal = "0";
                universal=utils.double2decimal(Double.parseDouble(universal)).toString();

                String discounted =  extras.getString("discounted");

                if (discounted.trim().equals("0")) discounted ="0";
                discounted=utils.double2decimal(Double.parseDouble(discounted)).toString();

                if (Double.parseDouble(universal)<=0)
                    this.universal.setEnabled(false);

                if(Double.parseDouble(discounted)<=0)
                    this.discounted.setEnabled(false);

                if (!id.isEmpty())
                {

                    this.name.setText(name);
                    this.discounted_amount.setText("Rs: "+discounted);
                    this.universal_amount.setText("Rs: "+universal);

                    utils.LoadImageFromURL(MerchantProfileActivity.this,image,merchant_icon);
                }


            }
        }

        universal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discounted.setChecked(false);
                final_amount.setText("0");
                amount.setText("");
            }
        });

        discounted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                universal.setChecked(false);
                final_amount.setText("0");
                amount.setText("");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_amount=amount.getText().toString();
                String final_amount = MerchantProfileActivity.this.final_amount.getText().toString();
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                String merchant_id = extras.getString("merchant_id");
                if (Validity.isAmountTrue(txt_amount,MerchantProfileActivity.this)){
                if (universal.isChecked())
                {
                    String txt_universal = universal_amount.getText().toString();
                    String[] splited = txt_universal.split("\\s+");

                    if (Double.parseDouble(final_amount) <= Double.parseDouble(splited[1]))
                    {

                     MakeTransaction(new SessionManager(MerchantProfileActivity.this).getId(),merchant_id,"2","abc123",final_amount);
                    }
                    else
                    {
                        SnackBar.makeCustomSnack(MerchantProfileActivity.this,"You can use maximum "+splited[1]+" from your Universal Amount.");
                    }

                }
                else if (discounted.isChecked())
                {
                    String txt_discounted= discounted_amount.getText().toString();
                    String[] splited =  txt_discounted.split("\\s+");

                    if (Double.parseDouble(final_amount) <= Double.parseDouble(splited[1]))
                    {
                        MakeTransaction(new SessionManager(MerchantProfileActivity.this).getId(),merchant_id,"1","abc123",final_amount);
                    }
                    else
                    {
                        SnackBar.makeCustomSnack(MerchantProfileActivity.this,"You can use maximum "+splited[1]+" from your Discounted Amount.");
                    }
                }
                else
                {

                    if (universal_amount.getText().toString().equals("Rs: 0") && discounted_amount.getText().toString().equals("Rs: 0"))
                       {
                           SnackBar.makeCustomErrorSnack(MerchantProfileActivity.this,"You don't have sufficient funds ");
                       }
                       else
                        {
                            SnackBar.makeCustomErrorSnack(MerchantProfileActivity.this,"Please Select Payment Type");
                        }

                }

            }}
        });


        amount.addTextChangedListener(

                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            if (discounted.isChecked())
                            final_amount.setText(utils.double2decimal(utils.SumwithPercent(s.toString())).toString());
                            else
                                final_amount.setText(s.toString());
                        }
                        catch (Exception e)
                        {
                            final_amount.setText("0");
                        }

                    }
                }
        );
    }

    public void MakeTransaction(String user_id, String merchant_id, String transaction_type, String qr, final String amount)
        {

            final HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("user_id", user_id);
            hashMap.put("merchant_id", merchant_id);
            hashMap.put("amount", "-"+amount);
            hashMap.put("qr_code", qr);
            hashMap.put("transaction_type", transaction_type);

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                public void run() {

                    JSONObject response = HttpRequest.SyncHttpRequest(MerchantProfileActivity.this, Constants.make_transaction, hashMap, progressBar);

                    if (response != null) {
                        try {

                            if (response.names().get(0).equals("success")) {
                                String current_amount=  new SessionManager(MerchantProfileActivity.this).getAmount();
                                Double new_amount = Double.parseDouble(current_amount)- Double.parseDouble(amount);
                                new SessionManager().setAmount(MerchantProfileActivity.this,new_amount.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        SnackBar.makeCustomSnack(MerchantProfileActivity.this, "Transaction Proceed.");

                                        Handler handler = new Handler();

                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                setResult(RESULT_OK);
                                                finish();
                                            }
                                        }, 1000);
                                    }
                                });


                            } else if (response.names().get(0).equals("failed")) {

                                SnackBar.makeCustomErrorSnack(MerchantProfileActivity.this, "Transaction can't proceed.");
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            } else {

                                SnackBar.makeCustomErrorSnack(MerchantProfileActivity.this, "Server Maintenance is on Progress");
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            }
                        } catch (JSONException e) {

                            SnackBar.makeCustomErrorSnack(MerchantProfileActivity.this, "Server Maintenance is on Progress");
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        }
                    }

                }
            });



        }

}