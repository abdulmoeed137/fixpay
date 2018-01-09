package fixmoney.fixshix.com.fixshixmoney.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fixmoney.fixshix.com.fixshixmoney.Holder.OrderHistoryHolder;
import fixmoney.fixshix.com.fixshixmoney.Holder.RestaurantListHolder;
import fixmoney.fixshix.com.fixshixmoney.Model.MerchantListModel;
import fixmoney.fixshix.com.fixshixmoney.Model.OrderHistoryModel;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

import static fixmoney.fixshix.com.fixshixmoney.R.id.m_id;

/**
 * Created by lenovo on 7/15/2017.
 */

public class OrderHistoryAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<OrderHistoryModel> list = new ArrayList<>();
    Context context;
public OrderHistoryAdapter(Context c, ArrayList<OrderHistoryModel> list )
    {
        inflater = LayoutInflater.from(c);
        this.context = c;
        this.list = list;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final OrderHistoryHolder holder;
        final OrderHistoryModel item = (OrderHistoryModel)getItem(position);

        if (convertView == null)
        {
            holder = new OrderHistoryHolder();

            convertView  = inflater.inflate(R.layout.order_history,null,false);

            holder.marchant_name = (TextView)convertView.findViewById(R.id.m_name);
            holder.time= (TextView)convertView.findViewById(R.id.time);
            holder.status = (TextView)convertView.findViewById(R.id.status);
            holder.amount = (TextView)convertView.findViewById(R.id.amount);
            holder.cashback = (TextView)convertView.findViewById(R.id.cashback);

            convertView.setTag(holder);
        }else
            holder=(OrderHistoryHolder) convertView.getTag();

            holder.marchant_name.setText(item.getMerchant_name());
            holder.time.setText(item.getTime());
            holder.status.setText(item.getStatus());
            holder.amount.setText(item.getAmount());
            holder.cashback.setText(item.getCashback());
        return convertView;
    }
}
