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
 * Created by DKotsopoulos on 09/09/2015.
 */

public class CallAdapter extends  ArrayAdapter<CallObject>{

    private  String TAG = CallAdapter.class.getSimpleName();
    public List<CallObject> callogs;
    private Context context;
    int textViewResourceId;

    class ViewHolder {

        public TextView Tphnumber;
        public TextView Tcallduration;
        public TextView Tcallname;
        public TextView Tcalltype;
        public TextView TcallGeo;
        public TextView Tcalldate;
    }

    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public CallAdapter(Context context, int textViewResourceId, List<CallObject> temporaryCalls)
    {

        super(context,textViewResourceId,temporaryCalls);
        this.callogs = temporaryCalls;
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

        CallObject UI = callogs.get(position);
        ViewHolder holder = new ViewHolder();

        holder.Tphnumber = (TextView) thisView.findViewById(R.id.Textphnumber);
        holder.Tphnumber.setText("Number: "+UI.getphnumber());
        holder.Tcallduration=(TextView) thisView.findViewById(R.id.Textcallduration);
        holder.Tcallduration.setText("Duration: "+UI.getcallduration());
        holder.Tcallname = (TextView) thisView.findViewById(R.id.Textcallname);
        holder.Tcallname.setText("Name: "+UI.getcallname());
        holder.Tcalltype = (TextView) thisView.findViewById(R.id.Textcalltype);
        holder.Tcalltype.setText("Type: " +UI.getcalltype());
        holder.TcallGeo = (TextView) thisView.findViewById(R.id.TextcallGeo);
        holder.TcallGeo.setText("Location:"+UI.getTempcallGeo());
        holder.Tcalldate= (TextView) thisView.findViewById(R.id.Textcalldate);
        holder.Tcalldate.setText(LongToDate(UI.getcalldate()));
        return thisView;
    }

    public CharSequence LongToDate(long timestamp)
    {
        CharSequence datefr= DateUtils.getRelativeTimeSpanString(timestamp);
        return datefr;
    }
}

