package fixmoney.fixshix.com.fixshixmoney.Adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fixmoney.fixshix.com.fixshixmoney.Activities.MenuListActivity;
import fixmoney.fixshix.com.fixshixmoney.Holder.MenuListHolder;

import fixmoney.fixshix.com.fixshixmoney.Model.MenutListModel;
import fixmoney.fixshix.com.fixshixmoney.R;
import fixmoney.fixshix.com.fixshixmoney.Utilities.utils;

/**
 * Created by lenovo on 7/15/2017.
 */

public class MenuListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<MenutListModel> list = new ArrayList<>();
    Context context;
    int numtest;
public MenuListAdapter(Context c, ArrayList<MenutListModel> list )
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
        final MenuListHolder holder;
        final MenutListModel item = (MenutListModel)getItem(position);

        if (convertView == null)
        {
            holder = new MenuListHolder();

            convertView  = inflater.inflate(R.layout.menu_list_item,null,false);
            holder.menu_name = (TextView)convertView.findViewById(R.id.menu_name);
            holder.menu_desc = (TextView)convertView.findViewById(R.id.menu_desc);
            holder.menu_price = (TextView)convertView.findViewById(R.id.amount);
            holder.add_order = (ImageView) convertView.findViewById(R.id.add);


            convertView.setTag(holder);
        }else
            holder=(MenuListHolder) convertView.getTag();

            holder.menu_name.setText(item.getmenu_name());

            holder.menu_desc.setText(item.getmenu_desc());
            holder.menu_price.setText(utils.double2decimal(Double.parseDouble(item.getmenu_price())).toString());
           /* holder.m_id.setText(item.getMerchant_id());*/
            /* utils.LoadImageFromURL(context,item.getImage(),holder.image);*/


        holder.add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                utils.mlist.add(new MenutListModel(item.getmenu_id(),item.getmenu_name(),item.getmenu_desc(),item.getmenu_price(),item.getmerchant_id()));
                    /*display.setText("" + numtest);
*/
                MenuListActivity.count.setText(utils.mlist.size()+"");


            }
        });


        return convertView;
    }

}
