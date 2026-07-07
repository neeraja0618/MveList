package project0;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditMovieForm {

    private Movie movie;

    public EditMovieForm(Movie movie) {
        this.movie = movie;
    }

    public void show() {
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("Edit Movie - " + movie.getTitle());

        // Pre filled with existing values
        Label titleLabel = new Label(movie.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; " +
            "-fx-font-size: 14px;");

        ComboBox<Integer> ratingBox = new ComboBox<>();
        ratingBox.getItems().addAll(0, 1, 2, 3, 4, 5);
        ratingBox.setValue(movie.getRating());

        TextArea reviewField = new TextArea();
        reviewField.setText(movie.getReview());
        reviewField.setPromptText("Your thoughts...");
        reviewField.setPrefRowCount(3);
        reviewField.setWrapText(true);

        ComboBox<Status> statusBox = new ComboBox<>();
        statusBox.getItems().addAll(Status.values());
        statusBox.setValue(movie.getStatus());

        Label messageLabel = new Label();

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white;");
        saveBtn.setOnAction(e -> {
            movie.setRating(ratingBox.getValue());
            movie.setReview(reviewField.getText().trim());
            movie.setStatus(statusBox.getValue());
            messageLabel.setText("✅ Saved!");
            messageLabel.setStyle("-fx-text-fill: green;");
        });

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> formStage.close());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Movie:"), 0, 0);
        grid.add(titleLabel, 1, 0);
        grid.add(new Label("Rating (1-5):"), 0, 1);
        grid.add(ratingBox, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(statusBox, 1, 2);
        grid.add(new Label("Review:"), 0, 3);
        grid.add(reviewField, 1, 3);
        grid.add(saveBtn, 1, 4);
        grid.add(messageLabel, 1, 5);
        grid.add(closeBtn, 1, 6);

        Scene scene = new Scene(grid, 380, 350);
        formStage.setScene(scene);
        formStage.showAndWait();
    }
}
