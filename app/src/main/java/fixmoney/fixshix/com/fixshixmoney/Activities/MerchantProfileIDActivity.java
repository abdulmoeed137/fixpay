package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import static fixmoney.fixshix.com.fixshixmoney.Utilities.utils.hideSoftKeyboard;


public class MerchantProfileIDActivity extends AppCompatActivity {

    LinearLayout btn_next;
    EditText id;
    ProgressBar progressBar;
    Context context;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_profile);
        initialize();
        setUpComponents();
    }

    private void setUpComponents() {
        this.context= this;
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.getText().toString().equals("1"))
                {
                    hideSoftKeyboard(MerchantProfileIDActivity.this);
                    SnackBar.makeCustomErrorSnack(context,"You Can't Pay To Admin");
                    return;
                }
                if (Validity.isIdTrue(id.getText().toString(),MerchantProfileIDActivity.this)) {

                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    final String m_id=id.getText().toString();
                    hashMap.put("merchant_id",id.getText().toString());
                    hashMap.put("user_id", new SessionManager(context).getId());

                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        public void run() {

                            final JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.merchant_detail, hashMap,progressBar);

                            if (response != null) {
                                try {
                                    Log.d("checking",response+"");
                                    if (response.names().get(0).equals("success")) {


                                        Intent i = new Intent(context,MerchantProfileActivity.class);


                                        JSONArray data = response.getJSONArray("success");
                                        JSONObject row = data.getJSONObject(0);

                                        String name = row.getString("name");
                                        String image = row.getString("image");
                                        String universal = row.getString("universal");
                                        String discounted = row.getString("discounted");

                                        i.putExtra("merchant_id",m_id);
                                        i.putExtra("merchant_name",name);
                                        i.putExtra("merchant_image",image);
                                        i.putExtra("universal",universal);
                                        i.putExtra("discounted",discounted);

                                       startActivityForResult(i,100);


                                    } else if (response.names().get(0).equals("failed")) {

                                        SnackBar.makeCustomErrorSnack(context, "Invalid ID");


                                    } else {
                                        SnackBar.makeCustomErrorSnack(context, "Server Maintenance is on Progress");

                                    }
                                } catch (JSONException e) {

                                    SnackBar.makeCustomErrorSnack(context, "Server Maintenance is on Progress");
                                }
                            }

                        }
                    });

                }
            }
        });


    }

    private void initialize() {

        btn_next = (LinearLayout)findViewById(R.id.btn_next);
        id = (EditText)findViewById(R.id.txt_id);
        progressBar = (ProgressBar)findViewById(R.id.pbar);
        progressBar.bringToFront();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100)
            if (resultCode == RESULT_OK)
                finish();
    }
}
