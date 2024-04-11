package flappybirdinjava;

import java.awt.*;
import javax.swing.*;

import com.google.common.primitives.Floats;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;

import java.awt.event.*;

public class Frame extends JFrame {
    BackgroundPanel pnlBackground = new BackgroundPanel();

    // Variable
    private float sizeMultiply = 1.0f;
    private final int ORIGIN_SIZE = 512;

    public Frame() {
        setTitle("Flappy Bird In Java");
        setSize(512, 512);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(256, 256)); // Dimension - 유니티로 치면 Vector2 

        add(pnlBackground);

    }

    public float getSizeMultiply() {
        return sizeMultiply;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int width = getWidth();
        int height = getHeight();

        // width와 height 중 작은 값을 뽑아내고, 이걸 setSize w, h 변수에 집어 넣는다.
        if (width > height) {
             setSize (height, height);
        }
        else {
            setSize(width, width);
        }
        sizeMultiply = (float)getWidth() / (float)ORIGIN_SIZE;
    }
}
