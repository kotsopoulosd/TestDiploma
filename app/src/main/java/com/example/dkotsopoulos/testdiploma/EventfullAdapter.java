package com.example.dkotsopoulos.testdiploma;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DKotsopoulos on 13/10/2015.
 */
public class EventfullAdapter extends ArrayAdapter<EventFullObject> {

    private  String TAG = EventfullAdapter.class.getSimpleName();
    public List<EventFullObject> EventFullObject;
    private Context context;
    int textViewResourceId;

    class ViewHolder {

        private TextView title;
        private TextView description;
        private TextView EventURL;
        private TextView start_time;
        private TextView stop_time;
        private TextView VenueURL;
        private TextView venue_name;
        private TextView venue_address;
        private TextView City;
    }

    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public EventfullAdapter(Context context, int textViewResourceId,List<EventFullObject> EventFullObject)
    {

        super(context,textViewResourceId,EventFullObject);
        this.EventFullObject = EventFullObject;
        this.context = context;
        this.textViewResourceId = textViewResourceId;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public void handleOnClick(View thisView)
    {
        switch(thisView.getId())
        {
            case R.id.title:
                //do your stufff here..
                break;
            default:
                break;
        }
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View thisView = convertView;

        if (thisView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            thisView = inflater.inflate(textViewResourceId, null, true);
        }


        EventFullObject UI = EventFullObject.get(position);
        final ViewHolder holder = new ViewHolder();

        holder.title = (TextView) thisView.findViewById(R.id.EventfullTitle);
        holder.title.setText(Html.fromHtml("<a href='" + (UI.getEventURL()) + "'>" + UI.getEventTitle() + "</a>"));
        holder.description=(TextView) thisView.findViewById(R.id.EventfullDescription);
        holder.description.setText("Description: \n"+Html.fromHtml(UI.getdescription()));
        holder.start_time= (TextView) thisView.findViewById(R.id.Eventfullstart_time);
        holder.start_time.setText("Starting datetime:\n "+Html.fromHtml(UI.getstart_time()));
        holder.stop_time= (TextView) thisView.findViewById(R.id.Eventfullstop_time);
        holder.stop_time.setText("End datetime:\n "+Html.fromHtml(UI.getstop_time()));
        holder.venue_name= (TextView) thisView.findViewById(R.id.Eventfullvenue_name);
        holder.venue_name.setText("Place: "+Html.fromHtml(UI.getvenue_name()));
        holder.venue_address= (TextView) thisView.findViewById(R.id.Eventfullvenue_address);
        holder.venue_address.setText(Html.fromHtml(UI.getvenue_address()));
        holder.City= (TextView) thisView.findViewById(R.id.Eventfullcity_name);
        holder.City.setText(Html.fromHtml(UI.getCity()));

        return thisView;
    }
}
