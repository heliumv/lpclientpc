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
package com.lp.client.finanz;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.PrintKontoblaetterModel;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.util.report.JasperPrintLP;


/**
 * <p> Diese Klasse kuemmert sich um den Druck der Saldenliste</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 15.06.05</p>
 *
 * <p>@author $Author: Gerold $</p>
 *
 * @version not attributable Date $Date: 2012/06/01 14:17:25 $
 */
public class ReportPeriodeBase extends PanelBasis implements PanelReportIfJRDS
{
	private static final long serialVersionUID = 1L;

	private WrapperLabel wlaGeschaeftsjahr = null;
	private WrapperComboBox wcoGeschaeftsjahr = null;
	private WrapperTextField wtfGeschaeftsjahr = null;
	private WrapperLabel wlaPeriode = null;
	private WrapperComboBox wcoPeriode = null;

	private final static int BREITE_SPALTE2 = 80;

	protected JPanel jpaWorkingOn = new JPanel();

	private PrintKontoblaetterModel model ;
	private GJComboboxModel gjModel ;
	
	public ReportPeriodeBase(InternalFrame internalFrame, String sAdd2Title, PrintKontoblaetterModel kbModel) throws Throwable {
		// reporttitel: das PanelReport kriegt einen Titel, der wird vom Framework hergenommen
		super(internalFrame, sAdd2Title);

		if(null == kbModel) throw new IllegalArgumentException("kbModel == null") ;		
		model = kbModel ;
		
		gjModel = new GJComboboxModel(model.getGeschaeftsjahr()) ;
				
		jbInit();
		initPanel();
		initComponents();
		
		invalidate() ;
	}

