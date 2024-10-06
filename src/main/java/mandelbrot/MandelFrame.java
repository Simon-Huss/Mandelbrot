package mandelbrot;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import javax.swing.*;
import static mandelbrot.Main.PIXELS;
import javax.swing.event.ChangeEvent;

public class MandelFrame extends JFrame{
   
    public int[] randomColors = new int[]{0, 0, 0};
    public int[] sliderValues = new int[]{25, 0, 0, 0}; // First is for iterations, rest are for RGB sliders
    
    private final JLabel[] sliderLabels = new JLabel[4];
    private final JLabel iterationStatus;
    private final JLabel colorStatus;
    private final String[] sliderText = new String[]{"Increment", "Red", "Green", "Blue"};
    private final String[] buttonText = new String[]{"Iter+", "Iter-", "Profile+", "Profile-", "Random (s)", "Reset (r)"};
    private final Mandelbrot mb;
    private final MandelPanel mandelPanel;
    
    public MandelFrame(Mandelbrot mandelbrot){   
        
        // Setting values and creating JPanel
        mb = mandelbrot;
        mandelPanel = new MandelPanel(this, mb);
        mandelPanel.setPreferredSize(new Dimension(PIXELS, PIXELS));
        getContentPane().setPreferredSize(new Dimension(PIXELS + 300, PIXELS));
                
        iterationStatus = new JLabel(" Iterations: " + mb.iterations);
        colorStatus = new JLabel(" Color profile: " + mandelPanel.colorPick);
        
        JButton[] buttons = createButtons(); // Creating the buttons
        JSlider[] sliders = createSliders(sliderValues); // Creating the sliders
        
        addZoom(mandelPanel); // Adding zoom handlers
        addKeyboardListeners(); // Adding keyboard handlers
        
        JPanel statusPanel = new JPanel(new GridLayout(1, 2));
        statusPanel.add(iterationStatus);
        statusPanel.add(colorStatus);
                
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2));        
        buttonPanel.add(buttons[0]); // Iter+
        buttonPanel.add(buttons[2]); // Profile+
        buttonPanel.add(buttons[1]); // Iter-
        buttonPanel.add(buttons[3]); // Profile-
        
        JPanel controlPanel = new JPanel(new GridLayout(8, 1, 0, 50));
        controlPanel.setPreferredSize(new Dimension(300, PIXELS));
        controlPanel.add(statusPanel);
        controlPanel.add(buttonPanel);
        
        // Adding the sliders to the control panel
        for (JSlider js: sliders)
            controlPanel.add(js);
        
        controlPanel.add(buttons[4]); // Randomize button
        controlPanel.add(buttons[5]); // Reset button
        
        // Adding the MandelPanel and controlPanel to the JFrame
        add(mandelPanel);
        add(controlPanel, BorderLayout.EAST);
        
        setTitle("Mandelbrot");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        
        // Placing the window in the center of the screen and making sure it appears on top of everything, but doesn't necessarily have to stay on top
        setLocationRelativeTo(null);
        setAlwaysOnTop(true); 
        setVisible(true);
        setAlwaysOnTop(false);
        
  }
    
    private JSlider[] createSliders(int[] increments){
        int[] high = new int[]{500, 255, 255, 255};
        int[] low = new int[]{1, 0, 0, 0};
        JSlider[] sliders = new JSlider[4];

        // Creating sliders and adding text
        for (int i = 0; i < 4; i++){
            sliders[i] = new JSlider(low[i], high[i], low[i]);
            sliders[i].setPaintLabels(true);
            Hashtable<Integer, JLabel> text = new Hashtable<Integer, JLabel>();
            text.put(low[i], new JLabel(Integer.toString(low[i])));
            text.put(high[i]-5, new JLabel(Integer.toString(high[i])));
            sliderLabels[i] = new JLabel("  " + sliderText[i] + ": " + increments[i] + "    ");
            text.put((int)(high[i]/2), sliderLabels[i]);
            sliders[i].setLabelTable(text);
            sliders[i].setFocusable(false);
        }

        // Adding change listeners for the sliders
        for (int j = 0; j < 4; j++){
            int loopvalue = j;
            sliders[j].addChangeListener((ChangeEvent e) -> {
                int val = (int)((JSlider)e.getSource()).getValue();
                sliderValues[loopvalue] = val;
                sliderLabels[loopvalue].setText(sliderText[loopvalue] + ":  " + val);

                // Skip the iteration slider and only do this for the color sliders
                if (loopvalue == 0)
                    return;

                if (mandelPanel.colorPick == 8)
                    sliderValues[loopvalue] = val;

                if (!sliders[loopvalue].getValueIsAdjusting())
                    repaint();
            });
        }
    
        return sliders;
    }
  
    private void addZoom(JPanel jp){
        jp.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                int x = e.getX();
                int y = e.getY();   

                if (mandelPanel.showCoordinateAxes)
                    mandelPanel.showCoordinateAxes = false;

                // Left-click => Zoom in
                if(e.getButton() == MouseEvent.BUTTON1)
                   mb.Zoom(x, y, true);

                // Right-click => Zoom out
                if(e.getButton() == MouseEvent.BUTTON3)
                   mb.Zoom(x, y, false);

                repaint();
              }
        });
    }
    
    private void addKeyboardListeners(){
          addKeyListener(new KeyListener(){
            
            @Override
            public void keyPressed(KeyEvent e) {
                char ch = Character.toLowerCase(e.getKeyChar());

                // Reset button
                if (ch == 'r')
                    mb.Restart();
                
                // Coordinate button
                else if (ch == 'c')
                    mandelPanel.showCoordinateAxes = !mandelPanel.showCoordinateAxes;        
                
                // Randomize button
                else if (ch == 's')
                {
                    for (int i = 0; i < 3; i++)
                        randomColors[i] = (int)(Math.random()*255);
                    mandelPanel.colorPick = 7;
                    colorStatus.setText(" Color profile: " + mandelPanel.colorPick);
                }
                
                // Control selected color using arrow keys
                else if (e.getKeyCode() == KeyEvent.VK_UP)
                    mandelPanel.colorPick++;
                
                else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    mandelPanel.colorPick--;
                
                mandelPanel.validateColorPickValue();
                
                colorStatus.setText("Color profile: " + mandelPanel.colorPick);
                repaint();
            } //KeyPressed
            
            @Override
            public void keyReleased(KeyEvent e) {
                // Not implemented
            }
                
            @Override
            public void keyTyped(KeyEvent e) {
                // Not implemented
            }
    
        }); //Key listener
    }
  
    private JButton[] createButtons(){
        JButton[] buttons = new JButton[6];
        for (int i = 0; i < 6; i++){
            buttons[i] = new JButton(buttonText[i]);
            buttons[i].setFocusable(false);
        }
        
        // Changing iteration amount where we avoid underflow (1 iteration minimum) as well as use the sign to differentiate between increasing/decreasing iteration count
        for (int i = 0; i < 2; i++){
            int sign = i == 0 ? 1 : -1;
            buttons[i].addActionListener(e ->{
                mb.Reiterate(mb.iterations + sign * sliderValues[0] > 0 ? mb.iterations + sign * sliderValues[0] : 1);
                iterationStatus.setText(" Iterations: " + mb.iterations);
            repaint();
            });
        }

        // Change color profiles
        for (int i = 2; i < 4; i++){
            int sign = i == 2? 1: -1;

            buttons[i].addActionListener(e ->{
                mandelPanel.colorPick += sign;
                mandelPanel.validateColorPickValue();
                colorStatus.setText(" Color profile: " + mandelPanel.colorPick);
            repaint();
            });
        }
        
        // Randomize button
        buttons[4].addActionListener(e -> {
            for (int i = 0; i < 3; i++)
                randomColors[i] = (int)(Math.random()*255);
            mandelPanel.colorPick = 7;
            colorStatus.setText(" Color profile: " + mandelPanel.colorPick);
            repaint();
        });
        
        // Reset button
        buttons[5].addActionListener(e -> {
            mb.Restart();
            repaint();
        });
                               
        return buttons;
    }
} 