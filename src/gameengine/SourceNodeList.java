/* 2003-05-08 Martin
 *	Formatted the class and implemented the registering methods.
 *
 * 2003-05-05 Martin
 *	Formatted the code.
 */
package gameengine;

import gameengine.gameobjects.SourceNode;
import java.util.*;

/**
 * A SortableList that can contain only ConversionNode objects.
 */
public class SourceNodeList extends SortableList {

	/**
	 * Registers, adds, the given SourceNode to the list.
	 *
	 * @param sourceNode the SourceNode to be registered.
	 */
	public void register(SourceNode sourceNode) {
		super.register((Object)sourceNode);
	}

	/**
	 * Unregisters, removes, the given SourceNode from the list.
	 *
	 * @param sourceNode the SourceNode to be unregistered.
	 */
	public void unRegister(SourceNode sourceNode) {
		super.unRegister((Object)sourceNode);
	}

}