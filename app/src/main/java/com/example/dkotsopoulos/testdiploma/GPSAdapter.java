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
 * Created by DKotsopoulos on 01/09/2015.
 */
public class GPSAdapter extends ArrayAdapter <GPSObject> {
    public List<GPSObject> listGPS;
    private Context context;
    int textViewResourceId;

    class ViewHolder {

        public TextView coordinationX;
        public TextView coordinationY;
        public TextView ACCvalue;
        public TextView timestamp;
        public TextView speed;
    }

        /*
         * here we must override the constructor for ArrayAdapter the only variable
         * we care about now is ArrayList<Item> objects, because it is the list of
         * objects we want to display.
         */
    public GPSAdapter(Context context, int textViewResourceId,List<GPSObject> temporaryGPS)
    {

        super(context,textViewResourceId,temporaryGPS);
        this.listGPS = temporaryGPS;
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




            GPSObject UI = listGPS.get(position);
            ViewHolder holder = new ViewHolder();

            holder.coordinationX = (TextView) thisView.findViewById(R.id.textCoordXInsert);
            holder.coordinationX.setText(("Coor_X: "+Double.toString(UI.getTemp_CoordinationX())));
            holder.coordinationY=(TextView) thisView.findViewById(R.id.textCoordYInsert);
            holder.coordinationY.setText(("Coor_Y: "+Double.toString(UI.getTemp_CoordinationY())));
            holder.ACCvalue = (TextView) thisView.findViewById(R.id.textACCInsert);
            holder.ACCvalue.setText(("Accuracy: "+Double.toString( UI.getTemp_ACC())));
            holder.timestamp= (TextView) thisView.findViewById(R.id.textDateGPS);
            holder.timestamp.setText(LongToDate(UI.getTimestamp()));
            return thisView;
        }

        public CharSequence LongToDate(long timestamp)
        {
            CharSequence datefr= DateUtils.getRelativeTimeSpanString(timestamp);
            return datefr;
        }
}

