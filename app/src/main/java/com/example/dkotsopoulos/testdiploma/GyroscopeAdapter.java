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
public class GyroscopeAdapter extends ArrayAdapter <GyroscopeObject> {

    private  String TAG = GyroscopeAdapter.class.getSimpleName();
    public List<GyroscopeObject> gyroscopeList;
    private Context context;
    int textViewResourceId;

    class ViewHolder {

        public TextView GyroscopeX;
        public TextView GyroscopeY;
        public TextView GyroscopeZ;
        public TextView timestamp;
    }

    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public GyroscopeAdapter(Context context, int textViewResourceId,List<GyroscopeObject> temporaryXYZ)
    {

        super(context,textViewResourceId,temporaryXYZ);
        this.gyroscopeList = temporaryXYZ;
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


        GyroscopeObject UI = gyroscopeList.get(position);
        ViewHolder holder = new ViewHolder();

        holder.GyroscopeX = (TextView) thisView.findViewById(R.id.textGyroscopeX);
        holder.GyroscopeX.setText(("GyroX: "+Double.toString(UI.getTemp_GyroscopeX())));
        holder.GyroscopeY=(TextView) thisView.findViewById(R.id.textGyroscopeY);
        holder.GyroscopeY.setText(("GyroY: "+Double.toString(UI.getTemp_GyroscopeY())));
        holder.GyroscopeZ= (TextView) thisView.findViewById(R.id.textGyroscopeZ);
        holder.GyroscopeZ.setText(("GyroZ: "+Double.toString(UI.getTemp_GyroscopeZ())));
        holder.timestamp= (TextView) thisView.findViewById(R.id.textDateGyroscope);
        holder.timestamp.setText(LongToDate(UI.getTimestamp()));
        return thisView;
    }

    public CharSequence LongToDate(long timestamp)
    {
        CharSequence datefr= DateUtils.getRelativeTimeSpanString(timestamp);
        return datefr;
    }
}

