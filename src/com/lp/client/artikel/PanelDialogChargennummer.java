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

import java.math.BigDecimal;
import java.text.MessageFormat;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperTable;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.SeriennrChargennrMitMengeDto;
import com.lp.util.Helper;

/**
 * <p>
 * <I>Dialog zur Auswahl von Chargen.</I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>16. 03. 2005</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version $Revision: 1.6 $
 */
public class PanelDialogChargennummer extends PanelDialogSeriennummer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PanelDialogChargennummer(InternalFrame oInternalFrameI,
			String add2Title, WrapperTable jtTabelleI,
			String[] aSeriennummernI, Integer iIdArtikelI, Integer iIdLagerI,
			Double ddGewuenschteMengeI, Double ddBisherigeMengeI,
			boolean bPruefeObMengeAufLagerI) throws Throwable {
		super(oInternalFrameI, add2Title, jtTabelleI, aSeriennummernI,
				iIdArtikelI, iIdLagerI, ddGewuenschteMengeI,
				bPruefeObMengeAufLagerI);
		jbInitPanel();
		initComponents();
	}

	private void jbInitPanel() throws Throwable {
		wlaEingabe.setText(LPMain.getTextRespectUISPr("artikel.chargennummer"));
	}

	protected boolean pruefeGewaehlteMenge() throws Throwable {
		return true;
	}

	/**
	 * Der Benutzer kann frei eingeben, hier wird seine Eingabe auf ihre
	 * Gueltigkeit ueberprueft.
	 * 
	 * @return boolean true, wenn die Eingabe gueltig ist.
	 * @throws Throwable
	 *             Ausnahme
	 * @param aSeriennummernBisherI
	 *            String[]
	 */
	protected boolean pruefeObAlleAufLager(String[] aSeriennummernBisherI)
			throws Throwable {
		boolean bAlleAufLager = false;
		int iMengeUINachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenMenge();
		// geprueft wird, ob die zusaetzliche Menge auf Lager liegt, es ist
		// moeglich, dass eine Buchung nach oben korrigiert werden soll
		BigDecimal ddZuPruefendeMenge = new BigDecimal(ddGewuenschteMenge);
		
		// setScale liefert einen neuen Bigdecimal zurueck der zu verwenden ist!
		ddZuPruefendeMenge = ddZuPruefendeMenge.setScale(iMengeUINachkommastellen,
				BigDecimal.ROUND_HALF_EVEN);

		// die letzte Charge im Array ist ev. nicht vollstaendig verbraucht
		if (aSeriennummernBisherI != null && aSeriennummernBisherI.length > 0) {
			String[] aTemp = new String[1];
			aTemp[0] = aSeriennummernBisherI[aSeriennummernBisherI.length - 1];

			BigDecimal ddMengeLetzteChargeAufLager = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getMengeMehrererSeriennummernChargennummernAufLager(
							iIdArtikel,
							iIdLager,
							SeriennrChargennrMitMengeDto
									.erstelleSrnDtoArrayAusStringArray(aTemp));

			if (ddZuPruefendeMenge.doubleValue() <= ddMengeLetzteChargeAufLager
					.doubleValue()) {
				bAlleAufLager = true;
			} else {
				ddZuPruefendeMenge = ddZuPruefendeMenge
						.subtract(ddMengeLetzteChargeAufLager);
			}
		}

		// man braucht neue Chargen, um die Menge vollzumachen
		if (!bAlleAufLager) {

			for (int i = 0; i < aSeriennummern.length; i++) {

				// die Menge wird nur fuer neue Chargennummern geprueft
				if (!Helper.enthaeltStringArrayString(aSeriennummernBisherI,
						aSeriennummern[i])) {
					String[] aTemp = new String[1];
					aTemp[0] = aSeriennummern[i];

					BigDecimal ddGewaehlteMenge = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getMengeMehrererSeriennummernChargennummernAufLager(
									iIdArtikel,
									iIdLager,
									SeriennrChargennrMitMengeDto
											.erstelleSrnDtoArrayAusStringArray(aTemp));

					ddZuPruefendeMenge = ddZuPruefendeMenge
							.subtract(ddGewaehlteMenge);

				}
			}
			if (ddZuPruefendeMenge.doubleValue() > 0) {
				MessageFormat mf = new MessageFormat(
						LPMain.getTextRespectUISPr("lp.hint.chargennummer"));

				mf.setLocale(LPMain.getTheClient().getLocUi());

				Object pattern[] = { ddGewuenschteMenge };

				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.warning"),
						mf.format(pattern));
			} else {
				bAlleAufLager = true;
			}
		}

		return bAlleAufLager;
	}

	/**
	 * Aus der Benutzereingabe ein Chargennummern Array erzeugen.
	 * 
	 * @return String[] das Array
	 * @throws Throwable
	 *             Ausnahme
	 */
	protected String[] erzeugeSeriennummernChargenArray() throws Throwable {
		return Helper.erzeugeSeriennummernArray(wsfEingabe.getText(),
				new BigDecimal(ddGewuenschteMenge.doubleValue()), false); // fuer
																			// Charge
																			// die
																			// Menge
																			// nicht
																			// pruefen
	}
}
