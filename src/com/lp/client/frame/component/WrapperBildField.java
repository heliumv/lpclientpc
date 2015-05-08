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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

/**
 * <p> Diese Klasse kuemmert sich ...</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Vorname Nachname; dd.mm.06</p>
 *
 * <p>@author $Author: christian $</p>
 *
 * @version not attributable Date $Date: 2011/03/07 15:04:46 $
 *
 * @todo scrollpane auf bild  PJ 3416
 * @todo texte uebersetzen  PJ 3416
 */
public class WrapperBildField extends PanelBasis implements IControl {

	private static final long serialVersionUID = 1L;

	private final static String ACTION_SPECIAL_DATEI = "action_special_datei";
	private final static String ACTION_SPECIAL_ANZEIGEN = "action_special_anzeigen";
	private final static String ACTION_SPECIAL_SPEICHERN = "action_special_speichern";
	private static final String ACTION_SPECIAL_LEEREN = "action_special_datefield_leeren";
	protected WrapperNumberField wnfGroesse = new WrapperNumberField();
	protected WrapperTextField wtfGroesse = new WrapperTextField();
	private WrapperTextField wtfDatei = new WrapperTextField();
	private JPanel jpaWorkingOn = new JPanel();
	// private PanelImage jpaBild = null;
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperButton wbuDatei = new WrapperButton();
	private WrapperButton wbuAnzeigen = new WrapperButton();
	private WrapperButton wbuSpeichern = new WrapperButton();
	private WrapperLabel wlaGroesse = new WrapperLabel();
	private WrapperLabel wlaKb = new WrapperLabel();
	private File temp;

	private ImageIcon imageIconLeeren = null;

	private ImageIcon getImageIconLeeren() {
		if (imageIconLeeren == null) {
			imageIconLeeren = new ImageIcon(getClass().getResource(
					"/com/lp/client/res/leeren.png"));
		}
		return imageIconLeeren;
	}

	private WrapperTextField fieldToDisplayFileName = null;

	private File sLetzteDatei = null;

	private ImageViewer imageviewer = new ImageViewer(null);

	private String bildExtension = new String(".jpg");
	private boolean bWithoutButtons = false;

	public WrapperButton getButtonDatei() {
		return wbuDatei;
	}

	private JButton jbuSetNull = null;

