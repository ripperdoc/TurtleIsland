/*
 * 2003-05-21 EliasAE
 * 	Added exit button.
 *
 * 2003-05-18 EliasAE
 * 	Formatting.
 *
 * 2003-05-18 EliasAE
 * 	Added priority when selecting.
 *
 * 2003-05-17 pergamon
 *  fixed the scroll to work with a view that isn't at 0,0
 *
 * 2003-05-17 EliasAE
 * 	Buttons upon resource nodes is now not displayed when a user places a
 * 	new resource node without haveing a individual selected.
 *
 * 2003-05-13 henko
 * 	Changed MoveToTask to CommandedMoveToTask which includes a pause after
 *  reaching the destination.
 *
 * 2003-05-12 henko
 * 	Only creates a task for a turtle if the user clicks within the maps
 *  (ie not out of bounds).
 *
 * 2003-05-11 EliasAE
 * 	Changed transportation a little. Icons are now hidden correctly.
 *
 * 2003-05-02 Martin
 *	Changed commit to setCommited. Maybe the possibility of uncommiting a
 *	TransportTask should be implemented (with the call setCommited(false)).
 *
 * 2003-05-02 EliasAE
 * 	Create new nodes when the user ctrl-right-clicks on a empty space.
 *
 * 2003-05-01 pergamon
 *  Changed so that the object on top is selected when they stand on the same
 *  place.
 *
 * 2003-04-29 EliasAE
 * 	Cleaned the code. Removed a function that I had added previously.
 * 	Cleaned the code. Removed a function that I had added previously.
 *
 * 2003-04-29 EliasAE
 * 	The selectionbox can now really be dragged in both directions.
 *
 * 2003-04-23 EliasAE
 * 	Added support for CommandedSacrificeTask.
 *
 * 2003-04-19 EliasAE
 * 	Added task to gather from source nodes. Selection box can now be dragged
 * 	in both directions.
 *
 * 2003-04-15 pergamon
 *  fixed the scrolling so that it moves faster the closer to the edge you come
 *  with the mouse.
 *
 * 2003-04-15 pergamon
 *  Chagned to use GraphicsEngines getView and getInformationPanel functions.
 *  Added mouseMoved(int,int) and the functionality to scroll the view.
 *
 * 2003-04-14 EliasAE
 * 	Changes to use the new interface for CoordinateConverter.
 *
 * 2003-04-13 pergamon
 *  Wrote mouseDragged and added functionality to select all Objects inside
 *  an area. Removed quit on middle mouse button click.
 *
 * 2003-04-13 pergamon
 *  Added alot of the functionality in left- and rightMouseButtonUp
 *
 * 2003-04-13 EliasAE
 * 	Formatting.
 *
 * 2003-04-13 EliasAE
 * 	Added code in the constructor to create a keyboard and i keyUp
 * 	to exit the application when escape is pressed.
 *
 * 2003-04-13 EliasAE
 * 	Changed unSelectAll to remove all elements in the collection over
 * 	selected objects.
 *
 * 2003-04-13 pergamon
 *  Changed to map-coordinates for the startingpositions of the Individuals.
 *
 * 2003-04-12 pergamon
 * 	Added a private GraphicsEngine. How should it else be accessed?
 * 	Functionality for leftMouseUp added; click on GraphicalObject.
 *  Added a List of currently selected objects. Also a private function
 *  to unselect them all.
 *
 * 2003-04-10 pergamon
 *	Some code in the constructor to add a mouse. Also print out the
 *	mouse-coordinates when left or right button is clicked.
 *	Sets quit to true when the middle mouse button is clicked.
 */

package input;

import gameengine.*;
import gameengine.gameobjects.*;
import gameengine.tasksystem.*;
import graphicsengine.*;

import java.awt.event.*;
import java.util.*;

/**
 * This class communicates with the graphics engine and game engine after
 * input from the user.
 */
public class InputEventTranslator {

	/**
	 * true if the application should quit, otherwise false.
	 */
	private boolean quit;

	/**
	 * The graphical game objects that are currently selected.
	 */
	private Collection currentlySelected;

