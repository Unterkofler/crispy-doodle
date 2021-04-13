/**
 * represents a node that contains multiple bodies
 */
public class DynOctreeNode implements DynOctreeNodeInterface {

    private final DynOctreeNodeInterface[] octant = new DynOctreeNodeInterface[8];
    private final Boundary3D area;
    private final CelestialClusterBody clusterBody = new CelestialClusterBody();
    private int size=0;

    /**
     * creates a new octree node and his 8 sub nodes
     * @param area the given area in space
     */
    public DynOctreeNode(Boundary3D area){
        this.area=area;
        Boundary3D[] subAreas = area.split();
        for(int i=0; i<octant.length;i++){
            octant[i] = new DynEmptyNode(subAreas[i]);
        }
    }

    /**
     * adds a given body to one of its sub nodes
     * @param body the body to add
     * @return this
     */
    @Override
    public DynOctreeNodeInterface add(CelestialBody body) {
        //check if the body is in the main area
        if(!area.contains(body.getPosition())) return this;
        // determent in which sub node the body should be added
        int posOfBody = area.whichOctant(body.getPosition());
        octant[posOfBody] = octant[posOfBody].add(body);
        // add the body to the cluster, we need that for the force computation
        clusterBody.addBody(body);
        size++;
        return this;
    }

    /**
     * calculates the force acting on a given body through this node or its children nodes
     * according to the BarnesHut algorithm
     * @param body the body
     * @return the cumulative force as vector
     */
    @Override
    public Vector3 calculateForce(CelestialBody body) {
        Vector3 force = new Vector3(0,0,0);
        double r = body.getPosition().distanceTo(clusterBody.getPosition());

        double d = area.getW();
        if(r/d > Config.THETA){
            force = body.gravitationalForce(this.clusterBody);
        } else {
            for (int i = 0; i < octant.length; i++) {
                force = force.plus(octant[i].calculateForce(body));
            }
        }
        return force;
    }

    /**
     * draws the boundary of the sub nodes
     */
    @Override
    public void show() {
        for(int i=0; i<octant.length;i++){
            octant[i].show();
        }
    }

    /**
     * returns how many bodies this node contains
     * @return the number of bodies
     */
    public int getSize(){return size;}
}
