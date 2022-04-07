package com.search.engine;

import java.io.File;

public class Settings {

    public static final String HTML_PATH = "Files"+ File.separator+ "htmlFiles"; 
    public static final String TEXT_PATH = "Files"+ File.separator+ "TextFiles"; 
    public static final String  TAG = "1.0.0";
    public static final Integer MAX_DEPTH = 2;
    public static final String CHARSET_NAME = "UTF-8"; // other wise BoyerMoore search wont work
    public static final String DELIMITER = "::";
    public static final Integer RadixSize = 255; // for BoyerMoore search taking ascii format
    public static final Integer NumberOfSearch = 10;
    public static final Integer AltWordDistance = 3;  // edit distance alogrithm
}