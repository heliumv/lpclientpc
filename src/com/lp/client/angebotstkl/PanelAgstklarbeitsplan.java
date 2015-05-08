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
package com.lp.client.angebotstkl;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.EventObject;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lp.client.artikel.ArtikelFilterFactory;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperCheckBox;
import com.lp.client.frame.component.WrapperComboBox;
import com.lp.client.frame.component.WrapperEditorField;
import com.lp.client.frame.component.WrapperEditorFieldTexteingabe;
import com.lp.client.frame.component.WrapperIdentField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperSpinner;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.zeiterfassung.ZeiterfassungFilterFactory;
import com.lp.server.angebotstkl.service.AgstklarbeitsplanDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.personal.service.MaschineDto;
import com.lp.server.stueckliste.service.StuecklisteFac;
import com.lp.server.stueckliste.service.StuecklistepositionDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
public class PanelAgstklarbeitsplan extends PanelBasis {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// von hier ...
	private GridBagLayout gridBagLayoutAll = null;
	private JPanel jpaWorkingOn = new JPanel();
	private JPanel jpaButtonAction = null;
	private GridBagLayout gridBagLayoutWorkingPanel = null;
	private InternalFrameAngebotstkl internalFrameStueckliste = null;
	private WrapperLabel wlaStueckzeit = new WrapperLabel();
	private WrapperSpinner wspStueckzeitStunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(50000), new Integer(1));
	private WrapperSpinner wspStueckzeitMinuten = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));
	private WrapperSpinner wspStueckzeitSekunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));
	private WrapperLabel wlaRuestzeit = new WrapperLabel();
	private WrapperSpinner wspRuestzeitStunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(50000), new Integer(1));
	private WrapperSpinner wspRuestzeitMinuten = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));
	private WrapperSpinner wspRuestzeitSekunden = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(59), new Integer(1));

	private WrapperLabel wlaKommentar = new WrapperLabel();
	private WrapperTextField wtfKommentar = new WrapperTextField();
	private WrapperLabel wlaArbeitsgang = new WrapperLabel();
	private WrapperNumberField wnfArbeitsgang = new WrapperNumberField();

	private WrapperNumberField wnfUnterarbeitsgang = new WrapperNumberField();

	private WrapperLabel wlaLangtext = new WrapperLabel();

	private WrapperLabel wlaRuestzeitUmgewandelt = new WrapperLabel();
	private WrapperLabel wlaStueckzeitUmgewandelt = new WrapperLabel();

	private WrapperLabel wlaLossgroesse = new WrapperLabel();

	private WrapperComboBox wcoAgart = new WrapperComboBox();
	private WrapperNumberField wnfAufspannung = new WrapperNumberField();
	// private WrapperSpinner wspTageStueckzeit = new WrapperSpinner(new
	// Integer(0),
	// new Integer(0),
	// new Integer(99), new Integer(1));

	boolean bTheoretischeIstZeit = false;

	int iAnzeigeArbeitszeit = 0;

	private WrapperSpinner wspMillisekundenStueckzeit = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(999), new Integer(1));

	// private WrapperSpinner wspTageRuestzeit = new WrapperSpinner(new
	// Integer(0),
	// new Integer(0),
	// new Integer(99), new Integer(1));
	private WrapperSpinner wspMillisekundenRuestzeit = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(999), new Integer(1));

	private WrapperSpinner wspTageRuestzeit = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(999), new Integer(1));
	private WrapperSpinner wspTageStueckzeit = new WrapperSpinner(
			new Integer(0), new Integer(0), new Integer(999), new Integer(1));

	private AgstklarbeitsplanDto agstklarbeitsplanDto = null;
	private WrapperCheckBox wcbNurMaschinenzeit = new WrapperCheckBox();
	private WrapperEditorField wefLangtext = null;

	static final public String ACTION_SPECIAL_MASCHINE_FROM_LISTE = "action_maschine_from_liste";

	private PanelQueryFLR panelQueryFLRMaschine = null;
	private WrapperIdentField wifArtikel = null;

	private WrapperButton wbuMaschine = new WrapperButton();
	private WrapperTextField wtfMaschine = new WrapperTextField();

	public PanelAgstklarbeitsplan(InternalFrame internalFrame,
			String add2TitleI, Object pk) throws Throwable {
		super(internalFrame, add2TitleI, pk);
		internalFrameStueckliste = (InternalFrameAngebotstkl) internalFrame;
		jbInit();
		setDefaults();
		initComponents();
		enableAllComponents(this, false);
	}

	private void setDefaults() throws Throwable {
		Map<String, String> m = new TreeMap<String, String>();
		m.put(StuecklisteFac.AGART_LAUFZEIT, StuecklisteFac.AGART_LAUFZEIT);
		m.put(StuecklisteFac.AGART_UMSPANNZEIT,
				StuecklisteFac.AGART_UMSPANNZEIT);
		wcoAgart.setMap(m);
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wifArtikel.getWtfIdent();
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, true, false);
		agstklarbeitsplanDto = new AgstklarbeitsplanDto();
		leereAlleFelder(this);
		wifArtikel.setArtikelDto(null);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);

		if (bTheoretischeIstZeit == true) {
			if (wcoAgart.getKeyOfSelectedItem() == null) {
				wspStueckzeitMinuten.setEnabled(false);
				wspStueckzeitSekunden.setEnabled(false);
				wspStueckzeitStunden.setEnabled(false);
				wspMillisekundenStueckzeit.setEnabled(false);

				wspStueckzeitMinuten.setValue(0);
				wspStueckzeitSekunden.setValue(0);
				wspStueckzeitStunden.setValue(0);
				wspMillisekundenStueckzeit.setValue(0);

			} else {
				wspRuestzeitMinuten.setEnabled(false);
				wspRuestzeitSekunden.setEnabled(false);
				wspRuestzeitStunden.setEnabled(false);
				wspMillisekundenRuestzeit.setEnabled(false);

				wspRuestzeitMinuten.setValue(0);
				wspRuestzeitSekunden.setValue(0);
				wspRuestzeitStunden.setValue(0);
				wspMillisekundenRuestzeit.setValue(0);
			}
		}

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_MASCHINE_FROM_LISTE)) {
			dialogQueryMaschineFromListe(e);
		}
		if (e.getSource().equals(wcoAgart)) {
			if (getLockedstateDetailMainKey() != null
					&& getLockedstateDetailMainKey().getIState() == PanelBasis.LOCK_IS_LOCKED_BY_ME)

				if (bTheoretischeIstZeit == true) {
					if (wcoAgart.getKeyOfSelectedItem() == null) {
						wspStueckzeitMinuten.setEnabled(false);
						wspStueckzeitSekunden.setEnabled(false);
						wspStueckzeitStunden.setEnabled(false);
						wspMillisekundenStueckzeit.setEnabled(false);
						wspStueckzeitMinuten.setValue(0);
						wspStueckzeitSekunden.setValue(0);
						wspStueckzeitStunden.setValue(0);
						wspMillisekundenStueckzeit.setValue(0);

						wspRuestzeitMinuten.setEnabled(true);
						wspRuestzeitSekunden.setEnabled(true);
						wspRuestzeitStunden.setEnabled(true);
						wspMillisekundenRuestzeit.setEnabled(true);

					} else {
						wspRuestzeitMinuten.setEnabled(false);
						wspRuestzeitSekunden.setEnabled(false);
						wspRuestzeitStunden.setEnabled(false);
						wspMillisekundenRuestzeit.setEnabled(false);

						wspRuestzeitMinuten.setValue(0);
						wspRuestzeitSekunden.setValue(0);
						wspRuestzeitStunden.setValue(0);
						wspMillisekundenRuestzeit.setValue(0);

						wspStueckzeitMinuten.setEnabled(true);
						wspStueckzeitSekunden.setEnabled(true);
						wspStueckzeitStunden.setEnabled(true);
						wspMillisekundenStueckzeit.setEnabled(true);
					}
				}
		}
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		Object[] o = internalFrameStueckliste.getTabbedPaneAngebotstkl()
				.getAngebotstklArbeitsplanTop().getSelectedIds();
		if (o != null) {
			for (int i = 0; i < o.length; i++) {
				AgstklarbeitsplanDto toRemove = new AgstklarbeitsplanDto();
				toRemove.setIId((Integer) o[i]);
				toRemove.setAgstklIId(agstklarbeitsplanDto.getAgstklIId());
				DelegateFactory.getInstance().getAngebotstklDelegate()
						.removeAgstklarbeitsplan(toRemove);

			}
		}
		this.setKeyWhenDetailPanel(null);
		super.eventActionDelete(e, false, false);
	}

	protected void components2Dto() throws Throwable {
		agstklarbeitsplanDto.setAgstklIId(internalFrameStueckliste
				.getAgstklDto().getIId());
		long rTage = wspTageRuestzeit.getInteger().longValue() * (3600000 * 24);
		long rStunden = wspRuestzeitStunden.getInteger().longValue() * 3600000;
		long rMinuten = wspRuestzeitMinuten.getInteger().longValue() * 60000;
		long rSekunden = wspRuestzeitSekunden.getInteger().longValue() * 1000;
		long rMsekunden = wspMillisekundenRuestzeit.getInteger().longValue();
		long lRuestzeit = rTage + rStunden + rMinuten + rSekunden + rMsekunden;

		long sTage = wspTageStueckzeit.getInteger().longValue()
				* (3600000 * 24);
		long sStunden = wspStueckzeitStunden.getInteger().longValue() * 3600000;
		long sMinuten = wspStueckzeitMinuten.getInteger().longValue() * 60000;
		long sSekunden = wspStueckzeitSekunden.getInteger().longValue() * 1000;
		long sMsekunden = wspMillisekundenStueckzeit.getInteger().longValue();
		long lStueckzeit = sTage + sStunden + sMinuten + sSekunden + sMsekunden;

		agstklarbeitsplanDto.setLRuestzeit(new Long(lRuestzeit));
		agstklarbeitsplanDto.setLStueckzeit(new Long(lStueckzeit));

		agstklarbeitsplanDto.setIArbeitsgang(wnfArbeitsgang.getInteger());
		agstklarbeitsplanDto.setIUnterarbeitsgang(wnfUnterarbeitsgang
				.getInteger());
		agstklarbeitsplanDto.setCKommentar(wtfKommentar.getText());

		agstklarbeitsplanDto.setXLangtext(wefLangtext.getText());
		agstklarbeitsplanDto.setArtikelIId(wifArtikel.getArtikelDto().getIId());
		agstklarbeitsplanDto.setBNurmaschinenzeit(wcbNurMaschinenzeit
				.getShort());
		agstklarbeitsplanDto.setIAufspannung(wnfAufspannung.getInteger());
		agstklarbeitsplanDto.setAgartCNr((String) wcoAgart
				.getKeyOfSelectedItem());

	}

	protected void dto2Components() throws Throwable {

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_STUECKLISTE_ARBEITSPLAN_ZEITEINHEIT,
						ParameterFac.KATEGORIE_STUECKLISTE,
						LPMain.getInstance().getTheClient().getMandant());
		String sEinheit = parameter.getCWert().trim();

		double lRuestzeit = agstklarbeitsplanDto.getLRuestzeit().longValue();
		double lStueckzeit = agstklarbeitsplanDto.getLStueckzeit().longValue();

		double dRuestzeit = 0;
		double dStueckzeit = 0;

		if (sEinheit.equals(SystemFac.EINHEIT_STUNDE.trim())) {
			dStueckzeit = lStueckzeit / 3600000;
			dRuestzeit = lRuestzeit / 3600000;
		} else if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
			dStueckzeit = lStueckzeit / 60000;
			dRuestzeit = lRuestzeit / 60000;
		} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
			dStueckzeit = lStueckzeit / 1000;
			dRuestzeit = lRuestzeit / 1000;
		}

		if (agstklarbeitsplanDto.getMaschineIId() != null) {
			MaschineDto maschineDto = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.maschineFindByPrimaryKey(
							agstklarbeitsplanDto.getMaschineIId());
			wtfMaschine.setText(maschineDto.getBezeichnung());
		} else {
			wtfMaschine.setText("");
		}

		// Maschine+Personalkosten
		BigDecimal kostenR = new BigDecimal(0.00);
		BigDecimal kostenSTK = new BigDecimal(0.00);

		if (Helper.short2Boolean(agstklarbeitsplanDto.getBNurmaschinenzeit()) == false) {
			BigDecimal preis = DelegateFactory
					.getInstance()
					.getLagerDelegate()
					.getGemittelterGestehungspreisAllerLaegerEinesMandanten(
							agstklarbeitsplanDto.getArtikelIId());

			if (preis == null) {
				preis = new BigDecimal(0);
			}

			if (agstklarbeitsplanDto.getIAufspannung() != null
					&& agstklarbeitsplanDto.getIAufspannung() >= 1) {
				preis = preis.divide(
						new BigDecimal(agstklarbeitsplanDto.getIAufspannung()),
						4, BigDecimal.ROUND_HALF_EVEN);
			}
			kostenR = kostenR.add(preis.multiply(Helper.rundeKaufmaennisch(
					new BigDecimal(dRuestzeit), 2)));
			kostenSTK = kostenSTK.add(Helper.rundeKaufmaennisch(
					preis.multiply(new BigDecimal(dStueckzeit)), 2));

		}

		if (agstklarbeitsplanDto.getMaschineIId() != null) {
			BigDecimal bdKosten = DelegateFactory
					.getInstance()
					.getZeiterfassungDelegate()
					.getMaschinenKostenZumZeitpunkt(
							agstklarbeitsplanDto.getMaschineIId());

			if (agstklarbeitsplanDto.getIAufspannung() != null
					&& agstklarbeitsplanDto.getIAufspannung() >= 1) {

				bdKosten = bdKosten.divide(
						new BigDecimal(agstklarbeitsplanDto.getIAufspannung()),
						4, BigDecimal.ROUND_HALF_EVEN);
			}

			if (bdKosten == null) {
				bdKosten = new BigDecimal(0);
			}
			kostenR = kostenR.add(bdKosten.multiply(Helper.rundeKaufmaennisch(
					new BigDecimal(dRuestzeit), 2)));
			kostenSTK = kostenSTK.add(bdKosten.multiply(Helper
					.rundeKaufmaennisch(new BigDecimal(dStueckzeit), 2)));

		}

		// SP2760
		if (sEinheit.equals(SystemFac.EINHEIT_MINUTE.trim())) {
			kostenR = kostenR.divide(new BigDecimal(60),
					BigDecimal.ROUND_HALF_UP);
			kostenSTK = kostenSTK.divide(new BigDecimal(60),
					BigDecimal.ROUND_HALF_UP);
		} else if (sEinheit.equals(SystemFac.EINHEIT_SEKUNDE.trim())) {
			kostenR = kostenR.divide(new BigDecimal(3600),
					BigDecimal.ROUND_HALF_UP);
			kostenSTK = kostenSTK.divide(new BigDecimal(3600),
					BigDecimal.ROUND_HALF_UP);
		}

		wlaRuestzeitUmgewandelt.setText("= "
				+ Helper.rundeKaufmaennisch(new BigDecimal(dRuestzeit), 2)
				+ " "
				+ sEinheit
				+ "/ "
				+ Helper.formatZahl(kostenR, 2, LPMain.getTheClient()
						.getLocUi()) + " "
				+ LPMain.getTheClient().getSMandantenwaehrung());
		wlaStueckzeitUmgewandelt.setText("= "
				+ Helper.rundeKaufmaennisch(new BigDecimal(dStueckzeit), 2)
				+ " "
				+ sEinheit
				+ "/ "
				+ Helper.formatZahl(kostenSTK, 2, LPMain.getTheClient()
						.getLocUi()) + " "
				+ LPMain.getTheClient().getSMandantenwaehrung());

		ArtikelDto artikelDto = DelegateFactory.getInstance()
				.getArtikelDelegate()
				.artikelFindByPrimaryKey(agstklarbeitsplanDto.getArtikelIId());

		wifArtikel.setArtikelDto(artikelDto);
		wtfKommentar.setText(agstklarbeitsplanDto.getCKommentar());

		dRuestzeit = (agstklarbeitsplanDto.getLRuestzeit().longValue());
		if (dRuestzeit > 0) {

			if (iAnzeigeArbeitszeit != 0) {
				double rTage = dRuestzeit / (3600000 * 24);
				if (rTage != 0) {
					dRuestzeit = dRuestzeit % (3600000 * 24);
				}
				wspTageRuestzeit.setInteger(new Integer((int) rTage));
			} else {
				wspTageRuestzeit.setInteger(0);
			}
			double rStunden = dRuestzeit / 3600000;
			if (rStunden != 0) {
				dRuestzeit = dRuestzeit % 3600000;
			}
			double rMinuten = dRuestzeit / 60000;
			if (rMinuten != 0) {
				dRuestzeit = dRuestzeit % 60000;
			}
			double rSekunden = dRuestzeit / 1000;
			if (rSekunden != 0) {
				dRuestzeit = dRuestzeit % 1000;
			}
			double rMsekunden = dRuestzeit;

			wspRuestzeitStunden.setInteger(new Integer((int) rStunden));
			wspRuestzeitMinuten.setInteger(new Integer((int) rMinuten));
			wspRuestzeitSekunden.setInteger(new Integer((int) rSekunden));
			wspMillisekundenRuestzeit.setInteger(new Integer((int) rMsekunden));
		} else {
			wspTageRuestzeit.setInteger(new Integer(0));
			wspRuestzeitStunden.setInteger(new Integer(0));
			wspRuestzeitMinuten.setInteger(new Integer(0));
			wspRuestzeitSekunden.setInteger(new Integer(0));
			wspMillisekundenRuestzeit.setInteger(new Integer(0));

		}
		dStueckzeit = (agstklarbeitsplanDto.getLStueckzeit().longValue());
		if (dStueckzeit > 0) {
			if (iAnzeigeArbeitszeit != 0) {
				double sTage = dStueckzeit / (3600000 * 24);
				if (sTage != 0) {
					dStueckzeit = dStueckzeit % (3600000 * 24);
				}
				wspTageStueckzeit.setInteger(new Integer((int) sTage));
			} else {
				wspTageStueckzeit.setInteger(0);
			}
			double sStunden = dStueckzeit / 3600000;
			if (sStunden != 0) {
				dStueckzeit = dStueckzeit % 3600000;
			}
			double sMinuten = dStueckzeit / 60000;
			if (sMinuten != 0) {
				dStueckzeit = dStueckzeit % 60000;
			}
			double sSekunden = dStueckzeit / 1000;
			if (sSekunden != 0) {
				dStueckzeit = dStueckzeit % 1000;
			}
			double sMsekunden = dStueckzeit;

			wspStueckzeitStunden.setInteger(new Integer((int) sStunden));
			wspStueckzeitMinuten.setInteger(new Integer((int) sMinuten));
			wspStueckzeitSekunden.setInteger(new Integer((int) sSekunden));
			wspMillisekundenStueckzeit
					.setInteger(new Integer((int) sMsekunden));
		} else {
			wspTageStueckzeit.setInteger(new Integer(0));
			wspStueckzeitStunden.setInteger(new Integer(0));
			wspStueckzeitMinuten.setInteger(new Integer(0));
			wspStueckzeitSekunden.setInteger(new Integer(0));
			wspMillisekundenStueckzeit.setInteger(new Integer(0));

		}

		wnfArbeitsgang.setInteger(agstklarbeitsplanDto.getIArbeitsgang());
		wnfUnterarbeitsgang.setInteger(agstklarbeitsplanDto
				.getIUnterarbeitsgang());

		wefLangtext.setText(agstklarbeitsplanDto.getXLangtext());

		wnfAufspannung.setInteger(agstklarbeitsplanDto.getIAufspannung());
		wcoAgart.setKeyOfSelectedItem(agstklarbeitsplanDto.getAgartCNr());
		wcbNurMaschinenzeit.setShort(agstklarbeitsplanDto
				.getBNurmaschinenzeit());
	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		wifArtikel.validate();
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (agstklarbeitsplanDto.getIId() == null) {
				agstklarbeitsplanDto.setIId(DelegateFactory.getInstance()
						.getAngebotstklDelegate()
						.createAgstklarbeitsplan(agstklarbeitsplanDto));
				setKeyWhenDetailPanel(agstklarbeitsplanDto.getIId());
			} else {
				DelegateFactory.getInstance().getAngebotstklDelegate()
						.updateAgstklarbeitsplan(agstklarbeitsplanDto);
			}
			super.eventActionSave(e, true);

			if (getInternalFrame().getKeyWasForLockMe() == null) {
				getInternalFrame().setKeyWasForLockMe(
						internalFrameStueckliste.getAgstklDto().getIId() + "");
			}
			eventYouAreSelected(false);
		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRMaschine) {
				Integer key = (Integer) ((ISourceEvent) e.getSource())
						.getIdSelected();
				MaschineDto maschineDto = null;
				maschineDto = DelegateFactory.getInstance()
						.getZeiterfassungDelegate()
						.maschineFindByPrimaryKey(key);
				agstklarbeitsplanDto.setMaschineIId(maschineDto.getIId());
				wtfMaschine.setText(maschineDto.getBezeichnung());
			}

		} else if (e.getID() == ItemChangedEvent.ACTION_LEEREN) {
			if (e.getSource() == panelQueryFLRMaschine) {
				wtfMaschine.setText(null);
				agstklarbeitsplanDto.setMaschineIId(null);
			}

		}
	}

	private void jbInit() throws Throwable {
		// das Aussenpanel hat immer das Gridbaglayout.
		gridBagLayoutAll = new GridBagLayout();
		this.setLayout(gridBagLayoutAll);

		wifArtikel = new WrapperIdentField(getInternalFrame(), this);
		wifArtikel.setDefaultFilter(ArtikelFilterFactory.getInstance()
				.createFKArtikellisteNurArbeitszeit());

		ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_THEORETISCHE_IST_ZEIT_RECHNUNG,
						ParameterFac.KATEGORIE_PERSONAL,
						LPMain.getTheClient().getMandant());

		if (((Boolean) parameter.getCWertAsObject()) == true) {
			bTheoretischeIstZeit = true;
		}
		parameter = (ParametermandantDto) DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getParametermandant(
						ParameterFac.PARAMETER_ANZEIGE_IN_ARBEITSPLAN,
						ParameterFac.KATEGORIE_FERTIGUNG,
						LPMain.getTheClient().getMandant());

		iAnzeigeArbeitszeit = (Integer) parameter.getCWertAsObject();

		// Actionpanel von Oberklasse holen und anhaengen.
		jpaButtonAction = getToolsPanel();
		this.setActionMap(null);

		wcoAgart.addActionListener(this);
		wlaKommentar.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentar"));
		wlaArbeitsgang.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.arbeitsplan.arbeitsgang"));
		wnfArbeitsgang.setFractionDigits(0);

		wcbNurMaschinenzeit.setText(LPMain
				.getTextRespectUISPr("stkl.arbeitsplan.nurmaschinenzeit"));

		wnfUnterarbeitsgang.setFractionDigits(0);
		wnfUnterarbeitsgang.setMaximumValue(0);
		wnfUnterarbeitsgang.setMaximumValue(9);

		wlaStueckzeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.arbeitsplan.stueckzeit"));
		wspStueckzeitStunden.setMandatoryField(true);
		wspStueckzeitMinuten.setMandatoryField(true);
		wspStueckzeitSekunden.setMandatoryField(true);

		wlaRuestzeit.setText(LPMain.getInstance().getTextRespectUISPr(
				"stkl.arbeitsplan.ruestzeit"));
		wspRuestzeitStunden.setMandatoryField(true);
		wspRuestzeitMinuten.setMandatoryField(true);
		wspRuestzeitSekunden.setMandatoryField(true);

		wlaLangtext.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.kommentar"));

		wnfAufspannung.setFractionDigits(0);

		wlaStueckzeitUmgewandelt.setHorizontalAlignment(SwingConstants.LEFT);
		wlaRuestzeitUmgewandelt.setHorizontalAlignment(SwingConstants.LEFT);
		wlaLossgroesse.setHorizontalAlignment(SwingConstants.LEFT);
		wnfArbeitsgang.setMandatoryField(true);
		wefLangtext = new WrapperEditorFieldTexteingabe(getInternalFrame(),
				LPMain.getInstance().getTextRespectUISPr("lp.bemerkung"));
		wbuMaschine.setText(LPMain.getInstance().getTextRespectUISPr(
				"lp.maschine")
				+ "...");
		wbuMaschine.setActionCommand(ACTION_SPECIAL_MASCHINE_FROM_LISTE);
		wbuMaschine.addActionListener(this);
		wtfMaschine.setColumnsMax(com.lp.server.util.Facade.MAX_UNBESCHRAENKT);
		wtfMaschine.setActivatable(false);

		getInternalFrame().addItemChangedListener(this);
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

		int zeile = 0;
		jpaWorkingOn.add(wifArtikel.getWbuArtikel(), new GridBagConstraints(0,
				zeile, 1, 1, 0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 80, 0));
		wifArtikel.getWtfIdent().setUppercaseField(true);

		jpaWorkingOn.add(wifArtikel.getWtfIdent(), new GridBagConstraints(1,
				zeile, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wifArtikel.getWtfBezeichnung(),
				new GridBagConstraints(5, zeile, 4, 1, 0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2),
						0, 0));
		zeile++;
		jpaWorkingOn.add(wlaArbeitsgang, new GridBagConstraints(0, zeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfArbeitsgang, new GridBagConstraints(1, zeile, 1, 1,
				0.2, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(new WrapperLabel("."), new GridBagConstraints(2,
				zeile, 1, 1, 0.0, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wnfUnterarbeitsgang, new GridBagConstraints(3, zeile,
				1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 30, 0));
		zeile++;
		if (iAnzeigeArbeitszeit == 0) {

			jpaWorkingOn
					.add(new WrapperLabel(LPMain
							.getMessageTextRespectUISPr("stkl.arbeitsplan.std")),
							new GridBagConstraints(1, zeile, 2, 1, 0.2, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 2), 0, 0));
			jpaWorkingOn
					.add(new WrapperLabel(LPMain
							.getMessageTextRespectUISPr("stkl.arbeitsplan.min")),
							new GridBagConstraints(3, zeile, 1, 1, 0.2, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 2), 0, 0));
			jpaWorkingOn
					.add(new WrapperLabel(LPMain
							.getMessageTextRespectUISPr("stkl.arbeitsplan.sek")),
							new GridBagConstraints(4, zeile, 1, 1, 0.2, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 2), 0, 0));
			jpaWorkingOn
					.add(new WrapperLabel(
							LPMain.getMessageTextRespectUISPr("stkl.arbeitsplan.millisek")),
							new GridBagConstraints(5, zeile, 1, 1, 0.2, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 2), 0, 0));
		} else {

			jpaWorkingOn
					.add(new WrapperLabel(
							LPMain.getMessageTextRespectUISPr("stkl.arbeitsplan.tage")),
							new GridBagConstraints(1, zeile, 2, 1, 0.2, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 2), 0, 0));
			jpaWorkingOn
					.add(new WrapperLabel(LPMain
							.getMessageTextRespectUISPr("stkl.arbeitsplan.std")),
							new GridBagConstraints(3, zeile, 1, 1, 0.2, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 2), 0, 0));
			jpaWorkingOn
					.add(new WrapperLabel(LPMain
							.getMessageTextRespectUISPr("stkl.arbeitsplan.min")),
							new GridBagConstraints(4, zeile, 1, 1, 0.2, 0.0,
									GridBagConstraints.WEST,
									GridBagConstraints.HORIZONTAL, new Insets(
											2, 2, 2, 2), 0, 0));
		}
		jpaWorkingOn.add(wlaLossgroesse, new GridBagConstraints(6, zeile, 1, 1,
				0.2, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 80, 0));
		jpaWorkingOn.add(
				new WrapperLabel(LPMain.getTextRespectUISPr("stkl.agart")),
				new GridBagConstraints(7, zeile, 1, 1, 0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wcoAgart, new GridBagConstraints(8, zeile, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(2, 2, 2, 2), 100, 0));
		zeile++;

		jpaWorkingOn.add(wlaRuestzeit, new GridBagConstraints(0, zeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (iAnzeigeArbeitszeit == 0) {
			jpaWorkingOn.add(wspRuestzeitStunden, new GridBagConstraints(1,
					zeile, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
			jpaWorkingOn.add(wspRuestzeitMinuten, new GridBagConstraints(3,
					zeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));

			jpaWorkingOn.add(wspRuestzeitSekunden, new GridBagConstraints(4,
					zeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
			jpaWorkingOn.add(wspMillisekundenRuestzeit, new GridBagConstraints(
					5, zeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 50, 0));
		} else {
			jpaWorkingOn.add(wspTageRuestzeit, new GridBagConstraints(1, zeile,
					2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
			jpaWorkingOn.add(wspRuestzeitStunden, new GridBagConstraints(3,
					zeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
			jpaWorkingOn.add(wspRuestzeitMinuten, new GridBagConstraints(4,
					zeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
		}
		jpaWorkingOn.add(wlaRuestzeitUmgewandelt, new GridBagConstraints(6,
				zeile, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		zeile++;

		jpaWorkingOn.add(wlaStueckzeit, new GridBagConstraints(0, zeile, 1, 1,
				0.15, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		if (iAnzeigeArbeitszeit == 0) {
			jpaWorkingOn.add(wspStueckzeitStunden, new GridBagConstraints(1,
					zeile, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
			jpaWorkingOn.add(wspStueckzeitMinuten, new GridBagConstraints(3,
					zeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));

			jpaWorkingOn.add(wspStueckzeitSekunden, new GridBagConstraints(4,
					zeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
			jpaWorkingOn.add(wspMillisekundenStueckzeit,
					new GridBagConstraints(5, zeile, 1, 1, 1.0, 0.0,
							GridBagConstraints.WEST, GridBagConstraints.BOTH,
							new Insets(2, 2, 2, 2), 50, 0));
		} else {
			jpaWorkingOn.add(wspTageStueckzeit, new GridBagConstraints(1,
					zeile, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
			jpaWorkingOn.add(wspStueckzeitStunden, new GridBagConstraints(3,
					zeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
			jpaWorkingOn.add(wspStueckzeitMinuten, new GridBagConstraints(4,
					zeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 30, 0));
		}

		jpaWorkingOn.add(wlaStueckzeitUmgewandelt, new GridBagConstraints(6,
				zeile, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn
				.add(new WrapperLabel(LPMain
						.getTextRespectUISPr("stkl.aufspannung")),
						new GridBagConstraints(7, zeile, 1, 1, 1.0, 0.0,
								GridBagConstraints.EAST,
								GridBagConstraints.HORIZONTAL, new Insets(2, 2,
										2, 2), 70, 0));
		jpaWorkingOn.add(wnfAufspannung, new GridBagConstraints(8, zeile, 1, 1,
				1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(2, 2, 2, 2), 30, 0));

		zeile++;
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG)) {

			jpaWorkingOn.add(wbuMaschine,
					new GridBagConstraints(0, zeile, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));

			jpaWorkingOn.add(wtfMaschine,
					new GridBagConstraints(1, zeile, 6, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			jpaWorkingOn.add(wcbNurMaschinenzeit,
					new GridBagConstraints(7, zeile, 2, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2,
									2), 0, 0));
			zeile++;
		}
		jpaWorkingOn.add(wlaKommentar, new GridBagConstraints(0, zeile, 1, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wtfKommentar, new GridBagConstraints(1, zeile, 4, 1,
				0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		zeile++;
		jpaWorkingOn.add(wlaLangtext, new GridBagConstraints(0, zeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wefLangtext, new GridBagConstraints(1, zeile, 8, 1,
				0.0, 1, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 50));

		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD, };

		enableToolsPanelButtons(aWhichButtonIUse);

	}

	protected String getLockMeWer() throws Exception {
		return HelperClient.LOCKME_STUECKLISTE;
	}

	void dialogQueryMaschineFromListe(ActionEvent e) throws Throwable {

		panelQueryFLRMaschine = ZeiterfassungFilterFactory.getInstance()
				.createPanelFLRMaschinen(getInternalFrame(),
						agstklarbeitsplanDto.getMaschineIId());

		new DialogQuery(panelQueryFLRMaschine);

	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		super.eventYouAreSelected(false);
		Object key = getKeyWhenDetailPanel();

		if (key == null
				|| (key != null && key.equals(LPMain.getLockMeForNew()))) {

			leereAlleFelder(this);

			clearStatusbar();

			Integer i = DelegateFactory
					.getInstance()
					.getAngebotstklDelegate()
					.getNextArbeitsgang(
							internalFrameStueckliste.getAgstklDto().getIId());

			if (i != null) {
				wnfArbeitsgang.setInteger(i);
			} else {
				wnfArbeitsgang.setInteger(new Integer(10));
			}
		} else {
			agstklarbeitsplanDto = DelegateFactory.getInstance()
					.getAngebotstklDelegate()
					.agstklarbeitsplanFindByPrimaryKey((Integer) key);

			dto2Components();
		}

		internalFrameStueckliste.getTabbedPaneAngebotstkl().refreshTitle();

	}
}
