package fixmoney.fixshix.com.fixshixmoney.Model;

/**
 * Created by lenovo on 8/28/2017.
 */

public class MerchantListModel {
    private  String merchant_id,name,email,contact,image,amount;

    public MerchantListModel(String merchant_id, String name , String email, String contact, String image, String amount)
        {
            this.merchant_id = merchant_id;
            this.name = name;
            this.email = email;
            this.contact = contact;
            this.image = image;
            this.amount = amount;
        }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
