/*
 * 2003-05-09 henko
 * Created. The class originally named ResourceContainer is now called
 * Resources.
 */

package gameengine.gameobjects;

/**
 * ResourceContainer means that it's implementor will have a Resources object.
 * It can return the number of units of a specific resource that is available.
 */
public interface ResourceContainer {

	/**
	 * Returns the number of units of a specific resource that is available.
	 *
	 * @return number o units available.
	 */
	public int getResourceAmount(int resourceType);

}