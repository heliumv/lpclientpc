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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperComboBoxPeriode;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.IPanelReportAction;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.pc.LPMain;
import com.lp.server.finanz.service.FibuFehlerDto;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzamtDto;
import com.lp.server.finanz.service.ReportUvaKriterienDto;
import com.lp.server.finanz.service.UvaverprobungDto;
import com.lp.server.system.ejb.Parametermandant;
import com.lp.server.system.service.GeschaeftsjahrMandantDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.HelperServer;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;


/**
 * <p> Diese Klasse kuemmert sich um den Druck der Saldenliste</p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellung: Martin Bluehweis; 15.06.05</p>
 *
 * <p>@author $Author: adi $</p>
 *
 * @version not attributable Date $Date: 2013/01/16 13:25:44 $
 */
public class ReportUva extends PanelBasis implements PanelReportIfJRDS, IPanelReportAction
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Border border1;

	private WrapperLabel wlaGeschaeftsjahr = null;
	private WrapperComboBox wcoGeschaeftsjahr = null;
	private WrapperTextField wtfGeschaeftsjahr = null;
	private WrapperLabel wlaPeriode = null;
	private WrapperComboBoxPeriode wcoPeriode = null;
	private WrapperLabel wlaFinanzamt = null;
	private WrapperComboBox wcoFinanzamt = null;
	private WrapperLabel wlaJaehrlich = null;
	private WrapperCheckBox wcbJaehrlich = null;
	private WrapperLabel wlaLetzteVerprobung = null;
	private WrapperLabel wlaVerprobung = null;
	private WrapperCheckBox wcbVerprobung = null;
	
	private FinanzamtDto[] faDtos = null;
	private String abrechnungszeitraum = null;
	private MandantDto mandantDto = null;
	private String geschaeftsjahr = null;
	
	private final static int BREITE_SPALTE2 = 80;

	protected JPanel jpaWorkingOn = new JPanel();

	private TabbedPaneKonten tbKonten;
	
public ReportUva(InternalFrame internalFrame, String sAdd2Title, String geschaeftsjahr, TabbedPaneKonten tbKonten) throws Throwable {
    // reporttitel: das PanelReport kriegt einen Titel, der wird vom Framework hergenommen
    super(internalFrame, sAdd2Title);
    this.geschaeftsjahr = geschaeftsjahr;
    this.tbKonten = tbKonten;
    jbInit();
    setDefaults();
    initPanel();
    initComponents();
//    this.setVisible(false);
  }

