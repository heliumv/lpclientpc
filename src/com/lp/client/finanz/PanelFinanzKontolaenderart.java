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
package com.lp.client.finanz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.FLRKontolaenderartPK;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontolaenderartDto;
import com.lp.server.finanz.service.LaenderartDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/*
 * * <p> Diese Klasse kuemmert sich um die Kontolaenderart</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Martin Bluehweis; 30.09.05</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2012/09/17 13:14:34 $
 */
public class PanelFinanzKontolaenderart extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;

	private WrapperButton wbuKonto = null;
	private WrapperTextNumberField wtnfKontonummer = null;
	private WrapperTextField wtfKontobezeichnung = null;

	private WrapperButton wbuLaenderart = null;
	private WrapperTextField wtfLaenderart = null;

	private WrapperButton wbuFinanzamt = null;
	private WrapperTextField wtfFinanzamt = null;

	private final static String ACTION_SPECIAL_LAENDERART = "action_special_laenderart";
	private final static String ACTION_SPECIAL_KONTO = "action_special_konto";
	private final static String ACTION_SPECIAL_FINANZAMT = "action_special_finanzamt";

	private PanelQueryFLR panelQueryFLRLaenderart = null;
	private PanelQueryFLR panelQueryFLRKonto = null;
	private PanelQueryFLR panelQueryFLRFinanzamt = null;

	private KontolaenderartDto klDto = null;
	private KontoDto kontoDto = null;
	private LaenderartDto laenderartDto = null;
	private FinanzamtDto finanzamtDto = null;

	private TabbedPaneKonten tabbedPaneKonten = null;

	public PanelFinanzKontolaenderart(InternalFrame internalFrame,
			String add2TitleI, Object pk, TabbedPaneKonten tabbedPaneKonten)
			throws Throwable {
		super(internalFrame, add2TitleI, pk);
		this.tabbedPaneKonten = tabbedPaneKonten;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void setDefaults() throws Throwable {
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		klDto = null;
		leereAlleFelder(this);
		setDefaults();
		clearStatusbar();
	}

	private void dto2Components() throws Throwable {
		if (klDto != null) {
			holeLaenderart(klDto.getLaenderartCNr());
			holeKonto(klDto.getKontoIIdUebersetzt());
			holeFinanzamt(klDto.getFinanzamtIId(), klDto.getMandantCNr());
			// StatusBar
			this.setStatusbarPersonalIIdAnlegen(klDto.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(klDto.getTAnlegen());
			this.setStatusbarPersonalIIdAendern(klDto.getPersonalIIdAendern());
			this.setStatusbarTAendern(klDto.getTAendern());
		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			FLRKontolaenderartPK kontolaenderartPK = (FLRKontolaenderartPK) key;
			klDto = DelegateFactory.getInstance().getFinanzDelegate()
					.kontolaenderartFindByPrimaryKey(
							kontolaenderartPK.getKonto_i_id(),
							kontolaenderartPK.getLaenderart_c_nr(),
							kontolaenderartPK.getFinanzamt_i_id(),
							kontolaenderartPK.getMandant_c_nr());
			dto2Components();
		}
	}

	private void components2Dto() throws Throwable {
		if (klDto == null) {
			klDto = new KontolaenderartDto();
			klDto.setKontoIId(tabbedPaneKonten.getKontoDto().getIId());
		}
		klDto.setLaenderartCNr(laenderartDto.getCNr());
		klDto.setKontoIIdUebersetzt(kontoDto.getIId());
		klDto.setFinanzamtIId(finanzamtDto.getPartnerIId());
		klDto.setMandantCNr(finanzamtDto.getMandantCNr());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			DelegateFactory.getInstance().getFinanzDelegate()
					.updateKontolaenderart(klDto);
			setKeyWhenDetailPanel(new FLRKontolaenderartPK(klDto.getKontoIId(),
					klDto.getLaenderartCNr(), klDto.getFinanzamtIId(), klDto
							.getMandantCNr()));
			super.eventActionSave(e, true);
			eventYouAreSelected(false);
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getFinanzDelegate()
				.removeKontolaenderart(klDto);
		super.eventActionDelete(e, false, false);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLaenderart) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeLaenderart((String) key);
			} else if (e.getSource() == panelQueryFLRKonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeKonto((Integer) key);
			} else if (e.getSource() == panelQueryFLRFinanzamt) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				holeFinanzamt((Integer) key, LPMain.getTheClient().getMandant());
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

		getInternalFrame().addItemChangedListener(this);

		wbuKonto = new WrapperButton();
		wbuLaenderart = new WrapperButton();
		wtfKontobezeichnung = new WrapperTextField();
		wtfLaenderart = new WrapperTextField();
		wtnfKontonummer = new WrapperTextNumberField();
		wbuFinanzamt = new WrapperButton();
		wtfFinanzamt = new WrapperTextField();

		wtfKontobezeichnung.setActivatable(false);
		wtfLaenderart.setActivatable(false);
		wtnfKontonummer.setActivatable(false);
		wtfFinanzamt.setActivatable(false);

		wtnfKontonummer.setMandatoryFieldDB(true);
		wtfLaenderart.setMandatoryFieldDB(true);
		wtfFinanzamt.setMandatoryFieldDB(true);

		wbuKonto.addActionListener(this);
		wbuLaenderart.addActionListener(this);
		wbuFinanzamt.addActionListener(this);
		wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
		wbuLaenderart.setActionCommand(ACTION_SPECIAL_LAENDERART);
		wbuFinanzamt.setActionCommand(ACTION_SPECIAL_FINANZAMT);

		wbuLaenderart.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.laenderart"));
		wbuLaenderart.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.laenderart.tooltip"));

		wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto"));
		wbuKonto.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto.tooltip"));
		wbuKonto.setMinimumSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));
		wbuKonto.setPreferredSize(new Dimension(120, Defaults.getInstance()
				.getControlHeight()));

		wbuFinanzamt.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.finanzamt"));
		wbuFinanzamt.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.finanzamt.tooltip"));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuFinanzamt, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfFinanzamt, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuLaenderart, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLaenderart, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKonto, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtnfKontonummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontobezeichnung, new GridBagConstraints(2, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };
		enableToolsPanelButtons(aWhichButtonIUse);
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_FINANZ_KONTO;
	}

	/**
	 * holeKonto.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeKonto(Integer key) throws Throwable {
		if (key != null) {
			this.kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
					.kontoFindByPrimaryKey(key);
			dto2ComponentsKonto();
		}
	}

	/**
	 * holeLaenderart.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeLaenderart(String key) throws Throwable {
		if (key != null) {
			this.laenderartDto = DelegateFactory.getInstance()
					.getFinanzServiceDelegate().laenderartFindByPrimaryKey(key);
			dto2ComponentsLaenderart();
		}
	}

	/**
	 * holeFinanzamt.
	 * 
	 * @param key
	 *            Object
	 * @throws Throwable
	 */
	private void holeFinanzamt(Integer finanzamtIId, String mandantCNr)
			throws Throwable {
		if (finanzamtIId != null && mandantCNr != null) {
			this.finanzamtDto = DelegateFactory.getInstance()
					.getFinanzDelegate().finanzamtFindByPartnerIIdMandantCNr(
							finanzamtIId, mandantCNr);
			dto2ComponentsFinanzamt();
		}
	}

	private void dto2ComponentsKonto() {
		if (kontoDto != null) {
			wtnfKontonummer.setText(kontoDto.getCNr());
			wtfKontobezeichnung.setText(kontoDto.getCBez());
		} else {
			wtnfKontonummer.setText(null);
			wtfKontobezeichnung.setText(null);
		}
	}

	private void dto2ComponentsLaenderart() {
		if (laenderartDto != null) {
			wtfLaenderart.setText(laenderartDto.getCNr());
		} else {
			wtfLaenderart.setText(null);
		}
	}

	private void dto2ComponentsFinanzamt() {
		if (finanzamtDto != null) {
			wtfFinanzamt.setText(finanzamtDto.getPartnerDto().formatName());
		} else {
			wtfFinanzamt.setText(null);
		}
	}

	private void dialogQueryKonto(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance()
				.createFKKonten(tabbedPaneKonten.getKontotyp());

		panelQueryFLRKonto = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"finanz.liste.konten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		 FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance()
			.createFKVKonto();
		panelQueryFLRKonto.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1,
				fkDirekt2, fkVersteckt);
		if (kontoDto != null) {
			panelQueryFLRKonto.setSelectedId(kontoDto.getIId());
		}
		new DialogQuery(panelQueryFLRKonto);
	}

	private void dialogQueryLaenderart(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
		QueryType[] qt = null;
		// Inland nicht uebersetzen
//		FilterKriterium[] filters = FinanzFilterFactory.getInstance()
//				.createFKLaenderartOhneInland();

		FilterKriterium[] filters = null;
		panelQueryFLRLaenderart = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_LAENDERART, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"finanz.tab.oben.laenderart.title"));
		if (laenderartDto != null) {
			panelQueryFLRLaenderart.setSelectedId(laenderartDto.getCNr());
		}
		new DialogQuery(panelQueryFLRLaenderart);
	}

	private void dialogQueryFinanzamt(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };
		QueryType[] qt = null;
		// Filter nach Mandant
		FilterKriterium[] filters = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		panelQueryFLRFinanzamt = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZAMT, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"finanz.liste.finanzaemter"));
		if (klDto != null) {
			if (klDto.getFinanzamtIId() != null) {
				panelQueryFLRFinanzamt.setSelectedId(klDto.getFinanzamtIId());
			}
		}
		new DialogQuery(panelQueryFLRFinanzamt);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
			dialogQueryKonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_LAENDERART)) {
			dialogQueryLaenderart(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FINANZAMT)) {
			dialogQueryFinanzamt(e);
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuFinanzamt;
	}
}
