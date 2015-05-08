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
package com.lp.client.zeiterfassung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.personal.service.TaetigkeitDto;
import com.lp.server.personal.service.TaetigkeitsprDto;
import com.lp.server.personal.service.ZeiterfassungFac;

public class PanelSondertaetigkeiten extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private TaetigkeitDto taetigkeitenDto = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperLabel wlaBezahlt = new WrapperLabel();
	private WrapperNumberField wnfBezahlt = new WrapperNumberField();
	private WrapperLabel wlaWarnmeldung = new WrapperLabel();
	private WrapperNumberField wnfWarnmeldung = new WrapperNumberField();
	private WrapperCheckBox wcbTagbuchbar = new WrapperCheckBox();
	private WrapperCheckBox wcbBdebuchbar = new WrapperCheckBox();
	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();
	private WrapperCheckBox wcbUnterbrichtWarnmeldungen = new WrapperCheckBox();
	private WrapperLabel wlaImportkennzeichen = new WrapperLabel();
	private WrapperTextField wtfImportkennzeichen = new WrapperTextField();

	// TODO: Controller ueber den Constructor uebergeben
	private ISondertaetigkeitenController stc = new SondertaetigkeitenController();

	public PanelSondertaetigkeiten(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);

		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	/**
	 * Einen neuen SondertaetigkeitenController setzen
	 * 
	 * @param controller
	 *            ist der neue Controller
	 */
	public void setSondertaetigkeitenController(
			ISondertaetigkeitenController controller) {
		if (null == controller)
			throw new IllegalArgumentException("controller");
		stc = controller;
	}

	public ISondertaetigkeitenController getSondertaetigkeitenController() {
		return stc;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	protected void setDefaults() throws Throwable {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		wnfBezahlt.setActivatable(true);
		wtfKennung.setActivatable(true);
		super.eventActionNew(eventObject, true, false);
		taetigkeitenDto = new TaetigkeitDto();
		leereAlleFelder(this);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (taetigkeitenDto != null) {
			handleTaetigkeitForComponents(taetigkeitenDto.getCNr());
		}
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	/**
	 * wtfKennung und wnfBezahlt abh&auml;ngig von der Taetigkeit umschalten
	 * 
	 * @param taetigkeit
	 */
	protected void handleTaetigkeitForComponents(String taetigkeit) {
		if (stc.isSystemTaetigkeit(taetigkeit)) {
			wtfKennung.setActivatable(false);
		} else {
			wtfKennung.setActivatable(true);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getZeiterfassungDelegate()
				.removeTaetigkeit(taetigkeitenDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		taetigkeitenDto.setCNr(wtfKennung.getText());
		taetigkeitenDto.setCImportkennzeichen(wtfImportkennzeichen.getText());
		if (taetigkeitenDto.getTaetigkeitsprDto() == null) {
			taetigkeitenDto.setTaetigkeitsprDto(new TaetigkeitsprDto());
		}
		taetigkeitenDto.getTaetigkeitsprDto().setCBez(wtfBezeichnung.getText());
		taetigkeitenDto.setFBezahlt(wnfBezahlt.getDouble());
		taetigkeitenDto.setBTagbuchbar(wcbTagbuchbar.getShort());
		taetigkeitenDto.setBBdebuchbar(wcbBdebuchbar.getShort());
		taetigkeitenDto.setBVersteckt(wcbVersteckt.getShort());
		taetigkeitenDto.setBUnterbrichtwarnmeldung(wcbUnterbrichtWarnmeldungen.getShort());
		taetigkeitenDto
				.setTaetigkeitartCNr(ZeiterfassungFac.TAETIGKEITART_SONDERTAETIGKEIT);
		taetigkeitenDto.setIWarnmeldunginkalendertagen(wnfWarnmeldung
				.getInteger());
	}

	protected void dto2Components() throws Throwable {
		wtfKennung.setText(taetigkeitenDto.getCNr());
		wtfImportkennzeichen.setText(taetigkeitenDto.getCImportkennzeichen());
		if (taetigkeitenDto.getTaetigkeitsprDto() != null) {
			wtfBezeichnung.setText(taetigkeitenDto.getTaetigkeitsprDto()
					.getCBez());
		} else {
			wtfBezeichnung.setText("");
		}
		wnfBezahlt.setDouble(taetigkeitenDto.getFBezahlt());
		wcbBdebuchbar.setShort(taetigkeitenDto.getBBdebuchbar());
		wcbVersteckt.setShort(taetigkeitenDto.getBVersteckt());
		wcbTagbuchbar.setShort(taetigkeitenDto.getBTagbuchbar());
		wcbUnterbrichtWarnmeldungen.setShort(taetigkeitenDto.getBUnterbrichtwarnmeldung());
		wnfWarnmeldung.setInteger(taetigkeitenDto
				.getIWarnmeldunginkalendertagen());

		handleTaetigkeitForComponents(taetigkeitenDto.getCNr());

		this.setStatusbarPersonalIIdAendern(taetigkeitenDto
				.getPersonalIIdAendern());
		this.setStatusbarTAendern(taetigkeitenDto.getTAendern());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (taetigkeitenDto.getIId() == null) {
				taetigkeitenDto.setIId(DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.createTaetigkeit(taetigkeitenDto));
				setKeyWhenDetailPanel(taetigkeitenDto.getIId());
			} else {
				DelegateFactory.getInstance().getZeiterfassungDelegate()
						.updateTaetigkeit(taetigkeitenDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						taetigkeitenDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		// ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaKennung.setText(LPMain.getTextRespectUISPr("label.kennung"));
		wtfKennung.setColumnsMax(ZeiterfassungFac.MAX_TAETIGKEIT_KENNUNG);
		wtfKennung.setText("");
		wtfKennung.setMandatoryField(true);
		wnfBezahlt.setMandatoryField(true);

		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));
		wtfBezeichnung.setToolTipText("");
		wtfBezeichnung.setColumns(ZeiterfassungFac.MAX_TAETIGKEIT_BEZEICHNUNG);
		getInternalFrame().addItemChangedListener(this);

		wlaBezahlt
				.setText(LPMain
						.getTextRespectUISPr("zeiterfassung.sondertaetigkeiten.bezahlt"));

		wlaWarnmeldung
				.setText(LPMain
						.getTextRespectUISPr("pers.taetigkeit.warnmeldunginkalendertagen"));
		wnfWarnmeldung.setMinimumValue(0);
		wnfWarnmeldung.setFractionDigits(0);

		wcbTagbuchbar.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.taetigkeiten.tageweise"));
		wcbBdebuchbar.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.taetigkeiten.bdebuchbar"));
		wcbVersteckt.setText(LPMain
				.getTextRespectUISPr("lp.versteckt"));

		
		wcbUnterbrichtWarnmeldungen.setText(LPMain
				.getTextRespectUISPr("zeiterfassung.taetigkeiten.unterbrichtwarnmeldung"));
		wlaImportkennzeichen.setText(LPMain.getTextRespectUISPr("pers.sondertaetigkeit.importkennzeichen"));
		
		
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		int iZeile = 0;
		
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, iZeile, 1, 1, 0.25, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 2, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBezahlt, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBezahlt, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 20, 0));
		iZeile++;
		jpaWorkingOn.add(wlaWarnmeldung, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWarnmeldung, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 20, 0));
		iZeile++;
		jpaWorkingOn.add(wcbTagbuchbar, new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbBdebuchbar, new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbUnterbrichtWarnmeldungen, new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbVersteckt, new GridBagConstraints(0, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaImportkennzeichen, new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfImportkennzeichen, new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_TAETIGKEITEN;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			taetigkeitenDto = DelegateFactory.getInstance()
					.getZeiterfassungDelegate()
					.taetigkeitFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}
}
