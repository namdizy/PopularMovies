package com.example.android.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nnamdi on 11/28/2017.
 */

public class Movies implements Parcelable {

    private long Id;
    private int VoteAverage;
    private String PosterPath;
    private String OriginalTitle;
    private String Overview;
    private String ReleaseDate;


    public long getId(){
        return Id;
    }
    public void setId(long id){
        this.Id = id;
    }

    public String getPosterPath(){
        return PosterPath;
    }
    public void setPosterPath( String PosterPath){
        this.PosterPath = PosterPath;
    }

    public int getVoteAverage() {
        return VoteAverage;
    }
    public void setVoteAverage(int VoteAverage) {
        this.VoteAverage = VoteAverage;
    }

    public void setOriginalTitle(String OriginalTitle) { this.OriginalTitle = OriginalTitle;}
    public String getOriginalTitle(){
        return OriginalTitle;
    }

    public String getOverview() {
        return Overview;
    }
    public void setOverview(String overview) {
        this.Overview = overview;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }
    public void setReleaseDate(String ReleaseDate) {
        this.ReleaseDate = ReleaseDate;
    }

    public Movies(){

    }
    public Movies(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeLong(this.Id);
       dest.writeInt(this.VoteAverage);
       dest.writeString(this.PosterPath);
       dest.writeString(this.OriginalTitle);
       dest.writeString(this.Overview);
       dest.writeString(this.ReleaseDate);
    }

    public void readFromParcel(Parcel in){
        this.Id = in.readLong();
        this.VoteAverage = in.readInt();
        this.PosterPath = in.readString();
        this.OriginalTitle = in.readString();
        this.Overview = in.readString();
        this.ReleaseDate = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Movies createFromParcel(Parcel in ) {
            return new Movies( in );
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };


}