	private boolean isActivatable = true;

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		jbuSetNull.setEnabled(enabled);
		wbuDatei.setEnabled(enabled);
		wbuAnzeigen.setEnabled(!enabled && getImage() != null);
		wbuSpeichern.setEnabled(!enabled && getImage() != null);
	}

	public WrapperBildField(InternalFrame internalFrame, String addTitel)
			throws Throwable {
		super(internalFrame, addTitel);
		jbInit();
		initComponents();
	}

	public WrapperButton getDateiButton() {
		return wbuDatei;
	}

	public WrapperBildField(InternalFrame internalFrame, String addTitel,
			WrapperTextField fieldToDisplayFileName) throws Throwable {
		super(internalFrame, addTitel);
		this.fieldToDisplayFileName = fieldToDisplayFileName;
		jbInit();
		initComponents();
	}

	public WrapperBildField(InternalFrame internalFrame, String addTitel,
			WrapperTextField fieldToDisplayFileName, boolean bWithoutButtons)
			throws Throwable {
		super(internalFrame, addTitel);
		this.fieldToDisplayFileName = fieldToDisplayFileName;
		this.bWithoutButtons = bWithoutButtons;
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());
		wbuDatei.setText(LPMain.getTextRespectUISPr("lp.datei"));
		wbuAnzeigen.setText(LPMain.getTextRespectUISPr("lp.anzeigen"));
		wbuSpeichern.setText(LPMain.getTextRespectUISPr("lp.speichern"));
		wlaGroesse.setText(LPMain.getTextRespectUISPr("lp.groesse"));
		wlaKb.setText("kB");
		jpaWorkingOn.setLayout(gridBagLayout2);
		wbuDatei.setMinimumSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wbuDatei.setPreferredSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));

		wtfGroesse.setMinimumSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wtfGroesse.setPreferredSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wnfGroesse.setMinimumSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wnfGroesse.setPreferredSize(new Dimension(80, Defaults.getInstance()
				.getControlHeight()));
		wnfGroesse.setActivatable(false);
		wtfGroesse.setActivatable(false);
		wbuDatei.setActionCommand(ACTION_SPECIAL_DATEI);
		wbuDatei.addActionListener(this);
		wbuAnzeigen.setActionCommand(ACTION_SPECIAL_ANZEIGEN);
		wbuAnzeigen.addActionListener(this);
		wbuSpeichern.setActionCommand(ACTION_SPECIAL_SPEICHERN);
		wbuSpeichern.addActionListener(this);
		wtfDatei.setColumnsMax(MediaFac.MAX_MEDIASTANDARD_DATEINAME);
		wtfDatei.setActivatable(false);

		jbuSetNull = new JButton();
		jbuSetNull.setActionCommand(ACTION_SPECIAL_LEEREN);
		jbuSetNull.addActionListener(this);
		jbuSetNull.setIcon(getImageIconLeeren());
		jbuSetNull
				.setMinimumSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));
		jbuSetNull
				.setPreferredSize(new Dimension(Defaults.getInstance()
						.getControlHeight(), Defaults.getInstance()
						.getControlHeight()));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		int iZeile = 0;
		jpaWorkingOn.add(imageviewer, 	new GridBagConstraints(3, iZeile, 1, 7, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		if (!bWithoutButtons) {
			jpaWorkingOn.add(wbuDatei, 		new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 22), 0, 0));
			jpaWorkingOn.add(jbuSetNull, 	new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wtfDatei, 		new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wlaGroesse,	new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 40, 0));
			jpaWorkingOn.add(wtfGroesse, 	new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wlaKb, 		new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wnfGroesse, 	new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wbuAnzeigen, 	new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
			jpaWorkingOn.add(wbuSpeichern, 	new GridBagConstraints(0, iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		}

	}

	protected void toTempFile() throws IOException {
		if(getImage() == null) {
			temp = null;
			return;
		}
		BufferedImage image = getImage();
		String name = getDateiname();
		if(name == null)
			name = "tempImageHV.gif";
		String mime = Helper.getMime(name);
		temp = File.createTempFile(
				Helper.getName(name), mime.isEmpty() ? null : mime);
		ImageIO.write(image, Helper.getMime(name).replace(".", ""), temp);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getSource().equals(jbuSetNull)) {
			setImage(null);
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_ANZEIGEN)) {
			toTempFile();
			HelperClient.desktopTryToOpenElseSave(temp, getInternalFrame());
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_SPEICHERN)) {
			toTempFile();
			HelperClient.showSaveFileDialog(temp, getDateiname() == null ? null : new File(getDateiname()), getInternalFrame(), Helper.getMime(getDateiname()));
		} else if (e.getActionCommand().equalsIgnoreCase(ACTION_SPECIAL_DATEI)) {
			JFileChooser fc = new JFileChooser();

			if (sLetzteDatei != null) {
				fc.setCurrentDirectory(sLetzteDatei);
			}

//			fc.setFileFilter(new FileFilter() {
//				public boolean accept(File f) {
//					return f.getName().toLowerCase().endsWith(bildExtension)
//							|| f.isDirectory();
//				}
//
//				public String getDescription() {
//					return "Bilder";
//				}
//			});

			FileFilter imageFilter = new FileNameExtensionFilter(
				    "Bilder", "gif", "jpeg", "jpg", "png");

			fc.setFileFilter(imageFilter);

			int returnVal = fc.showOpenDialog(getInternalFrame());

			File file = fc.getSelectedFile();

			boolean fileExist = true;
			if (file != null) {
			fileExist = file.exists();
			if (!fileExist)
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.warning"),
						LPMain.getMessageTextRespectUISPr("lp.warning.dateinichtvorhanden", file.getName()));
			}

			if (returnVal == JFileChooser.APPROVE_OPTION && fileExist) {

				sLetzteDatei = file;
				ParametermandantDto parameter = DelegateFactory
						.getInstance()
						.getParameterDelegate()
						.getMandantparameter(
								LPMain.getTheClient()
										.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_ALLGEMEIN_DOKUMENTE_MAXIMALE_GROESSE);
				double groesseInKB = ((double) file.length()) / ((double) 1024);

				if (groesseInKB > (Integer) parameter.getCWertAsObject()) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr(
									"lp.error.dateizugross"));
				} else {
					// darstellen
					imageviewer.setImage(Helper.imageToByteArray(ImageIO
							.read(file)));
					if (getImage() == null) {
						// Die Ausgewaehlte Datei ist kein Bild
						imageviewer.setImage((byte[]) null);
						DialogFactory.showModalDialog(
								LPMain.getTextRespectUISPr(
										"lp.error"),
								LPMain.getTextRespectUISPr(
										"lp.error.keinbildgewaehlt"));
					} else {
						if (fieldToDisplayFileName != null) {
							fieldToDisplayFileName.setText(file.getName());

						}
						wtfDatei.setText(file.getName());
						wbuDatei.setToolTipText(file.getAbsolutePath());

						wnfGroesse.setDouble(new Double(groesseInKB));

						wtfGroesse.setText(getImage().getWidth(this) + "x"
								+ getImage().getHeight(this));
					}
				}
			} else {
				// keine auswahl
				imageviewer.setImage((byte[]) null);
			}
		}
	}

	public String getBildExtension() {
		return bildExtension;
	}

	public String getDateiname() {
		return wtfDatei.getText();
	}

	public void setDateiname(String dateiname) {
		wtfDatei.setText(dateiname);
	}

	public void setBildExtension(String bildExtension) {
		this.bildExtension = bildExtension;
	}

	public java.awt.image.BufferedImage getImage() {
		return imageviewer.originalImage;
	}

	protected String getLockMeWer() {
		return null;
	}

	public void setImage(byte[] image) throws ExceptionLP {
		temp = null;
		imageviewer.setImage(image);
		if (image != null && getImage() != null) {
			wnfGroesse.setDouble(new Double(image.length / 1024));
			wtfGroesse.setText(getImage().getWidth(this) + "x"
					+ getImage().getHeight(this));
		} else {
			wnfGroesse.setDouble(new Double(0));
			wtfDatei.removeContent();
		}
	}

	public void cleanup(){
		imageviewer=null;
		setToolBar(null);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuDatei;
	}

	@Override
	public boolean isMandatoryField() {
		return wtfDatei.isMandatoryField();
	}

	@Override
	public void setMandatoryField(boolean isMandatoryField) {
		wtfDatei.setMandatoryField(isMandatoryField);
	}

	@Override
	public boolean isActivatable() {
		return isActivatable;
	}

	@Override
	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
		if(!isActivatable) {
			setEnabled(false) ;
		}
	}

	@Override
	public void removeContent() throws Throwable {
		setImage(null);
		setDateiname(null);
	}

	@Override
	public boolean hasContent() throws Throwable {
		return wtfDatei.hasContent();
	}
}
