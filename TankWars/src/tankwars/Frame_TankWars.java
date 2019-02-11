package tankwars;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author psoderquist
 */
class Frame_TankWars extends JFrame
{
    public Arena arena;
    JPanel controlPanel = new JPanel();
    JButton startButton = new JButton("Start Battle");
    public JCheckBox colorTeams = new JCheckBox("Color Teams");
    public JCheckBox colorBullets = new JCheckBox("Color Bullets");
    public JCheckBox canKillTeammates = new JCheckBox("Can Kill Teammates");
    public JCheckBox bulletsCollide = new JCheckBox("Bullets Collide");
    //public TimerComponent frameTimer;
    public JLabel timerLabel = new JLabel("    Seconds:");
    public JTextField timeAmount = new JTextField("90");
    public JComboBox numLivesBox = new JComboBox();
    public JCheckBox livesCheckBox = new JCheckBox("Lives");
        
    public Frame_TankWars(Arena tiles) 
    {
        timerLabel.setFont(new Font("Serif", Font.PLAIN, 24));

        this.setTitle("Tank Wars");
        
        //this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        arena = tiles;
        arena.setVisible(true);
        arena.frame = this;
        this.add(arena, BorderLayout.CENTER);
        colorTeams.setSelected(true);
        canKillTeammates.setSelected(true);
        
        numLivesBox.addItem(1);
        numLivesBox.addItem(2);
        numLivesBox.addItem(3);
        numLivesBox.addItem(4);
        numLivesBox.setEnabled(false);
        livesCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (livesCheckBox.isSelected())
                {
                    numLivesBox.setEnabled(true);
                }
                else
                {
                    numLivesBox.setEnabled(false);
                }
            }
        });
        
        controlPanel.add(livesCheckBox);
        controlPanel.add(numLivesBox);
        
        controlPanel.add(colorTeams);
        controlPanel.add(colorBullets);
        controlPanel.add(canKillTeammates);
        controlPanel.add(bulletsCollide);
        
        startButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
              arena.timeLimit = Integer.parseInt(timeAmount.getText());
              timerLabel.setText("    " + arena.timeLimit);
              arena.startGame();
              startButton.setEnabled(false);
              numLivesBox.setEnabled(false);
              livesCheckBox.setEnabled(false);
              arena.setTanksToStartValue((int)numLivesBox.getSelectedItem());
              controlPanel.remove(timeAmount);
              controlPanel.validate();
              controlPanel.repaint();
            } 
          } );
        
        
        controlPanel.add(startButton);
        controlPanel.add(timerLabel);
        controlPanel.add(timeAmount);
        
        
        
        this.add(controlPanel, BorderLayout.SOUTH);
        this.arena.paintBoard();
        this.setSize(new Dimension(this.arena.cellWidth*this.arena.numCols + 25,this.arena.cellHeight*this.arena.numRows + 100));
        this.setVisible(true);

        //this.pack();
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        this.setLocation(x, y);
        this.setFocusable(true);
        this.requestFocus();
        addKeyListener(new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {

            Tank t = arena.getControlTank1();
            if (t != null)
            {

                if (e.getKeyCode() == KeyEvent.VK_L) {
                    t.rotateRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_K) {
                    t.rotateLeft();
                }

                if (e.getKeyCode() == KeyEvent.VK_W) {
                    t.moveUp();
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    t.moveLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    t.moveDown();
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    t.moveRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    t.fire();
                }
                
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    t.setBomb();
                }
                
            }
            
            Tank t2 = arena.getControlTank2();
            if (t2 != null)
            {
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
                    t2.rotateRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
                    t2.rotateLeft();
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    t2.moveUp();
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    t2.moveLeft();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    t2.moveDown();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    t2.moveRight();
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD0) {
                    t2.fire();
                }
                if (e.getKeyCode() == KeyEvent.VK_NUMPAD4) {
                    t2.setBomb();
                }
            
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
        }
    });
    }
    
    
    
}