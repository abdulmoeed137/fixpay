package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
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

import static fixmoney.fixshix.com.fixshixmoney.Utilities.utils.hideSoftKeyboard;


public class UserProfileIDActivity extends AppCompatActivity {

    LinearLayout btn_find, btn_next;
    EditText id;
    ProgressBar progressBar;
    TextView txt_id,txt_name,txt_email,txt_contact;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initialize();
        setUpComponents();
    }

    private void setUpComponents() {
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("id")) {
                String c_id = getIntent().getExtras().getString("id");
                     id.setText(c_id);


            }}

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validity.isIdTrue(id.getText().toString(),UserProfileIDActivity.this))
                    {
                        if (id.getText().toString().equals(new SessionManager(UserProfileIDActivity.this).getId()))
                        {
                            try{  hideSoftKeyboard(UserProfileIDActivity.this);}
                            catch (Exception e){}
                            SnackBar.makeCustomErrorSnack(UserProfileIDActivity.this,"You Can't Share With Your Self");


                        }
                        else{
                        final HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("id", id.getText().toString());

                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(new Runnable() {
                            public void run() {
                                JSONObject response = HttpRequest.SyncHttpRequest(UserProfileIDActivity.this, Constants.user_details, hashMap, progressBar);
                                Log.d(response+"", "run: ");
                                try {
                                    if (response.names().get(0).equals("success")) {

                                        JSONArray data = response.getJSONArray("success");
                                        JSONObject row = data.getJSONObject(0);

                                       utils.id = row.getString("user_id");
                                       utils.name = row.getString("name");
                                        utils.email = row.getString("email");
                                        utils. contact = row.getString("contact");

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                txt_id.setText(utils.id);
                                                txt_name.setText(utils.name);
                                                txt_email.setText(utils.email);
                                                txt_contact.setText(utils.contact);

                                                btn_next.setVisibility(View.VISIBLE);
                                            }
                                        });


                                    }
                                    else if (response.names().get(0).equals("failed"))
                                    {
                                        SnackBar.makeCustomErrorSnack(UserProfileIDActivity.this, "Account not found");
                                    }
                                }catch (Exception e)
                                    {
                                        SnackBar.makeCustomErrorSnack(UserProfileIDActivity.this, "Server Maintenance is on Progress");

                                    }

                            }});
                            }}
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileIDActivity.this,MerchantListActivity.class);
                i.putExtra("isClick","true");
                startActivityForResult(i,100);
            }
        });

    }

    private void initialize() {

        btn_find = (LinearLayout)findViewById(R.id.btn_find);
        btn_next = (LinearLayout)findViewById(R.id.btn_next);
        id = (EditText)findViewById(R.id.txt_id);
        progressBar = (ProgressBar)findViewById(R.id.pbar);
        progressBar.bringToFront();
        txt_id = (TextView)findViewById(R.id.id);
        txt_name =(TextView)findViewById(R.id.name);
        txt_contact=(TextView)findViewById(R.id.contact);
        txt_email =(TextView)findViewById(R.id.email);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100)
            if (resultCode == RESULT_OK) {
                finish();
            }
    }
}
