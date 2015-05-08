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
package com.lp.client.fertigung;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelDialog;
import com.lp.client.frame.component.PanelDialogKriterien;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.fertigung.service.LosAusAuftragDto;
import com.lp.server.fertigung.service.LosDto;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.util.Helper;

/*
 * <p><I>Dialog zur Nachtraeglichen Entnahme von Material auf ein Los</I> </p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p> <p>Erstellungsdatum
 * <I>17. 10. 2005</I></p> <p> </p>
 * 
 * @author Martin Bluehweis
 * 
 * @version $Revision: 1.8 $
 */
public class PanelDialogLoseAusAuftrag extends PanelDialogKriterien {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<LosAusAuftragDto> losDtos = null;

	private WrapperLabel[] wlaLagerstand = null;
	private WrapperLabel[] wlaVerfuegbar = null;
	private WrapperLabel[] wlaReserviert = null;
	private WrapperLabel[] wlaPosNr = null;
	private WrapperLabel[] wlaFehlmenge = null;
	private WrapperLabel[] wlaOffeneFtgMng = null;
	private WrapperDateField[] wlaBeginn = null;
	private WrapperDateField[] wlaEnde = null;
	private WrapperLabel[] wlaArtikelNummer = null;
	private WrapperLabel[] wlaArtikelBezeichnung = null;
	private WrapperNumberField[] wnfLosgroesse = null;
	private WrapperLabel[] wlaInABLos = null;
	private Integer auftragId = null;

	private JScrollPane jspScrollPane = new JScrollPane();

	private String ACTION_LOSE_ANLEGEN = "ACTION_LOSE_ANLEGEN";

	public PanelDialogLoseAusAuftrag(InternalFrame internalFrame,
			Integer auftragIId, String title) throws Throwable {
		super(internalFrame, title);
		this.auftragId = auftragIId;
		init();
		setDefaults();
		initComponents();
	}

