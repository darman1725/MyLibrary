package org.aplas.mylibrary.viewmodels;

import org.aplas.mylibrary.BR;

import java.util.ArrayList;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class BookList extends BaseObservable{
    private ArrayList<String> list;
    private int size;

    public BookList() {
        size=0;
    }

    public BookList(ArrayList<String> list){
        setBookList(list);
    }

    @Bindable
    public ArrayList<String> getList() {
        return list;
    }

    @Bindable
    public int getSize() {
        return size;
    }

    public void setBookList(ArrayList<String> booklist) {
        this.list = booklist;
        this.size = booklist.size();
        notifyPropertyChanged(BR.list);
        notifyPropertyChanged(BR.size);
    }
}
