package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class jsonToListViewAdapter extends ArrayAdapter<jsonBonuses> implements View.OnClickListener{

    private ArrayList<jsonBonuses> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView bonusName;
        TextView bonusCategory;
        TextView bonusCode;
        ImageView info;
    }

    public jsonToListViewAdapter(ArrayList<jsonBonuses> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        jsonBonuses dataModel=(jsonBonuses) object;

        switch (v.getId())
        {
            case R.id.bonusListImage:
                Log.e("onClick in Adapter","Not sure to be honest");
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        jsonBonuses dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.bonusName = convertView.findViewById(R.id.bonusListName);
            viewHolder.bonusCategory = convertView.findViewById(R.id.bonusListCategory);
            viewHolder.bonusCode = convertView.findViewById(R.id.bonusListCode);
            viewHolder.info = convertView.findViewById(R.id.bonusListImage);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.bonusName.setText(dataModel.getName());
        viewHolder.bonusCategory.setText(dataModel.getCategory());
        viewHolder.bonusCode.setText(dataModel.getBonusCode());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
