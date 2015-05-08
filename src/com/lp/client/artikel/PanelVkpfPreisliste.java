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

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.server.artikel.service.VkpfartikelpreislisteDto;
import com.lp.server.artikel.service.WebshopDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Diese Klasse kuemmert sich um das Panel zur Erfassung von Preislisten. <br>
 * Preislisten sind mandantenabhaengig. <br>
 * Es koennen maximal 10 Preislisten pro Mandant erfasst werden.</I>
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellungsdatum <I>29. 09. 04</I>
 * </p>
 * 
 * @author $Author: christian $
 * @version $Revision: 1.8 $ Date $Date: 2012/10/09 08:01:39 $
 */
public class PanelVkpfPreisliste extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InternalFrameArtikel intFrame = null;

	/** Wenn true, dann die neue Position vor der aktuell markierten einfuegen. */
	protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;

	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jPanelWorkingOn = null;
	private GridBagLayout gridBagLayoutWorkingOn = null;
	private Border innerBorder = null;

	private WrapperLabel wlaPreislistenname = null;
	private WrapperTextField wtfPreislistenname = null;
	private WrapperCheckBox wcbPreislisteaktiv = null;

	private WrapperLabel wlaStandardrabattsatz = null;
	private WrapperNumberField wnfStandardrabattsatz = new WrapperNumberField();
	private WrapperButton wbuWebshop = new WrapperButton();
	private WrapperTextField wtfWebshop = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRWebshop = null;

	static final public String ACTION_SPECIAL_WEBSHOP_FROM_LISTE = "action_webshop_from_liste";

	private WrapperLabel wlaFremdsystemnummer = null;
	private WrapperTextField wtfFremdsystemnummer = null;

	private WrapperLabel wlaWaehrung = null;
	private WrapperComboBox wcoWaehrung = null;

	private int iAnzahlPreislisten = -1;

	public PanelVkpfPreisliste(InternalFrame internalFrame, String add2TitleI,
			Object key) throws Throwable {
		super(internalFrame, add2TitleI, key);

		intFrame = (InternalFrameArtikel) internalFrame;

		jbInit();
		initComponents();
		initPanel();
	}

	private void initPanel() throws Throwable {
		wcoWaehrung.setMap(DelegateFactory.getInstance().getLocaleDelegate()
				.getAllWaehrungen());
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		setLayout(gridBagLayoutAll);

		innerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		setBorder(innerBorder);

		wbuWebshop.setText(LPMain.getTextRespectUISPr("lp.webshop") + "...");
		wbuWebshop
				.setActionCommand(PanelShopgruppewebshop.ACTION_SPECIAL_WEBSHOP_FROM_LISTE);
		wbuWebshop.addActionListener(this);

		getInternalFrame().addItemChangedListener(this);

		wtfWebshop.setActivatable(false);
		wtfWebshop.setColumnsMax(100);

		// Actionpanel setzen und anhaengen
		JPanel panelButtonAction = getToolsPanel();
		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));

		// zusaetzliche buttons
		String[] aWhichButtonIUse = { PanelBasis.ACTION_UPDATE,
				PanelBasis.ACTION_SAVE, PanelBasis.ACTION_DELETE,
				PanelBasis.ACTION_DISCARD };
		enableToolsPanelButtons(aWhichButtonIUse);

		wtfFremdsystemnummer = new WrapperTextField();
		wtfFremdsystemnummer.setColumnsMax(15);

		// Workingpanel
		jPanelWorkingOn = new JPanel();
		gridBagLayoutWorkingOn = new GridBagLayout();
		jPanelWorkingOn.setLayout(gridBagLayoutWorkingOn);
		add(jPanelWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		wlaPreislistenname = new WrapperLabel(
				LPMain.getTextRespectUISPr("vkpf.preisliste"));
		wtfPreislistenname = new WrapperTextField();
		wtfPreislistenname.setMandatoryField(true);
		wcbPreislisteaktiv = new WrapperCheckBox(
				LPMain.getTextRespectUISPr("vkpf.preislisteaktiv"));
		wlaWaehrung = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.waehrung"));
		wcoWaehrung = new WrapperComboBox();
		wcoWaehrung.setMandatoryFieldDB(true);

		wlaStandardrabattsatz = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.standardrabattsatz") + " (%)");

		wnfStandardrabattsatz.setFractionDigits(2);
		wnfStandardrabattsatz.setMaximumValue(100);

		wlaFremdsystemnummer = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.fremdsystemnummer"));

		// Zeile
		jPanelWorkingOn.add(wlaPreislistenname, new GridBagConstraints(0,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfPreislistenname, new GridBagConstraints(1,
				iZeile, 1, 1, 0.5, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wcbPreislisteaktiv, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaWaehrung, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jPanelWorkingOn.add(wcoWaehrung, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaFremdsystemnummer, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfFremdsystemnummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 100, 0));
		// Zeile
		iZeile++;
		jPanelWorkingOn.add(wlaStandardrabattsatz, new GridBagConstraints(0,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wnfStandardrabattsatz, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), -30, 0));
		iZeile++;
		jPanelWorkingOn.add(wbuWebshop, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jPanelWorkingOn.add(wtfWebshop, new GridBagConstraints(1, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	private void setDefaults() throws Throwable {
		intFrame.getTabbedPaneVkpfPreislistenname().resetDtos();
		leereAlleFelder(this);
		wcbPreislisteaktiv.setSelected(true);
		iAnzahlPreislisten = DelegateFactory.getInstance()
				.getVkPreisfindungDelegate()
				.vkpfartikelpreislisteFindByMandantCNr().length;
		wcoWaehrung.setKeyOfSelectedItem(LPMain.getTheClient()
				.getSMandantenwaehrung());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		try {
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();

				// eine Preisliste kann nicht deaktiviert werden, solange ein
				// Kunde darauf
				// referenziert
				if (intFrame.getTabbedPaneVkpfPreislistenname()
						.getVkpfartikelpreislisteDto().getIId() != null
						&& DelegateFactory
								.getInstance()
								.getKundeDelegate()
								.kundeFindByVkpfArtikelpreislisteIIdStdpreislisteOhneExc(
										intFrame.getTabbedPaneVkpfPreislistenname()
												.getVkpfartikelpreislisteDto()
												.getIId()).length > 0
						&& !wcbPreislisteaktiv.isSelected()) {
					DialogFactory
							.showModalDialog(
									LPMain.getTextRespectUISPr("lp.warning"),
									LPMain.getTextRespectUISPr("vkpf.preislistekannnichtdeaktiviertwerden"));
				} else if (intFrame.getTabbedPaneVkpfPreislistenname()
						.getVkpfartikelpreislisteDto().getIId() == null) {
					// soll die neue Positione vor der aktuell selektierten
					// eingefuegt werden?
					if (bFuegeNeuePositionVorDerSelektiertenEin) {
						Integer iIdAktuellePosition = (Integer) ((InternalFrameArtikel) getInternalFrame())
								.getTabbedPaneVkpfPreislistenname()
								.getPreislistennameTop().getSelectedId();

						int iSortAktuellePosition = DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.vkpfartikelpreislisteFindByPrimaryKey(
										iIdAktuellePosition).getISort()
								.intValue();

						// die bestehenden Positionen muessen Platz fuer die
						// neue schaffen
						DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
										iSortAktuellePosition);

						// die neue Position wird an frei die gemachte Position
						// gesetzt
						intFrame.getTabbedPaneVkpfPreislistenname()
								.getVkpfartikelpreislisteDto()
								.setISort(new Integer(iSortAktuellePosition));
					}

					// einen neuen preislistennamen anlegen
					Integer pkPreislistenname = DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.createVkpfartikelpreisliste(
									intFrame.getTabbedPaneVkpfPreislistenname()
											.getVkpfartikelpreislisteDto());
					intFrame.getTabbedPaneVkpfPreislistenname()
							.getVkpfartikelpreislisteDto()
							.setIId(pkPreislistenname);
					setKeyWhenDetailPanel(pkPreislistenname);
				} else {

					VkpfartikelpreislisteDto dtoVorher = DelegateFactory
							.getInstance()
							.getVkPreisfindungDelegate()
							.vkpfartikelpreislisteFindByPrimaryKey(
									intFrame.getTabbedPaneVkpfPreislistenname()
											.getVkpfartikelpreislisteDto()
											.getIId());

					if (dtoVorher.getNStandardrabattsatz() != null
							&& intFrame.getTabbedPaneVkpfPreislistenname()
									.getVkpfartikelpreislisteDto()
									.getNStandardrabattsatz() != null
							&& dtoVorher.getNStandardrabattsatz().doubleValue() != intFrame
									.getTabbedPaneVkpfPreislistenname()
									.getVkpfartikelpreislisteDto()
									.getNStandardrabattsatz().doubleValue()) {

						boolean bAendern = DialogFactory
								.showModalJaNeinDialog(
										getInternalFrame(),
										LPMain.getTextRespectUISPr("artikel.preisliste.rabattsatzaendern"));

						if (bAendern == true) {

							java.sql.Date gueltigabNeu = DialogFactory
									.showDatumseingabe(LPMain
											.getTextRespectUISPr("artikel.preisliste.rabattsatzaendern.gueltigab"));
							if (gueltigabNeu == null) {
								bAendern = false;
							}

							DelegateFactory
									.getInstance()
									.getVkPreisfindungDelegate()
									.updateVkpfartikelpreisliste(
											intFrame.getTabbedPaneVkpfPreislistenname()
													.getVkpfartikelpreislisteDto(),
											bAendern, gueltigabNeu);

						} else {
							DelegateFactory
									.getInstance()
									.getVkPreisfindungDelegate()
									.updateVkpfartikelpreisliste(
											intFrame.getTabbedPaneVkpfPreislistenname()
													.getVkpfartikelpreislisteDto(),
											false, null);
						}

					} else {
						DelegateFactory
								.getInstance()
								.getVkPreisfindungDelegate()
								.updateVkpfartikelpreisliste(
										intFrame.getTabbedPaneVkpfPreislistenname()
												.getVkpfartikelpreislisteDto(),
										false, null);
					}

				}

				super.eventActionSave(e, true);

				eventYouAreSelected(false);
			}
		} finally {
			bFuegeNeuePositionVorDerSelektiertenEin = false;
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory
				.getInstance()
				.getVkPreisfindungDelegate()
				.removeVkpfartikelpreisliste(
						intFrame.getTabbedPaneVkpfPreislistenname()
								.getVkpfartikelpreislisteDto());

		super.eventActionDelete(e, false, false);
	}

	private void components2Dto() throws Throwable {
		if (intFrame.getTabbedPaneVkpfPreislistenname()
				.getVkpfartikelpreislisteDto() == null
				|| intFrame.getTabbedPaneVkpfPreislistenname()
						.getVkpfartikelpreislisteDto().getIId() == null) {
			Integer webshopIId = null;
			if (intFrame.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto() != null) {
				webshopIId = intFrame.getTabbedPaneVkpfPreislistenname()
						.getVkpfartikelpreislisteDto().getWebshopIId();
			}

			intFrame.getTabbedPaneVkpfPreislistenname()
					.setVkpfartikelpreislisteDto(new VkpfartikelpreislisteDto());
			intFrame.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto()
					.setMandantCNr(LPMain.getTheClient().getMandant());
			intFrame.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto().setWebshopIId(webshopIId);

			// posisort: 1 eine neue Position bekommt ein eindeutiges iSort
			int iSortNeu = DelegateFactory.getInstance()
					.getVkPreisfindungDelegate()
					.getMaxISort(LPMain.getTheClient().getMandant()).intValue() + 1;
			intFrame.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto()
					.setISort(new Integer(iSortNeu));
		}

		intFrame.getTabbedPaneVkpfPreislistenname()
				.getVkpfartikelpreislisteDto()
				.setCNr(wtfPreislistenname.getText());
		intFrame.getTabbedPaneVkpfPreislistenname()
				.getVkpfartikelpreislisteDto()
				.setBPreislisteaktiv(
						Helper.boolean2Short(wcbPreislisteaktiv.isSelected()));
		intFrame.getTabbedPaneVkpfPreislistenname()
				.getVkpfartikelpreislisteDto()
				.setWaehrungCNr((String) wcoWaehrung.getKeyOfSelectedItem());
		intFrame.getTabbedPaneVkpfPreislistenname()
				.getVkpfartikelpreislisteDto()
				.setCFremdsystemnr(wtfFremdsystemnummer.getText());

		intFrame.getTabbedPaneVkpfPreislistenname()
				.getVkpfartikelpreislisteDto()
				.setNStandardrabattsatz(wnfStandardrabattsatz.getBigDecimal());

	}

	private void dto2Components() throws Throwable {
		if (intFrame.getTabbedPaneVkpfPreislistenname()
				.getVkpfartikelpreislisteDto() != null
				&& intFrame.getTabbedPaneVkpfPreislistenname()
						.getVkpfartikelpreislisteDto().getIId() != null) {
			// int iNummer = preislistennameDto.getISort().intValue() + 1;
			// wtfPreislistenname.setText(iNummer + " " +
			// preislistennameDto.getCNr());
			wtfPreislistenname.setText(intFrame
					.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto().getCNr());
			wcbPreislisteaktiv.setSelected(Helper.short2boolean(intFrame
					.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto().getBPreislisteaktiv()));
			wcoWaehrung.setKeyOfSelectedItem(intFrame
					.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto().getWaehrungCNr());
			wtfFremdsystemnummer.setText(intFrame
					.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto().getCFremdsystemnr());

			wnfStandardrabattsatz.setBigDecimal(intFrame
					.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto().getNStandardrabattsatz());

			if (intFrame.getTabbedPaneVkpfPreislistenname()
					.getVkpfartikelpreislisteDto().getWebshopIId() != null) {
				WebshopDto webshopDto = DelegateFactory
						.getInstance()
						.getArtikelDelegate()
						.webshopFindByPrimaryKey(
								intFrame.getTabbedPaneVkpfPreislistenname()
										.getVkpfartikelpreislisteDto()
										.getWebshopIId());

				wtfWebshop.setText(webshopDto.getCBez());
			} else {
				wtfWebshop.setText(null);
			}

		}
	}

	void dialogQueryWebshopFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH,
				PanelBasis.ACTION_LEEREN };

		panelQueryFLRWebshop = new PanelQueryFLR(null, SystemFilterFactory
				.getInstance().createFKMandantCNr(),
				QueryParameters.UC_ID_WEBSHOP, aWhichButtonIUse,
				getInternalFrame(), LPMain.getTextRespectUISPr("lp.webshop"));

		panelQueryFLRWebshop.befuellePanelFilterkriterienDirekt(
				new FilterKriteriumDirekt("c_bez", "",
						FilterKriterium.OPERATOR_LIKE, LPMain
								.getTextRespectUISPr("lp.webshop"),
						FilterKriteriumDirekt.PROZENT_TRAILING, true, true,
						Facade.MAX_UNBESCHRAENKT), null);

		panelQueryFLRWebshop.setSelectedId(intFrame
				.getTabbedPaneVkpfPreislistenname()
				.getVkpfartikelpreislisteDto().getWebshopIId());

		new DialogQuery(panelQueryFLRWebshop);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRWebshop) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();

				WebshopDto webshopDto = DelegateFactory.getInstance()
						.getArtikelDelegate()
						.webshopFindByPrimaryKey((Integer) key);

				wtfWebshop.setText(webshopDto.getCBez());
				intFrame.getTabbedPaneVkpfPreislistenname()
						.getVkpfartikelpreislisteDto()
						.setWebshopIId((Integer) key);
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRWebshop) {
				wtfWebshop.setText(null);
				intFrame.getTabbedPaneVkpfPreislistenname()
						.getVkpfartikelpreislisteDto().setWebshopIId(null);
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_WEBSHOP_FROM_LISTE)) {
			dialogQueryWebshopFromListe(e);
		}
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		setDefaults();

		if (iAnzahlPreislisten == 10) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_VKPF_MAXIMALZEHNPREISLISTEN,
					new Exception("Anzahl Preislisten = 10"));
		}

		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			// Dieses Flag gibt an, ob die neue Position vor der aktuellen
			// eingefuegt werden soll
			bFuegeNeuePositionVorDerSelektiertenEin = true;
		}

		intFrame.getTabbedPaneVkpfPreislistenname().setTitlePreisliste(
				"lp.detail");
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		setDefaults();

		// Key neu einlesen, Ausloeser war ev. ein Refresh oder Discard
		Object oKey = getKeyWhenDetailPanel();

		if (oKey != null && !oKey.equals(LPMain.getLockMeForNew())) {
			intFrame.getTabbedPaneVkpfPreislistenname()
					.setVkpfartikelpreislisteDto(
							DelegateFactory
									.getInstance()
									.getVkPreisfindungDelegate()
									.vkpfartikelpreislisteFindByPrimaryKey(
											(Integer) oKey));

			dto2Components();
		}

		intFrame.getTabbedPaneVkpfPreislistenname().setTitlePreisliste(
				LPMain.getTextRespectUISPr("lp.detail"));

		iAnzahlPreislisten = DelegateFactory.getInstance()
				.getVkPreisfindungDelegate()
				.vkpfartikelpreislisteFindByMandantCNr().length;
	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 * 
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		super.eventActionDiscard(e);

		// bei Discard zuruecksetzen
		bFuegeNeuePositionVorDerSelektiertenEin = false;
	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_VKPF_PREISLISTENNAME;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfPreislistenname;
	}
}
