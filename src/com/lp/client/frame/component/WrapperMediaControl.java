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
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperPrint;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.ReportViewer;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich ...</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Vorname Nachname; dd.mm.05</p>
 * 
 * <p>@author $Author: heidi $</p>
 * 
 * @version not attributable Date $Date: 2009/04/24 07:55:59 $
 */
public class WrapperMediaControl extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String ACTION_SPECIAL_BILD_ENTFERNEN = "ACTION_SPECIAL_BILD_ENTFERNEN";
	private String jpgExtension = new String(".jpg");
	private String pngExtension = new String(".png");
	private String gifExtension = new String(".gif");
	private String tiffExtension = new String(".tiff");

	private JPanel jPanelArt = new JPanel();
	private JPanel paWorkOn = new JPanel();

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperLabel wlaArt = new WrapperLabel();

	private WrapperEditorField wef;
	private WrapperBildField wbf;
	private WrapperTiffViewer wtv;
	private WrapperSonstige ws;
	private WrapperPdfField wpf;
	private ReportViewer rv;
	private int iSpaltenbreite1 = 80;
	private boolean bMitDefaultbildFeld = false;
	protected boolean bWithoutButtons = false;

	private WrapperCheckBox wcbDefaultbild = new WrapperCheckBox();
	private WrapperComboBox wcoArt = new WrapperComboBox();
	private WrapperButton wbuBildEntfernen = new WrapperButton();
	private WrapperTextField wtfTextFieldFuerDateiname = null;

	public WrapperMediaControl(InternalFrame internalFrame, String addTitel)
			throws Throwable {
		this(internalFrame, addTitel, false);
	}

	public WrapperMediaControl(InternalFrame internalFrame, String addTitel,
			boolean bMitDefaultbildFeld,
			WrapperTextField wtfTextFieldFuerDateiname) throws Throwable {
		this(internalFrame, addTitel, bMitDefaultbildFeld);
		this.wtfTextFieldFuerDateiname = wtfTextFieldFuerDateiname;
		jbInit();
	}

	public WrapperMediaControl(InternalFrame internalFrame, String addTitel,
			boolean bMitDefaultbildFeld) throws Throwable {
		this(internalFrame, addTitel, bMitDefaultbildFeld, false);
	}

	public WrapperMediaControl(InternalFrame internalFrame, String addTitel,
			boolean bMitDefaultbildFeld, boolean bWithoutButtons)
			throws Throwable {
		super(internalFrame, addTitel);
		this.bMitDefaultbildFeld = bMitDefaultbildFeld;
		this.bWithoutButtons = bWithoutButtons;
		jbInit();
		initComponents();
	}

	public void cleanup() {
		wef.lpEditor.cleanup();
		wef.lpEditor = null;
		wef.jspScrollPane.setViewport(null);
		wef.setToolBar(null);

		wbf.cleanup();
		paWorkOn.remove(wbf);
		wbf = null;
		wtv.cleanup();
		paWorkOn.remove(wtv);
		wtv = null;

		wpf.setToolBar(null);
		ws.setToolBar(null);
		paWorkOn.remove(rv);
		rv.cleanup();
		rv = null;

	}

	protected String getLockMeWer() throws Exception {
		return null;
	}

	private void jbInit() throws Throwable {
		this.setLayout(new GridBagLayout());

		jPanelArt.setLayout(gridBagLayout1);
		paWorkOn.setLayout(gridBagLayout1);

		wef = createEditorField();
		wbf = new WrapperBildField(getInternalFrame(), "",
				wtfTextFieldFuerDateiname, bWithoutButtons);
		wtv = new WrapperTiffViewer(getInternalFrame(), "",
				wtfTextFieldFuerDateiname);
		ws = new WrapperSonstige(getInternalFrame(), "",
				wtfTextFieldFuerDateiname);
		wpf = new WrapperPdfField(getInternalFrame(), "",
				wtfTextFieldFuerDateiname);
		rv = new ReportViewer(null);
		wbf.setVisible(false);
		wtv.setVisible(false);
		wef.setVisible(false);
		ws.setVisible(false);
		wpf.setVisible(false);
		rv.setVisible(false);
		TreeMap<String, String> tmArten = new TreeMap<String, String>();
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML,
				MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG,
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG,
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF,
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF,
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF,
				MediaFac.DATENFORMAT_MIMETYPE_APP_PDF);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT,
				MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT);
		tmArten.put(MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER,
				MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER);
		wcoArt.setMap(tmArten);
		if (!bWithoutButtons) {
			if (jPanelArt == null) {
				jPanelArt = new JPanel();
				GridBagLayout gridBagLayout = new GridBagLayout();
				jPanelArt.setLayout(gridBagLayout);
				jPanelArt
						.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			}
			wlaArt = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
					"label.art"));
			wlaArt.setMaximumSize(new Dimension(iSpaltenbreite1, Defaults
					.getInstance().getControlHeight()));
			wlaArt.setMinimumSize(new Dimension(iSpaltenbreite1, Defaults
					.getInstance().getControlHeight()));
			wlaArt.setPreferredSize(new Dimension(iSpaltenbreite1, Defaults
					.getInstance().getControlHeight()));
			// wcoArt.setMandatoryField(true);

			wcbDefaultbild.setText(LPMain.getInstance().getTextRespectUISPr(
					"lp.defaultbild"));

			wbuBildEntfernen = new WrapperButton();
			wbuBildEntfernen.setIcon(new ImageIcon(getClass().getResource(
					"/com/lp/client/res/leeren.png")));
			wbuBildEntfernen.setActionCommand(ACTION_SPECIAL_BILD_ENTFERNEN);
			wbuBildEntfernen.addActionListener(this);

			wcoArt.addActionListener(new WrapperMediaControl_jComboBoxPositionsart_actionAdapter(
					this));
			jPanelArt.add(wlaArt, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.NONE,
					new Insets(2, 2, 2, 2), 50, 0));
			jPanelArt.add(wcoArt, new GridBagConstraints(1, 0, 1, 1, 1, 0,
					GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
					new Insets(2, 2, 2, 2), 0, 0));
			jPanelArt.add(wbuBildEntfernen, new GridBagConstraints(2, 0, 0, 1,
					0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE,
					new Insets(2, 2, 2, 2), 50, 0));

			if (bMitDefaultbildFeld == true) {
				jPanelArt.add(wcbDefaultbild, new GridBagConstraints(5, 0, 0,
						1, 0, 0, GridBagConstraints.WEST,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						150, 0));

				/*
				 * jPanelArt.add(wcbDefaultbild, new GridBagConstraints(2, 0, 1,
				 * 1, 0, 0.0 , GridBagConstraints.WEST, GridBagConstraints.NONE,
				 * new Insets(2, 2, 2, 2), 100, 0));
				 */
			}
		}
		paWorkOn.add(wbf, new GridBagConstraints(0, 0, 1, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		paWorkOn.add(wtv, new GridBagConstraints(0, 0, 1, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		paWorkOn.add(wef, new GridBagConstraints(0, 0, 1, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		paWorkOn.add(ws, new GridBagConstraints(0, 0, 1, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		paWorkOn.add(wpf, new GridBagConstraints(0, 0, 1, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		paWorkOn.add(rv, new GridBagConstraints(0, 0, 1, 2, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		this.add(jPanelArt, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		this.add(paWorkOn, new GridBagConstraints(0, 2, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
	}

	/**
	 * Override um anderen Editor zu Instanzieren.
	 * 
	 * @throws Throwable
	 */
	protected WrapperEditorField createEditorField() throws Throwable {
		return new WrapperEditorField(getInternalFrame(), "", bWithoutButtons);
	}

	public void setDefaultbildFeld(Short s) {
		wcbDefaultbild.setShort(s);
	}

	public Short getDefaultbildFeld() {
		return wcbDefaultbild.getShort();
	}

	public void setOMedia(byte[] media) throws Throwable {

		wcoArt.setKeyOfSelectedItem(getMimeType());
		if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			if (media != null) {
				wef.setText(new String(media));
			} else {
				wef.setText("");
			}
		} else if (getMimeType().equals(
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
			if (media != null) {
				wtv.setImage(media);
			}
		} else if (getMimeType()
				.equals(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT)) {
			if (media != null) {
				ws.setDatei(media);
			}
		} else if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
			if (media != null) {
				wpf.setPdf(media);
			}
		} else if (getMimeType().equals(
				MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER)) {
			if (media != null) {
				ByteArrayInputStream bStream = new ByteArrayInputStream(media);
				ObjectInputStream oStream = new ObjectInputStream(bStream);
				JasperPrint jPrint = (JasperPrint) oStream.readObject();
				rv.loadReport(jPrint);
				rv.refreshPage();
			}
		} else {
			try {
				BufferedImage image = Helper.byteArrayToImage(media);
				wbf.setImage(media);

				if (media != null && image != null) {
					wbf.wnfGroesse.setDouble(new Double(((double) media.length)
							/ ((double) 1024)));
					wbf.wtfGroesse.setText(image.getWidth(this) + "x"
							+ image.getHeight(this));
				} else {
					wbf.wnfGroesse.setDouble(new Double(0));
					wbf.wtfGroesse.setText("");
					wbf.setImage(null);
				}
			} catch (Exception ex) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.system.image.format"), "");
			}
		}
	}

	public void setOMediaText(String media) throws Throwable {

		wcoArt.setKeyOfSelectedItem(getMimeType());
		if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			if (media != null) {
				wef.setText(media);
			} else {
				wef.setText("");
			}
		}
	}

	public void setOMediaImage(byte[] media) throws Throwable {

		wcoArt.setKeyOfSelectedItem(getMimeType());
		if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
			wtv.setImage(media);
		} else if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
			wpf.setPdf(media);
		} else if (getMimeType()
				.equals(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT)) {
			ws.setDatei(media);
		} else {
			try {
				BufferedImage image = Helper.byteArrayToImage(media);
				wbf.setImage(media);

				if (media != null && image != null) {
					wbf.wnfGroesse.setDouble(new Double(((double) media.length)
							/ ((double) 1024)));
					wbf.wtfGroesse.setText(image.getWidth(this) + "x"
							+ image.getHeight(this));
				} else {
					wbf.wnfGroesse.setDouble(new Double(0));
					wbf.wtfGroesse.setText("");
					wbf.setImage(null);
				}
			} catch (Exception ex) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.system.image.format"), "");

			}

		}

	}

	/**
	 * 
	 * @deprecated AD gibt Probleme mit dem encoding bei Text use setOMediaText
	 *             oder setOMediaimage
	 * @throws Throwable
	 * @return byte[]
	 */
	public byte[] getOMedia() throws Throwable {
		if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
				|| getMimeType()
						.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
				|| getMimeType()
						.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {
			Image im = wbf.getImage();
			if (im != null) {
				return Helper.imageToByteArray((BufferedImage) im);
			} else {
				return null;
			}
		} else if (getMimeType()
				.equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			return wef.getText().getBytes();
		} else if (getMimeType()
				.equals(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT)) {
			return ws.getDatei();
		} else if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
			return wpf.getPdf();
		} else {
			// tiff to byte[]
			return wtv.getImage();
		}
	}

	public String getOMediaText() throws Throwable {
		if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			return wef.getText();
		} else {
			return null;
		}

	}

	public byte[] getOMediaImage() throws Throwable {
		if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)
				|| getMimeType()
						.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)
				|| getMimeType()
						.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {
			Image im = wbf.getImage();
			if (im != null) {
				return Helper.imageToByteArray((BufferedImage) im);
			} else {
				return null;
			}
		} else if (getMimeType().equals(
				MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
			return wtv.getImage();
		} else if (getMimeType()
				.equals(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT)) {
			// tiff to byte[]
			return ws.getDatei();
		} else if (getMimeType().equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
			return wpf.getPdf();
		}

		else {
			return null;
		}
	}

	public String getMimeType() {
		return wcoArt.getSelectedItem().toString();
	}

	public void setMimeType(String mimetype) {
		wcoArt.setKeyOfSelectedItem(mimetype);
		jComboBoxPositionsart_actionPerformed(null);
	}

	public void setDateiname(String dateiname) {
		wbf.getDateiButton().setToolTipText(dateiname);
		wbf.setDateiname(dateiname);
		wtv.setDateiname(dateiname);
		wpf.setDateiname(dateiname);
		ws.setDateiname(dateiname);
	}

	public String getDateiname() {
		String dateiName = "";
		Object currentArt = wcoArt.getKeyOfSelectedItem();
		if (currentArt != null) {
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
				dateiName = wtv.getDateiname();
			} else if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
				dateiName = wpf.getDateiname();
			} else if (currentArt
					.equals(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT)) {
				dateiName = ws.getDateiname();
			} else {
				dateiName = wbf.getDateiname();
			}
		}
		return dateiName;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_BILD_ENTFERNEN)) {
			wbf.setImage(null);
			wtv.setImage(null);
		}
	}

	/**
	 * Auswahl der Positionsart ist erfolgt.
	 * 
	 * @param e
	 *            ActionEvent
	 */
	void jComboBoxPositionsart_actionPerformed(ActionEvent e) {

		Object currentArt = wcoArt.getKeyOfSelectedItem();
		if (currentArt != null) {
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
				wbf.setVisible(false);
				wef.setVisible(true);
				wtv.setVisible(false);
				wcbDefaultbild.setVisible(false);
				ws.setVisible(false);
				wpf.setVisible(false);
				rv.setVisible(false);
			} else if (currentArt
					.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG)) {
				wbf.setVisible(true);
				wtv.setVisible(false);
				wbf.setBildExtension(jpgExtension);
				wef.setVisible(false);
				wcbDefaultbild.setVisible(true);
				ws.setVisible(false);
				wpf.setVisible(false);
				rv.setVisible(false);
			}
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_PNG)) {
				wbf.setVisible(true);
				wtv.setVisible(false);
				wbf.setBildExtension(pngExtension);
				wef.setVisible(false);
				wcbDefaultbild.setVisible(true);
				ws.setVisible(false);
				wpf.setVisible(false);
				rv.setVisible(false);
			}
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_GIF)) {
				wbf.setVisible(true);
				wtv.setVisible(false);
				wbf.setBildExtension(gifExtension);
				wef.setVisible(false);
				wcbDefaultbild.setVisible(true);
				ws.setVisible(false);
				wpf.setVisible(false);
				rv.setVisible(false);
			}
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_TIFF)) {
				wtv.setVisible(true);
				wbf.setVisible(false);
				wbf.setBildExtension(tiffExtension);
				wef.setVisible(false);
				wcbDefaultbild.setVisible(true);
				ws.setVisible(false);
				wpf.setVisible(false);
				rv.setVisible(false);
			}
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_UNBEKANNT)) {
				ws.setVisible(true);
				wtv.setVisible(false);
				wbf.setVisible(false);
				wef.setVisible(false);
				wcbDefaultbild.setVisible(true);
				wpf.setVisible(false);
				rv.setVisible(false);
			}
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_PDF)) {
				wpf.setVisible(true);
				wtv.setVisible(false);
				wbf.setVisible(false);
				wef.setVisible(false);
				wcbDefaultbild.setVisible(false);
				ws.setVisible(false);
				rv.setVisible(false);
			}
			if (currentArt.equals(MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER)) {
				wpf.setVisible(false);
				wtv.setVisible(false);
				wbf.setVisible(false);
				wef.setVisible(false);
				wcbDefaultbild.setVisible(false);
				ws.setVisible(false);
				rv.setVisible(true);
			}
		} else {
			wpf.setVisible(false);
			wtv.setVisible(false);
			wbf.setVisible(false);
			wef.setVisible(false);
			wcbDefaultbild.setVisible(false);
			ws.setVisible(false);
			if (rv != null) {
				rv.setVisible(false);
			}
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcoArt;
	}

	public WrapperComboBox getWcoArt() {
		return wcoArt;
	}

}

class WrapperMediaControl_jComboBoxPositionsart_actionAdapter implements
		java.awt.event.ActionListener {
	WrapperMediaControl adaptee;

	WrapperMediaControl_jComboBoxPositionsart_actionAdapter(
			WrapperMediaControl adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.jComboBoxPositionsart_actionPerformed(e);
	}
}
