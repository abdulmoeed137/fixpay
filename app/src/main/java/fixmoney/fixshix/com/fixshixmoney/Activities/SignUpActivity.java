package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;
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

public class SignUpActivity extends AppCompatActivity {

    EditText name , email , contact, password, transaction_password, txt_code;
    Button signup,send_code,verify_code;
    ProgressBar progressBar;
    String code = "",number="";
    LinearLayout code_layout, all_fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initialize();
        setUpComponent();

    }

    private void initialize() {
        name = (EditText)this.findViewById(R.id.name);
        email = (EditText)this.findViewById(R.id.email);
        contact = (EditText)this.findViewById(R.id.phone);
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

                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_contact = number;
                String txt_password = password.getText().toString();
                String txt_transaction_password = transaction_password.getText().toString();

                if (Validity.isNameTrue(txt_name,SignUpActivity.this)
                            && Validity.isContactTrue(txt_contact,SignUpActivity.this)
                             && Validity.isPasswordTrue(txt_password,SignUpActivity.this)
                                && Validity.isTransactionPasswordTrue(txt_transaction_password,SignUpActivity.this))
                                 {

                                        final HashMap<String, String> hashMap = new HashMap<String, String>();

                                        hashMap.put("name", txt_name);
                                        hashMap.put("email", txt_email);
                                        hashMap.put("contact", number);
                                        hashMap.put("password", txt_password);
                                        hashMap.put("transaction_password", txt_transaction_password);

                                     Executor executor = Executors.newSingleThreadExecutor();
                                     executor.execute(new Runnable() {
                                         @Override
                                         public void run() {

                                             JSONObject response = HttpRequest.SyncHttpRequest(SignUpActivity.this, Constants.signup, hashMap, progressBar);


                                             if (response != null) {
                                                 try {

                                                     Log.d("yoyo",response+"");
                                                     if (response.names().get(0).equals("success")) {


                                                         startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                                                         finish();
                                                         SnackBar.makeCustomSnack(SignUpActivity.this,"Account Created Successfully");

                                                     } else if (response.names().get(0).equals("failed")) {

                                                         SnackBar.makeCustomErrorSnack(SignUpActivity.this, response.getString("failed"));

                                                     } else {

                                                         SnackBar.makeCustomErrorSnack(SignUpActivity.this, response.getString("failed"));
                                                     }
                                                 } catch (JSONException e) {

                                                     SnackBar.makeCustomErrorSnack(SignUpActivity.this, "System Maintenance on Progress. Try bit Later ");

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
                final String txt_contact = contact.getText().toString();
                if (Validity.isContactTrue(txt_contact,SignUpActivity.this))
                {

                    final HashMap<String, String> hashMap = new HashMap<String, String>();

                    hashMap.put("number",txt_contact);

                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject response = HttpRequest.SyncHttpRequest(SignUpActivity.this, Constants.checkifnumberexist, hashMap, progressBar);


                            if (response != null) {
                                try {

                                    Log.d("yoyo",response+"");
                                    if (response.names().get(0).equals("success")) {


                                        code = response.getString("success");
                                        Log.d(code+"", "run: ");
                                        number =  txt_contact;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                send_code.setVisibility(View.GONE);
                                                code_layout.setVisibility(View.VISIBLE);
                                                contact.setEnabled(false);
                                            }
                                        });


                                    } else if (response.names().get(0).equals("failed")) {

                                        SnackBar.makeCustomErrorSnack(SignUpActivity.this, response.getString("failed"));

                                    } else {

                                        SnackBar.makeCustomErrorSnack(SignUpActivity.this, response.getString("failed"));
                                    }
                                } catch (JSONException e) {

                                    SnackBar.makeCustomErrorSnack(SignUpActivity.this, "System Maintenance on Progress. Try bit Later ");

                                }
                            }

                        }
                    });
                }
            }
        });

        verify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _code = txt_code.getText().toString();
                if ( Validity.isCodeTrue(_code,SignUpActivity.this))
                {
                    if ( _code.equals(code))
                    {
                       code_layout.setVisibility(View.GONE);
                        all_fields . setVisibility(View.VISIBLE);


                    }
                    else{
                        utils.hideSoftKeyboard(SignUpActivity.this);
                        SnackBar.makeCustomErrorSnack(SignUpActivity.this,"Incorrect Verification Code!");
                    }
                }
            }
        });
    }
}
