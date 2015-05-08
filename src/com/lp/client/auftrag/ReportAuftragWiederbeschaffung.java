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
package com.lp.client.auftrag;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperRadioButton;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.report.JasperPrintLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Druck der Verfuegbarkeit
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004 - 2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 23.07.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: robert $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/10/19 13:19:03 $
 */
public class ReportAuftragWiederbeschaffung extends PanelBasis implements
		PanelReportIfJRDS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer auftragIId = null;
	protected JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private GridBagLayout gridBagLayout1 = new GridBagLayout();

	private WrapperLabel wlaSortierung = new WrapperLabel();
	private WrapperRadioButton wrbSortLieferant = new WrapperRadioButton();
	private WrapperRadioButton wrbSortAuftragsposition = new WrapperRadioButton();
	private WrapperRadioButton wrbSortGesamtWBZ = new WrapperRadioButton();
	private ButtonGroup buttonGroupSortierung = new ButtonGroup();

	private WrapperLabel wlaWBZWennNichtDefiniert = new WrapperLabel();
	private WrapperNumberField wnfWBZWennNichtDefiniert = new WrapperNumberField();
	private WrapperLabel wlaEinheitWBZWennNichtDefiniert = new WrapperLabel();

	int iFaktor = 1;

	public ReportAuftragWiederbeschaffung(InternalFrame internalFrame,
			Integer iIdAuftragI, String sAdd2Title) throws Throwable {
		super(internalFrame, sAdd2Title);

		auftragIId = iIdAuftragI;
		jbInit();
		initComponents();
	}

	public String getModul() {
		return AuftragReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return AuftragReportFac.REPORT_AUFTRAG_WIEDERBESCHAFFUNG;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		int iSortierung = AuftragReportFac.SORT_REPORT_WIEDERBESCHAFFUNG_LIEFERANT;

		if (wrbSortAuftragsposition.isSelected()) {
			iSortierung = AuftragReportFac.SORT_REPORT_WIEDERBESCHAFFUNG_AUFTRAGSPOSITION;
		} else if (wrbSortGesamtWBZ.isSelected()) {
			iSortierung = AuftragReportFac.SORT_REPORT_WIEDERBESCHAFFUNG_GESAMT_WBZ;
		}

		return DelegateFactory
				.getInstance()
				.getAuftragReportDelegate()
				.printVerfuegbarkeitspruefung(auftragIId, iSortierung,
						wnfWBZWennNichtDefiniert.getDouble() * iFaktor);
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);

		jpaWorkingOn.setLayout(gridBagLayout2);
		wlaSortierung.setText(LPMain.getTextRespectUISPr("label.sortierung"));

		wlaWBZWennNichtDefiniert
				.setText(LPMain
						.getTextRespectUISPr("auft.wiederbeschaffung.wennnichtdefiniert"));

		wlaEinheitWBZWennNichtDefiniert
				.setHorizontalAlignment(SwingConstants.LEFT);

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ARTIKELWIEDERBESCHAFFUNGSZEIT,
						ParameterFac.KATEGORIE_ARTIKEL,
						LPMain.getInstance().getTheClient().getMandant());

		if (parameter.getCWert() != null) {
			if (parameter.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_KW)) {
				wlaEinheitWBZWennNichtDefiniert.setText(LPMain
						.getTextRespectUISPr("lp.kw"));
				iFaktor = 7;
			} else if (parameter.getCWert().equals(
					ArtikelFac.WIEDERBESCHAFFUNGSZEIT_EINHEIT_TAGE)) {
				wlaEinheitWBZWennNichtDefiniert.setText(LPMain
						.getTextRespectUISPr("lp.tage"));
				iFaktor = 1;
			} else {
				wlaEinheitWBZWennNichtDefiniert.setText("?");
			}
		}


		wnfWBZWennNichtDefiniert.setDouble(2D);
		wnfWBZWennNichtDefiniert.setMandatoryField(true);

		wrbSortAuftragsposition.setSelected(true);
		wrbSortAuftragsposition.setText(LPMain
				.getTextRespectUISPr("auft.auftragposition"));
		wrbSortLieferant.setText(LPMain.getTextRespectUISPr("label.lieferant"));
		wrbSortGesamtWBZ.setText(LPMain
				.getTextRespectUISPr("auft.wiederbeschaffung.sort.gesamtwbz"));

		buttonGroupSortierung.add(wrbSortAuftragsposition);
		buttonGroupSortierung.add(wrbSortLieferant);
		buttonGroupSortierung.add(wrbSortGesamtWBZ);

		getInternalFrame().addItemChangedListener(this);
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0,
						0, 0, 0), 0, 0));

		int iZeile = 0;

		jpaWorkingOn.add(wlaSortierung, new GridBagConstraints(0, iZeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wrbSortAuftragsposition, new GridBagConstraints(1,
				iZeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaWBZWennNichtDefiniert, new GridBagConstraints(2,
				iZeile, 1, 1, 0.2, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfWBZWennNichtDefiniert, new GridBagConstraints(3,
				iZeile, 1, 1, 0.1, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaEinheitWBZWennNichtDefiniert,
				new GridBagConstraints(4, iZeile, 1, 1, 0.1, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));

		iZeile++;

		jpaWorkingOn.add(wrbSortLieferant, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wrbSortGesamtWBZ, new GridBagConstraints(1, iZeile, 1,
				1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	protected void eventItemchanged(EventObject eI) {

	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}
}
