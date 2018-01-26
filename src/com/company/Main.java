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
    public static long Fib(int n) {
        if (n <=2) { return 1;}
        else{return Fib(n-1)+Fib(n-2);}
    }

}
class sequen {  //this is the sequential version
    public static List<String> weather;
    public static Map<String, float[]> count_sum=new HashMap<String, float[]>();
    public static void run() {
        float valuex[];
        List<String> line;
        for (int i = 0; i <=weather.size()-1; i++) {
            line=new ArrayList<String>(Arrays.asList(weather.get(i).split(",")));
            if (line.get(2).equals("TMAX")) {
                String station= line.get(0);
                float tmax= Float.parseFloat(line.get(3));
                if(count_sum.get(station)==null){
                    valuex=new float[] {1, tmax};
                    count_sum.put(station, valuex);
                    //processor.Fib(17);
                }
                else{
                    valuex=new float[] {1+count_sum.get(station)[0], tmax+count_sum.get(station)[1]};
                    count_sum.put(station, valuex);
                    //processor.Fib(17);
                }
            }
        }
    }

}

class no_share implements Runnable{ //this is no-sharing version
    public int start, end;  public static List<String> weather;
    public  Map<String, float[]> count_sum=new HashMap<String, float[]>();
    no_share(int x, int y){
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
                    //processor.Fib(17);
                }
                else{
                    valuex=new float[] {1+count_sum.get(station)[0], tmax+count_sum.get(station)[1]};
                    count_sum.put(station, valuex);
                    //processor.Fib(17);
                }
            }
        }
    }

}


class runner implements Runnable{ //this is the no-locker version
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
                    //processor.Fib(17);
                }
                else{
                    valuex=new float[] {1+count_sum.get(station)[0], tmax+count_sum.get(station)[1]};
                    count_sum.put(station, valuex);
                    //processor.Fib(17);
                }
            }
        }
    }

}

class lock_runner implements Runnable{  //this is coarse-lock version
    public int start, end;  public static List<String> weather;
    public static Map<String, float[]> count_sum=new HashMap<String, float[]>();
    lock_runner(int x, int y){
        start=x;end=y;
    }
    public void run() {
        float valuex[];
        List<String> line;

            for (int i = start; i <= end; i++) {
                line = new ArrayList<String>(Arrays.asList(weather.get(i).split(",")));
                if (line.get(2).equals("TMAX")) {
                    String station = line.get(0);
                    float tmax = Float.parseFloat(line.get(3));
                    synchronized(count_sum) {
                    if (count_sum.get(station) == null) {
                        valuex = new float[]{1, tmax};
                        count_sum.put(station, valuex);
                        //processor.Fib(17);
                    } else {
                        valuex = new float[]{1 + count_sum.get(station)[0], tmax + count_sum.get(station)[1]};
                        count_sum.put(station, valuex);
                        //processor.Fib(17);
                    }
                }
            }
        }
    }

}

class fine_lock_runner implements Runnable { //this is the fine-lock verision
    public int start, end;
    public static List<String> weather;
    public static Map<String, float[]> count_sum = new HashMap<String, float[]>();

    fine_lock_runner(int x, int y) {
        start = x;
        end = y;
    }

