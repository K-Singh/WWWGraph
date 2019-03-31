package com.company;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The WebPage class represents a a WebPage in the WebGraph. Each page has an index, rank, a list of words associated
 * with it, and a URL.
 *
 * @author Kirat Singh | E-mail: kirat.singh@stonybrook.edu | ID:112320621
 */
public class WebPage implements Comparable{
    private int index;
    private int rank;
    private ArrayList<String> keywords;
    private String URL;

    /**
     * The empty constructor for the WebPage
     */
    public WebPage() {

    }

    /**
     * This constructor for the WebPage assigns all instance variables a value
     * @param ind
     *      The index
     * @param ran
     *      The rank
     * @param keys
     *      The list of keywords
     * @param link
     *      The URL
     */
    public WebPage(int ind, int ran, ArrayList<String> keys, String link) {
        index = ind;
        rank = ran;
        keywords = keys;
        URL = link;
    }

    /**
     * Prints out the WebPage in a table ready form. It holds info about the index, URL, and rank, and contains space
     * to insert the links.
     * @return
     *      A string representing info about the page.
     */
    @Override
    public String toString() {

        String base = String.format(" %-5d| %-20s | %-3d |***| ", index, URL, rank);
        Object[] keys = keywords.toArray();
        for (int i = 0; i < keys.length; i++) {
            if (i != keys.length - 1) {
                base += keys[i] + ",";
            } else {
                base += keys[i];
            }
        }
        return base;
    }

    /**
     * Sets the keywords for the Page
     * @param keywords
     *      The list of words.
     */
    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Sets the index
     * @param index
     *      The new index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Sets the rank
     * @param rank
     *      The new rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Sets the URL
     * @param URL
     *      The new URL
     */
    public void setURL(String URL) {
        this.URL = URL;
    }

    /**
     * @return
     * Gets the index of the page
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return
     * Returns the keywords associated with the page
     */
    public Collection<String> getKeywords() {
        return keywords;
    }

    /**
     * @return
     * Returns the URL of the page
     */
    public String getURL() {
        return URL;
    }

    /**
     * @return
     * Returns the rank of the page
     */
    public int getRank() {
        return rank;
    }

    /**
     * Compares the webpage by index
     * @param o
     *      The webpage to compare
     * @return
     *      Returns an int representing the value of the webpage in comparison to the given object
     */
    public int compareTo(Object o){
        WebPage otherPage = (WebPage)o;
        if(otherPage.getIndex() == this.getIndex()){
            return 0;
        }else if(otherPage.getIndex() > this.getIndex()){
            return -1;
        }else
            return 1;
    }
}
