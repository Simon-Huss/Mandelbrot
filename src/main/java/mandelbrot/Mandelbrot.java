package mandelbrot;
import static mandelbrot.Main.PIXELS;

// The Mandelbrot Set are the complex numbers M = {z : |z_n(z)| < ∞ as n -> ∞}, 
// where z_n is defined by the recursive relation z_{n+1}(z) = z_n^2 + z with z_0 = 0

public class Mandelbrot{
    
    public int iterations = 250;
    public Square[][] MandelbrotSquares;
    
    private double StartRe = -2;
    private double EndRe = 2;
    private double StartIm = -2;
    private double EndIm = 2;
   
    public Mandelbrot(){
        MandelbrotSquares = new Square[PIXELS][PIXELS];
        CalculateMandelbrotSet();
    }
       
    private void CalculateMandelbrotSet(){     
             
        double Re_C, Im_C, Re_Z, Im_Z, Temp_Re, Temp_Im;
       
        double stepSizeRe = (EndRe - StartRe)/PIXELS; 
        double stepSizeIm = (EndIm - StartIm)/PIXELS;
       
        // Imaginary axis
        for (int y = 0; y < PIXELS; y++){
            // Real axis
            for (int x = 0; x < PIXELS; x++){
                
                Square currentPixel = new Square(x, y);
                MandelbrotSquares[x][y] = currentPixel;
                currentPixel.PositionRe = x;
                currentPixel.PositionIm = y;
                
                Re_C = StartRe + x * stepSizeRe;
                Im_C = StartIm + y * stepSizeIm;
                
                Re_Z = Re_C;
                Im_Z = Im_C;
                
                for (int i = 0; i < iterations; i++){
        
                    // If the magnitude (square root) is larger than 2 or eqv. squared sum larger than 4 it will diverge and we can abort early
                    if (Re_Z * Re_Z + Im_Z * Im_Z > 4)
                    {
                       currentPixel.SetMember = false; 
                       
                       // Increase the divergence rate for very quick divergences to amplify the colors in the images
                       if (i > iterations / 10)
                           currentPixel.DivergenceRate = (double)i / iterations;
                       else
                            currentPixel.DivergenceRate = 10.0 * i / iterations;
                       break;
                    } 

                    Temp_Re = Re_Z;
                    Temp_Im = Im_Z;

                    Re_Z = Temp_Re * Temp_Re - Temp_Im * Temp_Im + Re_C;
                    Im_Z = 2 * Temp_Re * Temp_Im + Im_C;

                } // For iterations
            } // For x (real axis)
        } // For y (imaginary axis)
    }
    
    // Resetting standard values
    public void Restart(){
        StartRe = StartIm = -2;
        EndRe = EndIm = 2;
        CalculateMandelbrotSet();
    }
    
    // Re-scales the coordinate axes to get a scaled (by factor 2) center at (x, y)
    public void Zoom(double x, double y, boolean zoomIn){
          
        double tempStartRe = StartRe;
        double tempEndRe = EndRe;
        double tempStartIm = StartIm;
        double tempEndIm = EndIm;

        // Actually want to half or double it here, but afterwards it will be halved again as half the scaling goes to the left and right side respectively
        double scale = zoomIn ? (tempEndRe-tempStartRe) / 4 : (tempEndRe-tempStartRe);

        StartRe = x * (tempEndRe - tempStartRe)/PIXELS + tempStartRe - scale;     
        EndRe = x * (tempEndRe - tempStartRe)/PIXELS + tempStartRe + scale;

        StartIm = y * (tempEndIm - tempStartIm)/PIXELS + tempStartIm - scale;
        EndIm = y * (tempEndIm - tempStartIm)/PIXELS + tempStartIm + scale;

        CalculateMandelbrotSet();
    }
    
    // Change amount of iterations being done
    public void Reiterate(int iter){
       iterations = iter;
       CalculateMandelbrotSet();
    }
    
}