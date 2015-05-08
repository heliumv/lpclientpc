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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import com.lp.client.angebotstkl.AngebotstklFilterFactory;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.system.service.MandantFac;

@SuppressWarnings("static-access")
/**
 * <p>PanelPositionen fuer Angebotsstueckliste.</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 13.12.05</p>
 * <p> </p>
 * @author Uli Walch
 * @version $Revision: 1.5 $
 */
public class PanelPositionenAngebotstueckliste extends
		PanelPositionenPreiseingabe {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String ACTION_SPECIAL_AGSTKL_FROM_LISTE = "action_special_agstkl_from_liste";

	// optionaler Block fuer die AGSTKL Auswahl und Anzeige
	public WrapperButton wbuAgstklauswahl = null;
	public PanelQueryFLR panelQueryFLRAgstklauswahl = null;

	public WrapperTextField wtfAgstkl = null;

	public WrapperLabel wlaBezeichnungAgstkl = null;
	public WrapperTextField wtfBezeichnungAgstkl = null;

	private WrapperCheckBox wcbAlternativeAgstkl = null;
	private WrapperLabel wlaKalkulatorischerWertAgstkl = null;
	private WrapperNumberField wnfKalkulatorischerWertAgstkl = null;
	private WrapperLabel wlaWaehrungKalkulatorischerWertAgstkl = null;

	public WrapperGotoButton wbuAgstklGoto = new WrapperGotoButton(
			WrapperGotoButton.GOTO_ANGEBOTSTKL_AUSWAHL);

	/** Die gewaehlte Angebotstueckliste hinterlegen. */
	public AgstklDto agstklDto = null;

	/** Die Waehrung des Belegs, zu dem die Position gehoert. */
	private String belegwaehrungCNr = null;

	private int iYGridBagNext = 0;

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
	 *            die Breites der ersten Spalte
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelPositionenAngebotstueckliste(InternalFrame internalFrame,
			String add2TitleI, Object key, String sLockMeWer,
			int iSpaltenbreite1I) throws Throwable {
		super(internalFrame, add2TitleI, key, sLockMeWer,
				internalFrame.bRechtDarfPreiseSehenVerkauf,
				internalFrame.bRechtDarfPreiseAendernVerkauf, iSpaltenbreite1I);
		jbInit();
		initComponents();
		initPanel();
	}

	private void jbInit() throws Throwable {
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		setLayout(new GridBagLayout());

		wbuAgstklauswahl = new WrapperButton(LPMain.getInstance()
				.getTextRespectUISPr("button.agstkl"));
		wbuAgstklauswahl.setActionCommand(ACTION_SPECIAL_AGSTKL_FROM_LISTE);
		wbuAgstklauswahl.addActionListener(this);

		wtfAgstkl = new WrapperTextField();
		wtfAgstkl.setActivatable(false);

		wlaBezeichnungAgstkl = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.bezeichnung"));

		wtfBezeichnungAgstkl = new WrapperTextField();

		wcbAlternativeAgstkl = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("angb.alternative"));

		wlaKalkulatorischerWertAgstkl = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("angb.kalkulatorischerwert"));
		wnfKalkulatorischerWertAgstkl = new WrapperNumberField();
		wlaWaehrungKalkulatorischerWertAgstkl = new WrapperLabel();

		add(wbuAgstklauswahl, new GridBagConstraints(0, iYGridBagNext, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 24), 0, 0));
		add(wbuAgstklGoto.getWrapperButtonGoTo(), new GridBagConstraints(0,
				iYGridBagNext, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 10, 0));
		add(wtfAgstkl, new GridBagConstraints(1, iYGridBagNext, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		add(wlaBezeichnungAgstkl, new GridBagConstraints(2, iYGridBagNext, 1,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		add(wtfBezeichnungAgstkl, new GridBagConstraints(3, iYGridBagNext, 5,
				1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iYGridBagNext++;
		addFormatierungszeileNettoeinzelpreis(this, iYGridBagNext);

		iYGridBagNext++;
		addZeileRabattsumme(this, iYGridBagNext);

		iYGridBagNext++;
		addZeileZusatzrabattsumme(this, iYGridBagNext);

		iYGridBagNext++;
		addZeileNettogesamtpreis(this, iYGridBagNext, false);

		iYGridBagNext++;
		addZeileMwstsumme(this, iYGridBagNext);

		iYGridBagNext++;
		addZeileBruttogesamtpreis(this, iYGridBagNext);

		add(wcbAlternativeAgstkl, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(10, 0, 2, 2), 0, 0));
		add(wlaKalkulatorischerWertAgstkl, new GridBagConstraints(3, 8, 3, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));
		add(wnfKalkulatorischerWertAgstkl, new GridBagConstraints(6, 8, 1, 1,
				0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));
		add(wlaWaehrungKalkulatorischerWertAgstkl, new GridBagConstraints(7, 8,
				1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
				GridBagConstraints.HORIZONTAL, new Insets(10, 2, 2, 2), 0, 0));
	}

	private void initPanel() throws Throwable {
		belegwaehrungCNr = LPMain.getInstance().getTheClient()
				.getSMandantenwaehrung();
	}

	protected void setDefaults() throws Throwable {
		super.setDefaults();

		agstklDto = new AgstklDto();

		if (wtfBezeichnungAgstkl != null) {
			wtfBezeichnungAgstkl.setActivatable(false);
		}

		wcbAlternativeAgstkl.setSelected(false);
		wnfRabattsumme.getWrbFixNumber().setSelected(true);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		super.eventItemchanged(eI);

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAgstklauswahl) {
				Integer agstklIId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				agstklDto = DelegateFactory.getInstance()
						.getAngebotstklDelegate()
						.agstklFindByPrimaryKey(agstklIId);
				agstklDto2components();
			}
		}
	}

	public void agstklDto2components() throws Throwable {
		wtfAgstkl.setText(agstklDto.getCNr());
		wtfBezeichnungAgstkl.setText(agstklDto.getCBez());

		wbuAgstklGoto.setOKey(agstklDto.getIId());

		preisBerechnen();

	}

	public void preisBerechnen() throws ExceptionLP, Throwable {

		// PJ18725 Wenn Mengenstaffel
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_AGSTKL_ARBEITSPLAN)) {
			BigDecimal bdMenge = BigDecimal.ONE;
			if (wnfMenge.getBigDecimal() != null) {
				bdMenge = wnfMenge.getBigDecimal();
			}

			BigDecimal nKalkulatorischerWert = DelegateFactory
					.getInstance()
					.getAngebotstklDelegate()
					.berechneKalkulatorischenAgstklwert(agstklDto.getIId(),
							bdMenge, belegwaehrungCNr);
			wnfKalkulatorischerWertAgstkl.setBigDecimal(nKalkulatorischerWert);

			BigDecimal bdVkPreisGewaehlt = DelegateFactory.getInstance()
					.getAngebotstklDelegate()
					.getVKPreisGewaehlt(bdMenge, agstklDto.getIId());

			BigDecimal bdVkPreisberechnet = DelegateFactory.getInstance()
					.getAngebotstklDelegate()
					.getVKPreis(bdMenge, agstklDto.getIId())[AngebotstklFac.VKPREIS_LT_AGTSKLPOSITIONSPREIS];

			if (bdVkPreisGewaehlt == null) {
				// IMS 1565 Wenn eine AGSTKL neu uebernommen wird, den
				// kalkulatorischen Wert
				// als Preisvorschlag anzeigen
				wnfEinzelpreis.setBigDecimal(bdVkPreisberechnet);
				wnfNettopreis.setBigDecimal(bdVkPreisberechnet);
			} else {

				wnfEinzelpreis.setBigDecimal(bdVkPreisberechnet);

				wnfNettopreis.setBigDecimal(bdVkPreisGewaehlt);
				wnfNettopreis.getWrbFixNumber().setSelected(true);
			}
			calculateFields();

		} else {

			BigDecimal nKalkulatorischerWert = DelegateFactory
					.getInstance()
					.getAngebotstklDelegate()
					.berechneKalkulatorischenAgstklwert(agstklDto.getIId(),
							null, belegwaehrungCNr);
			wnfKalkulatorischerWertAgstkl.setBigDecimal(nKalkulatorischerWert);
			// IMS 1565 Wenn eine AGSTKL neu uebernommen wird, den
			// kalkulatorischen Wert
			// als Preisvorschlag anzeigen
			wnfEinzelpreis.setBigDecimal(nKalkulatorischerWert);
			calculateFields();
		}
	}

	/**
	 * Hier kommen die events meiner speziellen Buttons an.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

		if (e.getActionCommand().equals(ACTION_SPECIAL_AGSTKL_FROM_LISTE)) {
			dialogQueryAgestkl(e);
		}
	}

	public void dialogQueryAgestkl(ActionEvent e) throws Throwable {
		panelQueryFLRAgstklauswahl = AngebotstklFilterFactory.getInstance()
				.createPanelFLRAgstkl(getInternalFrame(), true, false);
		new DialogQuery(panelQueryFLRAgstklauswahl);
	}

	public WrapperLabel getWlaWaehrungKalkulatorischerWertAgstkl() {
		return wlaWaehrungKalkulatorischerWertAgstkl;
	}

	public WrapperLabel getWlaKalkulatorischerWertAgstkl() {
		return wlaKalkulatorischerWertAgstkl;
	}

	public WrapperNumberField getWnfKalkulatorischerWertAgstkl() {
		return wnfKalkulatorischerWertAgstkl;
	}

	public String getBelegwaehrungCNr() {
		return belegwaehrungCNr;
	}

	public void setBelegwaehrungCNr(String cNrBelegwaehrungI) {
		belegwaehrungCNr = cNrBelegwaehrungI;
	}

	public WrapperCheckBox getWcbAlternativeAgstkl() {
		return wcbAlternativeAgstkl;
	}

	protected void wnfMenge_focusLost(FocusEvent e) {
		try {
			preisBerechnen();
		} catch (Throwable e1) {
			handleException(e1, false);
		}
	}

}
