import java.awt.*;
import java.util.Random;

public class Simulation {

    private static final Random random = new Random();

    // The main simulation method using instances of other classes.
    public static void main(String[] args) {

        // simulation whit randomly generated bodies

        //to create a uniform starting situation, we set a seed for the pseudo-random numbers
        setRandomSeed(123456789);

        // generate two black holes
        Vector3 center = new Vector3(Config.AREA_SIZE / 8, Config.AREA_SIZE / 8, 0);
        Vector3 axis1 = new Vector3(0, 0, 1);
        CelestialBody blackHole1 = new CelestialBody("black_Hole1", 10000 * Config.MAX_MASS, Config.MAX_RADIUS, center, new Vector3(0, 0, 0), Color.darkGray);

        Vector3 center2 = new Vector3(-Config.AREA_SIZE / 8, -Config.AREA_SIZE / 8, 0);
        Vector3 axis2 = new Vector3(0, 0, -1);
        CelestialBody blackHole2 = new CelestialBody("black_Hole2", 10000 * Config.MAX_MASS, Config.MAX_RADIUS, center2, new Vector3(0, 0, 0), Color.darkGray);

        // calculate the velocity which is necessary for the black holes to orbit each other
        Vector3 center3 = new Vector3(0, 0, 0);
        Vector3 axis3 = new Vector3(0, 0, 1);
        double sumMass = blackHole1.getMass() + blackHole2.getMass();

        sumMass = sumMass * 0.1; //correction factor, the calculation of the orbital velocity is incorrect because we ignore the bodies of the galaxies we will create
        blackHole1.setCurrentMovement(randCircleVel(sumMass, blackHole1.getPosition(), center3, axis3, 0));
        blackHole2.setCurrentMovement(randCircleVel(sumMass, blackHole2.getPosition(), center3, axis3, 0));


        //generate two galaxies
        double galaxySize = Config.AREA_SIZE / 2;
        double galaxyHeight = Config.AREA_SIZE / 4;
        CelestialBody[] galaxy1 = galaxyGenerator(Config.N_BODIES / 2, blackHole1, axis1, galaxySize, galaxyHeight);
        CelestialBody[] galaxy2 = galaxyGenerator(Config.N_BODIES - galaxy1.length, blackHole2, axis2, galaxySize, galaxyHeight);

        //combine the galaxies to one array
        CelestialBody[] testBodies = new CelestialBody[Config.N_BODIES];
        System.arraycopy(galaxy1, 0, testBodies, 0, galaxy1.length);
        System.arraycopy(galaxy2, 0, testBodies, galaxy1.length, galaxy2.length);

        // initiate the StdDraw-class
        StdDraw.setCanvasSize(Config.WINDOW_SIZE, Config.WINDOW_SIZE);
        StdDraw.setScale(-Config.AREA_SIZE/2,Config.AREA_SIZE/2);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(StdDraw.BLACK);


        Boundary3D boundary = new Boundary3D(0,0,0,Config.AREA_SIZE);

        double seconds = 0;

        boolean[] excludedBodies= new boolean[Config.N_BODIES];

        int remainingBodies = Config.N_BODIES;

        // timer for performance measurement
        long start, end, div;

        // simulation loop
        while(remainingBodies>0) { // to simulate one year (seconds < 3600*24*365)

            // set timer
            start = System.currentTimeMillis();

            seconds++; // each iteration computes the movement of the celestial bodies within one second.

            //add all bodies to the tree
            BarnesHutOctree octree = new DynOctree(boundary);

            for(int i=0; i<testBodies.length;i++){
                if(!excludedBodies[i]) {
                    boolean result = octree.add(testBodies[i]);
                    if (!result) {
                        excludedBodies[i] = true;
                        remainingBodies--;
                    }
                }
            }

            // compute the force for each body
            Vector3[] forceList = octree.calculateForce(testBodies);
            // now forceOnBody[i] holds the force vector exerted on body with index i.

            // for each body (with index i): move it according to the total force exerted on it.
            for(int i=0; i<testBodies.length;i++){
                testBodies[i].move(forceList[i]);
            }

            //stop the timer
            end = System.currentTimeMillis();

            // show all movements in StdDraw canvas only every x seconds (to speed up the simulation)
            if (seconds%(10) == 0 || seconds<=1){
                // clear old positions (exclude the following line if you want to draw orbits).
                StdDraw.clear(StdDraw.BLACK);

                for(int i=0; i<testBodies.length; i++) {
                    if (!excludedBodies[i]){
                        testBodies[i].draw(0.005);
                    }
                }

                //draw the boundaries of the tree
                if(false) {
                    StdDraw.setPenRadius(0.001);
                    octree.show();
                }

                //calculate the time we needed for this iteration
                div = end - start;

                // print it to the top right corner
                printStatistic(div,seconds,remainingBodies);

                // show new screen
                StdDraw.show();
            }
        }
        // print the statistic one last time and a message that the simulation has reached its end
        StdDraw.clear(StdDraw.BLACK);
        printStatistic(0,seconds,remainingBodies);
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.text(0,0,"End of Simulation");
        StdDraw.show();
    }

    /**
     * returns a random number in the specified interval
     * @param lower the lower limit
     * @param upper the upper limit
     * @return the random number in the interval
     */
    private static double randInInterval(double lower, double upper){
        if(lower > upper) return upper;
        return lower + random.nextDouble()*(upper-lower);
    }