	/**
	 * Dialog initialisieren
	 * 
	 * @throws Throwable
	 */
	private void init() throws Throwable {

		losDtos = DelegateFactory.getInstance().getFertigungDelegate()
				.vorschlagMitUnterlosenAusAuftrag(auftragId);

		// PJ18850
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_STUECKLISTENFREIGABE)) {

			boolean bDialogAngezeigt = false;
			boolean bTrotzdemProduzieren = false;

			ArrayList<LosAusAuftragDto> losDtosTemp = new ArrayList();

			if (losDtos != null && losDtos.size() > 0) {
				for (int i = 0; i < losDtos.size(); i++) {
					if (losDtos.get(i).getLosDto().getStuecklisteIId() != null) {
						StuecklisteDto stklDto = DelegateFactory
								.getInstance()
								.getStuecklisteDelegate()
								.stuecklisteFindByPrimaryKey(
										losDtos.get(i).getLosDto()
												.getStuecklisteIId());

						if (stklDto.getTFreigabe() == null) {

							if (bDialogAngezeigt == false) {

								bTrotzdemProduzieren = DialogFactory
										.showModalJaNeinDialog(
												getInternalFrame(),
												LPMain.getTextRespectUISPr("fert.error.losanlegen.auftrag.stklnichtfreigegeben"));
								bDialogAngezeigt = true;
							}

							if (bTrotzdemProduzieren == true) {
								losDtosTemp.add(losDtos.get(i));
							}

						} else {
							losDtosTemp.add(losDtos.get(i));
						}
					}

				}

				losDtos = losDtosTemp;

			}
		}

		if (losDtos != null && losDtos.size() > 0) {

			wlaLagerstand = new WrapperLabel[losDtos.size()];
			wlaFehlmenge = new WrapperLabel[losDtos.size()];
			wlaOffeneFtgMng = new WrapperLabel[losDtos.size()];
			wlaInABLos = new WrapperLabel[losDtos.size()];
			wlaReserviert = new WrapperLabel[losDtos.size()];
			wlaPosNr = new WrapperLabel[losDtos.size()];

			wlaArtikelBezeichnung = new WrapperLabel[losDtos.size()];

			wlaArtikelNummer = new WrapperLabel[losDtos.size()];
			wnfLosgroesse = new WrapperNumberField[losDtos.size()];
			wlaVerfuegbar = new WrapperLabel[losDtos.size()];
			wlaBeginn = new WrapperDateField[losDtos.size()];
			wlaEnde = new WrapperDateField[losDtos.size()];

			for (int i = 0; i < losDtos.size(); i++) {

				BigDecimal bdBereitsFuerAuftragInFertigung = BigDecimal.ZERO;

				LosAusAuftragDto laDto = losDtos.get(i);
				String sLabelVorhandene = "<html>";
				if (laDto.getBereitsVorhandeneLose() != null) {

					for (int k = 0; k < laDto.getBereitsVorhandeneLose().length; k++) {

						LosDto losDto = laDto.getBereitsVorhandeneLose()[k];

						if (!losDto.getStatusCNr().equals(
								LocaleFac.STATUS_STORNIERT)) {

							sLabelVorhandene += losDto.getCNr()
									+ " "
									+ losDto.getStatusCNr()
									+ " "
									+ Helper.formatZahl(
											losDto.getNLosgroesse(), LPMain
													.getTheClient().getLocUi())
									+ "<br>";
							bdBereitsFuerAuftragInFertigung = bdBereitsFuerAuftragInFertigung
									.add(losDto.getNLosgroesse());
						}
					}

				}

				sLabelVorhandene += "</html>";

				BigDecimal verfuegbar = new BigDecimal(0);

				if (laDto.getLagerstand() != null) {
					wlaLagerstand[i] = new WrapperLabel(
							Helper.formatZahlWennUngleichNull(laDto
									.getLagerstand(), 2, LPMain.getTheClient()
									.getLocUi()));
					verfuegbar = laDto.getLagerstand();
				} else {
					wlaLagerstand[i] = new WrapperLabel("");
				}

				if (laDto.getReservierungen() != null) {
					wlaReserviert[i] = new WrapperLabel(
							Helper.formatZahlWennUngleichNull(laDto
									.getReservierungen(), 2, LPMain
									.getTheClient().getLocUi()));
				} else {
					wlaReserviert[i] = new WrapperLabel("");
				}

				if (laDto.getFehlmengen() != null) {
					wlaFehlmenge[i] = new WrapperLabel(
							Helper.formatZahlWennUngleichNull(laDto
									.getFehlmengen(), 2, LPMain.getTheClient()
									.getLocUi()));

					verfuegbar = verfuegbar.subtract(laDto.getFehlmengen());

				} else {
					wlaFehlmenge[i] = new WrapperLabel("");
				}
				if (laDto.getOffeneFertigungsmenge() != null) {
					wlaOffeneFtgMng[i] = new WrapperLabel(
							Helper.formatZahlWennUngleichNull(laDto
									.getOffeneFertigungsmenge(), 2, LPMain
									.getTheClient().getLocUi()));

					verfuegbar = verfuegbar.add(laDto
							.getOffeneFertigungsmenge());

				} else {
					wlaOffeneFtgMng[i] = new WrapperLabel("");
				}
				if (laDto.getAuftragspositionsnummer() != null) {
					wlaPosNr[i] = new WrapperLabel(
							laDto.getAuftragspositionsnummer() + "");
				} else {
					wlaPosNr[i] = new WrapperLabel("U");
				}

				wlaVerfuegbar[i] = new WrapperLabel(
						Helper.formatZahlWennUngleichNull(verfuegbar, 2, LPMain
								.getTheClient().getLocUi()));

				wlaArtikelNummer[i] = new WrapperLabel("Materialliste");
				wlaArtikelBezeichnung[i] = new WrapperLabel(losDtos.get(i)
						.getLosDto().getCProjekt());

				if (losDtos.get(i).getLosDto().getStuecklisteIId() != null) {
					StuecklisteDto stklDto = DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.stuecklisteFindByPrimaryKey(
									losDtos.get(i).getLosDto()
											.getStuecklisteIId());
					wlaArtikelNummer[i] = new WrapperLabel(stklDto
							.getArtikelDto().getCNr());
					wlaArtikelBezeichnung[i] = new WrapperLabel(stklDto
							.getArtikelDto().formatBezeichnung());

				}
				wlaArtikelNummer[i].setHorizontalAlignment(SwingConstants.LEFT);
				wlaArtikelBezeichnung[i]
						.setHorizontalAlignment(SwingConstants.LEFT);

				WrapperDateField wdf = new WrapperDateField();
				wdf.setDate(laDto.getLosDto().getTProduktionsbeginn());
				wdf.setMandatoryField(true);
				wlaBeginn[i] = wdf;

				wdf = new WrapperDateField();
				wdf.setDate(laDto.getLosDto().getTProduktionsende());
				wdf.setMandatoryField(true);
				wlaEnde[i] = wdf;

				wlaInABLos[i] = new WrapperLabel(
						Helper.formatZahlWennUngleichNull(
								bdBereitsFuerAuftragInFertigung, 2, LPMain
										.getTheClient().getLocUi()));

				WrapperNumberField wnf = new WrapperNumberField();
				wnf.setBigDecimal(laDto.getLosDto().getNLosgroesse()
						.subtract(bdBereitsFuerAuftragInFertigung));

				wnf.setMandatoryField(true);
				wnfLosgroesse[i] = wnf;

				if (laDto.isBDatumVerschoben()) {
					wlaPosNr[i].setForeground(Color.RED);
					wlaArtikelNummer[i].setForeground(Color.RED);
					wlaArtikelBezeichnung[i].setForeground(Color.RED);
				}

				wlaArtikelNummer[i].setToolTipText(sLabelVorhandene);

			}

		}

		getInternalFrame().addItemChangedListener(this);

		iZeile++;

		jpaWorkingOn.add(new WrapperLabel("PosNr"), new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		WrapperLabel wlaStueckliste = new WrapperLabel(
				LPMain.getTextRespectUISPr("stkl.stueckliste"));

		wlaStueckliste.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaStueckliste, new GridBagConstraints(1, iZeile, 1,
				1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel wlaBezeichnung = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.bezeichnung"));

		wlaBezeichnung.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaBezeichnung, new GridBagConstraints(2, iZeile, 1,
				1, 1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("lp.lagerstand")),
				new GridBagConstraints(3, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("fert.loseausauftrag.offenftgmng")),
						new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));

		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("lp.reserviert")),
				new GridBagConstraints(5, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		jpaWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("fert.tab.oben.fehlmengen.title")),
						new GridBagConstraints(6, iZeile, 1, 1, 0.3, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("lp.verfuegbar")),
				new GridBagConstraints(7, iZeile, 1, 1, 0.3, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		WrapperLabel wlaBeginnLbl = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.beginn"));
		wlaBeginnLbl.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaBeginnLbl, new GridBagConstraints(8, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		WrapperLabel wlaEndeLbl = new WrapperLabel(
				LPMain.getTextRespectUISPr("lp.ende"));
		wlaEndeLbl.setHorizontalAlignment(SwingConstants.LEFT);
		jpaWorkingOn.add(wlaEndeLbl, new GridBagConstraints(9, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("fert.los.losausauftrag.inablos")),
						new GridBagConstraints(10, iZeile, 1, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));
		jpaWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("label.losgroesse")),
						new GridBagConstraints(11, iZeile, 1, 1, 0, 0.0,
								GridBagConstraints.CENTER,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 0, 0));

		iZeile++;
		for (int i = 0; i < losDtos.size(); i++) {
			jpaWorkingOn.add(wlaPosNr[i], new GridBagConstraints(0, iZeile, 1,
					1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 20,
					0));

			jpaWorkingOn.add(wlaArtikelNummer[i], new GridBagConstraints(1,
					iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 40,
					0));
			jpaWorkingOn.add(wlaArtikelBezeichnung[i], new GridBagConstraints(
					2, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 90,
					0));
			jpaWorkingOn.add(wlaLagerstand[i],
					new GridBagConstraints(3, iZeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wlaOffeneFtgMng[i],
					new GridBagConstraints(4, iZeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wlaReserviert[i], new GridBagConstraints(5,
					iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wlaFehlmenge[i], new GridBagConstraints(6, iZeile,
					1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			jpaWorkingOn.add(wlaVerfuegbar[i], new GridBagConstraints(7,
					iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wlaBeginn[i], new GridBagConstraints(8, iZeile, 1,
					1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wlaEnde[i], new GridBagConstraints(9, iZeile, 1,
					1, 0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			jpaWorkingOn.add(wlaInABLos[i], new GridBagConstraints(10, iZeile,
					1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

			BigDecimal bdBereitsFuerAuftragInFertigung = BigDecimal.ZERO;

			if (losDtos.get(i).getBereitsVorhandeneLose() != null) {
				for (int k = 0; k < losDtos.get(i).getBereitsVorhandeneLose().length; k++) {
					LosDto losDto = losDtos.get(i).getBereitsVorhandeneLose()[k];

					if (!losDto.getStatusCNr().equals(
							LocaleFac.STATUS_STORNIERT)) {
						bdBereitsFuerAuftragInFertigung = bdBereitsFuerAuftragInFertigung
								.add(losDto.getNLosgroesse());
					}
				}

			}

			if (wnfLosgroesse[i].getBigDecimal().doubleValue() < 0) {
				wnfLosgroesse[i].setBackground(Color.MAGENTA);
			} else if (bdBereitsFuerAuftragInFertigung.doubleValue() > 0
					&& wnfLosgroesse[i].getBigDecimal().doubleValue() > 0) {
				wnfLosgroesse[i].setBackground(Color.GREEN);
			}

			jpaWorkingOn.add(wnfLosgroesse[i], new GridBagConstraints(11,
					iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), -120, 0));

			iZeile++;

		}

		jspScrollPane.setViewportView(jpaWorkingOn);
		jspScrollPane.setAutoscrolls(true);

		this.add(jspScrollPane, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		super.eventActionSpecial(e);
		if (e.getActionCommand().equals(ACTION_SPECIAL_OK)) {

			if (allMandatoryFieldsSetDlg()) {
				for (int i = 0; i < losDtos.size(); i++) {
					losDtos.get(i).getLosDto()
							.setNLosgroesse(wnfLosgroesse[i].getBigDecimal());
					losDtos.get(i).getLosDto()
							.setTProduktionsbeginn(wlaBeginn[i].getDate());
					losDtos.get(i).getLosDto()
							.setTProduktionsende(wlaEnde[i].getDate());
				}
				int i = DelegateFactory.getInstance().getFertigungDelegate()
						.loseAusAuftragAnlegen(losDtos, auftragId);
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.info"), i
								+ " Lose angelegt.");
				getInternalFrame().closePanelDialog();
			}

		}
	}

	/**
	 * eventItemchanged.
	 * 
	 * @param eI
	 *            EventObject
	 * @throws ExceptionForLPClients
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {

		} else if (e.getID() == ItemChangedEvent.ACTION_KRITERIEN_HAVE_BEEN_SELECTED) {

		}
	}

	/**
	 * Dialogfenster zur Artikelauswahl.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */

	private void setDefaults() throws Throwable {

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}
}
