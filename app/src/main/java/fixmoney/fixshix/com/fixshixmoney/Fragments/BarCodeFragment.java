package fixmoney.fixshix.com.fixshixmoney.Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fixmoney.fixshix.com.fixshixmoney.Activities.UserProfileIDActivity;
import fixmoney.fixshix.com.fixshixmoney.BarCodeClasses.Result;
import fixmoney.fixshix.com.fixshixmoney.BarCodeClasses.ZBarScannerView;
import fixmoney.fixshix.com.fixshixmoney.Activities.MerchantProfileActivity;
import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.HttpRequest.HttpRequest;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;

import static fixmoney.fixshix.com.fixshixmoney.Utilities.utils.scan_flow;


public class BarCodeFragment extends Fragment implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    Context context;
    ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZBarScannerView(getActivity());
        context = getActivity();
        initialize();


        return mScannerView;
    }

    private void initialize() {
        progressBar= (ProgressBar)getActivity().findViewById(R.id.pbar);
        progressBar.bringToFront();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 100);}
        else
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }


    @Override
    public void handleResult(Result rawResult) {
        if (scan_flow.equals("share"))
        {
            scan_flow="";
            share_qr(rawResult.getContents());
        }
        else if (scan_flow.equals("receive"))
        {
            scan_flow="";
            debit_qr(rawResult.getContents());
        }
        else
        getItemDetails(rawResult.getContents());
    }

    private void debit_qr(String id) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("qr",id);
        hashMap.put("user_id", new SessionManager(getActivity()).getId());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {

                final JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.use_qr, hashMap,progressBar);

                if (response != null) {
                    try {
                        Log.d("checking",response+"");
                        if (response.names().get(0).equals("success")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    SnackBar.makeCustomSnack(getActivity(), "Successfully Proceed.");

                                    Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            getActivity().finish();
                                        }
                                    }, 1000);
                                }
                            });

                        } else if (response.names().get(0).equals("failed")) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    try {
                                        SnackBar.makeCustomErrorSnack(context, response.getString("failed"));
                                    }
                                    catch(Exception e ) {}
                                    Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            getActivity().finish();
                                        }
                                    }, 1000);
                                }
                            });



                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    SnackBar.makeCustomErrorSnack(context, "Server Maintenance is on Progress");

                                    Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            getActivity().finish();
                                        }
                                    }, 1000);
                                }
                            });


                        }
                    } catch (JSONException e) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                SnackBar.makeCustomErrorSnack(context, "Server Maintenance is on Progress");

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        getActivity().finish();
                                    }
                                }, 1000);
                            }
                        });


                    }
                }

            }
        });

    }

    private void share_qr(final String id) {
        Intent i = new Intent(getActivity(), UserProfileIDActivity.class);
        i.putExtra("id",id.toString());
        startActivity(i); getActivity().finish();
    }


    public void getItemDetails(final String id){

        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("merchant_id",id);
        hashMap.put("user_id", new SessionManager(getActivity()).getId());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            public void run() {

                final JSONObject response = HttpRequest.SyncHttpRequest(context, Constants.merchant_detail, hashMap,progressBar);

                if (response != null) {
                    try {
                        Log.d("checking",response+"");
                        if (response.names().get(0).equals("success")) {


                            Intent i = new Intent(getActivity(),MerchantProfileActivity.class);


                            JSONArray data = response.getJSONArray("success");
                            JSONObject row = data.getJSONObject(0);

                            String name = row.getString("name");
                            String image = row.getString("image");
                            String universal = row.getString("universal");
                            String discounted = row.getString("discounted");

                            i.putExtra("merchant_id",id);
                            i.putExtra("merchant_name",name);
                            i.putExtra("merchant_image",image);
                            i.putExtra("universal",universal);
                            i.putExtra("discounted",discounted);

                            startActivity(i);
                            getActivity().finish();

                        } else if (response.names().get(0).equals("failed")) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    try {
                                        SnackBar.makeCustomErrorSnack(context, response.getString("failed"));
                                    }
                                    catch(Exception e ) {}
                                    Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            getActivity().finish();
                                        }
                                    }, 1000);
                                }
                            });


                        } else {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    SnackBar.makeCustomErrorSnack(context, "Server Maintenance is on Progress");

                                    Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            getActivity().finish();
                                        }
                                    }, 1000);
                                }
                            });

                        }
                    } catch (JSONException e) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                SnackBar.makeCustomErrorSnack(context, "Server Maintenance is on Progress");

                                Handler handler = new Handler();

                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        getActivity().finish();
                                    }
                                }, 1000);
                            }
                        });

                    }
                }

            }
        });


  }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                onResume();
            } else SnackBar.makeCustomSnack(getActivity(),"Permission Required to Access Camera");
        }

    }

}
