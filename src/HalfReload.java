import processing.core.PApplet;
import processing.core.PImage;

public class HalfReload extends PowerUps
{
    PApplet sketch;
    PImage halfReload;

    HalfReload(int x, int y, PApplet sketch)
    {
        super(x, y, sketch);
        this.sketch = sketch;
        halfReload = sketch.loadImage("images/HalfReload.png");
    }

    @Override
    public void render()
    {
        sketch.stroke(0);
        sketch.fill(255);
        sketch.rect(x+7, y+1, -18, -18);
        sketch.image(halfReload, x-10, y-16);
    }
}