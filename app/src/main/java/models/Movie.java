package models;

public class Movie {

    private String name;
    private String desc;
    private String imgURL;
    private String actor;
    private String year;
    private String country;
    private String rating;
    private String movieURL;
    private String covURL;
    private String genre;


    public Movie(){

    }

    public Movie(String name, String desc, String imgURL, String covURL, String actor, String  rating, String movieURL, String country, String year, String genre) {
        this.name = name;
        this.desc = desc;
        this.country = country;
        this.imgURL = imgURL;
        this.covURL = covURL;
        this.actor = actor;
        this.rating = rating;
        this.movieURL = movieURL;
        this.year = year;
        this.genre = genre;

    }


    public String getCovURL() {
        return covURL;
    }

    public void setCovURL(String covURL) {
        this.covURL = covURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String  getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMovieURL() {
        return movieURL;
    }

    public void setMovieURL(String movieURL) {
        this.movieURL = movieURL;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
