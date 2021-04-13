/**
 * represents a node that contains exactly one body
 */
public class DynNotEmptyNode implements DynOctreeNodeInterface {

    private final CelestialBody body;
    private final Boundary3D area;

    /**
     * creates a non-empty node
     * @param body the contained body
     * @param area the given area in space
     */
    public DynNotEmptyNode(CelestialBody body, Boundary3D area){
        this.body = body;
        this.area = area;
    }


    /**
     * if we add a body into an not empty leaf node, we have to split this node and return an octree node
     * @param body the body to add
     * @return the octree node
     */
    @Override
    public DynOctreeNodeInterface add(CelestialBody body) {
        //check if the body is in the main area
        if(!area.contains(body.getPosition())) return this;
        DynOctreeNode octreeNode = new DynOctreeNode(area);
        octreeNode.add(this.body);
        octreeNode.add(body);
        return octreeNode;
    }

    /**
     * calculates the force applied to a given body through this node.
     * @param body the body
     * @return the calculated force
     */
    @Override
    public Vector3 calculateForce(CelestialBody body) {
        if(this.body.equals(body)){return new Vector3(0,0,0);}
        return body.gravitationalForce(this.body);
    }

    /**
     * draws the boundary of the area
     */
    @Override
    public void show() {
        area.show(body.getColor());
    }

    /**
     * the number of bodies in an non empty node is always 1
     * @return 1
     */
    @Override
    public int getSize() {
        return 1;
    }
}
