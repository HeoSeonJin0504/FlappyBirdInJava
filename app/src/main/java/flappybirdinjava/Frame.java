package flappybirdinjava;

import java.awt.*;
import javax.swing.*;

public class Frame extends JFrame {
    public Frame() {
        setTitle("Flappy Bird In Java");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setMinimumSize(new Dimension(300, 300)); // Dimension - 유니티로 치면 Vector2 

        setLayout(null);
    }
}