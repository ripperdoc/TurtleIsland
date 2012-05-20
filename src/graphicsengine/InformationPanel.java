/* 
 * 2003-05-21 EliasAE
 * 	Added exit button.
 *
 * 2003-05-20 EliasAE
 * 	Added help panel.
 *
 * 2003-05-19 EliasAE
 * 	Formatting.
 *
 * 2003-05-11 EliasAE
 * 	Added the name to the information panel. Added a separate panel for 
 * 	turtles that separate name, age and gender from the characteristics.
 * 
 * 2003-05-11 henko
 *  Updated so that the InformationPanel shows correct resource information for
 *  Turtles and the LightPillarNode.
 *
 * 2003-05-09 pergamon
 *  Long time since last comment, much have happened. But this time I changed
 *  so that updateCharacteristicsPanel and updateResourcePanel uses the new
 *  interfaces for roles, from gameengine, to update themselves.
 *
 * 2003-05-03 pergamon
 *  Totally changed how a Panel is and added Skills.
 *
 * 2003-05-01 pergamon
 *  Added support for Panels.. InformationPanel now show Information about
 *  Individuals and SourceNodes
 *
 * 2003-04-17 pergamon
 *  Testing with bars.
 *
 * 2003-04-14 pergamon
 *  Formatted syntax and started to work on implementation.
 */
package graphicsengine;

import gameengine.Environment;
import gameengine.gameobjects.*;
import graphicsengine.graphics.GraphicsManager;
import graphicsengine.graphics.ButtonGraphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Collection;

/**
 * The control panel that shows information for example about the selected
 * object and accept user input. View and this class together make up the
 * whole screen.
 */
public class InformationPanel extends GraphicalObject {

	/**
	 * Represents the width of the InformationPanel
	 */
	private int width;

	/**
	 * Represents the height of the InformationPanel.
	 */
	private int height;

	/**
	 * The current selected objects, if any.
	 */
	private Collection selectedObjects;

	/**
	 * Object that manages graphics, that one can retrieve graphics from.
	 */
	GraphicsManager graphicsManager;

	private Button exitButton;

	private Panel helpPanel;
	private static String HELP_LABEL = "helpLabel";

	private Panel turtlePanel;
	private static String NAME_LABEL = "nameLabel";
	private static String AGE_LABEL = "ageLabel";
	private static String GENDER_LABEL = "genderLabel";

	private Panel characteristicsPanel;
	private static String SAFETY_BAR = "safetyBar";
	private static String SATURATION_BAR = "saturationBar";
	private static String HAPPINESS_BAR = "happinessBar";
	private static String HEALTH_BAR = "healthBar";
	private static String GATHER_SKILL_BAR = "gatherSkillBar";
	private static String SACRIFICE_SKILL_BAR = "sacrificeSkillBar";
	private static String TRANSPORTATION_SKILL_BAR = "transportationSkillBar";

	private Panel resourcePanel;
	private static String NATURAL_RESOURCE_LABEL = "naturalResourceLabel";
	private static String FOOD_LABEL = "foodLabel";
	private static String HAPPINESS_LABEL = "happinessLabel";
	private static String SACRIFICE_LABEL = "sacrificeLabel";

	/**
	 * Creates an information panel on a position with width and height.
	 *
	 * @param graphicsManager a graphicsmanager to get graphics from.
	 * @param screenX the top left x-coordinate of the informationpanel
	 * @param screenY the top left y-coordinate of the informationpanel
	 * @param width the width of the informationpanel.
	 * @param height the height of the informationpanel.
	 */
	public InformationPanel(
			GraphicsManager graphicsManager,
			int screenX,
			int screenY,
			int width,
			int height
			) {
		super(screenX, screenY);
		this.graphicsManager = graphicsManager;
		this.width = width;
		this.height = height;

		helpPanel = createHelpPanel(15, 5);
		this.addSubObject(helpPanel);

		characteristicsPanel = createCharacteristicsPanel(300, 30);
		this.addSubObject(characteristicsPanel);

		turtlePanel = createTurtlePanel(100, 30);
		this.addSubObject(turtlePanel);

		// Create a Panel to show how much resources the selected object has.
		resourcePanel = createResourcePanel(500, 30);
		this.addSubObject(resourcePanel);

		exitButton = createExitButton();
		this.addSubObject(exitButton);
	}

