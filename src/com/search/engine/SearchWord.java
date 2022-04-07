package com.search.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchWord {
	private static HashMap<String, Integer> numbers = new HashMap<String, Integer>(); // Here we and put the alternative words and their distance
    private static String regex = "[a-z0-9]+";
    private static Pattern pattern = Pattern.compile(regex);
    private static Matcher matcher = pattern.matcher(" ");

    // sort for ranking of the pages
    public static void rankFiles(Hashtable<String, Integer> files) //passing frequency list(the occurrence of word in that url), basically putting the url
    {

        // Transfer as List and sort it from hast table
        ArrayList<Map.Entry<String, Integer>> fileList = new ArrayList<>(files.entrySet());//get values from hashtable and put in array list 

        Collections.sort(fileList, new Comparator<Map.Entry<String, Integer>>() {
// comparing the urls (based on the occurance of word in that url) and then sorting it and returing the urls in increasing order
            public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) 
            {
                return obj1.getValue().compareTo(obj2.getValue()); //will compare the value of the objects i.e the urls
            }
        });
// we have used this to reverse the sorted list to display top 5 results
        Collections.reverse(fileList);

    
        System.out.println("-------------------------");
        System.out.println("Top 10 search results");
        System.out.println("-------------------------");
        for ( int j = 0; fileList.size() > j &&  j < Settings.NumberOfSearch;  j++) {
            if(fileList.get(j).getKey()!=null) 
                System.out.printf("[%d] %s\n", j, fileList.get(j).getKey());
        } 
    }

    public static boolean suggestAltWord(String wordToSearch) {


        for (File f : new File(Settings.TEXT_PATH).listFiles()) 
                findWord(f, wordToSearch);
        

        int i = 0;
        for (Map.Entry entry : numbers.entrySet()) {
            if (Settings.AltWordDistance > (Integer)entry.getValue()) {
                i++;
                if (i==1)
                	 System.out.println("Did you mean? ");         	
                else if (i >= Settings.NumberOfSearch)
                	break;
                System.out.printf("[%d] %s\n", i,  entry.getKey());
            }
        }
        return i != 0;

    }

    // finds strings with similar pattern and calls edit distance() on those strings
    public static void findWord(File sourceFile, String str) {
        try(
    		FileReader f = new FileReader(sourceFile);
    		BufferedReader myRederObject = new BufferedReader(f);
		) {
         
            for (String line = myRederObject.readLine(); line != null; line = myRederObject.readLine()) {
                matcher.reset(line.toLowerCase());
                while (matcher.find()) {
                	String c = matcher.group().toLowerCase();
                    numbers.put(c, editDistance(str, c));
                }
            }

   
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }

    }

    public static int editDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();

        int[][] my_array = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            my_array[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            my_array[0][j] = j;
        }

        // iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = str1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = str2.charAt(j);

                if (c1 == c2) {
                    my_array[i + 1][j + 1] = my_array[i][j];
                } else {
                    int replace = my_array[i][j] + 1;
                    int insert = my_array[i][j + 1] + 1;
                    int delete = my_array[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    my_array[i + 1][j + 1] = min;
                }
            }
        }
        return my_array[len1][len2];
    }
    
}