protected void jbInit()
throws Throwable {
    border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    this.setLayout(new GridBagLayout());
    this.add(jpaWorkingOn,
            new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jpaWorkingOn.setLayout(new GridBagLayout());
    jpaWorkingOn.setBorder(border1);

	LPMain.getInstance();
	wlaGeschaeftsjahr = new WrapperLabel(LPMain.getTextRespectUISPr("label.geschaeftsjahr"));
	wcoGeschaeftsjahr = new WrapperComboBox();
	wtfGeschaeftsjahr = new WrapperTextField();
	wtfGeschaeftsjahr.setEditable(false);
	wtfGeschaeftsjahr.setSaveReportInformation(false);
	
    wlaPeriode = new WrapperLabel(LPMain.getTextRespectUISPr("label.periode"));
    abrechnungszeitraum = DelegateFactory.getInstance().getParameterDelegate()
    	.getMandantparameter(LPMain.getTheClient().getMandant(), 
    			ParameterFac.KATEGORIE_FINANZ, 
    			ParameterFac.PARAMETER_FINANZ_UVA_ABRECHNUNGSZEITRAUM).getCWert();
	wlaJaehrlich = new WrapperLabel(LPMain.getTextRespectUISPr("label.jaehrlich"));
    wcbJaehrlich = new WrapperCheckBox();
	wcbJaehrlich.setSelected(false);
	
	wcoPeriode = new WrapperComboBoxPeriode(geschaeftsjahr);
    if(abrechnungszeitraum.equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR)) {
    	wcbJaehrlich.setSelected(true);
    	wcbJaehrlich.setEnabled(false);
    }
    wlaVerprobung = new WrapperLabel(LPMain.getTextRespectUISPr("label.verprobung"));
    wcbVerprobung = new WrapperCheckBox();
    ParametermandantDto pm = (ParametermandantDto) DelegateFactory.getInstance().getParameterDelegate().getParametermandant(
    		ParameterFac.PARAMETER_FINANZ_SAMMELBUCHUNG_MANUELL, 
    		ParameterFac.KATEGORIE_FINANZ,
    		LPMain.getTheClient().getMandant());
    if (pm.getCWert().equals("1"))
        wcbVerprobung.setSelected(false);
    else    	
    	wcbVerprobung.setSelected(true);
    
    wlaLetzteVerprobung = new WrapperLabel(LPMain.getTextRespectUISPr("label.letzteverprobung"));
	wlaFinanzamt = new WrapperLabel(LPMain.getTextRespectUISPr("label.finanzamt"));
	wcoFinanzamt = new WrapperComboBox();
	faDtos = DelegateFactory.getInstance().getFinanzDelegate().finanzamtFindAllByMandantCNr(LPMain.getTheClient());
	if (faDtos != null) {
		for (int i=0; i<faDtos.length; i++) {
			wcoFinanzamt.addItem(faDtos[i].getPartnerDto().formatName());
		}
	}
	
	wlaGeschaeftsjahr.setPreferredSize(new Dimension(BREITE_SPALTE2,
            Defaults.getInstance().getControlHeight()));
	wlaPeriode.setPreferredSize(new Dimension(BREITE_SPALTE2,
            Defaults.getInstance().getControlHeight()));
	wlaFinanzamt.setPreferredSize(new Dimension(BREITE_SPALTE2,
            Defaults.getInstance().getControlHeight()));
	
	wtfGeschaeftsjahr.setPreferredSize(new Dimension(BREITE_SPALTE2,
            Defaults.getInstance().getControlHeight()));
	wcoPeriode.setPreferredSize(new Dimension(BREITE_SPALTE2,
            Defaults.getInstance().getControlHeight()));
	wcoFinanzamt.setPreferredSize(new Dimension(BREITE_SPALTE2 * 2,
            Defaults.getInstance().getControlHeight()));

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
	jpaWorkingOn.add(wlaPeriode,
			new GridBagConstraints(2, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));
	jpaWorkingOn.add(wcoPeriode,
			new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					100, 0));
	jpaWorkingOn.add(wlaJaehrlich,
			new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					60, 0));
	jpaWorkingOn.add(wcbJaehrlich,
			new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					50, 0));
	iZeile++;
	jpaWorkingOn.add(wlaFinanzamt,
			new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));
	jpaWorkingOn.add(wcoFinanzamt,
			new GridBagConstraints(1, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));

	jpaWorkingOn.add(wlaLetzteVerprobung,
			new GridBagConstraints(2, iZeile, 2, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					150, 0));

	jpaWorkingOn.add(wlaVerprobung,
			new GridBagConstraints(4, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					0, 0));

	jpaWorkingOn.add(wcbVerprobung,
			new GridBagConstraints(5, iZeile, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2),
					50, 0));
	

