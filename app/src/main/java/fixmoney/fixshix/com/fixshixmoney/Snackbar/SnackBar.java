package fixmoney.fixshix.com.fixshixmoney.Snackbar;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import fixmoney.fixshix.com.fixshixmoney.R;

/**
 * Created by lenovo on 8/26/2017.
 */

public class SnackBar {
    public static void makeCustomErrorSnack(Context c , String msg){
        View v = ((Activity)c).getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(c, R.color.fbutton_color_pomegranate));
        snackbar.show();

    }
    public static void makeCustomSnack(Context c , String msg){
        View v = ((Activity)c).getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(c, R.color.mainColor));
        snackbar.show();
    }

}
