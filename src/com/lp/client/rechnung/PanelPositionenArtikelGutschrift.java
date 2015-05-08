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
package com.lp.client.rechnung;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelPositionenArtikelVerkauf;
import com.lp.client.frame.component.WrapperSnrChnrField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.util.Helper;

/**
 * <p>
 * Basispanel fuer die Eingabe einer Artikelposition mit VKPF und Serien- bzw.
 * Chargennummerneingabe f&uuml;r die Gutschriften,
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum 2005-09-14
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version $Revision: 1.11 $
 */
public class PanelPositionenArtikelGutschrift extends
		PanelPositionenArtikelVerkauf {

	private static final long serialVersionUID = 1L;
	
	public WrapperSnrChnrField wtfSerienchargennummer = null;

	/**
	 * Konstruktor.
	 * 
	 * @param internalFrame
	 *            der InternalFrame auf dem das Panel sitzt
	 * @param add2TitleI
	 *            der default Titel des Panels
	 * @param key
	 *            PK des Auftrags
	 * @param sLockMeWer
	 *            String
	 * @param iSpaltenbreite1I
	 *            die Breite der ersten Spalte
	 * @throws Throwable
	 *             Ausnahme
	 */
	public PanelPositionenArtikelGutschrift(InternalFrame internalFrame,
			String add2TitleI, Object key, String sLockMeWer,
			int iSpaltenbreite1I) throws Throwable {
		super(internalFrame, add2TitleI, key, sLockMeWer, iSpaltenbreite1I); // VKPF
		jbInitPanel();
		initComponents();
	}


	private void jbInitPanel() throws Exception {

		wtfSerienchargennummer = new WrapperSnrChnrField(getInternalFrame(), true);
		wtfSerienchargennummer.setWnfBelegMenge(wnfMenge);
		wtfSerienchargennummer.setActivatable(false);
	}

	private void addWtfSerienchargennummer() {
		this.add(wtfSerienchargennummer, new GridBagConstraints(1, 8, 3, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	}

	private void addWbuSerienchargennummer() {
		add(wtfSerienchargennummer.getButtonSnrAuswahl(),
				new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		super.eventItemchanged(eI);

		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false); // buttons schalten

		// wenn der Lieferschein gerade gelockt ist, die Eingabefelder
		// freischalten
		if (getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
			zeigeSerienchargennummer(true, false);
		} else {
			zeigeSerienchargennummer(false, false);
		}
	}

	/**
	 * Die Eigenschaften des Artikels zur Anzeige bringen. <br>
	 * Serien- und Chargennummern beruecksichtigen.
	 * 
	 * @throws Throwable
	 */
	public void artikelDto2components() throws Throwable {
		super.artikelDto2components();

		zeigeSerienchargennummer(true, true);
	}

	/**
	 * Bei jeder Anzeige eines Artikels muss darueber entschieden werden, ob die
	 * Controls fuer die Serien- bzw. Chargennummereingabe angezeigt werden.
	 * 
	 * @param bEditableI
	 *            true, wenn eine Eingabe moeglich sein soll
	 * @param bSerienchargennrLeer
	 *            true, wenn die Eingabe zurueckgesetzt werden soll
	 * @throws java.lang.Throwable
	 *             Ausnahme
	 */
	public void zeigeSerienchargennummer(boolean bEditableI,
			boolean bSerienchargennrLeer) throws Throwable {

		remove(wtfSerienchargennummer.getButtonSnrAuswahl());
		remove(wtfSerienchargennummer);

		if (getArtikelDto() != null && getArtikelDto().getIId() != null) {
			if (Helper.short2boolean(getArtikelDto().getBChargennrtragend())) {
				wtfSerienchargennummer.setArtikelIdLagerId(getArtikelDto(),
						iIdLager);
				
				wtfSerienchargennummer.getButtonSnrAuswahl().setEnabled(
						bEditableI);
				// wtfSerienchargennummer.setEditable(bEditableI);
				wtfSerienchargennummer.setEditable(false);
				wtfSerienchargennummer.setMandatoryField(true);

				addWbuSerienchargennummer();
				addWtfSerienchargennummer();
			} else if (Helper.short2boolean(getArtikelDto()
					.getBSeriennrtragend())) {
				wtfSerienchargennummer.setArtikelIdLagerId(getArtikelDto(),
						iIdLager);

				if (bSerienchargennrLeer) {
					wtfSerienchargennummer.setText("");
				}

				wtfSerienchargennummer.getButtonSnrAuswahl().setEnabled(
						bEditableI);
				wtfSerienchargennummer.setEditable(false);
				wtfSerienchargennummer.setMandatoryField(true);

				addWbuSerienchargennummer();
				addWtfSerienchargennummer();
			}
		}
	}

	/**
	 * In Spalte 5 der Statusbar wird der neben dem Lager auch der Lagerstand
	 * angezeigt, wenn dafuer genug Information vorhanden ist.
	 * 
	 * @return String
	 * @throws Throwable
	 */
	protected String getLagerstandFuerStatusbarSpalte5() throws Throwable {
		String sLagerinfoO = null;
		sLagerinfoO = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(iIdLager).getCNr();

		if (getArtikelDto() != null && getArtikelDto().getIId() != null) {
			String serienchargennummer = null;

			if (Helper.short2boolean(getArtikelDto().getBSeriennrtragend())
					&& wtfSerienchargennummer.getText() != null
					&& wtfSerienchargennummer.getText().length() > 0) {
				serienchargennummer = wtfSerienchargennummer.getText();
			} else if (Helper.short2boolean(getArtikelDto()
					.getBChargennrtragend())
					&& wtfSerienchargennummer.getText() != null
					&& wtfSerienchargennummer.getText().length() > 0) {
				serienchargennummer = wtfSerienchargennummer.getText();
			}

			if ((!Helper.short2boolean(getArtikelDto().getBSeriennrtragend()) && !Helper
					.short2boolean(getArtikelDto().getBChargennrtragend()))
					|| serienchargennummer != null) {
				if (getArtikelDto() != null && getArtikelDto().getIId() != null) {
					BigDecimal ddMenge = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.getMengeAufLager(getArtikelDto().getIId(),
									iIdLager, serienchargennummer);

					sLagerinfoO += ": ";
					sLagerinfoO += ddMenge;
				}
			}
		}
		return sLagerinfoO;
	}

	protected void wnfMenge_focusLost(FocusEvent e) {
		try {
			// Wenn ich den Eintrag bearbeite
			LockStateValue lsv = this.getLockedstateDetailMainKey();
			if (lsv.getIState() == PanelBasis.LOCK_FOR_EMPTY
					|| lsv.getIState() == PanelBasis.LOCK_FOR_NEW
					|| lsv.getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME) {
				// Der SNR/Chargenbutton darf nur dann aktiv sein, wenn eine
				// Menge definiert ist.
				// (fuer Menge 0 duerfen keine SNR/Chargennummern angegeben
				// werden.)
				boolean bAktiviereSNrChargenNrButtons;
				if (wnfMenge.getBigDecimal() == null
						|| wnfMenge.getBigDecimal()
								.compareTo(new BigDecimal(0)) == 0) {
					bAktiviereSNrChargenNrButtons = false;
					wtfSerienchargennummer.setText("");
				} else {
					bAktiviereSNrChargenNrButtons = true;
				}
				wtfSerienchargennummer.getButtonSnrAuswahl().setEnabled(
						bAktiviereSNrChargenNrButtons);
				wtfSerienchargennummer
						.setMandatoryField(bAktiviereSNrChargenNrButtons);
			}
		} catch (Throwable t) {
			LPMain.getInstance().exitFrame(getInternalFrame(), t);
		}
	}

}
