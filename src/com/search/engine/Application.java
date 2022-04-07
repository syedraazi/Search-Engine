package com.search.engine;
import java.util.Scanner;
import java.util.stream.Collectors;



import java.util.ArrayList;
import java.util.Hashtable;
import java.io.File;


public class Application {

    private static Hashtable<String, Integer> FrequencyList = new Hashtable<String, Integer>();//  Used Hash Table for the Occurence of the word in the url 

    public static void resetFolder() {
        File file = new File(Settings.HTML_PATH.toString());
        System.out.println("HTML PATH:" + file.getAbsolutePath());
        file.mkdirs();
        for (File f: file.listFiles())
            f.delete();
        
        file = new File(Settings.TEXT_PATH.toString());
        file.mkdirs();
        for (File f: file.listFiles())
            f.delete();
    }
    
    public static boolean isWordFound(File file, String word) {
        try (Scanner scanner = new Scanner(file, Settings.CHARSET_NAME);) {
            scanner.useDelimiter("\\Z");
            if (scanner.hasNext()) {
	            String[] arr = scanner.next().toLowerCase().split(Settings.DELIMITER);
	            if (arr.length > 1) {
	            	String url = arr[0];
	                int wordFrequency = BoyerMoore.searchCount(arr[1], word);
	                if (wordFrequency > 0) {
	                    System.out.printf("[%d] url:%s count:%d\n", FrequencyList.size(), url, wordFrequency);
	                    FrequencyList.put(url, wordFrequency); //it will put word frequency occurred in the url into hash table
	                    return true;
	                }
	            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

  
   
    public static void main( String[] args )
    {
     
    	resetFolder();
        System.out.println("-------------------------");
        System.out.printf("Search Engine v%s\n", Settings.TAG.toString());
        System.out.println("-------------------------");
        
        try(Scanner sc = new Scanner(System.in);){
        	
        	while (true) {
        		System.out.print("Enter a valid url >>>>> ");
                String url = sc.next();
                if (!url.contains("http"))
                    url = "https://" + url;
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.err.println("Starting crawler...");
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                boolean isCrawled = WebCrawler.startCrawler(url, 0);
                
                if(isCrawled)
                	break;
                	
                System.err.println("-------------------------");
                System.err.println("Invalid URL. Try Again!");
                System.err.println("-------------------------");
        	}
        	
        	
        	
        	while(true) {
        		System.out.println("-------------------------------------------");
                System.out.println("Enter a word you want to search [Quit] >  ");
                String word = sc.next().toLowerCase();
                if (word.equals("quit")) {
                	System.out.println("terminated");
                	break;}
                FrequencyList.clear();   // It will clear the occurence of the word data in the previous search
                int totalFiles = 0;
                for (File file: new File(Settings.TEXT_PATH).listFiles()) {
                	if (isWordFound(file, word))
                			totalFiles++;
                }
                System.out.printf(" Word: \"%s\" found in %d files\n", word,  totalFiles);
                if(totalFiles == 0) {
                	if(SearchWord.suggestAltWord(word))
            			System.out.println("Entered word cannot be resolved....");
                } else {
            		SearchWord.rankFiles(FrequencyList);

            	}
                
           }
        	      
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
    }
  }
