import java.awt.*;

/**
 * represents a collection of bodies and supplies their total mass and centre of gravity
 */
public class CelestialClusterBody implements GeneralBody{
    private double mass;
    private Vector3 positionCOG;

    public CelestialClusterBody(){
        this.mass=0;
        this.positionCOG=new Vector3(0,0,0);
    }

    public CelestialClusterBody(GeneralBody body){
        this.mass=body.getMass();
        this.positionCOG=body.getPosition();
    }

    public double getMass(){
        return mass;
    }

    public Vector3 getPosition(){
        return positionCOG;
    }

    public void addBody(GeneralBody body){

        Vector3 newCOG = positionCOG.times(mass).plus(body.getPosition().times(body.getMass()));
        mass += body.getMass();
        newCOG = newCOG.times(1/mass);
        positionCOG = newCOG;

    }

    public static void main(String[] args) {
        CelestialBody m1 = new CelestialBody("m1",5,10,new Vector3(-100,-200,-300),new Vector3(0,0,0), Color.darkGray);
        CelestialBody m2 = new CelestialBody("m2",2,10,new Vector3(123,456,789),new Vector3(0,0,0), Color.darkGray);

        CelestialClusterBody clusterBody1 = new CelestialClusterBody();
        clusterBody1.addBody(m1);
        clusterBody1.addBody(m2);
        System.out.println(clusterBody1.positionCOG.toString());
        System.out.println("should be: [-36.285714,-12.571429,11.142857]");
        System.out.println(clusterBody1.mass);
        System.out.println(m1.getMass()+m2.getMass());
    }


}
