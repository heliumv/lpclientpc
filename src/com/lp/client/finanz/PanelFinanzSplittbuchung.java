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
package com.lp.client.finanz;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

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
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGeschaeftsjahrDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.component.WrapperTextNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.util.Helper;

/**
 * <p>
 * &UUml;berschrift:
 * </p>
 * <p>
 * Beschreibung:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation:
 * </p>
 * 
 * @author unbekannt
 * @version $Revision: 1.5 $
 */
public class PanelFinanzSplittbuchung extends PanelDialogKriterien implements ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BuchungDto buchungDto = null;
	private BuchungdetailDto[] buchungdetailDtos = null;
	private KostenstelleDto kostenstelleDto = null;
	private KontoDto kontoDto = null;

	private TabbedPaneKonten tpKonten = null;

	// Default wird auf sachkonten umgebucht
	private String kontotypKonto = FinanzServiceFac.KONTOTYP_SACHKONTO;

	private PanelQueryFLR panelQueryFLRKonto = null;
	private PanelQueryFLR panelQueryFLRKostenstelle = null;

	private ArrayList<BuchungdetailDto> detailList = new ArrayList<BuchungdetailDto>();

	private static final String ACTION_SPECIAL_KONTO = "action_special_ub_konto";
	private static final String ACTION_SPECIAL_KOSTENSTELLE = "action_special_ub_kostenstelle";
	private static final String ACTION_SPECIAL_RB_KONTO = "action_special_ub_rb_konto";
	private static final String ACTION_SPECIAL_RB_BELEG = "action_special_ub_rb_beleg";
	private static final String ACTION_SPECIAL_CO_WAEHRUNG = "action_special_ub_co_waehrung";
	private static final String ACTION_SPECIAL_NEW = "action_special_new";
	private static final String ACTION_SPECIAL_SAVE = "action_special_save";
	private static final String ACTION_SPECIAL_UPDATE = "action_special_update";
	private static final String ACTION_SPECIAL_DISCARD = "action_special_discard";
	private static final String ACTION_SPECIAL_DELETE = "action_special_delete";

	private int buttonLockState = LOCK_IS_NOT_LOCKED;

	private WrapperCheckBox wcbInSplittbuchungBleiben = new WrapperCheckBox();
	private WrapperGeschaeftsjahrDateField wdfDatum = null;

	private WrapperLabel wlaBetragSoll = new WrapperLabel();
	private WrapperNumberField wnfBetragSoll = new WrapperNumberField();
	private WrapperLabel wlaBetragHaben = new WrapperLabel();
	private WrapperNumberField wnfBetragHaben = new WrapperNumberField();
	private WrapperLabel wlaSaldo = new WrapperLabel();
	private WrapperNumberField wnfSaldo = new WrapperNumberField();

	private WrapperLabel wlaTeilBetrag = new WrapperLabel();
	private WrapperNumberField wnfTeilBetrag = new WrapperNumberField();
	private WrapperComboBox wcbSollHaben = new WrapperComboBox();

	private WrapperRadioButton wrbKontoSachkonto;
	private WrapperRadioButton wrbKontoDebitorenkonto;
	private WrapperRadioButton wrbKontoKreditorenkonto;
	private WrapperLabel wlaAbstandOben = new WrapperLabel();
	private WrapperButton wbuKonto = new WrapperButton();
	private WrapperTextField wtfKontoNummer = new WrapperTextField();
	private WrapperTextField wtfKontoBezeichnung = new WrapperTextField();
	private WrapperLabel wlaKonto = new WrapperLabel();
	private WrapperLabel wlaDatum = new WrapperLabel();
	private WrapperLabel wlaAuszug = new WrapperLabel();
	private WrapperTextNumberField wnfAuszug = new WrapperTextNumberField();
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
	private ButtonGroup buttongroupKonto = new ButtonGroup();
	private ButtonGroup buttongroupBeleg = new ButtonGroup();
	private JScrollPane jspBuchungen = new JScrollPane();
	private WrapperTable jtaBuchungen = new WrapperTable(null);
	private WrapperComboBox wcoBuchungsart = new WrapperComboBox();
	private boolean bAutoAuszugsnummer = false;
	private boolean bearbeitungsModus = false;

	public PanelFinanzSplittbuchung(InternalFrame internalFrame)
			throws Throwable {
		super(internalFrame, LPMain.getTextRespectUISPr("fb.menu.umbuchung"),"/com/lp/client/res/server_ok.png");
		jbInit();
		setDefaults();
		initComponents();
		// kein Locking auf diesem Panel

		LockStateValue lockstateValue = new LockStateValue(null, null,
				LOCK_NO_LOCKING);
		this.updateButtons(lockstateValue);
		enableDetailComponents(false);
		setFirstFocusableComponent();
	}

	private void jbInit() throws Throwable {

		LPMain.getInstance();
		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/note_add16x16.png",
						LPMain.getTextRespectUISPr("finanz.splittadd"),
						ACTION_SPECIAL_NEW,
						KeyStroke.getKeyStroke('N',
								java.awt.event.InputEvent.CTRL_MASK),
						RechteFac.RECHT_MODULWEIT_UPDATE);
		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/disk_blue.png",
						LPMain.getTextRespectUISPr("lp.save"),
						ACTION_SPECIAL_SAVE,
						KeyStroke.getKeyStroke('S',
								java.awt.event.InputEvent.CTRL_MASK),
						RechteFac.RECHT_MODULWEIT_UPDATE);
		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/edit.png",
						LPMain.getTextRespectUISPr("lp.edit"),
						ACTION_SPECIAL_UPDATE,
						KeyStroke.getKeyStroke('U',
								java.awt.event.InputEvent.CTRL_MASK),
						RechteFac.RECHT_MODULWEIT_UPDATE);
		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/undo.png",
						LPMain.getTextRespectUISPr("lp.undo"),
						ACTION_SPECIAL_DISCARD,
						KeyStroke.getKeyStroke('Z',
								java.awt.event.InputEvent.CTRL_MASK),
						RechteFac.RECHT_MODULWEIT_UPDATE);
		getToolBar()
				.addButtonLeft(
						"/com/lp/client/res/note_delete16x16.png",
						LPMain.getTextRespectUISPr("finanz.splittdelete"),
						ACTION_SPECIAL_DELETE,
						KeyStroke.getKeyStroke('D',
								java.awt.event.InputEvent.CTRL_MASK), null);
		

		getInternalFrame().addItemChangedListener(this);

		ParametermandantDto pm = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_FINANZ,
						ParameterFac.PARAMETER_AUSZUGSNUMMER_BEI_BANK_ANGEBEN);
		if (pm != null)
			if ((Boolean) pm.getCWertAsObject() == false)
				bAutoAuszugsnummer = true;

		// Splittkopf
		wcbInSplittbuchungBleiben.setText(LPMain
				.getTextRespectUISPr("finanz.inSplittbuchungBleiben"));
		wcbInSplittbuchungBleiben.setPreferredSize(new Dimension(150, 0));
		getToolBar().getToolsPanelLeft().add(wcbInSplittbuchungBleiben);
		wlaDatum.setText(LPMain.getTextRespectUISPr("lp.datum"));
		wdfDatum = new WrapperGeschaeftsjahrDateField(getInternalFrameFinanz()
				.getIAktuellesGeschaeftsjahr());
		wdfDatum.addPropertyChangeListener(new PanelFinanzSplittbuchung_wdfDatum_propertyChange(
				this));
		wdfDatum.addFocusListener(new PanelFinanzSplittbuchung_wtfText_focusAdapter(
				this));
		wdfDatum.setMandatoryField(true);
		wdfDatum.setMandatoryFieldDB(false);

		wlaBetragSoll.setText(LPMain.getTextRespectUISPr("finanz.soll"));
		wnfBetragSoll.setEditable(false);
		wnfBetragSoll.setActivatable(false);
		wlaBetragHaben.setText(LPMain.getTextRespectUISPr("finanz.haben"));
		wnfBetragHaben.setEditable(false);
		wnfBetragHaben.setActivatable(false);
		wlaSaldo.setText(LPMain.getTextRespectUISPr("finanz.saldo"));
		wnfSaldo.setEditable(false);
		wnfSaldo.setActivatable(false);
		wlaText.setText(LPMain.getTextRespectUISPr("lp.text"));
		wtfText.setColumnsMax(FinanzFac.MAX_UMBUCHUNG_TEXT);
		wtfText.setMandatoryField(true);
		wtfText.addFocusListener(new PanelFinanzSplittbuchung_wtfText_focusAdapter(
				this));

		wlaBeleg.setText(LPMain.getTextRespectUISPr("lp.beleg"));
		wrbBelegAuto.setText(LPMain.getTextRespectUISPr("lp.automatisch"));
		wrbBelegHand.setText(LPMain.getTextRespectUISPr("label.handeingabe"));
		wtfBeleg.setColumnsMax(FinanzFac.MAX_UMBUCHUNG_BELEG);
		wtfBeleg.setMandatoryField(true);
		wtfBeleg.addFocusListener(new PanelFinanzSplittbuchung_wnfBeleg_focusAdapter(
				this));

		buttongroupBeleg.add(wrbBelegHand);
		buttongroupBeleg.add(wrbBelegAuto);
		wrbBelegAuto.setActionCommand(ACTION_SPECIAL_RB_BELEG);
		wrbBelegHand.setActionCommand(ACTION_SPECIAL_RB_BELEG);
		wrbBelegAuto.addActionListener(this);
		wrbBelegHand.addActionListener(this);
		wrbBelegHand.setSelected(true);
		wtfBeleg.setEditable(true);

		// Splittdetail
		wrbKontoSachkonto = new WrapperRadioButton(true, LPMain.getTextRespectUISPr("lp.shortcut.sachkonto"));
		wrbKontoDebitorenkonto = new WrapperRadioButton(true, LPMain.getTextRespectUISPr("lp.shortcut.debitorenkonto"));
		wrbKontoKreditorenkonto = new WrapperRadioButton(true, LPMain.getTextRespectUISPr("lp.shortcut.kreditorenkonto"));
		wrbKontoDebitorenkonto.setActionCommand(ACTION_SPECIAL_RB_KONTO);
		wrbKontoKreditorenkonto.setActionCommand(ACTION_SPECIAL_RB_KONTO);
		wrbKontoSachkonto.setActionCommand(ACTION_SPECIAL_RB_KONTO);
		wrbKontoDebitorenkonto.addActionListener(this);
		wrbKontoKreditorenkonto.addActionListener(this);
		wrbKontoSachkonto.addActionListener(this);
		wrbKontoSachkonto.setSelected(true);
		wbuKonto.setText(LPMain.getTextRespectUISPr("button.konto"));
		wbuKonto.setToolTipText(LPMain
				.getTextRespectUISPr("button.konto.tooltip"));
		wbuKonto.setActionCommand(ACTION_SPECIAL_KONTO);
		wbuKonto.addActionListener(this);
		wtfKontoNummer.setMinimumSize(new Dimension(100, 23));
		wtfKontoNummer.setPreferredSize(new Dimension(100, 23));
		wtfKontoNummer.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				try {
					kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
							.kontoFindByCnrKontotypMandantOhneExc(wtfKontoNummer.getText().trim(),
									kontotypKonto, LPMain.getTheClient().getMandant());
					dto2ComponentsKonto();
				} catch (Throwable t) {
					handleException(t, false);
					wtfKontoNummer.removeContent();
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				wtfKontoNummer.selectAll();
			}
		});
		// wtfKontoNummer.setMandatoryField(true);
		wtfKontoBezeichnung.setActivatable(false);
		buttongroupKonto.add(wrbKontoDebitorenkonto);
		buttongroupKonto.add(wrbKontoKreditorenkonto);
		buttongroupKonto.add(wrbKontoSachkonto);
		wlaAuszug.setText(LPMain.getTextRespectUISPr("label.auszug"));
		wnfAuszug.setMaximumDigits(FinanzFac.MAX_UMBUCHUNG_AUSZUG);
		// wnfAuszug.setFractionDigits(0);
		wnfAuszug.setVisible(false);
		wlaAuszug.setVisible(false);
		wbuKostenstelle.setText(LPMain
				.getTextRespectUISPr("button.kostenstelle"));
		wbuKostenstelle.setToolTipText(LPMain
				.getTextRespectUISPr("button.kostenstelle.tooltip"));
		wbuKostenstelle.setActionCommand(ACTION_SPECIAL_KOSTENSTELLE);
		wbuKostenstelle.addActionListener(this);
		// wbuKostenstelle.setMandatoryField(true) ;
		wtfKostenstelleNummer.setActivatable(false);
		wtfKostenstelleNummer.setDependenceField(true);
		wtfKostenstelleNummer.setMandatoryField(true);
		wtfKostenstelleBezeichnung.setActivatable(false);
		wlaTeilBetrag.setText(LPMain.getTextRespectUISPr("label.betrag"));
		// wnfTeilBetrag.setMandatoryField(true);

		// wcbSollHaben.addItem("SOLL");
		// wcbSollHaben.addItem("HABEN");
		wcbSollHaben.addItem(LPMain.getTextRespectUISPr("finanz.soll"));
		wcbSollHaben.addItem(LPMain.getTextRespectUISPr("finanz.haben"));

		/*
		 * wlaAbstandLinks.setMaximumSize(new Dimension(80, 23));
		 * wlaAbstandLinks.setMinimumSize(new Dimension(80, 23));
		 * wlaAbstandLinks.setPreferredSize(new Dimension(80, 23));
		 * wlaAbstandRechts.setMaximumSize(new Dimension(80, 23));
		 * wlaAbstandRechts.setMinimumSize(new Dimension(80, 23));
		 * wlaAbstandRechts.setPreferredSize(new Dimension(80, 23));
		 */
		wcoWaehrung.setActionCommand(ACTION_SPECIAL_CO_WAEHRUNG);
		// wcoWaehrung.setMandatoryField(true);
		wcoWaehrung.addActionListener(this);
		wrapperLabel1.setHorizontalAlignment(SwingConstants.LEFT);
		wrapperLabel2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrungBetrag.setHorizontalAlignment(SwingConstants.LEFT);

		setupTable(jtaBuchungen);
		jspBuchungen.getViewport().add(jtaBuchungen, null);
		jtaBuchungen.getSelectionModel().addListSelectionListener(this);

		// Kopf
		iZeile++;
		jpaWorkingOn.add(wlaDatum, new GridBagConstraints(0, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatum, new GridBagConstraints(1, iZeile, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBetragSoll, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragSoll, new GridBagConstraints(3, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaBetragHaben, new GridBagConstraints(4, iZeile, 1,
				1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfBetragHaben, new GridBagConstraints(5, iZeile, 1,
				1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSaldo, new GridBagConstraints(6, iZeile, 1, 1, 0.1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSaldo, new GridBagConstraints(7, iZeile, 1, 1, 0.2,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaText, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfText, new GridBagConstraints(1, iZeile, 7, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuKostenstelle, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleNummer, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, new GridBagConstraints(2,
				iZeile, 6, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaBeleg, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBelegAuto, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbBelegHand, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBeleg, new GridBagConstraints(3, iZeile, 2, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoBuchungsart, new GridBagConstraints(5, iZeile, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;

		// Detail
		jpaWorkingOn.add(wlaKonto, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wrbKontoSachkonto, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbKontoDebitorenkonto, new GridBagConstraints(1,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wrbKontoKreditorenkonto, new GridBagConstraints(2,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wbuKonto, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoNummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoBezeichnung, new GridBagConstraints(2, iZeile,
				5, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaAuszug, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAuszug, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		jpaWorkingOn.add(wlaTeilBetrag, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfTeilBetrag, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcbSollHaben, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

		// Buchungen Tabelle
		iZeile++;
		jpaWorkingOn.add(wlaAbstandOben, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;

		jpaWorkingOn.add(jspBuchungen, new GridBagConstraints(0, iZeile, 7, 1,
				1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		enableDetailComponents(false);
	}

	private void setupTable(WrapperTable jta) {
		jta.setModel(new SplittModel(detailList));

		TableColumn valueColumn = jta.getColumnModel().getColumn(2);
		valueColumn.setCellRenderer(new FinanceValueCellRenderer());

		valueColumn = jta.getColumnModel().getColumn(3);
		valueColumn.setCellRenderer(new FinanceValueCellRenderer());

		jta.getTableHeader().setDefaultRenderer(
				new FinanceValueHeaderRenderer(jta));
	}

	protected void eventMouseClicked(java.awt.event.MouseEvent e)
			throws Throwable {
		if (e.getSource() == jtaBuchungen && jtaBuchungen.isEnabled()) {
			buchungSelectionChanged();
		}
	};

	@Override
	protected void eventKeyPressed(KeyEvent e) throws Throwable {
	}

	@Override
	protected void eventKeyReleased(KeyEvent e) throws Throwable {
		if (e.getSource() == jtaBuchungen
				&& jtaBuchungen.isEnabled()
				&& (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)) {
			buchungSelectionChanged();
		}
	}

	@Override
	protected void eventKeyTyped(KeyEvent e) throws Throwable {
	}

	private void buchungSelectionChanged() throws Throwable {
		if (jtaBuchungen.getSelectedRowCount() > 0) {
			dto2ComponentsBuchungsdetail(jtaBuchungen.getSelectedRow());
		} else {
			resetDetails();
		}
		setButtons();
	}

	/**
	 * Defaults setzen.
	 * 
	 * @throws Exception
	 * @throws Throwable
	 */
	private void setDefaults() throws Throwable {
		// Waehrungen setzen
		Map<?, ?> waehrungen = DelegateFactory.getInstance()
				.getLocaleDelegate().getAllWaehrungen();
		wcoWaehrung.setMap(waehrungen);
		wcoWaehrung.setKeyOfSelectedItem(LPMain.getTheClient()
				.getSMandantenwaehrung());
		TreeMap<?, ?> buchungsarten = DelegateFactory.getInstance().getFinanzServiceDelegate().getAllBuchungsarten();
		buchungsarten.remove(FinanzFac.BUCHUNGSART_MWST_ABSCHLUSS);
		wcoBuchungsart.setMap(buchungsarten);
		wcoBuchungsart.setKeyOfSelectedItem(FinanzFac.BUCHUNGSART_UMBUCHUNG);
		wdfDatum.setDefaultDate();
		
		
	}

	private void setAutoAuszugNummer() {
		if (wdfDatum.getDate() != null) {
			DateFormat df = new SimpleDateFormat("yyyyMM");
			Integer auszug = Integer.parseInt(df.format(wdfDatum.getDate()));
			wnfAuszug.setInteger(auszug);
			wnfAuszug.setEditable(false);
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

	public void setBuchungDto(BuchungDto buchungDtoI, TabbedPaneKonten tpKonten)
			throws ExceptionLP, Throwable {
		setBuchungDto(buchungDtoI, tpKonten, false);
	}
	
	public void setBuchungDto(BuchungDto buchungDtoI, TabbedPaneKonten tpKonten, boolean bearbeiten) 
			throws ExceptionLP, Throwable {
		bearbeitungsModus = bearbeiten;
		if(bearbeiten == true) {
			wcbInSplittbuchungBleiben.setSelected(false);
		}
		wcbInSplittbuchungBleiben.setVisible(!bearbeiten);
		this.tpKonten = tpKonten;
		buchungDto = buchungDtoI;
		buchungdetailDtos = DelegateFactory
				.getInstance()
				.getBuchenDelegate()
				.buchungdetailsFindByBuchungIIdOhneMitlaufende(
						buchungDto.getIId());
		for(BuchungdetailDto dto : buchungdetailDtos) {
			dto.setIId(0);
		}
		buchungDto.setIId(0);
		dto2Components();
		if(!bearbeiten)
			wdfDatum.removeContent();
//		wcoBuchungsart.setEnabled(false); (rk) ich habe ja mit der buchung
//		vielleicht was ganz anderes vor, warum also die buchungsart vorschreiben?
	}

	protected InternalFrameFinanz getInternalFrameFinanz() {
		return (InternalFrameFinanz) getInternalFrame();
	}

	/**
	 * Speichere Daten des Panels. Kommt von ACTION_SPECIAL_OK Button
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if(bearbeitungsModus) {

				BuchungdetailDto bDto = DelegateFactory
						.getInstance()
						.getBuchenDelegate()
						.buchungdetailFindByPrimaryKey(
								(Integer) tpKonten.getPanelQueryBuchungen().getSelectedId());

				DelegateFactory.getInstance().getBuchenDelegate()
						.storniereBuchung(bDto.getBuchungIId());
			}
			List<BuchungdetailDto> notZeroBetragDetailDtos = new ArrayList<BuchungdetailDto>();
			for(BuchungdetailDto detail : buchungdetailDtos) {
				if(detail.getNBetrag() != null &&
						detail.getNBetrag().signum() != 0) {
					notZeroBetragDetailDtos.add(detail);
				}
			}
			BuchungDto bDto = null;
			if(notZeroBetragDetailDtos.size() > 0) {
				bDto = DelegateFactory.getInstance().getBuchenDelegate()
					.verbucheUmbuchung(buchungDto, notZeroBetragDetailDtos.toArray(new BuchungdetailDto[0]));
			}
			if (!wcbInSplittbuchungBleiben.isSelected()) {
				buchungDto = bDto;
				if (tpKonten != null)
					tpKonten.getPanelQueryBuchungen().eventActionRefresh(null,
							false);
				tpKonten = null;
				getInternalFrame().closePanelDialog();
			} else
				wdfDatum.removeContent();
		}
	}
	
	private boolean showDialogVerwerfen() {
		Object am[] = { LPMain.getTextRespectUISPr("lp.abbrechen"),
				LPMain.getTextRespectUISPr("lp.verwerfen_ohne_frage"), };
		int iOption = DialogFactory.showModalDialog(getInternalFrame(),
						LPMain.getTextRespectUISPr("lp.warning.speichern"), "", am,
						0);
		return (iOption == 1);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO)) {
			dialogQueryKonto(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KOSTENSTELLE)) {
			dialogQueryKostenstelle(e);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_RB_BELEG)) {
			boolean auto = wrbBelegAuto.isSelected();
			wtfBeleg.setEditable(!auto);
			wtfBeleg.setMandatoryField(!auto);
			if (auto)
				wtfBeleg.setText(null);
			setButtons();
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
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CO_WAEHRUNG)) {
			if (wcoWaehrung.getSelectedItem() != null) {
				wlaWaehrungBetrag.setText(wcoWaehrung.getSelectedItem()
						.toString());
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {
			this.eventActionSave(e, false);
		} else if (e.getActionCommand().equals(PanelBasis.ESC)
				|| e.getActionCommand()
						.equals(ACTION_SPECIAL_CLOSE_PANELDIALOG)) {
			if(buttonLockState == LOCK_IS_LOCKED_BY_ME || buttonLockState == LOCK_FOR_NEW) {
				if(!showDialogVerwerfen())
					return;
			}
			reset();
			if (tpKonten != null) {
				tpKonten.getPanelQueryBuchungen().eventActionRefresh(null,
						false);
			}
			tpKonten = null;
			getInternalFrame().closePanelDialog();

		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			if (pruefePflichtDetail()) {
				BuchungdetailDto bdDto = new BuchungdetailDto();
				if (wcbSollHaben.getSelectedIndex() == 0)
					bdDto.setBuchungdetailartCNr(BuchenFac.SollBuchung);
				else
					bdDto.setBuchungdetailartCNr(BuchenFac.HabenBuchung);
				bdDto.setNBetrag(wnfTeilBetrag.getBigDecimal());
				bdDto.setKontoIId(kontoDto.getIId());
				bdDto.setIAuszug(wnfAuszug.getInteger());
				int selectedRow = jtaBuchungen.getSelectedRow();
				// bdDto.setNUst(nUst)
				if (buttonLockState == LOCK_FOR_NEW) {
					detailList.add(bdDto);
				} else {
					detailList.set(jtaBuchungen.getSelectedRow(), bdDto);
				}
				((SplittModel) jtaBuchungen.getModel()).fireTableDataChanged();
				if (buttonLockState == LOCK_FOR_NEW) {
					jtaBuchungen.getSelectionModel().setSelectionInterval(
							detailList.size() - 1, detailList.size());
				} else {
					jtaBuchungen.getSelectionModel().setSelectionInterval(
							selectedRow, selectedRow);
				}
				calc();
				buttonLockState = LOCK_IS_NOT_LOCKED;
				enableDetailComponents(false);
				setButtons();
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_DELETE)) {
			int row = jtaBuchungen.getSelectedRow();
			detailList.remove(row);
			((SplittModel) jtaBuchungen.getModel()).fireTableDataChanged();
			jtaBuchungen.getSelectionModel().setSelectionInterval(
					detailList.size() - 1, detailList.size());
			calc();
			setButtons();
			repaint();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_UPDATE)) {
			buttonLockState = LOCK_IS_LOCKED_BY_ME;
			enableDetailComponents(true);
			setButtons();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_DISCARD)) {
			buttonLockState = LOCK_IS_NOT_LOCKED;
			enableDetailComponents(false);
			resetDetails();
			buchungSelectionChanged();
			setButtons();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_NEW)) {

			buttonLockState = LOCK_FOR_NEW;
			resetDetails();
			enableDetailComponents(true);
			setButtons();
			wtfKontoNummer.requestFocus();
		}
	}

	private boolean pruefePflichtBuchung() throws ExceptionLP {
		if(wdfDatum.getDate() == null) return false;
		if(detailList.size() == 0) return false;
		if(wnfSaldo.getBigDecimal() == null) return false;
		if(wtfText.getText() == null) return false;
		if(wrbBelegHand.isSelected() && wtfBeleg.getText() == null) return false;
		if(wnfSaldo.getBigDecimal().signum() != 0) return false;
		if(wtfKostenstelleNummer.getText() == null) return false;
		
		return true;
	}

	private boolean pruefePflichtDetail() throws ExceptionLP {
		if (wtfKontoNummer.getText() == null)
			return false;
		if (wnfTeilBetrag.getBigDecimal() == null)
			return false;
		return true;
	}

	private void setButtons() throws ExceptionLP {
		if (buttonLockState == LOCK_IS_NOT_LOCKED) {
			enableToolsPanelButtons(true, ACTION_SPECIAL_NEW,
					ACTION_SPECIAL_CLOSE_PANELDIALOG);
			enableToolsPanelButtons(false, ACTION_SPECIAL_SAVE,
					ACTION_SPECIAL_DISCARD);
			enableToolsPanelButtons(jtaBuchungen.getSelectedRowCount() > 0,
					ACTION_SPECIAL_UPDATE, ACTION_SPECIAL_DELETE);

			enableToolsPanelButtons(pruefePflichtBuchung(), ACTION_SPECIAL_OK);

		} else if (buttonLockState == LOCK_FOR_NEW
				|| buttonLockState == LOCK_IS_LOCKED_BY_ME) {
			enableToolsPanelButtons(false, ACTION_SPECIAL_NEW,
					ACTION_SPECIAL_OK, ACTION_SPECIAL_UPDATE,
					ACTION_SPECIAL_DELETE, ACTION_SPECIAL_CLOSE_PANELDIALOG);
			enableToolsPanelButtons(true, ACTION_SPECIAL_SAVE,
					ACTION_SPECIAL_DISCARD);
		}
	}

	private void resetDetails() throws ExceptionLP {
		wtfKontoNummer.setText(null);
		wtfKontoBezeichnung.setText(null);
		// wtfKostenstelleNummer.setText(null);
		// wtfKostenstelleBezeichnung.setText(null);
		wnfTeilBetrag.setBigDecimal(null);
		wnfAuszug.setText(null);
		wlaAuszug.setVisible(false);
		wnfAuszug.setVisible(false);
		wnfAuszug.setMandatoryField(false);
	}

	private void components2Dto() throws Throwable {
		// zuerst die Kopfdaten der Buchung
		buchungDto = new BuchungDto();
		buchungDto.setBuchungsartCNr(wcoBuchungsart.getSelectedItem().toString());
		buchungDto.setCBelegnummer(wtfBeleg.getText());
		buchungDto.setCText(wtfText.getText());
		buchungDto.setDBuchungsdatum(wdfDatum.getDate());
		buchungDto.setKostenstelleIId(kostenstelleDto.getIId());
		buchungDto.setIGeschaeftsjahr(getInternalFrameFinanz()
				.getIAktuellesGeschaeftsjahr());

		// dann die Details
		buchungdetailDtos = new BuchungdetailDto[detailList.size()];
		for (int i = 0; i < detailList.size(); i++) {
			buchungdetailDtos[i] = detailList.get(i);
			buchungdetailDtos[i].setNUst(new BigDecimal(0));
		}
	}

	private void dto2Components() throws ExceptionLP, Throwable {
		wcoBuchungsart.setSelectedItem(buchungDto.getBuchungsartCNr());
		wtfBeleg.setText(buchungDto.getCBelegnummer().trim());
		wtfText.setText(buchungDto.getCText());
		wdfDatum.setDate(buchungDto.getDBuchungsdatum());
		wrbBelegHand.setSelected(true);
		kostenstelleDto = DelegateFactory.getInstance().getSystemDelegate()
				.kostenstelleFindByPrimaryKey(buchungDto.getKostenstelleIId());
		dto2ComponentsKostenstelle();
		kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
				.kontoFindByPrimaryKey(buchungdetailDtos[0].getKontoIId());
		for (int i = 0; i < buchungdetailDtos.length; i++) {
			detailList.add(buchungdetailDtos[i]);
		}
		calc();
	}

	private void dto2ComponentsBuchungsdetail(int i) throws ExceptionLP {
		if (i >= detailList.size()) {
			resetDetails();
			return;
		}
		BuchungdetailDto bdDto = detailList.get(i);
		if (bdDto.getBuchungdetailartCNr().equals(BuchenFac.SollBuchung))
			wcbSollHaben.setSelectedIndex(0);
		else
			wcbSollHaben.setSelectedIndex(1);
		wnfTeilBetrag.setBigDecimal(bdDto.getNBetrag());

		kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
				.kontoFindByPrimaryKey(bdDto.getKontoIId());
		kontotypKonto = kontoDto.getKontotypCNr();

		if (FinanzServiceFac.KONTOTYP_DEBITOR.equals(kontotypKonto)) {
			wrbKontoDebitorenkonto.setSelected(true);
		} else if (FinanzServiceFac.KONTOTYP_KREDITOR.equals(kontotypKonto)) {
			wrbKontoKreditorenkonto.setSelected(true);
		} else if (FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontotypKonto)) {
			wrbKontoSachkonto.setSelected(true);
		}

		wnfAuszug.setInteger(bdDto.getIAuszug());
		dto2ComponentsKonto();
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
				if (bAutoAuszugsnummer)
					setAutoAuszugNummer();
			} else {
				wnfAuszug.setMandatoryField(false);
				wnfAuszug.setVisible(false);
				wnfAuszug.setInteger(null);
				wlaAuszug.setVisible(false);
			}
		} else {
			wnfAuszug.setMandatoryField(false);
			wnfAuszug.setVisible(false);
			wnfAuszug.setInteger(null);
			wlaAuszug.setVisible(false);
			wtfKontoNummer.setText(null);
			wtfKontoBezeichnung.setText(null);
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
		LPMain.getInstance();
		panelQueryFLRKonto = new PanelQueryFLR(qt, filters,
				QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
				getInternalFrame(),
				LPMain.getTextRespectUISPr("finanz.liste.konten"));
		FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance()
				.createFKDKontonummer();
		FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance()
				.createFKDKontobezeichnung();
		panelQueryFLRKonto.befuellePanelFilterkriterienDirekt(fkDirekt1,
				fkDirekt2);
		new DialogQuery(panelQueryFLRKonto);
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
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void enableDetailComponents(boolean enable) {
		wnfAuszug.setEnabled(enable);
		wrbKontoDebitorenkonto.setEnabled(enable);
		wrbKontoKreditorenkonto.setEnabled(enable);
		wrbKontoSachkonto.setEnabled(enable);
		wbuKonto.setEnabled(enable);
		wtfKontoNummer.setEnabled(enable);
		wtfKontoNummer.setMandatoryField(enable);
		wnfTeilBetrag.setEnabled(enable);
		wnfTeilBetrag.setMandatoryField(enable);
		wcbSollHaben.setEnabled(enable);
		jtaBuchungen.setEnabled(!enable);
	}

	private void calc() throws ExceptionLP {
		BigDecimal soll = new BigDecimal(0);
		BigDecimal haben = new BigDecimal(0);
		for (int i = 0; i < detailList.size(); i++) {
			BuchungdetailDto detail = detailList.get(i);
			if (detail.getBuchungdetailartCNr().equals(BuchenFac.SollBuchung))
				soll = soll.add(detail.getNBetrag());
			else
				haben = haben.add(detail.getNBetrag());
		}
		wnfBetragSoll.setBigDecimal(soll);
		wnfBetragHaben.setBigDecimal(haben);
		wnfSaldo.setBigDecimal(soll.subtract(haben));
	}

	void reset() throws Throwable {
		this.wnfBetragSoll.setBigDecimal(BigDecimal.ZERO);
		this.wnfBetragHaben.setBigDecimal(BigDecimal.ZERO);
		detailList = new ArrayList<BuchungdetailDto>();
		buttonLockState = LOCK_IS_NOT_LOCKED;
		resetDetails();
		try {
			leereAlleFelder(this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		setupTable(jtaBuchungen);
		bearbeitungsModus = false;
		wcbInSplittbuchungBleiben.setVisible(true);
		wtfBeleg.removeContent();
		setButtons();

		kostenstelleDto = getInternalFrameFinanz().getDefaultKostenstelle();
		dto2ComponentsKostenstelle();
	}

	protected javax.swing.JComponent getFirstFocusableComponent()
			throws Exception {
		return wdfDatum;
	}

	void wtfText_focusLost() throws Throwable {
		setButtons();
	}

	void wnfBeleg_focusLost() throws Throwable {
		setButtons();
	}

	public void wdfDatum_propertyChanged() throws ExceptionLP {
		if (bAutoAuszugsnummer)
			setAutoAuszugNummer();
		setButtons();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		try {
			buchungSelectionChanged();
		} catch (Throwable e1) {
			//sollte eigentlich nicht passieren
			e1.printStackTrace();
		}
	}
}

class PanelFinanzSplittbuchung_wnfBeleg_focusAdapter implements
		java.awt.event.FocusListener {
	PanelFinanzSplittbuchung adaptee;

	PanelFinanzSplittbuchung_wnfBeleg_focusAdapter(
			PanelFinanzSplittbuchung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wnfBeleg_focusLost();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}

class PanelFinanzSplittbuchung_wtfText_focusAdapter implements
		java.awt.event.FocusListener {
	PanelFinanzSplittbuchung adaptee;

	PanelFinanzSplittbuchung_wtfText_focusAdapter(
			PanelFinanzSplittbuchung adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		try {
			adaptee.wtfText_focusLost();
		} catch (Throwable ex) {
			LPMain.getInstance().exitFrame(adaptee.getInternalFrame());
		}
	}

	public void focusGained(FocusEvent e) {
	}
}

class PanelFinanzSplittbuchung_wdfDatum_propertyChange implements
		PropertyChangeListener {
	PanelFinanzSplittbuchung adaptee;

	public PanelFinanzSplittbuchung_wdfDatum_propertyChange(
			PanelFinanzSplittbuchung adaptee) {
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

class SplittModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6981675392669256442L;
	private List<BuchungdetailDto> list;

	public SplittModel(List<BuchungdetailDto> list) {
		this.list = list;
	}

	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return LPMain.getTextRespectUISPr("lp.konto");
		case 1:
			return LPMain.getTextRespectUISPr("label.auszug");
		case 2:
			return LPMain.getTextRespectUISPr("finanz.soll");
		case 3:
			return LPMain.getTextRespectUISPr("finanz.haben");
		}
		return "";
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return list.size();
	}

	private Object getBetrag(BuchungdetailDto detailDto, String betragTyp) {
		if (betragTyp.equals(detailDto.getBuchungdetailartCNr())) {
			try {
				return Helper.formatAndRoundCurrency(detailDto.getNBetrag(),
						LPMain.getTheClient().getLocUi());
			} catch (Throwable t) {
				return detailDto.getNBetrag();
			}
		}

		return "";
	}

	public Object getValueAt(int row, int col) {
		BuchungdetailDto detail = list.get(row);
		switch (col) {
		case 0:
			KontoDto kontoDto = null;
			try {
				kontoDto = DelegateFactory.getInstance().getFinanzDelegate()
						.kontoFindByPrimaryKey(detail.getKontoIId());
				return kontoDto.getCNr() + ", " + kontoDto.getCBez();
			} catch (ExceptionLP e) {
				//
			}
			return "ID:" + detail.getKontoIId();
		case 1:
			return detail.getIAuszug();
		case 2:
			return getBetrag(detail, BuchenFac.SollBuchung);
		case 3:
			return getBetrag(detail, BuchenFac.HabenBuchung);
		}

		return null;
	}
}

class FinanceValueHeaderRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8998683400300449068L;
	private DefaultTableCellRenderer renderer;

	public FinanceValueHeaderRenderer(JTable table) {
		renderer = (DefaultTableCellRenderer) table.getTableHeader()
				.getDefaultRenderer();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		Component c = renderer.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, col);
		if (col == 2 || col == 3) {
			renderer.setHorizontalAlignment(SwingConstants.RIGHT);
		} else {
			renderer.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return c;
	}
}

class FinanceValueCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8343822995343621612L;

	public int getHorizontalAlignment() {
		return SwingConstants.RIGHT;
	}
}
