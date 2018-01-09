package fixmoney.fixshix.com.fixshixmoney.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fixmoney.fixshix.com.fixshixmoney.Adapter.MenuListAdapter;
import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.Model.MenutListModel;

import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

import static fixmoney.fixshix.com.fixshixmoney.Utilities.utils.merchant_id;
import static fixmoney.fixshix.com.fixshixmoney.Utilities.utils.mlist;

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

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, CardListActivity.class);
                startActivity(i);

            }
        });

        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", merchant_id);


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {

                JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.all_menu, hashMap, progressBar);

                if (response != null) {
                    try {

                        if (response.names().get(0).equals("success")) {

                            Log.d("errorr",response+"");
                            JSONArray data = response.getJSONArray("success");

                            for (int i = 0 ; i<data.length(); i ++)
                            {
                                 JSONObject row = data.getJSONObject(i);


                                 list.add(new MenutListModel(
                                         row.getString("menu_id"),
                                        row.getString("menu_name"),
                                         row.getString("menu_desc"),
                                         row.getString("menu_price"),
                                         row.getString("merchant_id"),
                                        row.getString("cashback"),
                                         row.getString("menu_image")));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(new MenuListAdapter(context,list));


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
    public static void total_array_item() {

        count.setText(mlist.size()+"");
    }

    private void initialize() {
     listView= (ListView)findViewById(R.id.listView1);
        progressBar = (ProgressBar)this.findViewById(R.id.pbar);
        count = (TextView)this.findViewById(R.id.count);
        basket = (FloatingActionButton)this.findViewById(R.id.basket);
        frame_basket = (FrameLayout)this.findViewById(R.id.frame_basket);





    }


    @Override
    protected void onResume() {
        super.onResume();
        total_array_item();
    }
    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(context)

                .setTitle("Confirm")
                .setMessage("Your Cart Will Be Empty If You Leave?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        mlist.clear();
                        finish();
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
