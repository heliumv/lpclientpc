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
package com.lp.client.lieferschein;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.EventObject;

import javax.swing.SwingConstants;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperDateField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.frame.report.PanelReportKriterien;
import com.lp.client.frame.report.ReportEtikett;
import com.lp.client.pc.LPMain;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.lieferschein.service.LieferscheinReportFac;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.report.JasperPrintLP;

@SuppressWarnings("static-access")
/*
 * <p>Report Lieferschein Adressetikett. <p>Copyright Logistik Pur Software GmbH
 * (c) 2004-2008</p> <p>Erstellungsdatum 28.09.05</p> <p> </p>
 * 
 * @author Uli Walch
 * 
 * @version $Revision: 1.10 $
 */
public class ReportLieferscheinAdressetikett extends ReportEtikett implements
		PanelReportIfJRDS {

	private static final long serialVersionUID = 1L;
	private LieferscheinDto lieferscheinDto = null;
	private WrapperLabel wlaPaketnummer = null;
	protected WrapperNumberField wnfPaketnummer = null;
	private WrapperLabel wlaPaketanzahl = null;
	private WrapperCheckBox wcbAlle = null;
	
	
	private WrapperLabel wlaPakete = null;
	private WrapperNumberField wnfPakete = null;
	private WrapperLabel wlaLiefertermin = null;
	private WrapperDateField wdfLiefertermin = null;
	private WrapperLabel wlaGewicht = null;
	private WrapperNumberField wnfGewicht = null;
	private WrapperLabel wlaEinheit = null;
	private WrapperLabel wlaVersandnummer = null;
	private WrapperTextField wtfVersandnummer = null;
	


	public ReportLieferscheinAdressetikett(InternalFrame internalFrame,
			LieferscheinDto lieferscheinDto, String sAdd2Title)
			throws Throwable {
		super(internalFrame, sAdd2Title);
		this.lieferscheinDto = lieferscheinDto;
		jbInit();
		setDefaults();
	}

	private void setDefaults() {
		wnfPaketnummer.setInteger(null);
		wcbAlle.setSelected(true);
	}

	public String getModul() {
		return LieferscheinReportFac.REPORT_MODUL;
	}

	public String getReportname() {
		return LieferscheinReportFac.REPORT_LIEFERSCHEIN_ETIKETT;
	}

	public JasperPrintLP getReport(String sDrucktype) throws Throwable {
		Integer iPaketnummer;
		if (wcbAlle.isSelected()) {
			iPaketnummer = null;
		} else {
			iPaketnummer = wnfPaketnummer.getInteger();
		}
		return DelegateFactory.getInstance().getLieferscheinReportDelegate()
				.printLieferscheinetikett(lieferscheinDto.getIId(),
						iPaketnummer);
	}

	public boolean getBErstelleReportSofort() {
		return true;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		MailtextDto mailtextDto = PanelReportKriterien
				.getDefaultMailtextDto(this);
		return mailtextDto;
	}

	private void jbInit() throws Throwable {
		
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DISCARD };
		
		wlaPaketnummer = new WrapperLabel();
		wlaPaketnummer.setText(LPMain.getInstance().getTextRespectUISPr(
				"label.paketnummer"));
		wlaPaketnummer.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaPaketnummer.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wnfPaketnummer = new WrapperNumberField();
		wnfPaketnummer.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wnfPaketnummer.setPreferredSize(new Dimension(30, Defaults
				.getInstance().getControlHeight()));
		wnfPaketnummer.setFractionDigits(0);
		wnfPaketnummer.setMaximumIntegerDigits(2);
		wlaPaketanzahl = new WrapperLabel();
		wlaPaketanzahl.setHorizontalAlignment(SwingConstants.LEFT);
		wlaPaketanzahl.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.von")
				+ " " + lieferscheinDto.getIAnzahlPakete());
		wcbAlle = new WrapperCheckBox();
		wcbAlle.setText(LPMain.getInstance().getTextRespectUISPr("lp.alle"));
		
	    wlaPakete = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "ls.pakete"));
	    wnfPakete = new WrapperNumberField();
	    wnfPakete.setFractionDigits(0);
	    wnfPakete.setMinimumValue(0);
	    wlaLiefertermin = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "lp.auslieferdatum"));

	    Date datCurrentDate = new Date(System.currentTimeMillis());

	    wdfLiefertermin = new WrapperDateField();
	    wdfLiefertermin.setMandatoryFieldDB(false);
	    wdfLiefertermin.setShowRubber(false);
	//    wdfLiefertermin.getDisplay().addPropertyChangeListener(this);
	      
	    wlaGewicht = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
	        "lp.gewicht"));
	    wnfGewicht = new WrapperNumberField();
	    wlaEinheit = new WrapperLabel(SystemFac.EINHEIT_KILOGRAMM.trim());
	    wlaEinheit.setHorizontalAlignment(SwingConstants.LEFT);
	    HelperClient.setDefaultsToComponent(wlaEinheit, 25);


	    wlaVersandnummer = new WrapperLabel(LPMain.getInstance().getTextRespectUISPr(
        "ls.versandnummer"));
	    wtfVersandnummer = new WrapperTextField();

		iZeile++;
		jpaWorkingOn.add(wlaVersandnummer, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfVersandnummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaGewicht, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfGewicht, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaLiefertermin, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wdfLiefertermin, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		iZeile++;
		jpaWorkingOn.add(wlaPakete, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPakete, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaPaketnummer, new GridBagConstraints(0, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfPaketnummer, new GridBagConstraints(1, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaPaketanzahl, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
	
		jpaWorkingOn.add(wcbAlle, new GridBagConstraints(3, iZeile, 1, 1, 8.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	   	enableToolsPanelButtons(aWhichButtonIUse);
		getInternalFrame().addItemChangedListener(this);

		this.add(getToolsPanel(), new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
	}
	

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		super.eventActionSave(e, true);
		components2Dto();
		if (lieferscheinDto != null)
			DelegateFactory.getInstance().getLsDelegate().updateLieferscheinOhneWeitereAktion(lieferscheinDto);
		
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		 if (lieferscheinDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_ANGELEGT) ||
		          lieferscheinDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_OFFEN) ||
				          lieferscheinDto.getStatusCNr().equals(LieferscheinFac.LSSTATUS_GELIEFERT)  ) {
			 super.eventActionUpdate(aE, false); // Buttons schalten
		} 
			 else {
			      MessageFormat mf = new MessageFormat(
			          LPMain.getInstance().getTextRespectUISPr("ls.warning.lskannnichtgeaendertwerden"));
			      mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());
			      Object pattern[] = {
			    		  lieferscheinDto.getStatusCNr()
			      };
			      DialogFactory.showModalDialog(
			          LPMain.getInstance().getTextRespectUISPr("lp.warning"),
			          mf.format(pattern));
			      }
	}
	
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
	throws Throwable {
		super.eventYouAreSelected(false);
		setDefaults();
		dto2Components();
	}
	
	
	public void updateButtons()
		throws Throwable {
		super.updateButtons();
	}
	
	public void updateButtons(boolean bHatVersandRecht)
		throws Throwable {
		super.updateButtons(bHatVersandRecht);
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
	}
	
	protected void dto2Components() throws Throwable {
		wnfGewicht.setDouble(lieferscheinDto.getFGewichtLieferung());
		if (lieferscheinDto.getTLiefertermin() != null) {
		wdfLiefertermin.setDate(lieferscheinDto.getTLiefertermin());
		}
		wnfPakete.setInteger(lieferscheinDto.getIAnzahlPakete());
		wtfVersandnummer.setText(lieferscheinDto.getCVersandnummer());
	}
	
	protected void components2Dto() throws Throwable {
		if(wnfGewicht.getDouble() != null)
			lieferscheinDto.setFGewichtLieferung(wnfGewicht.getDouble());
		lieferscheinDto.setTLiefertermin(wdfLiefertermin.getTimestamp());
		lieferscheinDto.setIAnzahlPakete(wnfPakete.getInteger());
		lieferscheinDto.setCVersandnummer(wtfVersandnummer.getText());
		
	}
	
	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_LIEFERSCHEIN;
	}
}
