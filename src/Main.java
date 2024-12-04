import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
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



public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        //setting window
        primaryStage.setTitle("Hello World!");
        //creating button
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        //button on click
        btn.setOnAction((ActionEvent event) -> {
            System.out.println("Hello World!");
        });
        //layout for root
        StackPane root = new StackPane();
        //adding button to layout
//        root.getChildren().add(btn);
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
        root.getChildren().add(fileSelectionButton);
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
        primaryStage.setScene(new Scene(root, 300, 250));
        //show
        primaryStage.show();
    }
    private void readCSV(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line); // Print each line of the CSV file
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }
}