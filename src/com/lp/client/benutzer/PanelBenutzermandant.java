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
package com.lp.client.benutzer;

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
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.personal.PersonalFilterFactory;
import com.lp.server.benutzer.service.BenutzerDto;
import com.lp.server.benutzer.service.BenutzerFac;
import com.lp.server.benutzer.service.BenutzermandantsystemrolleDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.benutzer.service.SystemrolleDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

@SuppressWarnings("static-access")
public class PanelBenutzermandant extends PanelBasis {

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
	private InternalFrameBenutzer internalFramePersonal = null;
	private WrapperButton wbuBenutzer = new WrapperButton();
	private WrapperTextField wtfBenutzer = new WrapperTextField();
	private BenutzermandantsystemrolleDto benutzermandantsystemrolleDto = null;
	private WrapperButton wbuMandant = new WrapperButton();
	private WrapperButton wbuSystemrolle = new WrapperButton();
	private WrapperTextField wtfMandant = new WrapperTextField();
	private WrapperTextField wtfSystemrolle = new WrapperTextField();

	private WrapperButton wbuPersonal = new WrapperButton();
	private WrapperTextField wtfPersonal = new WrapperTextField();

	private PanelQueryFLR panelQueryFLRBenutzer = null;
	private PanelQueryFLR panelQueryFLRMandant = null;
	private PanelQueryFLR panelQueryFLRSystemrolle = null;
	private PanelQueryFLR panelQueryFLRPersonal = null;

	static final public String ACTION_SPECIAL_BENUTZER_FROM_LISTE = "action_benutzer_from_liste";
	static final public String ACTION_SPECIAL_MANDANT_ABTEILUNG_FROM_LISTE = "action_mandant_from_liste";
	static final public String ACTION_SPECIAL_SYSTEMROLLE_FROM_LISTE = "action_systemrolle_from_liste";
	static final public String ACTION_SPECIAL_PERSONAL_FROM_LISTE = "action_personal_from_liste";

	public PanelBenutzermandant(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFramePersonal = (InternalFrameBenutzer) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuBenutzer;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		benutzermandantsystemrolleDto = new BenutzermandantsystemrolleDto();
		leereAlleFelder(this);
	}

