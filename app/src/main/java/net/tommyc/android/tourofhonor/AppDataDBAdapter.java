package net.tommyc.android.tourofhonor;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AppDataDBAdapter extends CursorAdapter {
    public AppDataDBAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView bonusCode = view.findViewById(R.id.bonusListCode);
        TextView bonusName = view.findViewById(R.id.bonusListName);
        TextView bonusCity = view.findViewById(R.id.bonusListCity);
        TextView bonusState = view.findViewById(R.id.bonusListState);
        TextView bonusCategory = view.findViewById(R.id.bonusListCategory);

        // Extract properties from cursor
        String sCode = cursor.getString(cursor.getColumnIndexOrThrow("sCode"));
        String sName = cursor.getString(cursor.getColumnIndexOrThrow("sName"));
        String sCity = cursor.getString(cursor.getColumnIndexOrThrow("sCity"));
        String sState = cursor.getString(cursor.getColumnIndexOrThrow("sState"));
        String sCategory = cursor.getString(cursor.getColumnIndexOrThrow("sCategory"));

        // Populate fields with extracted properties
        bonusCode.setText(sCode);
        bonusName.setText(sName);
        bonusCity.setText(sCity);
        bonusState.setText(sState);
        bonusCategory.setText(sCategory);
    }
}
