package fixmoney.fixshix.com.fixshixmoney.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fixmoney.fixshix.com.fixshixmoney.Holder.NotificationHolder;
import fixmoney.fixshix.com.fixshixmoney.Model.NotificationModel;
import fixmoney.fixshix.com.fixshixmoney.R;

/**
 * Created by lenovo on 7/15/2017.
 */

public class NotificationAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<NotificationModel> list = new ArrayList<>();
    Context context;
public NotificationAdapter(Context c, ArrayList<NotificationModel> list)
    {
        inflater = LayoutInflater.from(c);
        this.list=list;
        context=c;
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
        final NotificationHolder holder;
        final NotificationModel  item = (NotificationModel )getItem(position);

        if (convertView == null)
        {
            holder = new  NotificationHolder ();

            convertView  = inflater.inflate(R.layout.notification_item,null,false);

            holder.notification = (TextView)convertView.findViewById(R.id.notification);
            holder.amount = (TextView)convertView.findViewById(R.id.amount);
            holder.time = (TextView)convertView.findViewById(R.id.time);

            convertView.setTag(holder);
        }else
            holder=(NotificationHolder) convertView.getTag();

        holder.notification.setText(item.getNotification());

        holder.amount.setText(item.getAmount());

        holder.time.setText(item.getTime());

        return convertView;
    }
}
