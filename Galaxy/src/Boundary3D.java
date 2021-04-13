import java.awt.*;

/**
 * represents a cuboid in space and provides information about the position of a point in this cuboid
 */
public class Boundary3D {

    private final double x, y, z, l, w, h;

    /**
     * describes a cuboid in space, where (x y z) describes the coordinates of the center
     * and (l w h) defines the length (in x) width (in y) and height (in z) of the cuboid
     * Attention the dimensions are interpreted as o right-open intervals.
     * The lower limit therefore does lie in the boundary, the upper limit does not.
     * for example: x-range = [x-l/2, x+l/2)
     *
     *
     *                         .+------+
     *                       .' |    .'|
     *                      +---+--+'  |
     *                      |   |  |   |
     *                    h |  ,+--+---+
     *                      |.'    | .' w
     * Z                    +------+'
     * ^  Y                     l
     * | /
     * +-->X
     *
     * @param x the x-coordinate of the center
     * @param y the y-coordinate of the center
     * @param z the z-coordinate of the center
     * @param l the length in x-direction
     * @param w the width in y-direction
     * @param h the height in z-direction
     */
    public Boundary3D(double x, double y, double z, double l, double w, double h){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.h = h;
        this.l = l;
    }

    /**
     * describes a cube in space, where (x y z) describes the coordinates of the center
     * and edgeLength of the sides
     * Attention the dimensions are interpreted as o right-open intervals.
     * The lower limit therefore does lie in the boundary, the upper limit does not.
     * for example: x-range = [x-edgeLength/2, x+edgeLength/2)
     *
     *
     *                         .+------+
     *                       .' |    .'|
     *                      +---+--+'  |
     *                      |   |  |   |
     *                    h |  ,+--+---+
     *                      |.'    | .' w
     * Z                    +------+'
     * ^  Y                     l
     * | /
     * +-->X
     * @param x the x-coordinate of the center
     * @param y the y-coordinate of the center
     * @param z the z-coordinate of the center
     * @param edgeLength the length of the cube sides
     */
    public Boundary3D(double x, double y, double z, double edgeLength){
        this(x,y,z,edgeLength,edgeLength,edgeLength);
    }

    /**
     * returns the width of this cuboid
     * @return the width as double
     */
    public double getW() {return w;}


    /**
     * checks if a given point is located in the cuboid and returns true or false
     * attention points that are located exactly on the upper limit (right, back, top) of the cuboid are not included
     * @param point the position ot the point
     * @return true -> point is in the cuboid
     */
    public boolean contains(Vector3 point){
        double pX=point.getX(), pY=point.getY(), pZ=point.getZ();

        return (x - l / 2) <= pX && pX < (x + l / 2) &&
                (y - w / 2) <= pY && pY < (y + w / 2) &&
                (z - h / 2) <= pZ && pZ < (z + h / 2);

    }

    /**
     * checks if a given body is located in the cuboid and returns true or false
     * attention points that are located exactly on the upper limit (right, back, top) of the cuboid are not included
     * @param body the give body
     * @return true -> body is in the cuboid
     */
    public boolean contains(CelestialBody body){
        return contains(body.getPosition());
    }

    /**
     * returns the octant in which a point is located
     * according to the scheme:
     *                         _________
     *                        /_4_/_5_ /|
     *                       / 0 / 1 /|5|
     *                      --------- |/|
     *                      | 0 | 1 | +7|
     *                  z=0> ---+---- |/
     *                      | 2 | 3 | /^ y=0
     *                      ----^----
     *                          x=0
     * @param point the point whose position is to be determined
     * @return the octant as integers {-1,0,1,2,3,4,5,6,7}, -1 means that the point is not in the boundary.
     */
    public int whichOctant(Vector3 point){

        // check if the point is in this boundary
        if(!contains(point)) return -1;
        // compute a vector from the center of the boundary to the point
        Vector3 center = new Vector3(x,y,z);
        Vector3 distanceToCenter = point.minus(center);
        double pX=distanceToCenter.getX(), pY=distanceToCenter.getY(), pZ=distanceToCenter.getZ();
        // because we know the point is in the boundary we only have to decide in witch octant he is
        // for this we need only look at the signs of the coordinates
        if(0 <= pX){
            if (0 <= pY){
                if(0 <= pZ){
                    return 5;     //+++
                } else return 7;  //++-
            } else {
                if(0 <= pZ){
                    return 1;     //+-+
                } else return 3;  //+--
            }
        } else {
            if (0 <= pY){
                if(0 <= pZ){
                    return 4;     //-++
                } else return 6;  //-+-
            } else {
                if(0 <= pZ){
                    return 0;     //--+
                } else return 2;  //---
            }
        }

    }

