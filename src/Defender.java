import processing.core.*;

public class Defender
{
    PApplet sketch;

    int x;
    int y = 850;
    PImage defender01;

    Defender(int x, PApplet sketch)
    {
        this.sketch = sketch;
        this.x = x;

        defender01 = sketch.loadImage("images/defender.png");
    }

    public void render()
    {
        sketch.image(defender01, x, y);
    }

    public boolean isHit(Projectiles proj)
    {
        if(proj != null)
        {
            if(Math.abs((this.x+25)-proj.x) < 30 && Math.abs((this.y+10)-proj.y) < 10)
            {
                return true;
            }
        }
        return false;
    }

    public boolean powerUp(PowerUps pow)
    {
        if(pow != null)
        {
            if(Math.abs((this.x+25)-pow.x) < 30 && Math.abs((this.y+10)-pow.y) < 10)
            {
                return true;
            }
        }
        return false;
    }
}