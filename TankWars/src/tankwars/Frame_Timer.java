/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankwars;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author psoderquist
 */
class Frame_Timer extends JFrame {

    public Frame_Timer(Arena arena) {
        TimerComponent timerComp = new TimerComponent(arena);
        this.add(timerComp, BorderLayout.CENTER);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width) - 250;
        final int y = 25;
        this.setLocation(x, y);
        this.setSize(new Dimension(200,100));
        this.setVisible(true);
    }
    
}

class TimerComponent extends JComponent {

    int counter = 0;
    Arena arena;
    Font currentFont;
    Font newFont;
    TimerComponent(Arena arena) {
        this.arena = arena;
        
    }

    @Override
    public void paintComponent(Graphics g) {
        if(g instanceof Graphics2D)
        {
            if (counter < 1)
            {
                currentFont = g.getFont();
                newFont = currentFont.deriveFont(currentFont.getSize() * 5F);
                counter ++;
            }
            g.setFont(newFont);
            Graphics2D g2 = (Graphics2D)g;
            g2.drawString(""+arena.secondsLeft,5,45);
        }
    }
}