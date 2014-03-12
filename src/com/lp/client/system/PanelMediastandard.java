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
package com.lp.client.system;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperMediaControl;
import com.lp.client.frame.component.WrapperMediaControlTextmodul;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;

@SuppressWarnings("static-access")
/**
 * <p>Basisfenster fuer Mediastandards</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum 2005-04-21</p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.3 $
 */
public class PanelMediastandard extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WrapperMediaControl wmcMedia = null;
	private MediastandardDto mediastandardDto = null;
	private WrapperLabel wlaBezeichnung = null;
	private WrapperTextField wtfBezeichnung = null;
	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox();
	private JPanel jPanelWorkingOn = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK der Position
	 * @param tabbedPaneMediastandard
	 *            TabbedPaneMediastandard
	 * @throws Throwable
	 */
	public PanelMediastandard(InternalFrame internalFrame, String add2TitleI,
			Object key, TabbedPaneMediastandard tabbedPaneMediastandard)
			throws Throwable {
		super(internalFrame, add2TitleI, key);
		jbInit();
		setDefaults();
	}

	private void jbInit() throws Throwable {
		wmcMedia = new WrapperMediaControlTextmodul(getInternalFrame(), "",
				true);
		wlaBezeichnung = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("label.bezeichnung"));
		wtfBezeichnung = new WrapperTextField();
		wtfBezeichnung.setMandatoryFieldDB(true);
		wcbVersteckt.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.versteckt"));

		// das Aussenpanel hat immer das Gridbaglayout und einen Rahmen nach
		// innen
		setLayout(new GridBagLayout());
		// zusaetzliche Buttons setzen
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);
		createPanelWorkingOn();

		this.add(getToolsPanel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jPanelWorkingOn, new GridBagConstraints(0, 1, 6, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public void createPanelWorkingOn() throws Exception, Throwable {
		if (jPanelWorkingOn == null) {
			jPanelWorkingOn = new JPanel();
			GridBagLayout gridBagLayout = new GridBagLayout();
			jPanelWorkingOn.setLayout(gridBagLayout);
			jPanelWorkingOn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,
					0));
		}
		iZeile++;
		jPanelWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile,
				1, 1, 1.5, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		if (DelegateFactory
				.getInstance()
				.getTheJudgeDelegate()
				.hatRecht(
						com.lp.server.benutzer.service.RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			jPanelWorkingOn.add(wcbVersteckt, new GridBagConstraints(2, iZeile,
					1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100,
					0));
		}

		iZeile++;
		jPanelWorkingOn.add(wmcMedia, new GridBagConstraints(0, iZeile, 6, 1,
				0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_MEDIASTANDARD;
	}

	protected void components2Dto() throws Throwable {
		mediastandardDto.setCNr(wtfBezeichnung.getText());
		mediastandardDto.setBVersteckt(wcbVersteckt.getShort());
		mediastandardDto.setDatenformatCNr(wmcMedia.getWcoArt()
				.getKeyOfSelectedItem().toString());
		mediastandardDto.setMandantCNr(LPMain.getTheClient().getMandant());
		mediastandardDto.setLocaleCNr(LPMain.getTheClient().getLocUiAsString());
		mediastandardDto.setCDateiname(wmcMedia.getDateiname());
		if (wmcMedia.getWcoArt().getKeyOfSelectedItem()
				.equals(MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
			mediastandardDto.setOMediaText(wmcMedia.getOMediaText());
		} else {
			mediastandardDto.setOMediaImage(wmcMedia.getOMediaImage());
		}
	}

	protected void dto2Components() throws Throwable {

		if (mediastandardDto != null) {
			wcbVersteckt.setShort(mediastandardDto.getBVersteckt());
			wmcMedia.getWcoArt().setKeyOfSelectedItem(
					mediastandardDto.getDatenformatCNr());
			wtfBezeichnung.setText(mediastandardDto.getCNr());
			wmcMedia.setDateiname(mediastandardDto.getCDateiname());
			if (mediastandardDto.getDatenformatCNr().equalsIgnoreCase(
					MediaFac.DATENFORMAT_MIMETYPE_TEXT_HTML)) {
				wmcMedia.setOMediaText(mediastandardDto.getOMediaText());
			} else {
				wmcMedia.setOMediaImage(mediastandardDto.getOMediaImage());
			}
			// Statusbar
			setStatusbarPersonalIIdAendern(mediastandardDto
					.getPersonalIIdAendern());
			setStatusbarPersonalIIdAnlegen(mediastandardDto
					.getPersonalIIdAnlegen());
			setStatusbarTAendern(mediastandardDto.getTAendern());
			setStatusbarTAnlegen(mediastandardDto.getTAnlegen());
		}
	}

	/**
	 * Neue ER.
	 * 
	 * @param eventObject
	 *            EventObject
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		this.leereAlleFelder(this);
		setDefaults();
		this.clearStatusbar();
	}

	/**
	 * setDefaults
	 */
	private void setDefaults() {
		mediastandardDto = new MediastandardDto();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();
			if (key == null
					|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
				// einen neuen Eintrag anlegen oder die letzte Position wurde
				// geloescht.
				leereAlleFelder(this);
				clearStatusbar();
			} else {
				mediastandardDto = DelegateFactory.getInstance()
						.getMediaDelegate()
						.mediastandardFindByPrimaryKey((Integer) key);
				dto2Components();
			}
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		// Mischung zwischen Branche und Stati: cNr Eigentumsvorbehalt und
		// Lieferbedingungen
		// kann nicht geaendert werden
		if (mediastandardDto.getCNr().equals(
				MediaFac.MEDIAART_EIGENTUMSVORBEHALT)
				|| mediastandardDto.getCNr().equals(
						MediaFac.MEDIAART_LIEFERBEDINGUNGEN)) {
			DialogFactory.showModalDialog(LPMain.getInstance()
					.getTextRespectUISPr("lp.hint"), LPMain.getInstance()
					.getTextRespectUISPr("lp.hint.kannnichtgeloeschtwerden"));
		} else {
			DelegateFactory.getInstance().getMediaDelegate()
					.removeMediastandard(mediastandardDto);
			super.eventActionDelete(e, bAdministrateLockKeyI, bNeedNoDeleteI);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (mediastandardDto.getIId() == null) {
				mediastandardDto = DelegateFactory.getInstance()
						.getMediaDelegate()
						.createMediastandard(mediastandardDto);
			} else {
				mediastandardDto = DelegateFactory.getInstance()
						.getMediaDelegate()
						.updateMediastandard(mediastandardDto);
			}
			this.setKeyWhenDetailPanel(mediastandardDto.getIId());
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}

	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		eventYouAreSelected(false);
		super.eventActionUpdate(aE, false); // Buttons schalten
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
