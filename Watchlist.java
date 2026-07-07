package project0;

import java.io.Serializable;
import java.util.ArrayList;

public class Watchlist implements Serializable {

    private ArrayList<Movie> movies;

    public Watchlist() {
        movies = new ArrayList<>();
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
    }

    public ArrayList<Movie> getAllMovies() {
        return movies;
    }

    public ArrayList<Movie> searchByTitle(String keyword) {
        ArrayList<Movie> result = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getTitle().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                result.add(m);
            }
        }
        return result;
    }

    public ArrayList<Movie> filterByGenre(Genre genre) {
        ArrayList<Movie> result = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getGenre() == genre) {
                result.add(m);
            }
        }
        return result;
    }

    public ArrayList<Movie> filterByStatus(Status status) {
        ArrayList<Movie> result = new ArrayList<>();
        for (Movie m : movies) {
            if (m.getStatus() == status) {
                result.add(m);
            }
        }
        return result;
    }

    public int getTotalCount() {
        return movies.size();
    }
    public double getAverageRating() {
        int sum = 0;
        int count = 0;

        for (Movie m : movies) {
            if (m.getStatus() == Status.WATCHED && m.getRating() > 0) {
                sum += m.getRating();
                count++;
            }
        }

        if (count == 0) return 0;

        return (double) sum / count;
    }
    
    public int getFavoriteCount() {
        int count = 0;

        for (Movie m : movies) {
            if (m.isFavorite()) {
                count++;
            }
        }

        return count;
    }
    
    
}