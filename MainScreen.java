package project0;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainScreen {

    private Watchlist watchlist;
    private Stage stage;
    private TableView<Movie> table;
    private ObservableList<Movie> observableMovies;
    private ComboBox<String> genreFilter;
    private ComboBox<String> statusFilter;
    private ComboBox<String> sortBox;
    public MainScreen(Watchlist watchlist, Stage stage) {
        this.watchlist = watchlist;
        this.stage = stage;
        observableMovies = FXCollections
                .observableArrayList(watchlist.getAllMovies());
    } 

    public void show() {

    	Label titleLabel = new Label("🎬 My Movie Watchlist");
    	titleLabel.setStyle(
    		    "-fx-font-size: 22px;" +
    		    "-fx-font-weight: bold;" +
    		    "-fx-text-fill: white;"
    		);

        // Search
        TextField searchField = new TextField();
        searchField.setStyle(
        	    "-fx-background-color: #2c2c2c;" +
        	    "-fx-text-fill: white;" +
        	    "-fx-prompt-text-fill: #aaaaaa;"
        	);
        searchField.setPromptText("Search by title...");
        searchField.textProperty().addListener(
            (obs, old, newVal) -> {
                refreshTable(watchlist.searchByTitle(newVal));
        });

        // Genre filter
        genreFilter = new ComboBox<>();
        genreFilter.getItems().add("ALL");
        for (Genre g : Genre.values()) {
            genreFilter.getItems().add(g.toString());
        }
        genreFilter.setValue("ALL");
        genreFilter.setOnAction(e -> applyFilters());
        
        genreFilter.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-text-fill: white;");
            }
        });
        

        // Status filter
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("ALL", "WATCHED", "UNWATCHED");
        statusFilter.setValue("ALL");
        statusFilter.setOnAction(e -> applyFilters());

        statusFilter.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item);
                setStyle("-fx-text-fill: white;");
            }
        });
        
        
        // Sort
        sortBox = new ComboBox<>();
        sortBox.getItems().addAll(
            "NONE",
            "RATING HIGH → LOW",
            "RATING LOW → HIGH",
            "RECENT FIRST"
        );
        sortBox.setValue("NONE");
        sortBox.setOnAction(e -> applyFilters());
        genreFilter.setStyle(
        	    "-fx-background-color: #2c2c2c;" +
        	    "-fx-text-fill: white;"
        	);

        	statusFilter.setStyle(
        	    "-fx-background-color: #2c2c2c;" +
        	    "-fx-text-fill: white;"
        	);

        	sortBox.setStyle(
        	    "-fx-background-color: #2c2c2c;" +
        	    "-fx-text-fill: white;"
        	);
        	
        	sortBox.setButtonCell(new ListCell<String>() {
        	    @Override
        	    protected void updateItem(String item, boolean empty) {
        	        super.updateItem(item, empty);
        	        setText(empty ? "" : item);
        	        setStyle("-fx-text-fill: white;");
        	    }
        	});

        // Table
        table = new TableView<>();
        table.setItems(observableMovies);

        // Table styling
        table.setStyle(
            "-fx-background-color: #1e1e1e;" +
            "-fx-control-inner-background: #1e1e1e;" +
            "-fx-table-cell-border-color: transparent;" +
            "-fx-table-header-border-color: transparent;"
        );

        table.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            table.lookupAll(".column-header-background").forEach(node ->
                node.setStyle("-fx-background-color: #2c3e50;")
            );
        });

        // Row coloring
        table.setRowFactory(tv -> new TableRow<Movie>() {
            @Override
            protected void updateItem(Movie item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else if (item.getRating() >= 4) {
                    setStyle("-fx-background-color: #f1c40f22;");
                } else if (item.getStatus() == Status.WATCHED) {
                    setStyle("-fx-background-color: #1abc9c22;");
                } else {
                    setStyle("");
                }
            }
        });

        // Columns
        TableColumn<Movie, String> titleCol =
            new TableColumn<>("Title");
        titleCol.setCellValueFactory(
            new PropertyValueFactory<>("title"));

        TableColumn<Movie, String> genreCol =
            new TableColumn<>("Genre");
        genreCol.setCellValueFactory(
            new PropertyValueFactory<>("genre"));

        TableColumn<Movie, Integer> yearCol =
            new TableColumn<>("Year");
        yearCol.setCellValueFactory(
            new PropertyValueFactory<>("year"));

        TableColumn<Movie, Integer> ratingCol =
            new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(
            new PropertyValueFactory<>("rating"));

        TableColumn<Movie, String> statusCol =
            new TableColumn<>("Status");
        statusCol.setCellValueFactory(
            new PropertyValueFactory<>("status"));

        TableColumn<Movie, String> reviewCol =
            new TableColumn<>("Review");
        reviewCol.setCellValueFactory(
            new PropertyValueFactory<>("review"));

        TableColumn<Movie, Boolean> favCol =
            new TableColumn<>("⭐");
        favCol.setCellValueFactory(
            new PropertyValueFactory<>("favorite"));
        favCol.setCellFactory(col -> new TableCell<Movie, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item ? "⭐" : "");
            }
        });

        TableColumn<Movie, String> dateCol =
            new TableColumn<>("Added");
        dateCol.setCellValueFactory(cell -> {
            long time = cell.getValue().getAddedTime();
            java.time.LocalDateTime date =
                java.time.Instant.ofEpochMilli(time)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();
            return new javafx.beans.property.SimpleStringProperty(
                date.toString().substring(0, 16)
            );
        });

        table.getColumns().addAll(
            titleCol, genreCol, yearCol,
            ratingCol, statusCol, reviewCol,
            favCol, dateCol);

        // Stats label
        Label statsLabel = new Label();
        statsLabel.setStyle(
            "-fx-font-size: 12px; -fx-text-fill: gray;");
        updateStats(statsLabel);

        // Buttons
        Button addBtn = new Button("➕ Add Movie");
        addBtn.setStyle(
            "-fx-background-color: #2ecc71; -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            new AddMovieForm(watchlist, stage).show();
            refreshTable(watchlist.getAllMovies());
            updateStats(statsLabel);
            DataManager.save(watchlist);
        });

        Button editBtn = new Button("✏ Edit");
        editBtn.setStyle(
            "-fx-background-color: #8e44ad; -fx-text-fill: white;");
        editBtn.setOnAction(e -> {
            Movie selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                new EditMovieForm(selected).show();
                refreshTable(watchlist.getAllMovies());
                updateStats(statsLabel);
                DataManager.save(watchlist);
            }
        });

        Button removeBtn = new Button("🗑 Remove");
        removeBtn.setStyle(
            "-fx-background-color: #e74c3c; -fx-text-fill: white;");
        removeBtn.setOnAction(e -> {
            Movie selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                watchlist.removeMovie(selected);
                refreshTable(watchlist.getAllMovies());
                updateStats(statsLabel);
                DataManager.save(watchlist);
            }
        });

        Button markWatchedBtn = new Button("✅ Mark Watched");
        markWatchedBtn.setStyle(
            "-fx-background-color: #3498db; -fx-text-fill: white;");
        markWatchedBtn.setOnAction(e -> {
            Movie selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setStatus(Status.WATCHED);
                refreshTable(watchlist.getAllMovies());
                updateStats(statsLabel);
                DataManager.save(watchlist);
            }
        });

        Button markUnwatchedBtn = new Button("🔲 Mark Unwatched");
        markUnwatchedBtn.setStyle(
            "-fx-background-color: #95a5a6; -fx-text-fill: black;");
        markUnwatchedBtn.setOnAction(e -> {
            Movie selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setStatus(Status.UNWATCHED);
                refreshTable(watchlist.getAllMovies());
                updateStats(statsLabel);
                DataManager.save(watchlist);
            }
        });

        Button favoriteBtn = new Button("⭐ Toggle Favorite");
        favoriteBtn.setStyle(
            "-fx-background-color: #f1c40f; -fx-text-fill: black;");
        favoriteBtn.setOnAction(e -> {
            Movie selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setFavorite(!selected.isFavorite());
                refreshTable(watchlist.getAllMovies());
                updateStats(statsLabel);
                DataManager.save(watchlist);
            }
        });

        // Layout
        Label searchLabel = new Label("Search:");
        Label genreLabel = new Label("Genre:");
        Label statusLabel = new Label("Status:");
        Label sortLabel = new Label("Sort:");

        searchLabel.setStyle("-fx-text-fill: white;");
        genreLabel.setStyle("-fx-text-fill: white;");
        statusLabel.setStyle("-fx-text-fill: white;");
        sortLabel.setStyle("-fx-text-fill: white;");

        HBox searchBar = new HBox(10,
            searchLabel, searchField,
            genreLabel, genreFilter,
            statusLabel, statusFilter,
            sortLabel, sortBox);

        HBox buttonBar = new HBox(10,
            addBtn, editBtn, removeBtn,
            markWatchedBtn, markUnwatchedBtn,
            favoriteBtn);

        VBox root = new VBox(10,
            titleLabel, searchBar, buttonBar, table, statsLabel);

        root.setStyle("-fx-background-color: #121212; -fx-padding: 15;");

        Scene scene = new Scene(root, 900, 560);
        stage.setTitle("MveList - My Movie Watchlist");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> DataManager.save(watchlist));
        stage.show();
    }
    private void refreshTable(java.util.ArrayList<Movie> list) {
        observableMovies.setAll(list);
    }

    private void updateStats(Label statsLabel) {
        int total = watchlist.getTotalCount();
        int watched = watchlist
            .filterByStatus(Status.WATCHED).size();
        int unwatched = watchlist
            .filterByStatus(Status.UNWATCHED).size();
        double avgRating = watchlist.getAverageRating();
        int favorites = watchlist.getFavoriteCount();
        statsLabel.setText(
        	    "Total: " + total +
        	    "  |  Watched: " + watched +
        	    "  |  Unwatched: " + unwatched +
        	    "  |  ⭐ Avg Rating: " + String.format("%.1f", avgRating) +
        	    "  |  ❤️ Favorites: " + favorites
        	);
    }

    private void applyFilters() {
        java.util.ArrayList<Movie> result =
            watchlist.getAllMovies();

        // Genre filter
        if (!genreFilter.getValue().equals("ALL")) {
            Genre g = Genre.valueOf(genreFilter.getValue());
            result = watchlist.filterByGenre(g);
        }

        // Status filter
        if (!statusFilter.getValue().equals("ALL")) {
            Status s = Status.valueOf(statusFilter.getValue());
            result = watchlist.filterByStatus(s);
        }

        // 🔥 SORTING LOGIC
        if (sortBox.getValue().equals("RATING HIGH → LOW")) {
            result.sort((a, b) -> b.getRating() - a.getRating());
        } else if (sortBox.getValue().equals("RATING LOW → HIGH")) {
            result.sort((a, b) -> a.getRating() - b.getRating());
        }
        else if (sortBox.getValue().equals("RECENT FIRST")) {
            result.sort((a, b) ->
                Long.compare(b.getAddedTime(), a.getAddedTime()));
        }

        refreshTable(result);
    }
}