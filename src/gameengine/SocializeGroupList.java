/* 2003-05-08 Martin
 * Formatted the class and implemented the registering methods.
 */
package gameengine;

import gameengine.gameobjects.SocializeGroup;
import java.util.*;

/**
 * A SortableList that can contain only SocializeGroup objects.
 */
public class SocializeGroupList extends SortableList {

	/**
	 * Registers, adds, the given SocializeGroup to the list.
	 *
	 * @param group the SocializeGroup to be registered.
	 */
	public void register(SocializeGroup group) {
		super.register((Object)group);
	}

	/**
	 * Unregisters, removes, the given SocializeGroup from the list.
	 *
	 * @param group the SocializeGroup to be unregistered.
	 */
	public void unRegister(SocializeGroup group) {
		super.unRegister((Object)group);
	}

}