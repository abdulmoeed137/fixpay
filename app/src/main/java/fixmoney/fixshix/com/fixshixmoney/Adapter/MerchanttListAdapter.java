package fixmoney.fixshix.com.fixshixmoney.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fixmoney.fixshix.com.fixshixmoney.Holder.RestaurantListHolder;
import fixmoney.fixshix.com.fixshixmoney.Model.MerchantListModel;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

/**
 * Created by lenovo on 7/15/2017.
 */

public class MerchanttListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<MerchantListModel> list = new ArrayList<>();
    Context context;
public MerchanttListAdapter(Context c, ArrayList<MerchantListModel> list )
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
        final RestaurantListHolder holder;
        final MerchantListModel item = (MerchantListModel)getItem(position);

        if (convertView == null)
        {
            holder = new RestaurantListHolder();

            convertView  = inflater.inflate(R.layout.restaurant_list_item,null,false);

            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.email= (TextView)convertView.findViewById(R.id.email);
            holder.contact = (TextView)convertView.findViewById(R.id.contact);
            holder.image = (ImageView)convertView.findViewById(R.id.image);
            holder.amount = (TextView)convertView.findViewById(R.id.amount);
            holder.m_id = (TextView)convertView.findViewById(R.id.m_id);

            convertView.setTag(holder);
        }else
            holder=(RestaurantListHolder) convertView.getTag();

            holder.name.setText(item.getName());
            holder.email.setText(item.getEmail());
            holder.contact.setText(item.getContact());
            holder.amount.setText(utils.double2decimal(Double.parseDouble(item.getAmount())).toString());
            holder.m_id.setText(item.getMerchant_id());
        utils.LoadImageFromURL(context,item.getImage(),holder.image);

        return convertView;
    }
}
