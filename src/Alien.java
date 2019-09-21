import processing.core.PApplet;
import processing.core.PImage;

public class Alien
{
    PApplet sketch;
    PImage alien01, alien02;
    float x;
    float y;
    int alienLife;
    int counter = 0;

    Alien(float x, float y, PApplet sketch)
    {
        this.sketch = sketch;
        this.x = x;
        this.y = y;

        alien01 = sketch.loadImage("images/alien01.png");
        alien02 = sketch.loadImage("images/alien02.png");
    }

    void display()
    {
        if (counter < 20) sketch.image(alien01, x, y);
        else if(counter < 40) sketch.image(alien02, x, y);
        else {counter =0;}
        counter++;
    }

    boolean isHit(Projectiles proj)
    {
        if(proj != null)
        {
            if(Math.abs((this.x+30)-proj.x) < 30 && Math.abs((this.y+30)-proj.y) < 30)
            {
                return true;
            }
        }
        return false;
    }

    void update()
    {
        display();
    }

}