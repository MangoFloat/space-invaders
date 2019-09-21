import processing.core.PApplet;
import processing.core.PImage;

public class Shield extends PowerUps
{
    PApplet sketch;
    PImage shield;

    Shield(int x, int y, PApplet sketch)
    {
        super(x, y, sketch);
        this.sketch = sketch;
        shield = sketch.loadImage("images/Shield.png");
    }

    @Override
    public void render()
    {
        sketch.stroke(0);
        sketch.fill(255);
        sketch.rect(x+7, y+1, -18, -18);
        sketch.image(shield, x-10, y-16);
    }
}