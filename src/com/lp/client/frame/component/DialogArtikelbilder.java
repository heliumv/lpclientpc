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
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelkommentarDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediaFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class DialogArtikelbilder extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageViewer wbfBild = null;
	JButton buttonLeft = null;
	JButton buttonRight = null;
	WrapperLabel wlaAnzahl = new WrapperLabel();

	private GridBagLayout gridBagLayout = new GridBagLayout();
	private ArrayList<BufferedImage> bilder = new ArrayList<BufferedImage>();
	int iAktuellesBild = 1;

	private static String LINKS = "LINKS";
	private static String RECHTS = "RECHTS";

	public DialogArtikelbilder() {
		// nothing here
	}

	public static void addEscapeListener(final JDialog dialog) {
	    ActionListener escListener = new ActionListener() {

	        @Override
	        public void actionPerformed(ActionEvent e) {
	            dialog.setVisible(false);
	        }
	    };

	    dialog.getRootPane().registerKeyboardAction(escListener,
	            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
	            JComponent.WHEN_IN_FOCUSED_WINDOW);

	}
	
	public DialogArtikelbilder(Dimension size, Integer artikelIId)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("fert.artikelbild"), true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		holeBilder(artikelIId);
		jbInit();
		pack();
		this.setSize(size);
		addEscapeListener(this);
		if (bilder.size() == 0) {
			// Es sind keine Bilder Vorhanden
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.error"), LPMain.getInstance()
					.getTextRespectUISPr("fert.artikelbild.error"));
			this.setVisible(false);
			this.dispose();
		} else {
			this.setVisible(true);
		}
	
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void holeBilder(Integer artikelIId) throws Throwable {
		ArrayList<byte[]> bilderAlsByteArray = DelegateFactory
				.getInstance()
				.getArtikelkommentarDelegate()
				.getArtikelbilderFindByArtikelIIdBelegartCNr(artikelIId,

				LocaleFac.BELEGART_LOS,
						LPMain.getInstance().getTheClient().getLocUiAsString());

		for (int i = 0; i < bilderAlsByteArray.size(); i++) {
			bilder.add(Helper.byteArrayToImage(bilderAlsByteArray.get(i)));
		}

	}

	private void jbInit() throws Throwable {

		ImageIcon links = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/navigate_left.png"));
		buttonLeft = new JButton();
		buttonLeft.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"lp.vorheriges"));
		buttonLeft.setActionCommand(LINKS);
		buttonLeft.setIcon(links);

		ImageIcon rechts = new ImageIcon(getClass().getResource(
				"/com/lp/client/res/navigate_right.png"));
		buttonLeft.addActionListener(new DialogBilder_buttonLeft_actionAdapter(
				this));

		buttonRight = new JButton();
		buttonRight.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"lp.naechstes"));
		buttonRight.setActionCommand(RECHTS);
		buttonRight.setIcon(rechts);
		buttonRight
				.addActionListener(new DialogBilder_buttonRight_actionAdapter(
						this));

		if (bilder != null && bilder.size() > 0) {
			wbfBild = new ImageViewer(Helper.imageToByteArray(bilder.get(0)));
		} else {
			wbfBild = new ImageViewer(null);
		}

		setLabelTextUndZeigeBildAn();

		this.getContentPane().setLayout(gridBagLayout);

		this.getContentPane().add(
				buttonLeft,
				new GridBagConstraints(0, 0, 1, 1, 00, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(
				buttonRight,
				new GridBagConstraints(1, 0, 1, 1, 00, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(
				wlaAnzahl,
				new GridBagConstraints(2, 0, 1, 1, 1.0, 0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane().add(
				wbfBild,
				new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
	}

	public void setLabelTextUndZeigeBildAn() {
		if (bilder.size() > 0) {
			MessageFormat mf = new MessageFormat(LPMain.getInstance()
					.getTextRespectUISPr("fert.artikelbild.anzahl"));

			try {
				mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
			if (bilder != null) {
				Object pattern[] = { iAktuellesBild, bilder.size() };
				wlaAnzahl.setText(mf.format(pattern));
				wbfBild.setImage(Helper.imageToByteArray(bilder
						.get(iAktuellesBild - 1)));
			}
		}
	}

	public void buttonLeft_actionPerformed(ActionEvent e) {
		iAktuellesBild--;
		if (iAktuellesBild < 1) {
			iAktuellesBild = bilder.size();
		}
		setLabelTextUndZeigeBildAn();
	}

	public void buttonRight_actionPerformed(ActionEvent e) {
		iAktuellesBild++;
		if (iAktuellesBild > bilder.size()) {
			iAktuellesBild = 1;
		}
		setLabelTextUndZeigeBildAn();
	}
}

class DialogBilder_buttonLeft_actionAdapter implements ActionListener {
	private DialogArtikelbilder adaptee;

	DialogBilder_buttonLeft_actionAdapter(DialogArtikelbilder adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.buttonLeft_actionPerformed(e);
	}
}

class DialogBilder_buttonRight_actionAdapter implements ActionListener {
	private DialogArtikelbilder adaptee;

	DialogBilder_buttonRight_actionAdapter(DialogArtikelbilder adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.buttonRight_actionPerformed(e);
	}
}
