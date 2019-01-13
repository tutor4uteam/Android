package com.example.aljaz.tutor4u.listViewTutorTerms.freeTerms;

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

public class ListViewFreeTermsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    List<ModelFreeTerms> modelList;
    ArrayList<ModelFreeTerms> modelArrayList;

    public ListViewFreeTermsAdapter(Context mContext, List<ModelFreeTerms> modelList) {
        context = mContext;
        this.modelList = modelList;
        layoutInflater = LayoutInflater.from(context);
        this.modelArrayList = new ArrayList<>();
        this.modelArrayList.addAll(modelList);
    }

    public class ViewHolder{
        TextView subjectName, date, price;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.free_terms_row, null);

            holder.subjectName = convertView.findViewById(R.id.free_term_subject);
            holder.date = convertView.findViewById(R.id.free_term_date);
            holder.price  = convertView.findViewById(R.id.free_term_price);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.subjectName.setText(modelList.get(position).getSubject());
        holder.price.setText(modelList.get(position).getPrice());
        SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String finalDate = dt1.format(modelList.get(position).getDate());
        holder.date.setText(finalDate);


        return convertView;
    }
}
