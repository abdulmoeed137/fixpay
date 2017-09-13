package fixmoney.fixshix.com.fixshixmoney.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fixmoney.fixshix.com.fixshixmoney.Activities.SignInActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.TermsAndConditionsActivity;
import fixmoney.fixshix.com.fixshixmoney.QrCode.StringToQR;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.SessionManager.SessionManager;

/**
 * Created by lenovo on 7/5/2017.
 */

public class SettingFragment extends Fragment {

    View rootView;
    TextView id,name,contact;
    ImageView qr;
    ProgressBar progressBar_qr;
    Bitmap bitmap;
    Context context;
    LinearLayout logout, tnc;

    public SettingFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        {

            //bitmap = savedInstanceState.getParcelable("qr_image");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        initialize();
        setUpComponent();
        return rootView;
    }

    private void setUpComponent() {

        id.setText((new SessionManager(getActivity()).getId()));
        name.setText((new SessionManager(getActivity()).getName()));
        contact.setText((new SessionManager(getActivity()).getContact()));

        if (bitmap != null)
        {
            qr.setImageBitmap(bitmap);
        }
        else {
            progressBar_qr.setVisibility(View.VISIBLE);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    try {

                        bitmap = StringToQR.TextToImageEncode(new SessionManager(context).getId(), getActivity());

                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                qr.setImageBitmap(bitmap);

                                progressBar_qr.setVisibility(View.GONE);
                            }
                        });
                    } catch (WriterException e) {

                    }
                }
            });
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new AlertDialog.Builder(context)

                        .setTitle("Confirm")
                        .setMessage("Are You Sure You Want To Logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                               new SessionManager(context).Logout();
                                ((Activity)context).finish();
                                startActivity(new Intent(context, SignInActivity.class));
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, TermsAndConditionsActivity.class));
            }
        });
    }

    private void initialize() {

        id = (TextView)rootView.findViewById(R.id.id);
        name = (TextView)rootView.findViewById(R.id.name);
        contact = (TextView)rootView.findViewById(R.id.contact);
        qr= (ImageView)rootView.findViewById(R.id.qr);
        progressBar_qr = (ProgressBar)rootView.findViewById(R.id.pbar_qr);
        logout = (LinearLayout)rootView.findViewById(R.id.o3);
        tnc = (LinearLayout)rootView.findViewById(R.id.o2);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putParcelable("qr_image",bitmap);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}

