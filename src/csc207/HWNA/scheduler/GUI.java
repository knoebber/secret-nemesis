package csc207.HWNA.scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
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
 * Allows the user to specify a file to use and when
 * to generate the schedule. 
 * 
*/
public class GUI
    extends Application
{
  File info;
  ArrayList<Date> dates;
  ArrayList<PairSchools> schools;

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
    scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    //wasn't loading before, but this helped me solve the problem:
    // http://stackoverflow.com/questions/22627579/how-load-css-file-in-javafx8
    pane.getStyleClass().add("root");
    //note: used this example to use customization:
    //https://introjava.wordpress.com/2012/03/23/java-fx-2-linear-gradients/
    Rectangle rectangle = new Rectangle(420,250);
    rectangle.setLayoutY(20);
    rectangle.setLayoutX(40);
    pane.getChildren().add(rectangle);
    pane.getStyleClass().add("rectangle");
    
    //make the open button
    Button openResource = new Button("Open School Infomation File");
    
    openResource.setLayoutX(130);
    openResource.setLayoutY(50);
    openResource.getStyleClass().add("button");

    //make the button bigger
    openResource.scaleXProperty().set(1.5);
    openResource.scaleYProperty().set(1.5);
    //add button to pane
    pane.getChildren().add(openResource);

    //make generate button
    Button generate = new Button("Generate Schedule");
    generate.setLayoutY(180);
    generate.setLayoutX(170);
    generate.scaleXProperty().set(1.5);
    generate.scaleYProperty().set(1.5);
    pane.getChildren().add(generate);
    generate.getStyleClass().add("button");
    
    //make a "help" button
    Button help = new Button("Need help?");
    help.setLayoutY(450);
    help.setLayoutX(350);
    help.scaleXProperty().set(1.5);
    help.scaleYProperty().set(1.5);
    pane.getChildren().add(help);
    help.getStyleClass().add("button");
    
    //create a pop up from the help button
    final Popup popup = new Popup();
    popup.setX(300);
    popup.setY(200);
    popup.setHideOnEscape(true);
    Label label = new Label("Need help?");
    //label.getStylesheets().add(getClass().getResource("popup.css").toExternalForm());
    //^causes errors
    label.getStyleClass().add("popup");
    
    //make a text
    Text currentFile = new Text("Current File:");
    currentFile.setX(25);
    currentFile.setY(120);
    currentFile.setFont(Font.font(null, FontWeight.EXTRA_LIGHT, 12));
    pane.getChildren().add(currentFile);

    //shows where we opened the file
    Text openedFile = new Text();
    openedFile.setX(25);
    openedFile.setY(150);
    openedFile.setFont(Font.font(null, FontWeight.EXTRA_LIGHT, 12));
    pane.getChildren().add(openedFile);

    //tells us when it generated the schedule
    Text finished = new Text();
    finished.setX(25);
    finished.setY(385);
    finished.setFont(Font.font(null, FontWeight.EXTRA_LIGHT, 12));
    pane.getChildren().add(finished);
    //shows where the output is
    Text finalPath = new Text();
    finalPath.setX(25);
    finalPath.setY(415);
    finalPath.setFont(Font.font(null, FontWeight.EXTRA_LIGHT, 12));
    pane.getChildren().add(finalPath);
    
    openResource.setOnAction(new EventHandler<ActionEvent>()
      {
        @Override
        public void handle(ActionEvent event) //this is called on click
        {
          //make an object that can open a file
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Resource File");
          info = fileChooser.showOpenDialog(primaryStage);
          openedFile.setText(info.getPath());

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
                  ArrayList<PairSchools> thePairs = Parser.parse(info);
                  ArrayList<ScheduleDate> theDates = Parser.parseDates(info);
                  int schools = Parser.parseSchoolCount(info);
                  Schedule dummy = new Schedule();
                  Schedule test =
                      dummy.generateDummySchedule(thePairs, theDates,schools);
                  ScheduleDate testAddDate = test.allDates.get(3);
                  System.out.println(testAddDate.month);
                  PairSchools testAddPairSchools = test.gameList.get(0).competing;
                  System.out.println("Home School = "+testAddPairSchools.home.name+" Away School="+testAddPairSchools.away.name);
                  System.out.println(UtilsSchedule.canAdd(testAddPairSchools, testAddDate,test));
                  

                  //put the output in the same directory as where the file was chosen
                  //Collections.sort(theDates);
                  //sort the dates so they are in order

                  ScheduleWriter.write(test, info.getParent() + "/schedule.txt");
                  finished.setText("Wrote schedule:"); //tell user that it was wrote
                  finalPath.setText(info.getParent() + "/schedule.txt");
                }//try
              catch (Exception e)
                {
                  e.printStackTrace();
                }//catch
              //Scheduler.generate(schools)
            }//if info !=null

        }//handle
      });
    
    help.setOnAction(new EventHandler<ActionEvent>() 
    {
        @Override
        public void handle(ActionEvent event) {
                popup.show(primaryStage);
        }
    });

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
