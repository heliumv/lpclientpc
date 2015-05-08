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
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextArea;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.fastlanereader.generated.service.WwArtikellagerPK;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.artikel.service.ArtikellagerDto;
import com.lp.server.artikel.service.LagerDto;
import com.lp.server.artikel.service.LagerFac;
import com.lp.server.artikel.service.LagerbewegungDto;
import com.lp.server.artikel.service.SeriennrChargennrAufLagerDto;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelArtikellager extends PanelBasis {

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
	private InternalFrameArtikel internalFrameArtikel = null;
	private WrapperLabel wlaLager = new WrapperLabel();
	private WrapperTextField wtfLager = new WrapperTextField();
	private ArtikellagerDto artikellagerDto = null;
	private WrapperLabel wlaPreis = new WrapperLabel();
	private WrapperNumberField wnfPreis = new WrapperNumberField();
	private WrapperButton wbuLager = new WrapperButton();
	JTable jTableSnrChnrs = new JTable();
	JScrollPane scrollPane = new JScrollPane();
	static final public String ACTION_SPECIAL_LAGER_FROM_LISTE = "action_lager_from_liste";
	private PanelQueryFLR panelQueryFLRLager = null;
	private String[] colNames = new String[] {
			LPMain.getInstance().getTextRespectUISPr(
					"artikel.handlagerbewegung.seriennrchargennr"),
			LPMain.getInstance().getTextRespectUISPr("lp.lagerstand"),
			LPMain.getInstance().getTextRespectUISPr("artikel.lager.version") };

	public PanelArtikellager(InternalFrame internalFrame, String add2TitleI,
			Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameArtikel = (InternalFrameArtikel) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	protected void setDefaults() {

		if (!getInternalFrame().bRechtDarfPreiseSehenEinkauf) {
			wlaPreis.setVisible(false);
			wnfPreis.setVisible(false);
		}
	}

	void dialogQueryLagerFromListe(ActionEvent e) throws Throwable {

		ArtikelDto artikelDto = DelegateFactory
				.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(
						internalFrameArtikel.getArtikelDto().getIId());
		if (Helper.short2boolean(artikelDto.getBLagerbewirtschaftet())) {
			panelQueryFLRLager = ArtikelFilterFactory.getInstance()
					.createPanelFLRLager(getInternalFrame(),
							artikellagerDto.getLagerIId(), false, false);
		} else {
			panelQueryFLRLager = ArtikelFilterFactory.getInstance()
					.createPanelFLRLager(getInternalFrame(),
							artikellagerDto.getLagerIId(), false, true);

		}
		new DialogQuery(panelQueryFLRLager);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfPreis;
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		leereAlleFelder(this);
		artikellagerDto = new ArtikellagerDto();
	}

	protected void dto2Components() throws Throwable {
		LagerDto lagerDto = DelegateFactory.getInstance().getLagerDelegate()
				.lagerFindByPrimaryKey(artikellagerDto.getLagerIId());
		wtfLager.setText(lagerDto.getCNr());
		wnfPreis.setBigDecimal(artikellagerDto.getNGestehungspreis());

		if (Helper.short2boolean(internalFrameArtikel.getArtikelDto()
				.getBSeriennrtragend())
				|| Helper.short2boolean(internalFrameArtikel.getArtikelDto()
						.getBChargennrtragend())) {
			SeriennrChargennrAufLagerDto[] s = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getAllSerienChargennrAufLagerInfoDtos(
							artikellagerDto.getArtikelIId(),
							artikellagerDto.getLagerIId());

			Object[][] data = new Object[s.length][3];
			for (int i = 0; i < s.length; i++) {
				if (s[i] != null) {
					data[i][0] = s[i].getCSeriennrChargennr();
					data[i][1] = s[i].getNMenge();
					data[i][2] = s[i].getCVersion();

				}
			}
			jTableSnrChnrs = new JTable(data, colNames);
			jTableSnrChnrs.setDefaultRenderer(BigDecimal.class,
					HelperClient.getBigDecimalRenderer());

			scrollPane.getViewport().add(jTableSnrChnrs);

		} else {
			Object[][] data = new Object[0][2];
			jTableSnrChnrs = new JTable(data, colNames);
		}
		scrollPane.getViewport().add(jTableSnrChnrs);
	}

	protected void components2Dto() throws Throwable {
		artikellagerDto.setNGestehungspreis(wnfPreis.getBigDecimal());
		artikellagerDto.setArtikelIId(internalFrameArtikel.getArtikelDto()
				.getIId());
		artikellagerDto.setMandantCNr(LPMain.getInstance().getTheClient()
				.getMandant());
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLager) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				LagerDto lagerDto = DelegateFactory.getInstance()
						.getLagerDelegate()
						.lagerFindByPrimaryKey((Integer) key);

				try {
					ArtikellagerDto dtoTemp = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.artikellagerFindByPrimaryKey(
									internalFrameArtikel.getArtikelDto()
											.getIId(), lagerDto.getIId());
					DialogFactory.showModalDialog("Fehler",
							"Es gibt bereits einen Eintrag zu diesem Lager.");
					return;
				} catch (ExceptionLP ex) {
					// Wenn nicht, normal weiter

				}

				wtfLager.setText(lagerDto.getCNr());
				artikellagerDto.setLagerIId(lagerDto.getIId());
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

		wbuLager.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lager"));
		wbuLager.setActionCommand(PanelHandlagerbewegung.ACTION_SPECIAL_LAGER_FROM_LISTE);
		wbuLager.addActionListener(this);

		wlaLager.setText(LPMain.getInstance()
				.getTextRespectUISPr("label.lager"));
		wtfLager.setColumnsMax(ArtikelFac.MAX_KATALOG_KATALOG);
		wtfLager.setMandatoryField(true);
		wtfLager.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);

		wlaPreis.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.gestehungspreis"));

		int iNachkommastellen = Defaults.getInstance()
				.getIUINachkommastellenPreiseAllgemein();
		wnfPreis.setFractionDigits(iNachkommastellen);
		wnfPreis.setMandatoryField(true);
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
		jpaWorkingOn.add(wlaLager, new GridBagConstraints(0, 0, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLager, new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaPreis, new GridBagConstraints(2, 0, 1, 1, 0.05,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPreis, new GridBagConstraints(3, 0, 1, 1, 0.1, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		scrollPane.getViewport().add(jTableSnrChnrs);
		jpaWorkingOn.add(scrollPane, new GridBagConstraints(0, 1, 4, 1, 0, 1.0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));

		boolean bDarfGestpreiseaendern = false;
		try {
			bDarfGestpreiseaendern = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_WW_ARTIKEL_GESTPREISE_CU);
		} catch (Throwable ex) {
			handleException(ex, true);
		}

		String[] aWhichButtonIUse = null;
		if (bDarfGestpreiseaendern == true) {
			aWhichButtonIUse = new String[] { ACTION_UPDATE, ACTION_SAVE,
					ACTION_DISCARD, };
		} else {
			aWhichButtonIUse = new String[] {};
		}

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_ARTIKEL;
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null || (key.equals(LPMain.getLockMeForNew()))) {
			Object[][] data = new Object[0][3];
			jTableSnrChnrs = new JTable(data, colNames);
			jTableSnrChnrs.setDefaultRenderer(BigDecimal.class,
					HelperClient.getBigDecimalRenderer());

			scrollPane.getViewport().add(jTableSnrChnrs);

			leereAlleFelder(this);
			clearStatusbar();

			if (key != null && key.equals(LPMain.getLockMeForNew())) {
				jpaWorkingOn.remove(wlaLager);
				jpaWorkingOn.add(wbuLager, new GridBagConstraints(0, 0, 1, 1,
						0.05, 0.0, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
				wbuLager.setEnabled(true);

				if (Helper.short2boolean(internalFrameArtikel.getArtikelDto()
						.getBLagerbewirtschaftet())) {
					// CK Projekt 8688
					LagerDto lagerDto = DelegateFactory.getInstance()
							.getLagerDelegate().getHauptlagerDesMandanten();

					try {
						ArtikellagerDto dtoTemp = DelegateFactory
								.getInstance()
								.getLagerDelegate()
								.artikellagerFindByPrimaryKey(
										internalFrameArtikel.getArtikelDto()
												.getIId(), lagerDto.getIId());
					} catch (ExceptionLP ex) {
						wtfLager.setText(lagerDto.getCNr());
						artikellagerDto.setLagerIId(lagerDto.getIId());
					}
				} else {
					LagerDto tempLager = DelegateFactory
							.getInstance()
							.getLagerDelegate()
							.lagerFindByCNrByMandantCNr(
									LagerFac.LAGER_KEINLAGER);
					wtfLager.setText(tempLager.getCNr());
					artikellagerDto.setLagerIId(tempLager.getIId());
				}

			}

		} else {
			jpaWorkingOn.remove(wbuLager);
			jpaWorkingOn.add(wlaLager,
					new GridBagConstraints(0, 0, 1, 1, 0.05, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			WwArtikellagerPK pk = (WwArtikellagerPK) key;

			artikellagerDto = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.artikellagerFindByPrimaryKey(pk.getArtikel_i_id(),
							pk.getLager_i_id());
			dto2Components();
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LAGER_FROM_LISTE)) {
			dialogQueryLagerFromListe(e);
		}
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			DelegateFactory.getInstance().getLagerDelegate()
					.updateGestpreisArtikellager(artikellagerDto);

			setKeyWhenDetailPanel(new WwArtikellagerPK(
					artikellagerDto.getArtikelIId(),
					artikellagerDto.getLagerIId()));

			super.eventActionSave(e, true);
			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameArtikel.getArtikelDto().getIId()
								.toString());
			}
			eventYouAreSelected(false);
		}
	}
}