    public void run() {//
        float valuex[];
        List<String> line;

        for (int i = start; i <= end; i++) {
            line = new ArrayList<String>(Arrays.asList(weather.get(i).split(",")));
            if (line.get(2).equals("TMAX")) {
                String station = line.get(0);
                float tmax = Float.parseFloat(line.get(3));
                synchronized (count_sum) {
                    if (count_sum.get(station) == null) {
                        valuex = new float[]{0, 0};
                        count_sum.put(station, valuex);
                    }
                }
                synchronized(count_sum.get(station)) {
                  valuex = new float[]{1 + count_sum.get(station)[0], tmax + count_sum.get(station)[1]};
                  //processor.Fib(17);
                  count_sum.put(station, valuex);
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
        long start[]=new long[40];
        long time[]=new long[40];
        for(int i=0; i<=39;i++) {
            start[i]=System.currentTimeMillis();
            sequen.count_sum=new HashMap<String, float[]>();
            sequen.weather=weather;
            sequen.run();
            results=processor.divide(sequen.count_sum);
            time[i]=(System.currentTimeMillis()-start[i]);
            }
        System.out.println(processor.maxim(time,time.length));
        System.out.println(processor.minim(time,time.length));
        System.out.println(processor.average(time,time.length));
        System.out.println(results);System.out.println("spliter");

        Thread t1;
        Thread t2;
        for(int i=0; i<=39;i++){
            start[i]=System.currentTimeMillis();
            runner.count_sum=new HashMap<String, float[]>();
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
        System.out.println(results);System.out.println("spliter");

        for(int i=0; i<=39;i++){
            start[i]=System.currentTimeMillis();
            lock_runner.count_sum=new HashMap<String, float[]>();
            lock_runner.weather=weather;
            t1 =new Thread( new lock_runner(0,weather.size()/2) );
            t2=new Thread( new lock_runner(1 + weather.size()/2,weather.size()-1 ));
            t1.start();t2.start();
            try{ t1.join();t2.join();}
            catch(Exception xx) { System.out.println("error2");}
            results=processor.divide(lock_runner.count_sum);
            time[i]=(System.currentTimeMillis()-start[i]);
        }
        System.out.println(processor.maxim(time,time.length));
        System.out.println(processor.minim(time,time.length));
        System.out.println(processor.average(time,time.length));
        System.out.println(results); System.out.println("spliter");

        for(int i=0; i<=39;i++){
            start[i]=System.currentTimeMillis();
            fine_lock_runner.count_sum=new HashMap<String, float[]>();
            fine_lock_runner.weather=weather;
            t1 =new Thread( new fine_lock_runner(0,weather.size()/2) );
            t2=new Thread( new fine_lock_runner(1 + weather.size()/2,weather.size()-1 ));
            t1.start();t2.start();
            try{ t1.join();t2.join();}
            catch(Exception xx) { System.out.println("error2");}
            results=processor.divide(fine_lock_runner.count_sum);
            time[i]=(System.currentTimeMillis()-start[i]);
        }
        System.out.println(processor.maxim(time,time.length));
        System.out.println(processor.minim(time,time.length));
        System.out.println(processor.average(time,time.length));
        System.out.println(results);System.out.println("spliter");

        no_share m1=new no_share(0,weather.size()/2);
        no_share m2=new no_share(1 + weather.size()/2,weather.size()-1 );
        //float[] valuex=new float[2];
        for(int i=0; i<=39;i++){
            start[i]=System.currentTimeMillis();
            no_share.weather=weather;
            m1=new no_share(0,weather.size()/2); m2=new no_share(1 + weather.size()/2,weather.size()-1 );
            m1.count_sum=new HashMap<String, float[]>(); m2.count_sum=new HashMap<String, float[]>();
            t1 =new Thread( m1 );
            t2=new Thread(m2);
            t1.start();t2.start();
            try{ t1.join();t2.join();}
            catch(Exception xx) { System.out.println("error2");}
            for (String key : m1.count_sum.keySet()) {
                if (m2.count_sum.get(key)==null) { m2.count_sum.put(key, m1.count_sum.get(key)); }
                else {
                    float[] valuex=new float[] { m1.count_sum.get(key)[0]+m2.count_sum.get(key)[0],
                            m1.count_sum.get(key)[1]+m2.count_sum.get(key)[1] };
                    m2.count_sum.put( key, valuex ); //processor.Fib(17);
                }
            }
            results=processor.divide(m2.count_sum);
            time[i]=(System.currentTimeMillis()-start[i]);
        }
        System.out.println(processor.maxim(time,time.length));
        System.out.println(processor.minim(time,time.length));
        System.out.println(processor.average(time,time.length));
        System.out.println(results);
        }

    }



