package com.example.dkotsopoulos.testdiploma;

/**
 * Created by DKotsopoulos on 08/10/2015.
 */
public class GroupPointObject implements Comparable<GroupPointObject>
{
    private double x = 0;
    private double y = 0;
    private long datetime = 0;
    public double xCluster = 0;
    public double yCluster = 0;
    public int cluster_volume = 0;
    public int Work = 0;
    public int Home = 0;



    public GroupPointObject(double x, double y, long datetime)
    {
        this.setX(x);
        this.setY(y);
        this.setdatetime(datetime);
    }
    public GroupPointObject()
    {

    }
    public void setX(double x) {
        this.x = x;
    }

    public double getX()  {
        return this.x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return this.y;
    }
    public void setdatetime(long datetime) {
        this.datetime = datetime;
    }
    public long getdatetime() {
        return this.datetime;
    }

    public void setCluster(int n) {
        this.cluster_volume = n;
    }

    public int getCluster() {
        return this.cluster_volume;
    }

    public String toString() {
        return "********************" + "\nCluster Existing Point -> ("+x+","+y+")"
                + "\nCluster AVG Point -> ("+xCluster/cluster_volume
                +","+yCluster/cluster_volume+")"
                +"\n cluster_volume -> " + cluster_volume
                +"\n work_volume -> " + Work
                +"\n home_volume -> " + Home
                +"\n********************";

    }


    @Override
    public int compareTo(GroupPointObject another) {
        int compareQuantity = ((GroupPointObject) another).getCluster();
        return compareQuantity - this.cluster_volume; //desc
    }
}


