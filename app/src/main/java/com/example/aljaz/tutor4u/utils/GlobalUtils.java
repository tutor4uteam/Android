package com.example.aljaz.tutor4u.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.aljaz.tutor4u.Interface.DialogCallback;
import com.example.aljaz.tutor4u.R;
import com.example.aljaz.tutor4u.Widgets.CustomDialog;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

public class GlobalUtils {

    private static String TAG = "Smile";
    public static String rating = "Not given yet";

    public static void showDialog(Context context, final DialogCallback dialogCallback) {
        final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.layout_dialog, null);

        dialog.setContentView(v);
        SmileRating smileRating = dialog.findViewById(R.id.smile_rating);
        smileRating.setSelectedSmile(BaseRating.OKAY);

        RelativeLayout relativeLayout = dialog.findViewById(R.id.aroundRating);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  dialog.dismiss();
                                              }
                                          }
        );

        Button btn_done = dialog.findViewById(R.id.btn_submit_rating);


        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.BAD:
                        Log.i(TAG, "Bad");
                        rating = "2";
                        break;
                    case SmileRating.GOOD:
                        Log.i(TAG, "Good");
                        rating = "4";
                        break;
                    case SmileRating.GREAT:
                        Log.i(TAG, "Great");
                        rating = "5";
                        break;
                    case SmileRating.OKAY:
                        Log.i(TAG, "Okay");
                        rating = "3";
                        break;
                    case SmileRating.TERRIBLE:
                        Log.i(TAG, "Terrible");
                        rating = "1";
                        break;
                }
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCallback != null)
                    dialogCallback.callback(rating);
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
