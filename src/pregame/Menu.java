/* 2003-05-17 henko
 *  Added support for disabling the "Resume game" button and added a credits
 *  screen.
 *
 * 2003-05-12 henko
 *  Implemented.
 */
package pregame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Menu class is a menu with four buttons. They are for creating
 * a new game, resuming an old, show credits and exit the game respectively.
 * It also extends Observable, wich means it can be observed, allowing others
 * to see what button the user presses.
 */
public class Menu implements ActionListener {

	/**
	 * If userReponse() is equal to NO_RESPONSE it means that the user has
	 * not made any choice.
	 */
	public static final int NO_RESPONSE = 0;

	/**
	 * If userReponse() is equal to NEW_GAME it means that the user has
	 * chosen to start a new game.
	 */
	public static final int NEW_GAME = 1;

	/**
	 * If userReponse() is equal to RESUME_GAME it means that the user has
	 * chosen to resume the last game.
	 */
	public static final int RESUME_GAME = 2;

	/**
	 * If userReponse() is equal to EXIT_GAME it means that the user has
	 * chosen to exit the game.
	 */
	public static final int EXIT_GAME = 4;

	/**
	 * If userReponse() is equal to SHOW_CREDITS it means that the user has
	 * chosen to see the credits screen.
	 */
	public static final int SHOW_CREDITS = 3;

	/**
	 * If userReponse() is equal to HIDE_CREDITS it means that the user has
	 * chosen to return to the menu.
	 */
	public static final int HIDE_CREDITS = 5;

	/**
	 * A variable that saves the user's choice.
	 */
	private int userResponse;

	/**
	 * The JFrame to show the buttons in.
	 */
	private JFrame menuFrame;

	/**
	 * The panel that holds the buttons.
	 */
	private JPanel buttonPanel;

	/**
	 * The panel that holds the credits screen.
	 */
	private JPanel creditsPanel;

	/*
	 * Titles for the buttons.
	 */
	private static final String MENU_WINDOW_TITLE = "TurtleIsland";
	private static final String BUTTON_NEW_GAME_TITLE = "New game";
	private static final String BUTTON_RESUME_GAME_TITLE = "Resume game";
	private static final String BUTTON_SHOW_CREDITS_TITLE = "Show credits";
	private static final String BUTTON_EXIT_GAME_TITLE = "Exit";
	private static final String ICON_NEW_GAME_FILENAME
			= "/images/menu/NewGame.png";
	private static final String ICON_RESUME_GAME_FILENAME
			= "/images/menu/ResumeGame.png";
	private static final String ICON_RESUME_GAME_DISABLED_FILENAME
			= "/images/menu/ResumeGameDisabled.png";
	private static final String ICON_SHOW_CREDITS_FILENAME
			= "/images/menu/ShowCredits.png";
	private static final String ICON_EXIT_GAME_FILENAME
			= "/images/menu/Exit.png";
	private static final String ICON_CREDITS_SCREEN_FILENAME
			= "/images/menu/CreditsScreen.png";

	/**
	 * Creates a new menu with four buttons. The new JFrame will be placed
	 * in the middle of the screen.
	 *
	 * @param whether a savegame is available or not (the resume button should
	 * 		  be enabled or not).
	 */
	public Menu(boolean enableResume) {
		menuFrame = new JFrame(MENU_WINDOW_TITLE);
		menuFrame.addWindowListener(new WindowCloser());


		// Create menu buttons
		buttonPanel = new JPanel();
		buttonPanel.add(createMenuButton(BUTTON_NEW_GAME_TITLE,
				ICON_NEW_GAME_FILENAME, NEW_GAME));
		buttonPanel.add(createMenuButton(BUTTON_RESUME_GAME_TITLE,
				ICON_RESUME_GAME_FILENAME, RESUME_GAME, enableResume,
				ICON_RESUME_GAME_DISABLED_FILENAME));
		buttonPanel.add(createMenuButton(BUTTON_SHOW_CREDITS_TITLE,
				ICON_SHOW_CREDITS_FILENAME, SHOW_CREDITS));
		buttonPanel.add(createMenuButton(BUTTON_EXIT_GAME_TITLE,
				ICON_EXIT_GAME_FILENAME, EXIT_GAME));
		buttonPanel.setLayout(new GridLayout(4,1));
		buttonPanel.doLayout();
		menuFrame.setContentPane(buttonPanel);

		// Create credits screen
		creditsPanel = new JPanel();
		creditsPanel.setLayout(new BoxLayout(creditsPanel, BoxLayout.Y_AXIS));
		creditsPanel.add(new JLabel(
				createImageIcon(ICON_CREDITS_SCREEN_FILENAME)), BorderLayout.CENTER);
		JButton returnButton = new JButton("Return to menu");
		returnButton.setActionCommand(Integer.toString(HIDE_CREDITS));
		returnButton.addActionListener(this);
		creditsPanel.add(returnButton);

		// Slim appearance
		menuFrame.pack();
		menuFrame.setSize(new Dimension(212, 250));

		// Position on screen
		centerOnScreen();
	}

