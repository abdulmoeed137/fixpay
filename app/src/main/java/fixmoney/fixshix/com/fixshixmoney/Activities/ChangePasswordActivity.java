package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
 * Created by lenovo on 7/4/2017.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    EditText old_password, password, transaction_password, txt_code;
    Button signup,send_code,verify_code;
    ProgressBar progressBar;
    LinearLayout code_layout, all_fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initialize();
        setUpComponent();

    }

    private void initialize() {
       old_password = (EditText)this.findViewById(R.id.old_password);
        password = (EditText)this.findViewById(R.id.password);
        transaction_password = (EditText)this.findViewById(R.id.transaction_password);

        signup = (Button)this.findViewById(R.id.signup);

        progressBar = (ProgressBar)this.findViewById(R.id.pbar);
        progressBar.bringToFront();
        send_code = (Button)this.findViewById(R.id.send_code);

        code_layout = (LinearLayout)this.findViewById(R.id.code_layout);

        verify_code = (Button)this.findViewById(R.id.verify_code);

        txt_code= (EditText)this.findViewById(R.id.code);

        all_fields = (LinearLayout)this.findViewById(R.id.all_fields);
    }

    private void setUpComponent() {

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String txt_contact = new SessionManager(ChangePasswordActivity.this).getContact();
                String txt_password = password.getText().toString();
                String txt_transaction_password = transaction_password.getText().toString();

                if ( Validity.isContactTrue(txt_contact,ChangePasswordActivity.this)
                             && Validity.isPasswordTrue(txt_password,ChangePasswordActivity.this)
                                && Validity.isTransactionPasswordTrue(txt_transaction_password,ChangePasswordActivity.this))
                                 {

                                        final HashMap<String, String> hashMap = new HashMap<String, String>();

                                        hashMap.put("contact", txt_contact);
                                        hashMap.put("password", txt_password);
                                        hashMap.put("t_password", txt_transaction_password);

                                     Executor executor = Executors.newSingleThreadExecutor();
                                     executor.execute(new Runnable() {
                                         @Override
                                         public void run() {

                                             JSONObject response = HttpRequest.SyncHttpRequest(ChangePasswordActivity.this, Constants.forgetpassword, hashMap, progressBar);


                                             if (response != null) {
                                                 try {



                                                     if (response.names().get(0).equals("success")) {

                                                         runOnUiThread(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                                         WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                                 SnackBar.makeCustomSnack(ChangePasswordActivity.this,"Passwords Updated Successfully");
                                                                 Handler handler = new Handler();

                                                                 handler.postDelayed(new Runnable() {
                                                                     public void run() {

                                                                         finish();
                                                                     }
                                                                 }, 1000);
                                                             }
                                                         });



                                                     } else if (response.names().get(0).equals("failed")) {

                                                         SnackBar.makeCustomErrorSnack(ChangePasswordActivity.this, response.getString("failed"));

                                                     } else {

                                                         SnackBar.makeCustomErrorSnack(ChangePasswordActivity.this, response.getString("failed"));
                                                     }
                                                 } catch (JSONException e) {

                                                     SnackBar.makeCustomErrorSnack(ChangePasswordActivity.this, "System Maintenance on Progress. Try bit Later ");

                                                 }
                                             }

                                         }
                                     });



                                  }
            }
        });
        send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _old_password = old_password.getText().toString();
                if (Validity.isPasswordTrue(_old_password,ChangePasswordActivity.this))
                {

                    final HashMap<String, String> hashMap = new HashMap<String, String>();

                    hashMap.put("password",_old_password);
                    hashMap.put("user_id",new SessionManager(ChangePasswordActivity.this).getId());

                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject response = HttpRequest.SyncHttpRequest(ChangePasswordActivity.this, Constants.isPasswordTrue, hashMap, progressBar);


                            if (response != null) {
                                try {
                                    if (response.names().get(0).equals("success")) {





                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                send_code.setVisibility(View.GONE);

                                               old_password.setVisibility(View.GONE);
                                                all_fields . setVisibility(View.VISIBLE);
                                            }
                                        });


                                    } else if (response.names().get(0).equals("failed")) {

                                        SnackBar.makeCustomErrorSnack(ChangePasswordActivity.this, response.getString("failed"));

                                    } else {

                                        SnackBar.makeCustomErrorSnack(ChangePasswordActivity.this, response.getString("failed"));
                                    }
                                } catch (JSONException e) {

                                    SnackBar.makeCustomErrorSnack(ChangePasswordActivity.this, "System Maintenance on Progress. Try bit Later ");

                                }
                            }

                        }
                    });
                }
            }
        });

    }
}
