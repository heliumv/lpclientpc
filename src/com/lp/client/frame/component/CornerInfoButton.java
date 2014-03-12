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
 * Ein kleines blaues Dreieck in einer Component.
 * Zeigt einen WrapperToolTip an.
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
 * @version not attributable Date $Date: 2013/01/19 11:47:13 $
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.lp.client.frame.Defaults;
import com.lp.client.pc.LPMain;

public class CornerInfoButton extends JButton implements MouseListener {

	private static final long serialVersionUID = -6025101542645194436L;

	private final JComponent parent;

//	private static int counter = 0;

	private static final int INVALID_STATE = CornerInfoIcon.INVALID_STATE;
	private static final int DEFAULT_STATE = CornerInfoIcon.DEFAULT_STATE;
	private static final int ACTIVE_STATE = CornerInfoIcon.ACTIVE_STATE;
	private static final int SET_STATE = CornerInfoIcon.SET_STATE;
	private static final int EMPTY_STATE = CornerInfoIcon.EMPTY_STATE;

	private ImageIcon imageIcon = CornerInfoIcon.getCornerIcon(DEFAULT_STATE);
	private String toolTipToken = null;
	private WrapperToolTip toolTip = null;
	private Timer timer;
	private boolean firstTimePainted = true;
	private boolean addedToLayeredPane = false;
	private int delayIn = 500;
	private int delayOut = 500;
	private Point insetsByParent = null;
	private JLayeredPane layeredPane = null;
	private Boolean isParentCheckBox = null;
	private Boolean isParentRadioButton = null;

	private ComponentListener componentListener;
	private HierarchyBoundsListener hierarchyBoundsListener;
	private HierarchyListener hierarchyListener;
	private InternalFrameListener internalFrameListener;

	public CornerInfoButton(IDirektHilfe parentComp) {
		super();
		if (!Defaults.getInstance().isDirekthilfeEnabled()) {
			parent = null;
			return;
		}
		parent = (JComponent) parentComp;

		setDefaults();
		addListeners();
		refreshState();
	}

	private void setDefaults() {
		setVisible(true);
		setIconState(DEFAULT_STATE);
		setContentAreaFilled(false);
		setFocusPainted(false);
		setFocusable(false);
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		setBorderPainted(false);
		super.setBounds(0, 0, 1, 1); // damit paint() aufgerufen wird
	}

	private void addListeners() {
		if (isParentCheckBox() || isParentRadioButton()) {
			parent.addMouseListener(this);
			delayIn = 1000;
		} else
			addMouseListener(this);

		componentListener = new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				updateVisibleState();
				refreshState();
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				updateCornerLocation();
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				updateCornerLocation();
			}

