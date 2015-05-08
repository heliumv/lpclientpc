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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDtoSmall;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.FilterKriteriumDirekt;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelSteuerkategorie extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private Border border = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private SteuerkategorieDto steuerkategorieDto = null;
	private WrapperLabel wlaBezeichnung = new WrapperLabel();
	private WrapperTextField wtfBezeichnung = new WrapperTextField();

	private final TabbedPaneFinanzamt tabbedPaneFinanzamt;
	
	private WrapperLabel wlaKennung = new WrapperLabel();
	private WrapperTextField wtfKennung = new WrapperTextField();

	private WrapperCheckBox wcoReversecharge = new WrapperCheckBox();

	private WrapperButton wbuKontoForderungen = new WrapperButton();
	private WrapperTextField wtfKontoForderungen = new WrapperTextField(80);
	private WrapperButton wbuKontoVerbindlichkeiten = new WrapperButton();
	private WrapperTextField wtfKontoVerbindlichkeiten = new WrapperTextField(80);
	private WrapperButton wbuKontoKursverlust = new WrapperButton();
	private WrapperTextField wtfKontoKursverlust = new WrapperTextField(80);
	private WrapperButton wbuKontoKursgewinn = new WrapperButton();
	private WrapperTextField wtfKontoKursgewinn = new WrapperTextField(80);
	
	KontoDtoSmall kontoDtoForderungen = null;
	KontoDtoSmall kontoDtoVerbindlichkeiten = null;
	KontoDtoSmall kontoDtoKursverlust = null;
	KontoDtoSmall kontoDtoKursgewinn = null;
	
	static final public String ACTION_SPECIAL_KONTO_FORDERUNG = "action_special_konto_forderung";
	static final public String ACTION_SPECIAL_KONTO_VERBINDLICH = "action_special_konto_verbindlich";
	static final public String ACTION_SPECIAL_KONTO_KURSVERLUST = "action_special_konto_kursverlust";
	static final public String ACTION_SPECIAL_KONTO_KURSGEWINN = "action_special_konto_kursgewinn";
	
	private PanelQueryFLR panelQueryKontoF = null;
	private PanelQueryFLR panelQueryKontoV = null;
	private PanelQueryFLR panelQueryKontoKv = null;
	private PanelQueryFLR panelQueryKontoKg = null;
	
	public PanelSteuerkategorie(InternalFrame internalFrame, String add2TitleI,
			Object pk, TabbedPaneFinanzamt tabbedPaneFinanzamt) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		
		if(null == tabbedPaneFinanzamt) throw new IllegalArgumentException("tabbedPaneFinanzamt") ;
		
		this.tabbedPaneFinanzamt = tabbedPaneFinanzamt;
		
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wtfBezeichnung;
	}

	
	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);

		steuerkategorieDto = new SteuerkategorieDto();

		leereAlleFelder(this);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_FORDERUNG)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
			QueryType[] qt = null;
			FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkontenInklMitlaufende();
			panelQueryKontoF = new PanelQueryFLR(qt, filters,
					QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
							"finanz.liste.sachkonten"));
			FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
			FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
			FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
			panelQueryKontoF.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2, fkVersteckt);
			panelQueryKontoF.setSelectedId(steuerkategorieDto.getKontoIIdForderungen());
			new DialogQuery(panelQueryKontoF);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_VERBINDLICH)) {
				String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
				QueryType[] qt = null;
				FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkontenInklMitlaufende();
				panelQueryKontoV = new PanelQueryFLR(qt, filters,
						QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
						getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
								"finanz.liste.sachkonten"));
				FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
				FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
				FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
				panelQueryKontoV.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2, fkVersteckt);
				panelQueryKontoV.setSelectedId(steuerkategorieDto.getKontoIIdVerbindlichkeiten());
				new DialogQuery(panelQueryKontoV);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_KURSGEWINN)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
			QueryType[] qt = null;
			FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkonten();
			panelQueryKontoKg = new PanelQueryFLR(qt, filters,
					QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
							"finanz.liste.sachkonten"));
			FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
			FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
			FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
			panelQueryKontoKg.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2, fkVersteckt);
			panelQueryKontoKg.setSelectedId(steuerkategorieDto.getKontoIIdVerbindlichkeiten());
			new DialogQuery(panelQueryKontoKg);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_KONTO_KURSVERLUST)) {
			String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH, PanelBasis.ACTION_LEEREN };
			QueryType[] qt = null;
			FilterKriterium[] filters = FinanzFilterFactory.getInstance().createFKSachkonten();
			panelQueryKontoKv = new PanelQueryFLR(qt, filters,
					QueryParameters.UC_ID_FINANZKONTEN, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance().getTextRespectUISPr(
							"finanz.liste.sachkonten"));
			FilterKriteriumDirekt fkDirekt1 = FinanzFilterFactory.getInstance().createFKDKontonummer();
			FilterKriteriumDirekt fkDirekt2 = FinanzFilterFactory.getInstance().createFKDKontobezeichnung();
			FilterKriterium fkVersteckt = FinanzFilterFactory.getInstance().createFKVKonto();
			panelQueryKontoKv.befuellePanelFilterkriterienDirektUndVersteckte(fkDirekt1, fkDirekt2, fkVersteckt);
			panelQueryKontoKv.setSelectedId(steuerkategorieDto.getKontoIIdVerbindlichkeiten());
			new DialogQuery(panelQueryKontoKv);
		}
	}
	
	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
	throws Throwable {
		if (bNeedNoUpdateI) {
			return;
		}
		int iLockstate = getLockedstateDetailMainKey().getIState();
		if (iLockstate == LOCK_IS_NOT_LOCKED
				||
				// in diesen Faellen ein echtes update zulassen
				iLockstate == LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY
				|| iLockstate == LOCK_ENABLE_REFRESHANDUPDATE_ONLY) {
			// MB: nocheinmal ein refresh. (der datensatz muss bereits gelockt
			// sein)
			// damit werden die aktuellen Daten angezeigt. Solange der Lock
			// besteht,
			// kann kein anderer User die Daten veraendern.
			eventYouAreSelected(false);
			/**
			 * @todo MB->MB eigentlich sollte erst nach dem Locken refresht
			 *       werden aber, dann funktionieren die PanelSplit-FLR's nicht
			 *       mehr richtig ... keine ahnung wieso
			 */

			eventActionLock(null);
			// Lockstate setzen und Buttons schalten.
			LockStateValue lockstateValue = getLockedstateDetailMainKey();
			lockstateValue.setIState(LOCK_IS_LOCKED_BY_ME);
			updateButtons(lockstateValue);
			setFirstFocusableComponent();
		} else {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("lp.system.locked.text"));

			LockStateValue lockstateValue = getLockedstateDetailMainKey();
			lockstateValue
			.setIState((iLockstate == LOCK_IS_LOCKED_BY_ME) ? LOCK_IS_LOCKED_BY_OTHER_USER
					: iLockstate);
			updateButtons(lockstateValue);
		}
		
		wtfKennung.setEditable(false);

		// btnupd: andere informieren.
		//getInternalFrame().fireItemChanged(this, ItemChangedEvent.ACTION_UPDATE);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryKontoF) {
				Integer iId = (Integer) ( (ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					steuerkategorieDto.setKontoIIdForderungen(iId);
					kontoDtoForderungen = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(iId);
					wtfKontoForderungen.setText(kontoDtoForderungen.formatKtoNrBezForUI());
				} else {
					wtfKontoForderungen.setText(null);
				}
			} else if (e.getSource() == panelQueryKontoV) {
				Integer iId = (Integer) ( (ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					steuerkategorieDto.setKontoIIdVerbindlichkeiten(iId);
					kontoDtoVerbindlichkeiten = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(iId);
					wtfKontoVerbindlichkeiten.setText(kontoDtoVerbindlichkeiten.formatKtoNrBezForUI());
				} else {
					wtfKontoVerbindlichkeiten.setText(null);
				}
			} else if (e.getSource() == panelQueryKontoKg) {
				Integer iId = (Integer) ( (ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					steuerkategorieDto.setKontoIIdKursgewinn(iId);
					kontoDtoKursgewinn = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(iId);
					wtfKontoKursgewinn.setText(kontoDtoKursgewinn.formatKtoNrBezForUI());
				} else {
					wtfKontoKursgewinn.setText(null);
				}
			} else if (e.getSource() == panelQueryKontoKv) {
				Integer iId = (Integer) ( (ISourceEvent) e.getSource()).getIdSelected();
				if (iId != null) {
					steuerkategorieDto.setKontoIIdKursverlust(iId);
					kontoDtoKursverlust = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(iId);
					wtfKontoKursverlust.setText(kontoDtoKursverlust.formatKtoNrBezForUI());
				} else {
					wtfKontoKursverlust.setText(null);
				}
		     }
		}
		else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryKontoF) {
				steuerkategorieDto.setKontoIIdForderungen(null);
				wtfKontoForderungen.setText(null);
			} else if (e.getSource() == panelQueryKontoV) {
				steuerkategorieDto.setKontoIIdVerbindlichkeiten(null);
				wtfKontoVerbindlichkeiten.setText(null);
			} else if (e.getSource() == panelQueryKontoKg) {
				steuerkategorieDto.setKontoIIdKursgewinn(null);
				wtfKontoKursgewinn.setText(null);
			} else if (e.getSource() == panelQueryKontoKv) {
				steuerkategorieDto.setKontoIIdKursverlust(null);
				wtfKontoKursverlust.setText(null);
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

		wcoReversecharge.setText(LPMain.getInstance().getTextRespectUISPr("lp.reversecharge"));
		wcoReversecharge.setToken("reversecharge");
		wtfBezeichnung.setMandatoryField(true);
		wtfBezeichnung.setColumnsMax(FinanzServiceFac.MAX_STEUERKATEGORIE_BEZEICHNUNG);
		wtfBezeichnung.setToken("bezeichnung");
		wlaBezeichnung.setText(LPMain.getInstance().getTextRespectUISPr("lp.bezeichnung"));

		wlaKennung.setText(LPMain.getInstance().getTextRespectUISPr("label.kennung"));
		wtfKennung.setColumnsMax(15);
		wtfKennung.setToken("kennung");
		wtfKennung.setEditable(false);

		wbuKontoForderungen.setText(LPMain.getInstance().getTextRespectUISPr("fb.kontoforderung"));
		wbuKontoForderungen.setActionCommand(ACTION_SPECIAL_KONTO_FORDERUNG);
		wbuKontoForderungen.addActionListener(this);
		wtfKontoForderungen.setToken("kontoforderung");

		wbuKontoVerbindlichkeiten.setText(LPMain.getInstance().getTextRespectUISPr("fb.kontoverbindlich"));
		wbuKontoVerbindlichkeiten.setActionCommand(ACTION_SPECIAL_KONTO_VERBINDLICH);
		wbuKontoVerbindlichkeiten.addActionListener(this);
		wtfKontoVerbindlichkeiten.setToken("kontoverbindlich");

		wbuKontoKursverlust.setText(LPMain.getInstance().getTextRespectUISPr("fb.kontokursverlust"));
		wbuKontoKursverlust.setActionCommand(ACTION_SPECIAL_KONTO_KURSVERLUST);
		wbuKontoKursverlust.addActionListener(this);
		wtfKontoKursverlust.setToken("kontokursverlust");

		wbuKontoKursgewinn.setText(LPMain.getInstance().getTextRespectUISPr("fb.kontokursgewinn"));
		wbuKontoKursgewinn.setActionCommand(ACTION_SPECIAL_KONTO_KURSGEWINN);
		wbuKontoKursgewinn.addActionListener(this);
		wtfKontoKursgewinn.setToken("kontokursgewinn");

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 0, 0));

		// jetzt meine felder
		jpaWorkingOn = new JPanel();
		gridBagLayoutWorkingPanel = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingPanel);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.SOUTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

