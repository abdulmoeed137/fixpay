package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.transitionseverywhere.TransitionManager;

import org.json.JSONArray;
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

public class SignInActivity extends AppCompatActivity {
    int startClick = 0;
    LinearLayout logoLayout;
    LinearLayout letsGetStarted;
    LinearLayout editTextLayouts;
    ViewGroup transitionsContainer;
    TextView SignUp ;
    final Handler handler = new Handler();
    ProgressBar progressBar;
    EditText contact , password;
    TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseCrash.report(new Exception("My first Android non-fatal error"));
          initialize();
           setUpComponent();

    }

    private void setUpComponent() {
;       contact.setText("3452121442");
        password.setText("abcd1234");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                TransitionManager.beginDelayedTransition(transitionsContainer);
               if ( new SessionManager(SignInActivity.this).CheckIfSessionExist() )
               {
                   startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                   finish();
               }else
                letsGetStarted.setVisibility(View.VISIBLE);

            }
        }, 1500);

        letsGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startClick == 0 ){
                TransitionManager.beginDelayedTransition(transitionsContainer);
                editTextLayouts.setVisibility(View.VISIBLE);
                startClick=1;}
                else if(startClick == 1){

                    String txt_contact = contact. getText(). toString();
                    String txt_password = password. getText() . toString();

                    if (Validity.isContactTrue(txt_contact,SignInActivity.this) && Validity.isPasswordTrue(txt_password,SignInActivity.this)) {

                        final HashMap<String, String> hashMap = new HashMap<String, String>();

                        hashMap.put("contact", txt_contact);
                        hashMap.put("password", txt_password);

                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                            public void run() {

                                JSONObject response = HttpRequest.SyncHttpRequest(SignInActivity.this, Constants.login, hashMap, progressBar);

                                if (response != null) {
                                    try {

                                        if (response.names().get(0).equals("success")) {

                                            JSONArray data = response.getJSONArray("success");
                                            JSONObject row = data.getJSONObject(0);

                                            String id = row.getString("user_id");
                                            String name = row.getString("name");
                                            String email = row.getString("email");
                                            String contact = row.getString("contact");
                                            String amount = row.getString("amount");

                                            new SessionManager(SignInActivity.this,id,name,email,contact,amount);

                                            startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                                            finish();

                                        } else if (response.names().get(0).equals("failed")) {

                                            SnackBar.makeCustomErrorSnack(SignInActivity.this, "Invalid Number or Password");

                                        } else {

                                            SnackBar.makeCustomErrorSnack(SignInActivity.this, "Server Maintenance is on Progress");

                                        }
                                    } catch (JSONException e) {

                                        SnackBar.makeCustomErrorSnack(SignInActivity.this, "Server Maintenance is on Progress");

                                    }
                                }

                            }
                        });
                    }
                }
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,ForgetPasswordActivity.class));
            }
        });

    }


    private void initialize() {

        transitionsContainer = (ViewGroup)this. findViewById(R.id.transitions_container);
        letsGetStarted = (LinearLayout)this. transitionsContainer.findViewById(R.id.letsGetStarted2);
        logoLayout = (LinearLayout)this. transitionsContainer.findViewById(R.id.logoLayout);
        editTextLayouts = (LinearLayout)this. transitionsContainer.findViewById(R.id.editTextLayouts);
        SignUp = (TextView)this.findViewById(R.id.signUp);
        progressBar = (ProgressBar)this.findViewById(R.id.pbar);
        contact = (EditText)this.findViewById(R.id.contact);
        password = (EditText)this.findViewById(R.id.password);
        forgetPassword = (TextView)this.findViewById(R.id.forgotPassword);
    }
}