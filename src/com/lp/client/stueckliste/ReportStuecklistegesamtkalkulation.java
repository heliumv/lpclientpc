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
package com.lp.client.stueckliste;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JRExporterParameter;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.stueckliste.service.StuecklisteReportFac;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.HVPDFExporter;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class ReportStuecklistegesamtkalkulation extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private InternalFrameStueckliste internalFrameStueckliste = null;
	private Integer stuecklisteIId = null;
	private WrapperLabel wlaStueckliste = new WrapperLabel();
	private WrapperTextField wtStueckliste = new WrapperTextField();
	private JButton jbDokumentenblageLief1PreisUebernehmen = new JButton();

	private WrapperLabel wlaLosgroesse = new WrapperLabel();
	private WrapperNumberField wnfLosgroesse = new WrapperNumberField();
	private WrapperCheckBox wcbMitPreisenDerLetzten2Jahre = new WrapperCheckBox();
	private WrapperCheckBox wcbUnterstklVerdichten = new WrapperCheckBox();

	private String ACTION_SPEICHERN = "ACTION_SPEICHERN";

	private WrapperLabel wlaEinheit = new WrapperLabel();

	public ReportStuecklistegesamtkalkulation(
			InternalFrameStueckliste internalFrame, String add2Title,
			Integer stuecklisteIId) throws Throwable {
		super(internalFrame, add2Title);
		LPMain.getInstance().getTextRespectUISPr("stkl.stueckliste");
		internalFrameStueckliste = internalFrame;
		this.stuecklisteIId = stuecklisteIId;
		jbInit();
		initComponents();
		if (stuecklisteIId != null) {
			wtStueckliste.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
			StuecklisteDto dto = DelegateFactory.getInstance()
					.getStuecklisteDelegate()
					.stuecklisteFindByPrimaryKey(stuecklisteIId);
			wtStueckliste.setText(dto.getArtikelDto()
					.formatArtikelbezeichnung());
			wnfLosgroesse.setBigDecimal(dto.getNLosgroesse());
			wlaEinheit.setText(dto.getArtikelDto().getEinheitCNr().trim());
		}

	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfLosgroesse;
	}

	private void jbInit() throws Exception {
		this.setLayout(gridBagLayout1);
		jpaWorkingOn.setLayout(gridBagLayout2);
		internalFrameStueckliste.addItemChangedListener(this);
		wlaStueckliste.setText("Selektierte St\u00FCckliste");
		wlaLosgroesse.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.losgroesse"));

		jbDokumentenblageLief1PreisUebernehmen.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"stkl.gesamtkalkulation.lief1preisuebernehmen"));
		jbDokumentenblageLief1PreisUebernehmen
				.setActionCommand(ACTION_SPEICHERN);
		jbDokumentenblageLief1PreisUebernehmen.addActionListener(this);

		wcbMitPreisenDerLetzten2Jahre.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"stkl.gesamtkalkulation.mitpreisenderletzten2jahre"));

		wcbUnterstklVerdichten.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"stkl.gesamtkalkulation.unterstklverdichten"));

		wlaStueckliste.setHorizontalAlignment(SwingConstants.RIGHT);
		wlaStueckliste.setHorizontalTextPosition(SwingConstants.RIGHT);
		wtStueckliste.setEditable(false);
		wtStueckliste.setMandatoryField(true);
		wnfLosgroesse.setMandatoryField(true);
		wlaEinheit.setHorizontalAlignment(wlaEinheit.LEFT);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaStueckliste, new GridBagConstraints(0, 0, 1, 1, 1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wtStueckliste, new GridBagConstraints(1, 0, 1, 1, 1,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 150, 0));
		jpaWorkingOn.add(wcbMitPreisenDerLetzten2Jahre, new GridBagConstraints(
				3, 0, 1, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 250, 0));

		jpaWorkingOn.add(wlaLosgroesse, new GridBagConstraints(0, 1, 1, 1, 0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfLosgroesse, new GridBagConstraints(1, 1, 1, 1, 1,
				0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), -100, 0));
		jpaWorkingOn.add(wlaEinheit, new GridBagConstraints(2, 1, 1, 1, 1, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(jbDokumentenblageLief1PreisUebernehmen,
				new GridBagConstraints(3, 1, 1, 1, 1, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 0, 0));
		
		jpaWorkingOn.add(wcbUnterstklVerdichten, new GridBagConstraints(
				3, 2, 1, 1, 1, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 250, 0));


	}

	public String getModul() {
		return StuecklisteReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return StuecklisteReportFac.REPORT_STUECKLISTE_GESAMTKALKULATION;
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

		if (e.getActionCommand().equals(ACTION_SPEICHERN)) {
			JasperPrintLP print = DelegateFactory
					.getInstance()
					.getStuecklisteReportDelegate()
					.printGesamtkalkulation(stuecklisteIId,
							wnfLosgroesse.getBigDecimal(), true, false,
							wcbUnterstklVerdichten.isSelected());

			// und in Dokumentenablage speichern
			PrintInfoDto oInfo = DelegateFactory
					.getInstance()
					.getJCRDocDelegate()
					.getPathAndPartnerAndTable(stuecklisteIId,
							QueryParameters.UC_ID_STUECKLISTE);

			DocPath docPath = null;
			if (oInfo != null) {
				docPath = oInfo.getDocPath();
			}
			if (docPath != null) {
				// Nur archivieren wenn nicht in dieser Tabelle vorhanden

				String sName = "stk_gesamtkalkulation";

				JCRDocDto jcrDocDto = new JCRDocDto();

				Integer iPartnerIId = null;
				MandantDto mandantDto = DelegateFactory
						.getInstance()
						.getMandantDelegate()
						.mandantFindByPrimaryKey(
								LPMain.getTheClient().getMandant());
				iPartnerIId = mandantDto.getPartnerIId();

				// File file = File.createTempFile(sName, ".jrprint");
				// file.delete();
				// JRSaver.saveObject(print.getPrint(), file);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				HVPDFExporter exporter = new HVPDFExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						print.getPrint());
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

				exporter.exportReport();
				jcrDocDto.setbData(baos.toByteArray());

				// AD JR JRPrintSaveContributor saver = new
				// JRPrintSaveContributor(getLocale(),));
				// AD JR saver.save(print.getPrint(), file);
				// jcrDocDto.setbData(Helper.getBytesFromFile(file));
				// jcrDocDto.setJasperPrint(print.getPrint());
				// file.delete();
				jcrDocDto.setDocPath(docPath.add(new DocNodeFile(sName)));
				jcrDocDto.setlPartner(iPartnerIId);
				jcrDocDto.setsBelegnummer("");
				jcrDocDto.setsBelegart(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
				jcrDocDto.setlAnleger(LPMain.getTheClient().getIDPersonal());
				jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
				jcrDocDto.setsSchlagworte(" ");
				jcrDocDto.setsName(sName);
				jcrDocDto.setsFilename(sName + ".pdf");
				if (oInfo.getTable() != null) {
					jcrDocDto.setsTable(oInfo.getTable());
				} else {
					jcrDocDto.setsTable(" ");
				}
				jcrDocDto.setsRow(" ");

				jcrDocDto.setsMIME(".pdf");
				jcrDocDto.setlSicherheitsstufe(JCRDocFac.SECURITY_ARCHIV);
				jcrDocDto.setsGruppierung(JCRDocFac.DEFAULT_ARCHIV_GRUPPE);
				jcrDocDto.setbVersteckt(false);
				jcrDocDto.setlVersion(DelegateFactory.getInstance()
						.getJCRDocDelegate().getNextVersionNumer(jcrDocDto));
				DelegateFactory.getInstance().getJCRDocDelegate()
						.addNewDocumentOrNewVersionOfDocument(jcrDocDto);
			}

		}

	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		try {
			JasperPrintLP p = DelegateFactory
					.getInstance()
					.getStuecklisteReportDelegate()
					.printGesamtkalkulation(stuecklisteIId,
							wnfLosgroesse.getBigDecimal(), false,
							wcbMitPreisenDerLetzten2Jahre.isSelected(),
							wcbUnterstklVerdichten.isSelected());
			internalFrameStueckliste.getStuecklisteDto().setNLosgroesse(
					wnfLosgroesse.getBigDecimal());
			return p;
		} catch (ExceptionLP ex) {
			if (ex.getICode() == com.lp.util.EJBExceptionLP.FEHLER_ZAHL_ZU_KLEIN) {
				DialogFactory.showModalDialog("Fehler",
						"Losgr\u00F6sse zu klein");
			} else {
				// brauche ich
				handleException(ex, false);
			}

			return null;
		}

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