			@Override
			public void componentHidden(ComponentEvent arg0) {
				updateVisibleState();
			}
		};

		hierarchyBoundsListener = new HierarchyBoundsListener() {

			@Override
			public void ancestorResized(HierarchyEvent e) {
				updateCornerLocation();

			}

			@Override
			public void ancestorMoved(HierarchyEvent e) {
				updateCornerLocation();
			}
		};

		hierarchyListener = new HierarchyListener() {

			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				updateVisibleState();
			}
		};

		parent.addComponentListener(componentListener);
		parent.addHierarchyBoundsListener(hierarchyBoundsListener);
		parent.addHierarchyListener(hierarchyListener);

	}

	private void addMeToLayeredPane() {
		if (layeredPane == null)
			layeredPane = parent.getRootPane().getLayeredPane();
		layeredPane.add(this, JLayeredPane.DRAG_LAYER);
		addedToLayeredPane = true;
		addInternalFrameListener();

		if (toolTipToken != null)
			setToolTipToken(toolTipToken);
		refreshState();
	}

	private void removeFromLayeredPane() {
		if (addedToLayeredPane) {
			layeredPane.remove(this);
			addedToLayeredPane = false;
			layeredPane = null;
			this.removeAll();
			this.removeNotify();
		}
	}

	private void setIconState(int state) {
		if (state < DEFAULT_STATE || state > EMPTY_STATE) {
			setIcon(null);
			setVisible(false);
		} else {
			imageIcon = CornerInfoIcon.getCornerIcon(state);
		}
		refreshToggleButtonIcons(state);
	}

	private void refreshToggleButtonIcons(int state) {
		if (isParentCheckBox()) {
			prepareCheckBoxIcons(state);
			setVisible(false);
		} else if (isParentRadioButton()) {
			prepareRadioButtonIcons(state);
			setVisible(false);
		} else if (state != INVALID_STATE) {
			setIcon(imageIcon);
		}
	}

	@Override
	public void setVisible(boolean b) {
		if (isParentCheckBox() || isParentRadioButton())
			b = false;
		super.setVisible(b);
	}

	public void refreshState() {
		// ACTIVE (schwarz): Tooltip wird gerade angezeigt.
		// DEFAULT (blau): Keine Entwicklerrechte: message.properties vorhanden
		// (hat Text)
		// INVALID(-): Keine Entwicklerrechte: token fehlt in den
		// message.properties
		// EMPTY (rot): Entwicklerrechte: token fehlt in den message.properties
		// oder Tooltip == null
		// SET (gruen): Entwicklerrechte: token ist in den message.properties
		// vorhanden

		int state = ACTIVE_STATE;

		if (toolTip == null) {
			state = LPMain.getInstance().isLPAdmin() ? EMPTY_STATE
					: INVALID_STATE;
		} else if (!toolTip.isShowing()) {
			if (LPMain.getInstance().isLPAdmin()) {
				state = toolTip.isEmptyTip() ? EMPTY_STATE : SET_STATE;
			} else {
				state = toolTip.isEmptyTip() ? INVALID_STATE : DEFAULT_STATE;
			}
		}

		setIconState(state);
		repaintLayeredPane();

	}

	private void prepareRadioButtonIcons(int state) {
		AbstractButton button = ((AbstractButton) parent);

		button.setIcon(CornerInfoIcon.getRadioButtonIcon(state,
				"defaultIcon.png"));
		button.setPressedIcon(CornerInfoIcon.getRadioButtonIcon(state,
				"pressedIcon.png"));
		button.setRolloverIcon(CornerInfoIcon.getRadioButtonIcon(state,
				"rollOverIcon.png"));
		button.setSelectedIcon(CornerInfoIcon.getRadioButtonIcon(state,
				"selectedIcon.png"));
		button.setRolloverSelectedIcon(CornerInfoIcon.getRadioButtonIcon(state,
				"rollOverSelectedIcon.png"));
		button.setDisabledIcon(CornerInfoIcon.getRadioButtonIcon(state,
				"disabledIcon.png"));
		button.setDisabledSelectedIcon(CornerInfoIcon.getRadioButtonIcon(state,
				"disabledSelectedIcon.png"));
	}

	private void prepareCheckBoxIcons(int state) {
		AbstractButton button = ((AbstractButton) parent);

		button.setIcon(CornerInfoIcon.getCheckBoxIcon(state, "defaultIcon.png"));
		button.setPressedIcon(CornerInfoIcon.getCheckBoxIcon(state,
				"pressedIcon.png"));
		button.setRolloverIcon(CornerInfoIcon.getCheckBoxIcon(state,
				"rollOverIcon.png"));
		button.setSelectedIcon(CornerInfoIcon.getCheckBoxIcon(state,
				"selectedIcon.png"));
		button.setRolloverSelectedIcon(CornerInfoIcon.getCheckBoxIcon(state,
				"rollOverSelectedIcon.png"));
		button.setDisabledIcon(CornerInfoIcon.getCheckBoxIcon(state,
				"disabledIcon.png"));
		button.setDisabledSelectedIcon(CornerInfoIcon.getCheckBoxIcon(state,
				"disabledSelectedIcon.png"));
	}

	@Override
	public void setBounds(int arg0, int arg1, int arg2, int arg3) {
		updateCornerLocation();
//		System.out.println("setbounds");
	}

	@Override
	public void setBounds(Rectangle arg0) {
		updateCornerLocation();
//		System.out.println("setbounds");
	}

	public void paint(Graphics g) {
		if (firstTimePainted) {
			updateCornerLocation();
			firstTimePainted = false;
		}
		super.paint(g);
	}

	private void addInternalFrameListener() {
		final JInternalFrame frame = (JInternalFrame) SwingUtilities
				.getAncestorOfClass(JInternalFrame.class, parent);
		if (frame != null) {
//			System.out.println(++counter);
			internalFrameListener = new InternalFrameAdapter() {
				@Override
				public void internalFrameClosed(InternalFrameEvent arg0) {
					finalizeCib();
					frame.removeInternalFrameListener(this);
					((IDirektHilfe) parent).removeCib();
//					System.out.println(--counter);
				}
			};

			frame.addInternalFrameListener(internalFrameListener);
		} //else System.out.println("notadded");
	}

	private void finalizeCib() {
		removeFromLayeredPane();
		parent.removeComponentListener(componentListener);
		parent.removeHierarchyBoundsListener(hierarchyBoundsListener);
		parent.removeHierarchyListener(hierarchyListener);
		componentListener = null;
		hierarchyBoundsListener = null;
		hierarchyListener = null;
		clearTimer();
	}

	private Point getPositionInRootPane() {
		Component comp = parent;
		Point location = getLocation();
		while (comp != null && !(comp instanceof JRootPane)) {
			location.translate(comp.getX(), comp.getY());
			comp = comp.getParent();
		}
		return location;
	}

	@Override
	public Point getLocation() {
		insetsByParent = ((IDirektHilfe) parent).getLocationOffset();

		return new Point(parent.getWidth() - imageIcon.getIconWidth()
				- insetsByParent.x, insetsByParent.y);
	}

	public void updateCornerLocation() {
		if (!addedToLayeredPane)
			return;
		Point p = getPositionInRootPane();
		super.setBounds(p.x, p.y, imageIcon.getIconWidth(),
				imageIcon.getIconHeight());
		repaintLayeredPane();
	}

	protected void initToolTip() {
		if (!Defaults.getInstance().isDirekthilfeEnabled()) {
			return;
		}

		toolTip = new WrapperToolTip(this, toolTipToken);

		toolTip.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
				refreshState();
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				refreshState();
			}
		});

	}

	public void setToolTipToken(String token) {
		toolTipToken = token;
		if (addedToLayeredPane) {
			if (toolTip == null)
				initToolTip();
			toolTip.setToolTipToken(token);
		}
	}
	
	public String getToolTipToken() {
		return toolTipToken;
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON3 && toolTip != null) {
			toolTip.tokenToClipboard();
		}
	}

	public Container getParent() {
		return parent;
	}

	private void handleTimerOnCornerInfoButton(ToolTipTimerTask task,
			int delayTime) {
		clearTimer();
		timer = new Timer("cibTimer");
		timer.schedule(task, delayTime);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (toolTip == null && LPMain.getInstance().isLPAdmin()) {
			initToolTip();
		}
		handleTimerOnCornerInfoButton(new ToolTipTimerTaskShow(toolTip),
				delayIn);

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		handleTimerOnCornerInfoButton(new ToolTipTimerTaskHide(toolTip),
				delayOut);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		/*
		 * try { Desktop.getDesktop().browse(new
		 * URI("http://docs.heliumv.org/")); } catch (IOException e) {
		 * e.printStackTrace(); } catch (URISyntaxException e) {
		 * e.printStackTrace(); }
		 */
	}
	
	private void clearTimer() {
		if(timer != null) {
			timer.cancel();
			timer.purge();
		}
	}

	public void updateVisibleState() {
		if (!addedToLayeredPane && parent.getRootPane() != null
				&& parent.isShowing()) {
			addMeToLayeredPane();
		}
		if(!parent.isShowing()) {
			clearTimer();
		}
		setVisible(parent.isShowing());
		repaintLayeredPane();
	}

	private void repaintLayeredPane() {
		if (layeredPane != null)
			layeredPane.repaint();
	}

	public boolean isParentCheckBox() {
		if (isParentCheckBox == null)
			isParentCheckBox = (parent instanceof WrapperCheckBox);
		return isParentCheckBox;
	}

	public boolean isParentRadioButton() {
		if (isParentRadioButton == null)
			isParentRadioButton = (parent instanceof WrapperRadioButton);
		return isParentRadioButton;
	}
}