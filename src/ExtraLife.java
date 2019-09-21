import processing.core.PApplet;

public class ExtraLife extends PowerUps
{
    PApplet sketch;

    ExtraLife(int x, int y, PApplet sketch)
    {
        super(x, y, sketch);
        this.sketch = sketch;
    }

    @Override
    public void render()
    {
        sketch.stroke(0);
        sketch.fill(255);
        sketch.rect(x+7, y+1, -18, -18);
        sketch.stroke(255,0,0);
        sketch.fill(255,0,0);
        sketch.rect(x, y, -4, -16);
        sketch.rect(x-10, y-6, 16, -4);
    }
}