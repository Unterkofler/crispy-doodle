/**
 * represents an empty leaf node of an octree
 */
public class DynEmptyNode implements DynOctreeNodeInterface {

    private final Boundary3D area;

    /**
     * creates a new empty leaf node with a given area in space
     * @param area the given area in space
     */
    public DynEmptyNode(Boundary3D area){
        this.area=area;
    }

    /**
     * if we add a body into an empty leaf node it becomes a not empty leaf node
     * @param body the body to add
     * @return  an not empty leaf node
     */
    @Override
    public DynOctreeNodeInterface add(CelestialBody body) {
        //check if the body is in the main area
        if(!area.contains(body.getPosition())) return this;
        return new DynNotEmptyNode(body, this.area);

    }

    /**
     * calculates the force applied to a given body through this node.
     * this is obviously always zero
     * @param body the body
     * @return a zero vector
     */
    @Override
    public Vector3 calculateForce(CelestialBody body) {
        return new Vector3(0,0,0);
    }

    /**
     * draws the boundary of this node
     * because it is an empty node we don't need to draw anything
     */
    @Override
    public void show() {
    }

    /**
     * the number of bodies in an empty node is always 0
     * @return 0
     */
    @Override
    public int getSize() {
        return 0;
    }
}
