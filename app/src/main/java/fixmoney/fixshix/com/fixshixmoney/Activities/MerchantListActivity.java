package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.Model.MerchantListModel;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.Adapter.MerchanttListAdapter;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;

/**
 * Created by lenovo on 7/15/2017.
 */

public class MerchantListActivity extends AppCompatActivity {
    ArrayList<MerchantListModel> list = new ArrayList<>();
    ListView listView;
    Context context;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        initialize();
        setUpComponent();
    }

    private void setUpComponent() {
        this.context = this;
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("isClick")) {
               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       if (Double.parseDouble(list.get(position).getAmount())>0.00){
                       Intent i = new Intent(MerchantListActivity.this,FinalShareActivity.class);
                       i.putExtra("merchant_id",(list.get(position).getMerchant_id().toString()));
                           i.putExtra("merchant_name",(list.get(position).getName().toString()));
                       i.putExtra("merchant_amount",(list.get(position).getAmount().toString()));
                       startActivityForResult(i,100);
                       }
                       else
                       {
                           SnackBar.makeCustomErrorSnack(context,"Don't have enough amount to transfer.");
                       }
                   }
               });
            }

        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", (new SessionManager(context).getId()));


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {

                JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.all_merchant, hashMap, progressBar);

                if (response != null) {
                    try {

                        if (response.names().get(0).equals("success")) {
                            Double amount =0.0;
                            Log.d("errorr",response+"");
                            JSONArray data = response.getJSONArray("success");

                            for (int i = 0 ; i<data.length(); i ++)
                            {
                                 JSONObject row = data.getJSONObject(i);

                                 amount = amount+Double.parseDouble(row.getString("amount"));
                                 list.add(new MerchantListModel(
                                        row.getString("merchant_id"),
                                         row.getString("name"),
                                         row.getString("email"),
                                         row.getString("contact"),
                                         row.getString("image"),
                                         row.getString("amount")
                                 ));
                            }

                            final Double finalAmount = amount;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(new MerchanttListAdapter(context,list));
                                    new SessionManager().setAmount(context, finalAmount.toString());

                                }
                            });

                        } else if (response.names().get(0).equals("failed")) {

                            SnackBar.makeCustomErrorSnack(context, response.getString("failed"));

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

    private void initialize() {
     listView= (ListView)findViewById(R.id.listView);
        progressBar = (ProgressBar)this.findViewById(R.id.pbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == 100)
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }  }
}
