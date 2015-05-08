/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
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

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;

import com.lp.util.Helper;

public class ImageViewer extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel viewer;
	public BufferedImage originalImage;
	private Image currentImage;
	private double scale;

	public ImageViewer(byte[] image) throws IOException {
		super();
		setImage(image);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	}

	public void setImage(byte[] image) {
		setImage(Helper.byteArrayToImage(image));
	}
	
	@Override
	public Dimension getPreferredSize() {
		if(originalImage == null)
			return super.getPreferredSize();
		return new Dimension(originalImage.getWidth(), originalImage.getHeight());
	}

	public void setImage(BufferedImage image) {

		scale = 0;
		originalImage = image;
		currentImage = originalImage;
		viewer = new JLabel();
		if (image != null) {
			viewer.setIcon(new ImageIcon(currentImage));
		} else {
			viewer.setIcon(null);
		}
		setViewportView(viewer);
		viewer.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				doScale(e);
			}

			@Override
			public void componentShown(ComponentEvent e) {
				if (scale == 0) {
					doScale(e);
				}
			}

			private void doScale(ComponentEvent e) {
				double newscale;
				int h = e.getComponent().getHeight();
				int w = e.getComponent().getWidth();
				if (e.getComponent().getParent() != null && scale == 0) {
					int hp = e.getComponent().getParent().getHeight();
					int wp = e.getComponent().getParent().getWidth();
					double dx = (double) w / (double) wp;
					double dy = (double) h / (double) hp;
					if (dx != 1.0 && dy != 1.0) {
						if (dx >= dy) {
							newscale = (double) Math.round(1.0 / dx * 10) / 10;
						} else {
							newscale = (double) Math.round(1.0 / dy * 10) / 10;
						}
						if (newscale >= 0.1) {
							if (scale != newscale) {
								scale = newscale;
								setActualScale(0);
							}
						}
					}
				}
			}

		});

		viewer.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				int u = e.getUnitsToScroll();
				if (scale == 0)
					scale = calcScale(e);
				if (u > 0) {
					setActualScale(0.1);
				}
				if (u < 0) {
					setActualScale(-0.1);
				}
			}

			private double calcScale(MouseWheelEvent e) {
				if (e.getComponent().getParent() != null) {
					int h = e.getComponent().getHeight();
					int w = e.getComponent().getWidth();
					int hp = e.getComponent().getParent().getHeight();
					int wp = e.getComponent().getParent().getWidth();
					double dx = (double) w / (double) wp;
					double dy = (double) h / (double) hp;
					if (dx >= dy) {
						return (double) Math.round(1.0 / dx * 10) / 10;
					} else {
						return (double) Math.round(1.0 / dy * 10) / 10;
					}
				}
				return 0.0;
			}
		});

		MouseAdapter mAdapter = new MouseAdapter() {
			private Point initPoint;

			public void mousePressed(MouseEvent e) {
				initPoint = e.getPoint();
			}

			public void mouseReleased(MouseEvent e) {
//				int x = initPoint.x - e.getPoint().x;
//				int y = initPoint.y - e.getPoint().y;
//				JScrollBar h = getHorizontalScrollBar();
//				if (h != null) {
//					h.setValue(h.getValue() + (x / h.getUnitIncrement()));
//				}
//				JScrollBar v = getVerticalScrollBar();
//				if (v != null) {
//					v.setValue(v.getValue() + (y / v.getUnitIncrement()));
//				}
				initPoint = null;
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (initPoint == null)
					return;
				int x = initPoint.x - e.getPoint().x;
				int y = initPoint.y - e.getPoint().y;
				JScrollBar h = getHorizontalScrollBar();
				if (h != null) {
					h.setValue(h.getValue() + (x / h.getUnitIncrement()));
				}
				JScrollBar v = getVerticalScrollBar();
				if (v != null) {
					v.setValue(v.getValue() + (y / v.getUnitIncrement()));
				}
				repaint();
			}
		};

		viewer.addMouseMotionListener(mAdapter);
		viewer.addMouseListener(mAdapter);
	}

	public void setActualScale(double adjust) {
		if (originalImage != null) {
			scale += adjust;

			int newWidth = (int) (originalImage.getWidth() * scale);
			int newHeight = (int) (originalImage.getHeight() * scale);
			if (newWidth > 0 && newHeight > 0) {
				currentImage = originalImage.getScaledInstance(newWidth,
						newHeight, Image.SCALE_FAST);
				viewer.setIcon(new ImageIcon(currentImage));
			} else {
				scale -= adjust;
			}
		}
	}
	
	private class ScrollablePicture extends JLabel implements Scrollable {

		private static final long serialVersionUID = 954209497310980185L;

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return getPreferredSize();
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			// TODO Auto-generated method stub
			return 5;
		}
		
	}
}