	/**
	 * A reference to the graphicsEngine.
	 */
	private GraphicsEngine graphicsEngine;

	/**
	 * The keyboard listener.
	 */
	private Keyboard keyboard;

	/**
	 * The constant for the key used to exit the application
	 */
	private static final int QUIT_KEY = KeyEvent.VK_ESCAPE;

	/**
	 * The constant for the key that will be used to indicate that a
	 * transportation command is given.
	 */
	private static final int TRANSPORTATION_KEY = KeyEvent.VK_CONTROL;

	/**
	 * The constant for the key that will be used to center the screen
	 * at the light pillar.
	 */
	private static final int CENTER_VIEW_KEY = KeyEvent.VK_SPACE;

	/**
	 * The constant for the key that will be used to delete a node.
	 */
	private static final int DELETE_NODE_KEY = KeyEvent.VK_DELETE;

	/**
	 * The transportation task that currently is under build up, otherwise
	 * null.
	 */
	private CommandedTransportTask currentTransportationTask = null;

	/**
	 * The executer of the currentTransportationTask, or null if no
	 * transportation task is under build up.
	 */
	private GraphicalGameObject currentTransportationTaskExecuter = null;

	/**
	 * The graphical object for the next node that the turtle will
	 * pick up a resource from. This valid when the resource icons
	 * are shown at the current node of transportation. Otherwise
	 * null.
	 */
	private GraphicalGameObject currentTransportationTaskDestination = null;

	/**
	 * The position of the mouse when the button was first pressed.
	 */
	private int mousePositionDownX;

	/**
	 * The position of the mouse when the button was first pressed.
	 */
	private int mousePositionDownY;

	/**
	 * true if the left mouse button is pressed, otherwise false.
	 * Used to draw the selection box.
	 */
	private boolean leftMouseButtonPressed = false;

	/**
	 * Creates a input event tanslator attached to a graphics engine. Add a
	 * mouse and a keyboard to the grahics engine.
	 *
	 * @param graphicsEngine the graphics engine that this input event
	 * tanslator will attach to and give call when the a relevant input is
	 * detected.
	 */
	public InputEventTranslator(GraphicsEngine graphicsEngine) {
		this.graphicsEngine = graphicsEngine;
		currentlySelected = new ArrayList();
		Mouse mouse = new Mouse(this);
		graphicsEngine.addMouse(mouse);
		keyboard = new Keyboard(this);
		graphicsEngine.addKeyboard(keyboard);
	}

	/**
	 * Called when a mouse button is pressed and dragged. Its reaction depends
	 * on which mousebutton that is pressed. Left mouse button makes a select-
	 * box to select several turtles.
	 *
	 * @param positionX the current position of the mouse.
	 * @param positionY the current position of the mouse.
	 */
	public void mouseDragged(int positionX, int positionY) {
		if (leftMouseButtonPressed) {
			int topLeftX = Math.min(mousePositionDownX, positionX);
			int topLeftY = Math.min(mousePositionDownY, positionY);
			int bottomRightX = Math.max(mousePositionDownX, positionX);
			int bottomRightY = Math.max(mousePositionDownY, positionY);
			graphicsEngine.getView().drawMouseSelectBox(
					topLeftX, topLeftY, bottomRightX, bottomRightY);
		}
	}

	/**
	 * Called whenever the mouse moves. If the mouse position, relative the screen
	 * edges, is less or equal to the getViewOffset from the View the view
	 * will scroll.
	 *
	 * @param positionX the current position of the mouse.
	 * @param positionY the current position of the mouse.
	 */
	public void mouseMoved(int positionX, int positionY) {
		// tmpMoveOffset = how far from the edge of the frame the mouse
		// can be before the View scrolls.
		int tmpMoveOffset = graphicsEngine.getView().getViewMoveOffset();

		int boundLeft = tmpMoveOffset;
		int boundTop = tmpMoveOffset;
		int boundRight = graphicsEngine.getWidth() - tmpMoveOffset;
		int boundBottom = graphicsEngine.getHeight() - tmpMoveOffset;

		graphicsEngine.getView().setMoveX(0);
		graphicsEngine.getView().setMoveY(0);

		if (positionX <= boundLeft) {
			graphicsEngine.getView().setMoveX(positionX - boundLeft);
		}

		if (positionX >= boundRight) {
			graphicsEngine.getView().setMoveX(positionX - boundRight);
		}

		if (positionY <= boundTop) {
			graphicsEngine.getView().setMoveY(positionY - boundTop);
		}

		if (positionY >= boundBottom) {
			graphicsEngine.getView().setMoveY(positionY - boundBottom);
		}
	}

