public interface BarnesHutOctree {
    /**
     * tries to add a body to the octree
     * returns true if the operation was successful
     * false if the body can't be added because he isn't in the area
     * @param body the body to be added
     * @return result of the operation
     */
    boolean add(CelestialBody body);

    /**
     * draws the boundary of each leaf node
     */
    void show();

    /**
     * calculates the force for each body in the given array
     * @param bodyList a array white the bodies
     * @return a vector array white the calculated forces
     */
    Vector3[] calculateForce(CelestialBody[] bodyList);


}
