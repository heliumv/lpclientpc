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
package com.lp.client.system;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelImage;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;

@SuppressWarnings("static-access")
public class PanelMediastandardBild
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
public static final String EXTENSION_JPG = ".jpg";
  public static final String EXTENSION_JPEG = ".jpeg";
  public static final String EXTENSION_PNG = ".png";
  public static final String EXTENSION_GIF = ".gif";

  protected WrapperNumberField wnfGroesse = new WrapperNumberField();
  protected WrapperTextField wtfGroesse = new WrapperTextField();
  protected WrapperTextField wtfDatei = new WrapperTextField();

  private WrapperButton wbuDatei = new WrapperButton();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private PanelImage jpaBild = null;
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JPanel jpaWorkingOn = new JPanel();
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private WrapperLabel wlaGroesse = new WrapperLabel();
  private WrapperLabel wlaKb = new WrapperLabel();

  private Object currentArt;

  private final static String ACTION_SPECIAL_DATEI = "action_special_datei";

  /**
   * Konstruktor.
   *
   * @param internalFrame der InternalFrame auf dem das Panel sitzt
   * @throws Throwable
   */
  public PanelMediastandardBild(InternalFrame internalFrame)
      throws Throwable {
    super(internalFrame, "");
    jbInit();
    initComponents();
  }


  /**
   * jbInit
   */
  private void jbInit() {
    this.setLayout(new GridBagLayout());
    jpaBild = new PanelImage(null);
    wbuDatei.setText(LPMain.getInstance().getTextRespectUISPr("lp.datei"));
    wlaGroesse.setText(LPMain.getInstance().getTextRespectUISPr("lp.groesse"));
    wlaKb.setText(LPMain.getInstance().getTextRespectUISPr("lp.kb"));
    wlaKb.setHorizontalAlignment(SwingConstants.LEFT);
    jpaBild.setLayout(gridBagLayout1);
    jpaWorkingOn.setLayout(gridBagLayout2);
    wbuDatei.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wbuDatei.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wtfGroesse.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wtfGroesse.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wnfGroesse.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wnfGroesse.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wtfDatei.setColumnsMax(MediaFac.MAX_MEDIASTANDARD_DATEINAME);
    wtfDatei.setActivatable(false);
    wnfGroesse.setActivatable(false);
    wtfGroesse.setActivatable(false);
    wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI);
    wbuDatei.addActionListener(this);

    jScrollPane1.getViewport().add(jpaBild);
    this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 0, 0, 0), 0,
                                                  0));
    jpaWorkingOn.add(wbuDatei,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.NONE,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfDatei,
                     new GridBagConstraints(1, iZeile, 3, 1, 1.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(wlaGroesse,
                     new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wtfGroesse,
                     new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wnfGroesse,
                     new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    jpaWorkingOn.add(wlaKb,
                     new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
    iZeile++;
    jpaWorkingOn.add(jScrollPane1,
                     new GridBagConstraints(1, iZeile, 3, 1, 0.0, 1.0,
                                            GridBagConstraints.CENTER,
                                            GridBagConstraints.BOTH,
                                            new Insets(2, 2, 2, 2), 0, 0));
  }


  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_DATEI)) {
      JFileChooser fc = new JFileChooser();

      fc.setFileFilter(new FileFilter()
      {
        public boolean accept(File f) {
          boolean acc = true;
          if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(EXTENSION_GIF);
          }
          if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(EXTENSION_JPEG) ||
                f.getName().toLowerCase().endsWith(EXTENSION_JPG);
          }
          if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(EXTENSION_PNG);
          }
          return acc;
        }


        public String getDescription() {
          String fileType = null;
          if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
            fileType = "GIF" + "(" + "*" + EXTENSION_GIF + ")";
          }
          if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)) {
            fileType = "JPEG" + "(" + "*" + EXTENSION_JPG + "," + "*" + EXTENSION_JPEG + ")";
          }
          if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {
            fileType = "PNG" + "(" + "*" + EXTENSION_PNG + ")";
          }
          return fileType;

        }
      });
      int returnVal = fc.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {

        if (fc.getSelectedFile().getName().toLowerCase().endsWith(EXTENSION_JPEG) ||
            fc.getSelectedFile().getName().toLowerCase().endsWith(EXTENSION_JPG) ||
            fc.getSelectedFile().getName().toLowerCase().endsWith(EXTENSION_GIF) ||
            fc.getSelectedFile().getName().toLowerCase().endsWith(EXTENSION_PNG)) {
          File file = fc.getSelectedFile();

          wtfDatei.setText(file.getName());
          // darstellen
          jpaBild.setImage(ImageIO.read(file));
          if (jpaBild.getImage() != null) {
            wnfGroesse.setDouble(new Double( ( (double) file.length()) /
                                            ( (double) 1024)));
            wtfGroesse.setText(jpaBild.getImage().getWidth(this) + "x" +
                               jpaBild.getImage().getHeight(this));
            jScrollPane1.repaint();
            validate();
          } else {
            DialogFactory.showModalDialog(
             LPMain.getInstance().getTextRespectUISPr("lp.error"),
             LPMain.getInstance().getTextRespectUISPr("lp.error.dateizugross"));
          }
        }
        else {
          DialogFactory.showModalDialog(
              LPMain.getInstance().getTextRespectUISPr("lp.hint"),
              LPMain.getInstance().getTextRespectUISPr("lp.system.image.format"));
        }
      }
      else {
        // keine auswahl
      }
    }
  }


  Image getImage() {
    return jpaBild.getImage();
  }


  void setImage(BufferedImage image) {
    jpaBild.setImage(image);
  }


  protected void setCurrentArt(Object currentArt) {
    this.currentArt = currentArt;
  }


  protected Object getCurrentArt() {
    return currentArt;
  }


  protected String getExtension(File f) {
    String ext = null;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 && i < s.length() - 1) {
      ext = s.substring(i + 1).toLowerCase();
    }
    return ext;
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuDatei;
  }
}
