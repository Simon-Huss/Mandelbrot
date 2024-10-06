package mandelbrot;

import java.awt.*;
import javax.swing.*;
import static mandelbrot.Main.PIXELS;
import java.util.ArrayList;

public class MandelPanel extends JPanel {
    
    public int colorPick = 1;
    public boolean showCoordinateAxes = false;
    
    private final MandelFrame frame;
    private final Mandelbrot mb;
    
    
    public MandelPanel(MandelFrame frame, Mandelbrot mb){
        this.frame = frame;
        this.mb = mb;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
      
        ArrayList<Square> mandelbrotSet = new ArrayList<>();
      
        // Imaginary axis
        for (int y = 0; y < PIXELS; y++){
            // Real axis
            for (int x = 0; x < PIXELS; x++){
                
                Square currentPixel = mb.MandelbrotSquares[x][y];
                
                // If the current pixel is a SetMember add it to the collection to draw later
                if (currentPixel.SetMember)
                    mandelbrotSet.add(currentPixel);
                
                else{
                    Color newColor = switch (colorPick) {
                        // Black and white
                        case 1 -> new Color(255, 255, 255);
                        // Blue
                        case 2 -> new Color(0, 0, ((int)(currentPixel.DivergenceRate * 255)));
                        // Green
                        case 3 -> new Color(0, ((int)(currentPixel.DivergenceRate * 255)),0);
                        // Red
                        case 4 -> new Color(((int)(currentPixel.DivergenceRate * 255)), 0, 0);
                        // Cyan
                        case 5 -> new Color(0, ((int)(currentPixel.DivergenceRate * 255)),((int)(currentPixel.DivergenceRate * 255)));
                        // Pink
                        case 6 -> new Color((int)(currentPixel.DivergenceRate * 255), 0, (int)(currentPixel.DivergenceRate*140));
                        // Random color
                        case 7 -> new Color((int)(currentPixel.DivergenceRate * frame.randomColors[0]),(int)(currentPixel.DivergenceRate*frame.randomColors[1]), (int)(currentPixel.DivergenceRate*frame.randomColors[2]));
                        // Sliders
                        case 8 -> new Color((int)(currentPixel.DivergenceRate*frame.sliderValues[1]), (int)(currentPixel.DivergenceRate*frame.sliderValues[2]), (int)(currentPixel.DivergenceRate*frame.sliderValues[3]));
                        default -> new Color(0, 0, 0);
                    }; 
                    g.setColor(newColor);
                    g.drawLine(x, y, x, y);
                }
            }
        }
      
        // Draw the members of the Mandelbrot set in black
        g.setColor(Color.BLACK);
        for(Square s : mandelbrotSet)
          g.drawLine(s.PositionRe, s.PositionIm, s.PositionRe, s.PositionIm);

        // If we want to show the coordinate axes (only for default zoom)
        if (showCoordinateAxes){
            if (colorPick == 1)
                g.setColor(Color.BLACK);
            else
               g.setColor(Color.WHITE);
            
          g.drawLine((PIXELS / 2), 0, (PIXELS / 2), PIXELS);
          g.drawLine(0, (PIXELS / 2), PIXELS, (PIXELS / 2));
          g.drawString("Im Z = 2", PIXELS/2+5, 50);     //y-max
          g.drawString("Im Z = -2", PIXELS/2+5, PIXELS-10);   //y-min
          g.drawString("Re Z = 2", PIXELS-50, PIXELS/2-10);   //x-max
          g.drawString("Re Z = -2", 15, PIXELS/2-5);     //x-min
        }
          
       
    } // PaintComponent
    
    // Handle colorPick over- and underflow
    public void validateColorPickValue(){
        if (colorPick == 0)
            colorPick = 8;
        
        else if(colorPick == 9)
            colorPick = 1;
    }
}
