package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class SearchEngine {
    public static final String PAGES_FILE = "pages.txt";
    public static final String LINKS_FILE = "links.txt";
    private WebGraph web;
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Starting...");
        WebGraph graph = null;
        try {
            System.out.println("Hello");
            graph = WebGraph.buildFromFiles(PAGES_FILE, LINKS_FILE);
            graph.updatePageRank();
            System.out.println("Graph loaded from previous files.");
        }catch (IllegalArgumentException e){
            System.out.println("There was an error building the graph from the files!");
            System.out.println("The program will now terminate...");
        }

        String operation = "";
        while(!operation.equalsIgnoreCase("q")) {
            System.out.println(

                    "\nMenu: \n"+
                    "    (AP) - Add a new page to the graph.\n" +
                    "    (RP) - Remove a page from the graph.\n" +
                    "    (AL) - Add a link between pages in the graph.\n" +
                    "    (RL) - Remove a link between pages in the graph.\n" +
                    "    (P)  - Print the graph.\n" +
                    "    (S)  - Search for pages with a keyword.\n" +
                    "    (Q)  - Quit.");
            System.out.print("Please select an option: ");
            operation = s.nextLine();

            String src;
            String dst;
            switch (operation.toLowerCase().trim()) {
                case "ap":
                    System.out.print("Please enter a URL: ");
                    String URL = s.nextLine();
                    System.out.print("Enter keywords(space separated): ");
                    String keys = s.nextLine();
                    String[] keySplit = keys.trim().split(" ");
                    ArrayList<String> list = new ArrayList<String>(Arrays.asList(keySplit));
                    try {
                        graph.addPage(URL, list);
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "rp":
                    System.out.print("Enter a URL: ");
                    String url = s.nextLine();
                    try {
                        graph.removePage(url);
                        System.out.println("Webpage " + url + " was removed successfully!");
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "al":
                    System.out.print("Please enter a source URL: ");
                    src = s.nextLine();
                    System.out.print("Please enter a destination URL: ");
                    dst = s.nextLine();
                    try {
                        graph.addLink(src, dst);
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                case "rl":
                    System.out.print("Please enter a source URL: ");
                    src = s.nextLine();
                    System.out.print("Please enter a destination URL: ");
                    dst = s.nextLine();
                    graph.removeLink(src, dst);
                    break;
                case "p":
                    System.out.println(" " +
                            "   (I) Sort based on index (ASC)\n" +
                            "    (U) Sort based on URL (ASC)\n" +
                            "    (R) Sort based on rank (DSC)\n");

                    System.out.print("Please enter an option: ");
                    String subOp = s.nextLine();
                    switch (subOp.toLowerCase()){
                        case "i":
                            graph.printTable();
                            break;
                        case "u":
                            Collections.sort(graph.getPages(), new URLComparator());
                            graph.printTable();
                            break;
                        case "r":
                            Collections.sort(graph.getPages(), new RankComparator());
                            graph.printTable();
                            break;
                        default:
                            System.out.println("That option does not exist!");
                            break;
                    }

                    Collections.sort(graph.getPages());
                    break;
                case "s":
                    System.out.print("Please enter a keyword: ");
                    String key = s.nextLine();
                    graph.searchByKeyword(key);
                    break;
            }
        }
    }
}
