package com.company;

import java.util.Comparator;

public class RankComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        WebPage pageOne = (WebPage)o1;
        WebPage pageTwo = (WebPage)o2;

        if(pageOne.getRank() == pageTwo.getRank()){
            return 0;
        }else if(pageOne.getRank() > pageTwo.getRank()){
            return -1;
        }else
            return 1;
    }
}
