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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.BuchungdetailDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Panel zur Anzeige der Details einer Buchung</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>16. 11. 2004</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.22 $
 */
public class PanelFinanzBuchungDetails extends PanelBasis implements
		ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected BuchungdetailDto buchungdetailDto = null;
	protected BuchungDto buchungDto = null;
	// protected KontoDtoSmall kontoDtoGegenkonto = null;

	private TabbedPaneKonten tpKonten = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	protected WrapperLabel wlaGegenkonto = new WrapperLabel();
	protected WrapperTextField wtfGegenkontoNummer = new WrapperTextField();

	protected WrapperGotoButton wbuGegenkonto = new WrapperGotoButton(
			WrapperGotoButton.GOTO_BUCHUNGDETAIL);
	protected WrapperComboBox wcoGegenkonten = new WrapperComboBox();

	protected WrapperLabel wlaGegenkontoName = new WrapperLabel();
	protected WrapperTextField wtfGegenkontoName = new WrapperTextField();
	private WrapperLabel wlaUst = new WrapperLabel();
	protected WrapperLabel wlaSoll = new WrapperLabel();
	private WrapperLabel wlaText = new WrapperLabel();
	private WrapperTextField wtfText = new WrapperTextField();
	private Border border1;
	protected WrapperLabel wlaHaben = new WrapperLabel();
	private WrapperTextField wtfBeleg = new WrapperTextField();
	private WrapperNumberField wtfAuszug = new WrapperNumberField();
	private WrapperLabel wlaBeleg = new WrapperLabel();
	private WrapperLabel wlaAuszug = new WrapperLabel();
	private WrapperLabel wlaKostenstelle = new WrapperLabel();
	private WrapperTextField wtfKostenstelleNummer = new WrapperTextField();
	private WrapperTextField wtfKostenstelleBezeichnung = new WrapperTextField();
	private WrapperLabel wlaWaehrung1 = new WrapperLabel();
	private WrapperLabel wlaWaehrung2 = new WrapperLabel();
	private WrapperLabel wlaWaehrung3 = new WrapperLabel();
	private WrapperLabel wlaProzent = new WrapperLabel();
	private WrapperLabel wlaBuchungsart = new WrapperLabel();
	private WrapperTextField wtfBuchungsart = new WrapperTextField();
	private WrapperTextField wtfBelegart = new WrapperTextField();
	private WrapperNumberField wnfUst = null;
	private WrapperNumberField wnfSoll = null;
	private WrapperNumberField wnfHaben = null;
	private WrapperNumberField wnfUstProzent = null;
	private WrapperLabel wlaAusziffern = new WrapperLabel();;
	private WrapperTextField wtfAusziffern = null;
	private WrapperLabel wlaSaldo = new WrapperLabel();
	private WrapperNumberField wnfSaldoAuszug = null;
	private WrapperNumberField wnfSaldoAusziffern = null;

	JList list = null;
	Map<?, ?> mGegenkonten = new TreeMap<Object, Object>();

	public PanelFinanzBuchungDetails(InternalFrame internalFrame,
			String add2TitleI, TabbedPaneKonten tpKonten) throws Throwable {
		super(internalFrame, add2TitleI);
		this.tpKonten = tpKonten;
		jbInit();
		initComponents();
		PanelBasis.enableAllComponents(this, false);
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {

			if (list.getSelectedIndex() == -1) {
				wbuGegenkonto.setOKey(null);

			} else {
				Iterator<?> it = mGegenkonten.keySet().iterator();
				int i = 0;
				while (it.hasNext()) {

					Integer key = (Integer) it.next();

					if (i == list.getSelectedIndex()) {
						wbuGegenkonto.setOKey(key);
						break;
					}
					i++;
				}

			}
		}
	}

	private void jbInit() throws Throwable {
		wnfUst = new WrapperNumberField();
		wnfSoll = new WrapperNumberField();
		wnfHaben = new WrapperNumberField();
		wnfUstProzent = new WrapperNumberField();
		border1 = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaGegenkonto.setText(LPMain.getTextRespectUISPr("finanz.gegenkonto"));
		wbuGegenkonto.setText(LPMain.getTextRespectUISPr("finanz.gegenkonto"));
		wlaGegenkontoName.setText(LPMain.getTextRespectUISPr("lp.name"));
		wlaUst.setText(LPMain.getTextRespectUISPr("label.mwst"));
		wlaSoll.setText(LPMain.getTextRespectUISPr("finanz.soll"));
		wlaText.setText(LPMain.getTextRespectUISPr("lp.text"));
		wtfText.setActivatable(false);
		jpaWorkingOn.setBorder(border1);
		wlaHaben.setText(LPMain.getTextRespectUISPr("finanz.haben"));
		wtfGegenkontoNummer.setActivatable(false);
		wtfGegenkontoName.setActivatable(false);
		wnfSoll.setActivatable(false);
		wnfHaben.setActivatable(false);
		wnfUst.setActivatable(false);
		wtfBeleg.setActivatable(false);
		wtfAuszug.setActivatable(false);
		wlaBeleg.setText(LPMain.getTextRespectUISPr("lp.beleg"));
		wlaAuszug.setText(LPMain.getTextRespectUISPr("label.auszug"));
		wnfUstProzent.setActivatable(false);
		wlaKostenstelle.setText(LPMain
				.getTextRespectUISPr("label.kostenstelle"));
		wtfKostenstelleNummer.setActivatable(false);
		wtfKostenstelleBezeichnung.setActivatable(false);
//		wlaWaehrung1.setMinimumSize(new Dimension(30, Defaults.getInstance()
//				.getControlHeight()));
		// wlaWaehrung1.setPreferredSize(new Dimension(30,
		// Defaults.getInstance()
		// .getControlHeight()));
		wlaWaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung1.setText(LPMain.getTheClient().getSMandantenwaehrung());
		wlaWaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung2.setText(LPMain.getTheClient().getSMandantenwaehrung());
		wlaWaehrung3.setHorizontalAlignment(SwingConstants.LEFT);
		wlaWaehrung3.setText(LPMain.getTheClient().getSMandantenwaehrung());
		wlaProzent.setHorizontalAlignment(SwingConstants.LEFT);
		wlaProzent.setText("%");
		wlaBuchungsart
				.setText(LPMain.getTextRespectUISPr("finanz.buchungsart"));
		wlaSaldo.setText(LPMain.getTextRespectUISPr("finanz.saldo"));
		wnfSaldoAuszug = new WrapperNumberField();
		wlaAusziffern.setText(LPMain.getTextRespectUISPr("finanz.ausziffern"));
		wtfAusziffern = new WrapperTextField();
		wtfAusziffern.setEditable(false);
		wnfSaldoAusziffern = new WrapperNumberField();

		list = new JList();
		// list.setFont(new java.awt.Font("monospaced", 0, 12));
		list.setFont(new java.awt.Font("monospaced", 0, 11));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
//		listScroller.setMinimumSize(new Dimension(250, 80));
//		listScroller.setPreferredSize(new Dimension(300, 80));
		list.addListSelectionListener(this);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaSoll, new GridBagConstraints(0, iZeile, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wnfSoll, new GridBagConstraints(1, iZeile, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wlaWaehrung1, new GridBagConstraints(2, iZeile, 1, 1,
				0.2, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wlaHaben, new GridBagConstraints(3, iZeile, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wnfHaben, new GridBagConstraints(4, iZeile, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wlaWaehrung2, new GridBagConstraints(5, iZeile, 1, 1,
				0.25, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wlaUst, new GridBagConstraints(6, iZeile, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wnfUst, new GridBagConstraints(7, iZeile, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wlaWaehrung3, new GridBagConstraints(8, iZeile, 1, 1,
				0.25, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wnfUstProzent, new GridBagConstraints(9, iZeile, 1, 1,
				0.25, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 30, 0));
		jpaWorkingOn.add(wlaProzent, new GridBagConstraints(10, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 10, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBuchungsart, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBuchungsart, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBelegart, new GridBagConstraints(2, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaKostenstelle, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleNummer, new GridBagConstraints(4,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, new GridBagConstraints(5,
				iZeile, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAuszug, new GridBagConstraints(8, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAuszug, new GridBagConstraints(9, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaBeleg, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBeleg, new GridBagConstraints(1, iZeile, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaText, new GridBagConstraints(3, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfText, new GridBagConstraints(4, iZeile, 3, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaSaldo, new GridBagConstraints(8, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSaldoAuszug, new GridBagConstraints(9, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wlaGegenkonto, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfGegenkontoNummer, new GridBagConstraints(1, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaGegenkontoName, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfGegenkontoName, new GridBagConstraints(4, iZeile,
				3, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaAusziffern, new GridBagConstraints(7, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfAusziffern, new GridBagConstraints(8, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSaldoAusziffern, new GridBagConstraints(9, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuGegenkonto, new GridBagConstraints(0, iZeile, 2, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(listScroller, new GridBagConstraints(2, iZeile, 9, 1,
				0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 40));

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			Integer pkBuchungdetail = (Integer) ((ISourceEvent) e.getSource())
					.getIdSelected();
			setKeyWhenDetailPanel(pkBuchungdetail);
			dto2Components();
		}
	}

	/**
	 * eventYouAreSelected
	 * 
	 * @param bNeedNoYouAreSelectedI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		this.leereAlleFelder(this);
		dto2Components();
		berechneSaldofuerAuszug();
		berechneSaldofuerAusziffern();

		if (tpKonten != null) {
			LPButtonAction item = (LPButtonAction) tpKonten
					.getPanelQueryBuchungen()
					.getHmOfButtons()
					.get(PanelBasis.LEAVEALONE
							+ TabbedPaneKonten.ACTION_SPECIAL_UMBUCHUNG_AENDERN);
			LPButtonAction itemNeueSplittbuchung = (LPButtonAction) tpKonten
					.getPanelQueryBuchungen()
					.getHmOfButtons()
					.get(PanelBasis.LEAVEALONE
							+ TabbedPaneKonten.ACTION_SPECIAL_NEUE_SPLITTBUCHUNG);
			if (item != null) {
				boolean enable = false;
				if (buchungDto != null) {
					// es gibt Buchungen
					if (!DelegateFactory
							.getInstance()
							.getSystemDelegate()
							.isGeschaeftsjahrGesperrt(
									buchungDto.getIGeschaeftsjahr())) {
						// keine Geschaeftsjahrsperre
						if (!buchungDto.isAutomatischeBuchung()
								&& !DelegateFactory.getInstance()
										.getBuchenDelegate()
										.isUvaVerprobt(buchungDto.getIId())) {
							// Buchung ist nicht automatisch und nicht
							// Uva-verprobt
							if (buchungDto.getBuchungsartCNr().equals(
									FinanzFac.BUCHUNGSART_UMBUCHUNG)
									|| (buchungDto.getBuchungsartCNr().equals(
											FinanzFac.BUCHUNGSART_BANKBUCHUNG) && buchungDto
											.getBelegartCNr() == null)
									|| buchungDto.getBuchungsartCNr().equals(
											FinanzFac.BUCHUNGSART_EROEFFNUNG)
									|| (buchungDto.getBuchungsartCNr().equals(
											FinanzFac.BUCHUNGSART_BUCHUNG) && buchungDto
											.getBelegartCNr() == null)) {
								// Buchung ist Umbuchung oder Eroeffnung oder
								// Buchung die nicht einem Beleg zugeordnet ist
								// editieren erlauben
								enable = true;
							}
						}
					}
					itemNeueSplittbuchung.getButton().setEnabled(true);
				}
				item.getButton().setEnabled(enable);
			}
		}
	}

	private void berechneSaldofuerAusziffern() throws ExceptionLP, Throwable {
		if (buchungdetailDto != null) {
			if (buchungdetailDto.getIAusziffern() == null) {
				wnfSaldoAusziffern.setText("");
			} else {
				BigDecimal saldo = DelegateFactory
						.getInstance()
						.getBuchenDelegate()
						.getSaldoVonKontoByAusziffern(
								buchungdetailDto.getKontoIId(),
								buchungdetailDto.getIAusziffern(),
								buchungDto.getIGeschaeftsjahr());
				wnfSaldoAusziffern.setBigDecimal(saldo);
			}
		} else {
			wnfSaldoAusziffern.setText("");
		}
	}

	private void berechneSaldofuerAuszug() throws ExceptionLP, Throwable {
		if (buchungdetailDto != null) {
			if (buchungdetailDto.getIAuszug() == null) {
				wnfSaldoAuszug.setText("");
			} else {
				BigDecimal saldo = DelegateFactory
						.getInstance()
						.getBuchenDelegate()
						.getSaldoVonKontoByAuszug(
								buchungdetailDto.getKontoIId(),
								buchungdetailDto.getIAuszug(),
								buchungDto.getIGeschaeftsjahr());
				wnfSaldoAuszug.setBigDecimal(saldo);
			}
		} else {
			wnfSaldoAuszug.setText("");
		}
	}

	/**
	 * dto2Components.
	 * 
	 * @throws Throwable
	 */
	private void dto2Components() throws Throwable {
		if (this.getKeyWhenDetailPanel() != null) {
			buchungdetailDto = DelegateFactory
					.getInstance()
					.getBuchenDelegate()
					.buchungdetailFindByPrimaryKey(
							(Integer) this.getKeyWhenDetailPanel());
			buchungDto = DelegateFactory.getInstance().getBuchenDelegate()
					.buchungFindByPrimaryKey(buchungdetailDto.getBuchungIId());
			if (buchungdetailDto.getKontoIIdGegenkonto() != null) {
				KontoDtoSmall kontoDtoGegenkonto = DelegateFactory
						.getInstance()
						.getFinanzDelegate()
						.kontoFindByPrimaryKeySmall(
								buchungdetailDto.getKontoIIdGegenkonto());
				this.wtfGegenkontoNummer.setText(kontoDtoGegenkonto.getCNr());
				this.wtfGegenkontoName.setText(kontoDtoGegenkonto.getCBez());
			} else {
				this.wtfGegenkontoNummer.setText("");
				this.wtfGegenkontoName.setText("");
			}
			this.wtfText.setText(buchungDto.getCText());
			if (buchungdetailDto.getBuchungdetailartCNr().equals(
					BuchenFac.HabenBuchung)) {
				this.wnfHaben.setBigDecimal(buchungdetailDto.getNBetrag());
				this.wnfSoll.setText("");
				this.wnfHaben.setEditable(true);
				this.wnfSoll.setEditable(false);
			}
			if (buchungdetailDto.getBuchungdetailartCNr().equals(
					BuchenFac.SollBuchung)) {
				this.wnfSoll.setBigDecimal(buchungdetailDto.getNBetrag());
				this.wnfHaben.setText("");
				this.wnfHaben.setEditable(false);
				this.wnfSoll.setEditable(true);
			}

			mGegenkonten = DelegateFactory.getInstance().getBuchenDelegate()
					.getListeDerGegenkonten(buchungdetailDto.getIId());

			list.removeAll();
			wbuGegenkonto.setOKey(null);
			// Iterator it = mGegenkonten.keySet().iterator();
			// String[] zeilen = new String[mGegenkonten.size()];
			// int i = 0;
			// while (it.hasNext()) {
			// zeilen[i] = (String) mGegenkonten.get(it.next());
			// i++;
			// }
			// list.setListData(zeilen);

			Object[] tempZeilen = mGegenkonten.values().toArray();
			list.setListData(tempZeilen);

			this.wnfUst.setBigDecimal(buchungdetailDto.getNUst());
			this.wtfBeleg.setText(buchungDto.getCBelegnummer());
			this.wtfAuszug.setInteger(buchungdetailDto.getIAuszug());
			String sBuchungsartUebersetzt = DelegateFactory
					.getInstance()
					.getFinanzServiceDelegate()
					.uebersetzeBuchungsartOptimal(
							buchungDto.getBuchungsartCNr());
			this.wtfBuchungsart.setText(sBuchungsartUebersetzt);
			this.wtfBelegart.setText(buchungDto.getBelegartCNr());
			if (buchungDto.getKostenstelleIId() != null) {
				KostenstelleDto kostenstelleDto = DelegateFactory
						.getInstance()
						.getSystemDelegate()
						.kostenstelleFindByPrimaryKey(
								buchungDto.getKostenstelleIId());
				wtfKostenstelleBezeichnung.setText(kostenstelleDto.getCBez());
				wtfKostenstelleNummer.setText(kostenstelleDto.getCNr());
			} else {
				wtfKostenstelleBezeichnung.setText("");
				wtfKostenstelleNummer.setText("");
			}
			BigDecimal betrag = buchungdetailDto.getNBetrag();
			KontoDto kontoDto = DelegateFactory.getInstance()
					.getFinanzDelegate()
					.kontoFindByPrimaryKey(buchungdetailDto.getKontoIId());
			BigDecimal prozent = null;
			// Konten mit Saldo sind Bank und Kassenkonten, diese buchen Brutto
			boolean bIstBankoderKassenkonto = DelegateFactory.getInstance()
					.getFinanzDelegate()
					.isKontoMitSaldo(buchungdetailDto.getKontoIId());

			if (kontoDto.getKontotypCNr().equals(
					FinanzServiceFac.KONTOTYP_SACHKONTO)
					&& !bIstBankoderKassenkonto) {
				prozent = Helper.getProzentsatzBD(betrag,
						buchungdetailDto.getNUst(), FinanzFac.NACHKOMMASTELLEN);
			} else {
				prozent = Helper.getProzentsatzBD(
						betrag.subtract(buchungdetailDto.getNUst()),
						buchungdetailDto.getNUst(), FinanzFac.NACHKOMMASTELLEN);
			}

			wnfUstProzent.setBigDecimal(prozent);
			if (buchungdetailDto.getIAusziffern() == null)
				wtfAusziffern.setText(null);
			else
				wtfAusziffern.setText(buchungdetailDto.getIAusziffern()
						.toString());

			setStatusbarModification(buchungdetailDto);
		} else {
			this.leereAlleFelder(this);
			list.removeAll();
			list.setListData(new String[0]);
		}
	}

	public String getLockMeWer() {
		return HelperClient.LOCKME_FINANZ_BUCHUNGDETAIL;
	}

	protected javax.swing.JComponent getFirstFocusableComponent()
			throws Exception {
		// das panel ist nur lesend
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}
}
