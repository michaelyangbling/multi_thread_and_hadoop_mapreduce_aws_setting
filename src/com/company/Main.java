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
}

public class Main {


    public static void main(String[] args) {
        List<String> weather=processor.load("/Users/yzh/Downloads/1912.csv");
        List<List<String>> tmax=processor.filter_tmax(weather);
        Map<String, Float> results=processor.get_mean(tmax);

        System.out.println(results);


    }
}

