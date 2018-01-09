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

import fixmoney.fixshix.com.fixshixmoney.Adapter.MerchanttListAdapter;
import fixmoney.fixshix.com.fixshixmoney.Adapter.OrderHistoryAdapter;
import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.Model.MerchantListModel;
import fixmoney.fixshix.com.fixshixmoney.Model.OrderHistoryModel;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;

/**
 * Created by lenovo on 7/15/2017.
 */

public class OrderHistoryActivity extends AppCompatActivity {
    ArrayList<OrderHistoryModel> list = new ArrayList<>();
    ListView listView;
    Context context;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        initialize();
        setUpComponent();
    }

    private void setUpComponent() {
        this.context = this;

        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", (new SessionManager(context).getId()));


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {

                JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.order_history, hashMap, progressBar);

                if (response != null) {
                    try {

                        if (response.names().get(0).equals("success")) {

                            Log.d("errorr",response+"");
                            JSONArray data = response.getJSONArray("success");

                            for (int i = 0 ; i<data.length(); i ++)
                            {
                                String status = "";
                                 JSONObject row = data.getJSONObject(i);
                                if (row.getString("status").equals("0"))
                                {
                                    status = "Pending";
                                }
                                else if (row.getString("status").equals("1"))
                                {
                                    status = "Accepted";
                                }
                                else if (row.getString("status").equals("2"))
                                {
                                    status = "Rejected";
                                }
                                list.add(new OrderHistoryModel(row.getString("order_id"),
                                        row.getString("name"),
                                        row.getString("order_price"),
                                        row.getString("cashback"),
                                        status,
                                        row.getString("time")
                                        ));

                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(new OrderHistoryAdapter(context,list));


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


}
