package fixmoney.fixshix.com.fixshixmoney.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fixmoney.fixshix.com.fixshixmoney.Activities.ForgetPasswordActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.FundWithdrawActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.UploadFundsActivity;
import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.DialogBox;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.Activities.MerchantListActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.ScanActivity;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;


/**
 * Created by lenovo on 7/5/2017.
 */

public class FixMoneyFragment  extends Fragment {
    View rootView;
  LinearLayout tab2,share, pay, merchant, upload_fund, withdraw_fund;
    TextView amount ;
    ImageButton refresh;
    Context context;
    ProgressBar progressBar;

    public FixMoneyFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_fixmoney, container, false);
        initialize();
        setUpComponent();
        return rootView;
    }
    private void setUpComponent() {
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                utils.scan_flow="receive";
                startActivity(new Intent(getActivity(), ScanActivity.class));
            }
        });

        merchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MerchantListActivity.class));
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogBox().showDialog(getActivity());
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new DialogBox().showDialogPay(getActivity());
            }
        });

        upload_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),UploadFundsActivity.class));
            }
        });
     //  amount.setText(new SessionManager(getActivity()).getAmount());

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final HashMap<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("id",new SessionManager(context).getId());

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.total_transaction, hashMap, progressBar);


                        if (response != null) {
                            try {

                                Log.d("yoyo",response+"");
                                if (response.names().get(0).equals("success")) {

                                    new SessionManager(context).setAmount(context,response.getString("success"));
                                    ((Activity)context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            amount.setText(new SessionManager(context).getAmount());
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
        });

        withdraw_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, FundWithdrawActivity.class));
            }
        });
    }

    private void initialize() {

        tab2 = (LinearLayout)rootView.findViewById(R.id.tab2);
        share= (LinearLayout)rootView.findViewById(R.id.share);
        pay= (LinearLayout)rootView.findViewById(R.id.pay);
        amount = (TextView)rootView.findViewById(R.id.amount);
     merchant= (LinearLayout)rootView.findViewById(R.id.merchant);
        upload_fund = (LinearLayout)rootView.findViewById(R.id.o2);
        refresh= (ImageButton)rootView.findViewById(R.id.refresh);
        progressBar = (ProgressBar)rootView.findViewById(R.id.pbar);
        withdraw_fund = (LinearLayout)rootView.findViewById(R.id.o3);

    }

    @Override
    public void onResume() {
        super.onResume();
        amount.setText(new SessionManager(getActivity()).getAmount());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
}
