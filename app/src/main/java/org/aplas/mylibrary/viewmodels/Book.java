package org.aplas.mylibrary.viewmodels;

import org.aplas.mylibrary.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Book extends BaseObservable{
    private String bookNumber;
    private String bookTitle;
    private String bookType;
    private String bookYear;

    public Book() {
        setBook(0, "", "", "");
    }

    public Book(int number, String title, String type, String year){
        setBook(number, title,type,year);
    }

    @Bindable
    public String getBookNumber() {
        return bookNumber;
    }

    @Bindable
    public String getBookTitle() {
        return bookTitle;
    }

    @Bindable
    public String getBookType() {
        return bookType;
    }

    @Bindable
    public String getBookYear() {
        return bookYear;
    }

    public void setBook(int number, String title, String type, String year) {
        //Color.par
        this.bookNumber = ordinal(number);
        this.bookTitle = title;
        this.bookType = type;
        this.bookYear = year;
        notifyPropertyChanged(BR.book);
    }

    private String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }
}
