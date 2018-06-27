package com.tasree7a.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tasree7a.R;
import com.tasree7a.ThisApplication;
import com.tasree7a.models.popularsalons.CityModel;
import com.tasree7a.utils.UIUtils;

import java.util.List;

/**
 * Created by mac on 7/6/17.
 */

public class CitiesSpinnerAdapter extends ArrayAdapter<CityModel> {

    List<CityModel> cityModels = null;

    private static class ViewHolder {
        private TextView itemView;
    }


    public CitiesSpinnerAdapter(Context context, int textViewResourceId, List<CityModel> items) {

        super(context, textViewResourceId, items);

        cityModels = items;

    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {


            TextView textView = new TextView(ThisApplication.getCurrentActivity());

            textView.setTextSize(14);

            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);

            textView.setTextColor(UIUtils.getColor(R.color.APP_TEXT_COLOR));

            convertView = textView;

            viewHolder = new ViewHolder();

            viewHolder.itemView = (TextView) convertView;

            convertView.setTag(viewHolder);

        } else{

            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.itemView.setText(cityModels.get(position).getName());

        return convertView;
    }

    @Override
    public int getCount() {
        return cityModels.size();
    }
}
