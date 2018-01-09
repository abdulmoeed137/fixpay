package fixmoney.fixshix.com.fixshixmoney.Model;

/**
 * Created by lenovo on 8/28/2017.
 */

public class MenutListModel {
    private  String menu_id,menu_name,menu_desc,menu_price,merchant_id,cashback, image;

    public MenutListModel(String menu_id, String menu_name, String menu_desc, String menu_price, String merchant_id, String cashback,String image)
        {
            this.menu_id = menu_id;
            this.menu_name = menu_name;
            this.menu_desc = menu_desc;
            this.menu_price = menu_price;
            this.merchant_id = merchant_id;
            this.cashback = cashback;
            this.image= image;
        }

    public String getmenu_id() {
        return menu_id;
    }

    public void setmenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getmenu_name() {
        return menu_name;
    }

    public void setmenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getmenu_desc() {
        return menu_desc;
    }

    public void setmenu_desc(String menu_desc) {
        this.menu_desc = menu_desc;
    }

    public String getmenu_price() {
        return menu_price;
    }

    public void setmenu_price(String menu_price) {
        this.menu_price = menu_price;
    }

    public String getmerchant_id() {
        return merchant_id;
    }

    public void setmerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }


    public String getCashback() {
        return cashback;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
