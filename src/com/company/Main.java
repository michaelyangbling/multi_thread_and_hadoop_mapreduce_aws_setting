package com.company;

import java.util.*;
import java.io.*;

class processor {
    public static List<String> load(String path) {
        Scanner sc;
        try {
            sc = new Scanner(new File(path));
            List<String> lines = new ArrayList<String>();
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }

            //String[] arr = lines.toArray(new String[0]);
            //System.out.println(arr[6633010]);
            //return arr;
            return lines;
        } catch (Exception e) {
            System.out.println("error exists");
            //String[] error = {"error exists"};
            List<String> error = Arrays.asList("error exists");
            return error;
        }

    }

    public static List<List<String>> filter_tmax(List<String> all_lines) {
        List<List<String>> listOfLists = new ArrayList<List<String>>();
        List<String> line;
        for (int i = 0; i < all_lines.size(); i++) {
            line = new ArrayList<String>(Arrays.asList(all_lines.get(i).split(",")));
            if (line.get(2).equals("TMAX")) {
                line = Arrays.asList(line.get(0), line.get(3));
                listOfLists.add(line);
            }


        }
        return listOfLists;
    }

    public static Map<String, Float> get_mean(List<List<String>> listOfList) {
        Map<String, float[]> count_sum = new HashMap<String, float[]>();
        float valuex[];

        for (int i = 0; i < listOfList.size(); i++) {
            if (count_sum.get(listOfList.get(i).get(0)) == null) {
                valuex = new float[]{1, Float.parseFloat(listOfList.get(i).get(1))};
                count_sum.put(listOfList.get(i).get(0), valuex);
            } else {
                float[] value = count_sum.get(listOfList.get(i).get(0));
                valuex = new float[]{1 + value[0], Float.parseFloat(listOfList.get(i).get(1)) + value[1]};
                count_sum.put(listOfList.get(i).get(0), valuex);
            }
        }
        Map<String, Float> results = new HashMap<String, Float>();
        for (String key : count_sum.keySet()) {
            results.put(key, count_sum.get(key)[1] / count_sum.get(key)[0]);
        }

        return results;

    }

    public static Map<String, Float> divide(Map<String, float[]> count_sum){// get final result
        Map<String, Float> results = new HashMap<String, Float>();
        for (String key : count_sum.keySet()) {
            results.put(key, count_sum.get(key)[1] / count_sum.get(key)[0]);
        }
        return results;
    }
    public static long maxim(long[] arr, int len){
        long max=arr[0];
        for(int i=1;i<=len-1;i++){
            if(arr[i]>max) max =arr[i];
        }
        return max;
    }
    public static long minim(long[] arr, int len){
        long min=arr[0];
        for(int i=1;i<=len-1;i++){
            if(arr[i]<min) min =arr[i];
        }
        return min;
    }
    public static double average(long[] arr, int len){
        long sum=0;
        for(int i=0;i<=len-1;i++){
            sum=sum+arr[i];
        }
        return (double)sum/(double)len;
    }


}


class runner implements Runnable{
    public int start, end;  public static List<String> weather;
    public static Map<String, float[]> count_sum=new HashMap<String, float[]>();
    runner(int x, int y){
        start=x;end=y;
    }
    public void run() {
        float valuex[];
        List<String> line;
        for (int i = start; i <=end; i++) {
            line=new ArrayList<String>(Arrays.asList(weather.get(i).split(",")));
            if (line.get(2).equals("TMAX")) {
                String station= line.get(0);
                float tmax= Float.parseFloat(line.get(3));
                if(count_sum.get(station)==null){
                    valuex=new float[] {1, tmax};
                    count_sum.put(station, valuex);
                }
                else{
                    valuex=new float[] {1+count_sum.get(station)[0], tmax+count_sum.get(station)[1]};
                    count_sum.put(station, valuex);
                }
            }
        }
    }

}

class lock_runner implements Runnable{
    public int start, end;  public static List<String> weather;
    public static Map<String, float[]> count_sum=new HashMap<String, float[]>();
    lock_runner(int x, int y){
        start=x;end=y;
    }
    public void run() {
        float valuex[];
        List<String> line;
        synchronized(count_sum) {
            for (int i = start; i <= end; i++) {
                line = new ArrayList<String>(Arrays.asList(weather.get(i).split(",")));
                if (line.get(2).equals("TMAX")) {
                    String station = line.get(0);
                    float tmax = Float.parseFloat(line.get(3));
                    if (count_sum.get(station) == null) {
                        valuex = new float[]{1, tmax};
                        count_sum.put(station, valuex);
                    } else {
                        valuex = new float[]{1 + count_sum.get(station)[0], tmax + count_sum.get(station)[1]};
                        count_sum.put(station, valuex);
                    }
                }
            }
        }
    }

}

public class Main {


    public static void main(String[] args) {
        List<String> weather=processor.load("/Users/yzh/Downloads/1912.csv");
        System.out.println("split");
        Map<String, Float> results=new HashMap<String, Float>();
        long start[]=new long[4];
        long time[]=new long[4];
        for(int i=0; i<=3;i++) {
            start[i]=System.currentTimeMillis();
            results=processor.get_mean(processor.filter_tmax(weather));
            time[i]=(System.currentTimeMillis()-start[i]);
            }
        System.out.println(processor.maxim(time,time.length));
        System.out.println(processor.minim(time,time.length));
        System.out.println(processor.average(time,time.length));
        System.out.println(results);


        Thread t1;
        Thread t2;
        for(int i=0; i<=3;i++){
            start[i]=System.currentTimeMillis();
            runner.weather=weather;
            t1 =new Thread( new runner(0,weather.size()/2) );
            t2=new Thread( new runner(1 + weather.size()/2,weather.size()-1 ));
            t1.start();t2.start();
            try{ t1.join();t2.join();}
            catch(Exception xx) { System.out.println("error2");}
            results=processor.divide(runner.count_sum);
            time[i]=(System.currentTimeMillis()-start[i]);
        }
        System.out.println(processor.maxim(time,time.length));
        System.out.println(processor.minim(time,time.length));
        System.out.println(processor.average(time,time.length));
        System.out.println(results);


        for(int i=0; i<=3;i++){
            start[i]=System.currentTimeMillis();
            lock_runner.weather=weather;
            t1 =new Thread( new lock_runner(0,weather.size()/2) );
            t2=new Thread( new lock_runner(1 + weather.size()/2,weather.size()-1 ));
            t1.start();t2.start();
            try{ t1.join();t2.join();}
            catch(Exception xx) { System.out.println("error2");}
            results=processor.divide(runner.count_sum);
            time[i]=(System.currentTimeMillis()-start[i]);
        }
        System.out.println(processor.maxim(time,time.length));
        System.out.println(processor.minim(time,time.length));
        System.out.println(processor.average(time,time.length));
        System.out.println(results);



        }



    }



//txt results:{CA006119064=-11.0, USC00364972=22.0, USC00392207=-76.666664}