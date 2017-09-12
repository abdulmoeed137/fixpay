package fixmoney.fixshix.com.fixshixmoney.Encryption;



/**
 * Created by lenovo on 8/29/2017.
 */

public class Encryption {

    static  public  String Decrypt (String qr_result)
    {
       // qr_result = Add_Extra_Key(qr_result);

        return  qr_result;

    }

    private  static double Extra_Key(double i)
    {
        return  (((i*34/2)*437826193)/977*1928883);

    }

}
