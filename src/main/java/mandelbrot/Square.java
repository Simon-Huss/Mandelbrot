package mandelbrot;

public class Square{

    // Is the pixel a member of the Mandelbrot set?
    public boolean SetMember = true;

    // How fast does the pixel diverge?
    public double DivergenceRate = 0;

    // Real axis coordinate index
    public int PositionRe = 0;

    // Imaginary axis coordinate index
    public int PositionIm = 0;

    public Square(int Re, int Im)
    {
        this.PositionRe = Re;
        this.PositionIm = Im;
    }
    
}