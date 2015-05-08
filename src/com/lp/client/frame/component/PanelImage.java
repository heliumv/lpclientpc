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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.JComponent;


public class PanelImage
    extends JComponent
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Image drawImage;
  private Image scaledImage;

  public PanelImage(Image image) {
    this.setDoubleBuffered(true);
    this.setSize(100,100);
    setImage(image);
  }


  public Image getImage() {
    return drawImage;
  }


  public void setImage(Image image) {
    if (image == null) {
      this.drawImage = null;
      this.scaledImage = null;
      return;
    }

    int width = image.getWidth(this);

    if (width <= 0) {
      MediaTracker tracker = new MediaTracker(this);
      tracker.addImage(image, 0);

      try {
        tracker.waitForID(0);
      }
      catch (InterruptedException ie) {}

      width = image.getWidth(this);
    }

    int height = image.getHeight(this);

    // workaround: beim ersten anzeigen des panels kommt immer 0 raus
    int thisWidth = this.getSize().width != 0 ? this.getSize().width : 382;
    int thisHeight = this.getSize().height != 0 ? this.getSize().height : 258;

    // groessenverhaeltnis
    double dW = ( (double) width) / ( (double) thisWidth);
    double dH = ( (double) height) / ( (double) thisHeight);
    int widthScaled;
    int heightScaled;
    if (dW >= dH) {
      widthScaled = thisWidth;
      heightScaled = (int) ( ( (double) height) / dW);
    }
    else {
      widthScaled = (int) ( ( (double) width) / dH);
      heightScaled = thisHeight;
    }

    this.setPreferredSize(new Dimension(widthScaled, heightScaled));
    this.drawImage = image;
    this.scaledImage=drawImage.getScaledInstance(widthScaled, heightScaled, Image.SCALE_DEFAULT);
  }


  public void paintComponent(Graphics g) {
    if (scaledImage != null) {
      g.drawImage(scaledImage, 0, 0, this);
    }
    else {
      g.dispose();
    }
  }
}

