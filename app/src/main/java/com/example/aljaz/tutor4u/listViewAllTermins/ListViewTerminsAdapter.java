package com.example.aljaz.tutor4u.listViewAllTermins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aljaz.tutor4u.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListViewTerminsAdapter  extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<ModelAllTermins> modelAllTerminsList;
    ArrayList<ModelAllTermins> modelAllTerminsArrayList;

    public ListViewTerminsAdapter(Context mContext, List<ModelAllTermins> modelAllTerminsList) {
        context = mContext;
        layoutInflater = LayoutInflater.from(context);
        this.modelAllTerminsList = modelAllTerminsList;
        this.modelAllTerminsArrayList = new ArrayList<>();
        this.modelAllTerminsArrayList.addAll(modelAllTerminsList);
    }

    public class ViewHolder{
        TextView tutorName, date, price;

    }

    @Override
    public int getCount() {
        return modelAllTerminsList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelAllTerminsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.termin_row, null);

            holder.tutorName = convertView.findViewById(R.id.termin_row_tutorName);
            holder.date = convertView.findViewById(R.id.termin_row_date);
            holder.price = convertView.findViewById(R.id.termin_row_price);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String finalDate = dt1.format(modelAllTerminsArrayList.get(position).getDate());

        holder.tutorName.setText(modelAllTerminsList.get(position).getTutorName());
        holder.date.setText(finalDate);
        holder.price.setText(modelAllTerminsList.get(position).getPrice());

        return convertView;
    }
}
