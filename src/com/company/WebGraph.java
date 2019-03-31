package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * The WebGraph class represents a graph of the various Webpage and how they relate to one another using indices,
 * ranks, URLS, and keywords.
 *
 * @author Kirat Singh | E-mail: kirat.singh@stonybrook.edu | ID:112320621
 */
public class WebGraph {
    public static final int MAX_PAGES = 40;
    private ArrayList<WebPage> pages;
    private int[][] edges = new int[MAX_PAGES][MAX_PAGES];
    private int currentSize = 0;

    /**
     * Constructs the WebGraph and initializes the initial ArrayList
     */
    public WebGraph(){
        pages = new ArrayList<WebPage>();
    }

    /**
     * Given two files, a WebGraph will be constructed and will be given appropriate indices, ranks, and links.
     * @param pagesFiles
     *      This file represents the various webpages in the graph and the keywords associated with them
     * @param linksFiles
     *      This file represents the various links between pages in the graph
     * @return
     *      Returns a newly made WebGraph with proper rankings, links, and indices
     * @throws IllegalArgumentException
     *      Thrown when there is an error reading the files given.
     */
    public static WebGraph buildFromFiles(String pagesFiles, String linksFiles) throws IllegalArgumentException {
        try {
            if(!pagesFiles.contains(".txt") || !linksFiles.contains(".txt")){
                throw new IllegalArgumentException("That is not a valid .txt file!");
            }
            //Setup
            FileInputStream pageFis = new FileInputStream(pagesFiles);
            InputStreamReader pageIn = new InputStreamReader(pageFis);
            BufferedReader pageRead = new BufferedReader(pageIn);

            FileInputStream linkFis = new FileInputStream(linksFiles);
            InputStreamReader linkIn = new InputStreamReader(linkFis);
            BufferedReader linkRead = new BufferedReader(linkIn);

            //Read Pages
            String pageReader = pageRead.readLine();
            int index = 0;
            WebGraph wg = new WebGraph();

            while(pageReader != null){
                String[] split = pageReader.trim().split(" ");
                if(split[0].contains(".")){
                    ArrayList<String> keys = new ArrayList<String>();
                    for(int i = 1; i < split.length; i++){
                        keys.add(split[i]);
                    }

                    WebPage page = new WebPage(index, 0, keys, split[0]);
                    wg.getPages().add(page);
                    index++;
                    wg.incrementSize();
                    pageReader = pageRead.readLine();
                }else{
                    throw new IllegalArgumentException("\n"+"The file \"" + pagesFiles + "\" lacks the proper format!");
                }
            }

            //Read Links and make Adjacency Matrix

            String linkReader = linkRead.readLine();
            while(linkReader != null){
                String[] split = linkReader.trim().split(" ");
                int from = -1;
                int to = -1;
                if(split[0].contains(".") && split[1].contains(".")){
                    Object[] list = wg.getPages().toArray();
                    for(int i =0; i < list.length; i++) {
                        if(list[i] instanceof WebPage) {
                            WebPage src = (WebPage) list[i];
                            if (split[0].equalsIgnoreCase(src.getURL())) {
                                from = i;
                            }
                            for(int j = 0; j < list.length; j++) {
                                if(list[j] instanceof  WebPage) {
                                    WebPage dst = (WebPage)list[j];
                                    if (split[1].equalsIgnoreCase(dst.getURL())) {
                                        to = j;
                                    }


                                }
                            }
                        }
                    }
                    if (wg.getCurrentSize() < WebGraph.MAX_PAGES && from < wg.getCurrentSize() && to < wg.getCurrentSize()) {
                        wg.getEdges()[from][to] = 1;
                    }
                    linkReader = linkRead.readLine();
                }else{
                    throw new IllegalArgumentException("\n"+"The file \"" + linksFiles + "\" lacks the proper format!");
                }
            }
            wg.updatePageRank();
            return wg;
        }catch(IOException e){
            throw new IllegalArgumentException("\n"+"That file could not be found!");

        }
    }

    /**
     * Adds a page to the WebGraph
     * @param url
     *      The URL of the page
     * @param keywords
     *      The keywords associated with the page
     * @throws IllegalArgumentException
     *      Thro
     */
    public void addPage(String url, ArrayList<String> keywords) throws IllegalArgumentException{
        if(url == null || keywords == null){
            throw new IllegalArgumentException("\n"+"The arguments given are not valid!");
        }

        for(WebPage p : pages){
            if(p.getURL().equalsIgnoreCase(url)){
                throw new IllegalArgumentException("\n"+"That URL already exists!");
            }
        }

        WebPage page = new WebPage(pages.size(),0, keywords, url);
        pages.add(page);
        updatePageRank();
        System.out.println("\n"+url + " successfully added!");
    }

