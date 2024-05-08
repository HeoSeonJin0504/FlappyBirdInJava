package flappybirdinjava;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

import javax.swing.*;

public abstract class GameObject extends JLabel {
    private Image image;
    private int image_Width, image_Height;
    protected int x;
    protected int y;

    public GameObject(Image image) {
        super();
        this.image = image;
        setOpaque(false);

        image_Width = image.getWidth(null);
        image_Height = image.getHeight(null);
    }

    public void update() {
        float sizeMultiply = Main.getSizeMultiply();
        setSize((int) (image_Width * sizeMultiply), (int) (image_Height * sizeMultiply));
    }

    @Override
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        int fixedX = (int) (x * Main.getSizeMultiply());
        int fixedY = (int) (y * Main.getSizeMultiply());
        super.setLocation(fixedX, fixedY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    public int getImageWidth() {
        return image.getWidth(null);
    }

    public int getImageHeight() {
        return image.getHeight(null);
    }

    public void setImage(Image image) {
        this.image = image;
        image_Width = image.getWidth(null);
        image_Height = image.getHeight(null);
        repaint();
    }

    public boolean isColiided(GameObject object) {
        Rectangle rectThis = new Rectangle(getX(), getY(), getWidth(), getHeight());
        Rectangle rectObject = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight());
        return rectThis.intersects(rectObject);
    }
} // GameObject class

class BackgroundPanel extends JPanel {
    private Image imgBackground = new ImageIcon(Main.getPath("/sprites/background.png")).getImage();
    private final int WIDTH = imgBackground.getWidth(null);
    private final int HEIGHT = imgBackground.getHeight(null);

    public void update() {
        for (Component k : getComponents()) {
            GameObject obj = (GameObject) k;
            obj.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        float sizeMultiply = Main.getFrame().getSizeMultiply();
        int fixedWidth = (int) (WIDTH * sizeMultiply);
        int fixedHeight = (int) (HEIGHT * sizeMultiply);

        g.drawImage(imgBackground, 0, 0, fixedWidth, fixedHeight, this);
    }
} // BackgroundPanel class

class Bird extends GameObject {
    private final static Image image = new ImageIcon(Main.getPath("/sprites/bird_midflap.png")).getImage();

    private float jump = 0f;
    private final float GRAVITY = 3f;
    private final float G_FORCE = 0.5f;

    public Bird() {
        super(image);
    }

    @Override
    public void update() {
        super.update();

        if (jump > -GRAVITY) {
            jump -= G_FORCE;
        } else {
            jump = -GRAVITY;
        }

        y = Main.clamp((int) (y - jump), 0, 472 - image.getHeight(null));
        setLocation(x, y);
    }

    public void jump() {
        if (Main.getFrame().isGameOver() == false) {
            jump = 10;
        }
    }
} // Bird class

class Pipe extends GameObject {
    private int speed = 1;
    public static final int MIN_HEIGHT = 50;
    private Bird bird;
    public final String TAG;

    public Pipe(Image image, String tag) {
        super(image);
        bird = Main.getFrame().getBird();
        TAG = tag;
    }

    @Override
    public void update() {
        super.update();

        // Move
        x -= speed;
        setLocation(x, y);

        // Remove
        if (x <= -50) {
            getParent().remove(this);
        }

        // Collision
        if (Main.getFrame().isGameOver() || getX() + getWidth() < bird.getX()) {
            return;
        }
        if (isColiided(bird)) {
            switch (TAG) {
                case "Pipe":
                    Main.getFrame().gameOver();
                    break;
                case "ScoreAdder":
                    getParent().remove(this);
                    Main.getFrame().addScore();                    
                    break;
            }
        }
    }
} // Pipe class

class PipeDown extends Pipe {
    private static Image imgPipeDown = new ImageIcon(Main.getPath("/sprites/pipe_down.png")).getImage();

    public PipeDown() {
        super(imgPipeDown, "Pipe");
    }

