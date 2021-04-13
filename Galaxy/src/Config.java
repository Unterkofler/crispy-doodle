public class Config {

    /**
     * gravitational constant
     */
    public static final double G = 6.6743e-11;


    /**
     * the number of bodies in the simulation
     */
    public static final int N_BODIES=10000;

    /**
     * the theta value for the BarnesHut-algorithm
     */
    public static final double THETA = 1;

    /**
     * the size of the displayed window
     */
    public static final int WINDOW_SIZE = 800;

    /**
     * the minimum mass of a generated body
     */
    public static final double MIN_MASS= 3e23;

    /**
     * the maximum mass of a generated body
     */
    public static final double MAX_MASS = 2e60;

    /**
     * the minimum radius of a generated body
     */
    public static final double MIN_RADIUS =2e3;

    /**
     * the maximum radius of a generated body
     */
    public static final double MAX_RADIUS = 7e8;

    /**
     * the size of the observed area
     */
    public static final double AREA_SIZE = 3e20;

}
