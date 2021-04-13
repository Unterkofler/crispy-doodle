/**
 * represents a node in a dynamically bound octree
 */
public interface DynOctreeNodeInterface {
    /**
     * adds a body to the tree
     * @param body the body to add
     * @return the resulting tree
     */
    DynOctreeNodeInterface add(CelestialBody body);

    /**
     * calculate the force on this body
     * @param body the body
     * @return a vector that represents the force
     */
    Vector3 calculateForce(CelestialBody body);

    /**
     * draws the boundary of each not empty leaf node
     */
    void show();

    /**
     * returns the number of bodies in this node
     * @return the number of bodies
     */
    int getSize();
}
