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
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import net.miginfocom.swing.MigLayout;

import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.LiquititaetsvorschauImportDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Druck der Saldenliste
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 15.06.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/07/02 13:11:48 $
 */
public class ReportLiquiditaetsvorschau extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WrapperLabel wlaKontostand = new WrapperLabel();
	private WrapperLabel wlaBetrachtungszeitraum = new WrapperLabel();
	private WrapperLabel wlaKreditlimit = new WrapperLabel();
	private WrapperNumberField wnfKreditlimit = new WrapperNumberField();
	private JSpinner wspBetrachtungszeitraum = new JSpinner();
	private WrapperNumberField wnfKontostand = new WrapperNumberField();
	private WrapperLabel wlaBerechnungZieltage = new WrapperLabel();
	private WrapperCheckBox wcbMitPlankosten = new WrapperCheckBox();
	private WrapperCheckBox wcbMitOffenenAngeboten = new WrapperCheckBox();
	private WrapperCheckBox wcbMitOffenenBestellungen = new WrapperCheckBox();
	private WrapperCheckBox wcbMitOffenenAuftraegen = new WrapperCheckBox();
	private WrapperButton wbRefreshKontostand;

	private ButtonGroup buttonGroup = new ButtonGroup();
	WrapperRadioButton wrbZahlungsmoral = new WrapperRadioButton();
	WrapperRadioButton wrbVereinbZahlungsziel = new WrapperRadioButton();

	protected JPanel jpaWorkingOn = new JPanel();

	public ReportLiquiditaetsvorschau(InternalFrame internalFrame,
			String sAdd2Title) throws Throwable {
		// reporttitel: das PanelReport kriegt einen Titel, der wird vom
		// Framework hergenommen
		super(internalFrame, sAdd2Title);

		jbInit();
		setDefaults();
		initPanel();
		initComponents();

	}

	protected void jbInit() throws Throwable {
		
		this.setLayout(new GridBagLayout());
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		wbRefreshKontostand = new WrapperButton("", HelperClient.createImageIcon("refresh.png"));
		wbRefreshKontostand.addActionListener(this);
		
		wlaKontostand
				.setText(LPMain
						.getTextRespectUISPr("fb.report.liquiditaetsvorschau.kontostand"));
		wlaBetrachtungszeitraum
				.setText(LPMain
						.getTextRespectUISPr("fb.report.liquiditaetsvorschau.betrachtungszeitraum"));
		wlaKreditlimit.setText(LPMain.getTextRespectUISPr("part.kreditlimit"));

		wlaBerechnungZieltage
				.setText(LPMain
						.getTextRespectUISPr("fb.liquiditaetsvorschau.berechnungzieltage"));

		wrbZahlungsmoral.setText(LPMain
				.getTextRespectUISPr("fb.liquiditaetsvorschau.zahlungsmoral"));
		wrbVereinbZahlungsziel.setText(LPMain
				.getTextRespectUISPr("fb.liquiditaetsvorschau.zalungsziel"));

		wcbMitPlankosten
				.setText(LPMain
						.getTextRespectUISPr("finanz.liquiditaetsvorschau.mitplankosten"));

		wcbMitPlankosten
				.setText(LPMain
						.getTextRespectUISPr("finanz.liquiditaetsvorschau.mitplankosten"));
		wcbMitOffenenAngeboten
				.setText(LPMain
						.getTextRespectUISPr("finanz.liquiditaetsvorschau.mitoffenenangeboten"));
		wcbMitOffenenAuftraegen
				.setText(LPMain
						.getTextRespectUISPr("finanz.liquiditaetsvorschau.mitoffenenauftraegen"));
		wcbMitOffenenBestellungen
				.setText(LPMain
						.getTextRespectUISPr("finanz.liquiditaetsvorschau.mitoffenenbestellungen"));

		buttonGroup.add(wrbZahlungsmoral);
		buttonGroup.add(wrbVereinbZahlungsziel);
		wrbZahlungsmoral.setSelected(true);

		String sKreditlimit = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_KUNDEN,
						ParameterFac.PARAMETER_KREDITLIMIT).getCWert();

		if (sKreditlimit != null && sKreditlimit.trim().length() > 0) {
			int iKreditlimit = Integer.parseInt(sKreditlimit);
			if (iKreditlimit >= 0) {
				wnfKreditlimit.setBigDecimal(new BigDecimal(iKreditlimit));
			}
		}

		wspBetrachtungszeitraum.setValue(20);
		wnfKontostand.setMandatoryField(true);
		wnfKreditlimit.setMandatoryField(true);

		jpaWorkingOn.setLayout(new MigLayout("wrap 11", "[15%,fill|10%,fill|5%,fill|5%,fill|10%,fill|10%,fill|5%,fill|10%,fill|10%,fill|10%,fill|5%,fill]"));
		
		jpaWorkingOn.add(wlaKontostand);
		jpaWorkingOn.add(wnfKontostand);
		jpaWorkingOn.add(new JLabel(LPMain.getTheClient()
				.getSMandantenwaehrung()));
		jpaWorkingOn.add(wbRefreshKontostand);
		jpaWorkingOn.add(wlaBetrachtungszeitraum, "span 2");
		jpaWorkingOn.add(wspBetrachtungszeitraum);
		jpaWorkingOn.add(new JLabel(LPMain.getTextRespectUISPr("fb.report.liquiditaetsvorschau.wochen")));
		jpaWorkingOn.add(wlaKreditlimit);
		jpaWorkingOn.add(wnfKreditlimit);
		jpaWorkingOn.add(new JLabel(LPMain.getTheClient().getSMandantenwaehrung()));

		jpaWorkingOn.add(wlaBerechnungZieltage, "span 2");
		jpaWorkingOn.add(wrbZahlungsmoral, "span 3");
		jpaWorkingOn.add(wcbMitPlankosten, "span 3");
		jpaWorkingOn.add(wcbMitOffenenAuftraegen, "span");

		jpaWorkingOn.add(wrbVereinbZahlungsziel, "skip 2, span 3");
		jpaWorkingOn.add(wcbMitOffenenBestellungen, "span 3");
		jpaWorkingOn.add(wcbMitOffenenAngeboten, "span");

	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
