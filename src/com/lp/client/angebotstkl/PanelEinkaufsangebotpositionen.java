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
package com.lp.client.angebotstkl;

import gnu.trove.TPrimitiveHash;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklpositionDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.angebotstkl.service.EinkaufsangebotDto;
import com.lp.server.angebotstkl.service.EinkaufsangebotpositionDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.stueckliste.service.MontageartDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.util.Helper;

public class PanelEinkaufsangebotpositionen extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private InternalFrameAngebotstkl internalFrameStueckliste = null;
	private WrapperTextField wtfHandartikel = new WrapperTextField();
	private WrapperTextField wtfHandartikel2 = new WrapperTextField();
	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();
	private WrapperLabel wlaBemerkung = new WrapperLabel();
	private WrapperTextField wtfBemerkung = new WrapperTextField();
	private WrapperLabel wlaPositionsart = new WrapperLabel();
	private WrapperComboBox wcbEinheit = new WrapperComboBox();
	private WrapperComboBox wcbPositionsart = new WrapperComboBox();
	private WrapperLabel wlaPreis = new WrapperLabel();
	private WrapperLabel wlaPreis1 = new WrapperLabel();
	private WrapperNumberField wnfPreis1 = new WrapperNumberField();
	private WrapperLabel wlaPreis2 = new WrapperLabel();
	private WrapperNumberField wnfPreis2 = new WrapperNumberField();
	private WrapperLabel wlaPreis3 = new WrapperLabel();
	private WrapperNumberField wnfPreis3 = new WrapperNumberField();
	private WrapperLabel wlaPreis4 = new WrapperLabel();
	private WrapperNumberField wnfPreis4 = new WrapperNumberField();
	private WrapperLabel wlaPreis5 = new WrapperLabel();
	private WrapperNumberField wnfPreis5 = new WrapperNumberField();

	private WrapperCheckBox wcbMitdrucken = new WrapperCheckBox();

	private WrapperLabel wlaVolumen = new WrapperLabel();
	private WrapperLabel wlaVolumen1 = new WrapperLabel();
	private WrapperLabel wlaVolumen2 = new WrapperLabel();
	private WrapperLabel wlaVolumen3 = new WrapperLabel();
	private WrapperLabel wlaVolumen4 = new WrapperLabel();
	private WrapperLabel wlaVolumen5 = new WrapperLabel();

	private WrapperLabel wlaWiederbeschaffungszeit = new WrapperLabel();
	private WrapperNumberField wnfWiederbeschaffungszeit = new WrapperNumberField();
	private WrapperLabel wlaWiederbeschaffungszeitEinheit = new WrapperLabel();

	private WrapperLabel wlaVerpackungseinheit = new WrapperLabel();
	private WrapperNumberField wnfVerpackungseinheit = new WrapperNumberField();

	private WrapperLabel wlaMindestbestellmenge = new WrapperLabel();
	private WrapperNumberField wnfMindestbestellmenge = new WrapperNumberField();
	protected boolean bFuegeNeuePositionVorDerSelektiertenEin = false;

	private WrapperLabel wlaInterneBemerkung = new WrapperLabel();
	private WrapperTextField wtfInterneBemerkung = new WrapperTextField();

	private WrapperLabel wlaPosition = new WrapperLabel();
	private WrapperTextField wtfPosition = new WrapperTextField();

	private WrapperIdentField wifArtikel = null;

	private WrapperLabel wlaBezeichnung = new WrapperLabel();

	private EinkaufsangebotpositionDto einkaufsangebotpositionDto = null;

	private WrapperLabel wlaEinheitKalkpreis = new WrapperLabel();

	private ArtikelDto letzterArtikel = null;

	public MontageartDto defaultMontageartDto = null;

	public PanelEinkaufsangebotpositionen(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameStueckliste = (InternalFrameAngebotstkl) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wcbPositionsart;
	}

	private void setDefaults() throws Throwable {
		wcbEinheit.setMap(DelegateFactory.getInstance().getSystemDelegate()
				.getAllEinheiten());

		Map<String, String> m = new LinkedHashMap<String, String>();
		m.put(LocaleFac.POSITIONSART_IDENT, LocaleFac.POSITIONSART_IDENT);
		m.put(LocaleFac.POSITIONSART_HANDEINGABE,
				LocaleFac.POSITIONSART_HANDEINGABE);
		wcbPositionsart.setMap(m);

		MontageartDto[] dtos = DelegateFactory.getInstance()
				.getStuecklisteDelegate().montageartFindByMandantCNr();

		if (dtos != null && dtos.length > 0) {
			defaultMontageartDto = dtos[0];
		}

	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		letzterArtikel = wifArtikel.getArtikelDto();
		super.eventActionNew(eventObject, true, false);
		einkaufsangebotpositionDto = new EinkaufsangebotpositionDto();

		leereAlleFelder(this);
		wnfMenge.setBigDecimal(new java.math.BigDecimal(0));
		wifArtikel.setArtikelDto(null);

		if (((ItemChangedEvent) eventObject).getID() == ItemChangedEvent.ACTION_POSITION_VORPOSITIONEINFUEGEN) {
			// Dieses Flag gibt an, ob die neue Position vor der aktuellen
			// eingefuegt werden soll
			bFuegeNeuePositionVorDerSelektiertenEin = true;
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		
		Object[] o = internalFrameStueckliste.getTabbedPaneEinkaufsangebot().getPanelQueryEinkaufsangebotpositionen()
				.getSelectedIds();
		if (o != null) {
			for (int i = 0; i < o.length; i++) {
				EinkaufsangebotpositionDto toRemove = DelegateFactory.getInstance()
						.getAngebotstklDelegate()
						.einkaufsangebotpositionFindByPrimaryKey((Integer) o[i]);
				DelegateFactory.getInstance().getAngebotstklDelegate()
						.removeEinkaufsangebotposition(toRemove);

			}
		}

		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws ExceptionLP {

		einkaufsangebotpositionDto.setPositionsartCNr((String) wcbPositionsart
				.getKeyOfSelectedItem());
		einkaufsangebotpositionDto.setBelegIId(internalFrameStueckliste
				.getEinkaufsangebotDto().getIId());
		einkaufsangebotpositionDto.setCBemerkung(wtfBemerkung.getText());
		einkaufsangebotpositionDto.setCInternebemerkung(wtfInterneBemerkung
				.getText());
		einkaufsangebotpositionDto.setNMenge(wnfMenge.getBigDecimal());
		einkaufsangebotpositionDto.setNPreis1(wnfPreis1.getBigDecimal());
		einkaufsangebotpositionDto.setNPreis2(wnfPreis2.getBigDecimal());
		einkaufsangebotpositionDto.setNPreis3(wnfPreis3.getBigDecimal());
		einkaufsangebotpositionDto.setNPreis4(wnfPreis4.getBigDecimal());
		einkaufsangebotpositionDto.setNPreis5(wnfPreis5.getBigDecimal());

		einkaufsangebotpositionDto.setBMitdrucken(wcbMitdrucken.getShort());

		einkaufsangebotpositionDto.setCPosition(wtfPosition.getText());

		einkaufsangebotpositionDto.setEinheitCNr((String) wcbEinheit
				.getKeyOfSelectedItem());

		einkaufsangebotpositionDto.setBArtikelbezeichnunguebersteuert(Helper
				.boolean2Short(false));

		einkaufsangebotpositionDto.setIVerpackungseinheit(wnfVerpackungseinheit
				.getInteger());
		einkaufsangebotpositionDto
				.setIWiederbeschaffungszeit(wnfWiederbeschaffungszeit
						.getInteger());
		einkaufsangebotpositionDto
				.setFMindestbestellmenge(wnfMindestbestellmenge.getDouble());

		if (((String) wcbPositionsart.getKeyOfSelectedItem())
				.equals(LocaleFac.POSITIONSART_IDENT)) {
			einkaufsangebotpositionDto.setArtikelIId(wifArtikel.getArtikelDto()
					.getIId());
			einkaufsangebotpositionDto
					.setPositionsartCNr(AngebotstklFac.POSITIONSART_AGSTKL_IDENT);
		} else {
			einkaufsangebotpositionDto.setArtikelIId(null);
			einkaufsangebotpositionDto.setCBez(wtfHandartikel.getText());
			einkaufsangebotpositionDto.setCZusatzbez(wtfHandartikel2.getText());

			einkaufsangebotpositionDto
					.setPositionsartCNr(AngebotstklFac.POSITIONSART_AGSTKL_HANDEINGABE);
		}

	}

	protected void dto2Components() throws Throwable {

		EinkaufsangebotDto einkaufsangebotDto = DelegateFactory.getInstance()
				.getAngebotstklDelegate().einkaufsangebotFindByPrimaryKey(
						einkaufsangebotpositionDto.getBelegIId());

		internalFrameStueckliste.getTabbedPaneEinkaufsangebot()
				.getPanelQueryEinkaufsangebotpositionen()
				.uebersteuereSpaltenUeberschrift(
						4,
						LPMain.getTextRespectUISPr("lp.preis")
								+ " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto
										.getNMenge1(), 0));
		internalFrameStueckliste.getTabbedPaneEinkaufsangebot()
				.getPanelQueryEinkaufsangebotpositionen()
				.uebersteuereSpaltenUeberschrift(
						5,
						LPMain.getTextRespectUISPr("lp.preis")
								+ " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto
										.getNMenge2(), 0));
		internalFrameStueckliste.getTabbedPaneEinkaufsangebot()
				.getPanelQueryEinkaufsangebotpositionen()
				.uebersteuereSpaltenUeberschrift(
						6,
						LPMain.getTextRespectUISPr("lp.preis")
								+ " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto
										.getNMenge3(), 0));
		internalFrameStueckliste.getTabbedPaneEinkaufsangebot()
				.getPanelQueryEinkaufsangebotpositionen()
				.uebersteuereSpaltenUeberschrift(
						7,
						LPMain.getTextRespectUISPr("lp.preis")
								+ " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto
										.getNMenge4(), 0));
		internalFrameStueckliste.getTabbedPaneEinkaufsangebot()
				.getPanelQueryEinkaufsangebotpositionen()
				.uebersteuereSpaltenUeberschrift(
						8,
						LPMain.getTextRespectUISPr("lp.preis")
								+ " "
								+ Helper.rundeKaufmaennisch(einkaufsangebotDto
										.getNMenge5(), 0));

		wlaPreis1.setText(Helper.rundeKaufmaennisch(einkaufsangebotDto
				.getNMenge1(), 0)
				+ "");
		wlaPreis2.setText(Helper.rundeKaufmaennisch(einkaufsangebotDto
				.getNMenge2(), 0)
				+ "");
		wlaPreis3.setText(Helper.rundeKaufmaennisch(einkaufsangebotDto
				.getNMenge3(), 0)
				+ "");
		wlaPreis4.setText(Helper.rundeKaufmaennisch(einkaufsangebotDto
				.getNMenge4(), 0)
				+ "");
		wlaPreis5.setText(Helper.rundeKaufmaennisch(einkaufsangebotDto
				.getNMenge5(), 0)
				+ "");

		wlaVolumen1.setText(Helper.rundeKaufmaennisch(
				einkaufsangebotpositionDto.getNMenge().multiply(
						einkaufsangebotDto.getNMenge1()), 2)
				+ "");
		wlaVolumen2.setText(Helper.rundeKaufmaennisch(
				einkaufsangebotpositionDto.getNMenge().multiply(
						einkaufsangebotDto.getNMenge2()), 2)
				+ "");
		wlaVolumen3.setText(Helper.rundeKaufmaennisch(
				einkaufsangebotpositionDto.getNMenge().multiply(
						einkaufsangebotDto.getNMenge3()), 2)
				+ "");
		wlaVolumen4.setText(Helper.rundeKaufmaennisch(
				einkaufsangebotpositionDto.getNMenge().multiply(
						einkaufsangebotDto.getNMenge4()), 2)
				+ "");
		wlaVolumen5.setText(Helper.rundeKaufmaennisch(
				einkaufsangebotpositionDto.getNMenge().multiply(
						einkaufsangebotDto.getNMenge5()), 2)
				+ "");

		try {
			wcbEinheit.setKeyOfSelectedItem(einkaufsangebotpositionDto
					.getEinheitCNr());
		} catch (Throwable ex) {
			handleException(ex, false);
		}

		wcbMitdrucken.setShort(einkaufsangebotpositionDto.getBMitdrucken());

		wtfBemerkung.setText(einkaufsangebotpositionDto.getCBemerkung());
		wtfInterneBemerkung.setText(einkaufsangebotpositionDto
				.getCInternebemerkung());
		wnfMenge.setBigDecimal(einkaufsangebotpositionDto.getNMenge());

		wnfMindestbestellmenge.setDouble(einkaufsangebotpositionDto
				.getFMindestbestellmenge());
		wnfVerpackungseinheit.setInteger(einkaufsangebotpositionDto
				.getIVerpackungseinheit());
		wnfWiederbeschaffungszeit.setInteger(einkaufsangebotpositionDto
				.getIWiederbeschaffungszeit());

		wnfPreis1.setBigDecimal(einkaufsangebotpositionDto.getNPreis1());
		wnfPreis2.setBigDecimal(einkaufsangebotpositionDto.getNPreis2());
		wnfPreis3.setBigDecimal(einkaufsangebotpositionDto.getNPreis3());
		wnfPreis4.setBigDecimal(einkaufsangebotpositionDto.getNPreis4());
		wnfPreis5.setBigDecimal(einkaufsangebotpositionDto.getNPreis5());

		wtfPosition.setText(einkaufsangebotpositionDto.getCPosition());

		if (einkaufsangebotpositionDto.getPositionsartCNr().equals(
				AngebotstklFac.POSITIONSART_AGSTKL_HANDEINGABE)) {
			wcbPositionsart
					.setKeyOfSelectedItem(LocaleFac.POSITIONSART_HANDEINGABE);
			wtfHandartikel.setText(einkaufsangebotpositionDto.getCBez());
			wtfHandartikel2.setText(einkaufsangebotpositionDto.getCZusatzbez());
			wifArtikel.setArtikelDto(null);
		} else {
			wcbPositionsart.setKeyOfSelectedItem(LocaleFac.POSITIONSART_IDENT);

			ArtikelDto artikelDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(
							einkaufsangebotpositionDto.getArtikelIId());

			wifArtikel.setArtikelDto(artikelDto);
			wtfHandartikel.setText(null);
			wtfHandartikel2.setText(null);

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
			wifArtikel.validate();
			if (allMandatoryFieldsSetDlg()) {
				components2Dto();
				if (einkaufsangebotpositionDto.getIId() == null) {

					if (bFuegeNeuePositionVorDerSelektiertenEin) {
						Integer iIdAktuellePosition = (Integer) internalFrameStueckliste
								.getTabbedPaneEinkaufsangebot()
								.getAngebotstklPositionenTop().getSelectedId();

						// erstepos: 0 die erste Position steht an der Stelle 1
						Integer iSortAktuellePosition = new Integer(1);

						// erstepos: 1 die erste Position steht an der Stelle 1
						if (iIdAktuellePosition != null) {
							iSortAktuellePosition = DelegateFactory
									.getInstance().getAngebotstklDelegate()
									.einkaufsangebotpositionFindByPrimaryKey(
											iIdAktuellePosition).getISort();

							// Die bestehenden Positionen muessen Platz fuer die
							// neue schaffen
							DelegateFactory
									.getInstance()
									.getAngebotstklpositionDelegate()
									.sortierungAnpassenBeiEinfuegenEinerPositionVorPosition(
											iSortAktuellePosition.intValue(),
											einkaufsangebotpositionDto
													.getBelegIId());
						}

						// Die neue Position wird an frei gemachte Position
						// gesetzt
						einkaufsangebotpositionDto
								.setISort(iSortAktuellePosition);
					}

					einkaufsangebotpositionDto.setIId(DelegateFactory
							.getInstance().getAngebotstklDelegate()
							.createEinkaufsangebotposition(
									einkaufsangebotpositionDto));
					setKeyWhenDetailPanel(einkaufsangebotpositionDto.getIId());
					einkaufsangebotpositionDto = DelegateFactory.getInstance()
							.getAngebotstklDelegate()
							.einkaufsangebotpositionFindByPrimaryKey(
									einkaufsangebotpositionDto.getIId());
				} else {
					DelegateFactory.getInstance().getAngebotstklDelegate()
							.updateEinkaufsangebotposition(
									einkaufsangebotpositionDto);
				}
				super.eventActionSave(e, true);

				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().setKeyWasForLockMe(
							internalFrameStueckliste.getEinkaufsangebotDto()
									.getIId()
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

			if (e.getSource() == wifArtikel) {
				wcbEinheit.setKeyOfSelectedItem(wifArtikel.getArtikelDto()
						.getEinheitCNr());
			}

		}
	}

	private void jbInit() throws Throwable {
		// von hier ...
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);
		wtfHandartikel.setMandatoryField(true);

		wcbMitdrucken.setText(LPMain
				.getTextRespectUISPr("stkl.positionen.mitdrucken"));
		wcbMitdrucken.setMnemonic('A');

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);

		wlaPreis.setText(LPMain.getTextRespectUISPr("lp.preis"));

		wlaVolumen.setText(LPMain.getTextRespectUISPr(
				"lp.volumen"));

		wlaMindestbestellmenge.setText(LPMain.getTextRespectUISPr("artikel.mindestbestellmenge"));
		wlaWiederbeschaffungszeit.setText(LPMain.getTextRespectUISPr(
						"artikel.artikellieferant.wiederbeschaffungszeit"));

		wlaWiederbeschaffungszeitEinheit
				.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWiederbeschaffungszeitEinheit.setText(LPMain
				.getTextRespectUISPr("lp.kw"));
		wlaVerpackungseinheit.setText(LPMain.getTextRespectUISPr(
				"artikel.verpackungseinheit"));
		// Mandantenwaehrung
		String whg = DelegateFactory.getInstance().getMandantDelegate()
				.mandantFindByPrimaryKey(
						LPMain.getTheClient().getMandant())
				.getWaehrungCNr();

		wlaEinheitKalkpreis.setText(whg.trim());

		wcbEinheit.setMandatoryField(true);

		wlaBezeichnung.setText(LPMain.getTextRespectUISPr(
				"lp.bezeichnung"));

		wlaBemerkung.setText(LPMain.getTextRespectUISPr(
				"lp.referenzbemerkung"));

		wlaInterneBemerkung.setText(LPMain.getTextRespectUISPr(
				"lp.internebemerkung"));

		wlaMenge.setText(LPMain.getTextRespectUISPr("lp.menge"));
		wlaPositionsart.setText(LPMain.getTextRespectUISPr(
				"lp.positionsart"));

		wnfWiederbeschaffungszeit.setFractionDigits(0);
		wnfWiederbeschaffungszeit.setMinimumValue(0);

		wnfVerpackungseinheit.setFractionDigits(0);
		wnfVerpackungseinheit.setMinimumValue(0);

		wnfMenge.setMandatoryField(true);

		int iNachkommastellenMenge = Defaults.getInstance()
				.getIUINachkommastellenMenge();

		wlaPosition.setText(LPMain.getTextRespectUISPr("lp.position"));

		wnfMenge.setFractionDigits(iNachkommastellenMenge);

		int iNachkommastellenPreis = Defaults.getInstance()
				.getIUINachkommastellenPreiseVK();

		wnfPreis1.setFractionDigits(iNachkommastellenPreis);
		wnfPreis2.setFractionDigits(iNachkommastellenPreis);
		wnfPreis3.setFractionDigits(iNachkommastellenPreis);
		wnfPreis4.setFractionDigits(iNachkommastellenPreis);
		wnfPreis5.setFractionDigits(iNachkommastellenPreis);

		wtfBemerkung.setColumnsMax(300);
		wtfInterneBemerkung.setColumnsMax(300);

		wtfHandartikel.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ARTIKELBEZEICHNUNG);
		wtfHandartikel2
				.setColumnsMax(ArtikelFac.MAX_ARTIKEL_ARTIKELBEZEICHNUNG);
		getInternalFrame().addItemChangedListener(this);
		wcbPositionsart
				.addActionListener(new PanelEinkaufsangebotpositionen_wcbPositionsart_actionAdapter(
						this));
		wcbPositionsart.setMandatoryField(true);

		wifArtikel.getWtfIdent().addFocusListener(
				new PanelEinkaufsangebotpositionen_wtfIdent_focusAdapter(this));

		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel(new MigLayout("wrap 7, hidemode 3", "[fill, 20%][fill, 15%][fill, 15%][fill, 15%][fill, 15%][fill, 15%][fill, 5%]"));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		wifArtikel.getWtfIdent().setUppercaseField(true);
		wlaEinheitKalkpreis.setHorizontalAlignment(SwingConstants.LEFT);
		
		jpaWorkingOn.add(wlaPositionsart);
		jpaWorkingOn.add(wcbPositionsart, "span 2, wrap");

		jpaWorkingOn.add(wlaBezeichnung);
		jpaWorkingOn.add(wtfHandartikel, "span 5, wrap");
		jpaWorkingOn.add(wtfHandartikel2, "skip, span 5, wrap");
		
		jpaWorkingOn.add(wifArtikel.getKundenidentnummerButton(), "growx 10, split 2");
		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), "growx 90");
		jpaWorkingOn.add(wifArtikel.getWtfIdent());
		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(), "span 2, wrap");

		jpaWorkingOn.add(wifArtikel.getWtfZusatzBezeichnung(), "skip 2, span 2, wrap");
		
		jpaWorkingOn.add(wlaMenge);
		jpaWorkingOn.add(wnfMenge);
		jpaWorkingOn.add(wcbEinheit, "wrap");
		
		jpaWorkingOn.add(wlaPreis);
		jpaWorkingOn.add(wlaPreis1);
		jpaWorkingOn.add(wlaPreis2);
		jpaWorkingOn.add(wlaPreis3);
		jpaWorkingOn.add(wlaPreis4);
		jpaWorkingOn.add(wlaPreis5, "wrap");

		jpaWorkingOn.add(wlaVolumen);
		jpaWorkingOn.add(wlaVolumen1);
		jpaWorkingOn.add(wlaVolumen2);
		jpaWorkingOn.add(wlaVolumen3);
		jpaWorkingOn.add(wlaVolumen4);
		jpaWorkingOn.add(wlaVolumen5, "wrap");
		
		jpaWorkingOn.add(wnfPreis1, "skip");
		jpaWorkingOn.add(wnfPreis2);
		jpaWorkingOn.add(wnfPreis3);
		jpaWorkingOn.add(wnfPreis4);
		jpaWorkingOn.add(wnfPreis5);
		jpaWorkingOn.add(wlaEinheitKalkpreis, "wrap");
		
		jpaWorkingOn.add(wlaVerpackungseinheit);
		jpaWorkingOn.add(wnfVerpackungseinheit);
		jpaWorkingOn.add(wlaWiederbeschaffungszeit, "skip, span 2");
		jpaWorkingOn.add(wnfWiederbeschaffungszeit);
		jpaWorkingOn.add(wlaWiederbeschaffungszeitEinheit, "wrap");
		
		jpaWorkingOn.add(wlaMindestbestellmenge);
		jpaWorkingOn.add(wnfMindestbestellmenge);
		jpaWorkingOn.add(wlaPosition, "skip, span 2");
		jpaWorkingOn.add(wtfPosition, "wrap");
		
		jpaWorkingOn.add(wlaBemerkung);
		jpaWorkingOn.add(wtfBemerkung, "span 5, wrap");
		
		jpaWorkingOn.add(wlaInterneBemerkung);
		jpaWorkingOn.add(wtfInterneBemerkung, "span 5, wrap");

		jpaWorkingOn.add(wcbMitdrucken, "skip, span 5");
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_EINKAUFSANGEBOT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);
			clearStatusbar();

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance().getParameterDelegate().getParametermandant(
							ParameterFac.PARAMETER_DEFAULT_ARTIKEL_EINHEIT,
							ParameterFac.KATEGORIE_ARTIKEL,
							LPMain.getTheClient().getMandant());
			wcbEinheit.setKeyOfSelectedItem(Helper.fitString2Length(parameter
					.getCWert(), 15, ' '));

			if (key != null && key.equals(LPMain.getLockMeForNew())) {

				wnfMenge.setBigDecimal(new java.math.BigDecimal(1));

				if (letzterArtikel != null) {
					wifArtikel.getWtfIdent().setText(letzterArtikel.getCNr());
					wcbEinheit.setKeyOfSelectedItem(letzterArtikel
							.getEinheitCNr());
				}

				wcbMitdrucken.setSelected(true);

			}
		} else {
			einkaufsangebotpositionDto = DelegateFactory.getInstance()
					.getAngebotstklDelegate()
					.einkaufsangebotpositionFindByPrimaryKey((Integer) key);

			dto2Components();
		}
	}

	public void wcbPositionsart_actionPerformed(ActionEvent e) {
		String key = (String) wcbPositionsart.getKeyOfSelectedItem();
		if (key.equals(LocaleFac.POSITIONSART_IDENT)) {
			wtfHandartikel.setText(null);
			wlaBezeichnung.setVisible(false);
			wtfHandartikel.setVisible(false);
			wtfHandartikel2.setVisible(false);
			wtfHandartikel.setMandatoryField(false);

			wtfHandartikel.setEditable(true);
			wtfHandartikel2.setEditable(true);

			try {
				LockStateValue v = getLockedstateDetailMainKey();
				if (v != null && v.getIState() == LOCK_FOR_NEW) {
					wifArtikel.getWbuArtikel().setEnabled(true);
					wifArtikel.getWtfIdent().setEditable(true);
					wifArtikel.getWtfIdent().setEditable(true);
				}
			} catch (Throwable e1) {
				handleException(e1, true);
			}
			wifArtikel.getKundenidentnummerButton().setVisible(true);
			wifArtikel.getWbuArtikel().setVisible(true);
									
			wifArtikel.getWtfIdent().setVisible(true);
			wifArtikel.getWtfIdent().setMandatoryField(true);
			wifArtikel.getWtfBezeichnung().setVisible(true);
			wifArtikel.getWtfZusatzBezeichnung().setVisible(true);

		} else if (key.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
			wifArtikel.getWtfIdent().setText(null);
			wtfHandartikel.setActivatable(true);
			wtfHandartikel2.setActivatable(true);

			try {
				if (getLockedstateDetailMainKey().getIState() == LOCK_IS_LOCKED_BY_ME
						|| getLockedstateDetailMainKey().getIState() == LOCK_FOR_NEW) {
					wtfHandartikel.setEditable(true);
					wtfHandartikel2.setEditable(true);
				} else {
					wtfHandartikel.setEditable(false);
					wtfHandartikel2.setEditable(false);
				}
			} catch (Throwable e2) {
				handleException(e2, true);
			}

			if (einkaufsangebotpositionDto != null) {
				einkaufsangebotpositionDto.setArtikelIId(null);
			}
			wifArtikel.getKundenidentnummerButton().setVisible(false);
			wifArtikel.getWbuArtikel().setVisible(false);
			wifArtikel.getWtfIdent().setVisible(false);
			wifArtikel.getWtfIdent().setMandatoryField(false);
			wifArtikel.getWtfBezeichnung().setVisible(false);
			wifArtikel.getWtfZusatzBezeichnung().setVisible(false);
			
			wlaBezeichnung.setVisible(true);
			wtfHandartikel.setVisible(true);
			wtfHandartikel.setMandatoryField(true);
			wtfHandartikel2.setVisible(true);
			jpaWorkingOn.repaint();
		}

	}

	void wtfIdent_focusLost(FocusEvent e) {
		if (wifArtikel.getArtikelDto() != null
				&& einkaufsangebotpositionDto.getIId() == null) {
			wcbEinheit.setKeyOfSelectedItem(wifArtikel.getArtikelDto()
					.getEinheitCNr());
		}
	}

}

class PanelEinkaufsangebotpositionen_wcbPositionsart_actionAdapter implements
		ActionListener {
	private PanelEinkaufsangebotpositionen adaptee;

	PanelEinkaufsangebotpositionen_wcbPositionsart_actionAdapter(
			PanelEinkaufsangebotpositionen adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.wcbPositionsart_actionPerformed(e);
	}
}

class PanelEinkaufsangebotpositionen_wtfIdent_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelEinkaufsangebotpositionen adaptee;

	PanelEinkaufsangebotpositionen_wtfIdent_focusAdapter(
			PanelEinkaufsangebotpositionen adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wtfIdent_focusLost(e);
	}
}
