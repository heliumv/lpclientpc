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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.finanz.FinanzFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtgrusprDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.finanz.service.KontoDtoSmall;

@SuppressWarnings("static-access")
public class PanelArtikelgruppen extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaPanelWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private WrapperLabel wlaLager = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();
	private ArtgruDto artgruDto = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();
	private WrapperButton wbuVatergruppe = new WrapperButton();
	private WrapperTextField wtfVatergruppe = new WrapperTextField();
	private WrapperButton wbuKonto = new WrapperButton();
	private WrapperTextField wtfKonto = new WrapperTextField();
	private PanelQueryFLR panelQueryFLRArtikelgruppe = null;
	private PanelQueryFLR panelQueryFLRKonto = null;

	private WrapperCheckBox wcbRueckgabe = new WrapperCheckBox();
	private WrapperCheckBox wcbZertifizierung = new WrapperCheckBox();
	private WrapperCheckBox wcbKeineWarnungimLS = new WrapperCheckBox();

	static final public String ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE = "action_artikelgruppe_from_liste";
	static final public String ACTION_SPECIAL_FLR_KONTO = "ACTION_SPECIAL_FLR_KONTO";

	public PanelArtikelgruppen(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfKennung;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		artgruDto = new ArtgruDto();

		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand()
				.equals(ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE)) {
			dialogQueryArtikelgruppeFromListe(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_KONTO)) {
			panelQueryFLRKonto = FinanzFilterFactory.getInstance()
					.createPanelFLRFinanzKonto(getInternalFrame(),
							artgruDto.getKontoIId(), true);
			new DialogQuery(panelQueryFLRKonto);
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRArtikelgruppe) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				ArtgruDto temp = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.artgruFindByPrimaryKey((Integer) key);
				wtfVatergruppe.setText(temp.getCNr());
				artgruDto.setArtgruIId(temp.getIId());
			} else if (e.getSource() == panelQueryFLRKonto) {
				// JA ist mein lokaler FLR
				// hol jetzt den kontokey
				Integer iId = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (iId != null) {
					artgruDto.setKontoIId(iId);
					KontoDtoSmall kontoDtoSmall = DelegateFactory.getInstance()
							.getFinanzDelegate()
							.kontoFindByPrimaryKeySmall(iId);
					wtfKonto.setText(kontoDtoSmall.getCNr());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRArtikelgruppe) {
				wtfVatergruppe.setText(null);
				artgruDto.setArtgruIId(null);
			} else if (e.getSource() == panelQueryFLRKonto) {
				wtfKonto.setText(null);
				artgruDto.setKontoIId(null);
			}
		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(border);
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wlaLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.kennung"));
		wtfKennung.setColumnsMax(ArtikelFac.MAX_ARTIKELGRUPPE_NAME);
		wtfKennung.setText("");
		wtfKennung.setMandatoryField(true);

		wcbRueckgabe.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.rueckgabe"));
		wcbZertifizierung.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.gruppe.zertifizierung"));


		wcbKeineWarnungimLS.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.artgru.keinevkwarnungimls"));

		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.bezeichnung"));
		wtfBezeichnung.setToolTipText("");
		wtfBezeichnung.setColumnsMax(ArtikelFac.MAX_ARTIKELGRUPPE_BEZEICHNUNG);
		wtfBezeichnung.setText("");
		wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto"));
		wbuKonto.setActionCommand(PanelArtikelgruppen.ACTION_SPECIAL_FLR_KONTO);
		wbuKonto.addActionListener(this);
		wbuVatergruppe.setText(LPMain.getInstance().getTextRespectUISPr(
				"artikel.vatergruppe")
				+ "...");
		wbuVatergruppe
				.setActionCommand(PanelArtikelgruppen.ACTION_SPECIAL_ARTIKELGRUPPE_FROM_LISTE);
		wbuVatergruppe.addActionListener(this);
		getInternalFrame().addItemChangedListener(this);
		wtfVatergruppe.setText("");
		wtfVatergruppe.setActivatable(false);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaPanelWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0,
				1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaPanelWorkingOn.add(wlaLager, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaPanelWorkingOn.add(wtfKennung, new GridBagConstraints(1, 0, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaPanelWorkingOn.add(wlaBezeichnung, new GridBagConstraints(2, 0, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaPanelWorkingOn.add(wtfBezeichnung, new GridBagConstraints(3, 0, 1,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaPanelWorkingOn.add(wbuVatergruppe, new GridBagConstraints(0, 1, 1,
				1, 0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaPanelWorkingOn.add(wtfVatergruppe, new GridBagConstraints(1, 1, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaPanelWorkingOn.add(wcbRueckgabe, new GridBagConstraints(2, 1, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaPanelWorkingOn.add(wcbZertifizierung, new GridBagConstraints(2, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaPanelWorkingOn.add(wcbKeineWarnungimLS, new GridBagConstraints(3, 2,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaPanelWorkingOn.add(wbuKonto, new GridBagConstraints(0, 2, 1, 1,
				0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		jpaPanelWorkingOn.add(wtfKonto, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	void dialogQueryArtikelgruppeFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRArtikelgruppe = ArtikelFilterFactory.getInstance()
				.createPanelFLRArtikelgruppe(getInternalFrame(),
						artgruDto.getArtgruIId());

		new DialogQuery(panelQueryFLRArtikelgruppe);
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKELGRUPPE;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getArtikelDelegate()
				.removeArtgru(artgruDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
		artgruDto.setCNr(wtfKennung.getText());
		artgruDto.setBRueckgabe(wcbRueckgabe.getShort());
		artgruDto.setBZertifizierung(wcbZertifizierung.getShort());
		artgruDto.setBKeinevkwarnmeldungimls(wcbKeineWarnungimLS.getShort());
		if (artgruDto.getArtgrusprDto() == null) {
			artgruDto.setArtgrusprDto(new ArtgrusprDto());
		}
		artgruDto.getArtgrusprDto().setCBez(wtfBezeichnung.getText());
	}

	protected void dto2Components() throws Throwable {
		wtfKennung.setText(artgruDto.getCNr());
		if (artgruDto.getArtgrusprDto() != null) {
			wtfBezeichnung.setText(artgruDto.getArtgrusprDto().getCBez());
		} else {
			wtfBezeichnung.setText("");
		}
		if (artgruDto.getArtgruIId() != null) {
			ArtgruDto temp = DelegateFactory.getInstance().getArtikelDelegate()
					.artgruFindByPrimaryKey(artgruDto.getArtgruIId());
			wtfVatergruppe.setText(temp.getCNr());
		} else {
			wtfVatergruppe.setText(null);
		}
		if (artgruDto.getKontoIId() != null) {
			KontoDtoSmall kontoDtoSmall = DelegateFactory.getInstance()
					.getFinanzDelegate()
					.kontoFindByPrimaryKeySmall(artgruDto.getKontoIId());
			wtfKonto.setText(kontoDtoSmall.getCNr());
		} else {
			wtfKonto.setText(null);
		}
		wcbRueckgabe.setShort(artgruDto.getBRueckgabe());
		wcbZertifizierung.setShort(artgruDto.getBZertifizierung());
		wcbKeineWarnungimLS.setShort(artgruDto.getBKeinevkwarnmeldungimls());

		this.setStatusbarPersonalIIdAendern(artgruDto.getPersonalIIdAendern());
		this.setStatusbarTAendern(artgruDto.getTAendern());
		this.setStatusbarPersonalIIdAnlegen(artgruDto.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(artgruDto.getTAnlegen());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (artgruDto.getIId() == null) {
				artgruDto.setIId(DelegateFactory.getInstance()
						.getArtikelDelegate().createArtgru(artgruDto));
				setKeyWhenDetailPanel(artgruDto.getIId());
			} else {
				DelegateFactory.getInstance().getArtikelDelegate()
						.updateArtgru(artgruDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(artgruDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			artgruDto = DelegateFactory.getInstance().getArtikelDelegate()
					.artgruFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}
}
