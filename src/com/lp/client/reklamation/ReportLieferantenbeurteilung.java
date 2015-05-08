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
package com.lp.client.reklamation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.EventObject;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.sf.jasperreports.engine.JasperExportManager;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperDateRangeController;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperSelectField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.fastlanereader.gui.QueryType;
import com.lp.server.partner.service.BrancheDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.reklamation.service.ReklamationReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportLieferantenbeurteilung extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	static final public String ACTION_SPECIAL_LIEFERANT_FROM_LISTE = "ACTION_SPECIAL_LIEFERANT_FROM_LISTE";
	static final private String ACTION_SPECIAL_FLR_BRANCHE = "action_special_flr_branche";

	static final public String ACTION_SPECIAL_ALS_EMAIL_VERSENDEN = "ACTION_SPECIAL_ALS_EMAIL_VERSENDEN";

	Integer brancheIId = null;
	private Integer lieferantIId = null;
	protected WrapperButton wbuBranche = null;
	protected WrapperTextField wtfBranche = null;

	private WrapperSelectField wsfLiefergruppe = new WrapperSelectField(
			WrapperSelectField.LIEFERGRUPPE, getInternalFrame(), true);

	private WrapperLabel wlaDatumVon = new WrapperLabel();
	private WrapperDateField wdfDatumVon = new WrapperDateField();

	private WrapperLabel wlaDatumBis = new WrapperLabel();
	private WrapperDateField wdfDatumBis = new WrapperDateField();

	private PanelQueryFLR panelQueryFLRLieferant = null;
	protected PanelQueryFLR panelQueryFLRBranche = null;

	private WrapperDateRangeController wdrBereich = null;

	private WrapperButton wbuLieferant = new WrapperButton();

	private WrapperButton wbuAlsEmailVersenden = new WrapperButton();

	private WrapperTextField wtfLieferant = new WrapperTextField();

	public ReportLieferantenbeurteilung(InternalFrame internalFrame,
			String add2Title) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance()
				.getTextRespectUISPr("rekla.lieferantenbeurteilung");
		jbInit();
		initComponents();
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wdfDatumVon;
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);

		wlaDatumVon.setText(LPMain.getInstance().getTextRespectUISPr("lp.von"));
		wlaDatumBis.setText(LPMain.getInstance().getTextRespectUISPr("lp.bis"));

		wdfDatumVon.setMandatoryField(true);
		wdfDatumBis.setMandatoryField(true);

		wdrBereich = new WrapperDateRangeController(wdfDatumVon, wdfDatumBis);

		// Letztes Quartal vorbesetzen

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());

		int iMonat = c.get(Calendar.MONTH) + 1;

		int iQuartal = iMonat / 3;

		c.set(Calendar.MONTH, (iQuartal * 3) - 1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

		wdfDatumBis.setTimestamp(new Timestamp(c.getTimeInMillis()));

		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 2);

		wdfDatumVon.setTimestamp(new Timestamp(c.getTimeInMillis()));

		wbuLieferant.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.lieferant"));

		wbuLieferant.setActionCommand(ACTION_SPECIAL_LIEFERANT_FROM_LISTE);
		wbuLieferant.addActionListener(this);

		wbuAlsEmailVersenden.setText(LPMain.getInstance().getTextRespectUISPr(
				"rekla.lieferantenbeurteilung.alsemailversenden"));
		wbuAlsEmailVersenden
				.setActionCommand(ACTION_SPECIAL_ALS_EMAIL_VERSENDEN);
		wbuAlsEmailVersenden.addActionListener(this);

		wtfLieferant.setActivatable(false);
		wtfLieferant.setColumnsMax(Facade.MAX_UNBESCHRAENKT);

		wbuBranche = new WrapperButton();
		wbuBranche.setActionCommand(ACTION_SPECIAL_FLR_BRANCHE);

		wbuBranche.addActionListener(this);

		wbuBranche.setText(LPMain.getInstance().getTextRespectUISPr(
				"button.branche"));

		wtfBranche = new WrapperTextField(PartnerFac.MAX_BRANCHE);
		wtfBranche.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		int iZeile = 0;

		jpaWorkingOn.add(wlaDatumVon, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatumVon, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaDatumBis, new GridBagConstraints(2, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfDatumBis, new GridBagConstraints(3, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdrBereich, new GridBagConstraints(4, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		jpaWorkingOn.add(wbuLieferant, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfLieferant, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_EMAILVERSAND)) {
			jpaWorkingOn.add(wbuAlsEmailVersenden,
					new GridBagConstraints(3, iZeile, 2, 1, 0.1, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
		}

		iZeile++;

		jpaWorkingOn.add(wbuBranche, new GridBagConstraints(0, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfBranche, new GridBagConstraints(1, iZeile, 1, 1,
				0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wsfLiefergruppe.getWrapperButton(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wsfLiefergruppe.getWrapperTextField(),
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

	}

	public String getModul() {
		return ReklamationReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return ReklamationReportFac.REPORT_LIEFERANTENBEURTEILUNG;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRLieferant) {
				try {
					Integer key = (Integer) ((ISourceEvent) e.getSource())
							.getIdSelected();

					LieferantDto lieferantDto = DelegateFactory.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(key);
					wtfLieferant.setText(lieferantDto.getPartnerDto()
							.formatFixTitelName1Name2());
					lieferantIId = lieferantDto.getIId();
				} catch (Throwable ex) {
					LPMain.getInstance().exitFrame(getInternalFrame(), ex);
				}
			} else if (e.getSource() == panelQueryFLRBranche) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				if (key != null) {
					BrancheDto brancheDto = DelegateFactory.getInstance()
							.getPartnerDelegate().brancheFindByPrimaryKey(key);
					brancheIId = key;
					wtfBranche.setText(brancheDto.getCNr());
				}
			}
		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRLieferant) {
				wtfLieferant.setText(null);
				lieferantIId = null;
			} else if (e.getSource() == panelQueryFLRBranche) {
				wtfBranche.setText(null);
				brancheIId = null;
			}
		}
	}

	private void dialogQueryLieferant() throws Throwable {
		panelQueryFLRLieferant = PartnerFilterFactory.getInstance()
				.createPanelFLRLieferantGoto(getInternalFrame(), lieferantIId,
						true, true);
		new DialogQuery(panelQueryFLRLieferant);
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_LIEFERANT_FROM_LISTE)) {
			dialogQueryLieferant();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_FLR_BRANCHE)) {
			String[] aWhichButtonIUse = SystemFilterFactory.getInstance()
					.createButtonArray(false, true);

			final QueryType[] querytypes = null;
			final FilterKriterium[] filters = null;
			panelQueryFLRBranche = new PanelQueryFLR(querytypes, filters,
					QueryParameters.UC_ID_BRANCHE, aWhichButtonIUse,
					getInternalFrame(), LPMain.getInstance()
							.getTextRespectUISPr("lp.branche"));

			if (brancheIId != null) {
				panelQueryFLRBranche.setSelectedId(brancheIId);
			}
			new DialogQuery(panelQueryFLRBranche);
		} else if (e.getActionCommand().equals(
				ACTION_SPECIAL_ALS_EMAIL_VERSENDEN)) {

			JasperPrintLP lpreport = getReport(null);
			Integer[] lieferantenIdsFuerEmailVersand = (Integer[]) lpreport
					.getAdditionalInformation("LIEFERANTEN");

			Integer lieferantIIdVorher = null;
			if (lieferantIId != null) {
				lieferantIIdVorher = new Integer(lieferantIId);
			}

			TreeMap<String, String> hmLieferantenNichtVersandt = new TreeMap<String, String>();
			int iEmailsVersandt = 0;
			if (lieferantenIdsFuerEmailVersand != null
					&& lieferantenIdsFuerEmailVersand.length > 0) {
				String sAbsenderadresse = null;
				PersonalDto personalDto = DelegateFactory
						.getInstance()
						.getPersonalDelegate()
						.personalFindByPrimaryKey(
								LPMain.getInstance().getTheClient()
										.getIDPersonal());
				if (personalDto.getCEmail() != null) {
					sAbsenderadresse = personalDto.getCEmail();
					if (sAbsenderadresse == null) {

						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
								new Exception(
										LPMain.getTextRespectUISPr("bestellung.fehler.keinemailadressedefiniert")));
					}
					if (!Helper.validateEmailadresse(sAbsenderadresse)) {
						throw new ExceptionLP(
								EJBExceptionLP.FEHLER_UNGUELTIGE_EMAILADRESSE,
								new Exception(
										LPMain.getTextRespectUISPr("bestellung.fehler.ungueltigemailadressedefiniert")));
					}
				} else {
					throw new ExceptionLP(
							EJBExceptionLP.FEHLER_MAHNUNGSVERSAND_KEINE_ABSENDERADRESSE,
							new Exception(
									LPMain.getTextRespectUISPr("bestellung.fehler.keinemailadressedefiniert")));
				}

				for (int i = 0; i < lieferantenIdsFuerEmailVersand.length; i++) {
					lieferantIId = lieferantenIdsFuerEmailVersand[i];
					JasperPrintLP reportEinesLieferanten = getReport(null);
					// Email aus Kopfdaten holen
					LieferantDto lfDto = DelegateFactory.getInstance()
							.getLieferantDelegate()
							.lieferantFindByPrimaryKey(lieferantIId);
					if (lfDto.getPartnerDto().getCEmail() != null
							&& lfDto.getPartnerDto().getCEmail().length() > 0) {
						String email = lfDto.getPartnerDto().getCEmail();

						VersandauftragDto versDto = new VersandauftragDto();
						versDto.setCEmpfaenger(email);
						versDto.setCAbsenderadresse(sAbsenderadresse);
						String sText = LPMain
								.getTextRespectUISPr("rekla.lieferantenbeurteilung.mailversand.betreff");
						versDto.setCDateiname(sText + ".pdf");
						lfDto.getPartnerDto().getLocaleCNrKommunikation();
						versDto.setCBetreff(sText);
						versDto.setOInhalt(JasperExportManager
								.exportReportToPdf(reportEinesLieferanten
										.getPrint()));
						DelegateFactory.getInstance().getVersandDelegate()
								.updateVersandauftrag(versDto, false);

						iEmailsVersandt++;

					} else {
						// Liste von nicht vorhandenen Lieferanten ausgeben
						hmLieferantenNichtVersandt.put(lfDto.getPartnerDto()
								.formatFixTitelName1Name2(), "");
					}

				}

			}
			lieferantIId = lieferantIIdVorher;

			String s = "Es wurden " + iEmailsVersandt + " Versandauftr\u00E4ge erzeugt.";

			if (hmLieferantenNichtVersandt.size() > 0) {
				s += "\r\n\r\nFolgende Lieferanten haben jedoch keine E-Mail Adresse hinterlegt:\r\n";
				Iterator<String> it = hmLieferantenNichtVersandt.keySet()
						.iterator();
				while (it.hasNext()) {
					String key = it.next();
					s += key + "\r\n";
				}
			}

			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.info"), s);

		}
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		JasperPrintLP lpreport = DelegateFactory
				.getInstance()
				.getReklamationReportDelegate()
				.printLieferantenbeurteilung(wdfDatumVon.getTimestamp(),
						wdfDatumBis.getTimestamp(), brancheIId, lieferantIId,
						wsfLiefergruppe.getIKey());

		return lpreport;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
