package mandelbrot;

public class Main {

    // Setting fixed screen size (needs to be a multiple of 2 for optimal performance with zooming)
    public final static int PIXELS = 1024; 

    public static void main(String[] args) {
                
        // Calculate the Mandelbrot set
        Mandelbrot mb = new Mandelbrot();
        
        // Initialize the UI
        MandelFrame mf = new MandelFrame(mb);
    }
}
