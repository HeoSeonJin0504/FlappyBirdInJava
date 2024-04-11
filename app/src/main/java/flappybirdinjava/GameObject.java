package flappybirdinjava;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public abstract class GameObject extends JPanel {
    abstract void update();

    public GameObject() {
        setBackground(null);
    }

}

class BackgroundPanel extends JPanel {
    // 무엇을 만들 것인가? (목표)
    // 우리가 무엇을 가져올 수 있는가? (입력)
    // 가공을 하여 어떤 것을 줄 것인가? (출력)
    private Image imgBackground = new ImageIcon(Main.getPath("/sprites/background.png")).getImage();
    private final int WIDTH = imgBackground.getWidth(this);
    private final int HEIGHT = imgBackground.getHeight(this);
    Bird bird = new Bird();

    public BackgroundPanel() {
        setLayout(null);

        bird.setLocation(100, 100);
        bird.setSize(bird.getWidth(), bird.getHeight());
        add(bird);

        addMouseListener(new MyMouseListener());
    }

    public void update() {
        bird.update();
    }

    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            bird.setJumpPower(-5);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Frame frame = Main.getFrame();
        float sizeMultiply = frame.getSizeMultiply();
        int fixedWidth = (int) (WIDTH * sizeMultiply);
        int fixedHeight = (int) (HEIGHT * sizeMultiply);

        for (int i = 0; i < frame.getWidth() / fixedWidth + 1; i++) {
            g.drawImage(imgBackground, i * fixedWidth, 0, fixedWidth, fixedHeight, this);
        }

        bird.repaint();
    }
}

class Bird extends GameObject {
    private Image imgBird = new ImageIcon(Main.getPath("/sprites/bird_midflap.png")).getImage();
    private final int WIDTH = imgBird.getWidth(this);
    private final int HEIGHT = imgBird.getHeight(this);
    private int jumpPower = -1;
    private final int MAX_JUMP_POWER = 2;
    private int y = getY();

    public void update() {
        y = Main.clamp(y + jumpPower, 0, Main.getFrame().getBackgroundPanel().getHeight());
        setLocation(100, y - getHeight());

        if (jumpPower < MAX_JUMP_POWER) {
            jumpPower += 1;
        }

    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public void setJumpPower(int jumpPower) {
        this.jumpPower = jumpPower;

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Frame frame = Main.getFrame();
        float sizeMultiply = frame.getSizeMultiply();
        int fixedWidth = (int) (WIDTH * sizeMultiply);
        int fixedHeight = (int) (HEIGHT * sizeMultiply);
        g.drawImage(imgBird, 0, 0, fixedWidth, fixedHeight, this);
        setSize(fixedWidth, fixedHeight);

    }

}