// Es gibt keine Modifikationsdaten im Datensatz
//		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
//				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
//				new Insets(0, 0, 0, 0), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaKennung, new GridBagConstraints(0, iZeile, 1,
				1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKennung, new GridBagConstraints(1, iZeile, 1,
				1, 2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBezeichnung, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wcoReversecharge, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKontoForderungen, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoForderungen, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKontoVerbindlichkeiten, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoVerbindlichkeiten, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKontoKursgewinn, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoKursgewinn, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wbuKontoKursverlust, new GridBagConstraints(0, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKontoKursverlust, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				 ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_STEUERKATEGORIE;
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		DelegateFactory.getInstance().getFinanzServiceDelegate()
				.removeSteuerkategorie(steuerkategorieDto.getIId());
		setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable{
		steuerkategorieDto.setCBez(wtfBezeichnung.getText());
		steuerkategorieDto.setBReversecharge(wcoReversecharge.getShort());
		steuerkategorieDto.setCNr(wtfKennung.getText());
		steuerkategorieDto.setMandantCNr(LPMain.getTheClient().getMandant());
		setKeyWhenDetailPanel(steuerkategorieDto.getIId());
	}

	protected void dto2Components() throws Throwable {
		KontoDtoSmall kontoDto = null;
		wtfBezeichnung.setText(steuerkategorieDto.getCBez());
		wcoReversecharge.setShort(steuerkategorieDto.getBReversecharge());
		wtfKennung.setText(steuerkategorieDto.getCNr());
		if (steuerkategorieDto.getKontoIIdForderungen() == null)
			wtfKontoForderungen.setText(null);
		else {
			kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(steuerkategorieDto.getKontoIIdForderungen());
			wtfKontoForderungen.setText(kontoDto.formatKtoNrBezForUI());
		}
		if (steuerkategorieDto.getKontoIIdVerbindlichkeiten() == null)
			wtfKontoVerbindlichkeiten.setText(null);
		else {
			kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(steuerkategorieDto.getKontoIIdVerbindlichkeiten());
			wtfKontoVerbindlichkeiten.setText(kontoDto.formatKtoNrBezForUI());
		}
		if (steuerkategorieDto.getKontoIIdKursgewinn() == null)
			wtfKontoKursgewinn.setText(null);
		else {
			kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(steuerkategorieDto.getKontoIIdKursgewinn());
			wtfKontoKursgewinn.setText(kontoDto.formatKtoNrBezForUI());
		}
		if (steuerkategorieDto.getKontoIIdKursverlust() == null)
			wtfKontoKursverlust.setText(null);
		else {
			kontoDto = DelegateFactory.getInstance().getFinanzDelegate().kontoFindByPrimaryKeySmall(steuerkategorieDto.getKontoIIdKursverlust());
			wtfKontoKursverlust.setText(kontoDto.formatKtoNrBezForUI());
		}
		setKeyWhenDetailPanel(steuerkategorieDto.getIId());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (steuerkategorieDto.getIId() == null) {
				steuerkategorieDto.setIId(DelegateFactory.getInstance()
						.getFinanzServiceDelegate().createSteuerkategorie(steuerkategorieDto));
				setKeyWhenDetailPanel(steuerkategorieDto.getIId());
			} else {
				DelegateFactory.getInstance().getFinanzServiceDelegate()
						.updateSteuerkategorie(steuerkategorieDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(steuerkategorieDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}

	}

	private TabbedPaneFinanzamt getTabbedPaneFinanzamt() {
		return tabbedPaneFinanzamt;
	}
	
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();
		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			steuerkategorieDto = DelegateFactory.getInstance().getFinanzServiceDelegate()
					.steuerkategorieFindByPrimaryKey((Integer) key);
			dto2Components();
			setKeyWhenDetailPanel(steuerkategorieDto.getIId());
		}
		getInternalFrame().setLpTitle(
				InternalFrame.TITLE_IDX_AS_I_LIKE,
				getTabbedPaneFinanzamt().getFinanzamtDto().getPartnerDto()
						.getCName1nachnamefirmazeile1());	
	}
}
