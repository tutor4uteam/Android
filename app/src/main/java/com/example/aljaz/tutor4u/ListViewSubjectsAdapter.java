package com.example.aljaz.tutor4u;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewSubjectsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<Model> modelList;
    ArrayList<Model> modelArrayList;

    public ListViewSubjectsAdapter(Context mContext, List<Model> modelList) {
        context = mContext;
        this.modelList = modelList;
        layoutInflater = LayoutInflater.from(context);
        this.modelArrayList = new ArrayList<>();
        this.modelArrayList.addAll(modelList);
    }

    public class ViewHolder{
        TextView subjectName, tutorNum;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.all_subjects_row, null);

            holder.subjectName = convertView.findViewById(R.id.subjectName);
            holder.tutorNum  = convertView.findViewById(R.id.tutorsNum);

            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.subjectName.setText(modelList.get(position).getSubjectName());
        holder.tutorNum.setText(modelList.get(position).getTutorNum());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO klik na subject
            }
        });

        return convertView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modelList.clear();
        if (charText.length() == 0){
            modelList.addAll(modelArrayList);
        }else {
            for (Model model:modelArrayList) {
                if (model.getSubjectName().toLowerCase(Locale.getDefault()).contains(charText)){
                    modelList.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}
