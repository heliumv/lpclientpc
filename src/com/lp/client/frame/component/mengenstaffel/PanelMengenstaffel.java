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
package com.lp.client.frame.component.mengenstaffel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.partner.InternalFrameKunde;
import com.lp.client.partner.PanelKundesokomengenstaffel;
import com.lp.client.pc.LPMain;

@SuppressWarnings("static-access")
/**
 * <p>In diesem Fenster wird eine Kundesokomengenstaffel erfasst.
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 07.07.2006</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.7 $
 */
public abstract class PanelMengenstaffel extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jPanelArt = null;

	private WrapperLabel wlaArt = null;
	protected WrapperComboBox wcoArt = null;

	private WrapperLabel wlaKundeartikelnummer = null;
	protected WrapperTextField wtfKundeartikelnummer = new WrapperTextField(25);

	protected String ART_ARTIKEL = null;
	protected String ART_ARTIKELGRUPPE = null;
	private Map<String, String> artMap = null;

	protected PanelMengenstaffelArtikel panelArtikel = null;
	protected PanelMengenstaffelArtgru panelArtgru = null;
	protected boolean bMitKundenartikelnummer = false;

	public PanelMengenstaffel(InternalFrame internalFrame, String add2TitleI,
			Object key, boolean bMitKundenartikelnummer) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.bMitKundenartikelnummer = bMitKundenartikelnummer;
		initPanel();
		jbInit();
		initComponents();
	}

	protected InternalFrameKunde getInternalFrameKunde() {
		return (InternalFrameKunde) getInternalFrame();
	}

	void jbInit() throws Throwable {
		// Integer kundeIId = getInternalFrameKunde().getKundeDto().getIId();
		// das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach
		// innen
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(3, 3, 10, 3));

		// Zeile - die Toolbar
		add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		createPanelArt();
		iZeile++;
		add(jPanelArt, new GridBagConstraints(0, iZeile++, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		if (this instanceof PanelKundesokomengenstaffel) {
			panelArtikel = new PanelMengenstaffelArtikel(getInternalFrame(),
					"Artikel", false);
		} else {
			panelArtikel = new PanelMengenstaffelArtikel(getInternalFrame(),
					"Artikel", true);
		}

		add(panelArtikel, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		panelArtgru = new PanelMengenstaffelArtgru(getInternalFrame(),
				"Artikelgruppe");

		add(panelArtgru, new GridBagConstraints(0, iZeile, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen
		iZeile++;
		add(getPanelStatusbar(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public void createPanelArt() {
		if (jPanelArt == null) {
			jPanelArt = new JPanel();
			jPanelArt.setLayout(new GridBagLayout());
			jPanelArt.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		}

		wlaArt = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"label.art"));
		HelperClient.setDefaultsToComponent(wlaArt, 94);

		wlaKundeartikelnummer = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("kunde.soko.kundeartikelnummer"));

		wcoArt = new WrapperComboBox();
		wcoArt.setMandatoryFieldDB(true);
		wcoArt.setMap(artMap);
		wcoArt.addActionListener(new PanelMengenstaffel_wcoArt_actionAdapter(
				this));

		jPanelArt.add(wlaArt, new GridBagConstraints(0, 0, 1, 1, 0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jPanelArt.add(wcoArt, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		if (bMitKundenartikelnummer) {

			jPanelArt.add(wlaKundeartikelnummer,
					new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jPanelArt.add(wtfKundeartikelnummer,
					new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0,
							GridBagConstraints.NORTH,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}
	}

	private void initPanel() throws Throwable {

		ART_ARTIKEL = LPMain.getInstance().getTextRespectUISPr("lp.artikel");
		ART_ARTIKELGRUPPE = LPMain.getInstance().getTextRespectUISPr(
				"lp.artikelgruppe");

		artMap = new HashMap<String, String>();
		artMap.put(ART_ARTIKEL, ART_ARTIKEL);
		artMap.put(ART_ARTIKELGRUPPE, ART_ARTIKELGRUPPE);
	}

	protected void setDefaults() throws Throwable {
		Object vorherselektiert = wcoArt.getKeyOfSelectedItem();

		wcoArt.setKeyOfSelectedItem(ART_ARTIKEL);

		leereAlleFelder(this);

		panelArtikel.setDefaults();
		panelArtgru.setDefaults();

		wcoArt.setKeyOfSelectedItem(vorherselektiert);

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	/**
	 * Auswahl der Art der SOKO ist erfolgt.
	 * 
	 * @param e
	 *            ActionEvent
	 */
	void wcoArt_actionPerformed(ActionEvent e) {
		Object currentArt = wcoArt.getKeyOfSelectedItem();
		String waehrungCNr = getInternalFrameKunde().getTpKunde().getKundeDto()
				.getCWaehrung();

		if (currentArt.equals(ART_ARTIKEL)) {
			panelArtikel.setVisible(true);
			panelArtikel.setMandatoryFields(true);

			panelArtgru.setVisible(false);
			panelArtgru.setMandatoryFields(false);
			wlaKundeartikelnummer.setVisible(true);
			wtfKundeartikelnummer.setVisible(true);
			panelArtikel.setWaehrungCNr(waehrungCNr);
			panelArtikel.setKundenpreislisteIId(getInternalFrameKunde()
					.getTpKunde().getKundeDto()
					.getVkpfArtikelpreislisteIIdStdpreisliste());
			panelArtikel.setLabels(waehrungCNr);
		} else if (currentArt.equals(ART_ARTIKELGRUPPE)) {
			panelArtikel.setVisible(false);
			panelArtikel.setMandatoryFields(false);

			panelArtgru.setVisible(true);
			panelArtgru.setMandatoryFields(true);
			wlaKundeartikelnummer.setVisible(false);
			wtfKundeartikelnummer.setVisible(false);
			wtfKundeartikelnummer.setText(null);
			panelArtgru.setWaehrungCNr(waehrungCNr);
			panelArtgru.setKundenpreislisteIId(getInternalFrameKunde()
					.getTpKunde().getKundeDto()
					.getVkpfArtikelpreislisteIIdStdpreisliste());
		}
	}
}

class PanelMengenstaffel_wcoArt_actionAdapter implements
		java.awt.event.ActionListener {
	PanelMengenstaffel adaptee;

	PanelMengenstaffel_wcoArt_actionAdapter(PanelMengenstaffel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcoArt_actionPerformed(e);
	}
}
