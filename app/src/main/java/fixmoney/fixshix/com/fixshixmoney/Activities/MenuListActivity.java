package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fixmoney.fixshix.com.fixshixmoney.Adapter.MenuListAdapter;
import fixmoney.fixshix.com.fixshixmoney.Adapter.MerchanttListAdapter;
import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.Holder.MenuListHolder;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.Model.MenutListModel;

import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;

/**
 * Created by lenovo on 7/15/2017.
 */

public class MenuListActivity extends AppCompatActivity {
    ArrayList<MenutListModel> list = new ArrayList<>();
    ListView listView;
    Context context;
    FloatingActionButton basket;
    ProgressBar progressBar;
    FrameLayout frame_basket;
    public static TextView count;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        initialize();
        setUpComponent();
    }

    private void setUpComponent() {
        this.context = this;

        Intent intent = getIntent();
        String id = intent.getStringExtra("Merchant_id");

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, CardListActivity.class);
                startActivity(i);





            }
        });






        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("isClick")) {
               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       if (Double.parseDouble(list.get(position).getmenu_price())>0.00){
                       Intent i = new Intent(MenuListActivity.this,FinalShareActivity.class);
                       i.putExtra("menu_name",(list.get(position).getmenu_name().toString()));
                           i.putExtra("menu_desc",(list.get(position).getmenu_desc().toString()));
                       i.putExtra("menu_price",(list.get(position).getmenu_price().toString()));
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
        hashMap.put("id", id);


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {

                JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.all_menu, hashMap, progressBar);

                if (response != null) {
                    try {

                        if (response.names().get(0).equals("success")) {
                            Double menu_price =0.0;
                            Log.d("errorr",response+"");
                            JSONArray data = response.getJSONArray("success");

                            for (int i = 0 ; i<data.length(); i ++)
                            {
                                 JSONObject row = data.getJSONObject(i);

                                menu_price = menu_price+Double.parseDouble(row.getString("menu_price"));
                                 list.add(new MenutListModel(
                                         row.getString("menu_id"),
                                        row.getString("menu_name"),
                                         row.getString("menu_desc"),
                                         row.getString("menu_price"),
                                         row.getString("merchant_id")

                                 ));
                            }
                            final Double finalAmount = menu_price;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(new MenuListAdapter(context,list));
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
     listView= (ListView)findViewById(R.id.listView1);
        progressBar = (ProgressBar)this.findViewById(R.id.pbar);
        count = (TextView)this.findViewById(R.id.count);
        basket = (FloatingActionButton)this.findViewById(R.id.basket);
        frame_basket = (FrameLayout)this.findViewById(R.id.frame_basket);





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
