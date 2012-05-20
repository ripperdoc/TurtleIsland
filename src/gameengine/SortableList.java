/* 2003-05-10 Martin
 *	Made several corrections, and added the method isEmpty().
 *
 * 2003-05-09 Martin
 *	Implemented the sort() and added the getBestItem() that returns the first
 *	element after a sorting.
 *
 * 2003-05-08 Martin
 *	Put all the fixes for the Concurrent Modification Error in SortableList and
 *	cleaned the code a bit.
 *
 * 2003-05-04 EliasAE
 * 	Added iterator()
 *
 * 2003-04-15 Martin
 *	Implemented Serializable.
 *
 * 2003-04-11 pergamon
 *  Formatted syntax.
 *
 */
package gameengine;

import java.util.*;
import java.util.Comparator;
import java.util.List;
import java.io.*;

/**
 * An abstract class containing a list that can be sorted by sending in a
 * specific Comparator.
 */
public abstract class SortableList implements Serializable{

	/**
	 * The list of objects to be added to the GameObjectList.
	 */
	protected List addList;

	/**
	 * The list of objects to be removed from the GameObjectList.
	 */
	protected List removeList;

	/**
	 * The list of the objects.
	 */
	protected List list;

	/**
	 * Constructs a new SortableList, but because SortableList is abstract
	 * the constructor must be callen from a subclass.
	 */
	public SortableList() {
		addList = new ArrayList();
		removeList = new ArrayList();
		list = new ArrayList();
		}

	/**
	 * Sorts the list according to the specified Comparator.
	 *
	 * @param comparator the specified Comparator.
	 */
	public void sort(Comparator comparator) {
		Collections.sort(list, comparator);
	}

	/**
	 * Retrieves a new iterator for this list, but first it updates the list
	 * by adding and removing those specified in the add- and remove list.
	 *
	 * @return the iterator to be used.
	 */
	public Iterator iterator() {
			update();
			return new StaticIterator(list.iterator());
	}

	/**
	 * Registers, adds, the given Object to the list.
	 *
	 * @param object the object to be registered.
	 */
	public void register(Object object) {
		addList.add(object);
	}

	/**
	 * Unregisters, removes, the given Object from the list.
	 *
	 * @param object the object to be unregistered.
	 */
	public void unRegister(Object object) {
		removeList.add(object);
	}

	/**
	 * Updates the list by removing and adding the contents of
	 * the removeList and addList.
	 */
	public void update() {
		// Adds all new objects to the list
		list.addAll(addList);
		addList.clear();
		// Removes all new objects from the list
		list.removeAll(removeList);
		removeList.clear();
	}

	/**
	 * Returns the first object from the list after sorting
	 * the list with the given comparator..
	 *
	 * @param comparator the specified Comparator.
	 * @return the object that is first in the list, i.e. "best".
	 * Returns null if the list is empty.
	 */
	public Object getBestItem(Comparator comparator) {
		sort(comparator);
		// Returns the first item (index 0). The List interface
		// does not have any getFirst() method. Returns null if the
		// list is empty.
		if(!isEmpty()) {
			return list.get(0);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns a boolean stating if the list is empty.
	 *
	 * @return a boolean stating if the list is empty.
	 */
	public boolean isEmpty() {
		if(list.size() > 0) {
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * An iterator that wraps another iterator and
	 * makes it impossible to remvove elements, for
	 * encapsulation and security. This ensures that
	 * the iterator is static during its lifetime.
	 */
	private class StaticIterator implements Iterator {

		/**
		 * The wrapped, ordinary iterator.
		 */
		private Iterator innerIterator;

		/**
		 * Constructs an iterator that does not support removal of items.
		 */
		public StaticIterator(Iterator innerIterator) {
			this.innerIterator = innerIterator;
		}

		/**
		 * Calls the inner iterator's hasNext().
		 */
		public boolean hasNext() {
			return innerIterator.hasNext();
		}

		/**
		 * Calls the inner iterator's next().
		 */
		public Object next() {
			return innerIterator.next();
		}

		/**
		 * The function of removing items from a list via the iterator
		 * is not supported in StaticIterator..
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

}