    /**
     * returns a random normal distributed number in the specified interval
     * @param lower the lower limit
     * @param upper the upper limit
     * @return the random normal distributed number in the interval
     */
    private static double randInGaussInterval(double lower, double upper){
        // in case the parameters make no sens we return the upper limit.
        if(lower > upper) return upper;

        double range=3;

        // the nextGaussian() return numbers witch are normal distributed around 0
        // for our calculation we need da number in a specified rang -> repeat until we get this number
        double gauss;
        do{
            gauss = random.nextGaussian();
        }while (gauss < (-1*range) || gauss > range);

        gauss = (gauss+range)/(2*range);
        return lower + gauss*(upper-lower);
    }

    /**
     * returns a random color
     * @return the color as Color
     */
    private static Color randColor(){
        int[] rgbMin = new int[3];
        // to avoid too dark colours we randomly choose one base colour and set a minimum for its value
        rgbMin[random.nextInt(3)]=150;
        return new Color(random.nextInt(255-rgbMin[0])+rgbMin[0],random.nextInt(255-rgbMin[1])+rgbMin[1],random.nextInt(255-rgbMin[2])+rgbMin[2]);
    }


    /**
     * calculates the necessary velocity for a stable orbit around one point an a given axis
     * @param M the mass of the system
     * @param bodyPos the position of the body
     * @param center the center of the orbit
     * @param axis the axis of the orbit
     * @param deviation a deviation from the ideal velocity
     * @return the velocity vector ot the body
     */
    private static Vector3 randCircleVel(double M, Vector3 bodyPos, Vector3 center, Vector3 axis, double deviation){

        //calculate vector between center and body
        Vector3 centerToBody = bodyPos.minus(center);
        Vector3 vel = centerToBody.crossProduct(axis);
        vel.normalize();
        double r = centerToBody.length();//bodyPos.distanceTo(center);
        double stableVel = Math.sqrt(Config.G*M/r);
        //add some instability into the velocity
        double deviatedVel = (randInInterval(-(deviation/2),+(deviation/2))/100+1)*stableVel;
        vel = vel.times(deviatedVel);
        return vel;
    }


    /**
     * sets the seed for random
     * this is useful for testing the program because the initial scenario will be always the same
     * @param seed a user specified number
     */
    private static void setRandomSeed(long seed){
        random.setSeed(seed);
    }

    /**
     * prints some data in the right top corner of the window.
     * @param iterationTime time required for one iteration in ms
     * @param elapsedTime elapsed time in the simulation
     * @param remainingBodies the number of the remaining bodies in the simulation
     */
    private static void printStatistic(long iterationTime,double elapsedTime, int remainingBodies){
        // print it to the top right corner
        StdDraw.setPenColor(Color.GREEN);
        String msg = String.format("iteration time: %dms",iterationTime);
        double factorXPos = Config.AREA_SIZE/2*(0.97);
        double factorYPos = Config.AREA_SIZE/2*(0.97);
        StdDraw.textRight(factorXPos,factorYPos,msg);
        msg = String.format("passed time: %.0fs",elapsedTime);
        factorYPos = Config.AREA_SIZE/2*(0.92);
        StdDraw.textRight(factorXPos,factorYPos,msg);
        msg = String.format("remaining bodies: %d", remainingBodies);
        factorYPos = Config.AREA_SIZE/2*(0.87);
        StdDraw.textRight(factorXPos,factorYPos,msg);

    }

    /**
     * generates a galaxy around a given body
     * the generated bodies will orbit around this given body
     * @param nBodies number of bodies to be generated
     * @param centerBody the given body
     * @param axis the axis for the orbit of the other bodies
     * @param diameter the diameter of the galaxy
     * @param height the height of the galaxy
     * @return an array white the generated bodies including the given body on position 0
     */
    private static CelestialBody[] galaxyGenerator(int nBodies, CelestialBody centerBody, Vector3 axis, double diameter, double height){

        // to calculate the velocity we need to know the total mass of the galaxy
        // therefor we have to generate the masses of the bodies first
        double[] massList = new double[nBodies];
        massList[0] = centerBody.getMass();
        double totalMass = centerBody.getMass();
        for(int i=1; i<nBodies; i++){
            massList[i]=randInGaussInterval(Config.MIN_MASS,Config.MAX_MASS);
            totalMass+=massList[i];
        }

        Vector3 center = centerBody.getPosition();

        CelestialBody[] galaxyBodies = new CelestialBody[nBodies];
        galaxyBodies[0]=centerBody;
        for(int i=1; i<nBodies; i++){
            Vector3 p = new Vector3(randInGaussInterval(center.getX()-diameter/2,center.getX()+diameter/2),
                    randInGaussInterval(center.getY()-diameter/2,center.getY()+diameter/2),
                    randInGaussInterval(center.getZ()-height/2,center.getZ()+height/2));
            Vector3 v = randCircleVel(totalMass,p,center,axis,20);
            v = v.plus(centerBody.getCurrentMovement());
            CelestialBody bodyToAdd = new CelestialBody("Body_"+i,massList[i],randInGaussInterval(Config.MIN_RADIUS,Config.MAX_RADIUS),p,v,randColor());
            galaxyBodies[i]=bodyToAdd;
        }
        return galaxyBodies;
    }
}
