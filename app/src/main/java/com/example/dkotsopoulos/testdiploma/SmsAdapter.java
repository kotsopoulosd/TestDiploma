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
 * Created by DKotsopoulos on 10/09/2015.
 */
public class SmsAdapter extends  ArrayAdapter<SmsObject>{

    private  String TAG = SmsAdapter.class.getSimpleName();
    public List<SmsObject> smslogs;
    private Context context;
    int textViewResourceId;

    class ViewHolder {

        public TextView Tsmsbody;
        public TextView Tsmsaddress;
        public TextView TsmsPerson;
        public TextView Tsmstype;
        public TextView Tcalldate;

    }

    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public SmsAdapter(Context context, int textViewResourceId, List<SmsObject> tempsmslogs)
    {

        super(context,textViewResourceId,tempsmslogs);
        this.smslogs = tempsmslogs;
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




        SmsObject UI = smslogs.get(position);
        ViewHolder holder = new ViewHolder();

        holder.Tsmsbody = (TextView) thisView.findViewById(R.id.Textbody);
        holder.Tsmsbody.setText("Content: "+UI.getbody());
        holder.Tsmsaddress = (TextView) thisView.findViewById(R.id.TextSMSNumber);
        holder.Tsmsaddress.setText("Number: "+UI.getaddress());
        holder.TsmsPerson=(TextView) thisView.findViewById(R.id.TextPerson);
        holder.TsmsPerson.setText("Person: "+UI.getPerson());
        holder.Tsmstype = (TextView) thisView.findViewById(R.id.Texttype);
        holder.Tsmstype.setText("Type: "+UI.gettype());
        holder.Tcalldate= (TextView) thisView.findViewById(R.id.Textsmscalldate);
        holder.Tcalldate.setText("Date: "+LongToDate(UI.getcalldate()));
        return thisView;
    }

    public CharSequence LongToDate(long timestamp)
    {
        CharSequence datefr= DateUtils.getRelativeTimeSpanString(timestamp);
        return datefr;
    }
}
