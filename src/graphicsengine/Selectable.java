/* 
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-04-13 pergamon
 *	Formatted sytax.
 *
 */
package graphicsengine;

/**
 * All objects that implements this interface is possible to be selected by
 * the user.
 */
public interface Selectable {

	/**
	 * If this object is selected or not..
	 *
	 * @return true if this object is selected, otherwise false.
	 */
	public boolean isSelected();

	/**
	 * Set this object as selected or not selected.
	 *
	 * @param selected true if this object shall be selected or false if not.
	 */
	public void setSelected(boolean selected);

	/**
	 * Does a check if the inparameter position is within this object's area.
	 *
	 * @param screenX The x-position to check against.
	 * @param screenY The y-position to check against.
	 * @return true if the position was within this objects area, otherwise
	 * false.
	 */
	public boolean hitTest(int screenX, int screenY);

	/**
	 * Does a check if this object is within the rectangle that are
	 * inparameters.
	 *
	 * @param screenLeftX The top left x-position of the rectangle to check
	 * against, inclusive bound, in screen coordinates.
	 * @param screenTopY The top left y-position of the rectangle to check
	 * against, inclusive bound, in screen coordinates.
	 * @param screenRightX The bottom right x-position of the rectangle to
	 * check against, exclusive bound, in screen coordinates.
	 * @param screenBottomY The bottom right y-position of the rectangle to
	 * check against, exclusive bound, in screen coordinates.
	 * @return true if this object was within the rectangle, otherwise false.
	 */
	public boolean hitTest(int screenLeftX, int screenTopY, int screenRightX,
			int screenBottomY);

}