	/**
	 * Creates the panel on which the help text will be displayed.
	 *
	 * @param relativeX the relative x-position of the Panel to its owner.
	 * @param relativeY The relative y-position of the Panel to its owner.
	 * @return The created Panel.
	 */
	private Panel createHelpPanel(int relativeX, int relativeY) {
		int tmpFontHeight = 10;
		Panel tmpPanel = new Panel("", relativeX, relativeY);
		tmpPanel.setFont(new Font("Arial", Font.PLAIN, tmpFontHeight));
		tmpPanel.setDescriptionColor(new Color(50, 115, 180));
		tmpPanel.setInformationColor(Color.white);

		tmpPanel.addSubObject(
				HELP_LABEL,
				new Label("Selection:", -60, 0),
				new Label("", 66, 0)
				);
		return tmpPanel;
	}

	/**
	 * Creates the panel on which turtles basic information
	 *
	 * @param relativeX The relative x-position of the Panel to its owner.
	 * @param relativeY The relative y-position of the Panel to its owner.
	 * @return The created Panel.
	 */
	private Panel createTurtlePanel(int relativeX, int relativeY) {
		int tmpFontHeight = 10;
		Panel tmpPanel = new Panel("Turtle", relativeX, relativeY);
		tmpPanel.setFont(new Font("Arial", Font.PLAIN, tmpFontHeight));
		tmpPanel.setDescriptionColor(new Color(50, 115, 180));
		tmpPanel.setInformationColor(Color.white);
		int tmpRelativeX = 66;
		int tmpRelativeY = 20;
		int tmpColumnWidth = 54;
		int tmpDistanceBetweenRows = 2;

		tmpPanel.addSubObject(
				NAME_LABEL,
				new Label("Name:", -tmpColumnWidth, 0),
				new Label("", tmpRelativeX, tmpRelativeY)
				);

		tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

		tmpPanel.addSubObject(
				AGE_LABEL,
				new Label("Age:", -tmpColumnWidth, 0),
				new Label("", tmpRelativeX, tmpRelativeY)
				);

		tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

		tmpPanel.addSubObject(
				GENDER_LABEL,
				new Label("Gender:", -tmpColumnWidth, 0),
				new Label("", tmpRelativeX, tmpRelativeY)
				);

		tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows + 5);

