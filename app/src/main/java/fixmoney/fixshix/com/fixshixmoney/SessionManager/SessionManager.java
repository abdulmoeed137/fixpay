package fixmoney.fixshix.com.fixshixmoney.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import fixmoney.fixshix.com.fixshixmoney.Constants.Constants;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

/**
 * Created by lenovo on 8/27/2017.
 */

public class SessionManager {
    private String id, name , email , contact, amount;
    SharedPreferences session ;

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
}
