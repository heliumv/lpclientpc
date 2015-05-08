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
package com.lp.client.artikel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.ItemChangedListener;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.system.service.KeyvalueDto;
import com.lp.server.system.service.SystemServicesFac;

@SuppressWarnings("static-access")
public class DialogArtikelExport extends JDialog implements ActionListener,
		ItemChangedListener {

	private static final long serialVersionUID = 1L;

	private Integer artikelIId_Von = null;
	private Integer artikelIId_Bis = null;
	private PanelQueryFLR panelQueryFLRArtikel_Von = null;
	private PanelQueryFLR panelQueryFLRArtikel_Bis = null;

	private WrapperSelectField wsfArtikelgruppe = null;
	private WrapperSelectField wsfArtikelklasse = null;
	private WrapperCheckBox wcbVersteckte = null;

	private WrapperTextField wtfArtikelnrVon = new WrapperTextField();
	private WrapperTextField wtfArtikelnrBis = new WrapperTextField();
	static final public String ACTION_SPECIAL_ARTIKELVON_FROM_LISTE = "ACTION_SPECIAL_ARTIKELVON_FROM_LISTE";
	static final public String ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE = "ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE";
	private WrapperButton wbuArtikelnrVon = new WrapperButton();
	private WrapperButton wbuArtikelnrBis = new WrapperButton();

	private JButton btnOK = new WrapperButton();
	private JButton btnAbbrechen = new WrapperButton();

	InternalFrameArtikel internalFrame = null;

	public DialogArtikelExport(InternalFrameArtikel internalFrame)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("artikel.preispflege.export"), true);
		this.internalFrame = internalFrame;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		// Gespeicherte Werte setzen
		setKeyValueDtos(DelegateFactory
				.getInstance()
				.getSystemDelegate()
				.keyvalueFindyByCGruppe(
						SystemServicesFac.KEYVALUE_ARTIKEL_KOPIEREN));
