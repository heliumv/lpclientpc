/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.client.frame.component;

/**
 * <p>
 * Das WrapperToolTip Objekt zur Darstellung der Direkthilfe.
 * </p>
 * 
 * <p>
 * Copyright Helium V IT-Solutions (c) 2004 - 2012
 * </p>
 * 
 * <p>
 * Erstellung: Robert Kreiseder; 02.12.2012
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 * </p>
 * 
 * @version not attributable Date $Date: 2013/01/19 11:47:14 $
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTMLEditorKit;

import com.lp.client.frame.HelperClient;
import com.lp.client.pc.LPMain;
import com.lp.client.pc.SystemProperties;

public class WrapperToolTip extends JWindow implements ActionListener,
		MouseListener, MouseMotionListener, HierarchyBoundsListener,
		HierarchyListener {

	public static final String TOKEN_PREFIX = "tooltip.";

	/**
	 * 
	 */
	private static final long serialVersionUID = 7426797261164393138L;
	private static final Dimension TOOLTIP_MINSIZE = new Dimension(400, 225);
	private static final Dimension TOOLTIP_MAXSIZE = new Dimension(400, 500);
	private static final int SCREEN_INSET = 50;
	private static final Dimension SCREENSIZE = Toolkit.getDefaultToolkit()
			.getScreenSize();
	private static final int INSETS = 2;

	private static final List<String> standardNames = Arrays.asList("Desktop",
			"JDesktopPane", "JRootPane", "JLayeredPane", "JPanel",
			"JTabbedPane", "JSplitPane", "PanelSplit", "WrapperTextField",
			"CornerInfoButton", "WrapperNumberField", "WrapperEmailField",
			"WrapperURLField", "WrapperTelefonField", "WrapperComboBox",
			"WrapperButton", "WrapperGotoButton", "WrapperCheckBox",
			"WrapperRadioButton", "WrapperDateField", "JTextFieldDateEditor",
			"WrapperKeyValueField", "PaintingCornerInfoButton");
	
	private static final List<String> useComponentNaming = Arrays.asList("WrapperDateField");

	// private static final List<String> standardNames =
	// Arrays.asList("Desktop", "JDesktopPane", "JRootPane", "JLayeredPane",
	// "JPanel", "JTabbedPane", "JSplitPane", "PanelSplit", "CornerInfoButton",
	// "PaintingCornerInfoButton");

	private JTextPane textPane = null;
	private JPanel jPanel = null;
	private JScrollPane scrollPane = null;
	private JTextField textFieldTitle = null;
	private JButton editButton = null;
	private JButton exitButton = null;
	private JButton dockButton = null;
	private Boolean isStandalone = false;
	private ImageIcon imageIconEdit = HelperClient
			.createImageIcon("editToolTip.png");
	private ImageIcon imageIconClose = HelperClient
			.createImageIcon("closeToolTip.png");
	private ImageIcon imageIconDock = HelperClient
			.createImageIcon("dockToolTip.png");
	private JComponent parentComponent = null;
	private boolean hasCustomPosition = false;
	private Point dragOffset;
	private boolean isEmptyTip = true;
	private Timer resetTitleToTokenTimer = null;
	private String token;
	private String fullToken;
	private boolean mouseHover = false;

	private static WrapperToolTip existingToolTip = null;

	WrapperToolTip(JComponent parentComponent, String toolTipToken) {
		super(SwingUtilities.getWindowAncestor(parentComponent));
		this.parentComponent = parentComponent;
		super.setVisible(false);
		setFocusableWindowState(true);

		setAlwaysOnTop(true);
		setSize(TOOLTIP_MINSIZE);

		jPanel = new JPanel();
		jPanel.setLayout(null);
		this.getContentPane().add(jPanel);

		textFieldTitle = new JTextField("Direkthilfe");
		textFieldTitle.setDisabledTextColor(Color.black);
		textFieldTitle.setEditable(false);
		textFieldTitle.setFocusable(false);
		textFieldTitle.setBorder(null);
		textFieldTitle.setCursor(new Cursor(Cursor.MOVE_CURSOR));
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setEditorKit(new HTMLEditorKit());
		textPane.setFocusable(false);
		textPane.setText(null);

		scrollPane = new JScrollPane(textPane);

		editButton = new JButton();
		editButton.setIcon(imageIconEdit);
		editButton.setContentAreaFilled(false);
		editButton.setBorderPainted(false);
		editButton.setMargin(new Insets(0, 0, 0, 0));
		editButton.setFocusPainted(false);
		editButton.setVisible(true);

		exitButton = new JButton();
		exitButton.setIcon(imageIconClose);
		exitButton.setContentAreaFilled(false);
		exitButton.setBorderPainted(false);
		exitButton.setMargin(new Insets(0, 0, 0, 0));
		exitButton.setFocusPainted(false);
		exitButton.setVisible(false);
		// dockButton.setCursor(new Cursor(Cursor.));

		dockButton = new JButton();
		dockButton.setIcon(imageIconDock);
		dockButton.setContentAreaFilled(false);
		dockButton.setBorderPainted(false);
		dockButton.setMargin(new Insets(0, 0, 0, 0));
		dockButton.setFocusPainted(false);
		dockButton.setVisible(false);
		// dockButton.setCursor(new Cursor(Cursor.));

		packComponents();

		jPanel.add(textFieldTitle);
		jPanel.add(editButton);
		jPanel.add(exitButton);
		jPanel.add(dockButton);
		jPanel.add(scrollPane);

		editButton.addActionListener(this);
		exitButton.addActionListener(this);
		dockButton.addActionListener(this);
		textFieldTitle.addMouseMotionListener(this);
		textFieldTitle.addMouseListener(this);
		scrollPane.addMouseListener(this);
		scrollPane.getViewport().addMouseListener(this);
		jPanel.addMouseListener(this);
		textPane.addMouseListener(this);

		JInternalFrame internalFrame = (JInternalFrame) SwingUtilities
				.getAncestorOfClass(JInternalFrame.class, parentComponent);
		if (internalFrame != null)
			internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
				@Override
				public void internalFrameDeactivated(InternalFrameEvent arg0) {
					setVisible(false);
				}
			});
		if (SwingUtilities.getWindowAncestor(parentComponent) != null) {
			SwingUtilities.getWindowAncestor(parentComponent)
					.addWindowListener(new WindowAdapter() {
						public void windowDeactivated(WindowEvent arg0) {
							setVisible(false);
						}
					});
		}
		parentComponent.getParent().addHierarchyBoundsListener(this);
		parentComponent.getParent().addHierarchyListener(this);

		setStandalone(false);
		setHasCustomPosition(false);

		setToolTipToken(toolTipToken);
	}

	protected static boolean hasToolTip() {
		return existingToolTip != null;
	}

	private void packComponents() {
		Dimension size = this.getSize();
		textFieldTitle
				.setBounds(
						INSETS,
						INSETS,
						size.width
								- INSETS
								* 2
								- (isStandalone() ? imageIconClose
										.getIconWidth() : 0)
								- (hasCustomPosition() ? imageIconDock
										.getIconWidth() + INSETS : 0)
								- (isStandalone()
										&& LPMain.getInstance().isLPAdmin() ? imageIconEdit
										.getIconWidth() + INSETS
										: 0), imageIconClose.getIconHeight());
		scrollPane.setBounds(INSETS, imageIconClose.getIconHeight() + INSETS
				* 2, size.width - INSETS * 2,
				size.height - imageIconClose.getIconHeight() - INSETS * 3);
		exitButton.setBounds(size.width - imageIconClose.getIconWidth()
				- INSETS, INSETS, imageIconClose.getIconWidth(),
				imageIconClose.getIconHeight());
		dockButton.setBounds(size.width - imageIconClose.getIconWidth()
				- imageIconDock.getIconWidth() - INSETS * 2, INSETS,
				imageIconDock.getIconWidth(), imageIconEdit.getIconHeight());
		editButton.setBounds(size.width
				- imageIconClose.getIconWidth()
				- (dockButton.isVisible() ? imageIconDock.getIconWidth()
						+ INSETS : 0) - imageIconEdit.getIconWidth() - INSETS
				* 2, INSETS, imageIconEdit.getIconWidth(),
				imageIconEdit.getIconHeight());
	}

	// public void dispose() {
	// setVisible(false);
	// }

	public boolean isEmptyTip() {
		return isEmptyTip;
	}

	public boolean isStandalone() {
		return isStandalone;
	}

	public boolean isMouseHover() {
		return mouseHover;
	}

	private void setStandalone(boolean b) {
		isStandalone = b;
		if (!b) {
			jPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
			jPanel.setBackground(new Color(255, 255, 240));
			scrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			scrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		} else {
			jPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			jPanel.setBackground(new Color(255, 255, 225));
			scrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPane
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		}
		textPane.setBackground(jPanel.getBackground());
		textFieldTitle.setBackground(jPanel.getBackground());
		scrollPane.setBackground(jPanel.getBackground());
		scrollPane.getViewport().setBackground(jPanel.getBackground());
		scrollPane.setBorder(jPanel.getBorder());
		exitButton.setVisible(b);
		if (LPMain.getInstance().isLPAdmin()) {
			editButton.setVisible(b);
		}
		updateToolTipLocation();
	}

	private Point getComponentMitteRelativ(Component comp) {
		return new Point(comp.getWidth() / 2, comp.getHeight() / 2);
	}

	private Point sumPoints(Point p1, Point p2) {
		return new Point(p1.x + p2.x, p1.y + p2.y);
	}

	private boolean istAufDerLinkenHaelfte(Component comp) {
		Point p = sumPoints(comp.getLocationOnScreen(),
				getComponentMitteRelativ(comp));
		return p.x < SCREENSIZE.width / 2;
	}

	private boolean istAufDerOberenHaelfte(Component comp) {
		Point p = sumPoints(comp.getLocationOnScreen(),
				getComponentMitteRelativ(comp));
		return p.y < SCREENSIZE.height / 2;
	}

	private int getXByParent(Component comp) {
		if (istAufDerLinkenHaelfte(comp))
			return comp.getLocationOnScreen().x;
		else
			return comp.getLocationOnScreen().x + comp.getWidth()
					- this.getWidth();
	}

	private int getYByParent(Component comp) {
		if (istAufDerOberenHaelfte(comp))
			return comp.getLocationOnScreen().y + comp.getHeight();
		else
			return comp.getLocationOnScreen().y - this.getHeight();

	}

	private Point getLocationByParent(Component comp) {
		return new Point(getXByParent(comp), getYByParent(comp));
	}

	private Dimension getSizeByParent(Component comp) {
		Dimension newSize = new Dimension();
		Point parentLocation = comp.getLocationOnScreen();

		newSize.width = this.getWidth();

		if (istAufDerOberenHaelfte(comp)) {
			newSize.height = SCREENSIZE.height - parentLocation.y
					- comp.getHeight() - SCREEN_INSET;
		} else {
			newSize.height = parentLocation.y - SCREEN_INSET;
		}

		return newSize;
	}

	private Dimension getOptimizedSizeByTextPane(Dimension currentSize,
			JEditorPane textPane, boolean allowEnlarge) {
		Dimension newSize = new Dimension(currentSize);
		int heightDifference = this.getHeight()
				- textPane.getParent().getHeight();

		newSize.height = textPane.getHeight() + heightDifference;

		if (!allowEnlarge && newSize.height > currentSize.height)
			return currentSize;
		return newSize;
	}

	private Dimension toValidSize(Dimension d) {
		Dimension newSize = new Dimension(d);
		newSize.width = d.width < TOOLTIP_MINSIZE.width ? TOOLTIP_MINSIZE.width
				: newSize.width;
		newSize.height = d.height < TOOLTIP_MINSIZE.height ? TOOLTIP_MINSIZE.height
				: newSize.height;

		newSize.width = d.width > TOOLTIP_MAXSIZE.width ? TOOLTIP_MAXSIZE.width
				: newSize.width;
		newSize.height = d.height > TOOLTIP_MAXSIZE.height ? TOOLTIP_MAXSIZE.height
				: newSize.height;

		return newSize;
	}

	public void updateToolTipLocation() {
		if (!(parentComponent.getParent().isShowing()))
			return;
		if (hasCustomPosition()) {
			setSize(toValidSize(getOptimizedSizeByTextPane(getSize(), textPane,
					true)));
		} else {
			Dimension size = isStandalone ? getOptimizedSizeByTextPane(
					getSizeByParent(parentComponent.getParent()), textPane,
					false) : getSize();

			setSize(toValidSize(size));
			setLocation(getLocationByParent(parentComponent.getParent()));

		}
		packComponents();
	}

	public void setVisible(boolean b) {
		if (b && (!isEmptyTip() || LPMain.getInstance().isLPAdmin())) {
			replaceExistingToolTip(this);
		} else {
			super.setVisible(false);
			if (textPane.isEditable()) {
				String t = token;
				setToolTipToken(null);
				setToolTipToken(t);
				setEditable(false);
			}
		}
	}

	private void superSetVisible(boolean b) {
		super.setVisible(b);
	}

	private static void replaceExistingToolTip(WrapperToolTip newTip) {
		if (newTip.equals(existingToolTip)) {
			if (!newTip.hasCustomPosition())
				newTip.setStandalone(false);
			newTip.superSetVisible(true);
			return;
		}

		if (existingToolTip != null) {
			existingToolTip.setVisible(false);
			if (existingToolTip.hasCustomPosition()) {
				newTip.setVisible(false);
				newTip.setStandalone(true);
				newTip.setHasCustomPosition(true);
				newTip.setLocation(existingToolTip.getLocation());
			} else {
				newTip.setHasCustomPosition(false);
				newTip.setStandalone(false);
			}
		}

		existingToolTip = newTip;
		newTip.superSetVisible(true);
		newTip.updateToolTipLocation();
	}

	public void setLocation(Point p) {
		if (p != null)
			super.setLocation(p);
	}

	public boolean hasCustomPosition() {
		return hasCustomPosition;
	}

	private void setHasCustomPosition(boolean b) {
		hasCustomPosition = b;
		dockButton.setVisible(b);
	}

	public String getToolTipToken() {
		return token;
	}
	public String getFullToolTipToken() {
		return fullToken;
	}

	public void setToolTipToken(String token) {
		if (token == null) {
			this.token = null;
			this.fullToken = null;
			textPane.setText("");
			return;
		}
		if (isEmptyTip == false && token.equals(this.token))
			return;
		String compPath = getParentComponentPath(parentComponent, false);
		String searchToken = new String(compPath + token);
		String toolTipText = ToolTipFileWriter.getToolTip(TOKEN_PREFIX
				+ searchToken);
		if (toolTipText == null)
			toolTipText = LPMain.getTextRespectUISPrWithNull(TOKEN_PREFIX
					+ searchToken);

		this.fullToken = TOKEN_PREFIX + searchToken;
		while (toolTipText == null && searchToken.indexOf(".") > -1) {
			searchToken = searchToken.substring(searchToken.indexOf(".") + 1);
			toolTipText = ToolTipFileWriter.getToolTip(TOKEN_PREFIX
					+ searchToken);
			if (toolTipText == null)
				toolTipText = LPMain.getTextRespectUISPrWithNull(TOKEN_PREFIX
						+ searchToken);
		}

		textPane.setText(toolTipText);
		textPane.setCaretPosition(0);
		isEmptyTip = (toolTipText == null);

		if (LPMain.getInstance().isLPAdmin()) {
			setTokenToTitleText(compPath + token, searchToken,
					toolTipText != null);
			textFieldTitle.setFocusable(!SystemProperties.isMacOs());
		} else {
			textFieldTitle.setFocusable(false);
			textFieldTitle.setText("Direkthilfe");
		}
		updateToolTipLocation();

		
	}

	private void setTokenToTitleText(String fullToken, String workingToken,
			boolean foundText) {
		int index = (fullToken).indexOf(workingToken);
		textFieldTitle.setSelectedTextColor(Color.white);
		if (!foundText) {
			textFieldTitle.setText(TOKEN_PREFIX + fullToken);
			textFieldTitle.setSelectionColor(new Color(150, 0, 0));
		} else if (index > 0) {
			textFieldTitle.setText(TOKEN_PREFIX + "("
					+ (fullToken).substring(0, index) + ")" + workingToken);
			textFieldTitle.setSelectionColor(new Color(0, 150, 0));
		} else {
			textFieldTitle.setText(TOKEN_PREFIX + workingToken);
			textFieldTitle.setSelectionColor(new Color(0, 150, 0));
		}

		textFieldTitle.setToolTipText(textFieldTitle.getText());

	}

	private String getParentComponentPath(Component o, boolean withStandards) {
		StringBuffer path = new StringBuffer();

		while (o != null) {

			String myName = o.getClass().getSimpleName();

			if(useComponentNaming(myName) && o.getName() != null && !o.getName().isEmpty()) {
				path.insert(0, o.getName() + ".");
			} else if (withStandards || !isStandardComponentName(myName)) {
				path.insert(0, myName + ".");
			}
			o = o.getParent();
		}

		return path.toString();
	}

	private boolean isStandardComponentName(String name) {
		return standardNames.contains(name);
	}
	
	private boolean useComponentNaming(String name) {
		return useComponentNaming.contains(name);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(exitButton)) {
			this.setVisible(false);
		} else if (event.getSource().equals(dockButton)) {
			setHasCustomPosition(false);
			updateToolTipLocation();
		} else if (event.getSource().equals(editButton)) {
			if (textPane.isEditable()) {
				ToolTipFileWriter.putToolTip(fullToken, textPane.getText());
				ToolTipFileWriter.saveToolTips();
				isEmptyTip = (textPane.getText() == null || textPane.getText().isEmpty());
			}
			setEditable(!textPane.isEditable());
		}
	}

	private void setEditable(boolean b) {
		String text = textPane.getText();
		if (b) {
			textPane.setEditorKit(new StyledEditorKit());
		} else {
			textPane.setEditorKit(new HTMLEditorKit());
		}
		textPane.setEditable(b);
		textPane.setFocusable(b);
		textPane.setText(text);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (!isStandalone) {
			setStandalone(true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		mouseHover = true;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		Point m = MouseInfo.getPointerInfo().getLocation();
		if (!this.getBounds().contains(m) && !isStandalone()) {
			this.setVisible(false);
			mouseHover = false;
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.getSource().equals(textFieldTitle)) {
			dragOffset = event.getLocationOnScreen();
			dragOffset.x -= this.getBounds().x;
			dragOffset.y -= this.getBounds().y;
			dockButton.setVisible(true);
			hasCustomPosition = true;
			updateToolTipLocation();
			setStandalone(true);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (dragOffset != null) {
			Point newLocation = event.getLocationOnScreen();
			newLocation.x -= dragOffset.x;
			newLocation.y -= dragOffset.y;
			setLocation(newLocation);
			updateToolTipLocation();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {

	}

	@Override
	public void ancestorMoved(HierarchyEvent arg0) {
		if (!hasCustomPosition && isShowing())
			updateToolTipLocation();
	}

	@Override
	public void ancestorResized(HierarchyEvent arg0) {
		if (isShowing())
			updateToolTipLocation();
	}

	@Override
	public void hierarchyChanged(HierarchyEvent arg0) {
		if (!isShowing())
			return;
		if (parentComponent.isShowing())
			updateToolTipLocation();
		else {
			setVisible(false);
		}

	}

	public void tokenToClipboard() {
		if (LPMain.getInstance().isLPAdmin() && resetTitleToTokenTimer == null) {
			StringSelection ss = new StringSelection(textFieldTitle.getText());
			Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(ss, null);

			final String token = textFieldTitle.getText();
			textFieldTitle.setText("Copied to Clipboard!");

			resetTitleToTokenTimer = new Timer();
			resetTitleToTokenTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					textFieldTitle.setText(token);
					resetTitleToTokenTimer.cancel();
					resetTitleToTokenTimer = null;
				}
			}, 2000);

		}
	}
}
