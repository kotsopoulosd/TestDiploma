package com.example.dkotsopoulos.testdiploma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by DKotsopoulos on 16/09/2015.
 */
public class CalendarAdapter extends ArrayAdapter<CalendarObject>{

        public List<CalendarObject> calendarlogs;
        private Context context;
        int textViewResourceId;

        class ViewHolder {

            public TextView TCalendarEventName;
            public TextView TCalendarEventDescription;
            public TextView TCalendarEventLocation;
            public TextView TCalendarEventStartime;
            public TextView TCalendarEventEndtime;

        }

        /*
         * here we must override the constructor for ArrayAdapter the only variable
         * we care about now is ArrayList<Item> objects, because it is the list of
         * objects we want to display.
         */
        public CalendarAdapter(Context context, int textViewResourceId,List<CalendarObject> calendarlog)
        {

            super(context,textViewResourceId,calendarlog);
            this.calendarlogs = calendarlog;
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




            CalendarObject UI = calendarlogs.get(position);
            ViewHolder holder = new ViewHolder();

            holder.TCalendarEventName = (TextView) thisView.findViewById(R.id.TCalendarEventName);
            holder.TCalendarEventName.setText(UI.getnameOfEvent());

            holder.TCalendarEventDescription = (TextView) thisView.findViewById(R.id.TCalendarEventDescription);
            holder.TCalendarEventDescription.setText(UI.getdescriptions());

            holder.TCalendarEventLocation=(TextView) thisView.findViewById(R.id.TCalendarEventLocation);
            holder.TCalendarEventLocation.setText(UI.geteventLocation());

            holder.TCalendarEventStartime = (TextView) thisView.findViewById(R.id.TCalendarEventStartime);
            holder.TCalendarEventStartime.setText(getDate(UI.getstartDates()));

            holder.TCalendarEventEndtime= (TextView) thisView.findViewById(R.id.TCalendarEventEndtime);
            holder.TCalendarEventEndtime.setText(getDate(UI.getendDates()));

            return thisView;
        }

        public static String getDate(long milliSeconds)
        {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
        }
    }

