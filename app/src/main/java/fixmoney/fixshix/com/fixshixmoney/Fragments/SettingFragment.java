package fixmoney.fixshix.com.fixshixmoney.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import fixmoney.fixshix.com.fixshixmoney.Snackbar.SnackBar;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

import static android.content.Context.CLIPBOARD_SERVICE;
import static fixmoney.fixshix.com.fixshixmoney.Constants.Constants.QR_KEY_SHARE;

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
    ImageView  copy ;
    public SettingFragment(){}



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
        if (new SessionManager(context).getQrimage(context) != null )
        {

            this.bitmap =  new SessionManager(context).getQrimage(context);
            qr.setImageBitmap(bitmap);
        }
        else {
            progressBar_qr.setVisibility(View.VISIBLE);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    try {

                        bitmap = StringToQR.TextToImageEncode(QR_KEY_SHARE+new SessionManager(context).getId(), context);

                        new SessionManager(context).setQrimage(bitmap,context);

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
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (qr.getDrawable() != null )
                {
                    utils.ImageDialog(context,bitmap);
                }
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",new SessionManager(context).getId());
                clipboard.setPrimaryClip(clip);
                SnackBar.makeCustomSnack(context,"ID copies to Clipboard");
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
        copy = (ImageView) rootView.findViewById(R.id.copy);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

}

