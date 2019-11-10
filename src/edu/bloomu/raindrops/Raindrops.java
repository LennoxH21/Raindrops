package edu.bloomu.raindrops;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.concurrent.ThreadLocalRandom;

/**
 *  This JavaFX application displays "Raindrops" (in random range [20, 100]) that are
 *  added by clicking the mouse at the desired location and are filled by a radial
 *  gradient that transitions across three shades of green.
 *
 *  NOTE:
 *  - Clicking the mouse at the desired location causes all previously added
 *  raindrops to become semi-transparent.
 *
 *  - However, if the mouse is clicked inside an already existing raindrop then a new one
 *  is not created but the user can drag an existing raindrop with the mouse.
 *
 * @author Lennox Haynes
 */

public class Raindrops extends Application {

    @Override
    public void start(Stage primaryStage) {

        Pane root = new Pane();
        final int WIDTH = 800;
        final int HEIGHT = 400;
        Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        root.setOnMouseClicked(event -> {

            int x = (int) event.getSceneX(); // X-coordinate of mouse click
            int y = (int) event.getSceneY(); // Y-coordinate of mouse click
            int r = rand.nextInt(20, 100); // Radius for the raindrop

            /*
             Iterate over all circles in the root pane. Each circle will be made
             opaque or semitransparent depending on whether or not it
             contains the point at which the mouse was clicked.
             */
            boolean raindropHere = false; // False if click on a space without a raindrop

            for (Node node: root.getChildren()) {
                if (node.contains(x, y)) {
                    raindropHere = true; // If you click on an existing raindrop
                }
                if (!raindropHere) {
                    node.setOpacity(.25); // reduces the opacity of the raindrop to 25%
                }
            }

            /*
             If the mouse was not clicked inside an already existing circle,
             then add the new circle to the root pane.
             */
            if (!raindropHere) {
                root.getChildren().add(getRainDrop(x, y, r));
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setTitle("Raindrops");
        primaryStage.show();
    }

    /**
     * Returns a circle with a radial gradient fill pattern and
     * an event handler for mouse drag events.
     *
     * @param x X-coordinate of the center of the circle
     * @param y Y-coordinate of the center of the circle
     * @param r Radius of the circle.
     */
    private Circle getRainDrop(int x, int y, int r) {
        Circle newRaindrop = new Circle(x, y, r);
        newRaindrop.setFill(getRadialGradient(x, y, r));

        // Makes the raindrop draggable
        newRaindrop.setOnMouseDragged(event -> {
            newRaindrop.setCursor(Cursor.CLOSED_HAND);
            newRaindrop.setCenterX(event.getSceneX());
            newRaindrop.setCenterY(event.getSceneY());

            // Moves the Radial Gradient with the circle
            newRaindrop.setFill(getRadialGradient((int) event.getSceneX(),
                    (int) event.getSceneY(), r));
        });
        return newRaindrop;
    }

    /**
     * Creates a Radial Gradient that transitions across
     * three shades of green.
     */
    private RadialGradient getRadialGradient (int centerX, int centerY, int radius) {
        return new RadialGradient(0, 0, centerX, centerY, radius,
                false, CycleMethod.REPEAT,
                new Stop(0.33, Color.DARKGREEN.darker()),
                new Stop(0.67, Color.GREEN),
                new Stop(1.0, Color.LIGHTGREEN.brighter()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
