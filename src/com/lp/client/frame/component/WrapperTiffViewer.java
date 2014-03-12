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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;

@SuppressWarnings("static-access") 
/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: valentin $</p>
 *
 * @version not attributable Date $Date: 2008/08/11 08:39:21 $
 *
 * @todo scrollpane auf bild  PJ 5350
 * @todo texte uebersetzen  PJ 5350
 */
public class WrapperTiffViewer
    extends PanelBasis
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final static String ACTION_SPECIAL_DATEI = "action_special_datei";
  private final static String ACTION_SPECIAL_TIFFFIRST = "action_special_tifffirst";
  private final static String ACTION_SPECIAL_TIFFLAST = "action_special_tifflast";
  private final static String ACTION_SPECIAL_TIFFRIGHT = "action_special_tiffright";
  private final static String ACTION_SPECIAL_TIFFLEFT = "action_special_tiffleft";


  protected WrapperNumberField wnfGroesse = new WrapperNumberField();
  protected WrapperTextField wtfSeite = new WrapperTextField();

  private JPanel paWorkingOn = new JPanel();
//  private PanelImage jpaBild = null;
  private GridBagLayout gridBagLayout2 = new GridBagLayout();
  private WrapperButton wbuDatei = new WrapperButton();
  private WrapperLabel wlaSeite = new WrapperLabel();
  private WrapperLabel wlaKb = new WrapperLabel();
  private WrapperTextField wtfDatei = new WrapperTextField();

  private ButtonGroup bgTiffControls = null;
  private WrapperButton wbuTiffLeft = new WrapperButton();
  private WrapperButton wbuTiffRight = new WrapperButton();
  private WrapperButton wbuTiffFirst = new WrapperButton();
  private WrapperButton wbuTiffLast = new WrapperButton();

  private WrapperTextField fieldToDisplayFileName=null;

  private File sLetzteDatei=null;

  private ImageViewer imageviewer = new ImageViewer(null);

  private String bildExtension = new String(".tiff");
  private ImageDecoder decoder = null;
  private RenderedImage image[] = null;
  private SeekableStream ss = null;
  private byte[] imageOutput = null;

  private int imageIndex = 0;


  public WrapperButton getButtonDatei(){
    return wbuDatei;
  }

  public WrapperTiffViewer(InternalFrame internalFrame, String addTitel)
      throws Throwable {
    super(internalFrame, addTitel);
    jbInit();
    initComponents();
  }

  public WrapperTiffViewer(InternalFrame internalFrame, String addTitel, WrapperTextField fieldToDisplayFileName)
      throws Throwable {
    super(internalFrame, addTitel);
    this.fieldToDisplayFileName=fieldToDisplayFileName;
    jbInit();
    initComponents();
  }


  public void setDefaults()
      throws Throwable {
    decoder = null;
    image = null;
    ss = null;
    imageOutput = null;

  }

  private void jbInit()
      throws Throwable {
    this.setLayout(new GridBagLayout());
    wbuDatei.setText(LPMain.getInstance().getTextRespectUISPr("lp.datei"));
    wtfDatei.setColumnsMax(MediaFac.MAX_MEDIASTANDARD_DATEINAME);
    wtfDatei.setActivatable(false);
    wlaSeite.setText(LPMain.getInstance().getTextRespectUISPr("artikel.katalogseite.seite"));
    wlaKb.setText("kB");
    paWorkingOn.setLayout(gridBagLayout2);
    wbuDatei.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wbuDatei.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wtfSeite.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wtfSeite.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wnfGroesse.setMinimumSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wnfGroesse.setPreferredSize(new Dimension(80, Defaults.getInstance().getControlHeight()));
    wnfGroesse.setActivatable(false);
    wtfSeite.setActivatable(false);
    wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI);
    wbuDatei.addActionListener(this);
    bgTiffControls = new ButtonGroup();
    wbuTiffLeft.setIcon(new ImageIcon(getClass().getResource(
        "/com/lp/client/res/navigate_left.png")));
    wbuTiffLeft.setActionCommand(ACTION_SPECIAL_TIFFLEFT);
    wbuTiffLeft.addActionListener(this);
    wbuTiffRight.setIcon(new ImageIcon(getClass().getResource(
        "/com/lp/client/res/navigate_right.png")));
    wbuTiffRight.setActionCommand(ACTION_SPECIAL_TIFFRIGHT);
    wbuTiffRight.addActionListener(this);
    wbuTiffFirst.setIcon(new ImageIcon(getClass().getResource(
        "/com/lp/client/res/navigate_beginning.png")));
    wbuTiffFirst.setActionCommand(ACTION_SPECIAL_TIFFFIRST);
    wbuTiffFirst.addActionListener(this);
    wbuTiffLast.setIcon(new ImageIcon(getClass().getResource(
        "/com/lp/client/res/navigate_end.png")));
    wbuTiffLast.setActionCommand(ACTION_SPECIAL_TIFFLAST);
    wbuTiffLast.addActionListener(this);
    bgTiffControls.add(wbuTiffLeft);
    bgTiffControls.add(wbuTiffRight);
    bgTiffControls.add(wbuTiffFirst);
    bgTiffControls.add(wbuTiffLast);


    this.add(paWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
                                                 , GridBagConstraints.CENTER,
                                                 GridBagConstraints.BOTH,
                                                 new Insets(0, 0, 0, 0), 0, 0));


    paWorkingOn.add(wbuDatei,
                    new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(2, 2, 2, 2), 0, 0));
    paWorkingOn.add(wtfDatei,
                  new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                                         GridBagConstraints.CENTER,
                                         GridBagConstraints.HORIZONTAL,
                                         new Insets(2, 2, 2, 2), 0, 0));

    paWorkingOn.add(wbuTiffFirst,
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(2, 2, 2, 2), 0, 0));
    paWorkingOn.add(wbuTiffLast,
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(2, 2, 2, 2), 0, 0));
    paWorkingOn.add(wbuTiffLeft,
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(2, 2, 2, 2), 0, 0));
    paWorkingOn.add(wbuTiffRight,
                    new GridBagConstraints(3, 2, 1, 1, 0.1, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.HORIZONTAL,
                                           new Insets(2, 2, 2, 2), 0, 0));



    paWorkingOn.add(wlaSeite,
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(2, 2, 2, 2), 40, 0));
    paWorkingOn.add(wtfSeite,
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(2, 2, 2, 2), 0, 0));
    paWorkingOn.add(wnfGroesse,
                    new GridBagConstraints(1, 4, 1, 1, 0.1, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(2, 2, 2, 2), 0, 0));
    paWorkingOn.add(wlaKb,
                    new GridBagConstraints(0, 4, 1, 1, 0.1, 0.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(2, 2, 2, 2), 0, 0));



    paWorkingOn.add(imageviewer,
                    new GridBagConstraints(4, 0, 4, 6, 1.0, 1.0,
                                           GridBagConstraints.CENTER,
                                           GridBagConstraints.BOTH,
                                           new Insets(2, 2, 2, 2), 0, 0));



  }

  protected void eventActionSpecial(ActionEvent e)
      throws Throwable {
    if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_DATEI)) {
      JFileChooser fc = new JFileChooser();

      if(sLetzteDatei!=null){
        fc.setCurrentDirectory(sLetzteDatei);
      }
      fc.setFileFilter(new FileFilter()
      {
        public boolean accept(File f) {
          return f.getName().toLowerCase().endsWith(bildExtension) || f.isDirectory();
        }


        public String getDescription() {
          return "Bilder";
        }
      });

      int returnVal = fc.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        sLetzteDatei=file;
        double groesseInKB = ( (double) file.length()) /
            ( (double) 1024);

                 // darstellen
          if (fieldToDisplayFileName != null) {
            fieldToDisplayFileName.setText(file.getName());
          }
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          copyStream(new FileInputStream(file), byteArrayOutputStream);
          imageOutput = byteArrayOutputStream.toByteArray();
          showImageFromFile(file);
          imageviewer.setImage(getImage(0));
          wtfDatei.setText(file.getName());
          wnfGroesse.setDouble(new Double(groesseInKB));
          wtfSeite.setText(""+imageIndex);
      }
      else {
        // keine auswahl
      }
      } else
      if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_TIFFFIRST)) {
        imageIndex = 0;
        imageviewer.setImage(getImage(imageIndex));
        wtfSeite.setText(""+imageIndex);
      }
      else
      if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_TIFFLAST)) {
        imageIndex = image.length - 1;
        imageviewer.setImage(getImage(imageIndex));
        wtfSeite.setText(""+imageIndex);
      }
      else
      if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_TIFFRIGHT)) {
        imageIndex++;
        if (imageIndex >= 0 && imageIndex <= image.length-1){
          imageviewer.setImage(getImage(imageIndex));
           wtfSeite.setText(""+imageIndex);
        }
      }
      else
      if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_TIFFLEFT)) {
        imageIndex--;
        if (imageIndex >= 0 && imageIndex <= image.length){
          imageviewer.setImage(getImage(imageIndex));
            wtfSeite.setText(""+imageIndex);
        }
      }

  }


  private void showImageFromFile(File file)  {
    int numPages = 0;
    try {
      ss = new FileSeekableStream(file);
      decoder = ImageCodec.createImageDecoder("tiff", ss,
                                              null);
      numPages = decoder.getNumPages();

    }
    catch (IOException ex1) {
    }

    image = new RenderedImage[numPages];

    for (int i = 0; i < numPages; i++) {
      try {
        image[i] = decoder.decodeAsRenderedImage(i);
        WritableRaster wr = null;
        wr = image[i].copyData(wr);
      }
      catch (IOException ex2) {
      }
    }

  }


  private void showImage(byte[] array)  {
    int numPages = 0;
    try {
      ss = SeekableStream.wrapInputStream(new ByteArrayInputStream(array),true);
      decoder = ImageCodec.createImageDecoder("tiff", ss,null);
      numPages = decoder.getNumPages();
    }
    catch (IOException ex1) {
    }
    image = new RenderedImage[numPages];
    for (int i = 0; i < numPages; i++) {
      try {
        image[i] = decoder.decodeAsRenderedImage(i);
        WritableRaster wr = null;
        wr = image[i].copyData(wr);

      }
      catch (IOException ex2) {
      }
    }

  }


  public String getBildExtension() {
    return bildExtension;
  }


  public void setBildExtension(String bildExtension) {
    this.bildExtension = bildExtension;
  }


  public String getDateiname() {
      return wtfDatei.getText();
  }
  public void setDateiname(String dateiname) {
    wtfDatei.setText(dateiname);
  }

  public byte[] getImage() {
    return imageOutput;
  }


  public java.awt.image.BufferedImage getImage(int index)
      throws IOException {
    image[index] = decoder.decodeAsRenderedImage(index);
    WritableRaster wr = null;
    wr = image[index].copyData(wr);
    ColorModel colorModel = image[index].getColorModel();
    BufferedImage bi = new BufferedImage(colorModel,
                                         wr,
                                         colorModel.isAlphaPremultiplied(),
                                         null);

    return bi;
  }


  public void setImage(byte[] array)
      throws ExceptionLP {
    BufferedImage bImage = null;
    try {
      if (array != null) {
        showImage(array);
        bImage = getImage(0);
        imageviewer.setImage(bImage);
      }
      else {
        image = null;
        imageviewer.setImage(bImage);
      }
    }
    catch (IOException ex) {
    }
  }


  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return wbuDatei;
  }

  private static void copyStream(InputStream src, OutputStream dest)
      throws Exception {
    byte[] buffer = new byte[16384];
    int len;
    while ( (len = src.read(buffer)) > 0) {
      dest.write(buffer, 0, len);
    }
    src.close();
    dest.close();
  }

}
