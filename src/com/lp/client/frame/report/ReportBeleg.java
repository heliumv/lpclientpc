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
package com.lp.client.frame.report;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MailtextDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p><I>Diese Klasse kuemmert sich ...</I> </p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 *
 * <p>Erstellungsdatum <I>dd.mm.05</I></p>
 *
 * <p> </p>
 *
 * @author Martin Bluehweis
 * @version $Revision: 1.5 $
 */
public abstract class ReportBeleg extends PanelBasis implements
		PanelReportIfJRDS, IAktiviereBelegReport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JPanel jpaWorkingOn = null;
	private WrapperLabel wlaKopien = null;
	protected WrapperNumberField wnfKopien = null;
	private String belegartCNr = null;
	private Integer iIdBeleg;
	private boolean bPrintLogo = false;

	private WrapperLabel wlaKostenstelle = null;
	private WrapperTextField wtfKostenstelleNummer = null;
	private WrapperTextField wtfKostenstelleBezeichnung = null;
	private final Integer kostenstelleIId;
	private PanelBasis panelToRefresh;

//	public ReportBeleg(InternalFrame internalFrame, String sTitle,
//			String belegartCNr, Integer iIdBeleg, Integer iIdKostenstelle)
//			throws Throwable {
//		this(internalFrame, null, sTitle, belegartCNr, iIdBeleg,
//				iIdKostenstelle);
//	}

	public ReportBeleg(InternalFrame internalFrame, PanelBasis panelToRefresh,
			String sTitle, String belegartCNr, Integer iIdBeleg,
			Integer iIdKostenstelle) throws Throwable {
		super(internalFrame, sTitle);
		this.panelToRefresh = panelToRefresh;
		this.belegartCNr = belegartCNr;
		this.iIdBeleg = iIdBeleg;
		this.kostenstelleIId = iIdKostenstelle;
		jbInit();
		setDefaults();
		initComponents();
	}

	private void jbInit() throws Throwable {
		jpaWorkingOn = new JPanel(new GridBagLayout());
		wlaKopien = new WrapperLabel(LPMain.getTextRespectUISPr(
				"report.kopien"));
		wlaKopien.setMinimumSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wlaKopien.setPreferredSize(new Dimension(100, Defaults.getInstance()
				.getControlHeight()));
		wnfKopien = new WrapperNumberField();
		wnfKopien.setMinimumSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wnfKopien.setPreferredSize(new Dimension(30, Defaults.getInstance()
				.getControlHeight()));
		wnfKopien.setFractionDigits(0);
		wnfKopien.setMaximumIntegerDigits(2);

		wlaKostenstelle = new WrapperLabel(LPMain.getTextRespectUISPr("label.kostenstelle"));
		wlaKostenstelle.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wlaKostenstelle.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wtfKostenstelleNummer = new WrapperTextField();
		wtfKostenstelleNummer.setMinimumSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wtfKostenstelleNummer.setPreferredSize(new Dimension(100, Defaults
				.getInstance().getControlHeight()));
		wtfKostenstelleNummer.setEditable(false);
		wtfKostenstelleBezeichnung = new WrapperTextField();
		wtfKostenstelleBezeichnung.setEditable(false);

		this.setLayout(new GridBagLayout());
		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		jpaWorkingOn.add(wlaKopien, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfKopien, new GridBagConstraints(1, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaKostenstelle, new GridBagConstraints(2, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleNummer, new GridBagConstraints(3,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKostenstelleBezeichnung, new GridBagConstraints(4,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;

	}

	private void setDefaults() throws Throwable {
		wnfKopien.setInteger(new Integer(0));
		// Kostenstelle anzeigen
		if (getKostenstelleIId() != null) {
			KostenstelleDto kstDto = DelegateFactory.getInstance()
					.getSystemDelegate()
					.kostenstelleFindByPrimaryKey(getKostenstelleIId());
			wtfKostenstelleNummer.setText(kstDto.getCNr());
			wtfKostenstelleBezeichnung.setText(kstDto.getCBez());
		}
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public Integer getIIdBeleg() {
		return iIdBeleg;
	}

	protected boolean isBPrintLogo() {
		return bPrintLogo;
	}

	public void setBPrintLogo(boolean bPrintLogo) {
		this.bPrintLogo = bPrintLogo;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wnfKopien;
	}

	public boolean getBErstelleReportSofort() {
		return false;
	}

	public MailtextDto getMailtextDto() throws Throwable {
		return null;
	}

	public final Integer getKostenstelleIId() {
		return kostenstelleIId;
	}
	
	@Override
	protected abstract String getLockMeWer() throws Exception;

	@Override
	public Object getKeyWhenDetailPanel() {
		return getIIdBeleg() + "";
	}
	
	@Override
	public void aktiviereBeleg(Timestamp t) throws Throwable {
		if(!isNotLocked())
			throw new ExceptionLP(EJBExceptionLP.FEHLER_IS_ALREADY_LOCKED, new Throwable("Beleg ist gesperrt"));
		aktiviereBelegImpl(t);
	}
	
	@Override
	public void refreshPanelInBackground() throws Throwable {
		if (panelToRefresh != null)
			panelToRefresh.eventYouAreSelected(false);
	}
	
	@Override
	protected void eventActionEscape(ActionEvent e) throws Throwable {
		eventActionUnlock(null);
		super.eventActionEscape(e);
	}
	@Override
	public Timestamp berechneBeleg() throws Throwable {
		return berechneBelegImpl();
	}

	protected abstract Timestamp berechneBelegImpl() throws Throwable;
	protected abstract void aktiviereBelegImpl(Timestamp t) throws Throwable;
}
