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
package com.lp.client.partner;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.partner.service.LflfliefergruppeDto;
import com.lp.server.partner.service.LieferantbeurteilungDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * <p> Diese Klasse kuemmert sich ...</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Vorname Nachname; dd.mm.05</p>
 * 
 * <p>@author $Author: christoph $</p>
 * 
 * @version not attributable Date $Date: 2009/08/19 08:13:55 $
 */
public class PanelLieferantbeurteilung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jpaWorking = null;
	private GridBagLayout gblWorkingPanel = null;
	private Border border = null;
	private GridBagLayout gblAussen = null;

	private LieferantbeurteilungDto lieferantbeurteilungDto = null;

	private WrapperLabel wlaDatum = null;
	private WrapperLabel wlaPunkte = null;
	private WrapperCheckBox wcbGesperrt = null;
	private WrapperDateField wdfDatum = null;
	private WrapperNumberField wnfPunkte = null;

	private WrapperCheckBox wcbManuellGeaendert = null;
	private WrapperLabel wlaKommentar = null;
	private WrapperTextField wtfKommentar = new WrapperTextField();
	private WrapperLabel wlaKlasse = null;
	private WrapperTextField wtfKlasse = new WrapperTextField();

	public PanelLieferantbeurteilung(InternalFrame internalFrame,
			String add2TitleI, Object keyI) throws Throwable {

		super(internalFrame, add2TitleI, keyI);

		jbInit();
		initComponents();
		initPanel();
	}

	/**
	 * jbInit
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		getInternalFrame().addItemChangedListener(this);

		// buttons.
		resetToolsPanel();

		String[] aWhichButtonIUse = null;

		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_REKLA_QUALITAETSSICHERUNG_CUD)) {

			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DISCARD, };
		} else {
			aWhichButtonIUse = new String[] {};
		}

		enableToolsPanelButtons(aWhichButtonIUse);

		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);

		wlaDatum = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.datum"));
		wlaPunkte = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"part.punkte"));

		wcbGesperrt = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("pers.gleitzeitsaldo.gesperrt"));

		wcbManuellGeaendert = new WrapperCheckBox(LPMain.getInstance()
				.getTextRespectUISPr("lp.manuellgeaendert"));
		wlaKlasse = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
				"lp.partnerklasse"));
		wlaKommentar = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("lp.kommentar"));

		wtfKommentar.setColumnsMax(80);
		wtfKlasse.setMandatoryField(true);
		wtfKlasse.setActivatable(false);
		wcbManuellGeaendert.setActivatable(false);

		wdfDatum = new WrapperDateField();
		wnfPunkte = new WrapperNumberField();
		wnfPunkte.setFractionDigits(0);
		wnfPunkte.setMandatoryField(true);

		wnfPunkte
				.addFocusListener(new PanelLieferantbeurteilung_wnfPunkte_focusAdapter(
						this));

		wdfDatum.setActivatable(false);

		// das aussenpanel hat immer das gridbaglayout.
		gblAussen = new GridBagLayout();
		setLayout(gblAussen);

		// actionpanel von Oberklasse holen und anhaengen.
		add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// Statusbar an den unteren Rand des Panels haengen.
		add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Workingpanel
		jpaWorking = new JPanel();
		gblWorkingPanel = new GridBagLayout();
		jpaWorking.setLayout(gblWorkingPanel);
		add(jpaWorking, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// Ab hier einhaengen.
		// Zeile
		jpaWorking.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wdfDatum, new GridBagConstraints(1, iZeile, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorking.add(wlaPunkte, new GridBagConstraints(0, iZeile, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wnfPunkte, new GridBagConstraints(1, iZeile, 1, 1, 0.2,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorking.add(wlaKlasse, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfKlasse, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 100, 0));
		iZeile++;
		jpaWorking.add(wlaKommentar, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorking.add(wtfKommentar, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorking.add(wcbGesperrt, new GridBagConstraints(1, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorking.add(wcbManuellGeaendert, new GridBagConstraints(1, iZeile,
				1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			lieferantbeurteilungDto = DelegateFactory.getInstance()
					.getLieferantDelegate()
					.lieferantbeurteilungFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}

	/**
	 * dto2Components
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		wcbGesperrt.setShort(lieferantbeurteilungDto.getBGesperrt());
		wnfPunkte.setInteger(lieferantbeurteilungDto.getIPunkte());
		wdfDatum.setTimestamp(lieferantbeurteilungDto.getTDatum());

		wtfKlasse.setText(lieferantbeurteilungDto.getCKlasse());
		wtfKommentar.setText(lieferantbeurteilungDto.getCKommentar());
		wcbManuellGeaendert.setShort(lieferantbeurteilungDto
				.getBManuellgeaendert());

		this.setStatusbarPersonalIIdAendern(lieferantbeurteilungDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(lieferantbeurteilungDto.getTAendern());

	}

	/**
	 * Behandle Ereignis Save.
	 * 
	 * @param e
	 *            Ereignis
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		if (allMandatoryFieldsSetDlg()) {
			berechneKlasseNeu();
			components2Dto();
			
			if (lieferantbeurteilungDto.getIId() == null) {
				lieferantbeurteilungDto.setIId(DelegateFactory.getInstance()
						.getLieferantDelegate().createLieferantbeurteilung(
								lieferantbeurteilungDto));
				setKeyWhenDetailPanel(lieferantbeurteilungDto.getIId());
			} else {
				DelegateFactory.getInstance().getLieferantDelegate()
						.updateLieferantbeurteilung(lieferantbeurteilungDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						lieferantbeurteilungDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getLieferantDelegate()
				.removeLieferantbeurteilung(lieferantbeurteilungDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	private void berechneKlasseNeu() throws Throwable {

		// Default MWST-Satz setzen
		int iPunkteA = 0;
		int iPunkteB = 0;
		int iPunkteC = 0;

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_A,
						ParameterFac.KATEGORIE_LIEFERANT,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
			iPunkteA = (Integer) parameter.getCWertAsObject();

		}

		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_B,
						ParameterFac.KATEGORIE_LIEFERANT,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
			iPunkteB = (Integer) parameter.getCWertAsObject();

		}
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_LIEFERANTENBEURTEILUNG_PUNKTE_KLASSE_C,
						ParameterFac.KATEGORIE_LIEFERANT,
						LPMain.getTheClient().getMandant());

		if (parameter.getCWert() != null && parameter.getCWert().length() > 0) {
			iPunkteC = (Integer) parameter.getCWertAsObject();

		}

		if (wnfPunkte.getInteger() != null) {
			int iPunkte = wnfPunkte.getInteger();
			wtfKlasse.setText("D");

			if (iPunkte >= iPunkteC) {
				wtfKlasse.setText("C");
			} 

			if (iPunkte >= iPunkteB) {
				wtfKlasse.setText("B");
			} 

			if (iPunkte >= iPunkteA) {
				wtfKlasse.setText("A");
			}

		}
	}

	public void wnfPunkte_focusLost(FocusEvent e) {
		try {

			if (this.getLockedstateDetailMainKey() == null
					|| this.getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW
					|| this.getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME) {

				berechneKlasseNeu();
			}
		} catch (Throwable e1) {
			handleException(e1, true);
		}
	}

	/**
	 * components2Dto
	 */
	private void components2Dto() throws Throwable {

		lieferantbeurteilungDto.setLieferantIId(getInternalFrameLieferant()
				.getLieferantDto().getIId());
		lieferantbeurteilungDto.setBGesperrt(wcbGesperrt.getShort());
		lieferantbeurteilungDto.setIPunkte(wnfPunkte.getInteger());
		lieferantbeurteilungDto.setTDatum(wdfDatum.getTimestamp());
		lieferantbeurteilungDto.setCKlasse(wtfKlasse.getText());
		lieferantbeurteilungDto
				.setBManuellgeaendert(Helper.boolean2Short(true));
		lieferantbeurteilungDto.setCKommentar(wtfKommentar.getText());
	}

	/**
	 * Fuelle die Helpercontrols, jene aus denen ausgewaehlt werden kann.
	 * 
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void initPanel() throws Throwable {
		// nothing here
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERANT;
	}

	private InternalFrameLieferant getInternalFrameLieferant() {
		return ((InternalFrameLieferant) getInternalFrame());
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, true, false);

		lieferantbeurteilungDto = new LieferantbeurteilungDto();

		if (!bNeedNoNewI) {
			getInternalFrameLieferant().setLflfliefergruppeDto(
					new LflfliefergruppeDto());
			setDefaults();
		}
	}

	/**
	 * setDefaults
	 * 
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		// nothing here
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventItemchanged(EventObject eI) throws ExceptionLP,
			Throwable {

		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfPunkte;
	}
}

class PanelLieferantbeurteilung_wnfPunkte_focusAdapter extends FocusAdapter {
	private PanelLieferantbeurteilung adaptee;

	PanelLieferantbeurteilung_wnfPunkte_focusAdapter(
			PanelLieferantbeurteilung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfPunkte_focusLost(e);
	}
}