	/**
	 * Called when the left mouse button is pressed. Reacts in the
	 * right way depending on where the mouse cursor is placed.
	 *
	 * @param positionX the x-position of the mouse cursor in screen
	 * coordinates.
	 * @param positionY the y-position of the mouse cursor in screen
	 * coordiantes.
	 */

	public void leftMouseButtonDown(int positionX, int positionY) {
		leftMouseButtonPressed = true;
		mousePositionDownX = positionX;
		mousePositionDownY = positionY;
	}

	/**
	 * Should be called when the left mouse button is released. Reacts in the
	 * right way depending on where the mouse cursor is placed.
	 *
	 * @param positionX the x-position of the mouse cursor in screen
	 * coordinates.
	 * @param positionY the y-position of the mouse cursor in screen
	 * coordiantes.
	 */
	public void leftMouseButtonUp(int positionX, int positionY) {
		// End any transportationstasks
		if (currentTransportationTask != null) {
			commitTransportationTask();
		}

		// Check for if any button in the information panel was pressed
		if (
				graphicsEngine.getInformationPanel().getExitButton().hitTest(
					positionX, positionY)
				) {
			quit();
		}

		leftMouseButtonPressed = false;
		List destinationsCollection;
		Iterator grahicalObjectsIterator;

		// If there is an area that has been selected.
		if ( (Math.abs(mousePositionDownX - positionX) > 1) ||
				(Math.abs(mousePositionDownY - positionY) > 1)) {
			// get all objects in the area.
			int topLeftX = Math.min(mousePositionDownX, positionX);
			int topLeftY = Math.min(mousePositionDownY, positionY);
			int bottomRightX = Math.max(mousePositionDownX, positionX);
			int bottomRightY = Math.max(mousePositionDownY, positionY);
			destinationsCollection =
					graphicsEngine.getView().getObjectFromPosition(
					topLeftX, topLeftY, bottomRightX, bottomRightY);

			// If there is more than one object selected, remove
			// all none-GameObject:s and Node:s from the selected objects.
			if (destinationsCollection.size() > 1) {
				Iterator graphicalObjectsIterator =
						destinationsCollection.iterator();
				while (graphicalObjectsIterator.hasNext()) {
					GraphicalObject tmpGraphicalObject =
							(GraphicalObject) graphicalObjectsIterator.next();
					if (tmpGraphicalObject instanceof GraphicalGameObject) {
						if (
								((GraphicalGameObject) tmpGraphicalObject).getGameObject()
								instanceof Node
								) {
							graphicalObjectsIterator.remove();
						}
					} else {
						graphicalObjectsIterator.remove();
					}
				}
			}
		// If there isn't an area that have been selected.
		} else {
			// Get the selected objects at the point.
			Object clickedGraphicalObject = null;
			GameObject clickedGameObject = null;
			destinationsCollection = graphicsEngine.getView().
					getObjectFromPosition(positionX, positionY);
			Iterator destinationsIterator = destinationsCollection.iterator();

			// Get the best objects to selected.
			while (destinationsIterator.hasNext()) {
				GraphicalObject graphicalObjectCandidate =
						(GraphicalObject) destinationsIterator.next();
				GameObject gameObjectCandidate = null;
				if (graphicalObjectCandidate instanceof GraphicalGameObject) {
						gameObjectCandidate =
								((GraphicalGameObject) graphicalObjectCandidate).
									getGameObject();
				}
				// Enforce the priority that Buttons is always selected,
				// and if no button was clicked the node will be selected.
				// If there is not any node nor any buttons, use the last
				// in the collection.
				if (
						(graphicalObjectCandidate instanceof Button)
						||
						(gameObjectCandidate instanceof Node &&
							! (clickedGraphicalObject instanceof Button))
						||
							(!(clickedGameObject instanceof Node) &&
							!(clickedGraphicalObject instanceof Button))
						) {
					clickedGraphicalObject = graphicalObjectCandidate;
					clickedGameObject = gameObjectCandidate;
				}
			}

			// If there where an active button on the clicked position.
			if (clickedGraphicalObject instanceof Button) {
				Button clickedButton = (Button) clickedGraphicalObject;
				if (clickedButton.isActive()) {
					if (clickedButton.isSelected()) {
						unSelect(clickedButton);
					} else {
						select(clickedButton);
					}
					return;
				}
			}

			// Use only one object
			destinationsCollection.clear();
			if (clickedGraphicalObject != null) {
				destinationsCollection.add(clickedGraphicalObject);
			}
		}

		// If there are objects selected; unselect them
		if (!currentlySelected.isEmpty()) {
			unSelectAll();
			graphicsEngine.getInformationPanel().showInformation(
					destinationsCollection);
		}
		// Select all objects at this position and add them to the list of current
		// selected objects.
		grahicalObjectsIterator = destinationsCollection.iterator();
		graphicsEngine.getInformationPanel().showInformation(destinationsCollection);
		while (grahicalObjectsIterator.hasNext()) {
			Selectable tmpSelected = (Selectable) grahicalObjectsIterator.next();
			select(tmpSelected);
		}
	}

