package com.example.aljaz.tutor4u.listViewStudentTerms;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aljaz.tutor4u.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewStudentTermsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<ModelStudentTerm> modelList;
    ArrayList<ModelStudentTerm> modelArrayList;

    public ListViewStudentTermsAdapter(Context mContext, List<ModelStudentTerm> modelList) {
        context = mContext;
        this.modelList = modelList;
        layoutInflater = LayoutInflater.from(context);
        this.modelArrayList = new ArrayList<>();
        this.modelArrayList.addAll(modelList);
    }

    public class ViewHolder{
        TextView tutorName, tutorAddress, subject, date;
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
            convertView = layoutInflater.inflate(R.layout.student_terms, null);

            holder.tutorName = convertView.findViewById(R.id.student_term_name);
            holder.tutorAddress = convertView.findViewById(R.id.student_term_address);
            holder.subject  = convertView.findViewById(R.id.student_term_subject);
            holder.date = convertView.findViewById(R.id.student_term_date);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.tutorName.setText(modelList.get(position).getTutorName());
        holder.tutorName.setSelected(true);
        holder.tutorAddress.setText(modelList.get(position).getTutorAddress());
        holder.tutorAddress.setSelected(true);
        holder.subject.setText(modelList.get(position).getSubject());
        holder.subject.setSelected(true);
        SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String finalDate = dt1.format(modelList.get(position).getDate());
        holder.date.setText(finalDate);


        return convertView;
    }

}
