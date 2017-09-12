package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

/**
 * Created by lenovo on 9/6/2017.
 */

public class FinalShareActivity extends AppCompatActivity {
    
    TextView id,name,email,contact,merchant;
    ProgressBar progressBar;
    LinearLayout btn_confirm;
    EditText amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_share);
        initialize();
        setUpComponent();
    }

    private void setUpComponent() {

        id.setText(utils.id);
        name.setText(utils.name);
        contact.setText(utils.contact);
        email.setText(utils.email);
        merchant.setText(getIntent().getExtras().getString("merchant_name"));
        final String merchant_id = getIntent().getExtras().getString("merchant_id");
        final double merchant_amount =  Double.parseDouble(getIntent().getExtras().getString("merchant_amount"));
        String transaction_type = "1";
        if ( merchant_id.equals("1"))
            transaction_type = "2";
        final String finalTransaction_type = transaction_type;
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  amount =FinalShareActivity.this.amount.getText().toString();
                if (Validity.isAmountTrue(amount,FinalShareActivity.this)) {
                    Double amount2=Double.parseDouble(FinalShareActivity.this.amount.getText().toString());
                    if (amount2 > merchant_amount){
                        SnackBar.makeCustomErrorSnack(FinalShareActivity.this," Maximum "+merchant_amount+" can share");
                    }
                    else
                    MakeTransaction(new SessionManager(FinalShareActivity.this).getId(), merchant_id, finalTransaction_type, "abc123",amount.toString(),utils.id
                    );
                }            }
        });
    }

    private void initialize() {
        id = (TextView)findViewById(R.id.id);
        name =(TextView)findViewById(R.id.name);
        contact=(TextView)findViewById(R.id.contact);
        email =(TextView)findViewById(R.id.email);
        merchant =(TextView)findViewById(R.id.merchant);

        progressBar = (ProgressBar)findViewById(R.id.pbar);
        progressBar.bringToFront();

        btn_confirm = (LinearLayout)findViewById(R.id.btn_confirm);

        amount = (EditText) findViewById(R.id.amount);
    }

    public void MakeTransaction(String user_id, String merchant_id, String transaction_type, String qr, final String amount,String receiver_id)
    {

        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("user_id", user_id);
        hashMap.put("merchant_id", merchant_id);
        hashMap.put("amount", "-"+amount);
        hashMap.put("qr_code", qr);
        hashMap.put("transaction_type", transaction_type);
        hashMap.put("receiver_id", receiver_id);

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {

                JSONObject response = HttpRequest.SyncHttpRequest(FinalShareActivity.this, Constants.share_transaction, hashMap, progressBar);

                if (response != null) {
                    try {

                        if (response.names().get(0).equals("success")) {
                            String current_amount=  new SessionManager(FinalShareActivity.this).getAmount();
                            Double new_amount = Double.parseDouble(current_amount)- Double.parseDouble(amount);
                            new SessionManager().setAmount(FinalShareActivity.this,new_amount.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    SnackBar.makeCustomSnack(FinalShareActivity.this, "Transaction Proceed.");

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

                            SnackBar.makeCustomErrorSnack(FinalShareActivity.this, "Transaction can't proceed.");
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        } else {

                            SnackBar.makeCustomErrorSnack(FinalShareActivity.this, "Server Maintenance is on Progress");
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        }
                    } catch (JSONException e) {

                        SnackBar.makeCustomErrorSnack(FinalShareActivity.this, "Server Maintenance is on Progress");
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }
                }

            }
        });



    }
}
