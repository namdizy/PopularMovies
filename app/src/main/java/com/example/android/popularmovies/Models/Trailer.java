package com.example.android.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nnamdi on 1/14/2018.
 */

public class Trailer implements Parcelable {

    private String Id;
    private String Key;
    private String Name;

    public Trailer(){

    }

    public Trailer(Parcel parcel){
        this.Id = parcel.readString();
        this.Key = parcel.readString();
        this.Name = parcel.readString();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.Id);
        parcel.writeString(this.Key);
        parcel.writeString(this.Name);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public Object createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Object[] newArray(int i) {
            return new Trailer[i];
        }
    };
}