//		this.setSize(Defaults.getInstance().bySizeFactor(500), Defaults.getInstance().bySizeFactor(200));

	}

	public void actionPerformed(ActionEvent e) {

		try {
			if (e.getActionCommand().equals(
					ACTION_SPECIAL_ARTIKELVON_FROM_LISTE)) {
				dialogQueryArtikelFromListe_Von(e);
			} else if (e.getActionCommand().equals(
					ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE)) {
				dialogQueryArtikelFromListe_Bis(e);
			}
		} catch (Throwable e1) {
			internalFrame.getTabbedPaneArtikel().handleException(e1, true);
		}
		if (e.getSource().equals(btnAbbrechen)) {
			this.setVisible(false);
		}
		if (e.getSource().equals(btnOK)) {

			// Artikel ersetzen

			try {

				this.setVisible(false);
				
				FileFilter fileFilterXLS = new XLSMyFilter();
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(fileFilterXLS);

				fileChooser.setLocale(this.getLocale());
				fileChooser.updateUI();
				fileChooser.setSelectedFile(new File("vkpreise.xls"));
				fileChooser.setFileFilter(fileFilterXLS);
				int retValue = fileChooser.showSaveDialog(this);

				if (retValue == JFileChooser.APPROVE_OPTION) {

					byte[] datenXlsFile = DelegateFactory
							.getInstance()
							.getArtikelDelegate()
							.getXLSForPreispflege(wsfArtikelgruppe.getIKey(),
									wsfArtikelklasse.getIKey(),
									wtfArtikelnrVon.getText(),
									wtfArtikelnrBis.getText(),
									wcbVersteckte.isSelected());

					String path = fileChooser.getSelectedFile().getPath();

					if (!path.toLowerCase().endsWith(".xls"))
						path = path + ".xls";

					File file = new File(path);

					FileOutputStream fo = new FileOutputStream(file);
					fo.write(datenXlsFile);
					fo.close();

					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.hinweis"),
									LPMain.getTextRespectUISPr("lp.hint.dateiwurdegespeichert")
											+ " ("
											+ file.getAbsolutePath()
											+ ") ");

				}
			} catch (FileNotFoundException e2) {

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hinweis"),
						e2.getMessage());
				return;
			} catch (Throwable e1) {

				internalFrame.getTabbedPaneArtikel().handleException(e1, true);
			}
			this.setVisible(false);
		}

	}

	private void setKeyValueDtos(KeyvalueDto[] dtos) throws Throwable {

		for (int z = 0; z < dtos.length; z++) {

			for (int i = 0; i < this.getContentPane().getComponents().length; ++i) {

				{

					if (this.getContentPane().getComponents()[i].getName() != null
							&& this.getContentPane().getComponents()[i]
									.getName().equals(dtos[z].getCKey())) {
						if (this.getContentPane().getComponents()[i] instanceof WrapperCheckBox) {

							WrapperCheckBox wcb = (WrapperCheckBox) this
									.getContentPane().getComponents()[i];
							wcb.setShort(new Short(dtos[z].getCValue()));

						}
					}
				}
			}
		}

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	void dialogQueryArtikelFromListe_Von(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Von = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(internalFrame, artikelIId_Von, true);

		new DialogQuery(panelQueryFLRArtikel_Von);
	}

	void dialogQueryArtikelFromListe_Bis(ActionEvent e) throws Throwable {
		panelQueryFLRArtikel_Bis = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikel(internalFrame, artikelIId_Bis, true);

		new DialogQuery(panelQueryFLRArtikel_Bis);
	}

	private void jbInit() throws Throwable {

		btnOK.setText(LPMain.getInstance().getTextRespectUISPr("button.ok"));
		btnAbbrechen.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.abbrechen"));
		btnOK.addActionListener(this);
		btnAbbrechen.addActionListener(this);

		wbuArtikelnrVon.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer")
				+ " "
				+ LPMain.getTextRespectUISPr("lp.von"));
		wbuArtikelnrBis.setText(LPMain
				.getTextRespectUISPr("artikel.artikelnummer")
				+ " "
				+ LPMain.getTextRespectUISPr("lp.bis"));
		wbuArtikelnrVon.setActionCommand(ACTION_SPECIAL_ARTIKELVON_FROM_LISTE);
		wbuArtikelnrVon.addActionListener(this);

		wbuArtikelnrBis.setActionCommand(ACTION_SPECIAL_ARTIKELBIS_FROM_LISTE);
		wbuArtikelnrBis.addActionListener(this);
		wcbVersteckte = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("lp.versteckte"));
		wsfArtikelgruppe = new WrapperSelectField(
				WrapperSelectField.ARTIKELGRUPPE, internalFrame, true);
		wsfArtikelklasse = new WrapperSelectField(
				WrapperSelectField.ARTIKELKLASSE, internalFrame, true);

		internalFrame.addItemChangedListener(this);
		this.getContentPane().setLayout(new MigLayout("wrap 2", "[fill,33%|fill,67%]"));

		this.getContentPane().add(wbuArtikelnrVon);
		this.getContentPane().add(wtfArtikelnrVon);

		this.getContentPane().add(wbuArtikelnrBis);
		this.getContentPane().add(wtfArtikelnrBis);

		this.getContentPane().add(wsfArtikelgruppe.getWrapperButton());
		this.getContentPane().add(wsfArtikelgruppe.getWrapperTextField());

		this.getContentPane().add(wsfArtikelklasse.getWrapperButton());
		this.getContentPane().add(wsfArtikelklasse.getWrapperTextField());

		this.getContentPane().add(wcbVersteckte, "span");

		this.getContentPane().add(btnOK, "span, split 2, r, w 33%!");
		this.getContentPane().add(btnAbbrechen, "l, w 33%!");

		pack();
		Dimension d = getContentPane().getPreferredSize();
		d.width = Defaults.getInstance().bySizeFactor(400);
		getContentPane().setSize(d);
		pack();
	}

	@Override
	public void changed(EventObject eI) {
		// TODO Auto-generated method stub
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			try {
				if (e.getSource() == panelQueryFLRArtikel_Von) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey((Integer) key);
					artikelIId_Von = artikelDto.getIId();
					wtfArtikelnrVon.setText(artikelDto.getCNr());
				} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
					Object key = ((ISourceEvent) e.getSource()).getIdSelected();
					ArtikelDto artikelDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artikelFindByPrimaryKey((Integer) key);
					artikelIId_Bis = artikelDto.getIId();
					wtfArtikelnrBis.setText(artikelDto.getCNr());
				}
			} catch (Throwable e1) {
				internalFrame.getTabbedPaneArtikel().handleException(e1, true);
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikel_Von) {
				wtfArtikelnrVon.setText(null);
				artikelIId_Von = null;
			} else if (e.getSource() == panelQueryFLRArtikel_Bis) {
				wtfArtikelnrBis.setText(null);
				artikelIId_Bis = null;
			}
		}
	}

}

class XLSMyFilter extends javax.swing.filechooser.FileFilter {
	public boolean accept(File file) {

		if (file.isDirectory() == true) {
			return true;
		} else {
			String filename = file.getName();
			return filename.endsWith(".xls");
		}

	}

	public String getDescription() {
		return "*.xls";
	}
}