	/**
	 * A little helpfunction to unselect all currently selected objects.
	 */
	private void unSelectAll() {
		Iterator selectedObjects = currentlySelected.iterator();
		while (selectedObjects.hasNext()) {
			Selectable tmpSelected = (Selectable) selectedObjects.next();
			tmpSelected.setSelected(false);
			if (tmpSelected instanceof GraphicalObject) {
				((GraphicalObject) tmpSelected).setShowSubObjects(false);
			}
			selectedObjects.remove();
		}
	}

	/**
	 * Selected a selectable object and adds it to the list
	 * of selected objects.
	 *
	 * @param selected the object to select.
	 */
	public void select(Selectable selected) {
		selected.setSelected(true);
		currentlySelected.add(selected);
	}

	/**
	 * Unselects a selectable object and removes it from the list
	 * of selected objects.
	 *
	 * @param selected the object to deselect.
	 */
	public void unSelect(Selectable selected) {
		selected.setSelected(false);
		currentlySelected.remove(selected);
	}

	/**
	 * Called when the right mouse button is pressed. Reacts in the
	 * right way depending on where the mouse cursor is placed.
	 *
	 * @param positionX the x-position of the mouse cursor in screen
	 * coordinates.
	 * @param positionY the y-position of the mouse cursofr in screen
	 * coordiantes.
	 */

	public void rightMouseButtonDown(int positionX, int positionY) {

	}

