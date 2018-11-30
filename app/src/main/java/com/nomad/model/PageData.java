package com.nomad.model;

public class PageData {
    private String page;

    private int totalPages;
    private int noOfRecords;

    public String getPage ()
    {
        return page;
    }

    public void setPage (String page)
    {
        this.page = page;
    }

    public int getTotalPages ()
    {
        return totalPages;
    }

    public void setTotalPages (int totalPages)
    {
        this.totalPages = totalPages;
    }


    public int getNoOfRecords() {
        return noOfRecords;
    }

    public void setNoOfRecords(int noOfRecords) {
        this.noOfRecords = noOfRecords;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [page = "+page+", totalPages = "+totalPages+"]";
    }
}
