package com.example.dkotsopoulos.testdiploma;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by DKotsopoulos on 16/09/2015.
 */
public class LightAdapter extends ArrayAdapter<LightObject> {

    private  String TAG = LightAdapter.class.getSimpleName();
    public List<LightObject> lightlogs;
    private Context context;
    int textViewResourceId;

    class ViewHolder {

        public TextView TLightVolume;
        public TextView TLightDate;
    }

    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public LightAdapter(Context context, int textViewResourceId,List<LightObject> lightlogs)
    {

        super(context,textViewResourceId,lightlogs);
        this.lightlogs = lightlogs;
        this.context = context;
        this.textViewResourceId = textViewResourceId;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        View thisView = convertView;

        if (thisView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            thisView = inflater.inflate(textViewResourceId, null, true);
        }

        LightObject UI = lightlogs.get(position);
        ViewHolder holder = new ViewHolder();

        holder.TLightVolume = (TextView) thisView.findViewById(R.id.TLightVolume);
        holder.TLightVolume.setText("Lux: "+UI.getLighhtVolume());
        holder.TLightDate = (TextView) thisView.findViewById(R.id.TLightDate);
        holder.TLightDate.setText(LongToDate(UI.getLightTimestamp()));


        return thisView;
    }

    public CharSequence LongToDate(long timestamp)
    {
        CharSequence datefr= DateUtils.getRelativeTimeSpanString(timestamp);
        return datefr;
    }
}