	/**
	 * Should be called when the right mouse button is released. Reacts in the
	 * right way depending on where the mouse cursor is placed.
	 *
	 * @param positionX the x-position of the mouse cursor in screen
	 * coordinates.
	 * @param positionY the y-position of the mouse cursofr in screen
	 * coordiantes.
	 */
	public void rightMouseButtonUp(int positionX, int positionY) {
		// Get all the objects at the clicked position.
		Collection destinationsCollection =
				graphicsEngine.getView().getObjectFromPosition(positionX, positionY);

		Iterator destinations = destinationsCollection.iterator();
		GameObject destination = null;
		CoordinateConverter cc = CoordinateConverter.getCoordinateConverter();
		GraphicalGameObject destinationGraphicalGameObject = null;

		// Check if there is any objectes at the specific position.
		while (destinations.hasNext()) {
			Object destinationCandidate = destinations.next();
			// Check if there is a resource button, in that case, do pick up.
			if (destinationCandidate instanceof Button) {
				Button clickedButton = (Button) destinationCandidate;
				if (clickedButton.isActive()) {
					// If a transportation is under way, this can be a press on
					// a resource button to perform a pick up of a given type
					// of resource.
					if (isTransportationTaskPickUpPermitted()) {
						if (clickedButton.getRepresentedObject() instanceof Integer) {
							unSelect(clickedButton);
							Integer representedInteger =
								(Integer) clickedButton.getRepresentedObject();
							// If the resource type is valid, add the pickup task
							if (representedInteger.intValue() >= 0 &&
									representedInteger.intValue() <
									World.getEnvironment().getNumberOfResourceTypes()
									) {
								addTransportationTaskPickUp(representedInteger.intValue());
								return;
							}
						}
					}
				}
			} else if (destinationCandidate instanceof GraphicalGameObject) {
				// Nodes have higher priority as destinations than other
				// game objects.
				if (!(destination instanceof Node)) {
					destinationGraphicalGameObject =
							(GraphicalGameObject) destinationCandidate;
					destination = destinationGraphicalGameObject.getGameObject();
				}
			}
		}
		// If the task that will be given should be a
		// transporatation task.
		boolean isTransportationTask = keyboard.isKeyPressed(TRANSPORTATION_KEY);

		// If it wasn't any object at the position then create
		// a SpecialPoint as destination. But only if it is not a
		// transportation task. In that case we create a node
		// instead
		if (!isTransportationTask && destination == null) {
			try {
				destination = new SpecialPoint(cc.screenToMap(positionX, positionY));
			} catch (IllegalArgumentException iae) {
				// Don't create the destination, it was out of bounds.
			}

		}

		// This was a transportation task command but with no destination.
		// Create a new node at the clicked position
		if (isTransportationTask && destination == null) {
			try {
				Point nodePoint = cc.screenToMap(positionX, positionY);
				destination = new Node(nodePoint);
				destinationGraphicalGameObject =
						(GraphicalGameObject) graphicsEngine.getView().
						createGraphicalGameObject(destination);
				select(destinationGraphicalGameObject);
			} catch (GraphicalGameObjectExistsException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException iae) {
				// Don't create the destination, it was out of bounds.
			}
		}

		// Only proceed if a destination was created.
		// In other words, if user clicked inside the map.
		if (destination != null) {

			// Give tasks to different gameobjects.
			Iterator executers = currentlySelected.iterator();
			while (executers.hasNext()) {
				Object executerGraphicalObject = executers.next();
				if (executerGraphicalObject instanceof GraphicalGameObject) {
					GraphicalGameObject executerGraphicalGameObject =
							(GraphicalGameObject) executerGraphicalObject;
					GameObject executer = executerGraphicalGameObject.getGameObject();

					if (isTransportationTask &&
							executer instanceof Individual &&
							destination instanceof Node
							) {
						// If it is a transportation task that should be
						// manipulated, start a new one if no earlier
						// is under build up. If there is a unfinished
						// transportation task, continue it, but only
						// if this is the right individual. If this
						// is another Individual than the task was
						// originaly created for, do nothing. The
						// current transportation task will be cleared
						// when the user releases control (in keyUp).
						if (currentTransportationTask == null) {
							startTransportationTask(executerGraphicalGameObject,
									destinationGraphicalGameObject);
						} else if (executerGraphicalGameObject ==
											 currentTransportationTaskExecuter) {
							continueTransportationTask(destinationGraphicalGameObject);
						}
					} else if (!isTransportationTask) {
						giveDefaultActionTask(executer, destination);
					}
				}
			}
		}
	}

	/**
	 * Checks if a transportation task pick up can be done with the
	 * current selected object.
	 *
	 * @return true if there is a current transportation task and
	 * the transportation task key is down and the current executer is
	 * selected, otherwise false.
	 */
	private boolean isTransportationTaskPickUpPermitted() {
		return
				currentlySelected.contains(currentTransportationTaskExecuter) &&
				keyboard.isKeyPressed(TRANSPORTATION_KEY) &&
				currentTransportationTask != null &&
				currentTransportationTaskDestination != null
				;
	}

