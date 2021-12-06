package org.aplas.mylibrary.viewmodels;

import org.aplas.mylibrary.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class User extends BaseObservable{
    private String profile;
    private int color;

    public User(String name, String country, String phone, int color){
        setProfile(name,country,phone);
        setColor(color);
    }

    @Bindable
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        notifyPropertyChanged(BR.color);
    }

    @Bindable
    public String getProfile() { return profile; }

    public void setProfile(String name, String country, String phone) {
        profile = "My Profile - Name: "+name+" - Country: "+country+" - Phone: "+phone+"";
        notifyPropertyChanged(BR.profile);
    }
/*
    private String getColorCode(int clr) {
        return String.format("#%06X", 0xFFFFFF & clr);
    }
*/
}
