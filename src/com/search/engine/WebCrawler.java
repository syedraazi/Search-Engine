package com.search.engine;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class WebCrawler {
    // mapping url to path
    private static Set<String> urls = new HashSet<>();   // Using the hashset to eliminate the duplicate values

    public static Set<String> getUrls() {
        return urls;
    }

    public static void clear() {
        urls.clear();
    }

    public static boolean isValidURL(String url) {
        if (!url.contains("http"))
            url = "https://" + url;
        try {
            URL obj = new URL(url);
            obj.toURI();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean startCrawler(String url, int depth) {
        if (!urls.contains(url) && depth < Settings.MAX_DEPTH) {
            System.out.printf("[%d]: %s\n", urls.size(), url);
            try {
                Document document = Jsoup.connect(url).get();
                String fileName = document.title().toString().replace(" ", "_").replace("|", "_"); 
                saveToHtml(document.text(), fileName);
                saveToText(url + Settings.DELIMITER + document.text(), fileName);
                urls.add(url);
                for (Element page: document.select("a[href]")) {
                    String link = page.attr("abs:href");
                    startCrawler(link, depth+1);
                }
            } catch(java.net.UnknownHostException| org.jsoup.HttpStatusException e) {
            }catch (Exception e) {
                System.err.printf("[%s] %s\n",url, e);
                e.printStackTrace();
            }
        }
        return urls.size() > 0;
    }


    public static void saveToHtml(String document, String fileName) {
        try(
            FileWriter fw = new FileWriter(Settings.HTML_PATH + File.separator+ fileName + ".html");
            PrintWriter pw = new PrintWriter(fw);
        ) {
            pw.write(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveToText(String document, String fileName) {
        try(
            FileWriter fw = new FileWriter(Settings.TEXT_PATH + File.separator+ fileName +  ".txt");
            PrintWriter pw = new PrintWriter(fw);
        ) {
        	
            String data = document.toLowerCase();
            pw.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
