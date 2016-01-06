package com.example.dkotsopoulos.testdiploma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DKotsopoulos on 18/09/2015.
 */
public class InstalledAppAdapter extends ArrayAdapter<String> {

    private String TAG = InstalledAppAdapter.class.getSimpleName();
    public List<String> installedapplogs;
    private Context context;
    int textViewResourceId;

    class ViewHolder {

        public TextView TApplicationName;

    }

    /*
     * here we must override the constructor for ArrayAdapter the only variable
     * we care about now is ArrayList<Item> objects, because it is the list of
     * objects we want to display.
     */
    public InstalledAppAdapter(Context context, int textViewResourceId, List<String> installedapplogs) {

        super(context, textViewResourceId, installedapplogs);
        this.installedapplogs = installedapplogs;
        this.context = context;
        this.textViewResourceId = textViewResourceId;
    }


    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        View thisView = convertView;

        if (thisView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            thisView = inflater.inflate(textViewResourceId, null, true);
        }


        String UI = installedapplogs.get(position);
        ViewHolder holder = new ViewHolder();

        holder.TApplicationName = (TextView) thisView.findViewById(R.id.TInstalledAppName);
        holder.TApplicationName.setText(UI);

        return thisView;

    }
}