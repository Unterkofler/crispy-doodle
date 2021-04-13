/**
 * represents a generalized body in space and provides information about it
 */
public interface GeneralBody {
    /**
     * return the mass of the body
     * @return the mass
     */
    double getMass();

    /**
     * return the position of the body
     * @return the position as vector
     */
    Vector3 getPosition();
}