	/**
	 * Centers the frame on screen.
	 */
	private void centerOnScreen() {
		GraphicsConfiguration screen =
				GraphicsEnvironment.getLocalGraphicsEnvironment().
				getDefaultScreenDevice().getDefaultConfiguration();
		int x = (int) (screen.getBounds().getCenterX() -
				 (menuFrame.getWidth() / 2));
		int y = (int) (screen.getBounds().getCenterY() -
				 (menuFrame.getHeight() / 2));
		menuFrame.setLocation(x, y);
	}

	/**
	 * Creates a new menu with four buttons. The new JFrame will be placed
	 * in the middle of the screen.
	 */
	public Menu() {
		this(false);
	}

	/**
	 * Creates a new button with a given title and action command and adds
	 * it to the menu's content pane.
	 *
	 * @param buttonTitle the caption of the button.
	 * @param actionCommand the command to send to the listener when the button
	 * 		  is clicked.
	 * @param enabled whether the button should be enabled or not.
	 */
	private JButton createMenuButton(
			String buttonTitle,
			String iconFileName,
			int actionCommand,
			boolean enabled,
			String disabledIconFileName)
		{
		Icon icon = createImageIcon(iconFileName);
		Icon disabledIcon = createImageIcon(disabledIconFileName);
		JButton newButton = new JButton(icon);
		newButton.setDisabledIcon(disabledIcon);
		newButton.setEnabled(enabled);
		newButton.setActionCommand(Integer.toString(actionCommand));
		newButton.addActionListener(this);
		return newButton;
	}

	private ImageIcon createImageIcon(String path) {
		URL imgURL = TurtleIslandGame.class.getResource(path);
		return new ImageIcon(imgURL);
	}


	/**
	 * Creates a new button with a given title and action command and adds
	 * it to the menu's content pane.
	 *
	 * @param buttonTitle the caption of the button.
	 * @param actionCommand the command to send to the listener when the button
	 * 		  is clicked.
	 */
	private JButton createMenuButton(
			String buttonTitle,
			String iconFileName,
			int actionCommand)
		{
		return createMenuButton(buttonTitle, iconFileName, actionCommand, true, "");
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Listens to the buttons. Will notify all observers that a button is pressed.
	 *
	 * @param ae the action event from the pressed button.
	 */
	public void actionPerformed(ActionEvent ae) {
		userResponse = Integer.parseInt(ae.getActionCommand());
		if (userResponse == SHOW_CREDITS) {
			userResponse = NO_RESPONSE;
			showCredits();
		} else if (userResponse == HIDE_CREDITS) {
			userResponse = NO_RESPONSE;
			hideCredits();
		}
	}

	/**
	 *
	 *
	 *
	 */
	private void showCredits() {
		menuFrame.setContentPane(creditsPanel);
		menuFrame.pack();
		centerOnScreen();
	}

	/**
	 *
	 *
	 *
	 */
	private void hideCredits() {
		menuFrame.pack();
		menuFrame.setSize(new Dimension(212, 250));
		menuFrame.setContentPane(buttonPanel);
		centerOnScreen();
	}

	/**
	 * Returns the number of the button the user pressed. Returns NO_RESPONSE
	 * if no button is pressed.
	 *
	 * @return the number of the button the user pressed.
	 */
	public int userResponse() {
		return userResponse;
	}

	/**
	 * Make the menu visible on screen.
	 */
	public void show() {
		menuFrame.show();
		menuFrame.toFront();
	}

	/**
	 * Make the menu invisible on screen.
	 */
	public void hide() {
		menuFrame.hide();
	}

	/**
	 * This class sets the userResponse as if the user clicked the exit button
	 * when the form is closed.
	 */
	class WindowCloser extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			userResponse = EXIT_GAME;
		}
	}

}




