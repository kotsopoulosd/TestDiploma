package com.example.dkotsopoulos.testdiploma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by DKotsopoulos on 16/09/2015.
 */
public class ApplicationAdapter extends ArrayAdapter<ApplicationObject> {

    public List<ApplicationObject> applogs;
    private Context context;
    int textViewResourceId;

    class ViewHolder {

        public TextView TApplicationName;
        public TextView TApplicationForeground;
    }

    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public ApplicationAdapter(Context context, int textViewResourceId,List<ApplicationObject> appslogs)
    {

        super(context,textViewResourceId,appslogs);
        this.applogs = appslogs;
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


        ApplicationObject UI = applogs.get(position);
        ViewHolder holder = new ViewHolder();

        holder.TApplicationName = (TextView) thisView.findViewById(R.id.TApplicationName);
        holder.TApplicationName.setText("App: "+UI.getApplicationName());

        holder.TApplicationForeground = (TextView) thisView.findViewById(R.id.TApplicationForeground);
        holder.TApplicationForeground.setText("Foreground: "+getDate(UI.getApplicationForeground()));


        return thisView;
    }

    public static String getDate(long millis)
    {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        sb.append(days);
        sb.append(" Days ");
        sb.append(hours);
        sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Min ");
        sb.append(seconds);
        sb.append(" Secs");

        return(sb.toString());

    }
}