    /**
     * divides the cuboid into 8 equal-sized sub-cubes
     * @return a array that contains the sub-cubes
     */
    public Boundary3D[] split(){
        Boundary3D[] subAreas = new Boundary3D[8];
        subAreas[0]= new Boundary3D(x-(l/4),y-(w/4),z+(h/4),l/2,w/2,h/2);
        subAreas[1]= new Boundary3D(x+(l/4),y-(w/4),z+(h/4),l/2,w/2,h/2);
        subAreas[2]= new Boundary3D(x-(l/4),y-(w/4),z-(h/4),l/2,w/2,h/2);
        subAreas[3]= new Boundary3D(x+(l/4),y-(w/4),z-(h/4),l/2,w/2,h/2);
        subAreas[4]= new Boundary3D(x-(l/4),y+(w/4),z+(h/4),l/2,w/2,h/2);
        subAreas[5]= new Boundary3D(x+(l/4),y+(w/4),z+(h/4),l/2,w/2,h/2);
        subAreas[6]= new Boundary3D(x-(l/4),y+(w/4),z-(h/4),l/2,w/2,h/2);
        subAreas[7]= new Boundary3D(x+(l/4),y+(w/4),z-(h/4),l/2,w/2,h/2);
        return subAreas;
    }

    /**
     * draws the boundary of this cuboid projected on the xy-plane
     * @param color the colour of the boundary
     */
    public void show(Color color){
        StdDraw.setPenColor(color);
        StdDraw.rectangle(x, y, l/2, w/2);
    }

    /**
     * a test method for checking the implementation of this class
     * @param args no arguments expected
     */
    public static void main(String[] args) {
        Boundary3D octant1 = new Boundary3D(0,0,0,10);
        Vector3[] point = new Vector3[8];
        point[0] = new Vector3(-1,-1,1);
        point[1] = new Vector3(1,-1,1);
        point[2] = new Vector3(-1,-1,-1);
        point[3] = new Vector3(1,-1,-1);
        point[4] = new Vector3(-1,1,1);
        point[5]= new Vector3(1,1,1);
        point[6] = new Vector3(-1,1,-1);
        point[7] = new Vector3(1,1,-1);
        System.out.println("Test of contains");
        for(int i=0; i<point.length; i++){
            boolean res = octant1.contains(point[i]);
            if(!res){
                System.out.println("Fehler in der Implementierung");
            } else System.out.println("OK");
        }
        boolean res2 = octant1.contains(new Vector3(Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE));
        if(res2){
            System.out.println("Fehler in der Implementierung");
        } else System.out.println("OK");


        System.out.println("Test of whichOctant");
        for(int i=0; i<point.length; i++){
            int res = octant1.whichOctant(point[i]);
            if(i!=res){
                System.out.println("Fehler in der Implementierung");
            } else System.out.println("OK");
        }
        int res3 = octant1.whichOctant(new Vector3(Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE));
        if(res3!=-1){
            System.out.println("Fehler in der Implementierung");
        } else System.out.println("OK");


        System.out.println("Test of split");
        Boundary3D[] subOctant = octant1.split();
        for(int i=0; i<subOctant.length; i++){
            for(int k=0; k<point.length; k++){
                if(i==k && !subOctant[i].contains(point[k])){
                    System.out.println("Fehler in der Implementierung");
                } else System.out.println("OK");
            }

        }

    }
}
