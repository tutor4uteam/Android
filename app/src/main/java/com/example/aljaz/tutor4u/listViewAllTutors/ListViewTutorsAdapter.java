package com.example.aljaz.tutor4u.listViewAllTutors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aljaz.tutor4u.R;
import com.example.aljaz.tutor4u.listViewAllSubjects.ModelAllSubjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewTutorsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<ModelAllTutors> modelList;
    ArrayList<ModelAllTutors> modelArrayList;

    public ListViewTutorsAdapter(Context mContext, List<ModelAllTutors> modelList) {
        context = mContext;
        this.modelList = modelList;
        layoutInflater = LayoutInflater.from(context);
        this.modelArrayList = new ArrayList<>();
        this.modelArrayList.addAll(modelList);
    }

    public class ViewHolder{
        ImageView profile_picture;
        TextView profile_name, profile_grade;
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
            convertView = layoutInflater.inflate(R.layout.all_tutors_row, null);

            holder.profile_picture = convertView.findViewById(R.id.profile_image);
            holder.profile_name = convertView.findViewById(R.id.profile_name);
            holder.profile_grade  = convertView.findViewById(R.id.profile_grade);

            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();

        }

        holder.profile_name.setText(modelList.get(position).getProfile_name() + " " + modelList.get(position).getProfile_surname());
        holder.profile_grade.setText(modelList.get(position).getProfile_grade());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO klik na tutorja
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
            for (ModelAllTutors model:modelArrayList) {
                if (model.getProfile_name().toLowerCase(Locale.getDefault()).contains(charText)){
                    modelList.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
}
