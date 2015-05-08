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
package com.lp.client.eingangsrechnung;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


import com.lp.client.auftrag.AuftragFilterFactory;
import com.lp.client.frame.Defaults;
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
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.eingangsrechnung.service.EingangsrechnungAuftragszuordnungDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
/**
 * <p>Panel zum Bearbeiten der Auftragszuordnungen einer ER</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>20. 02. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.7 $
 */
public class PanelEingangsrechnungAuftragszuordnung extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung = null;
	private EingangsrechnungAuftragszuordnungDto erAzDto = null;
	private AuftragDto auftragDto = null;
	private final static String ACTION_SPECIAL_AUFTRAG = "action_special_er_auftrag";
	private final static String ACTION_SPECIAL_REST = "action_special_er_rest";
	private PanelQueryFLR panelQueryFLRAuftrag = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private WrapperTextField wtfAuftragNummer = new WrapperTextField();
	private WrapperTextField wtfAuftragBezeichnung = new WrapperTextField();
	private WrapperButton wbuAuftrag = new WrapperButton();
	private WrapperNumberField wnfBetrag = new WrapperNumberField();
	private WrapperNumberField wnfBetragOffen = new WrapperNumberField();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private WrapperLabel wlaAbstand1 = new WrapperLabel();
	private WrapperLabel wlaAbstand2 = new WrapperLabel();
	private WrapperLabel wlaAbstand3 = new WrapperLabel();
	private WrapperLabel wlaBetrag = new WrapperLabel();
	private WrapperLabel wlaBetragOffen = new WrapperLabel();
	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperLabel wlaWaehrung1 = new WrapperLabel();
	private WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperTextField wtfText = new WrapperTextField();
	private WrapperButton wbuRest = new WrapperButton();
	private WrapperCheckBox wcbKeineAuftragswertung = new WrapperCheckBox();
	private Border border1;

	public PanelEingangsrechnungAuftragszuordnung(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneEingangsrechnung tabbedPaneEingangsrechnung)
			throws Throwable {
		super(internalFrame, add2TitleI);
		this.tabbedPaneEingangsrechnung = tabbedPaneEingangsrechnung;
		jbInit();
		initComponents();
	}

	private TabbedPaneEingangsrechnung getTabbedPane() {
		return tabbedPaneEingangsrechnung;
	}

	/**
	 * Die Klasse initialisieren.
	 * 
	 * @throws Throwable
	 */
	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);
		JPanel panelButtonAction = getToolsPanel();
		// wegen Dialogauswahl auf FLR events hoeren
		getInternalFrame().addItemChangedListener(this);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(gridBagLayout1);
		wtfAuftragNummer.setMandatoryField(true);
		wbuAuftrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.auftrag"));
		wbuAuftrag.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"button.auftrag.tooltip"));
		wbuRest.setText(LPMain.getInstance().getTextRespectUISPr(
				"er.button.rest"));
		wbuRest.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"er.button.rest.tooltip"));
		wnfBetrag.setMandatoryField(true);
		wnfBetragOffen.setActivatable(false);

		wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.betrag"));
		wlaBetragOffen.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.offen"));
		wlaText.setText(LPMain.getInstance().getTextRespectUISPr("label.text"));
		
		wcbKeineAuftragswertung.setText(LPMain.getInstance().getTextRespectUISPr("er.auftragszuordnung.keineauftragswertung"));
		
		
		jpaWorkingOn.setLayout(gridBagLayout3);
		wlaAbstand1.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand1.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand2.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand2.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand3.setMinimumSize(new Dimension(40, Defaults.getInstance()
				.getControlHeight()));
		wlaAbstand3.setPreferredSize(new Dimension(40, Defaults.getInstance()
				.getControlHeight()));
		jpaWorkingOn.setBorder(border1);
		wtfAuftragBezeichnung.setActivatable(false);
		wtfAuftragNummer.setActivatable(false);
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wnfBetrag.setMinimumValue(new BigDecimal(0));

		wtfAuftragBezeichnung.setMandatoryField(false);
		wbuAuftrag.setActionCommand(ACTION_SPECIAL_AUFTRAG);
		wbuRest.setActionCommand(ACTION_SPECIAL_REST);
		wbuAuftrag.addActionListener(this);
		wbuRest.addActionListener(this);
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuAuftrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftragNummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuftragBezeichnung, new GridBagConstraints(2,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetrag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung1, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wcbKeineAuftragswertung, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 40, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wbuRest, new GridBagConstraints(3, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBetragOffen, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragOffen, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrung2, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaText, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfText, new GridBagConstraints(1, iZeile, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaAbstand1, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaAbstand2, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaAbstand3, new GridBagConstraints(2, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_EINGANGSRECHNUNG;
	}

	public void eventActionNew(EventObject e, boolean bChangeKeyLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		// locknew: 1 hier muss dafuer gesorgt werden, dass der Framework lockt
		super.eventActionNew(e, true, false);
		clearStatusbar();
		erAzDto = null;
		auftragDto = null;
		leereAlleFelder(this);

		Integer iId = getTabbedPane().getEingangsrechnungDto().getIId();
		BigDecimal betrag = DelegateFactory.getInstance()
				.getEingangsrechnungDelegate()
				.getWertNochNichtZuAuftraegenZugeordnet(iId);
		wnfBetrag.setMaximumValue(betrag);
		// Restwert vorschlagen
		wnfBetrag.setBigDecimal(betrag);
	}

	/**
	 * Loeschen einer Rechnungsposition
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.erAzDto != null) {
			if (erAzDto.getIId() != null) {
				if (!isLockedDlg()) {
					DelegateFactory.getInstance().getEingangsrechnungDelegate()
							.removeEingangsrechnungAuftragszuordnung(erAzDto);
					this.erAzDto = null;
					// selectafterdelete: key null setzen
					this.setKeyWhenDetailPanel(null);
					this.leereAlleFelder(this);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
		wnfBetrag.setMaximumValue(erAzDto.getNBetrag().add(
				DelegateFactory
						.getInstance()
						.getEingangsrechnungDelegate()
						.getWertNochNichtZuAuftraegenZugeordnet(
								erAzDto.getEingangsrechnungIId())));
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (erAzDto != null) {
				EingangsrechnungAuftragszuordnungDto savedDto = DelegateFactory
						.getInstance().getEingangsrechnungDelegate()
						.updateEingangsrechnungAuftragszuordnung(erAzDto);
				this.erAzDto = savedDto;
				setKeyWhenDetailPanel(erAzDto.getIId());

				// muss den hoechstwert fuer den betrag wieder wegtun
				wnfBetrag.setMaximumValue(getTabbedPane()
						.getEingangsrechnungDto().getNBetragfw());

				super.eventActionSave(e, true);
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							getTabbedPane().getEingangsrechnungDto().getIId()
									.toString());
				}
				// jetz den anzeigen
				eventYouAreSelected(false);
			}
		}
	}

	private void components2Dto() throws Throwable {
		if (erAzDto == null) {
			erAzDto = new EingangsrechnungAuftragszuordnungDto();
			erAzDto.setEingangsrechnungIId(getTabbedPane()
					.getEingangsrechnungDto().getIId());
		}
		if (auftragDto != null) {
			erAzDto.setAuftragIId(auftragDto.getIId());
		} else {
			erAzDto.setAuftragIId(null);
		}
		erAzDto.setNBetrag(wnfBetrag.getBigDecimal());
		erAzDto.setCText(wtfText.getText());
		erAzDto.setBKeineAuftragswertung(wcbKeineAuftragswertung.getShort());

	}

	private void dto2Components() throws Throwable {
		if (getKeyWhenDetailPanel() != null) {
			erAzDto = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungAuftragszuordnungFindByPrimaryKey(
							(Integer) getKeyWhenDetailPanel());
		}

		if (getTabbedPane().getEingangsrechnungDto().getNBetragfw()
				.doubleValue() < 0) {
			wnfBetrag.setMaximumValue(0);
			wnfBetrag.setMinimumValue(getTabbedPane().getEingangsrechnungDto()
					.getNBetragfw());
		} else {
			wnfBetrag.setMaximumValue(getTabbedPane().getEingangsrechnungDto()
					.getNBetragfw());
			wnfBetrag.setMinimumValue(0);
		}

		if (erAzDto != null) {
			holeAuftrag(erAzDto.getAuftragIId());
			dto2ComponentsAuftrag();
			// den maximalwert setzen, denn der ist ja 0, wenn alles zugeordnet
			// ist

			wnfBetrag.setBigDecimal(erAzDto.getNBetrag());
			wtfText.setText(erAzDto.getCText());
			wnfBetragOffen.setBigDecimal(DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.getWertNochNichtZuAuftraegenZugeordnet(
							getTabbedPane().getEingangsrechnungDto().getIId()));
			wcbKeineAuftragswertung
					.setShort(erAzDto.getBKeineAuftragswertung());
			this.setStatusbarPersonalIIdAnlegen(erAzDto.getPersonalIIdAnlegen());
			this.setStatusbarTAnlegen(erAzDto.getTAnlegen());
			this.setStatusbarPersonalIIdAendern(erAzDto.getPersonalIIdAendern());
			this.setStatusbarTAendern(erAzDto.getTAendern());
		}
	}

	/**
	 * dto2ComponentsAuftrag
	 */
	private void dto2ComponentsAuftrag() {
		if (auftragDto != null) {
			wtfAuftragNummer.setText(auftragDto.getCNr());
			wtfAuftragBezeichnung.setText(auftragDto
					.getCBezProjektbezeichnung());
		} else {
			wtfAuftragNummer.setText(null);
			wtfAuftragBezeichnung.setText(null);
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_AUFTRAG)) {
			dialogQueryAuftrag(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_REST)) {
			wnfBetrag.setBigDecimal(wnfBetragOffen.getBigDecimal());
		}
	}

	void dialogQueryAuftrag(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		QueryType[] qt = null;
		FilterKriterium[] fk = SystemFilterFactory.getInstance()
				.createFKMandantCNr();

		panelQueryFLRAuftrag = new PanelQueryFLR(qt, fk,
				QueryParameters.UC_ID_AUFTRAG, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"title.auftragauswahlliste"));
		panelQueryFLRAuftrag.befuellePanelFilterkriterienDirektUndVersteckte(
				AuftragFilterFactory.getInstance().createFKDAuftragnummer(),
				AuftragFilterFactory.getInstance().createFKDKundenname(),
				AuftragFilterFactory.getInstance().createFKVAuftrag());
		new DialogQuery(panelQueryFLRAuftrag);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRAuftrag) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
						.auftragFindByPrimaryKey((Integer) key);

				if (auftragDto.getStatusCNr().equals(
						AuftragServiceFac.AUFTRAGSTATUS_ERLEDIGT)) {
					if (DelegateFactory
							.getInstance()
							.getTheJudgeDelegate()
							.hatRecht(
									com.lp.server.benutzer.service.RechteFac.RECHT_AUFT_DARF_AUFTRAG_ERLEDIGEN)) {

						boolean b = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"er.auftragszuordnung.frage.erleidgte"));
						if (b == true) {
							holeAuftrag(key);
						}

					} else {
						DialogFactory
								.showModalDialog(
										LPMain.getInstance()
												.getTextRespectUISPr("lp.error"),
										LPMain.getInstance()
												.getTextRespectUISPr(
														"er.auftragszuordnung.error.auftragerledigt"));
					}

				} else {
					holeAuftrag(key);
				}

			}
		}
	}

	private void holeAuftrag(Object key) throws Throwable {
		if (key != null) {
			auftragDto = DelegateFactory.getInstance().getAuftragDelegate()
					.auftragFindByPrimaryKey((Integer) key);
		} else {
			auftragDto = null;
		}
		dto2ComponentsAuftrag();
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (getTabbedPane().getEingangsrechnungDto().getNBetragfw()
				.doubleValue() < 0) {
			wnfBetrag.setMaximumValue(0);
			wnfBetrag.setMinimumValue(getTabbedPane().getEingangsrechnungDto()
					.getNBetragfw());
		} else {
			wnfBetrag.setMaximumValue(getTabbedPane().getEingangsrechnungDto()
					.getNBetragfw());
			wnfBetrag.setMinimumValue(0);
		}
		wlaWaehrung1.setText(getTabbedPane().getEingangsrechnungDto()
				.getWaehrungCNr());
		wlaWaehrung2.setText(getTabbedPane().getEingangsrechnungDto()
				.getWaehrungCNr());
		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
			// einen neuen Eintrag anlegen oder die letzte Position wurde
			// geloescht.

			leereAlleFelder(this);
			

			wnfBetragOffen.setBigDecimal(DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.getWertNochNichtZuAuftraegenZugeordnet(
							getTabbedPane().getEingangsrechnungDto().getIId()));
			clearStatusbar();
		} else {
			// einen alten Eintrag laden.
			erAzDto = DelegateFactory
					.getInstance()
					.getEingangsrechnungDelegate()
					.eingangsrechnungAuftragszuordnungFindByPrimaryKey(
							(Integer) key);
			dto2Components();
			getTabbedPane().enablePanels();
		}
		if (key != null && key.equals(LPMain.getLockMeForNew())) {
			// wenn nix drinsteht, dann den restwert vorschlagen
			wnfBetrag.setBigDecimal(wnfBetragOffen.getBigDecimal());
		}
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuAuftrag;
	}

	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {
		super.eventActionRefresh(e, bNeedNoRefreshI);
		getTabbedPane().enablePanels();
	}

	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		super.eventActionDiscard(e);
		getTabbedPane().enablePanels();
	}
}
