package fixmoney.fixshix.com.fixshixmoney.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fixmoney.fixshix.com.fixshixmoney.Activities.CardListActivity;
import fixmoney.fixshix.com.fixshixmoney.Activities.MenuListActivity;
import fixmoney.fixshix.com.fixshixmoney.Holder.CardListHolder;
import fixmoney.fixshix.com.fixshixmoney.Holder.MenuListHolder;
import fixmoney.fixshix.com.fixshixmoney.Model.CardListModel;
import fixmoney.fixshix.com.fixshixmoney.Model.MenutListModel;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.Toast.Toast;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

import static android.R.id.list;

/**
 * Created by lenovo on 7/15/2017.
 */

public class CardListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<MenutListModel> mlist = new ArrayList<>();
    Context context;
    int numtest;
public CardListAdapter(Context c, ArrayList<MenutListModel> list )
    {
        inflater = LayoutInflater.from(c);
        this.context = c;
        this.mlist = list;

    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CardListHolder holder;
        final MenutListModel item = (MenutListModel)getItem(position);

        if (convertView == null)
        {
            holder = new CardListHolder();

            convertView  = inflater.inflate(R.layout.card_list_item,null,false);
            holder.menu_name = (TextView)convertView.findViewById(R.id.menu_name);
            holder.menu_desc = (TextView)convertView.findViewById(R.id.menu_desc);
            holder.menu_price = (TextView)convertView.findViewById(R.id.amount);
            holder.minus_order = (ImageView) convertView.findViewById(R.id.minus);
            holder.cashback = (TextView)convertView.findViewById(R.id.cashback);

            convertView.setTag(holder);
        }else
            holder=(CardListHolder) convertView.getTag();

            holder.menu_name.setText(item.getmenu_name());

            holder.menu_desc.setText(item.getmenu_desc());
            holder.menu_price.setText(utils.double2decimal(Double.parseDouble(item.getmenu_price())).toString());
              holder.cashback.setText(item.getCashback());


               holder.minus_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                utils.mlist.remove(position);
                notifyDataSetChanged();
                CardListActivity.total_array_item();
                Toast.makeCustomErrorToast(context,"Item removed");



            }
        });


        return convertView;
    }

}
