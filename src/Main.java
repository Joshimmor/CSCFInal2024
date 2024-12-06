import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.event.ActionEvent;

import javafx.stage.FileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    Store state;
    private ObservableList<Student> activeStudents = FXCollections.observableArrayList();
    private ListView<Student> listView = new ListView<>();
    private TextField searchField = new TextField();
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        //setting window
        primaryStage.setTitle("Student Grade Analyzer");
        //creating button

        //layout for root
        StackPane root = new StackPane();
        //File Selection Button and Logic
        Button fileSelectionButton = new Button("Select File");
        fileSelectionButton.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();

            // Set file chooser title
            fileChooser.setTitle("Select CSV File");

            // Set the initial directory (optional)
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            // Filter for CSV files
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            // Show open file dialog
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            readCSV(selectedFile);
        });

        //Student List and Filter
//        searchField.setPromptText("Search for Student");
        searchField.textProperty().addListener((observable,oldValue,newValue )->filterList(newValue));
        listView.setItems(activeStudents);
        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                if (empty || student == null) {
                    setText(null);
                } else {
                    setText(student.getName() + "   -   Grade: " + String.format("%.2f", student.getGrade()));
                }
            }
        });
        VBox dataFilteringView = new VBox(10,searchField,listView,fileSelectionButton);
        dataFilteringView.setPadding(new Insets(20));
        //Adding graph
//        final NumberAxis xAxis = new NumberAxis();
//        final NumberAxis yAxis = new NumberAxis();
//        xAxis.setLabel("Number of Month");
//        //creating the chart
//        final LineChart<Number,Number> lineChart =
//                new LineChart<Number,Number>(xAxis,yAxis);
//
//        lineChart.setTitle("Stock Monitoring, 2010");
//        //defining a series
//        XYChart.Series series = new XYChart.Series();
//        series.setName("My portfolio");
//        //populating the series with data
//        series.getData().add(new XYChart.Data(1, 23));
//        series.getData().add(new XYChart.Data(2, 14));
//        series.getData().add(new XYChart.Data(3, 15));
//        series.getData().add(new XYChart.Data(4, 24));
//        series.getData().add(new XYChart.Data(5, 34));
//        series.getData().add(new XYChart.Data(6, 36));
//        series.getData().add(new XYChart.Data(7, 22));
//        series.getData().add(new XYChart.Data(8, 45));
//        series.getData().add(new XYChart.Data(9, 43));
//        series.getData().add(new XYChart.Data(10, 17));
//        series.getData().add(new XYChart.Data(11, 29));
//        series.getData().add(new XYChart.Data(12, 25));
//        lineChart.getData().add(series);
//        root.getChildren().add(lineChart);
        //creating new scene
        primaryStage.setScene(new Scene(dataFilteringView, 800, 800));
        //show
        primaryStage.show();
    }
    private void readCSV(File file) {
        List<Student> studentList = new ArrayList<Student>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] args = line.split(",");
                Student _student = new Student(args[0],Float.parseFloat(args[1]));
                studentList.add(_student);
            }
            state = new Store(studentList);
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            state = new Store(new ArrayList<Student>());
        }
        activeStudents.addAll(state.students);
    }
    private void filterList(String query) {
        ObservableList<Student> filteredList = FXCollections.observableArrayList();

        // Check if the query matches the name (case insensitive)
        for (Student _student: activeStudents) {
            if (_student.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(_student);
            }
        }

        // Update the ListView with the filtered list
        listView.setItems(filteredList);
    }
}