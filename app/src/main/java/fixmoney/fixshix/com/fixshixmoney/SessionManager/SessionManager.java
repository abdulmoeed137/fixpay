package fixmoney.fixshix.com.fixshixmoney.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.Model.NotificationModel;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

/**
 * Created by lenovo on 8/27/2017.
 */

public class SessionManager {
    private String id, name , email , contact, amount;
    SharedPreferences session ;
    private String  qrimage;
    private ArrayList<NotificationModel> list = new ArrayList<>();
    public SessionManager(){}
    public  SessionManager (Context c)
    {
        session= c.getSharedPreferences(Constants.SESSION,Context.MODE_PRIVATE);
        this. id = session.getString("id",null);
        this.email= session.getString("email",null);
        this.name = session.getString("name",null);
        this.contact = session.getString("contact",null);
        this.amount= session.getString("amount",null);
    }


    public  SessionManager(Context c ,String id, String name , String email, String contact,String amount)
        {
            session= c.getSharedPreferences(Constants.SESSION,Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = session.edit();
            editor.putString("id", id);
            editor.putString("name",name);
            editor.putString("email",email);
            editor.putString("contact",contact);
            editor.putString("amount",amount);
            editor.commit();

            new SessionManager(c);

        }

public boolean CheckIfSessionExist(){
    if (session.contains("id"))
        return true;
    else
        return  false;

}
    public void Logout (){
        SharedPreferences.Editor editor = session.edit();
        editor.clear();
        editor.commit();

        this.id=null;
        this.name=null;
        this.email=null;
        this.contact=null;
        this.amount=null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getAmount() {
        String s = utils.double2decimal((Double.parseDouble(amount))).toString();
        return s;
    }

    public void setAmount(Context c,String amount) {

        session= c.getSharedPreferences(Constants.SESSION,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = session.edit();
        editor.putString("amount", amount);
        editor.commit();

    }



    public Bitmap  getQrimage(Context c) {
        session= c.getSharedPreferences(Constants.SESSION,Context.MODE_PRIVATE);
       qrimage = session.getString("qrimage",null);
        try {
            return utils.decodeBase64(qrimage);
        }
        catch ( Exception e){
            return null;}
    }

    public void setQrimage(Bitmap qrimage,Context c) {
        session= c.getSharedPreferences(Constants.SESSION,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        editor.putString("qrimage", utils.encodeTobase64(qrimage));
        editor.commit();
    }

    public ArrayList<NotificationModel> getList(Context c) {
        session= c.getSharedPreferences(Constants.SESSION,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = session.getString("notification_list", null);
        Type type = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
        ArrayList<NotificationModel> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    public void setList(ArrayList<NotificationModel> list, Context c ) {
        session= c.getSharedPreferences(Constants.SESSION,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = session.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("notification_list", json);
        editor.commit();
    }
}
