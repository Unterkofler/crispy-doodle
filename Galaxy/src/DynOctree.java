/**
 * represents a dynamic implementation of an BarnesHut-Octree
 */
public class DynOctree implements BarnesHutOctree{

    DynOctreeNodeInterface root;
    private final Boundary3D area;

    /**
     * creates a new BarnesHut-Octree white a given area in space
     * @param area the given area
     */
    public DynOctree(Boundary3D area){
        this.area = area;
        root = new DynEmptyNode(area);
    }

    /**
     * adds a body to the tree and returns if the operation succeeded
     * @param body the body to be added
     * @return the result of the operation
     */
    @Override
    public boolean add(CelestialBody body) {
        // if the body is not in the area we can return false immediately
        // this brings also a better performance
        if(!area.contains(body)){return false;}
        // to determine if the tree was changed we simply compare the size before and after add
        int oldSize = root.getSize();
        root = root.add(body);
        return oldSize != root.getSize();
    }

    /**
     * draws the boundaries of the nodes
     */
    @Override
    public void show() {
        root.show();
    }

    /**
     * calculates the force acting on each body in the array,
     * assuming the body itself is part of the tree
     * @param bodyList a array white the bodies
     * @return a array of the forces acting on the bodies
     */
    @Override
    public Vector3[] calculateForce(CelestialBody[] bodyList) {
        Vector3[] forceList = new Vector3[bodyList.length];
        for(int i=0; i<bodyList.length;i++){
            if(area.contains(bodyList[i])) {
                forceList[i] = root.calculateForce(bodyList[i]);
            } else forceList[i] = new Vector3(0,0,0);
        }
        return forceList;
    }
}