	void dialogQueryMandantFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRMandant = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_MANDANT, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr("title.mandantauswahlliste"));

		panelQueryFLRMandant.setSelectedId(benutzermandantsystemrolleDto
				.getMandantCNr());

		new DialogQuery(panelQueryFLRMandant);
	}

	void dialogQueryBenutzerFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRBenutzer = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_BENUZTER, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr("title.benutzerauswahlliste"));

		panelQueryFLRBenutzer.befuellePanelFilterkriterienDirekt(
				BenutzerFilterFactory.getInstance().createFKDBenutzerkennung(),
				BenutzerFilterFactory.getInstance().createFKDNachname());

		panelQueryFLRBenutzer.setSelectedId(benutzermandantsystemrolleDto
				.getBenutzerIId());

		new DialogQuery(panelQueryFLRBenutzer);
	}

	void dialogQueryPersonalFromListe(ActionEvent e) throws Throwable {

		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		FilterKriterium[] filtersPersonal = new FilterKriterium[1];
		FilterKriterium krit1 = new FilterKriterium("mandant_c_nr", true, "'"
				+ benutzermandantsystemrolleDto.getMandantCNr() + "'",
				FilterKriterium.OPERATOR_EQUAL, false);
		filtersPersonal[0] = krit1;

		panelQueryFLRPersonal = new PanelQueryFLR(null, filtersPersonal,
				QueryParameters.UC_ID_PERSONAL, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr("title.personalauswahlliste"));
		FilterKriterium fkVersteckte = null;

		if (DelegateFactory.getInstance().getTheJudgeDelegate().hatRecht(
				RechteFac.RECHT_LP_DARF_VERSTECKTE_SEHEN)) {
			fkVersteckte = PersonalFilterFactory.getInstance()
					.createFKVPersonal();
		}

		panelQueryFLRPersonal.befuellePanelFilterkriterienDirektUndVersteckte(
				PersonalFilterFactory.getInstance().createFKDPersonalname(),
				PersonalFilterFactory.getInstance().createFKDPersonalnummer(),
				fkVersteckte);

		panelQueryFLRPersonal.setSelectedId(benutzermandantsystemrolleDto
				.getPersonalIIdZugeordnet());

		new DialogQuery(panelQueryFLRPersonal);

	}

	void dialogQuerySystemrolleFromListe(ActionEvent e) throws Throwable {
		String[] aWhichButtonIUse = { PanelBasis.ACTION_REFRESH };

		panelQueryFLRSystemrolle = new PanelQueryFLR(null, null,
				QueryParameters.UC_ID_SYSTEMROLLE, aWhichButtonIUse,
				internalFramePersonal, LPMain.getInstance()
						.getTextRespectUISPr("title.systemrolleauswahlliste"));

		panelQueryFLRSystemrolle.setSelectedId(benutzermandantsystemrolleDto
				.getSystemrolleIId());

		new DialogQuery(panelQueryFLRSystemrolle);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_BENUTZER_FROM_LISTE)) {
			dialogQueryBenutzerFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_MANDANT_ABTEILUNG_FROM_LISTE)) {
			dialogQueryMandantFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_SYSTEMROLLE_FROM_LISTE)) {
			dialogQuerySystemrolleFromListe(e);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_PERSONAL_FROM_LISTE)) {
			if (wtfMandant.getText() != null) {

				dialogQueryPersonalFromListe(e);
			} else {
				DialogFactory.showModalDialog("Achtung!",
						"Bitte zuerst Mandant ausw\u00E4hlen.");
			}

		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		
		if(benutzermandantsystemrolleDto.getMandantCNr().equals(DelegateFactory.getInstance().getSystemDelegate().getHauptmandant())){
			if(benutzermandantsystemrolleDto.getBenutzerDto().getCBenutzerkennung().equals("lpwebappzemecs")){
				DialogFactory.showModalDialog(LPMain.getInstance()
						.getTextRespectUISPr("lp.error"),
						LPMain.getInstance()
						.getTextRespectUISPr("lp.error.lpwebappzemecsbeihauptmandantnichtloeschbar"));
				return;
			}
		}
		
		
		
		
		DelegateFactory.getInstance().getBenutzerDelegate()
				.removeBenutzermandantsystemrolle(
						benutzermandantsystemrolleDto.getIId());
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() {
	}

	protected void dto2Components() throws ExceptionLP, Throwable {

		if (benutzermandantsystemrolleDto.getBenutzerDto() != null) {
			wtfBenutzer.setText(benutzermandantsystemrolleDto.getBenutzerDto()
					.getCBenutzerkennung());
		} else {
			wtfBenutzer.setText("");
		}
		wtfMandant.setText(benutzermandantsystemrolleDto.getMandantCNr()
				+ " - "
				+ DelegateFactory.getInstance().getMandantDelegate()
						.mandantFindByPrimaryKey(
								benutzermandantsystemrolleDto.getMandantCNr())
						.getPartnerDto().formatAnrede());

		if (benutzermandantsystemrolleDto.getSystemrolleDto() != null) {
			wtfSystemrolle.setText(benutzermandantsystemrolleDto
					.getSystemrolleDto().getCBez());
		} else {
			wtfSystemrolle.setText("");
		}
		if (benutzermandantsystemrolleDto.getPersonalIIdZugeordnet() != null) {
			wtfPersonal
					.setText(DelegateFactory.getInstance()
							.getPersonalDelegate().personalFindByPrimaryKey(
									benutzermandantsystemrolleDto
											.getPersonalIIdZugeordnet())
							.formatAnrede());
		} else {
			wtfPersonal.setText("");
		}

		this.setStatusbarPersonalIIdAendern(benutzermandantsystemrolleDto
				.getPersonalIIdAendern());
		this.setStatusbarPersonalIIdAnlegen(benutzermandantsystemrolleDto
				.getPersonalIIdAnlegen());
		this.setStatusbarTAnlegen(benutzermandantsystemrolleDto.getTAnlegen());
		this.setStatusbarTAendern(benutzermandantsystemrolleDto.getTAendern());

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (benutzermandantsystemrolleDto.getIId() == null) {
				benutzermandantsystemrolleDto.setIId(DelegateFactory
						.getInstance().getBenutzerDelegate()
						.createBenutzermandantsystemrolle(
								benutzermandantsystemrolleDto));
				setKeyWhenDetailPanel(benutzermandantsystemrolleDto.getIId());
			} else {
				DelegateFactory.getInstance().getBenutzerDelegate()
						.updateBenutzermandantsystemrolle(
								benutzermandantsystemrolleDto);
			}
			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						benutzermandantsystemrolleDto.getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRBenutzer) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				BenutzerDto benutzerTempDto = DelegateFactory.getInstance()
						.getBenutzerDelegate().benutzerFindByPrimaryKey(
								(Integer) key);
				wtfBenutzer.setText(benutzerTempDto.getCBenutzerkennung());
				benutzermandantsystemrolleDto.setBenutzerIId(benutzerTempDto
						.getIId());
			} else if (e.getSource() == panelQueryFLRMandant) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				wtfMandant.setText(key
						+ " - "
						+ DelegateFactory.getInstance().getMandantDelegate()
								.mandantFindByPrimaryKey((String) key)
								.getPartnerDto().formatAnrede());

				benutzermandantsystemrolleDto.setMandantCNr(key + "");
				wtfPersonal.setText(null);
				benutzermandantsystemrolleDto.setPersonalIIdZugeordnet(null);
			} else if (e.getSource() == panelQueryFLRPersonal) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					PersonalDto personalDto = DelegateFactory.getInstance()
							.getPersonalDelegate().personalFindByPrimaryKey(
									(Integer) key);
					wtfPersonal.setText(personalDto.formatAnrede());
					benutzermandantsystemrolleDto
							.setPersonalIIdZugeordnet((Integer) key);
				}
			} else if (e.getSource() == panelQueryFLRSystemrolle) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				SystemrolleDto systemrolleTempDto = DelegateFactory
						.getInstance().getBenutzerDelegate()
						.systemrolleFindByPrimaryKey((Integer) key);
				wtfSystemrolle.setText(systemrolleTempDto.getCBez());
				benutzermandantsystemrolleDto
						.setSystemrolleIId(systemrolleTempDto.getIId());
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

		wbuBenutzer.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.benutzer")
				+ "...");
		wbuBenutzer
				.setActionCommand(PanelBenutzermandant.ACTION_SPECIAL_BENUTZER_FROM_LISTE);
		wbuBenutzer.addActionListener(this);
		wtfBenutzer.setColumnsMax(BenutzerFac.MAX_BENUTZER_KENNUNG);
		wtfBenutzer.setText("");
		wtfBenutzer.setActivatable(false);
		wtfBenutzer.setMandatoryField(true);
		wtfMandant.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		getInternalFrame().addItemChangedListener(this);
		wbuMandant.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.mandant")
				+ "...");

		wbuMandant
				.setActionCommand(PanelBenutzermandant.ACTION_SPECIAL_MANDANT_ABTEILUNG_FROM_LISTE);
		wbuMandant.addActionListener(this);

		wbuPersonal
				.setActionCommand(PanelBenutzermandant.ACTION_SPECIAL_PERSONAL_FROM_LISTE);
		wbuPersonal.addActionListener(this);

		wbuSystemrolle.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.systemrolle")
				+ "...");
		wbuPersonal.setText(LPMain.getInstance().getTextRespectUISPr(
				"benutzer.zugeordnetespersonal")
				+ "...");

		wbuSystemrolle
				.setActionCommand(PanelBenutzermandant.ACTION_SPECIAL_SYSTEMROLLE_FROM_LISTE);
		wbuSystemrolle.addActionListener(this);
		wtfMandant.setActivatable(false);
		wtfMandant.setMandatoryField(true);
		wtfPersonal.setActivatable(false);
		wtfPersonal.setMandatoryField(true);

		wtfMandant.setText("");
		wtfSystemrolle.setActivatable(false);
		wtfSystemrolle.setMandatoryField(true);
		wtfSystemrolle.setText("");
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
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wbuBenutzer, new GridBagConstraints(0, 0, 1, 1, 0.25,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBenutzer, new GridBagConstraints(1, 0, 1, 1, 0.5,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wbuMandant, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfMandant, new GridBagConstraints(1, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuPersonal, new GridBagConstraints(0, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfPersonal, new GridBagConstraints(1, 2, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wbuSystemrolle, new GridBagConstraints(0, 3, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfSystemrolle, new GridBagConstraints(1, 3, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_BENUTZERMANDANT;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			leereAlleFelder(this);
			clearStatusbar();
		} else {
			benutzermandantsystemrolleDto = DelegateFactory.getInstance()
					.getBenutzerDelegate()
					.benutzermandantsystemrolleFindByPrimaryKey((Integer) key);
			dto2Components();
		}
	}
}