	protected void jbInit()
			throws Throwable {

		this.setLayout(new GridBagLayout());
		this.add(jpaWorkingOn,
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
						GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jpaWorkingOn.setLayout(new GridBagLayout());
		jpaWorkingOn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		wlaGeschaeftsjahr = new WrapperLabel("Gesch\u00E4ftsjahr");
		wcoGeschaeftsjahr = new WrapperComboBox();
		wtfGeschaeftsjahr = new WrapperTextField();
		wtfGeschaeftsjahr.setEditable(false);
		wlaPeriode = new WrapperLabel("Periode");
		wcoPeriode = new WrapperComboBox(gjModel);
		wcoPeriode.setMaximumRowCount(12) ;
		
		setDefaults();

		wlaGeschaeftsjahr.setPreferredSize(new Dimension(BREITE_SPALTE2,
				Defaults.getInstance().getControlHeight()));
		wlaPeriode.setPreferredSize(new Dimension(BREITE_SPALTE2,
				Defaults.getInstance().getControlHeight()));

		layoutGrid();
	}


	protected void layoutGrid() {
		iZeile++;
		jpaWorkingOn.add(wlaGeschaeftsjahr,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2),
						0, 0));
		jpaWorkingOn.add(wtfGeschaeftsjahr,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2),
						0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaPeriode,
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2),
						150, 0));
		jpaWorkingOn.add(wcoPeriode,
				new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2),
						150, 0));
		iZeile++;
	}

	private void initPanel() {
		wlaGeschaeftsjahr.setVisible(true);
		wcoGeschaeftsjahr.setVisible(false);
		wtfGeschaeftsjahr.setVisible(true);
		wlaPeriode.setVisible(true);
		wcoPeriode.setVisible(true);
	}

	private void setDefaults() {
		try {
			wtfGeschaeftsjahr.setText(model.getGeschaeftsjahrString());
			setSelectedPeriode() ;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


	public int getSelectedPeriode() {
		return gjModel.getSelectedPeriode() ;
	}
	
	private void setSelectedPeriode() {
		Calendar cal = Calendar.getInstance() ;
		wcoPeriode.setSelectedIndex(gjModel.getPeriodeForMonth(cal.get(Calendar.MONTH)))  ;	
	}
	
	public String getModul() {
		return FinanzReportFac.REPORT_MODUL;
	}


	public String getReportname() {
		return FinanzReportFac.REPORT_SALDENLISTE;
	}


	public boolean getBErstelleReportSofort() {
		return false;
	}


	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		model.setPeriodeImGJ(gjModel.getSelectedPeriode()) ;
		return DelegateFactory.getInstance().getFinanzReportDelegate().printKassabuch(model) ;
	}


	public MailtextDto getMailtextDto() throws Throwable  {
		MailtextDto mailtextDto=PanelReportKriterien.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	protected JComponent getFirstFocusableComponent()
			throws Exception {
		return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
	}
	
		
	public class GJComboboxModel extends AbstractListModel implements ComboBoxModel {
		private static final long serialVersionUID = 1L;
		private HashMap<Integer, String> months = new HashMap<Integer, String>() ;
		private Integer[] gjMonths = new Integer[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0} ;
		
		private Object selectedItem ;
		
		public GJComboboxModel() {
			months.put( 0, LPMain.getTextRespectUISPr("lp.januar")) ;
			months.put( 1, LPMain.getTextRespectUISPr("lp.februar")) ;
			months.put( 2, LPMain.getTextRespectUISPr("lp.maerz")) ;
			months.put( 3, LPMain.getTextRespectUISPr("lp.april")) ;
			months.put( 4, LPMain.getTextRespectUISPr("lp.mai")) ; 
			months.put( 5, LPMain.getTextRespectUISPr("lp.juni")) ;
			months.put( 6, LPMain.getTextRespectUISPr("lp.juli")) ;
			months.put( 7, LPMain.getTextRespectUISPr("lp.august")) ;
			months.put( 8, LPMain.getTextRespectUISPr("lp.september")) ;
			months.put( 9, LPMain.getTextRespectUISPr("lp.oktober")) ; 
			months.put(10, LPMain.getTextRespectUISPr("lp.november")) ;
			months.put(11, LPMain.getTextRespectUISPr("lp.dezember")) ;		
			
			setupStartMonth(0) ;
		}
		
		public GJComboboxModel(Integer geschaeftsjahr) {
			this() ;
			setGeschaeftsjahr(geschaeftsjahr) ;
		}

		public void setGeschaeftsjahr(int geschaeftsjahr) {
			try {
				Timestamp[] tVonBis = DelegateFactory.getInstance().getBuchenDelegate().getDatumVonBisGeschaeftsjahr(geschaeftsjahr);
 				Calendar cal = Calendar.getInstance();
				cal.setTime(tVonBis[0]);
				int monatvon = cal.get(Calendar.MONTH) ;

				setupStartMonth(monatvon);
			} catch (Throwable e) {
				setupStartMonth(0) ;
			}						
		}
		
		public Integer getPeriodeForMonth(int month0) {
			for(int i = 0 ; i < gjMonths.length; i++) {
				if(gjMonths[i] == month0) return i ;
			}

			throw new IllegalArgumentException("Month '" + month0 + "' unknown") ;
		}

		
		/**
		 * Die ausgew&auml;hlte Periode im Bereich 1 - 12. Wurde nichts ausgew&auml;hlt, dann 0
		 * @return Die ausgew&auml;hlte Periode im Bereich 1 - 12. Wurde nichts ausgew&auml;hlt, dann 0.
		 */
		public Integer getSelectedPeriode() {
			Iterator<Entry<Integer, String>> iterator = months.entrySet().iterator() ;
			while(iterator.hasNext()) {
				Entry<Integer, String> entry = iterator.next() ;
				if(entry.getValue().equals(selectedItem)) {
					return 1 + entry.getKey() ;
				}
			}

			return 0 ;
		}
		
		public String getSelectedPeriodeName() {
			return (String) selectedItem ;
		}

		protected void setupStartMonth(int monatvon) {
			for(int i = 0 ; i < 12; i++) {
				gjMonths[i] = new Integer((monatvon + i) % 12) ;
			}
		}

		@Override
		public Object getElementAt(int index) {
			if(index < 0 || index > gjMonths.length - 1) return null ;
			return months.get(gjMonths[index]);
		}

		@Override
		public int getSize() {
			return gjMonths.length ;
		}

		@Override
		public Object getSelectedItem() {
			return selectedItem ;
		}

		@Override
		public void setSelectedItem(Object anItem) {
			selectedItem = anItem ;
		}		
	}
}
