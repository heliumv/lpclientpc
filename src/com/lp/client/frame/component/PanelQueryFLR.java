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
package com.lp.client.frame.component;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.LockStateValue;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;

/**
 * 
 * <p>
 * Diese Klasse kuemmert sich ...
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Josef Ornetsmueller; dd.mm.05
 * </p>
 * 
 * <p>
 * @author $Author: christian $
 * </p>
 * 
 * @version not attributable Date $Date: 2012/12/11 08:37:04 $
 */
public class PanelQueryFLR extends PanelQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Dialog dialog = null;

	public PanelQueryFLR(QueryType[] typesI, FilterKriterium[] filtersI,
			int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI,
				add2TitleI);
		// Eine Aenderung der Auswahl loest kein ItemChangedEvent aus
		fireItemChangedEventAfterChange = false;
	}

	public PanelQueryFLR(QueryType[] typesI, FilterKriterium[] filtersI,
			int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI,FilterKriterium kritVersteckteFelderNichtAnzeigenI) throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI,
				add2TitleI,false,kritVersteckteFelderNichtAnzeigenI);
		// Eine Aenderung der Auswahl loest kein ItemChangedEvent aus
		fireItemChangedEventAfterChange = false;
	}

	/**
	 * Konstruktor fuer eine FLR Liste, in der ein bestimmter Datensatz
	 * selektiert ist. <br>
	 * Der Key dieses Datensatzes wird als Parameter uebergeben.
	 * 
	 * @param typesI
	 *            die UI Filterkriterien fuer den Benutzer
	 * @param filtersI
	 *            die default Filterkriterien, die fuer den Benutzer nicht
	 *            sichtbar sind
	 * @param idUsecaseI
	 *            die ID des gewuenschten UseCase
	 * @param aWhichButtonIUseI
	 *            welche Buttons sind auf dem Panel sichtbar
	 * @param internalFrameI
	 *            den InternalFrame als Kontext setzen
	 * @param add2TitleI
	 *            der Titel dieses Panels
	 * @param oSelectedIdI
	 *            der Datensatz mit diesem Key soll selektiert werden
	 * @throws Throwable
	 */
	public PanelQueryFLR(QueryType[] typesI, FilterKriterium[] filtersI,
			int idUsecaseI, String[] aWhichButtonIUseI,
			InternalFrame internalFrameI, String add2TitleI, Object oSelectedIdI)
			throws Throwable {
		super(typesI, filtersI, idUsecaseI, aWhichButtonIUseI, internalFrameI,
				add2TitleI, oSelectedIdI);
		// Eine Aenderung der Auswahl loest kein ItemChangedEvent aus
		fireItemChangedEventAfterChange = false;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * cmd:
	 * 
	 * @param eventObject
	 *            ActionEvent
	 * @param bLockMeI
	 *            boolean
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {

		super.eventActionNew(eventObject, bLockMeI, bNeedNoNewI);
		getInternalFrame().removeItemChangedListener(this);
		dialog.dispose();
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	/**
	 * Hier sind immer alle buttons enabled.
	 * 
	 * @throws Exception
	 * @param iAspectI
	 *            int
	 * @param lockstateValueI
	 *            LockStateValue
	 */
	public void updateButtons(int iAspectI, LockStateValue lockstateValueI)
			throws Exception {
		// kein super!
		Collection<?> buttons = getHmOfButtons().values();

		for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
			LPButtonAction item = (LPButtonAction) iter.next();

			item.getButton().setEnabled(true);
		}
	}

	protected void eventActionLeeren(ActionEvent e) throws Throwable {
		getInternalFrame()
				.fireItemChanged(this, ItemChangedEvent.ACTION_LEEREN);
		getInternalFrame().removeItemChangedListener(this);
		dialog.dispose();
		cleanup();
	}

	protected void eventActionEscape(ActionEvent e) throws Throwable {
		dialog.dispose();
		cleanup();
	}

	/**
	 * 
	 * @param e
	 *            MouseEvent
	 * @throws ExceptionLP
	 */
	protected void eventMouseClicked(MouseEvent e) throws ExceptionLP {
		if (e.getSource().getClass() == WrapperTable.class) {
			if (e.getClickCount() == 2) {
				// Doppelklick in die Tabelle -> Dialog schliessen
				getInternalFrame().removeItemChangedListener(this);
				dialog.dispose();
				super.eventMouseClicked(e);
				cleanup();
				return;
			}
		}
		super.eventMouseClicked(e);
	}

	/**
	 * //JO 24.4.06 opt. kein evt wenn key up.
	 * 
	 * @param e
	 *            KeyEvent
	 * @throws Throwable
	 */
	protected void eventKeyPressed(KeyEvent e) throws Throwable {
		boolean bFilterChangedOri = getBFilterHasChanged();
		// Event an die Superklasse weiterleiten
		super.eventKeyPressed(e);
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			// wenn Enter und die Filter sich nicht geaendert haben, dann den
			// Dialog schliessen
			// und auch ein Datensatz gewaehlt wurde
			if (!bFilterChangedOri && this.getSelectedId() != null) {
				getInternalFrame().removeItemChangedListener(this);
				dialog.dispose();
				cleanup();
			}
		}
	}

	public Dialog getDialog() {
		return dialog;
	}
}