	/**
	 * Called when a key on the keyboard is pressed.
	 *
	 * @param key the identifier for the key as recieved in the event from Java.
	 */
	public void keyDown(int key) {

	}

	/**
	 * Should be called when a key on the keyboard is released.
	 *
	 * @param key the identifier for the key as recieved in the event from Java.
	 */
	public void keyUp(int key) {
		switch (key) {
			case QUIT_KEY:
				quit();
				break;
			case TRANSPORTATION_KEY:
				if (currentTransportationTask != null) {
					commitTransportationTask();
				}
				break;
			case CENTER_VIEW_KEY:
				centerView();
				break;
			case DELETE_NODE_KEY:
				deleteNode();
				break;
			default:
				break;
		}
	}

	/**
	 * Centers the view at the light pillar.
	 */
	private void centerView() {
		CoordinateConverter cc = CoordinateConverter.getCoordinateConverter();
		Point lightPillarPoint = World.getWorld().getLightPillar().getPoint();
		float isoX = cc.mapToIsoX(lightPillarPoint);
		float isoY = cc.mapToIsoY(lightPillarPoint);
		isoX -= graphicsEngine.getView().getWidth() / 2;
		isoY -= graphicsEngine.getView().getHeight() / 2;
		graphicsEngine.getView().setIsoX(isoX);
		graphicsEngine.getView().setIsoY(isoY);
	}

	/**
	 * Deletes all selected nodes.
	 */
	private void deleteNode() {
		Iterator iterator = currentlySelected.iterator();
		while (iterator.hasNext()) {
			Object selected = iterator.next();
			if (selected instanceof GraphicalGameObject) {
				GameObject gameObject =
					((GraphicalGameObject) selected).getGameObject();
				if (Node.class.equals(gameObject.getClass())) {
					gameObject.delete();
					iterator.remove();
				}
			}
		}
	}

	/**
	 * Gives a game object the task that is specified by that objects default
	 * action. Is a turtle is selected and this method is called with a special
	 * point the turtle will recieve a task that orders it to walk to the
	 * special point.
	 *
	 * @param gameObject the goal for the default action.
	 */
	private void giveDefaultActionTask(
			GameObject executer, GameObject destination) {
		// If the selected object is an Individual
		if (executer instanceof Individual) {
			Individual tmpIndividual = (Individual) executer;
			// Give different tasks depending on what object the
			// destination is
			if (destination instanceof LightPillarNode) {
				tmpIndividual.setCommandedTask(new CommandedSacrificeTask());
			} else if (destination instanceof SourceNode) {
				tmpIndividual.setCommandedTask(
						new CommandedGatherTask( (SourceNode) destination));
			} else {
				tmpIndividual.setCommandedTask(new CommandedMoveToTask(destination));
			}
		}
	}

	/**
	 * Initiates a transportation task. The task will be filled with more and
	 * more nodes for tansportations via continueTransportationTask and when
	 * the user indicates that he or she is finished with the transportation
	 * task, the task is closed by a call to commitTransportationTask. The task
	 * is sent to the turtle by this call, but not finished until
	 * commitTransportationTask
	 *
	 * @param executerGraphicalGameObject the graphical game object for the
	 * individual that should perform the transportation.
	 * @param destinationGraphicalGameObject the graphical game object for the
	 * node that the tansportation task should start at.
	 */
	private void startTransportationTask(
			GraphicalGameObject executerGraphicalGameObject,
			GraphicalGameObject destinationGraphicalGameObject
			) {
		if (currentTransportationTask != null ||
				currentTransportationTaskExecuter != null ||
				currentTransportationTaskDestination != null
				) {
			throw new IllegalStateException(
					"There is already a transportation task under build up.");
		}
		if (!(executerGraphicalGameObject.getGameObject() instanceof Individual)) {
			throw new IllegalArgumentException("The executer graphical game object" +
					"must represent a indivudal.");
		}
		if (!(destinationGraphicalGameObject.getGameObject() instanceof Node)) {
			throw new IllegalArgumentException("The destination graphical game object" +
					"must represent a node.");
		}

		Node destination = (Node) destinationGraphicalGameObject.getGameObject();
		GameObject executer = executerGraphicalGameObject.getGameObject();
		destinationGraphicalGameObject.setShowSubObjects(true);
		currentTransportationTask = new CommandedTransportTask(destination);
		((Individual) executer).setCommandedTask(currentTransportationTask);
		currentTransportationTaskExecuter = executerGraphicalGameObject;
		currentTransportationTaskDestination = destinationGraphicalGameObject;
	}

