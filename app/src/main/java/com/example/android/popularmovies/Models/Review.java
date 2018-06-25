package com.example.android.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.IdentityHashMap;

/**
 * Created by Nnamdi on 1/14/2018.
 */

public class Review implements Parcelable{

    private String Id;
    private String Author;
    private String Content;
    private String Url;

    public Review(){

    }

    public Review(Parcel parcel){
        this.Id = parcel.readString();
        this.Author = parcel.readString();
        this.Content = parcel.readString();
        this.Url = parcel.readString();
    }
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.Id);
        parcel.writeString(this.Author);
        parcel.writeString(this.Content);
        parcel.writeString(this.Url);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Review[i];
        }
    };

}
