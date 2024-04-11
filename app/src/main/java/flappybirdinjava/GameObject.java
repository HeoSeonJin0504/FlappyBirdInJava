package flappybirdinjava;

import java.awt.*;
import javax.swing.*;

public class GameObject {
    
}

class BackgroundPanel extends JPanel {
    // 무엇을 만들 것인가? (목표)
    // 우리가 무엇을 가져올 수 있는가? (입력)
    // 가공을 하여 어떤 것을 줄 것인가? (출력)
    Image imgBackground = new ImageIcon(Main.getPath("/sprites/background.png")).getImage();
    private final int WIDTH = imgBackground.getWidth(this);
    private final int HEIGHT= imgBackground.getHeight(this);

    public BackgroundPanel() {
        setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Frame frame = Main.getFrame();
        float sizeMultiply = frame.getSizeMultiply();
        int fixedWidth = (int)(WIDTH * sizeMultiply);
        int fixedHeight = (int)(HEIGHT * sizeMultiply);

        for (int i = 0; i<frame.getWidth() / fixedWidth + 1; i++) {
            g.drawImage(imgBackground, i * fixedWidth, 0, fixedWidth, fixedHeight, this);
        }
    }
}