	/**
	 * Adds another node in the chain of transportations. A transportation must
	 * have been opened by startTransportationTask and not ended by
	 * commitTransportationTask when this method is called.
	 *
	 * @param gameObject the game object that the transportation task will have
	 * as its next destination. It must represent a node.
	 */
	private void continueTransportationTask(GraphicalGameObject
			destinationGraphicalGameObject) {
		if (currentTransportationTask == null ||
				currentTransportationTaskExecuter == null
				) {
			throw new IllegalStateException(
					"There is no transportation task under build up.");
		}
		if (!(destinationGraphicalGameObject.getGameObject() instanceof Node)) {
			throw new IllegalArgumentException("The destination graphical game object" +
					"must represent a node.");
		}
		Node destination = (Node) destinationGraphicalGameObject.getGameObject();
		currentTransportationTask.addNode(destination);
		if (currentTransportationTaskDestination != null) {
			currentTransportationTaskDestination.setShowSubObjects(false);
		}
		currentTransportationTaskDestination = destinationGraphicalGameObject;
		currentTransportationTaskDestination.setShowSubObjects(true);
	}

	/**
	 * Adds a pickup of a resource to the queue of inner tasks in the
	 * transportation task.
	 *
	 * @param resourceType the type of resoruce that the turtle
	 * should pick up.
	 */
	private void addTransportationTaskPickUp(int resourceType) {
		if (currentTransportationTask == null ||
				currentTransportationTaskExecuter == null ||
				currentTransportationTaskDestination == null
				) {
			throw new IllegalStateException(
					"There is no transportation task under build up.");
		}
		if (
				resourceType < 0 ||
				resourceType >= World.getEnvironment().getNumberOfResourceTypes()
				) {
			throw new IllegalArgumentException("Invalid resource type.");
		}
		currentTransportationTask.addPickUp(resourceType);
		currentTransportationTaskDestination.setShowSubObjects(false);
		currentTransportationTaskDestination = null;
	}

	/**
	 * Checks to se if a collection contains a resource button.
	 *
	 * @param searchIn the objects that will be searched for a button
	 * that represents a resource type. If a button is found, the resource
	 * type will be returned as an Integer, or null if no button is found.
	 */
	private Button getClickedButton(Collection searchIn) {
		Iterator searchIterator = searchIn.iterator();
		while (searchIterator.hasNext()) {
			Object current = searchIterator.next();
			if (current instanceof Button) {
				return (Button) current;
			}
		}
		return null;
	}

	/**
	 * Finishes a transportation task and sends it to the selected individual.
	 */
	private void commitTransportationTask() {
		if (currentTransportationTask == null ||
				currentTransportationTaskExecuter == null
				) {
			throw new IllegalStateException(
						 "There is no transportation task under build up.");
		}
		if (currentTransportationTaskDestination != null) {
			currentTransportationTaskDestination.setShowSubObjects(false);
		}
		if (currentTransportationTask != null) {
			currentTransportationTask.setCommited(true);
		}
		currentTransportationTask = null;
		currentTransportationTaskExecuter = null;
		currentTransportationTaskDestination = null;
	}

	/**
	 * Sets a flag indicating that the application should quit.
	 */
	public void quit() {
		quit = true;
	}

	/**
	 * Retrieves if the application should quit or not.
	 *
	 * @return true if the application should quit, otherwise false.
	 */
	public boolean isQuiting() {
		return quit;
	}
}
