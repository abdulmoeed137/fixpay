package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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

import fixmoney.fixshix.com.fixshixmoney.Adapter.CardListAdapter;
import fixmoney.fixshix.com.fixshixmoney.Adapter.MenuListAdapter;
import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.Model.MenutListModel;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;
import fixmoney.fixshix.com.fixshixmoney.Validity.Validity;

import static fixmoney.fixshix.com.fixshixmoney.Utilities.utils.merchant_id;
import static fixmoney.fixshix.com.fixshixmoney.Utilities.utils.mlist;

/**
 * Created by lenovo on 7/15/2017.
 */

public class CardListActivity extends AppCompatActivity {


    ListView listView;
    Context context;
    ProgressBar progressBar;
    TextView  btn_placeOrder;
   static public TextView total_amount ;
    static public TextView total_cashback ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        initialize();
        setUpComponent();
    }

    private void setUpComponent() {

        btn_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mlist.size()>0)
                {
                    String item_id="";

                        final HashMap<String, String> hashMap = new HashMap<String, String>();

                        hashMap.put("user_id", new SessionManager(context).getId().toString());
                        hashMap.put("price", total_amount.getText().toString());
                        hashMap.put("cashback", total_cashback.getText().toString());

                        for (int i=0;i<mlist.size();i++)
                        {
                            if (i == mlist.size()-1){
                                item_id = item_id+mlist.get(i).getmenu_id();
                            }
                            else
                            item_id = item_id+mlist.get(i).getmenu_id()+"-";
                        }

                        hashMap.put("item_id",item_id);
                        hashMap.put("merchant_id",merchant_id);

                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.new_order, hashMap, progressBar);


                            if (response != null) {
                                try {

                                    Log.d("yoyo",response+"");
                                    if (response.names().get(0).equals("success")) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                SnackBar.makeCustomSnack(context,"Order Placed Successfully");

                                                Handler handler = new Handler();

                                                handler.postDelayed(new Runnable() {
                                                    public void run() {
                                                        mlist.clear();
                                                        finish();
                                                    }
                                                }, 1000);
                                            }
                                        });

                                    } else if (response.names().get(0).equals("failed")) {

                                        SnackBar.makeCustomErrorSnack(context, response.getString("failed"));

                                    } else {

                                        SnackBar.makeCustomErrorSnack(context, response.getString("failed"));
                                    }
                                } catch (JSONException e) {

                                    SnackBar.makeCustomErrorSnack(context, "System Maintenance on Progress. Try bit Later ");

                                }
                            }

                        }
                    });
                }
else
                {
                    fixmoney.fixshix.com.fixshixmoney.Toast.Toast.makeCustomToast(context,"Cart is empty");
                }




            }
        });

        total_array_item();
        listView.setAdapter(new CardListAdapter(context,mlist));





    }

    public static void total_array_item() {
        Double x = 0.0; Double y = 0.0;
        for (int i = 0 ; i <mlist.size(); i++)
        {
            x = x+ Integer.parseInt(mlist.get(i).getmenu_price());
            y = y + Integer.parseInt(mlist.get(i).getCashback());
        }
        total_amount.setText(x+"");
        total_cashback.setText(y+"");
    }

    private void initialize()
    {

          this.context = this;
         listView= (ListView)findViewById(R.id.listView2);
        progressBar = (ProgressBar)this.findViewById(R.id.pbar);
        total_amount = (TextView)this.findViewById(R.id.total_amount);
        total_cashback = (TextView)this.findViewById(R.id.total_cashback);
        btn_placeOrder = (TextView)this.findViewById(R.id.place_order);
    }


}
