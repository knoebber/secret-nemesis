package csc207.HWNA.scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 *
 * @date November 18, 2014
 * 
 * Creates the GUI. 
 * Allows the user to specify a file to use, 
 * the parameters for the algorithm, and when to generate the schedule. 
 * Also provides an explanation of the input format
 * 
 * Got help from
 * http://stackoverflow.com/questions/22627579/how-load-css-file-in-javafx8
 * https://introjava.wordpress.com/2012/03/23/java-fx-2-linear-gradients/
 * http://java-buddy.blogspot.com/2014/05/javafx-example-rectangle-filled-with.html
*/
public class GUI
    extends Application
{
  File info;
  ArrayList<Date> dates;
  ArrayList<PairSchools> schools;
  boolean showingSettings = false;
  int trials = 1000000;
  int permutations = 3;

  /**
   * A template for making a TextField
   * @param prompt, what the text field should say by default
   * @param x, x location on screen
   * @param y, y location on screen
   * @return a positioned TextField
   */
  private TextField makeTextField(String prompt, int x, int y)
  {
    TextField field = new TextField(prompt);
    field.setLayoutX(x);
    field.setLayoutY(y);
    field.setMaxWidth(75);
    return field;
  }

  /**
   * A template for making a Button
   * @param name, the label on the button
   * @param x location on screen
   * @param y location on screen
   * @return a positioned Button
   */
  private Button makeButton(String name, int x, int y)
  {
    Button button = new Button(name);
    button.setLayoutX(x);
    button.setLayoutY(y);
    button.scaleXProperty().set(1.5);
    button.scaleYProperty().set(1.5);
    button.getStyleClass().add("button");
    return button;
  }

  /**
   * a template for creating a blank Text
   * 
   * @param x location on screen
   * @param y location on screen
   * @return
   */
  private Text makeText(int x, int y)
  {
    Text text = new Text();
    text.setLayoutX(x);
    text.setLayoutY(y);
    text.setFont(Font.font(null, FontWeight.EXTRA_LIGHT, 12));
    text.setFill(Color.WHITE);
    text.getStyleClass().add("text");
    return text;
  }

  /**
   * Starts the GUI
   */
  @Override
  public void start(Stage primaryStage)
  {
    //the title of the window
    primaryStage.setTitle("Scheduler");
    primaryStage.setResizable(false);
    Pane pane = new Pane();
    //Pane allows for most basic positioning
    pane.setPadding(new Insets(25, 25, 25, 25));
    Scene scene = new Scene(pane, 500, 500);
    //add css to the scene
    scene.getStylesheets().add(getClass().getResource("application.css")
                                         .toExternalForm());
    //give the css a way to modify the pane
    pane.getStyleClass().add("root");
    /*
     * Make the red to black gradient box
     */
    Stop[] stops =
        new Stop[] { new Stop(0, Color.BLACK), new Stop(1, Color.DARKRED) };
    LinearGradient linearGradient =
        new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops);
    Rectangle rectangle = new Rectangle(420, 250);
    rectangle.setLayoutY(20);
    rectangle.setLayoutX(40);
    rectangle.setFill(linearGradient);
    pane.getChildren().add(rectangle);
    /*
     * Make the buttons
     */
    Button openResource = makeButton("Open School Infomation File", 130, 50);
    Button generate = makeButton("Generate Schedule", 170, 170);
    Button help = makeButton("Need Help?", 350, 450);
    Button settings = makeButton("More Settings", 60, 450);
    pane.getChildren().add(settings);
    pane.getChildren().add(openResource);
    pane.getChildren().add(generate);
    pane.getChildren().add(help);
    /*
     * Make the texts
     */
    Text currentFile = makeText(50, 120);
    currentFile.setText("Current File: ");
    Text openedFile = makeText(50, 150);
    Text finished = makeText(50, 385);
    Text finalPath = makeText(50, 415);
    Text permText = makeText(45, 225);
    Text trialText = makeText(380, 225);
    permText.setText("Permutations per trial");
    trialText.setText("Trials");
    pane.getChildren().add(currentFile);
    pane.getChildren().add(openedFile);
    pane.getChildren().add(finished);
    pane.getChildren().add(finalPath);
    /*
     * Make the textfields
     */
    TextField permutationsField = makeTextField("3", 45, 230);
    TextField trialsField = makeTextField("1000000", 380, 230);

    //create a pop up for the help button
    Pane helpPane = new Pane(); //the pane for the help window
    helpPane.setPadding(new Insets(25, 25, 25, 25));
    Stage helpWindow = new Stage();
    helpWindow.setResizable(false);
    helpWindow.setTitle("Information");
    Scene helpScene = new Scene(helpPane, 565, 280);
    helpWindow.setScene(helpScene);
    helpPane.getStyleClass().add("popup");
    Image img = new Image("exampleschedule.PNG");
    ImageView imgView = new ImageView(img);
    helpPane.getChildren().add(imgView);
    
    /*
     * Below are all the action handlers for the various buttons and text fields 
     */
    openResource.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event) //this is called on click
        {
          //make an object that can open a file
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Resource File");
          info = fileChooser.showOpenDialog(primaryStage);
          if (info != null)
            openedFile.setText(info.getPath());

        }//handle
      });
    permutationsField.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        {
          permutations = Integer.valueOf(permutationsField.getText());
        }//handle
      });
    trialsField.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        {
          trials = Integer.valueOf(trialsField.getText());
        }//handle
      });
    generate.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        {
          if (info != null)//only if info exists
            {
              //schools = Parser.parse(info);
              try
                {
                  Schedule originalSchedule = Parser.parse(info);

                  UtilsSchedule.makeSchedule(originalSchedule, trials,
                                             permutations);

                  ScheduleWriter.write(originalSchedule, info.getParent()
                                                         + "/schedule.txt");

                  finished.setText("Wrote schedule:"); //tell user that it was wrote
                  finalPath.setText(info.getParent() + "/schedule.txt");
                }//try
              catch (Exception e)
                {
                  //e.printStackTrace();
                }//catch
              //Scheduler.generate(schools)
            }//if info !=null
        }//handle
      });
    help.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        {
          helpWindow.show();
        }//handle
      });
    settings.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event)
        {
          showingSettings = !showingSettings;
          if (showingSettings)
            {
              //show the settings
              pane.getChildren().add(permutationsField);
              pane.getChildren().add(trialsField);
              pane.getChildren().add(permText);
              pane.getChildren().add(trialText);
            }//if
          else
            {
              //don't show the settings
              pane.getChildren().remove(permutationsField);
              pane.getChildren().remove(trialsField);
              pane.getChildren().remove(permText);
              pane.getChildren().remove(trialText);
            }//else
        }//handle
      });
    //set the window
    primaryStage.setScene(scene);
    primaryStage.show(); //show the scene
  }//start

  /**
   * The main() method is ignored in correctly deployed JavaFX application.
   * main() serves only as fallback in case the application can not be
   * launched through deployment artifacts, e.g., in IDEs with limited FX
   * support. NetBeans ignores main().
   *
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    launch(args);
  }//main
}//class GUI
