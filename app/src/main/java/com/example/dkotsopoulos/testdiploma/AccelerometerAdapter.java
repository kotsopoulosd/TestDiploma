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
public class AccelerometerAdapter extends ArrayAdapter <AccelerometerObject> {

    private  String TAG = AccelerometerAdapter.class.getSimpleName();
    public List<AccelerometerObject> accelerationList;
    private Context context;
    int textViewResourceId;

    class ViewHolder {

        public TextView AccelarationX;
        public TextView AccelarationY;
        public TextView AccelarationZ;
        public TextView timestamp;
    }

    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public AccelerometerAdapter(Context context, int textViewResourceId,List<AccelerometerObject> temporaryXYZ)
    {

        super(context,textViewResourceId,temporaryXYZ);
        this.accelerationList = temporaryXYZ;
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

        AccelerometerObject UI = accelerationList.get(position);
        ViewHolder holder = new ViewHolder();

        holder.AccelarationX = (TextView) thisView.findViewById(R.id.textAccXInsert);
        holder.AccelarationX.setText(("AccX: "+Double.toString(UI.getTemp_AccelerationX())));
        holder.AccelarationY=(TextView) thisView.findViewById(R.id.textAccYInsert);
        holder.AccelarationY.setText(("AccY: "+Double.toString(UI.getTemp_AccelerationY())));
        holder.AccelarationZ= (TextView) thisView.findViewById(R.id.textAccZInsert);
        holder.AccelarationZ.setText(("AccZ: "+Double.toString(UI.getTemp_AccelerationZ())));
        holder.timestamp= (TextView) thisView.findViewById(R.id.textDateXYZ);
        holder.timestamp.setText(LongToDate(UI.getTimestamp()));
        return thisView;
    }

    public CharSequence LongToDate(long timestamp)
    {
        CharSequence datefr= DateUtils.getRelativeTimeSpanString(timestamp);
        return datefr;
    }
}

