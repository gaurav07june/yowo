package com.nomad.model;

import java.util.List;

public class BookingResModel {

    private PageData pageData;

    private List<BookingsGeneralModel> bookings;

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(PageData pageData) {
        this.pageData = pageData;
    }

    public List<BookingsGeneralModel> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingsGeneralModel> bookings) {
        this.bookings = bookings;
    }
}
