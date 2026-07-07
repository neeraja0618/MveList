package project0;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddMovieForm {

    private Watchlist watchlist;
    private Stage parentStage;

    public AddMovieForm(Watchlist watchlist, Stage parentStage) {
        this.watchlist = watchlist;
        this.parentStage = parentStage;
    }

    public void show() {
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("Add New Movie");

        TextField titleField = new TextField();
        titleField.setPromptText("Movie title");

        ComboBox<Genre> genreBox = new ComboBox<>();
        genreBox.getItems().addAll(Genre.values());
        genreBox.setValue(Genre.ACTION);

        TextField yearField = new TextField();
        yearField.setPromptText("Release year eg: 2023");

        // Rating dropdown 1-5
        ComboBox<Integer> ratingBox = new ComboBox<>();
        ratingBox.getItems().addAll(0, 1, 2, 3, 4, 5);
        ratingBox.setValue(0);
        ratingBox.setPromptText("Rate 1-5 (optional)");

        // Personal review/note
        TextArea reviewField = new TextArea();
        reviewField.setPromptText(
            "Your thoughts about this movie (optional)");
        reviewField.setPrefRowCount(3);
        reviewField.setWrapText(true);

        // Status
        ComboBox<Status> statusBox = new ComboBox<>();
        statusBox.getItems().addAll(Status.values());
        statusBox.setValue(Status.UNWATCHED);

        Label messageLabel = new Label();

        Button saveBtn = new Button("Add Movie");
        saveBtn.setStyle(
            "-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> {
            String title = titleField.getText().trim();
            String yearText = yearField.getText().trim();

            if (title.isEmpty() || yearText.isEmpty()) {
                messageLabel.setText("Please fill title and year!");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            try {
            	int year = Integer.parseInt(yearText);

            	int currentYear = java.time.Year.now().getValue();

            	if (year < 1900 || year > currentYear) {
            	    messageLabel.setText("Enter valid year (1900 - current)");
            	    messageLabel.setStyle("-fx-text-fill: red;");
            	    return;
            	}
            	Movie movie = new Movie(title,
            		    genreBox.getValue(), year);

            		Status status = statusBox.getValue();
            		movie.setStatus(status);

            		if (status == Status.WATCHED) {
            		    movie.setRating(ratingBox.getValue());
            		    movie.setReview(reviewField.getText().trim());
            		} else {
            		    movie.setRating(0);
            		    movie.setReview("");
            		}
                watchlist.addMovie(movie);
                messageLabel.setText("✅ Movie added!");
                messageLabel.setStyle("-fx-text-fill: green;");
                titleField.clear();
                yearField.clear();
                reviewField.clear();
                ratingBox.setValue(0);
                statusBox.setValue(Status.UNWATCHED);
            } catch (NumberFormatException ex) {
                messageLabel.setText("Year must be a number!");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> formStage.close());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Genre:"), 0, 1);
        grid.add(genreBox, 1, 1);
        grid.add(new Label("Year:"), 0, 2);
        grid.add(yearField, 1, 2);
        grid.add(new Label("Rating (1-5):"), 0, 3);
        grid.add(ratingBox, 1, 3);
        grid.add(new Label("Status:"), 0, 4);
        grid.add(statusBox, 1, 4);
        grid.add(new Label("Your Review:"), 0, 5);
        grid.add(reviewField, 1, 5);
        grid.add(saveBtn, 1, 6);
        grid.add(messageLabel, 1, 7);
        grid.add(closeBtn, 1, 8);

        Scene scene = new Scene(grid, 380, 420);
        formStage.setScene(scene);
        formStage.showAndWait();
    }
}