		return tmpPanel;
	}

	/**
	 * Creates the Panel on which information about the turtle's bars
	 * are displayed.
	 *
	 * @param relativeX The relative x-position of the Panel to its owner.
	 * @param relativeY The relative y-position of the Panel to its owner.
	 * @return The created Panel.
	 */
	private Panel createCharacteristicsPanel(int relativeX, int relativeY) {
		int tmpFontHeight = 10;
		Panel tmpPanel = new Panel("Characteristics", relativeX, relativeY);
		tmpPanel.setFont(new Font("Arial", Font.PLAIN, tmpFontHeight));
		tmpPanel.setDescriptionColor(new Color(50, 115, 180));
		tmpPanel.setInformationColor(Color.white);
		int tmpRelativeX = 66;
		int tmpRelativeY = 20;
		int tmpColumnWidth = 54;
		int tmpDistanceBetweenRows = 2;


		try {
			int tmpBarHeight = tmpFontHeight - 2;

			tmpPanel.addSubObject(
					SAFETY_BAR,
					new Label("Safety:", -tmpColumnWidth, -2),
					new Bar(
						graphicsManager, tmpRelativeX, tmpRelativeY, 100,
						tmpBarHeight, 0, 1, 0, new Color(200, 0, 0),
						new Color(50, 115, 180)
						)
					);

			tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

			tmpPanel.addSubObject(
					SATURATION_BAR,
					new Label("Saturation:", -tmpColumnWidth, -2),
					new Bar(
						graphicsManager, tmpRelativeX, tmpRelativeY, 100,
						tmpBarHeight, 0, 1, 0, new Color(200, 0, 0),
						new Color(50, 115, 180)
						)
					);

			tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

			tmpPanel.addSubObject(
					HAPPINESS_BAR,
					new Label("Happiness:", -tmpColumnWidth, -2),
					new Bar(
						graphicsManager, tmpRelativeX, tmpRelativeY, 100,
						tmpBarHeight, 0, 1, 0, new Color(200, 0, 0),
						new Color(50, 115, 180)
						)
					);

			tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

			tmpPanel.addSubObject(
					HEALTH_BAR,
					new Label("Health:", -tmpColumnWidth, -2),
					new Bar(
						graphicsManager, tmpRelativeX, tmpRelativeY, 100,
						tmpBarHeight, 0, 1, 0, new Color(200, 0, 0),
						new Color(50, 115, 180)
						)
					);

			tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

			tmpPanel.addSubObject(
					GATHER_SKILL_BAR,
					new Label("Gather:", -tmpColumnWidth, -2),
					new Bar(
						graphicsManager, tmpRelativeX, tmpRelativeY, 100,
						 tmpBarHeight, 0, 1, 0, new Color(200, 0, 0),
						 new Color(50, 115, 180)
						 )
					);

			tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

			tmpPanel.addSubObject(
					SACRIFICE_SKILL_BAR,
					new Label("Sacrifice:", -tmpColumnWidth, -2),
					new Bar(
						graphicsManager, tmpRelativeX, tmpRelativeY, 100,
						tmpBarHeight, 0, 1, 0, new Color(200, 0, 0),
						new Color(50, 115, 180)
						)
					);

			tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

			tmpPanel.addSubObject(
					TRANSPORTATION_SKILL_BAR,
					new Label("Transport:", -tmpColumnWidth, -2),
					new Bar(
						graphicsManager, tmpRelativeX, tmpRelativeY, 100,
						tmpBarHeight, 0, 1, 0, new Color(200, 0, 0),
						new Color(50, 115, 180)
						)
					);

		} catch (WrongValuesInBarException e) {
			e.printStackTrace();
		}

		return tmpPanel;
	}

	/**
	 * Creates the Panel on which information about sourceNodes are displayed.
	 *
	 * @param relativeX The relative x-position of the Panel to its owner.
	 * @param relativeY The relative y-position of the Panel to its owner.
	 * @return The created Panel.
	 */
	private Panel createResourcePanel(int relativeX, int relativeY) {
		int tmpFontHeight = 10;
		Panel tmpPanel = new Panel("ResourceInfo", relativeX, relativeY);
		tmpPanel.setFont(new Font("Arial", Font.PLAIN, tmpFontHeight));
		tmpPanel.setDescriptionColor(new Color(50, 115, 180));
		tmpPanel.setInformationColor(Color.white);
		int tmpRelativeX = 66;
		int tmpRelativeY = 20;
		int tmpColumnWidth = 54;
		int tmpDistanceBetweenRows = 2;

		tmpPanel.addSubObject(
				NATURAL_RESOURCE_LABEL,
				new Label("Natural:", -tmpColumnWidth, 0),
				new Label("", tmpRelativeX, tmpRelativeY)
				);

		tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

		tmpPanel.addSubObject(
				FOOD_LABEL,
				new Label(
					Environment.getResourceString(Environment.FOOD) + ":",
					-tmpColumnWidth, 0
					),
				new Label("", tmpRelativeX, tmpRelativeY)
				);

		tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

		tmpPanel.addSubObject(
				HAPPINESS_LABEL,
				new Label(
					Environment.getResourceString(Environment.HAPPINESS) + ":",
					-tmpColumnWidth, 0
					),
				new Label("", tmpRelativeX, tmpRelativeY)
				);

		tmpRelativeY += (tmpFontHeight + tmpDistanceBetweenRows);

		tmpPanel.addSubObject(
				SACRIFICE_LABEL,
				new Label(
					Environment.getResourceString(Environment.SACRIFICE) + ":",
					-tmpColumnWidth, 0
					),
				new Label("", tmpRelativeX, tmpRelativeY)
				);

		return tmpPanel;
	}

	/**
	 * Creates the exit button.
	 */
	private Button createExitButton() {
		ButtonGraphics buttonGraphics =
				graphicsManager.getExitButtonGraphics();
		int buttonWidth = buttonGraphics.getActive().getWidth();
		int buttonHeight = buttonGraphics.getActive().getHeight();
		return new Button(
				buttonGraphics,
				width - 30 - buttonWidth,
				10
				);
	}

	/**
	 * Retrieves the exit button
	 *
	 * @return the exit button of this information panel.
	 */
	public Button getExitButton() {
		return exitButton;
	}

	/**
	 * Draws the information panel on a graphics object.
	 *
	 * @param graphics the graphics object to draw the information panel on.
	 */
	public void draw(Graphics2D graphics) {
		if (isVisible()) {
			graphics.setColor(new Color(00, 70, 90));
			graphics.fillRect(getScreenX(), getScreenY(), width, height);

			// set all panel's visibility to false;
			turtlePanel.setVisible(false);
			characteristicsPanel.setVisible(false);
			resourcePanel.setVisible(false);
			helpPanel.setVisible(false);

			// if there are one selected object
			if (! (selectedObjects == null)) {
				if (selectedObjects.size() == 1) {
					// get the selected object.
					Selectable tmpSelectedObject =
							(Selectable) selectedObjects.iterator().next();

					// check if the selected object is an GraphicalGameObject
					if (tmpSelectedObject instanceof GraphicalGameObject) {
						// in that case get its gameobject.
						GameObject tmpGameObject =
								(GameObject) ( (GraphicalGameObject) tmpSelectedObject).
								getGameObject();

						if (tmpGameObject.isDeleted()) {
							selectedObjects.clear();
						} else {
							updateHelpPanel(tmpGameObject);

							// check if the GameObject is an Individual.
							if (tmpGameObject instanceof CharacteristicsContainer) {
								updateCharacteristicsPanel((CharacteristicsContainer) tmpGameObject);
								characteristicsPanel.setVisible(true);
								updateTurtlePanel((CharacteristicsContainer) tmpGameObject);
								turtlePanel.setVisible(true);
							}

							if (tmpGameObject instanceof ResourceContainer) {
								updateResourcePanel((ResourceContainer) tmpGameObject);
								resourcePanel.setVisible(true);
							}
						}
					}
				// if there is more than one selected object.
				} else if (selectedObjects.size() > 1) {
					helpPanel.setVisible(true);
					((Label) helpPanel.getSubObject(HELP_LABEL)).setLabel(
							"More than one object selected");
				}
			}
			super.draw(graphics);
		}
	}

	/**
	 * Updates the help panel by displaying information about a game object.
	 *
	 * @param gameObject the game object to display information about.
	 */
	private void updateHelpPanel(GameObject gameObject) {
		if (gameObject.getDescription() != null) {
			helpPanel.setVisible(true);
			((Label) helpPanel.getSubObject(HELP_LABEL)).setLabel(
					gameObject.getDescription());
		}
	}

	/**
	 * To update the resourcePanel with information from a ResourceContainer.
	 *
	 * @param rc the object to get information from.
	 */
	private void updateResourcePanel(ResourceContainer rc) {
		if ((rc instanceof SourceNode) || (rc instanceof LightPillarNode)) {

			// Show natural resource information for those who have it.			
			Label naturalResourceLabel =
					((Label) resourcePanel.getSubObject(NATURAL_RESOURCE_LABEL));

			// SourceNode
			if (rc instanceof SourceNode) {
				SourceNode tmpSourceNode = (SourceNode) rc;
				int tmpResourceType = tmpSourceNode.getNaturalResourceType();
				String tmpNatural = Environment.getResourceString(tmpResourceType);

				naturalResourceLabel.getDescription().setLabel(tmpNatural + "+");
				naturalResourceLabel.setLabel("" + tmpSourceNode.getNaturalResourceAmount());

			// LightPillarNode
			} else if (rc instanceof LightPillarNode) {
				LightPillarNode tmpPillarNode = (LightPillarNode) rc;
				naturalResourceLabel.getDescription().setLabel("Sacrificed:");
				naturalResourceLabel.setLabel("" + tmpPillarNode.getSacrificedAmount());
			}

			resourcePanel.addSubObject(
					NATURAL_RESOURCE_LABEL,
					naturalResourceLabel
					);

			naturalResourceLabel.setVisible(true);

		} else {
			// Hide natural resource for those who don't have it.
			((Label) resourcePanel.getSubObject(NATURAL_RESOURCE_LABEL)).
					setVisible(false);
		}

		((Label) resourcePanel.getSubObject(FOOD_LABEL)).
				setLabel("" + rc.getResourceAmount(Environment.FOOD));
		((Label) resourcePanel.getSubObject(HAPPINESS_LABEL)).
				setLabel("" + rc.getResourceAmount(Environment.HAPPINESS));
		((Label) resourcePanel.getSubObject(SACRIFICE_LABEL)).
				setLabel("" + rc.getResourceAmount(Environment.SACRIFICE));
	}

	/**
	 * Update the turtle panel with informaion from a CharacteristicsContainer.
	 *
	 * @param characteristics the object to get information from.
	 */
	private void updateTurtlePanel(CharacteristicsContainer characteristics) {
		String tmpGender = "";
		if (characteristics.getGender() == Environment.MALE) {
			tmpGender = "Male";
		} else if (characteristics.getGender() == Environment.FEMALE) {
			tmpGender = "Female";
		}

		// Set the values on the different Lables and Bars.
		String name = "";
		if (characteristics instanceof GameObject) {
			name = ((GameObject) characteristics).getName();
		}
		((Label) turtlePanel.getSubObject(NAME_LABEL)).setLabel(name);
		((Label) turtlePanel.getSubObject(AGE_LABEL)).setLabel(
					"" + characteristics.getAge());

		((Label) turtlePanel.getSubObject(GENDER_LABEL)).setLabel(tmpGender);
	}

	/**
	 * Updates the Characteristics information from a CharacteristicsContainer.
	 *
	 * @param characteristics the object to get information from.
	 */
	private void updateCharacteristicsPanel(CharacteristicsContainer characteristics) {

		((Bar) characteristicsPanel.getSubObject(SAFETY_BAR)).setValue(
					characteristics.getSafety());

		((Bar) characteristicsPanel.getSubObject(SATURATION_BAR)).setValue(
					characteristics.getSaturation());

		((Bar) characteristicsPanel.getSubObject(HAPPINESS_BAR)).setValue(
					characteristics.getHappiness());

		((Bar) characteristicsPanel.getSubObject(HEALTH_BAR)).setValue(
					characteristics.getHealth());

		((Bar) characteristicsPanel.getSubObject(GATHER_SKILL_BAR)).setValue(
					characteristics.getGatherSkill());

		((Bar) characteristicsPanel.getSubObject(SACRIFICE_SKILL_BAR)).setValue(
					characteristics.getSacrificeSkill());

		((Bar) characteristicsPanel.getSubObject(TRANSPORTATION_SKILL_BAR)).setValue(
					characteristics.getTransportationSkill());
	}

	/**
	 * Shows an information string
	 *
	 * @param text the string that should be displayed in the information panel.
	 */
	public void showInformation(String text) {
		// your code here
	}

	/**
	 * Shows information about a game object.
	 *
	 * @param selectedObject the game object to show information about in the
	 * information panel.
	 */
	public void showInformation(Selectable selectedObject) {
		// your code here
	}

	/**
	 * Show information about the selected objects.
	 *
	 * @param selectedObjects a Collection of selected objects to show
	 * information about.
	 */
	public void showInformation(Collection selectedObjects) {
		this.selectedObjects = selectedObjects;
	}

	/**
	 * Retrieves the width of the information panel.
	 *
	 * @return the width of the information panel in pixels.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width of the information panel.
	 *
	 * @param width the width of the information panel in pixels. Cannot be
	 * less than zero.
	 */
	void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Retrieves the height of the information panel.
	 *
	 * @return the height of the information panel in pixels.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height of the information panel.
	 *
	 * @param height the height of the information panel in pixels. Cannot be
	 * less than zero.
	 */
	void setHeight(int height) {
		this.height = height;
	}

}
