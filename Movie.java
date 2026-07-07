package project0;

import java.io.Serializable;

public class Movie implements Serializable {

    private String title;
    private Genre genre;
    private int year;
    private int rating;
    private String review;
    private Status status;
    private boolean favorite;
    private long addedTime;

    public Movie(String title, Genre genre, int year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = 0;
        this.review = "";
        this.status = Status.UNWATCHED;
        this.favorite=false;
        this.addedTime = System.currentTimeMillis();
    }

    public String getTitle() { return title; }
    public Genre getGenre() { return genre; }
    public int getYear() { return year; } 
    public int getRating() { return rating; }
    public String getReview() { return review; }
    public Status getStatus() { return status; }
    public boolean isFavorite() {	return favorite; }
    
    public void setTitle(String title) { this.title = title; }
    public void setGenre(Genre genre) { this.genre = genre; }
    public void setYear(int year) { this.year = year; }
    public void setRating(int rating) { this.rating = rating; }
    public void setReview(String review) { this.review = review; }
    public void setStatus(Status status) { 
    	this.status = status;
    if(status == Status.UNWATCHED) {
    	this.rating=0;
    	this.review="";
    	}
    }
    public void setFavorite(boolean favorite) { this.favorite=favorite;}
    public long getAddedTime() {
        return addedTime;
    }
    @Override
    public String toString() {
        return title + " (" + year + ") - " + genre + " - " + status;
    }
}