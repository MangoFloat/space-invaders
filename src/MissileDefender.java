import processing.core.PApplet;

public class MissileDefender extends Projectiles
{
    PApplet sketch;

    MissileDefender(int x, int y, PApplet sketch)
    {
        super(x, y, sketch);
        this.sketch = sketch;
    }

    @Override
    public void render()
    {
        sketch.fill(255,255,0);
        sketch.stroke(255,255,0);
        sketch.rect(x,y,6,6);

        y = y - 8;
    }

}