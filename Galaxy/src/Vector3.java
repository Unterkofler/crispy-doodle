import java.awt.*;

// This class represents vectors in a 3D vector space.
public class Vector3 {

    private double x;
    private double y;
    private double z;

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * returns the value of the x component of this vector
     * @return the x component
     */
    public double getX() {
        return x;
    }

    /**
     * returns the value of the y component of this vector
     * @return the y component
     */
    public double getY() {
        return y;
    }

    /**
     * returns the value of the z component of this vector
     * @return the z component
     */
    public double getZ() {
        return z;
    }


    // Returns the sum of this vector and vector 'v'.
    public Vector3 plus(Vector3 v) {
        return new Vector3(this.x+v.x, this.y+v.y, this.z+v.z);
    }

    // Returns the product of this vector and 'd'.
    public Vector3 times(double d) {
        return new Vector3(this.x*d, this.y*d, this.z*d);
    }

    // Returns the sum of this vector and -1*v.
    public Vector3 minus(Vector3 v) {
        return new Vector3(this.x-v.x, this.y-v.y, this.z-v.z);
    }

    // Returns the Euclidean distance  of this vector
    // to the specified vector 'v'.
    public double distanceTo(Vector3 v) {
        return this.minus(v).length();
    }

    // Returns the length (norm) of this vector.
    public double length() {
        return Math.sqrt(x*x+y*y+z*z);
    }

    // Normalizes this vector: changes the length of this vector such that it becomes one.
    // The direction and orientation of the vector is not affected.
    public void normalize() {
        double length = this.length();
        this.x/=length;
        this.y/=length;
        this.z/=length;
    }

    /**
     * computes the cross product of this an an second vector
     * @param v the other vector
     * @return the cross product as vector
     */
    public Vector3 crossProduct(Vector3 v){
        return new Vector3(this.getY()*v.getZ()-this.getZ()*v.getY(),
                -(this.getX()*v.getZ() - this.getZ()*v.getX()),
                this.getX()*v.getY() - this.getY()*v.getX());
    }

    // Draws a filled circle with the center at (x,y) coordinates of this vector
    // in the existing StdDraw canvas. The z-coordinate is not used.
    public void drawAsDot(double radius, Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(x, y, radius);
    }


    // Returns the coordinates of this vector in brackets as a string
    // in the form "[x,y,z]", e.g., "[1.48E11,0.0,0.0]".
    public String toString() {
        //return "["+x+","+y+","+z+"]";
        return String.format("[%f,%f,%f]",x,y,z);
    }

    // Prints the coordinates of this vector in brackets to the console (without newline)
    // in the form [x,y,z], e.g.,
    // [1.48E11,0.0,0.0]
    public void print() {
        System.out.println(toString());
    }

}
