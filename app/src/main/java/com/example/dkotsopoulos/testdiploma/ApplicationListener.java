package com.example.dkotsopoulos.testdiploma;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.nfc.Tag;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by DKotsopoulos on 18/09/2015.
 */
public class ApplicationListener extends Service{

    public static final String TAG = "ApplicationLogger";
    public static final long month =18144000000L; // month -> //604800000; week
    ApplicationInfo applicationInfo = null;
    ApplicationInfo ApplicationInstalledInfo = null;
    DBHelper dbHelper;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("ApplicationListener", "Entered");
        dbHelper = new DBHelper(this);
       // dbHelper.DeleteApplicationLogs();
        InstalledApp();
        PackageManager packageManager = getPackageManager();
        UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long timestamp = calendar.getTimeInMillis();
        long endTime = calendar.getTimeInMillis();
        long startTime = calendar.getTimeInMillis() - month;//a week ago
        final PackageManager pm = getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));
//
//        for (ResolveInfo temp : appList) {
//
//            Log.v("my logs", "package  name = "
//                            + temp.activityInfo.packageName
//                   + "  and activity name " + temp.activityInfo.name
//            );
//
//        }

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY, startTime,endTime);
        for (UsageStats u : usageStatsList) {

            try {

                applicationInfo = packageManager.getApplicationInfo(u.getPackageName(), 0);
                applicationInfo.loadLabel(packageManager);


            } catch (final PackageManager.NameNotFoundException e) {

            }
            if(applicationInfo!=null)
            {
                // Check if application is system application!
                if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) != 1) {
                    final String title = (String) ((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "???");
                    String packagename= applicationInfo.packageName.toString();
                    dbHelper.InsertApplicationLogs(title, packagename, u.getTotalTimeInForeground(), timestamp);


                }
            }

        }


        dbHelper.close();
        return super.onStartCommand(intent, flags, startId);

    }
    public void InstalledApp() {

        dbHelper = new DBHelper(this);
        PackageManager packageManager = getPackageManager();


        //All the installed Applications
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo x : packages)
        {
            try {
                ApplicationInstalledInfo = packageManager.getApplicationInfo(x.packageName, 0);
                if ((ApplicationInstalledInfo.flags & applicationInfo.FLAG_SYSTEM) != 1) {
                    final String title = (String) ((ApplicationInstalledInfo != null) ? packageManager.getApplicationLabel(ApplicationInstalledInfo) : "???");

                    dbHelper.InsertInstalledApplications(title);

                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        dbHelper.close();
    }
    public void onDestroy()
    {
        stopSelf();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

