package com.company;

import java.util.Comparator;

public class URLComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        WebPage pageOne = (WebPage)o1;
        WebPage pageTwo = (WebPage)o2;

        return pageOne.getURL().compareToIgnoreCase(pageTwo.getURL());
    }
}