//	this.setEinschraenkungDatumBelegnummerSichtbar(false);
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
		//wcoGeschaeftsjahr.setMap(DelegateFactory.getInstance().getSystemDelegate().getAllGeschaeftsjahr());
	    //wcoGeschaeftsjahr.setKeyOfSelectedItem(DelegateFactory.getInstance().getParameterDelegate().
        //        getGeschaeftsjahr());
	    wtfGeschaeftsjahr.setText(geschaeftsjahr);
	} catch (Throwable e) {
		e.printStackTrace();
	}
	
	try {
		mandantDto = DelegateFactory.getInstance().getMandantDelegate().mandantFindByPrimaryKey(LPMain.getTheClient().getMandant());
		if (mandantDto.getPartnerIIdFinanzamt() != null) {
			for (int i=0; i<faDtos.length; i++) {
				if (faDtos[i].getPartnerIId().equals(mandantDto.getPartnerIIdFinanzamt())) {
					wcoFinanzamt.setSelectedIndex(i);
					break;
				}
			}
		}
	} catch (ExceptionLP e) {
		e.printStackTrace();
	} catch (Throwable e) {
		e.printStackTrace();
	}
	
}


  public String getModul() {
    return FinanzReportFac.REPORT_MODUL;
  }


  public String getReportname() {
    return FinanzReportFac.REPORT_UVA;
  }


  public boolean getBErstelleReportSofort() {
    return false;
  }


  public JasperPrintLP getReport(String sDrucktype)      throws Throwable {
    return DelegateFactory.getInstance().getFinanzReportDelegate().printUva(this.getKriterien());
  }


  public MailtextDto getMailtextDto() throws Throwable  {
    MailtextDto mailtextDto=PanelReportKriterien.getDefaultMailtextDto(this);
    return mailtextDto;
  }

  protected JComponent getFirstFocusableComponent()
      throws Exception {
    return PanelBasis.NO_VALUE_THATS_OK_JCOMPONENT;
  }

  protected ReportUvaKriterienDto getKriterien() {
	    ReportUvaKriterienDto krit = new ReportUvaKriterienDto();
	    
	    krit.setIGeschaeftsjahr(Integer.parseInt(wtfGeschaeftsjahr.getText()));//wcoGeschaeftsjahr.getSelectedItem().toString()));
	    
	    krit.setIPeriode(wcoPeriode.getPeriode());
	    krit.setSPeriode(wcoPeriode.getSelectedItem().toString());
	    
	    krit.setFinanzamtIId(faDtos[wcoFinanzamt.getSelectedIndex()].getPartnerIId());
	    
	    if (wcbJaehrlich.isSelected()) {
	    	krit.setSAbrechnungszeitraum(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR);
	    	wcoPeriode.setEnabled(false);
	    } else {
	    	krit.setSAbrechnungszeitraum(abrechnungszeitraum);
	    	wcoPeriode.setEnabled(true);
	    }
	    updateLetzteVerprobung(faDtos[wcoFinanzamt.getSelectedIndex()].getPartnerIId());
	    return krit;
	  }

  private void updateLetzteVerprobung(Integer finanzamtIId) {
	  UvaverprobungDto uvap = null;
	  try {
		  uvap = DelegateFactory.getInstance().getFinanzServiceDelegate().letzteVerprobung(finanzamtIId);
	  } catch (Throwable e) {
		  //
	  }
	  if (uvap != null) {
		  wlaLetzteVerprobung.setText(LPMain.getTextRespectUISPr("label.letzteverprobung") + " " + uvap.toInfo());
	  } else {
		  wlaLetzteVerprobung.setText(LPMain.getTextRespectUISPr("label.letzteverprobung") + " ?");
	  }
  }
  

  @Override
  public void eventActionPrint(ActionEvent e) throws Throwable {
	  eventActionSave(e, false) ;
  }

  
  @Override
  public void eventActionSave(ActionEvent e) throws Throwable {
	  eventActionSave(e, false) ;
  }


  @Override
  public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI) throws Throwable {
	  // jetzt wenn erforderlich die Verprobung durchfuehren, da die UVA gedruckt wurde
	  ReportUvaKriterienDto krit = getKriterien();
	  ArrayList<FibuFehlerDto> fehler = new ArrayList<FibuFehlerDto>();
	  if (krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_QUARTAL)) {
		  Integer[] perioden = null;
		  perioden = HelperServer.getMonateFuerQuartal(krit.getIPeriode());
		  for (int i=0; i<perioden.length; i++) {
			  fehler.addAll(DelegateFactory.getInstance().getFinanzServiceDelegate()
					  .pruefeBelege(krit.getIGeschaeftsjahr(), perioden[i], true));
		  }
	  } else if(krit.getSAbrechnungszeitraum().equals(FinanzFac.UVA_ABRECHNUNGSZEITRAUM_JAHR)) {
		  throw new ExceptionLP(EJBExceptionLP.FEHLER_FINANZ_UVA_AUF_GANZES_JAHR_NICHT_ERLAUBT, new Throwable("UVA Verprobung f\u00FCr Jahr nicht m\u00F6glich!"));
	  } else {
		  // Monat
		  fehler = DelegateFactory.getInstance().getFinanzServiceDelegate()
				  .pruefeBelege(krit.getIGeschaeftsjahr(), krit.getIPeriode(), true);
	  }

	  if (fehler.size() > 0) {
		  DialogFactory.showBelegPruefergebnis(getInternalFrame(), fehler, LPMain.getTextRespectUISPr("fb.uvaverprobung.fehlgeschlagen"));
	  } else {
		  if (wcbVerprobung.isSelected()) {
			  Integer uvaverprobungIId = DelegateFactory.getInstance().getFinanzServiceDelegate().uvaVerprobung(krit);
			  if (uvaverprobungIId != null) {
				  myLogger.info("UVAverprobung " + uvaverprobungIId.intValue() );
				  tbKonten.updateUvaMenus();
			  } else {
				  GeschaeftsjahrMandantDto gj = DelegateFactory.getInstance().getSystemDelegate().geschaeftsjahrFindByPrimaryKey(krit.getIGeschaeftsjahr());
				  if (gj.getTSperre() != null)
					  DialogFactory.showMeldung(LPMain.getTextRespectUISPr("finanz.error.geschaeftsjahr.gesperrt"), 
							  LPMain.getTextRespectUISPr("fb.uvaverprobung.fehlgeschlagen"), JOptionPane.DEFAULT_OPTION );
			  }
		  }
	  }
  }

}
