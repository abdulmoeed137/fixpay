package fixmoney.fixshix.com.fixshixmoney.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fixmoney.fixshix.com.fixshixmoney.Activities.FinalShareActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.MenuListActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.MerchantListActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.OrderHistoryActivity;
import fixmoney.fixshix.com.fixshixmoney.Adapter.MerchanttListAdapter;
import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.Model.MerchantListModel;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lenovo on 7/5/2017.
 */

public class FixshixFragment extends Fragment
{

    ArrayList<MerchantListModel> list = new ArrayList<>();
    ListView listView;
    Context context;
    ProgressBar progressBar;
    FloatingActionButton basket;

    View rootView;

   public FixshixFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_merchant_list, container, false);
     initialize();
        setUpComponent();
    return rootView;
    }

    private void setUpComponent() {

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(context, OrderHistoryActivity.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



         Intent i = new Intent(context, MenuListActivity.class);
                utils.merchant_id = list.get(position).getMerchant_id();
                startActivity(i);

            }
        });


        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", (new SessionManager(context).getId()));


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {

                JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.all_merchant, hashMap, progressBar);

                if (response != null) {
                    try {

                        if (response.names().get(0).equals("success")) {

                            Log.d("errorr",response+"");
                            JSONArray data = response.getJSONArray("success");

                            for (int i = 1 ; i<data.length(); i ++)
                            {
                                JSONObject row = data.getJSONObject(i);


                                list.add(new MerchantListModel(
                                        row.getString("merchant_id"),
                                        row.getString("name"),
                                        row.getString("email"),
                                        row.getString("contact"),
                                        row.getString("image"),
                                        row.getString("amount")
                                ));
                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listView.setAdapter(new MerchanttListAdapter(context,list));


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

    private void initialize()
    {
        listView= (ListView)rootView.findViewById(R.id.listView);
        progressBar = (ProgressBar)rootView.findViewById(R.id.pbar);
        progressBar.bringToFront();
        basket = (FloatingActionButton)rootView.findViewById(R.id.basket);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
