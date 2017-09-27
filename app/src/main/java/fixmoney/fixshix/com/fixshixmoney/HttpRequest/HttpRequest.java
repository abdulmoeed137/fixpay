package fixmoney.fixshix.com.fixshixmoney.HttpRequest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import fixmoney.fixshix.com.fixshixmoney.Activities.UserProfileIDActivity;
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

import static fixmoney.fixshix.com.fixshixmoney.Utilities.utils.hideSoftKeyboard;


/**
 * Created by lenovo on 8/26/2017.
 */

public class HttpRequest {

    static public  JSONObject SyncHttpRequest (final Context c, String url, HashMap<String,String> hashMap, final ProgressBar progressBar){
        ((Activity)c).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideSoftKeyboard(((Activity)c));
                progressBar.setVisibility(View.VISIBLE);
                ((Activity)c) .getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        hashMap.put("hash","starlet");
        String URL = utils.URLwithParams(url,hashMap);
        Log.d( "SyncHttpRequest: ",URL);
        JSONObject response = null;
        final RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(c);
        int REQUEST_TIMEOUT = 30;
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(URL, new JSONObject(), future, future);
        int socketTimeout = 3000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);

         try {
             response = future.get(REQUEST_TIMEOUT,TimeUnit.SECONDS); // this will block (forever)
        } catch (InterruptedException e) {
             SnackBar.makeCustomErrorSnack(c,"Connection Error");
            // exception handling
        } catch (ExecutionException e) {
             SnackBar.makeCustomErrorSnack(c,"Connection Error");

             // exception handling
        } catch (TimeoutException e) {
             SnackBar.makeCustomErrorSnack(c,"Connection Timeout");

         }
        ((Activity)c).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                ((Activity)c).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });

        return response;
    }

//    static public JSONObject Request (final Context c, final ProgressBar progress, String URL, final HashMap<String, String> hashMap ){
//       progress.setVisibility(View.VISIBLE);
//        RequestFuture<JSONObject> future = RequestFuture.newFuture();
//        final JSONObject[] response_data = {null};
//        final RequestQueue requestQueue;
//        requestQueue = Volley.newRequestQueue(c);
//        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,  URL,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                    response_data[0] = response;
//                        Log.d("demo_new_volley", response+"");
//                        progress.setVisibility(View.GONE);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //    Log.d("err",error.getMessage());
//                        progress.setVisibility(View.GONE);
//                        response_data[0]=null;
//                        SnackBar.makeCustomErrorSnack(c,"Couldnt Load Data");
//                    }
//                })
//                {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                      //  Log.d(TAG, "getParams: ");
//                     return hashMap;
//                    }};
//
//        int socketTimeout = 3000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        requestQueue.add(jsonObjectRequest);
//        return response_data[0];
//    }
}
