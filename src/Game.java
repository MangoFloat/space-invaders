import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Game extends PApplet{

    PImage background;
    PImage alienExample;
    PImage defenderExample;
    PImage doublePointsExample;
    PImage shieldExample;
    PImage rapidFireExample;
    int backgroundY = 0;
    int defenderDeltaX = 0;
    float alienDeltaXMultiplier = 0.1f;
    float alienDeltaYMultiplier = 0.03f;
    float alienDeltaX = 0.7f;
    float alienDeltaY = 0.1f;
    int powerUpDeltaY = 3;
    long reloadTimer = millis();
    int reloadDelay = 1100;
    long doublePointsTimer = millis();
    long shieldTimer = millis();
    long rapidFireTimer = millis();
    int powerUpDelay = 7000; // Power up lasts for 7 seconds
    boolean reloading;
    boolean rapidFire = false;
    boolean invincible = false;
    boolean doublePoints = false;
    int score = 0;
    int lives;
    int screen = 0;
    int scoreAdder = 10;
    int highScore;
    int round = 1;
    int time;
    long timer = millis();
    int row;
    int col;
    int rowSize = 8; //8
    int colSize = 3; //3

    Alien[][] setOfAliens = new Alien[rowSize][colSize]; //Declaring and initialising
    ArrayList<Projectiles> projectiles = new ArrayList<Projectiles>();
    ArrayList<PowerUps> powerUps = new ArrayList<PowerUps>();

    Defender defender01; // Declaring a variable

    public static void main(String[] args){
        PApplet.main("Game");
    }

    @Override
    public void setup()
    {
        size(900,900);
        background = loadImage("images/spaceBackground.png"); //loading an image into variables
        alienExample = loadImage("images/alien01.png");
        defenderExample = loadImage("images/defender.png");
        shieldExample = loadImage("images/Shield.png");
        doublePointsExample = loadImage("images/ScoreMultiplier.png");
        rapidFireExample = loadImage("images/HalfReload.png");

        defender01 = new Defender(425, this); // Initialising
    }

    @Override
    public void draw()
    {
        image(background, 0, backgroundY);
        image(background, 0, backgroundY+height);
        backgroundY = backgroundY - 1;

        if( backgroundY < -height)
        {
            backgroundY = 0;
        }

        if(score > highScore)
        {
            highScore = score; // Sets high score
        }

        textSize(20);
        fill(255);
        text("Highscore: " + highScore, 10, 890);

        switch(screen){
            case 0: // Main menu

                newAliens();
                alienDeltaX = 0.7f;
                alienDeltaY = 0.1f;

                for(int i = 100; i <= 500; i = i + 200) // Create a loop of rectangles
                {
                    int colour = 55;
                    if((mouseX > 300 && mouseX < 600) && (mouseY > i && mouseY < i+100)) // Changes colour when highlighted, visual preferences.
                    {
                        colour = 255;
                    }
                    fill(255);
                    rect(300, i, 300, 100);
                    fill(0, 50, colour);
                    rect(300, i, 300, 100, 30, 30, 30, 30);
                }
                textSize(56);
                stroke(255);
                fill(255);
                text("PLAY", 392, 169);
                text("HELP", 392, 369);
                text("EXIT", 392, 569);

                projectiles.clear(); // Reset ~ prevent bugs
                powerUps.clear();
                scoreAdder = 10; // Default values
                reloadDelay = 1100;
                rapidFire = false;
                invincible = false;
                doublePoints = false;
                score = 0;
                lives = 3;
                round = 1;
                break;

            case 1: // Playing screen
                int enemyKilled = 0; // Kill count
                defender01.render();
                defender01.x = defender01.x + defenderDeltaX;

                textSize(20);
                fill(255); //220, 90, 60
                text("Lives: " + lives, 10, 30);
                text("Score: " + score, 750, 30);
                text("Round: " + round, 400, 30);
                text("Active power ups:", 610, 890);
                // Power ups
                if(rapidFire)
                {
                    reloadDelay = 550; // Half of base value (1100)
                    image(rapidFireExample, 860, 875);

                    if(millis() > powerUpDelay + rapidFireTimer)
                    {
                        reloadDelay = 1100;
                        rapidFire = false;
                    }
                }

                if(doublePoints)
                {
                    scoreAdder = 20; // Double of base value(10)
                    image(doublePointsExample, 825, 875);

                    if(millis() > powerUpDelay + doublePointsTimer)
                    {
                        scoreAdder = 10;
                        doublePoints = false;
                    }
                }

                if(invincible)
                {
                    image(shieldExample, 790, 875);

                    if(millis() > powerUpDelay + shieldTimer)
                    {
                        invincible = false;
                    }
                }
                //
                for(Projectiles proj: projectiles) // Renders all projectiles in one loop, placing them inside the alien array loop changes their speed value for some reason ~ ???
                {
                    if(!(proj == null))
                    {
                        proj.render();
                    }
                }

                for(int m = 0; m < powerUps.size(); m++)
                {
                    PowerUps pow = powerUps.get(m);
                    if(!(pow == null))
                    {
                        pow.render();
                        pow.y = pow.y + powerUpDeltaY;
                        if(defender01.powerUp(pow)) // Collision
                        {
                            if(pow.getClass() == ScoreMultiplier.class) // Only filters out the necessary classes, for different power ups
                            {
                                scoreAdder = 2*scoreAdder;
                                powerUps.remove(pow); // Removes the projectile
                                doublePoints = true;
                                doublePointsTimer = millis();
                            }

                            if(pow.getClass() == Shield.class)
                            {
                                powerUps.remove(pow);
                                invincible = true;
                                shieldTimer = millis();
                            }

                            if(pow.getClass() == HalfReload.class)
                            {
                                powerUps.remove(pow);
                                rapidFire = true;
                                rapidFireTimer = millis();
                            }

                            if(pow.getClass() == ExtraLife.class)
                            {
                                powerUps.remove(pow);
                                lives = lives + 1;
                            }
                        }
                    }
                }

                for(row = 0; row < rowSize; row++)
                {
                    for(col = 0; col < colSize; col++)
                    {
                        Alien enemy = setOfAliens[row][col];
                        if(!(enemy == null))
                        {
                            float alienShoot = random(0,10000); // Generate random number to determine whether the alien shoot or not
                            float powerUpDrop = random(0,300); // Generate random number to determine power up drops, between 0 - 300
                            enemy.update();
                            enemy.x = enemy.x + alienDeltaX;
                            enemy.y = enemy.y + alienDeltaY;

                            if(enemy.x > 840 || enemy.x < 0) // Changes alien direction
                            {
                                alienDeltaX = -alienDeltaX;
                            }

                            if(enemy.y > 790) // Game over
                            {
                                screen = 4;
                            }

                            if(alienShoot > 9970) // When alien shoots
                            {
                                projectiles.add(new MissileAlien((int)enemy.x+27, (int)enemy.y+60, this)); // Adds missile to arraylist
                            }

                            for(int m = 0; m < projectiles.size(); m++)
                            {
                                Projectiles proj = projectiles.get(m);
                                if(!(proj == null))
                                {
                                    if(proj.getClass() == MissileDefender.class)
                                    {
                                        if(enemy.isHit(proj))
                                        {
                                            if(powerUpDrop > 0 && powerUpDrop < 20) // All have a (20/300)*100 chance of dropping power ups.
                                            {
                                                powerUps.add(new ExtraLife((int)enemy.x+27, (int)enemy.y+60, this));
                                            }

                                            if(powerUpDrop > 20 && powerUpDrop < 40) // Put different numbers for drops to avoid duplicates.
                                            {
                                                powerUps.add(new HalfReload((int)enemy.x+27, (int)enemy.y+60, this));
                                            }

                                            if(powerUpDrop > 60 && powerUpDrop < 80)
                                            {
                                                powerUps.add(new ScoreMultiplier((int)enemy.x+27, (int)enemy.y+60, this));
                                            }

                                            if(powerUpDrop > 80 && powerUpDrop < 100)
                                            {
                                                powerUps.add(new Shield((int)enemy.x+27, (int)enemy.y+60, this));
                                            }

                                            projectiles.remove(proj);
                                            setOfAliens[row][col] = null;  // enemy = null doesn't work for some reason
                                            score = score + scoreAdder;
                                        }

                                        if(proj.y < 0)
                                        {
                                            projectiles.remove(proj);
                                            score = score - 5; // Lose points when missile misses
                                        }

                                    }

                                    if(proj.getClass() == MissileAlien.class)
                                    {
                                        if(defender01.isHit(proj))
                                        {
                                            projectiles.remove(proj);
                                            if(!(invincible)) // Only lose life if shield is inactive
                                            {
                                                lives = lives - 1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else  //if alien is null then a counter is added.
                        {
                            enemyKilled = enemyKilled + 1;
                        }
                    }
                }

                if(enemyKilled >= 24) // Array is 8 by 3, meaning there will be 24 aliens,
                {
                    timer = millis();
                    screen = 5; // Sets up for next round
                }

                if(defender01.x < 0 || defender01.x > 850) // Makes sure defender stays on screen;
                {
                    defenderDeltaX = 0;
                }

                if(reloading) // Reloading mechanic ~ so player can fire too many bullets
                {
                    if(millis() > reloadTimer + reloadDelay)
                    {
                        reloading = false;
                    }
                }

                if(lives <= 0) // Game over
                {
                    screen = 4;
                }

                break;

            case 2: // Info. screen
                int colour = 55;

                if((mouseX > 300 && mouseX < 600) && (mouseY > 500 && mouseY < 600)) // Changes colour when hovered, visual preference.
                {
                    colour = 255;
                }

                stroke(255);
                rect(300, 500, 300, 100);
                fill(0, 50, colour);
                rect(300, 500, 300, 100, 30, 30, 30, 30);
                powerUps.add(new ExtraLife(180,150, this));
                powerUps.add(new ScoreMultiplier(180,200, this));
                powerUps.add(new Shield(180,250, this));
                powerUps.add(new HalfReload(180,300, this));

                image(alienExample, 460, 390);
                image(defenderExample, 110, 410);

                for(PowerUps pow : powerUps)
                {
                    pow.render();
                }

                fill(255);
                textSize(40);
                text("Power Ups", 200, 50);
                text("Defender(You)", 100, 375);
                text("Alien(Enemy)", 460, 375);
                textSize(20);
                text("Up arrow: Shoot \nLeft arrow: Move left \nRight arrow: Move right", 170, 400);
                text("Shoot these to destroy them \nand earn points, miss \nand you lose points.\nDestroy all to beat the round.", 530, 400);
                text("Collect these to gain extra abilities.", 200, 90);
                textLeading(50); // Sets text spacing between each \n
                text("Extra life: Gain an extra life \nDouble points: Double the points you earn\nShield: Become invulnerable to enemy missiles\nRapid Fire: Shoot twice as fast", 200, 150);
                textSize(50);
                text("RETURN", 353, 570);

                break;

            case 3: // Exit
                exit();

                break;

            case 4: // Game over screen
                textSize(50);
                text("Game Over!", 310, 450);
                colour = 55;
                if((mouseX > 300 && mouseX < 600) && (mouseY > 500 && mouseY < 600))
                {
                    colour = 255;
                }

                stroke(255);
                fill(255);
                rect(300, 500, 300, 100);
                fill(0, 50, colour);
                rect(300, 500, 300, 100, 30, 30, 30, 30);
                fill(255);
                textSize(50);
                text("RETURN", 353, 570);

                break;

            case 5: // Intermediate/splash screen between each round
                time = 4;
                textSize(52);
                fill(255);
                text("Round Complete!", 232, 330);

                if(millis() > timer + 1000)
                {
                    time = time - 1; // timer for count down
                    if(millis() > timer + 2000)
                    {
                        time = time - 1;
                        if(millis() > timer + 3000)
                        {
                            time = time - 1;
                            if(millis() > timer + 4000)
                            {
                                time = time - 1;
                                alienDeltaX = Math.abs(alienDeltaX) + alienDeltaXMultiplier; // Movement of aliens become faster
                                alienDeltaY = Math.abs(alienDeltaY) + alienDeltaYMultiplier;
                                projectiles.clear(); // Remove all projectiles from screen
                                powerUps.clear(); // Remove all power ups from screen
                                round = round + 1; // New round
                                newAliens(); // Fresh new aliens
                                enemyKilled = 0; // Reset kill counter
                                defender01.x = 425; // Reset defender position
                                rapidFire = false; // Reset power ups
                                reloadDelay = 1100;
                                invincible = false;
                                doublePoints = false;
                                scoreAdder = 10;
                                screen = 1; // Back to play
                            }
                        }
                    }
                    text("Next round in..", 260, 430);
                    text(time, 445, 500); // timer
                }

                break;

        }
    }

    void newAliens() // function to create new aliens
    {
        for(row = 0; row < rowSize; row++)
        {
            for(col = 0; col < colSize; col++)
            {
                setOfAliens[row][col] = new Alien(row*100+40, col*90+40, this);
            }
        }
    }

    @Override
    public void mouseClicked()
    {
        switch(screen){
            case 0:
                int screenCount = 1;
                for(int i = 100; i <= 500; i = i + 200) // From screen 0, with 3 rectangles.
                {
                    if((mouseX > 300 && mouseX < 600) && (mouseY > i && mouseY < i+100))
                    {
                        screen = screenCount;
                    }
                    screenCount++;
                }
                break;

            case 2:
                if((mouseX > 300 && mouseX < 600) && (mouseY > 500 && mouseY < 600)) // Return from Information screen
                {
                    screen = 0;
                }

                break;

            case 4:
                if((mouseX > 300 && mouseX < 600) && (mouseY > 500 && mouseY < 600)) // Return from Game over screen
                {
                    screen = 0;
                }
                break;
        }
    }

    @Override
    public void keyPressed()
    {
        if (key == CODED)
        {
            if( keyCode == LEFT && defender01.x > 0)
            {
                defenderDeltaX = -6;
            }
            if( keyCode == RIGHT && defender01.x < 850)
            {
                defenderDeltaX = 6;
            }
            if( keyCode == UP && reloading == false) // Only shoots if not reloading
            {
                projectiles.add(new MissileDefender(defender01.x+22, defender01.y-10, this)); // Adds missile to arraylist
                reloading = true;
                reloadTimer = millis();
            }
        }
    }

    @Override
    public void keyReleased() // When keys are released
    {
        if (key == CODED)
        {
            if(keyCode == LEFT)
            {
                defenderDeltaX = 0;
            }
            if(keyCode == RIGHT)
            {
                defenderDeltaX = 0;
            }
        }
    }
}