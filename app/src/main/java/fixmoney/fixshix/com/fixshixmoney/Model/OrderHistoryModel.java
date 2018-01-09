package fixmoney.fixshix.com.fixshixmoney.Model;

/**
 * Created by lenovo on 11/30/2017.
 */

public class OrderHistoryModel {

    private  String order_id, merchant_name, amount, cashback,status,time;

    public OrderHistoryModel(String order_id, String merchant_name, String amount, String cashback, String status, String time) {
        this.order_id = order_id;
        this.merchant_name = merchant_name;
        this.amount = amount;
        this.cashback = cashback;
        this.status = status;
        this.time = time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getAmount() {
        return amount;
    }

    public String getCashback() {
        return cashback;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }
}
