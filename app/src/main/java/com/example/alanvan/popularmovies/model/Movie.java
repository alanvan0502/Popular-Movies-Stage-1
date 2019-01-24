package com.example.alanvan.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    public static final String MOVIE_INFO = "movieInfo";

    private int id;
    private String title;
    private String posterPath;
    private String overview;
    private double voteAverage;
    private String releaseDate;

    public Movie() {

    }

    public Movie(int id, String title, String posterPath,
                 String overview, double voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Parcelable implementation
    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>(){

                @Override
                public Movie createFromParcel(Parcel source) {
                    return new Movie(source);
                }

                @Override
                public Movie[] newArray(int size) {
                    return new Movie[0];
                }
            };

    private Movie(Parcel source) {
        id = source.readInt();
        title = source.readString();
        posterPath = source.readString();
        overview = source.readString();
        voteAverage = source.readDouble();
        releaseDate = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
    }
}
