import processing.core.PApplet;

public class MissileAlien extends Projectiles
{
    PApplet sketch;

    MissileAlien(int x, int y, PApplet sketch)
    {
        super(x, y, sketch);
        this.sketch = sketch;
    }

    @Override
    public void render()
    {
        sketch.fill(255,0,0);
        sketch.stroke(255,0,0);
        sketch.rect(x,y,6,6);

        y = y + 5;
    }
}