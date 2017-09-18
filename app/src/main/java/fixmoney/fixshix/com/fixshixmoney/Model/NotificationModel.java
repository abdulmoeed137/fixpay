package fixmoney.fixshix.com.fixshixmoney.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 8/30/2017.
 */

public class NotificationModel {
    private  String transaction_id,transaction_type, amount,user_name, merchant_name,time, notification;

    public NotificationModel (String transaction_id,String transaction_type, String amount,String user_name,String  merchant_name,String time, String notification)
    {
        this.transaction_id=transaction_id;
        this.transaction_type=transaction_type;
        this.amount=amount;
        this.user_name=user_name;
        this.merchant_name=merchant_name;
        this.notification= notification;
        this.time=time;
    }

    public NotificationModel (Parcel in)
    {
        this.transaction_id=in.readString();
        this.transaction_type=in.readString();
        this.amount=in.readString();
        this.user_name=in.readString();
        this.merchant_name=in.readString();
        this.notification= in.readString();
        this.time=in.readString();
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public String getAmount() {
        return amount;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getTime() {
        return time;
    }

    public String getNotification() {
        return notification;
    }


}
