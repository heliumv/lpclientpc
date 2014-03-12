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
package com.lp.client.instandhaltung;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTexteingabe;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.instandhaltung.service.WartungslisteDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.Helper;

public class PanelWartungsliste extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameInstandhaltung internalFrameStueckliste = null;
	private WrapperTextField wtfHandartikel = new WrapperTextField();
	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();
	private WrapperLabel wlaPositionsart = new WrapperLabel();
	private WrapperComboBox wcbPositionsart = new WrapperComboBox();

	private WrapperLabel wlaVeraltet = new WrapperLabel();
	private WrapperLabel wlaVeraltetAb = new WrapperLabel();
	private WrapperDateField wdfVeraltet = new WrapperDateField();
	private WrapperTextField wtfVeraltete = new WrapperTextField();
	private WrapperLabel wlaVeraltetPerson = new WrapperLabel();
	public WrapperEditorField wefText = null;

	protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;

	private WrapperIdentField wifArtikel = null;

	private WrapperLabel wlaBezeichnung = new WrapperLabel();

	private WartungslisteDto wartungslisteDto = null;

	private WrapperCheckBox wcbVerrechenbar = new WrapperCheckBox();
	private WrapperCheckBox wcbWartungsmaterial = new WrapperCheckBox();

	static final public String ACTION_SPECIAL_ARTIKEL_FROM_LISTE = "action_artikel_from_liste";

	private ArtikelDto letzterArtikel = null;

	public PanelWartungsliste(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameStueckliste = (InternalFrameInstandhaltung) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcbPositionsart;
	}

	private void setDefaults() throws Throwable {

		Map<String, String> m = new LinkedHashMap<String, String>();
		m.put(LocaleFac.POSITIONSART_IDENT, LocaleFac.POSITIONSART_IDENT);
		m.put(LocaleFac.POSITIONSART_HANDEINGABE,
				LocaleFac.POSITIONSART_HANDEINGABE);
		wcbPositionsart.setMap(m);
		wefText.getLpEditor().setText(null);

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		letzterArtikel = wifArtikel.getArtikelDto();
		super.eventActionNew(eventObject, true, false);
		wartungslisteDto = new WartungslisteDto();

		leereAlleFelder(this);
		wnfMenge.setBigDecimal(new java.math.BigDecimal(0));
		wifArtikel.setArtikelDto(null);

		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			// Dieses Flag gibt an, ob die neue Position vor der aktuellen
			// eingefuegt werden soll
			bFuegeNeuePositionVorDerSelektiertenEin = true;
		}

	}

	protected void eventActionText(ActionEvent e) throws Throwable {
		super.eventActionText(e);
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			// Editor auf Read Only schalten

		}
		getInternalFrame().showPanelEditor(wefText, this.getAdd2Title(),
				wefText.getLpEditor().getText(),
				getLockedstateDetailMainKey().getIState());
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getInstandhaltungDelegate()
				.removeWartungsliste(wartungslisteDto);
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {

		wartungslisteDto.setXBemerkung(wefText.getText());
		wartungslisteDto.setGeraetIId(internalFrameStueckliste
				.getTabbedPaneInstandhaltung().getGeraetDto().getIId());
		wartungslisteDto.setBVerrechenbar(wcbVerrechenbar.getShort());
		wartungslisteDto.setBWartungsmaterial(wcbWartungsmaterial.getShort());

		wartungslisteDto.setNMenge(wnfMenge.getBigDecimal());

		wartungslisteDto.setCVeraltet(wtfVeraltete.getText());
		wartungslisteDto.settVeraltet(wdfVeraltet.getTimestamp());

		if (((String) wcbPositionsart.getKeyOfSelectedItem())
				.equals(LocaleFac.POSITIONSART_IDENT)) {
			wartungslisteDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());
		} else {
			wartungslisteDto.setCBez(wtfHandartikel.getText());
			wartungslisteDto.setArtikelDto(null);

		}

	}

	protected void dto2Components() throws Throwable {

		// wtfArtikel.setText(stuecklistepositionDto.getArtikelDto().formatArtikelbezeichnung());
		wnfMenge.setBigDecimal(wartungslisteDto.getNMenge());

		wcbVerrechenbar.setShort(wartungslisteDto.getBVerrechenbar());
		wcbWartungsmaterial.setShort(wartungslisteDto.getBWartungsmaterial());

		wefText.setText(wartungslisteDto.getXBemerkung());

		if (wartungslisteDto.getArtikelIId() == null) {
			wcbPositionsart
					.setKeyOfSelectedItem(LocaleFac.POSITIONSART_HANDEINGABE);
			wtfHandartikel.setText(wartungslisteDto.getCBez());
			wifArtikel.setArtikelDto(null);
		} else {
			wcbPositionsart.setKeyOfSelectedItem(LocaleFac.POSITIONSART_IDENT);

			wifArtikel.setArtikelDto(DelegateFactory.getInstance()
					.getArtikelDelegate()
					.artikelFindByPrimaryKey(wartungslisteDto.getArtikelIId()));

			wtfHandartikel.setText(null);

		}

		wdfVeraltet.setTimestamp(wartungslisteDto.getTVeraltet());
		wtfVeraltete.setText(wartungslisteDto.getCVeraltet());

		if (wartungslisteDto.getTPersonalVeraltet() != null) {

			String ausg = "";

			ausg += Helper.formatDatum(wartungslisteDto.getTPersonalVeraltet(),
					LPMain.getTheClient().getLocUi());

			if (wartungslisteDto.getPersonalIIdVeraltet() != null) {
				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								wartungslisteDto.getPersonalIIdVeraltet());
				ausg += " (" + personalDto.getCKurzzeichen() + ")";
			}

			wlaVeraltetPerson.setText(ausg);

		} else {
			wlaVeraltetPerson.setText("");
		}

	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);

		// bei Discard zuruecksetzen
		bFuegeNeuePositionVorDerSelektiertenEin = false;
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		try {
			wifArtikel.refresh();
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();

				if ((wartungslisteDto.getCVeraltet() != null && wartungslisteDto
						.getTVeraltet() == null)
						|| (wartungslisteDto.getCVeraltet() == null && wartungslisteDto
								.getTVeraltet() != null)) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.error"),
									LPMain.getTextRespectUISPr("is.wartungsliste.veraltet.error"));
					return;
				}

				if (wartungslisteDto.getIId() == null) {

					if (bFuegeNeuePositionVorDerSelektiertenEin) {
						Integer iIdAktuellePosition = (Integer) internalFrameStueckliste
								.getTabbedPaneInstandhaltung()
								.getPanelQueryWartungsliste().getSelectedId();

						// erstepos: 0 die erste Position steht an der Stelle 1
						Integer iSortAktuellePosition = new Integer(1);

						// erstepos: 1 die erste Position steht an der Stelle 1
						if (iIdAktuellePosition != null) {
							iSortAktuellePosition = DelegateFactory
									.getInstance()
									.getInstandhaltungDelegate()
									.wartungslisteFindByPrimaryKey(
											iIdAktuellePosition).getISort();

							// Die bestehenden Positionen muessen Platz fuer die
							// neue schaffen
							DelegateFactory
									.getInstance()
									.getInstandhaltungDelegate()
									.sortierungWartungslisteAnpassenBeiEinfuegenEinerPositionVorPosition(
											internalFrameStueckliste
													.getTabbedPaneInstandhaltung()
													.getGeraetDto().getIId(),
											iSortAktuellePosition.intValue());
						}
						// Die neue Position wird an frei gemachte Position
						// gesetzt
						wartungslisteDto.setISort(iSortAktuellePosition);
					}

					wartungslisteDto.setIId(DelegateFactory.getInstance()
							.getInstandhaltungDelegate()
							.createWartungsliste(wartungslisteDto));
					setKeyWhenDetailPanel(wartungslisteDto.getIId());
					wartungslisteDto = DelegateFactory
							.getInstance()
							.getInstandhaltungDelegate()
							.wartungslisteFindByPrimaryKey(
									wartungslisteDto.getIId());
				} else {
					DelegateFactory.getInstance().getInstandhaltungDelegate()
							.updateWartungsliste(wartungslisteDto);
				}
				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							internalFrameStueckliste
									.getTabbedPaneInstandhaltung()
									.getGeraetDto().getIId()
									+ "");
				}
				eventYouAreSelected(false);
			}

		} finally {
			bFuegeNeuePositionVorDerSelektiertenEin = false;
		}

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

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
		wtfHandartikel.setMandatoryField(true);

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);

		wefText = new WrapperEditorFieldTexteingabe(getInternalFrame(),
				LPMain.getTextRespectUISPr("label.text"));

		wcbVerrechenbar.setText(LPMain.getTextRespectUISPr("lp.verrechenbar"));
		wcbWartungsmaterial.setText(LPMain
				.getTextRespectUISPr("is.wartungsmaterial"));
		wcbVerrechenbar.setMnemonic('A');
		wlaBezeichnung.setText(LPMain.getTextRespectUISPr("lp.bezeichnung"));

		wlaVeraltet = new WrapperLabel(
				LPMain.getTextRespectUISPr("is.wartungsliste.veraltet"));
		wlaVeraltetAb.setText(LPMain
				.getTextRespectUISPr("is.wartungsliste.veraltetab"));
		wlaVeraltetAb.setHorizontalAlignment(SwingConstants.LEFT);

		wlaMenge.setText(LPMain.getTextRespectUISPr("lp.menge"));
		wlaPositionsart.setText(LPMain.getTextRespectUISPr("lp.positionsart"));

		wnfMenge.setMandatoryField(true);

		int iNachkommastellenMenge = Defaults.getInstance()
				.getIUINachkommastellenMenge();

		wnfMenge.setFractionDigits(iNachkommastellenMenge);

		wtfHandartikel.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ARTIKELBEZEICHNUNG);
		getInternalFrame().addItemChangedListener(this);
		wcbPositionsart
				.addActionListener(new PanelWartungsliste_wcbPositionsart_actionAdapter(
						this));
		wcbPositionsart.setMandatoryField(true);

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

		jpaWorkingOn.add(wlaPositionsart, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wcbPositionsart, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(new WrapperLabel(), new GridBagConstraints(2, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));

		iZeile++;

		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		wifArtikel.getWtfIdent().setUppercaseField(true);
		jpaWorkingOn.add(wifArtikel.getKundenidentnummerButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
				new GridBagConstraints(2, iZeile, 3, 1, 1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), -45, 0));

		iZeile++;
		jpaWorkingOn.add(wcbVerrechenbar, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcbWartungsmaterial, new GridBagConstraints(1, iZeile,
				1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaVeraltet, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVeraltete, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaVeraltetAb, new GridBagConstraints(2, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfVeraltet, new GridBagConstraints(3, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaVeraltetPerson, new GridBagConstraints(4, iZeile,
				1, 1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, ACTION_TEXT };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_STUECKLISTE;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();

			if (key != null && key.equals(LPMain.getLockMeForNew())) {

				wnfMenge.setBigDecimal(new java.math.BigDecimal(1));

				if (letzterArtikel != null) {
					wifArtikel.getWtfIdent().setText(letzterArtikel.getCNr());

				}

			}
		} else {
			wartungslisteDto = DelegateFactory.getInstance()
					.getInstandhaltungDelegate()
					.wartungslisteFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}

	public void wcbPositionsart_actionPerformed(ActionEvent e) {
		String key = (String) wcbPositionsart.getKeyOfSelectedItem();
		if (key.equals(LocaleFac.POSITIONSART_IDENT)) {
			wtfHandartikel.setText(null);
			jpaWorkingOn.remove(wlaBezeichnung);
			jpaWorkingOn.remove(wtfHandartikel);
			wtfHandartikel.setEditable(true);

			jpaWorkingOn.add(wifArtikel.getKundenidentnummerButton(),
					new GridBagConstraints(0, 1, 1, 1, 0, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.NONE,
							new Insets(2, 2, 2, 2), 10, 0));
			jpaWorkingOn.add(wifArtikel.getWbuArtikel(),
					new GridBagConstraints(0, 1, 1, 1, 0, 0.0,
							GridBagConstraints.EAST,
							GridBagConstraints.HORIZONTAL, new Insets(2, 22, 2,
									2), 0, 0));
			jpaWorkingOn.repaint();
			jpaWorkingOn.add(wifArtikel.getWtfIdent(),
					new GridBagConstraints(1, 1, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
					new GridBagConstraints(2, 1, 4, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

		} else if (key.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			wifArtikel.getWtfIdent().setText(null);
			wtfHandartikel.setActivatable(true);
			if (wartungslisteDto != null) {
				wartungslisteDto.setArtikelIId(null);
			}
			jpaWorkingOn.remove(wifArtikel.getKundenidentnummerButton());
			jpaWorkingOn.remove(wifArtikel.getWbuArtikel());
			jpaWorkingOn.remove(wifArtikel.getWtfIdent());
			jpaWorkingOn.remove(wifArtikel.getWtfBezeichnung());
			jpaWorkingOn.add(wlaBezeichnung,
					new GridBagConstraints(0, 1, 1, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wtfHandartikel,
					new GridBagConstraints(1, 1, 5, 1, 0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.repaint();
			jpaWorkingOn.revalidate();
		}

	}

}

class PanelWartungsliste_wcbPositionsart_actionAdapter implements
		ActionListener {
	private PanelWartungsliste adaptee;

	PanelWartungsliste_wcbPositionsart_actionAdapter(PanelWartungsliste adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcbPositionsart_actionPerformed(e);
	}
}
