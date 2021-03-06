package fixmoney.fixshix.com.fixshixmoney.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.Model.NotificationModel;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.Adapter.NotificationAdapter;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;

/**
 * Created by lenovo on 7/5/2017.
 */

public class NotificationFragment  extends Fragment {
    View rootView;
    ListView listView;
    Context context;
    ProgressBar progressBar;
    ArrayList<NotificationModel> list = new ArrayList<>();
    SwipeRefreshLayout switeRefreshLayout ;
    TextView empty_notification;
    public NotificationFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new SessionManager(context).getList(context) != null )
        {

            list = new SessionManager(context).getList(context);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        initialize();
        setUpComponent();
        return rootView;
    }
    private void setUpComponent() {



        if (list.isEmpty())
        updateList();
        else
        listView.setAdapter(new NotificationAdapter(context,list));

        switeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               list.clear();
                switeRefreshLayout.setRefreshing(true);
                updateList();
                switeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void updateList() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", (new SessionManager(context).getId()));


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {

                JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.notification, hashMap, progressBar);

                if (response != null) {
                    try {

                        if (response.names().get(0).equals("success")) {
                            empty_notification.setVisibility(View.GONE);
                            Double amount2 =0.0;
                            Log.d("errorr",response+"");
                            JSONArray data = response.getJSONArray("success");

                            for (int i = 0 ; i<data.length(); i ++)
                            {
                                JSONObject row = data.getJSONObject(i);
                                amount2 = amount2+Double.parseDouble(row.getString("amount"));
                                String transaction_id = row.getString("transaction_id");
                                String transaction_type = row.getString("transaction_type");
                                String amount = row.getString("amount");
                                String user_name = row.getString("user_name");
                                String merchant_name = row.getString("merchant_name");
                                String time = row.getString("time");
                                String other_user = row.getString("other_user_name");
                                String isUser = row.getString("isUser");
                                String isMerchant = row.getString("isMerchant");
                                String notification ="";
                                Double t_amount = Double.parseDouble(amount.trim());
                                String merchant_id = row.getString("merchant_id");

                                if (isUser.equals("1"))
                                {
                                    if (t_amount<0)
                                    {
                                        notification="You Shared With "+other_user+" in "+merchant_name+" Account.";
                                    }
                                    else
                                        notification=other_user+" Shared with You in "+merchant_name+" Account";

                                }
                                if (isMerchant.equals("1"))
                                {
                                    if (t_amount<0)
                                    {
                                        if (merchant_id.equals("1"))
                                        {
                                            notification = "You Request to withdraw fund";
                                        }else
                                        notification="You Paid "+merchant_name;
                                    }
                                    else
                                        notification="You Received From "+ merchant_name;
                                }

                                list.add(new NotificationModel(transaction_id,
                                        transaction_type,
                                        amount,
                                        user_name,
                                        merchant_name,
                                        time,
                                        notification


                                ));
                            }

                            new SessionManager(context).setList(list,context);
                            final Double finalAmount = amount2;
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    listView.setAdapter(new NotificationAdapter(context,list));
                                    new SessionManager().setAmount(context, finalAmount.toString());
                                }
                            });

                        } else if (response.names().get(0).equals("failed")) {

                          //  SnackBar.makeCustomErrorSnack(context, response.getString("failed"));
                            ((Activity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    empty_notification.setVisibility(View.VISIBLE);
                                }
                            });

                        } else {

                            SnackBar.makeCustomErrorSnack(context, "Server Maintenance is on Progress");

                        }
                    } catch (JSONException e) {

                        SnackBar.makeCustomErrorSnack(context, "Server Maintenance is on Progress");
                        Log.d(e.getMessage()+"", "run: ");

                    }
                }

            }
        });

    }

    private void initialize() {
        listView = (ListView)rootView.findViewById(R.id.listView);
        progressBar = (ProgressBar)rootView.findViewById(R.id.pbar);
        progressBar.bringToFront();
        switeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefresh);
        empty_notification = (TextView)rootView.findViewById(R.id.empty_noti);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
