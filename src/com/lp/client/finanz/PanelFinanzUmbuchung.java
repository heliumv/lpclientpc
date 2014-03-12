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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.EventObject;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldKommentar;
import com.lp.client.frame.component.WrapperGeschaeftsjahrDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.MwstsatzbezDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.WechselkursDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>&UUml;berschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Organisation: </p>
 * @author unbekannt
 * @version $Revision: 1.32 $
 */
public class PanelFinanzUmbuchung extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BuchungDto buchungDto = null;
	private BuchungdetailDto[] buchungdetailDtos = null;
	private KostenstelleDto kostenstelleDto = null;
	private KontoDto kontoDto = null;
	private KontoDto gegenkontoDto = null;
	private MwstsatzDto mwstsatzDto = null;

	// Default wird auf sachkonten umgebucht
	private String kontotypKonto = FinanzServiceFac.KONTOTYP_SACHKONTO;
	private String kontotypGegenkonto = FinanzServiceFac.KONTOTYP_SACHKONTO;

	private PanelQueryFLR panelQueryFLRKonto = null;
	private PanelQueryFLR panelQueryFLRGegenkonto = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;

	private static final String ACTION_SPECIAL_KONTO = "action_special_ub_konto";
	private static final String ACTION_SPECIAL_GEGENKONTO = "action_special_ub_gegenkonto";
	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_ub_kostenstelle";
	private static final String ACTION_SPECIAL_ERZEUGEN = "action_special_ub_erzeugen";
	private static final String ACTION_SPECIAL_RB_KONTO = "action_special_ub_rb_konto";
	private static final String ACTION_SPECIAL_RB_GEGENKONTO = "action_special_ub_rb_gegenkonto";
	private static final String ACTION_SPECIAL_RB_BELEG = "action_special_ub_rb_beleg";
	private static final String ACTION_SPECIAL_CO_WAEHRUNG = "action_special_ub_co_waehrung";
	private static final String ACTION_SPECIAL_CO_UST = "action_special_ub_co_ust";
	private static final String ACTION_SPECIAL_KONTO_TAUSCHEN = "action_special_konto_tauschen";
	private static final String ACTION_SPECIAL_BUCHUNGSART = "action_special_buchungsart";
	private WrapperLabel wlaUST = new WrapperLabel();
	private WrapperNumberField wnfUST = new WrapperNumberField();
	private WrapperGeschaeftsjahrDateField wdfDatum = null;
	private WrapperLabel wlaBetrag = new WrapperLabel();
	private WrapperNumberField wnfBetrag = new WrapperNumberField();
	private WrapperRadioButton wrbKontoSachkonto = new WrapperRadioButton();
	private WrapperRadioButton wrbKontoDebitorenkonto = new WrapperRadioButton();
	private WrapperRadioButton wrbGegenkontoSachkonto = new WrapperRadioButton();
	private WrapperRadioButton wrbGegenkontoDebitorenkonto = new WrapperRadioButton();
	private WrapperRadioButton wrbKontoKreditorenkonto = new WrapperRadioButton();
	private WrapperRadioButton wrbGegenkontoKreditorenkonto = new WrapperRadioButton();
	private WrapperLabel wlaAbstandOben = new WrapperLabel();
	private WrapperButton wbuKonto = new WrapperButton();
	private WrapperButton wbuGegenkonto = new WrapperButton();
	private WrapperTextField wtfKontoNummer = new WrapperTextField();
	private WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
	private WrapperTextField wtfGegenkontoNummer = new WrapperTextField();
	private WrapperTextField wtfGegenkontoBezeichnung = new WrapperTextField();
	private WrapperLabel wlaAbstandLinks = new WrapperLabel();
	private WrapperLabel wlaAbstandRechts = new WrapperLabel();
	private WrapperLabel wlaKonto = new WrapperLabel();
	private WrapperLabel wlaGegenkonto = new WrapperLabel();
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperLabel wlaAuszug = new WrapperLabel();
	private WrapperNumberField wnfAuszug = new WrapperNumberField();
	private WrapperButton wbuKostenstelle = new WrapperButton();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperTextField wtfText = new WrapperTextField();
	private WrapperComboBox wcoWaehrung = new WrapperComboBox();
	private WrapperLabel wrapperLabel1 = new WrapperLabel();
	private WrapperLabel wrapperLabel2 = new WrapperLabel();
	private WrapperLabel wlaBeleg = new WrapperLabel();
	private WrapperRadioButton wrbBelegAuto = new WrapperRadioButton();
	private WrapperRadioButton wrbBelegHand = new WrapperRadioButton();
	private WrapperTextField wtfBeleg = new WrapperTextField();
	private WrapperLabel wlaWaehrungBetrag = new WrapperLabel();
	private WrapperLabel wlaWaehrungUST = new WrapperLabel();
	private ButtonGroup buttongroupKonto = new ButtonGroup();
	private ButtonGroup buttongroupGegenkonto = new ButtonGroup();
	private ButtonGroup buttongroupBeleg = new ButtonGroup();
	private JScrollPane jspBuchungen = new JScrollPane();
	private WrapperTable jtaBuchungen = new WrapperTable(null);
	private WrapperComboBox wcoBuchungsart = new WrapperComboBox();
	private WrapperLabel wlaBuchungsart = new WrapperLabel();
	private WrapperLabel wlaAuszugGegenkonto = new WrapperLabel();
	private WrapperNumberField wnfAuszugGegenkonto = new WrapperNumberField();
	private WrapperComboBox wcoUst = new WrapperComboBox();
	private WrapperLabel wlaKursBetrag = new WrapperLabel();
	private WechselkursDto kursDto = null;
	private boolean editierMode = false;
	private TabbedPaneKonten tpKonten = null;
	private boolean bAutoAuszugsnummer = false;
	private WrapperEditorField wefKommentar = new WrapperEditorFieldKommentar(
			getInternalFrame(), LPMain.getTextRespectUISPr("lp.kommentar"));

	public PanelFinanzUmbuchung(InternalFrame internalFrame) throws Throwable {
		super(internalFrame, LPMain.getInstance().getTextRespectUISPr(
				"fb.menu.umbuchung"));
		jbInit();
		setDefaults();
		initComponents();
		// kein Locking auf diesem Panel

		LockStateValue lockstateValue = new LockStateValue(null, null,
				LOCK_NO_LOCKING);
		this.updateButtons(lockstateValue);
	}

	private void jbInit() throws Throwable {
		// border3 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		// this.setLayout(gridBagLayout1);

		this.createAndSaveAndShowButton(
				"/com/lp/client/res/document_exchange.png",
				LPMain.getInstance().getTextRespectUISPr(
						"finanz.konten.tauschen"),
				this.ACTION_SPECIAL_KONTO_TAUSCHEN, null);

		ParametermandantDto pm = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FINANZ,
						ParameterFac.PARAMETER_AUSZUGSNUMMER_BEI_BANK_ANGEBEN);
		if (pm != null)
			if ((Boolean) pm.getCWertAsObject() == false)
				bAutoAuszugsnummer = true;

		getInternalFrame().addItemChangedListener(this);
		wlaUST.setText(LPMain.getInstance().getTextRespectUISPr("label.mwst"));
		wlaBetrag.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.betrag"));
		// jpaWorkingOn.setBorder(border3);
		// jpaWorkingOn.setLayout(gridBagLayout3);
		wrbKontoSachkonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.sachkonto"));
		wrbGegenkontoSachkonto.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.sachkonto"));
		wrbKontoDebitorenkonto.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.debitorenkonto"));
		wrbGegenkontoDebitorenkonto.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.debitorenkonto"));
		wrbKontoKreditorenkonto.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.kreditorenkonto"));
		wrbGegenkontoKreditorenkonto.setText(LPMain.getInstance()
				.getTextRespectUISPr("lp.kreditorenkonto"));
		wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto"));
		wbuKonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto.tooltip"));
		wbuGegenkonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto"));
		wbuGegenkonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.konto.tooltip"));
		wtfKontoNummer.setMinimumSize(new Dimension(100, 23));
		wtfKontoNummer.setPreferredSize(new Dimension(100, 23));
		wtfKontoNummer.setActivatable(true);
		wtfKontoNummer.setMandatoryField(true);
		wtfKontoBezeichnung.setActivatable(false);
		wtfGegenkontoNummer.setMinimumSize(new Dimension(100, 23));
		wtfGegenkontoNummer.setPreferredSize(new Dimension(100, 23));
		wtfGegenkontoNummer.setActivatable(true);
		wtfGegenkontoNummer.setMandatoryField(true);

		FocusListener listener = new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					if (e.getSource() == wtfKontoNummer) {
						kontoDto = DelegateFactory
								.getInstance()
								.getFinanzDelegate()
								.kontoFindByCnrKontotypMandantOhneExc(
										wtfKontoNummer.getText().trim(),
										getKontoTyp(),
										LPMain.getTheClient().getMandant());
						if (kontoDto == null) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getMessageTextRespectUISPr(
													"fb.error.kontomitnummernichtgefunden",
													getKontoTyp(),
													wtfKontoNummer
															.getText()));
						}
						dto2ComponentsKonto();
					} else if (e.getSource() == wtfGegenkontoNummer) {
						gegenkontoDto = DelegateFactory
								.getInstance()
								.getFinanzDelegate()
								.kontoFindByCnrKontotypMandantOhneExc(
										wtfGegenkontoNummer.getText().trim(),
										getGegenkontoKontoTyp(),
										LPMain.getTheClient().getMandant());
						if (gegenkontoDto == null) {
							DialogFactory
									.showModalDialog(
											LPMain.getTextRespectUISPr("lp.error"),
											LPMain.getMessageTextRespectUISPr(
													"fb.error.kontomitnummernichtgefunden",
													getGegenkontoKontoTyp(),
													wtfGegenkontoNummer
															.getText()));
						}
						dto2ComponentsGegenkonto();
					}
				} catch (ExceptionLP e1) {
				} catch (Throwable e1) {
				}
			}
		};
		wtfKontoNummer.addFocusListener(listener);
		wtfGegenkontoNummer.addFocusListener(listener);

		wtfGegenkontoBezeichnung.setActivatable(false);
		wlaAbstandLinks.setMaximumSize(new Dimension(80, 23));
		wlaAbstandLinks.setMinimumSize(new Dimension(80, 23));
		wlaAbstandLinks.setPreferredSize(new Dimension(80, 23));
		wlaAbstandRechts.setMaximumSize(new Dimension(80, 23));
		wlaAbstandRechts.setMinimumSize(new Dimension(80, 23));
		wlaAbstandRechts.setPreferredSize(new Dimension(80, 23));
		wlaKonto.setHorizontalAlignment(SwingConstants.LEFT);
		wlaKonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"finanz.konto.soll"));
		wlaGegenkonto.setHorizontalAlignment(SwingConstants.LEFT);
		wlaGegenkonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"finanz.gegenkonto.haben"));
		wlaDatum.setText(LPMain.getInstance().getTextRespectUISPr("lp.datum"));
		wlaAuszug.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.auszug"));
		wnfAuszug.setMaximumIntegerDigits(FinanzFac.MAX_UMBUCHUNG_AUSZUG);
		wnfAuszug.setFractionDigits(0);
		// wnfAuszug.setMaximumIntegerDigits(5);
		/** @todo parametrierbar PJ 4978 */
		wnfAuszugGegenkonto.setFractionDigits(0);
		/** @todo parametrierbar PJ 4978 */
		wnfAuszugGegenkonto
				.setMaximumIntegerDigits(FinanzFac.MAX_UMBUCHUNG_AUSZUG);
		wbuKostenstelle.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain.getInstance()
				.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wtfKostenstelleNummer.setActivatable(false);
		wtfKostenstelleNummer.setMandatoryField(true);
		wlaText.setText(LPMain.getInstance().getTextRespectUISPr("lp.text"));
		wtfText.setColumnsMax(FinanzFac.MAX_UMBUCHUNG_TEXT);
		wtfText.setMandatoryField(true);
		wcoWaehrung.setActionCommand(ACTION_SPECIAL_CO_WAEHRUNG);
		wcoWaehrung.setMandatoryField(true);
		wcoWaehrung.addActionListener(this);
		wnfBetrag
				.addFocusListener(new PanelFinanzUmbuchung_wnfBetrag_focusAdapter(
						this));
		wnfUST.setActivatable(false);
		wrapperLabel1.setHorizontalAlignment(SwingConstants.LEFT);
		wrapperLabel2.setHorizontalAlignment(SwingConstants.LEFT);
		wnfBetrag.setMandatoryField(true);
		wnfBetrag.setDependenceField(true);
		wlaBeleg.setText(LPMain.getInstance().getTextRespectUISPr("lp.beleg"));
		wrbBelegAuto.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.automatisch"));
		wrbBelegHand.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.handeingabe"));
		wtfBeleg.setColumnsMax(FinanzFac.MAX_UMBUCHUNG_BELEG);

		wtfBeleg.setMandatoryField(true);
		wlaWaehrungBetrag.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungUST.setHorizontalAlignment(SwingConstants.LEFT);
		wtfKostenstelleBezeichnung.setActivatable(false);

		wdfDatum = new WrapperGeschaeftsjahrDateField(getInternalFrameFinanz()
				.getIAktuellesGeschaeftsjahr());
		wdfDatum.addPropertyChangeListener(new PanelFinanzUmbuchung_wdfDatum_propertyChange(
				this));
		wdfDatum.setMandatoryField(true);
		wdfDatum.setMandatoryFieldDB(false);
		wlaBuchungsart.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.art"));
		wnfUST.setMandatoryField(true);
		wnfUST.setDependenceField(true);
		wcoBuchungsart.setMandatoryFieldDB(true);
		wcoBuchungsart.setActionCommand(ACTION_SPECIAL_BUCHUNGSART);
		wcoBuchungsart.addActionListener(this);
		wlaAuszugGegenkonto.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.auszug"));
		buttongroupBeleg.add(wrbBelegHand);
		buttongroupBeleg.add(wrbBelegAuto);
		buttongroupGegenkonto.add(wrbGegenkontoDebitorenkonto);
		buttongroupGegenkonto.add(wrbGegenkontoKreditorenkonto);
		buttongroupGegenkonto.add(wrbGegenkontoSachkonto);
		buttongroupKonto.add(wrbKontoDebitorenkonto);
		buttongroupKonto.add(wrbKontoKreditorenkonto);
		buttongroupKonto.add(wrbKontoSachkonto);
		wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
		wbuKonto.addActionListener(this);
		wbuGegenkonto.setActionCommand(ACTION_SPECIAL_GEGENKONTO);
		wbuGegenkonto.addActionListener(this);
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKostenstelle.addActionListener(this);

		wrbBelegHand.setActionCommand(ACTION_SPECIAL_RB_BELEG);
		wrbBelegAuto.setActionCommand(ACTION_SPECIAL_RB_BELEG);
		wrbBelegHand.addActionListener(this);
		wrbBelegAuto.addActionListener(this);

		wrbGegenkontoDebitorenkonto
				.setActionCommand(ACTION_SPECIAL_RB_GEGENKONTO);
		wrbGegenkontoKreditorenkonto
				.setActionCommand(ACTION_SPECIAL_RB_GEGENKONTO);
		wrbGegenkontoSachkonto.setActionCommand(ACTION_SPECIAL_RB_GEGENKONTO);
		wrbGegenkontoDebitorenkonto.addActionListener(this);
		wrbGegenkontoKreditorenkonto.addActionListener(this);
		wrbGegenkontoSachkonto.addActionListener(this);

		wrbKontoDebitorenkonto.setActionCommand(ACTION_SPECIAL_RB_KONTO);
		wrbKontoKreditorenkonto.setActionCommand(ACTION_SPECIAL_RB_KONTO);
		wrbKontoSachkonto.setActionCommand(ACTION_SPECIAL_RB_KONTO);
		wrbKontoDebitorenkonto.addActionListener(this);
		wrbKontoKreditorenkonto.addActionListener(this);
		wrbKontoSachkonto.addActionListener(this);
		wcoUst.setMandatoryFieldDB(true);
		wcoUst.setActionCommand(ACTION_SPECIAL_CO_UST);
		wcoUst.addActionListener(this);

		// jetzt setzen, damit gleich alles richtig initialisiert ist
		wrbBelegHand.setSelected(true);
		wrbGegenkontoSachkonto.setSelected(true);
		wrbKontoSachkonto.setSelected(true);
		wnfAuszug.setVisible(false);
		wlaAuszug.setVisible(false);
		wnfAuszugGegenkonto.setVisible(false);
		wlaAuszugGegenkonto.setVisible(false);

		jspBuchungen.getViewport().add(jtaBuchungen, null);

		iZeile++;
		jpaWorkingOn.add(wdfDatum, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoBuchungsart, new GridBagConstraints(3, iZeile, 4,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBuchungsart, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaKonto, new GridBagConstraints(0, iZeile, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaGegenkonto, new GridBagConstraints(4, iZeile, 3, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wrbKontoSachkonto, new GridBagConstraints(0, iZeile,
				3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbGegenkontoSachkonto, new GridBagConstraints(4,
				iZeile, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAbstandOben, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wrbKontoDebitorenkonto, new GridBagConstraints(0,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wrbGegenkontoDebitorenkonto, new GridBagConstraints(4,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbGegenkontoKreditorenkonto, new GridBagConstraints(
				4, iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbKontoKreditorenkonto, new GridBagConstraints(0,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wtfGegenkontoNummer, new GridBagConstraints(5, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuKonto, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuGegenkonto, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoNummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wtfKontoBezeichnung, new GridBagConstraints(1, iZeile,
				2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wtfGegenkontoBezeichnung, new GridBagConstraints(5,
				iZeile, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAbstandLinks, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAbstandRechts, new GridBagConstraints(4, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaAuszug, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAuszugGegenkonto, new GridBagConstraints(4, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wnfAuszugGegenkonto, new GridBagConstraints(5, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAuszug, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 1, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleNummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, new GridBagConstraints(2,
				iZeile, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wcoWaehrung, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaBetrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetrag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungBetrag, new GridBagConstraints(2, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoUst, new GridBagConstraints(5, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaUST, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfUST, new GridBagConstraints(1, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaWaehrungUST, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKursBetrag, new GridBagConstraints(4, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wtfText, new GridBagConstraints(1, iZeile, 6, 1, 5.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaText, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaBeleg, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBelegHand, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBeleg, new GridBagConstraints(2, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wefKommentar, new GridBagConstraints(4, iZeile, 3, 2,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 100, 50));
		iZeile++;
		jpaWorkingOn.add(wrbBelegAuto, new GridBagConstraints(1, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(jspBuchungen, new GridBagConstraints(0, iZeile, 7, 1,
				0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

	}

	/**
	 * Defaults setzen.
	 * 
	 * @throws Exception
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		// Waehrungen setzen
		Map<?, ?> waehrungen = DelegateFactory.getInstance()
				.getLocaleDelegate().getAllWaehrungen();
		wcoWaehrung.setMap(waehrungen);
		wcoWaehrung.setKeyOfSelectedItem(LPMain.getTheClient()
				.getSMandantenwaehrung());
		wcoBuchungsart.setMap(DelegateFactory.getInstance()
				.getFinanzServiceDelegate().getAllBuchungsarten());
		wcoBuchungsart.setKeyOfSelectedItem(FinanzFac.BUCHUNGSART_UMBUCHUNG);
		wcoUst.setMap(DelegateFactory.getInstance().getMandantDelegate()
				.getAllMwstsatzbez(LPMain.getTheClient().getMandant()));
		// wdfDatum.setDate(new java.util.Date(System.currentTimeMillis())) ;
		wdfDatum.setDefaultDate();
		if (bAutoAuszugsnummer)
			setAutoAuszugNummer();
		setKursBetrag();
	}

	private void setAutoAuszugNummer() throws ExceptionLP {

		if (wcoBuchungsart.getKeyOfSelectedItem().equals(
				FinanzFac.BUCHUNGSART_EROEFFNUNG)) {
			wnfAuszug.setBigDecimal(new BigDecimal("0"));
			wnfAuszug.setEditable(false);
			wnfAuszugGegenkonto.setBigDecimal(new BigDecimal("0"));
			wnfAuszugGegenkonto.setEditable(false);
		} else {
			if (bAutoAuszugsnummer) {
				wnfAuszug.setEditable(false);
				wnfAuszugGegenkonto.setEditable(false);
				if (wdfDatum.getDate() != null) {
					DateFormat df = new SimpleDateFormat("yyyyMM");
					Integer auszug = Integer.parseInt(df.format(wdfDatum
							.getDate()));
					wnfAuszug.setInteger(auszug);
					wnfAuszugGegenkonto.setInteger(auszug);
					wnfAuszug.setEditable(false);
					wnfAuszugGegenkonto.setEditable(false);
				}
			} else {
				wnfAuszug.setEditable(true);
				wnfAuszugGegenkonto.setEditable(true);
			}
		}
	}

	/**
	 * Das Geschaeftsjahr explizt setzen
	 * 
	 * @param geschaeftsjahr
	 */
	public void setGeschaeftsjahr(Integer geschaeftsjahr) {
		if (geschaeftsjahr != wdfDatum.getGeschaeftsjahr()) {
			wdfDatum.setGeschaeftsjahr(geschaeftsjahr);
			wdfDatum.setDefaultDate();
		}
	}

	protected InternalFrameFinanz getInternalFrameFinanz() {
		return (InternalFrameFinanz) getInternalFrame();
	}

	/**
	 * Speichere Daten des Panels.
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws ExitFrameException
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		dto2ComponentsKonto();
		dto2ComponentsGegenkonto();
		if (allMandatoryFieldsSetDlg()) {
			// es darf nicht 2mal dasselbe konto ausgewaehlt sein
			if (kontoDto.getIId().equals(gegenkontoDto.getIId())) {
				DialogFactory
						.showModalDialog(
								LPMain.getInstance().getTextRespectUISPr(
										"lp.error"),
								LPMain.getInstance().getTextRespectUISPr(
										"finanz.error.verschiedenekonten"));
				return;
			}
			if (wnfBetrag.getDouble().doubleValue() == 0.0) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("finanz.error.betragdefinieren"));
				return;
			}
			if (Math.abs(wnfBetrag.getDouble().doubleValue()) < wnfUST
					.getDouble().doubleValue()) {
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"), LPMain.getInstance()
						.getTextRespectUISPr("finanz.error.ustkleinerbetrag"));
				return;
			}

			components2Dto();
			buchungDto = DelegateFactory.getInstance().getBuchenDelegate()
					.verbucheUmbuchung(buchungDto, buchungdetailDtos);
			// Tabelle updaten
			int breite = getColumnNames().length;
			int hoehe = jtaBuchungen.getRowCount() + buchungdetailDtos.length;
			Object[][] data = new Object[hoehe][breite];
			// bisherige daten auslesen
			// die neuen buchungszeilen werden oben eingehengt
			for (int i = 0; i < jtaBuchungen.getRowCount(); i++) {
				for (int j = 0; j < jtaBuchungen.getColumnCount(); j++) {
					data[i + buchungdetailDtos.length][j] = jtaBuchungen
							.getValueAt(i, j);
				}
			}
			for (int j = 0; j < buchungdetailDtos.length; j++) {
				KontoDto konto = DelegateFactory
						.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKey(
								buchungdetailDtos[j].getKontoIId());
				KontoDto gegenkonto = DelegateFactory
						.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKey(
								buchungdetailDtos[j].getKontoIIdGegenkonto());
				data[j][0] = Helper.formatDatum(buchungDto.getDBuchungsdatum(),
						LPMain.getInstance().getTheClient().getLocUi());
				data[j][1] = konto.getCNr();
				data[j][2] = gegenkonto.getCNr();
				data[j][3] = buchungDto.getCBelegnummer();
				if (buchungdetailDtos[j].getBuchungdetailartCNr().equals(
						BuchenFac.SollBuchung))
					data[j][4] = Helper.formatZahl(
							buchungdetailDtos[j].getNBetrag(),
							FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance()
									.getTheClient().getLocUi());
				else
					data[j][4] = Helper.formatZahl(0,
							FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance()
									.getTheClient().getLocUi());

				if (buchungdetailDtos[j].getBuchungdetailartCNr().equals(
						BuchenFac.HabenBuchung))
					data[j][5] = Helper.formatZahl(
							buchungdetailDtos[j].getNBetrag(),
							FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance()
									.getTheClient().getLocUi());
				else
					data[j][5] = Helper.formatZahl(0,
							FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance()
									.getTheClient().getLocUi());
				data[j][6] = Helper.formatZahl(buchungdetailDtos[j].getNUst(),
						FinanzFac.NACHKOMMASTELLEN, LPMain.getInstance()
								.getTheClient().getLocUi());
			}
			if (editierMode) {
				if (tpKonten != null)
					tpKonten.getPanelQueryBuchungen().eventActionRefresh(null,
							false);
				tpKonten = null;
				this.reset();
				jtaBuchungen.setModel(new DefaultTableModel(data,
						getColumnNames()));
				getInternalFrame().closePanelDialog();
			} else {
				this.reset();
				jtaBuchungen.setModel(new DefaultTableModel(data,
						getColumnNames()));
			}
		}
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
			dialogQueryKonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_GEGENKONTO)) {
			dialogQueryGegenkonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_ERZEUGEN)) {
			erzeugeBelegnummer();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RB_BELEG)) {
			boolean auto = wrbBelegAuto.isSelected();
			wtfBeleg.setEditable(!auto);
			wtfBeleg.setMandatoryField(!auto);
			if (auto)
				wtfBeleg.setText(null);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RB_GEGENKONTO)) {
			String newKontotyp = null;
			if (wrbGegenkontoDebitorenkonto.isSelected()) {
				newKontotyp = FinanzServiceFac.KONTOTYP_DEBITOR;
			} else if (wrbGegenkontoKreditorenkonto.isSelected()) {
				newKontotyp = FinanzServiceFac.KONTOTYP_KREDITOR;
			} else {
				newKontotyp = FinanzServiceFac.KONTOTYP_SACHKONTO;
			}
			// hat er sich geaendert?
			if (!kontotypGegenkonto.equals(newKontotyp)) {
				kontotypGegenkonto = newKontotyp;
				gegenkontoDto = null;
				dto2ComponentsGegenkonto();
				setSteuersatz(false);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RB_KONTO)) {
			String newKontotyp = null;
			if (wrbKontoDebitorenkonto.isSelected()) {
				newKontotyp = FinanzServiceFac.KONTOTYP_DEBITOR;
			} else if (wrbKontoKreditorenkonto.isSelected()) {
				newKontotyp = FinanzServiceFac.KONTOTYP_KREDITOR;
			} else {
				newKontotyp = FinanzServiceFac.KONTOTYP_SACHKONTO;
			}
			// hat er sich geaendert?
			if (!kontotypKonto.equals(newKontotyp)) {
				kontotypKonto = newKontotyp;
				kontoDto = null;
				dto2ComponentsKonto();
				setSteuersatz(false);
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CO_WAEHRUNG)) {
			if (wcoWaehrung.getSelectedItem() != null) {
				wlaWaehrungBetrag.setText(wcoWaehrung.getSelectedItem()
						.toString());
				wlaWaehrungUST
						.setText(wcoWaehrung.getSelectedItem().toString());
				setKursBetrag();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CO_UST)) {
			if (wcoUst.getSelectedItem() != null) {
				calc();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			this.eventActionSave(e, false);
		} else if (e.getActionCommand().equals(PanelBasis.ESC)
				|| e.getActionCommand()
						.equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			if (tpKonten != null)
				tpKonten.getPanelQueryBuchungen().eventActionRefresh(null,
						false);
			tpKonten = null;
			getInternalFrame().closePanelDialog();

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_TAUSCHEN)) {
			tauscheKonten();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_BUCHUNGSART)) {
			setAutoAuszugNummer();
		}
	}

	private void setKursBetrag() {
		try {
			if (wcoWaehrung.getKeyOfSelectedItem().equals(
					LPMain.getTheClient().getSMandantenwaehrung())) {
				wlaKursBetrag.setText(null);
			} else {
				kursDto = DelegateFactory
						.getInstance()
						.getLocaleDelegate()
						.getKursZuDatum(
								(String) wcoWaehrung.getKeyOfSelectedItem(),
								LPMain.getTheClient().getSMandantenwaehrung(),
								wdfDatum.getDate());
				BigDecimal betragMandant = wnfBetrag.getBigDecimal();
				if (kursDto.getNKurs().compareTo(new BigDecimal(1)) != 0) {
					betragMandant = Helper.rundeKaufmaennisch(
							betragMandant.multiply(kursDto.getNKurs()),
							FinanzFac.NACHKOMMASTELLEN);
				}
				wlaKursBetrag.setText("Kurs: " + kursDto.getNKurs()
						+ "  Betrag: " + betragMandant + " "
						+ LPMain.getTheClient().getSMandantenwaehrung());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void tauscheKonten() throws ExceptionLP {
		boolean helperSachkonto = true;
		boolean helperKredi = true;
		boolean helperDebi = true;
		Integer auszug = wnfAuszug.getInteger();
		Integer auszugGkto = wnfAuszugGegenkonto.getInteger();

		KontoDto helperKonto = new KontoDto();

		helperSachkonto = wrbKontoSachkonto.isSelected();
		helperKredi = wrbKontoKreditorenkonto.isSelected();
		helperDebi = wrbKontoDebitorenkonto.isSelected();

		wrbKontoSachkonto.setSelected(wrbGegenkontoSachkonto.isSelected());
		wrbKontoKreditorenkonto.setSelected(wrbGegenkontoKreditorenkonto
				.isSelected());
		wrbKontoDebitorenkonto.setSelected(wrbGegenkontoDebitorenkonto
				.isSelected());

		wrbGegenkontoSachkonto.setSelected(helperSachkonto);
		wrbGegenkontoKreditorenkonto.setSelected(helperKredi);
		wrbGegenkontoDebitorenkonto.setSelected(helperDebi);

		helperKonto = gegenkontoDto;
		gegenkontoDto = kontoDto;
		kontoDto = helperKonto;

		kontotypKonto = getKontoTyp();

		if (kontoDto != null) {
			wtfKontoNummer.setText(kontoDto.getCNr());
			wtfKontoBezeichnung.setText(kontoDto.getCBez());
		} else {
			wtfKontoNummer.setText("");
			wtfKontoBezeichnung.setText("");
		}

		kontotypGegenkonto = getGegenkontoKontoTyp();
		dto2ComponentsGegenkonto();
		dto2ComponentsKonto();

		if (gegenkontoDto != null) {
			wtfGegenkontoNummer.setText(gegenkontoDto.getCNr());
			wtfGegenkontoBezeichnung.setText(gegenkontoDto.getCBez());
		} else {
			wtfGegenkontoNummer.setText("");
			wtfGegenkontoBezeichnung.setText("");
		}
		if (wnfAuszug.isVisible())
			wnfAuszug.setInteger(auszugGkto);
		if (wnfAuszugGegenkonto.isVisible())
			wnfAuszugGegenkonto.setInteger(auszug);
	}

	private String getKontoTyp() {
		if (wrbKontoDebitorenkonto.isSelected()) {
			return FinanzServiceFac.KONTOTYP_DEBITOR;
		} else if (wrbKontoKreditorenkonto.isSelected()) {
			return FinanzServiceFac.KONTOTYP_KREDITOR;
		} else {
			return FinanzServiceFac.KONTOTYP_SACHKONTO;
		}
	}

	private String getGegenkontoKontoTyp() {
		if (wrbGegenkontoDebitorenkonto.isSelected()) {
			return FinanzServiceFac.KONTOTYP_DEBITOR;
		} else if (wrbGegenkontoKreditorenkonto.isSelected()) {
			return FinanzServiceFac.KONTOTYP_KREDITOR;
		} else {
			return FinanzServiceFac.KONTOTYP_SACHKONTO;
		}
	}

	/**
	 * erzeuge eine neue Belegnummer.
	 * 
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	private void erzeugeBelegnummer() throws Throwable {
		wtfBeleg.setText(DelegateFactory
				.getInstance()
				.getBuchenDelegate()
				.getBelegnummerUmbuchung(
						getInternalFrameFinanz().getIAktuellesGeschaeftsjahr()));
	}

	private void components2Dto() throws Throwable {
		// zuerst die Kopfdaten der Buchung
		if (!editierMode)
			buchungDto = new BuchungDto();
		buchungDto.setBuchungsartCNr(FinanzFac.BUCHUNGSART_UMBUCHUNG);
		buchungDto.setCBelegnummer(wtfBeleg.getText());
		buchungDto.setCText(wtfText.getText());
		buchungDto.setDBuchungsdatum(wdfDatum.getDate());
		buchungDto.setKostenstelleIId(kostenstelleDto.getIId());
		buchungDto.setBuchungsartCNr((String) wcoBuchungsart
				.getKeyOfSelectedItem());
		buchungDto.setIGeschaeftsjahr(getInternalFrameFinanz()
				.getIAktuellesGeschaeftsjahr());

		BigDecimal zero = new BigDecimal(0);

		/**
		 * @todo weiterfuehrendes UST konto holen, falls noch nicht definiert PJ
		 *       4979
		 */
		Integer ustKontoIId = null; // gegenkontoDto.getKontoIIdWeiterfuehrendUst();
		boolean bSteuerSoll = false;
		if (wcoUst.isEnabled() && wnfUST.getBigDecimal().floatValue() != 0.0) {
			if (kontoDto.getKontotypCNr().equals(
					FinanzServiceFac.KONTOTYP_DEBITOR)
					|| kontoDto.getKontotypCNr().equals(
							FinanzServiceFac.KONTOTYP_KREDITOR)) {
				// Brutto ist Soll, Steuer in Haben buchen
				bSteuerSoll = false;
				if (kontoDto.getKontotypCNr().equals(
						FinanzServiceFac.KONTOTYP_DEBITOR))
					ustKontoIId = DelegateFactory
							.getInstance()
							.getFinanzServiceDelegate()
							.getUstKontoFuerSteuerkategorie(
									kontoDto.getSteuerkategorieIId(),
									mwstsatzDto.getIIMwstsatzbezId());
				else if (kontoDto.getKontotypCNr().equals(
						FinanzServiceFac.KONTOTYP_KREDITOR))
					ustKontoIId = DelegateFactory
							.getInstance()
							.getFinanzServiceDelegate()
							.getVstKontoFuerSteuerkategorie(
									kontoDto.getSteuerkategorieIId(),
									mwstsatzDto.getIIMwstsatzbezId());
			}

			if (gegenkontoDto.getKontotypCNr().equals(
					FinanzServiceFac.KONTOTYP_DEBITOR)
					|| gegenkontoDto.getKontotypCNr().equals(
							FinanzServiceFac.KONTOTYP_KREDITOR)) {
				// Brutto ist Haben, Steuer in Soll buchen
				bSteuerSoll = true;
				if (gegenkontoDto.getKontotypCNr().equals(
						FinanzServiceFac.KONTOTYP_DEBITOR))
					ustKontoIId = DelegateFactory
							.getInstance()
							.getFinanzServiceDelegate()
							.getUstKontoFuerSteuerkategorie(
									gegenkontoDto.getSteuerkategorieIId(),
									mwstsatzDto.getIIMwstsatzbezId());
				else if (gegenkontoDto.getKontotypCNr().equals(
						FinanzServiceFac.KONTOTYP_KREDITOR))
					ustKontoIId = DelegateFactory
							.getInstance()
							.getFinanzServiceDelegate()
							.getVstKontoFuerSteuerkategorie(
									gegenkontoDto.getSteuerkategorieIId(),
									mwstsatzDto.getIIMwstsatzbezId());
			}
		}
		if (ustKontoIId == null) {
			// keine Ust buchen
			buchungdetailDtos = new BuchungdetailDto[2];
		} else {
			buchungdetailDtos = new BuchungdetailDto[3];
			buchungdetailDtos[2] = new BuchungdetailDto();
		}

		Integer auszug = wnfAuszug.getInteger();
		if (auszug == null)
			auszug = wnfAuszugGegenkonto.getInteger();
		buchungdetailDtos[0] = new BuchungdetailDto();
		buchungdetailDtos[1] = new BuchungdetailDto();

		buchungdetailDtos[0].setKommentar(wefKommentar.getText());
		buchungdetailDtos[0].setKontoIId(kontoDto.getIId());
		buchungdetailDtos[0].setKontoIIdGegenkonto(gegenkontoDto.getIId());
		buchungdetailDtos[0].setBuchungdetailartCNr(BuchenFac.SollBuchung);
		boolean bUmrechnen = !wcoWaehrung.getKeyOfSelectedItem().equals(
				LPMain.getTheClient().getSMandantenwaehrung())
				&& kursDto != null;
		BigDecimal betragNettoMandant = null;
		BigDecimal betragMandant = null;
		BigDecimal betragUstMandant = null;
		if (bUmrechnen) {
			// in Mandantenwaehrung umrechnen
			betragNettoMandant = Helper.rundeKaufmaennisch(
					wnfBetrag.getBigDecimal().subtract(wnfUST.getBigDecimal())
							.multiply(kursDto.getNKurs()),
					FinanzFac.NACHKOMMASTELLEN);
			betragMandant = Helper.rundeKaufmaennisch(wnfBetrag.getBigDecimal()
					.multiply(kursDto.getNKurs()), FinanzFac.NACHKOMMASTELLEN);
			betragUstMandant = Helper.rundeKaufmaennisch(wnfUST.getBigDecimal()
					.multiply(kursDto.getNKurs()), FinanzFac.NACHKOMMASTELLEN);
		}

		if (bSteuerSoll) {
			if (bUmrechnen) {
				buchungdetailDtos[0].setNBetrag(betragNettoMandant);
			} else {
				buchungdetailDtos[0].setNBetrag(wnfBetrag.getBigDecimal()
						.subtract(wnfUST.getBigDecimal()));
			}
			buchungdetailDtos[0].setNUst(zero);
		} else {
			if (bUmrechnen) {
				// in Mandantenwaehrung umrechnen
				buchungdetailDtos[0].setNBetrag(betragMandant);
				buchungdetailDtos[0].setNUst(betragUstMandant);
			} else {
				buchungdetailDtos[0].setNBetrag(wnfBetrag.getBigDecimal());
				buchungdetailDtos[0].setNUst(wnfUST.getBigDecimal());
			}
		}
		buchungdetailDtos[0].setIAuszug(auszug);

		buchungdetailDtos[1].setKontoIId(gegenkontoDto.getIId());
		buchungdetailDtos[1].setKontoIIdGegenkonto(kontoDto.getIId());
		buchungdetailDtos[1].setBuchungdetailartCNr(BuchenFac.HabenBuchung);
		buchungdetailDtos[1].setKommentar(wefKommentar.getText());
		if (bSteuerSoll) {
			if (bUmrechnen) {
				buchungdetailDtos[1].setNBetrag(betragMandant);
				buchungdetailDtos[1].setNUst(betragUstMandant);
			} else {
				buchungdetailDtos[1].setNBetrag(wnfBetrag.getBigDecimal());
				buchungdetailDtos[1].setNUst(wnfUST.getBigDecimal());
			}
		} else {
			if (bUmrechnen) {
				buchungdetailDtos[1].setNBetrag(betragNettoMandant);
			} else {
				buchungdetailDtos[1].setNBetrag(wnfBetrag.getBigDecimal()
						.subtract(wnfUST.getBigDecimal()));
			}
			buchungdetailDtos[1].setNUst(zero);
		}
		buchungdetailDtos[1].setIAuszug(wnfAuszugGegenkonto.getInteger());

		if (ustKontoIId != null) {
			buchungdetailDtos[2].setKontoIId(ustKontoIId);
			buchungdetailDtos[2].setKontoIIdGegenkonto(kontoDto.getIId());
			buchungdetailDtos[2].setKommentar(wefKommentar.getText());
			if (bSteuerSoll)
				buchungdetailDtos[2]
						.setBuchungdetailartCNr(BuchenFac.SollBuchung);
			else
				buchungdetailDtos[2]
						.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
			if (bUmrechnen) {
				buchungdetailDtos[2].setNBetrag(betragUstMandant);
			} else {
				buchungdetailDtos[2].setNBetrag(wnfUST.getBigDecimal());
			}
			buchungdetailDtos[2].setNUst(zero);
			buchungdetailDtos[2].setIAuszug(auszug);
		}
	}

	private void dto2Components() throws ExceptionLP, Throwable {
		wcoBuchungsart.setSelectedItem(buchungDto.getBuchungsartCNr());
		wtfBeleg.setText(buchungDto.getCBelegnummer());
		wtfText.setText(buchungDto.getCText());
		wdfDatum.setDate(buchungDto.getDBuchungsdatum());
		kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
				.kostenstelleFindByPrimaryKey(buchungDto.getKostenstelleIId());
		dto2ComponentsKostenstelle();
		kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
				.kontoFindByPrimaryKey(buchungdetailDtos[0].getKontoIId());
		setRadioKontotyp(kontoDto, new WrapperRadioButton[] {
				wrbKontoSachkonto, wrbKontoDebitorenkonto,
				wrbKontoKreditorenkonto });
		dto2ComponentsKonto();
		if (buchungdetailDtos[0].getKontoIIdGegenkonto() == null) {
			gegenkontoDto = DelegateFactory.getInstance().getFinanzDelegate()
					.kontoFindByPrimaryKey(buchungdetailDtos[1].getKontoIId());
		} else {
			gegenkontoDto = DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.kontoFindByPrimaryKey(
							buchungdetailDtos[0].getKontoIIdGegenkonto());
		}
		setRadioKontotyp(gegenkontoDto, new WrapperRadioButton[] {
				wrbGegenkontoSachkonto, wrbGegenkontoDebitorenkonto,
				wrbGegenkontoKreditorenkonto });
		dto2ComponentsGegenkonto();
		wnfAuszug.setInteger(buchungdetailDtos[0].getIAuszug());
		wefKommentar.setText(buchungdetailDtos[0].getKommentar());
		wnfAuszugGegenkonto.setInteger(buchungdetailDtos[1].getIAuszug());
		wnfBetrag.setBigDecimal(buchungdetailDtos[0].getNBetrag());
		wnfUST.setBigDecimal(buchungdetailDtos[0].getNUst());
		wrbBelegHand.setSelected(true);
		setSteuersatz(wnfUST.getBigDecimal().signum() == 0);
		// buchungDto.setBuchungsartCNr((String)
		// wcoBuchungsart.getKeyOfSelectedItem());
		// buchungDto.setIGeschaeftsjahr(getInternalFrameFinanz().getIAktuellesGeschaeftsjahr());
	}

	private void setRadioKontotyp(KontoDto kontoDtoI,
			WrapperRadioButton[] radios) {
		if (kontoDtoI.getKontotypCNr().equals(
				FinanzServiceFac.KONTOTYP_SACHKONTO)) {
			radios[0].setSelected(true);
		} else if (kontoDtoI.getKontotypCNr().equals(
				FinanzServiceFac.KONTOTYP_DEBITOR)) {
			radios[1].setSelected(true);
		} else if (kontoDtoI.getKontotypCNr().equals(
				FinanzServiceFac.KONTOTYP_KREDITOR)) {
			radios[2].setSelected(true);
		}
	}

	private void dto2ComponentsKostenstelle() {
		if (kostenstelleDto != null) {
			wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
			wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
		}
	}

	private void dto2ComponentsKonto() throws ExceptionLP {
		if (kontoDto != null) {
			wtfKontoNummer.setText(kontoDto.getCNr());
			wtfKontoBezeichnung.setText(kontoDto.getCBez());
			// bankvebindung suchen
			if (DelegateFactory.getInstance().getFinanzDelegate()
					.bankverbindungFindByKontoIIdOhneExc(kontoDto.getIId()) != null) {
				// ist eine Bank
				wnfAuszug.setMandatoryField(true);
				wnfAuszug.setVisible(true);
				wlaAuszug.setVisible(true);
				setAutoAuszugNummer();
			} else {
				wnfAuszug.setMandatoryField(false);
				wnfAuszug.setVisible(false);
				wnfAuszug.setBigDecimal(null);
				wlaAuszug.setVisible(false);
			}
		} else {
			wnfAuszug.setMandatoryField(false);
			wnfAuszug.setVisible(false);
			wnfAuszug.setBigDecimal(null);
			wlaAuszug.setVisible(false);
			wtfKontoNummer.setText(null);
			wtfKontoBezeichnung.setText(null);
		}
	}

	private void dto2ComponentsGegenkonto() throws ExceptionLP {
		if (gegenkontoDto != null) {
			wtfGegenkontoNummer.setText(gegenkontoDto.getCNr());
			wtfGegenkontoBezeichnung.setText(gegenkontoDto.getCBez());
			if (DelegateFactory
					.getInstance()
					.getFinanzDelegate()
					.bankverbindungFindByKontoIIdOhneExc(gegenkontoDto.getIId()) != null) {
				// ist eine Bank
				wnfAuszugGegenkonto.setMandatoryField(true);
				wnfAuszugGegenkonto.setVisible(true);
				wlaAuszugGegenkonto.setVisible(true);
				setAutoAuszugNummer();
			} else {
				wnfAuszugGegenkonto.setMandatoryField(false);
				wnfAuszugGegenkonto.setVisible(false);
				wnfAuszugGegenkonto.setBigDecimal(null);
				wlaAuszugGegenkonto.setVisible(false);
			}
		} else {
			wnfAuszugGegenkonto.setMandatoryField(false);
			wnfAuszugGegenkonto.setBigDecimal(null);
			wnfAuszugGegenkonto.setVisible(false);
			wlaAuszugGegenkonto.setVisible(false);
			wtfGegenkontoNummer.setText(null);
			wtfGegenkontoBezeichnung.setText(null);
		}
	}

	void dialogQueryKostenstelle(ActionEvent e) throws Throwable {
		panelQueryFLRKostenstelle = SystemFilterFactory.getInstance()
				.createPanelFLRKostenstelle(getInternalFrame(), false, false);
		if (kostenstelleDto != null) {
			panelQueryFLRKostenstelle.setSelectedId(kostenstelleDto.getIId());
		}
		new DialogQuery(panelQueryFLRKostenstelle);
	}

	void dialogQueryKonto(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance()
				.createFKKonten(this.kontotypKonto);
		panelQueryFLRKonto = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"finanz.liste.konten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		panelQueryFLRKonto.befuellePanelFilterkriterienDirekt(fkDirekt1,
				fkDirekt2);
		new DialogQuery(panelQueryFLRKonto);
	}

	void dialogQueryGegenkonto(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, };
		QueryType[] qt = null;
		// nur Sachkonten dieses Mandanten
		FilterKriterium[] filters = FinanzFilterFactory.getInstance()
				.createFKKonten(this.kontotypGegenkonto);
		;
		panelQueryFLRGegenkonto = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
						"finanz.liste.konten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		panelQueryFLRGegenkonto.befuellePanelFilterkriterienDirekt(fkDirekt1,
				fkDirekt2);
		new DialogQuery(panelQueryFLRGegenkonto);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRKostenstelle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				kostenstelleDto = DelegateFactory.getInstance()
						.getSystemDelegate()
						.kostenstelleFindByPrimaryKey((Integer) key);
				dto2ComponentsKostenstelle();
			} else if (e.getSource() == panelQueryFLRKonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
						.kontoFindByPrimaryKey((Integer) key);
				dto2ComponentsKonto();
			} else if (e.getSource() == panelQueryFLRGegenkonto) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				gegenkontoDto = DelegateFactory.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKey((Integer) key);
				dto2ComponentsGegenkonto();
			}
		}
	}

	/**
	 * Sobald eine UST eingegeben wurde, wird die UST in Prozent berechnet
	 * 
	 * @throws Throwable
	 */
	void wnfBetrag_focusLost() throws Throwable {
		calc();
	}

	private void calc() throws Throwable {
		if (wdfDatum.getTimestamp() == null) {
			mwstsatzDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzFindByMwstsatzbezIIdAktuellster(
							(Integer) wcoUst.getKeyOfSelectedItem());
		} else {
			mwstsatzDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mwstsatzFindZuDatum(
							(Integer) wcoUst.getKeyOfSelectedItem(),
							wdfDatum.getTimestamp());
		}
		if (mwstsatzDto == null) {
			wnfUST.setBigDecimal(null);
			JOptionPane.showMessageDialog(this, "Mwstsatz zu Datum "
					+ wdfDatum.getTimestamp().toString()
					+ " kann nicht bestimmt werden");
		} else {
			Double dMwstSatz = mwstsatzDto.getFMwstsatz();
			BigDecimal bdMwstSatz = (new BigDecimal(dMwstSatz.doubleValue()))
					.divide(new BigDecimal(100), FinanzFac.NACHKOMMASTELLEN,
							BigDecimal.ROUND_HALF_EVEN);
			if (wnfBetrag.getBigDecimal() != null) {
				BigDecimal bdBetragBasis = wnfBetrag.getBigDecimal().divide(
						new BigDecimal(1).add(bdMwstSatz),
						FinanzFac.NACHKOMMASTELLEN, BigDecimal.ROUND_HALF_EVEN);
				wnfUST.setBigDecimal(bdBetragBasis.multiply(bdMwstSatz));
			} else {
				wnfUST.setBigDecimal(null);
			}
		}
		setKursBetrag();
	}

	void reset() throws Throwable {
		this.editierMode = false;
		this.wnfBetrag.setBigDecimal(new BigDecimal(0.00));
		this.wnfUST.setBigDecimal(new BigDecimal(0.00));
		jtaBuchungen.setModel(new DefaultTableModel(null, getColumnNames()));
		wtfBeleg.removeContent();
		wefKommentar.removeContent();
		wcoBuchungsart.setEnabled(true);

		if (gegenkontoDto != null) {
			gegenkontoDto = DelegateFactory.getInstance().getFinanzDelegate()
					.kontoFindByPrimaryKey(gegenkontoDto.getIId());
		}
		if (kontoDto != null) {
			kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
					.kontoFindByPrimaryKey(kontoDto.getIId());
		}
		setSteuersatz(true);
		setKursBetrag();
	}

	private void setSteuersatz(boolean bsetSteuerfrei) throws ExceptionLP,
			Throwable {
		MwstsatzbezDto mwstbezDto = DelegateFactory.getInstance()
				.getMandantDelegate().getMwstsatzbezSteuerfrei();
		boolean bSteuerzulaessig = false;
		if (wrbKontoSachkonto.isSelected() != wrbGegenkontoSachkonto.isSelected())
			bSteuerzulaessig = true;
		wcoUst.setEnabled(bSteuerzulaessig);

		if (!bSteuerzulaessig || bsetSteuerfrei)
			if (mwstbezDto != null) {
				wcoUst.setSelectedItem(mwstbezDto.getCBezeichnung());
			}
	}

	private String[] getColumnNames() {
		String[] columnNames = new String[7];
		columnNames[0] = LPMain.getInstance().getTextRespectUISPr("lp.datum");
		columnNames[1] = LPMain.getInstance().getTextRespectUISPr("lp.konto");
		columnNames[2] = LPMain.getInstance().getTextRespectUISPr(
				"finanz.gegenkonto");
		columnNames[3] = LPMain.getInstance().getTextRespectUISPr("lp.beleg");
		columnNames[4] = LPMain.getInstance()
				.getTextRespectUISPr("finanz.soll");
		columnNames[5] = LPMain.getInstance().getTextRespectUISPr(
				"finanz.haben");
		columnNames[6] = LPMain.getInstance().getTextRespectUISPr("label.mwst");
		return columnNames;
	}

	protected javax.swing.JComponent getFirstFocusableComponent()
			throws Exception {
		return wdfDatum;
	}

	public void wdfDatum_propertyChanged() throws ExceptionLP {
		setKursBetrag();
		if (bAutoAuszugsnummer)
			setAutoAuszugNummer();
	}

	public void setBuchungDto(BuchungDto buchungDtoI, TabbedPaneKonten tpKonten)
			throws ExceptionLP, Throwable {
		this.tpKonten = tpKonten;
		buchungDto = buchungDtoI;
		buchungdetailDtos = DelegateFactory.getInstance().getBuchenDelegate()
				.buchungdetailsFindByBuchungIId(buchungDto.getIId());
		dto2Components();
		editierMode = true;
	}

}

class PanelFinanzUmbuchung_wnfBetrag_focusAdapter implements
		java.awt.event.FocusListener {
	PanelFinanzUmbuchung adaptee;

	PanelFinanzUmbuchung_wnfBetrag_focusAdapter(PanelFinanzUmbuchung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfBetrag_focusLost();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}

class PanelFinanzUmbuchung_wdfDatum_propertyChange implements
		PropertyChangeListener {
	PanelFinanzUmbuchung adaptee;

	public PanelFinanzUmbuchung_wdfDatum_propertyChange(
			PanelFinanzUmbuchung adaptee) {
		this.adaptee = adaptee;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		try {
			if (evt.getPropertyName().equals("date"))
				adaptee.wdfDatum_propertyChanged();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}
}