    public void removePage(String url){
        if(url == null){
            throw new IllegalArgumentException("\n"+"The argument given is not valid!");
        }
        int index = -1;

        for(int i = 0; i < pages.size(); i++){
            WebPage p = pages.get(i);
            if(p.getURL().equalsIgnoreCase(url)){
                pages.remove(i);
                index = i;
            }
        }


        if(index == -1){
            return;
        }

        int[][] temp = new int[MAX_PAGES][MAX_PAGES];

        for(int i = 0; i < index; i++){
            for(int j = 0; j < index; j++){
                temp[i][j] = edges[i][j];
            }
        }

        for(int i = index; i < pages.size(); i++){
            for(int j = index; j < pages.size(); j++){
                if(i != MAX_PAGES || j != MAX_PAGES) {
                    temp[i][j] = edges[i+1][j+1];
                }else{
                    temp[i][j] = 0;
                }
            }
        }
        edges = temp;
        for(int i = index; i < pages.size(); i++){
            WebPage page = pages.get(i);
            page.setIndex(page.getIndex()-1);
        }
        updatePageRank();
        System.out.println("\n"+url + " successfully removed!");
    }

    public void addLink(String source, String destination) throws IllegalArgumentException{
        int from = -1;
        int to = -1;

        if(source == null || destination == null){
            throw new IllegalArgumentException("\n"+"Those values for source and/or destination are invalid!");
        }

        for (int i = 0; i < pages.size(); i++) {
            WebPage webPage =  pages.get(i);
            if(webPage.getURL().equalsIgnoreCase(source)){
                from = i;
            }

            if(webPage.getURL().equalsIgnoreCase(destination)){
                to = i;
            }
        }

        if(from == -1 || to == -1){
            throw new IllegalArgumentException("\n"+"Pages with that set of URLs could not be found!");
        }

        edges[from][to] = 1;
        updatePageRank();
        System.out.println("\n"+"Link successfully added from " + source + " to " + destination);
    }



    public void removeLink(String source, String destination){
        int from = -1;
        int to = -1;

        if(source == null || destination == null){
            return;
        }

        for (int i = 0; i < pages.size(); i++) {
            WebPage webPage =  pages.get(i);
            if(webPage.getURL().equalsIgnoreCase(source)){
                from = i;
            }

            if(webPage.getURL().equalsIgnoreCase(destination)){
                to = i;
            }
        }

        if(from == -1 || to == -1){
            return;
        }

        edges[from][to] = 0;
        updatePageRank();
        System.out.println("\n"+"Link removed successfully from " + source + " to " + destination);
    }

    public void updatePageRank(){
        for(WebPage pg : pages){
            pg.setRank(0);
        }
        for(int i = 0; i < pages.size(); i++){
            for(int j = 0; j < pages.size(); j++){
                if(edges[i][j] == 1){
                   // System.out.println("Edge" + i +" Edge" + j);
                    WebPage page = pages.get(j);
                    int rank = page.getRank() + 1;
                    page.setRank(rank);
                }
            }
        }
    }
    public void searchByKeyword(String word){
        boolean found = false;
        String base = "\n" +
                "Index     URL               PageRank \n"+
                "--------------------------------------------";
        for(WebPage page : pages) {
            for(String key : page.getKeywords()) {
                if(key.equalsIgnoreCase(word)) {
                    found = true;
                    base += "\n" + String.format(" %-5d| %-20s | %-3d| ", page.getIndex(), page.getURL(), page.getRank());
                }
            }
        }

        if(found)
            System.out.println(base);
        else
            System.out.println("No search results found for the keyword " + word);
    }
    public void printTable(){
        System.out.println("\n" +
                "Index     URL               PageRank  Links               Keywords\n" +
                "------------------------------------------------------------------------------------------------------");
        for(int i = 0; i < pages.size(); i++){
            WebPage pg = pages.get(i);
            String s = pg.toString();

            String replace = "";
            for(int j = 0; j < pages.size(); j++){
                if(edges[i][j] == 1){
                    replace += j;
                    replace += ", ";
                }
            }
            if(replace.length() > 2) {
                replace = replace.substring(0, replace.length() - 2);
            }
            String msg = s.replace("***", String.format(" %-20s", replace));
            System.out.println(msg);
        }
    }

    public ArrayList<WebPage> getPages() {
        return pages;
    }

    public int[][] getEdges() {
        return edges;
    }

    public void incrementSize(){
        currentSize++;
    }

    public int getCurrentSize() {
        return currentSize;
    }
}
