import processing.core.*;

public class ScoreMultiplier extends PowerUps
{
    PApplet sketch;
    PImage doublePoints;

    ScoreMultiplier(int x, int y, PApplet sketch)
    {
        super(x, y, sketch);
        this.sketch = sketch;
        doublePoints = sketch.loadImage("images/ScoreMultiplier.png");
    }
    @Override
    public void render()
    {
        sketch.stroke(0);
        sketch.fill(255);
        sketch.rect(x+7, y+1, -18, -18);
        sketch.image(doublePoints, x-10, y-16);
    }
}