    @Override
    public void setLocation(int x, int y) {
        int clampY = Main.clamp(y, -imgPipeDown.getHeight(null) + Pipe.MIN_HEIGHT, 0);
        super.setLocation(x, clampY);
    }

}

class PipeUp extends Pipe {
    private static Image imgPipeUp = new ImageIcon(Main.getPath("/sprites/pipe_up.png")).getImage();

    public PipeUp() {
        super(imgPipeUp, "Pipe");
    }

    @Override
    public void setLocation(int x, int y) {
        int clampY = Main.clamp(y, 472 - imgPipeUp.getHeight(null), 472 - Pipe.MIN_HEIGHT);
        super.setLocation(x, clampY);
    }

}

class PipeSpawner {
    public static final int SPAWN_DELAY = 2500;
    public static final int GAP = 100;

    public static void spawnPipe(BackgroundPanel root, int y) {
        if (Main.getFrame().isGameOver()) {
            return;
        }
        PipeUp pipeUp = new PipeUp();
        PipeDown pipeDown = new PipeDown();
        ScoreAdder scoreAdder = new ScoreAdder();

        pipeUp.setLocation(600, y + GAP);
        pipeDown.setLocation(600, y - GAP - pipeDown.getImageHeight());
        scoreAdder.setLocation(642, y - scoreAdder.getImageHeight() / 2);

        root.add(pipeUp);
        root.add(pipeDown);
        root.add(scoreAdder);
    }
}

class ScoreAdder extends Pipe {

    public ScoreAdder() {
        super(getImage(), "ScoreAdder");
    }

    public static Image getImage() {
        // width = 10
        // height = frame.getHeight * 2 = 512 * 2 = 1024
        // x, y = spawner
        BufferedImage buffImage = new BufferedImage(10, 1024, BufferedImage.TYPE_INT_ARGB);
        return new ImageIcon(buffImage).getImage();
    }

}

class ScoreText extends GameObject {
    private static final Image[] aryImage = {
        new ImageIcon(Main.getPath("/sprites/0.png")).getImage(),
        new ImageIcon(Main.getPath("/sprites/1.png")).getImage(),
        new ImageIcon(Main.getPath("/sprites/2.png")).getImage(),
        new ImageIcon(Main.getPath("/sprites/3.png")).getImage(),
        new ImageIcon(Main.getPath("/sprites/4.png")).getImage(),
        new ImageIcon(Main.getPath("/sprites/5.png")).getImage(),
        new ImageIcon(Main.getPath("/sprites/6.png")).getImage(),
        new ImageIcon(Main.getPath("/sprites/7.png")).getImage(),
        new ImageIcon(Main.getPath("/sprites/8.png")).getImage(),
        new ImageIcon(Main.getPath("/sprites/9.png")).getImage()
    };

    private class Margin {
        public static final int X = 3;
        public static final int Y = 12;
    }

    private int score = 0;
    private Image image;

    public ScoreText() {
        super(aryImage[0]);
        updateImage();
    }

    public void addScore(int score) {
        try {
            this.score += score;
            updateImage();
        }
        catch(Exception e) {}
    }

    public void updateImage() {
        int[] parsedScore = Stream.of(String.valueOf(score).split("")).mapToInt(Integer::parseInt).toArray();
        int height = aryImage[0].getHeight(null) + Margin.Y;
        BufferedImage newImage = new BufferedImage(512, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = newImage.createGraphics();

        int offset = 0;
        for(int k : parsedScore) {
            offset += aryImage[k].getWidth(null) + Margin.X;
        }
        int x = 256 - offset / 2;
        for (int k : parsedScore) {
            Image imgNumber = aryImage[k];
            graphics.drawImage(imgNumber, x, Margin.Y, null);
            x += imgNumber.getWidth(null) + Margin.X;
        }
        graphics.dispose();

        new ImageIcon(newImage).getImage();

        setImage(new ImageIcon(newImage).getImage());
    }
}