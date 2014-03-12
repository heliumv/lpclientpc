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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikellieferantDto;
import com.lp.server.artikel.service.MaterialDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.system.service.LocaleFac;

/**
 * <p>
 * Basisfenster fuer LP5 Positionen.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-11
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.10 $
 */
public class PanelPositionenArtikelEinkauf extends PanelPositionenPreiseingabe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int iYGridBagNext = 0;
	private LieferantDto lieferantDto;
	private ArtikellieferantDto artliefDto;
	protected BigDecimal bdWechselkurs;
	public WrapperLabel wlaRabattsatz = null;

	public WrapperButton wbuPreisauswahl = null;
	static final public String ACTION_SPECIAL_EK_PREIS_HOLEN = "action_special_ek_preis_holen";

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param sLockMeWer
	 *            String
	 * @param iSpaltenbreite1I
	 *            die Breite der ersten Spalte
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelPositionenArtikelEinkauf(InternalFrame internalFrame,
			String add2TitleI, Object key, String sLockMeWer,
			int iSpaltenbreite1I) throws Throwable {
		super(internalFrame, add2TitleI, key, sLockMeWer,
				internalFrame.bRechtDarfPreiseSehenEinkauf,
				internalFrame.bRechtDarfPreiseAendernEinkauf, iSpaltenbreite1I);
		jbInit();
		initComponents();
	}

	private void jbInit() throws Throwable {
		wlaRabattsatz = new WrapperLabel();
		setLayout(new GridBagLayout());

		// Zeile 1 + 2 ist der Artikelblock
		addArtikelblock(this, iYGridBagNext);

		// Zeile
		iYGridBagNext++;
		iYGridBagNext++;
		addFormatierungszeileNettoeinzelpreis(this, iYGridBagNext);

		iYGridBagNext++;
		addZeileRabattsumme(this, iYGridBagNext);

		// UW 27.04.06 AF, BS brauchen beide keine Zusatzrabattsumme
		// iYGridBagNext++;
		// addZeileZusatzrabattsumme(this, iYGridBagNext);

		// CK bei Bestellung einfuegen
		if (getInternalFrame().getBelegartCNr().equals(
				LocaleFac.BELEGART_BESTELLUNG)
				|| getInternalFrame().getBelegartCNr().equals(
						LocaleFac.BELEGART_AGSTUECKLISTE)) {
			iYGridBagNext++;
			addZeileMaterialzuschlag(this, iYGridBagNext);
		}

		iYGridBagNext++;
		addZeileNettogesamtpreis(this, iYGridBagNext, false);

		remove(wlaEinzelpreis);
		wbuPreisauswahl = new WrapperButton();
		HelperClient.setDefaultsToComponent(wbuPreisauswahl, 70);
		wbuPreisauswahl.setActionCommand(ACTION_SPECIAL_EK_PREIS_HOLEN);
		wbuPreisauswahl.setText(LPMain.getTextRespectUISPr("button.preis"));

		// darf Preis sehen Recht, keinen Button zeigen, wenn nicht erlaubt
		if (!bRechtDarfPreiseSehen) {
			wbuPreisauswahl.setVisible(false);
		}
		if (bRechtDarfPreiseAendern == false) {
			wbuPreisauswahl.setActivatable(false);
		}

		add(wbuPreisauswahl, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(2,
						2, 2, 2), 0, 0));

	}

	public void artikelDto2components() throws Throwable {
		super.artikelDto2components();

		if (getArtikelDto() != null && wnfMaterialzuschlag != null
				&& wlaMaterialzuschlag != null) {
			BigDecimal zuschlag = DelegateFactory
					.getInstance()
					.getMaterialDelegate()
					.getKupferzuschlagInLieferantenwaehrung(
							getArtikelDto().getIId(), getIIdLieferant());
			if (zuschlag != null) {
				wnfMaterialzuschlag.setBigDecimal(zuschlag);
			}

			if (getArtikelDto().getMaterialIId() != null) {
				MaterialDto mDto = DelegateFactory
						.getInstance()
						.getMaterialDelegate()
						.materialFindByPrimaryKey(
								getArtikelDto().getMaterialIId());
				wlaMaterialzuschlag.setText(mDto.getBezeichnung());
			} else {
				wlaMaterialzuschlag.setText(LPMain
						.getTextRespectUISPr("lp.materialzuschlag"));
			}
		}

		BigDecimal nNettoBetrag = wnfEinzelpreis.getBigDecimal().subtract(
				wnfRabattsumme.getBigDecimal());

		if (wnfMaterialzuschlag != null
				&& wnfMaterialzuschlag.getBigDecimal() != null) {
			nNettoBetrag = nNettoBetrag
					.add(wnfMaterialzuschlag.getBigDecimal());
		}

		if ((wnfNettopreis.getBigDecimal()).compareTo(nNettoBetrag) != 0) {
			wnfNettopreis.getWrbFixNumber().setSelected(true);
			wnfNettopreis.setBigDecimal(nNettoBetrag);
		}

	}

	/**
	 * Nettopreis und Einzelpreis bestimmen den Rabattsatz.
	 * 
	 * @param e
	 *            FocusEvent
	 * @throws Throwable
	 */
	void wnfNettopreis_focusLost(FocusEvent e) throws Throwable {

		BigDecimal nNettoBetrag = wnfEinzelpreis.getBigDecimal().subtract(
				wnfRabattsumme.getBigDecimal());

		if ((wnfNettopreis.getBigDecimal()).compareTo(nNettoBetrag) != 0) {
			wnfNettopreis.getWrbFixNumber().setSelected(true);
		}
		super.wnfNettopreis_focusLost(e);
	}

	public void setArtikellieferantDto(ArtikellieferantDto artliefDto) {
		this.artliefDto = artliefDto;
	}

	public ArtikellieferantDto getArtikellieferantDto() {
		return artliefDto;
	}

	public void setLieferantDto(LieferantDto lieferantDto) {
		this.lieferantDto = lieferantDto;
	}

	public void setBdWechselkurs(BigDecimal bdWechselkurs) {
		this.bdWechselkurs = bdWechselkurs;
	}

	public Integer getIIdLieferant() {
		if (lieferantDto != null) {
			return lieferantDto.getIId();
		} else {
			return null;
		}
	}

	public BigDecimal getBdWechselkurs() {
		return bdWechselkurs;
	}

}
