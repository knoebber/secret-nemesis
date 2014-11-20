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
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

    //make the open button
    Button openResource = new Button("Open School Infomation File");
    openResource.setLayoutX(130);
    openResource.setLayoutY(50);

    //make the button bigger
    openResource.scaleXProperty().set(1.5);
    openResource.scaleYProperty().set(1.5);
    //add button to pane
    pane.getChildren().add(openResource);

    //make generate button
    Button generate = new Button("Generate Schedule");
    generate.setLayoutY(320);
    generate.setLayoutX(170);
    generate.scaleXProperty().set(1.5);
    generate.scaleYProperty().set(1.5);
    pane.getChildren().add(generate);

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
                  Schedule dummy = new Schedule();
                  Schedule test =
                      dummy.generateDummySchedule(thePairs, theDates);

                  //put the output in the same directory as where the file was chosen
                  Collections.sort(theDates);
                  //sort the dates so they are in order

                  ScheduleWriter.write(test, info.getParent() + "/schedule.txt");
                  finished.setText("Wrote schedule:"); //tell user that it was wrote
                  finalPath.setText(info.getParent() + "/schedule.txt");
                }//try
              catch (Exception e)
                {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }//catch
              //Scheduler.generate(schools)
            }//if info !=null

        }//handle
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