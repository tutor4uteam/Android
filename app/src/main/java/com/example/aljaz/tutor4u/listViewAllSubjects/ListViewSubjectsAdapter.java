package com.example.aljaz.tutor4u.listViewAllSubjects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.aljaz.tutor4u.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewSubjectsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<ModelAllSubjects> modelAllSubjectsList;
    ArrayList<ModelAllSubjects> modelAllSubjectsArrayList;

    public ListViewSubjectsAdapter(Context mContext, List<ModelAllSubjects> modelAllSubjectsList) {
        context = mContext;
        this.modelAllSubjectsList = modelAllSubjectsList;
        layoutInflater = LayoutInflater.from(context);
        this.modelAllSubjectsArrayList = new ArrayList<>();
        this.modelAllSubjectsArrayList.addAll(modelAllSubjectsList);
    }

    public class ViewHolder{
        TextView subjectName, tutorNum;
    }

    @Override
    public int getCount() {
        return modelAllSubjectsList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelAllSubjectsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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

        holder.subjectName.setText(modelAllSubjectsList.get(position).getSubjectName());
        holder.tutorNum.setText(modelAllSubjectsList.get(position).getTutorNum());



        return convertView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modelAllSubjectsList.clear();
        if (charText.length() == 0){
            modelAllSubjectsList.addAll(modelAllSubjectsArrayList);
        }else {
            for (ModelAllSubjects modelAllSubjects : modelAllSubjectsArrayList) {
                if (modelAllSubjects.getSubjectName().toLowerCase(Locale.getDefault()).contains(charText)){
                    modelAllSubjectsList.add(modelAllSubjects);
                }
            }
        }
        notifyDataSetChanged();
    }
}
