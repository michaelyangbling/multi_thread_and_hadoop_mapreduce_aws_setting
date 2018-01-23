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
    //public static Map<>
    public static Map<String, Float> divide(Map<String, float[]> count_sum){
        Map<String, Float> results = new HashMap<String, Float>();
        for (String key : count_sum.keySet()) {
            results.put(key, count_sum.get(key)[1] / count_sum.get(key)[0]);
        }
        return results;
    }
}

class runner implements Runnable{
    public int start, end;  public static List<List<String>> listOfList;
    public static Map<String, float[]> count_sum=new HashMap<String, float[]>();
    runner(int x, int y){
        start=x;end=y;
    }
    public void run() {
        float valuex[];

        for (int i = start; i <=end; i++) {
            if (count_sum.get(listOfList.get(i).get(0)) == null) {
                valuex = new float[]{1, Float.parseFloat(listOfList.get(i).get(1))};
                count_sum.put(listOfList.get(i).get(0), valuex);
            } else {
                float[] value = count_sum.get(listOfList.get(i).get(0));
                valuex = new float[]{1 + value[0], Float.parseFloat(listOfList.get(i).get(1)) + value[1]};
                count_sum.put(listOfList.get(i).get(0), valuex);
            }
        }

    }
}

public class Main {


    public static void main(String[] args) {
        List<String> weather=processor.load("/Users/yzh/Downloads/1912.csv");
        System.out.println("split");
        Map<String, Float> results=new HashMap<String, Float>();
        long start=System.currentTimeMillis();
        for(int i=0; i<=3;i++)
        {results=processor.get_mean(processor.filter_tmax(weather));}
        System.out.println(System.currentTimeMillis()-start);
        System.out.println(results);

        start=System.currentTimeMillis();
        List<List<String>> listOfList;
        for(int i=0; i<=3;i++){
        listOfList=processor.filter_tmax(weather); runner.listOfList=listOfList;
        Thread t1=new Thread( new runner(0,listOfList.size()/2) );
        Thread t2=new Thread( new runner(1 + listOfList.size()/2,listOfList.size()-1 ));
        t1.start();
        try{ t1.join();t2.join();}
        catch(Exception xx) { System.out.println("error2");}
        results=processor.divide(runner.count_sum);}
        System.out.println(System.currentTimeMillis()-start);
        System.out.println(results);

        }



    }



//txt results:{CA006119064=-11.0, USC00364972=22.0, USC00392207=-76.666664}