//		ItemChangedEvent e = (ItemChangedEvent) eI;
	}

	private void initPanel() throws Throwable {

	}

	private void setDefaults() {

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if(e.getSource() ==  wbRefreshKontostand) {
			Integer geschaeftsjahrIId = LPMain.getTheClient().getGeschaeftsJahr();
			wnfKontostand.setBigDecimal(DelegateFactory.getInstance().getFinanzServiceDelegate().getLiquiditaetsKontostand(geschaeftsjahrIId));
		}
	}

	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return FinanzReportFac.REPORT_LIQUIDITAETSVORSCHAU;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {

		ArrayList<LiquititaetsvorschauImportDto> alPlankosten = new ArrayList<LiquititaetsvorschauImportDto>();
		if (wcbMitPlankosten.isSelected()) {

			ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
					.getInstance()
					.getParameterDelegate()
					.getParametermandant(
							ParameterFac.PARAMETER_IMPORT_PLANKOSTEN_DATEI,
							ParameterFac.KATEGORIE_FINANZ,
							LPMain.getTheClient().getMandant());

			String sPfad = parameter.getCWert();

			File f = new File(sPfad);

			if (f.exists() == false) {
				DialogFactory
						.showModalDialog("Fehler", "Die angegebene Datei '"
								+ sPfad + "' exisitert nicht.");
				return null;
			}
			Workbook workbook = Workbook.getWorkbook(f);
			Sheet sheet = workbook.getSheet(0);

			for (int i = 1; i < sheet.getRows(); i++) {
				Cell[] cells = sheet.getRow(i);

				LiquititaetsvorschauImportDto zeile = new LiquititaetsvorschauImportDto();

				if (cells.length > 0 && cells[0] != null
						&& cells[0].getType() != CellType.EMPTY) {
					if (cells[0].getType() == CellType.LABEL) {
						zeile.setBeleg(((LabelCell) cells[0]).getString());
					}
				}
				if (cells.length > 1 && cells[1] != null
						&& cells[1].getType() != CellType.EMPTY) {
					if (cells[1].getType() == CellType.DATE) {
						Date d = ((DateCell) cells[1]).getDate();
						zeile.setDatum(d);
					}
				}

				if (cells.length > 2 && cells[2] != null
						&& cells[2].getType() != CellType.EMPTY) {
					if (cells[2].getType() == CellType.NUMBER
							|| cells[2].getType() == CellType.NUMBER_FORMULA) {
						zeile.setNetto(new BigDecimal(((NumberCell) cells[2])
								.getValue()));
					}
				}

				if (zeile.getNetto() == null) {
					zeile.setNetto(new BigDecimal(0));
				}
				if (cells.length > 3 && cells[3] != null
						&& cells[3].getType() != CellType.EMPTY) {
					if (cells[3].getType() == CellType.NUMBER
							|| cells[3].getType() == CellType.NUMBER_FORMULA) {
						zeile.setMwst(new BigDecimal(((NumberCell) cells[3])
								.getValue()));
					}
				}

				if (zeile.getMwst() == null) {
					zeile.setMwst(new BigDecimal(0));
				}

				if (cells.length > 4 && cells[4] != null
						&& cells[4].getType() != CellType.EMPTY) {
					if (cells[4].getType() == CellType.LABEL) {
						zeile.setPartner(((LabelCell) cells[4]).getString());
					}
				}

				if (zeile.getDatum() != null) {
					alPlankosten.add(zeile);
				}

			}

		}

		return DelegateFactory
				.getInstance()
				.getFinanzReportDelegate()
				.printLiquiditaetsvorschau(wnfKontostand.getBigDecimal(),
						wnfKreditlimit.getBigDecimal(),
						(Integer) wspBetrachtungszeitraum.getValue(),
						wrbZahlungsmoral.isSelected(),
						wcbMitPlankosten.isSelected(), alPlankosten,
						wcbMitOffenenAngeboten.isSelected(),
						wcbMitOffenenBestellungen.isSelected(),
						wcbMitOffenenAuftraegen.isSelected());
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}

}
