import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

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
        // setting window
        primaryStage.setTitle("Student Grade Analyzer");

        // creating the tabPlane
        TabPane tabPane = new TabPane();

        // Tab for root landing page
        Tab fileSelectionPage = new Tab("file");

        // creating button for data tabs
        Button displayDataButton = new Button("Show data");
        displayDataButton.setOnAction((ActionEvent event) -> {
            // Generate new tab
            Tab dataTab = new Tab("Group Data");
            // inner organization
            VBox dataVBox = new VBox();
            // VBox Spacing
            dataVBox.setSpacing(10);
            dataVBox.setPadding(new Insets(10));
            // add items to be horizontally aligned
            HBox sampleSizeAlignment = new HBox();
            sampleSizeAlignment.setAlignment(Pos.BASELINE_CENTER);
            Text sampleSize = new Text("Number of students: " + activeStudents.size());
            sampleSizeAlignment.getChildren().add(0, sampleSize);
            // margins for sample size
            VBox.setMargin(sampleSizeAlignment, new Insets(5, 0, 0, 0));
            dataVBox.getChildren().add(0, sampleSizeAlignment);
            // center a div
            // dataVBox.setAlignment(Pos.BASELINE_CENTER);
            // graph creation call
            dataHandler(dataVBox);

            // add Average value
            // add VBox layout to tab
            dataTab.setContent(dataVBox);
            // add tab to tab pane
            tabPane.getTabs().add(dataTab);
        });

        // creating button
        // layout for root
        StackPane root = new StackPane();
        // File Selection Button and Logic
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

        // Student List and Filter
        // searchField.setPromptText("Search for Student");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterList(newValue));
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
        VBox dataFilteringView = new VBox(10, searchField, listView, fileSelectionButton, displayDataButton);
        dataFilteringView.setPadding(new Insets(20));

        // add dataFilteringView to first tab
        fileSelectionPage.setContent(dataFilteringView);
        // Adding graph
        // final NumberAxis xAxis = new NumberAxis();
        // final NumberAxis yAxis = new NumberAxis();
        // xAxis.setLabel("Number of Month");
        // //creating the chart
        // final LineChart<Number,Number> lineChart =
        // new LineChart<Number,Number>(xAxis,yAxis);
        //
        // lineChart.setTitle("Stock Monitoring, 2010");
        // //defining a series
        // XYChart.Series series = new XYChart.Series();
        // series.setName("My portfolio");
        // //populating the series with data
        // series.getData().add(new XYChart.Data(1, 23));
        // series.getData().add(new XYChart.Data(2, 14));
        // series.getData().add(new XYChart.Data(3, 15));
        // series.getData().add(new XYChart.Data(4, 24));
        // series.getData().add(new XYChart.Data(5, 34));
        // series.getData().add(new XYChart.Data(6, 36));
        // series.getData().add(new XYChart.Data(7, 22));
        // series.getData().add(new XYChart.Data(8, 45));
        // series.getData().add(new XYChart.Data(9, 43));
        // series.getData().add(new XYChart.Data(10, 17));
        // series.getData().add(new XYChart.Data(11, 29));
        // series.getData().add(new XYChart.Data(12, 25));
        // lineChart.getData().add(series);
        // root.getChildren().add(lineChart);
        // adding tabs to the tab pane
        tabPane.getTabs().addAll(fileSelectionPage);

        // creating new scene
        primaryStage.setScene(new Scene(tabPane, 1400, 1000));
        // show
        primaryStage.show();
    }

    private void readCSV(File file) {
        List<Student> studentList = new ArrayList<Student>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] args = line.split(",");
                Student _student = new Student(args[0], Float.parseFloat(args[1]));
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
        for (Student _student : activeStudents) {
            if (_student.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(_student);
            }
        }

        // Update the ListView with the filtered list
        listView.setItems(filteredList);
    }

    /*
     * @params VBox view, layout for data tab
     * 
     * @returns nothing
     * Function for creating and handling nodes for data tab in the view
     */
    private void dataHandler(VBox view) {
        // create graphs
        // Student individual grades distribution via scatterplot
        scatterPlot(view);
        // Average grade as a linegraph
        lineAverages(view);
        // High/Low grades
        highLowGraph(view);
    }

    /*
     * @params VBox view, layout for data tab
     * 
     * @returns nothing
     * Function for creating the scatter plot and adding it to the view.
     */
    private void scatterPlot(VBox view) {
        final NumberAxis xAxis = new NumberAxis(0, 100, 10);
        final NumberAxis yAxis = new NumberAxis(0, 100, 10);
        final ScatterChart<Number, Number> sc = new ScatterChart<Number, Number>(xAxis, yAxis);
        xAxis.setLabel("Grade ranges");
        yAxis.setLabel("Grades");
        sc.setTitle("Student grade distribution");
        // Chart data
        int index = 0;
        XYChart.Series<Number, Number> gradeData = new XYChart.Series<>();
        gradeData.setName("Student Grades");
        for (Student student : state.students) {
            gradeData.getData().add(new XYChart.Data<>(index++, student.getGrade()));
        }
        sc.getData().add(gradeData);
        // Adjust size
        sc.setPrefSize(800, 600);     
        // Append to the view
        view.getChildren().add(sc);
    }

    /*
     * @params VBox layout to add to
     * 
     * @returns none
     * Generates the linegraph for the student averages
     */
    private void lineAverages(VBox view) {
        final NumberAxis xAxis = new NumberAxis(0, 100, 10);
        final NumberAxis yAxis = new NumberAxis(0, 100, 10);
        // Line chart for average grade
        final LineChart<Number, Number> lc = new LineChart<>(xAxis, yAxis);
        lc.setTitle("Average Grade");
        lc.setLegendVisible(false);
        // prvent animation glitches
        // lc.setAnimated(false);
        // Hide data point symbols
        // lc.setCreateSymbols(false); 
        XYChart.Series<Number, Number> avgGrade = new XYChart.Series<>();
        // avgGrade.setName("Average grade");
        avgGrade.getData().add(new XYChart.Data<>(0, state.average));
        avgGrade.getData().add(new XYChart.Data<>(100, state.average));
        lc.getData().add(avgGrade);
        // set size
        lc.setPrefSize(800, 600);
        view.getChildren().add(lc);
    }

    /*
     * @params VBox view, layout to add to
     * 
     * @returns none
     * Generates bar graphs for high/low grades
     */
    private void highLowGraph(VBox view) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis(0, 100, 5);
        xAxis.setLabel("Grade type");
        yAxis.setLabel("Grades");
        // Create the Bar Chart
        BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle("High/Low Grades");
        // Data for chart
        XYChart.Series<String, Number> highLowSeries = new XYChart.Series<>();
        highLowSeries.setName("Grades");
        highLowSeries.getData().add(new XYChart.Data<>("Low", state.minGrade));
        highLowSeries.getData().add(new XYChart.Data<>("High", state.maxGrade));
        // Add data to chart
        bc.getData().add(highLowSeries);
        // Size chart
        bc.setPrefSize(800, 600);  
        view.getChildren().add(bc);
    }
}