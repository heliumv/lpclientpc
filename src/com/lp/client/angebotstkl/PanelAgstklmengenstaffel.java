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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.ISourceEvent;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.ItemChangedEvent;
import com.lp.client.frame.component.PanelBasis;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.component.WrapperButton;
import com.lp.client.frame.component.WrapperKeyValueField;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.component.WrapperNumberField;
import com.lp.client.frame.component.WrapperTextField;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.angebotstkl.service.AgstklmengenstaffelDto;
import com.lp.server.angebotstkl.service.AngebotstklFac;
import com.lp.server.fertigung.service.LosklasseDto;
import com.lp.server.fertigung.service.LoslosklasseDto;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/**
 * <p>Panel zum Bearbeiten der Klassen eines Loses</p>
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * <p>Erstellungsdatum <I>24. 10. 2005</I></p>
 * <p> </p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.4 $
 */
public class PanelAgstklmengenstaffel extends PanelBasis {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TabbedPaneAngebotstkl tabbedPaneAgstkl = null;

	private AgstklmengenstaffelDto agstklmengenstaffelDto = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jpaWorkingOn = new JPanel();
	private GridBagLayout gridBagLayout3 = new GridBagLayout();
	private Border border1;

	private WrapperLabel wlaMenge = new WrapperLabel();
	private WrapperNumberField wnfMenge = new WrapperNumberField();

	private WrapperLabel wlaVkPreisGewaehlt = new WrapperLabel();
	private WrapperNumberField wnfVkPreisGewaehlt = new WrapperNumberField();

	private WrapperKeyValueField wkvDBVkpreis = new WrapperKeyValueField(50);
	private WrapperKeyValueField wkvDBVkpreisProzent = new WrapperKeyValueField(
			50);

	private WrapperKeyValueField wkvDBVkpreisGewaehlt = new WrapperKeyValueField(
			50);
	private WrapperKeyValueField wkvDBVkpreisGewaehltProzent = new WrapperKeyValueField(
			50);

	private WrapperLabel wlaMaterialeinsatzLief1 = new WrapperLabel();
	private WrapperNumberField wnfMaterialeinsatzLief1 = new WrapperNumberField();
	private WrapperLabel wlaAZEinsatzLief1 = new WrapperLabel();
	private WrapperNumberField wnfAZEinsatzLief1 = new WrapperNumberField();
	private WrapperLabel wlaVkPreisAgstkl = new WrapperLabel();
	private WrapperNumberField wnfVkPreisAgstkl = new WrapperNumberField();

	private WrapperLabel wlaSummeLief1 = new WrapperLabel();
	private WrapperNumberField wnfSummeLief1 = new WrapperNumberField();

	private WrapperLabel wlaVkPreisKundenpreisfindung = new WrapperLabel();
	private WrapperNumberField wnfVkPreisKundenpreisfindung = new WrapperNumberField();

	WrapperLabel wlaMandantenwaehrung1 = new WrapperLabel("");
	WrapperLabel wlaMandantenwaehrung2 = new WrapperLabel("");
	WrapperLabel wlaMandantenwaehrung3 = new WrapperLabel("");
	WrapperLabel wlaMandantenwaehrung4 = new WrapperLabel("");
	WrapperLabel wlaMandantenwaehrung5 = new WrapperLabel("");
	WrapperLabel wlaMandantenwaehrung6 = new WrapperLabel("");
	WrapperLabel wlaMandantenwaehrung7 = new WrapperLabel("");
	WrapperLabel wlaMandantenwaehrung8 = new WrapperLabel("");

	public PanelAgstklmengenstaffel(InternalFrame internalFrame,
			String add2TitleI, Object key,
			TabbedPaneAngebotstkl tabbedPaneAgstkl) throws Throwable {
		super(internalFrame, add2TitleI, key);
		this.tabbedPaneAgstkl = tabbedPaneAgstkl;
		jbInit();
		initComponents();
	}

	private TabbedPaneAngebotstkl getTabbedPaneAgstkl() {
		return tabbedPaneAgstkl;
	}

	private void jbInit() throws Throwable {
		String[] aWhichButtonIUse = { ACTION_UPDATE, ACTION_SAVE,
				ACTION_DELETE, ACTION_DISCARD };
		this.enableToolsPanelButtons(aWhichButtonIUse);

		border1 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		this.setLayout(gridBagLayout1);

		jpaWorkingOn.setBorder(border1);
		jpaWorkingOn.setLayout(gridBagLayout3);
		JPanel panelButtonAction = getToolsPanel();
		getInternalFrame().addItemChangedListener(this);

		// controls
		wlaMenge.setText(LPMain.getInstance().getTextRespectUISPr("lp.menge"));
		wnfMenge.setMandatoryField(true);

		wlaVkPreisGewaehlt.setText(LPMain.getInstance().getTextRespectUISPr(
				"as.agstkl.mengenstaffel.vkpreisgewaehlt"));

		wlaVkPreisAgstkl.setText(LPMain.getInstance().getTextRespectUISPr(
				"as.agstkl.mengenstaffel.vkpreis"));

		wlaVkPreisKundenpreisfindung.setText(LPMain.getInstance()
				.getTextRespectUISPr(
						"as.agstkl.mengenstaffel.vkpreis.preisfindung"));

		wlaMaterialeinsatzLief1.setText(LPMain.getInstance()
				.getTextRespectUISPr("as.agstkl.mengenstaffel.materiallief1"));

		wlaAZEinsatzLief1.setText(LPMain.getInstance().getTextRespectUISPr(
				"as.agstkl.mengenstaffel.azlief1"));

		wkvDBVkpreisGewaehltProzent.getWlaKey().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"as.agstkl.mengenstaffel.dbvkpreisgewaehltprozent"));
		wkvDBVkpreisGewaehlt.getWlaKey().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"as.agstkl.mengenstaffel.dbvkpreisgewaehlt"));

		wkvDBVkpreisProzent.getWlaKey().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"as.agstkl.mengenstaffel.dbvkpreisprozent"));
		wkvDBVkpreis.getWlaKey().setText(
				LPMain.getInstance().getTextRespectUISPr(
						"as.agstkl.mengenstaffel.dbvkpreis"));
		wlaSummeLief1.setText(LPMain.getInstance().getTextRespectUISPr(
				"agstkl.mengenstaffel.summe.lief1"));

		// Nachkommastellen
		wnfMenge.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenMenge());
		wnfMaterialeinsatzLief1.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfAZEinsatzLief1.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfSummeLief1.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseEK());
		wnfVkPreisAgstkl.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());
		wnfVkPreisKundenpreisfindung.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());
		wnfVkPreisGewaehlt.setFractionDigits(Defaults.getInstance()
				.getIUINachkommastellenPreiseVK());

		wnfMenge.addFocusListener(new PanelAgstklmengenstaffel_wnfMenge_focusAdapter(
				this));

		wnfVkPreisGewaehlt
				.addFocusListener(new PanelAgstklmengenstaffel_wnfVkPreisGewaehlt_focusAdapter(
						this));

		wnfMaterialeinsatzLief1.setActivatable(false);
		wnfAZEinsatzLief1.setActivatable(false);
		wnfVkPreisAgstkl.setActivatable(false);

		wlaMandantenwaehrung1.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMandantenwaehrung2.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMandantenwaehrung3.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMandantenwaehrung4.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMandantenwaehrung5.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMandantenwaehrung6.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMandantenwaehrung7.setHorizontalAlignment(SwingConstants.LEFT);
		wlaMandantenwaehrung8.setHorizontalAlignment(SwingConstants.LEFT);

		WrapperLabel wlaProzent1 = new WrapperLabel("%");
		wlaProzent1.setHorizontalAlignment(SwingConstants.LEFT);
		WrapperLabel wlaProzent2 = new WrapperLabel("%");
		wlaProzent2.setHorizontalAlignment(SwingConstants.LEFT);

		this.add(panelButtonAction, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 0, 0));
		this.add(jpaWorkingOn, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		// statusbarneu: 1 an den unteren rand des panels haengen
		this.add(getPanelStatusbar(), new GridBagConstraints(0, 2, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		iZeile++;
		jpaWorkingOn.add(wlaMenge, new GridBagConstraints(0, iZeile, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfMenge, new GridBagConstraints(1, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaMaterialeinsatzLief1, new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfMaterialeinsatzLief1, new GridBagConstraints(1,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaMandantenwaehrung1, new GridBagConstraints(2,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaVkPreisAgstkl, new GridBagConstraints(3, iZeile, 1,
				1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfVkPreisAgstkl, new GridBagConstraints(4, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMandantenwaehrung8, new GridBagConstraints(5,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaAZEinsatzLief1, new GridBagConstraints(0, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfAZEinsatzLief1, new GridBagConstraints(1, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMandantenwaehrung2, new GridBagConstraints(2,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaVkPreisKundenpreisfindung, new GridBagConstraints(
				3, iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfVkPreisKundenpreisfindung, new GridBagConstraints(
				4, iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMandantenwaehrung7, new GridBagConstraints(5,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wlaSummeLief1, new GridBagConstraints(0, iZeile, 1, 1,
				0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wnfSummeLief1, new GridBagConstraints(1, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMandantenwaehrung3, new GridBagConstraints(2,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaVkPreisGewaehlt, new GridBagConstraints(3, iZeile,
				1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 100, 0));
		jpaWorkingOn.add(wnfVkPreisGewaehlt, new GridBagConstraints(4, iZeile,
				1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMandantenwaehrung5, new GridBagConstraints(5,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wkvDBVkpreis.getWlaKey(), new GridBagConstraints(0,
				iZeile, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wkvDBVkpreis.getWlaValue(), new GridBagConstraints(1,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMandantenwaehrung4, new GridBagConstraints(2,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wkvDBVkpreisGewaehlt.getWlaKey(),
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wkvDBVkpreisGewaehlt.getWlaValue(),
				new GridBagConstraints(4, iZeile, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaMandantenwaehrung6, new GridBagConstraints(5,
				iZeile, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		jpaWorkingOn.add(wkvDBVkpreisProzent.getWlaKey(),
				new GridBagConstraints(0, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wkvDBVkpreisProzent.getWlaValue(),
				new GridBagConstraints(1, iZeile, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wlaProzent1, new GridBagConstraints(2, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		jpaWorkingOn.add(wkvDBVkpreisGewaehltProzent.getWlaKey(),
				new GridBagConstraints(3, iZeile, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wkvDBVkpreisGewaehltProzent.getWlaValue(),
				new GridBagConstraints(4, iZeile, 1, 1, 1.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(2, 2, 2, 2), 0, 0));
		jpaWorkingOn.add(wlaProzent2, new GridBagConstraints(5, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		if (allMandatoryFieldsSetDlg()) {
			components2Dto();
			if (agstklmengenstaffelDto != null) {
				if (agstklmengenstaffelDto.getIId() == null) {
					agstklmengenstaffelDto.setIId(DelegateFactory.getInstance()
							.getAngebotstklDelegate()
							.createAgstklmengenstaffel(agstklmengenstaffelDto));
				} else {
					DelegateFactory.getInstance().getAngebotstklDelegate()
							.updateAgstklmengenstaffel(agstklmengenstaffelDto);
				}

				this.agstklmengenstaffelDto = DelegateFactory
						.getInstance()
						.getAngebotstklDelegate()
						.agstklmengenstaffelFindByPrimaryKey(
								agstklmengenstaffelDto.getIId());
				setKeyWhenDetailPanel(agstklmengenstaffelDto.getIId());
				super.eventActionSave(e, true);
				// jetz den anzeigen
				eventYouAreSelected(false);
			}
		}
	}

	/**
	 * Die eingegebenen Daten in ein Dto schreiben
	 * 
	 * @throws Throwable
	 */
	private void components2Dto() throws Throwable {
		agstklmengenstaffelDto.setAgstklIId(getTabbedPaneAgstkl()
				.getInternalFrameAngebotstkl().getAgstklDto().getIId());
		agstklmengenstaffelDto.setNMenge(wnfMenge.getBigDecimal());
		agstklmengenstaffelDto.setNVkpreisGewaehlt(wnfVkPreisGewaehlt
				.getBigDecimal());

		if (wnfVkPreisGewaehlt.getBigDecimal() != null) {
			agstklmengenstaffelDto
					.setNMaterialeinsatzLief1(wnfMaterialeinsatzLief1
							.getBigDecimal());
			agstklmengenstaffelDto.setNAzeinsatzLief1(wnfAZEinsatzLief1
					.getBigDecimal());
			agstklmengenstaffelDto
					.setNVkpreis(wnfVkPreisAgstkl.getBigDecimal());

		} else {
			agstklmengenstaffelDto.setNMaterialeinsatzLief1(null);
			agstklmengenstaffelDto.setNAzeinsatzLief1(null);
			agstklmengenstaffelDto.setNVkpreis(null);
		}

	}

	/**
	 * Ein Dto-Objekt ins Panel uebertragen
	 * 
	 * @throws Throwable
	 */

	private BigDecimal setWareneinsatz() throws Throwable {

		return DelegateFactory
				.getInstance()
				.getAngebotstklDelegate()
				.getWareneinsatzLief1(
						wnfMenge.getBigDecimal(),
						getTabbedPaneAgstkl().getInternalFrameAngebotstkl()
								.getAgstklDto().getIId());

	}

	private BigDecimal setAZEinsatz() throws Throwable {

		return DelegateFactory
				.getInstance()
				.getAngebotstklDelegate()
				.getAZeinsatzLief1(
						wnfMenge.getBigDecimal(),
						getTabbedPaneAgstkl().getInternalFrameAngebotstkl()
								.getAgstklDto().getIId());

	}

	private BigDecimal[] setVKPreis() throws Throwable {

		return DelegateFactory
				.getInstance()
				.getAngebotstklDelegate()
				.getVKPreis(
						wnfMenge.getBigDecimal(),
						getTabbedPaneAgstkl().getInternalFrameAngebotstkl()
								.getAgstklDto().getIId());

	}

	private void berechneDB() throws Throwable {

		BigDecimal bdVkpreis = BigDecimal.ZERO;

		if (wnfVkPreisAgstkl.getBigDecimal() != null) {
			bdVkpreis = wnfVkPreisAgstkl.getBigDecimal();
		}

		BigDecimal bdWareneinsatz = BigDecimal.ZERO;

		if (wnfMaterialeinsatzLief1.getBigDecimal() != null) {
			bdWareneinsatz = wnfMaterialeinsatzLief1.getBigDecimal();
		}

		BigDecimal bdAZEinsatz = BigDecimal.ZERO;

		if (wnfAZEinsatzLief1.getBigDecimal() != null) {
			bdAZEinsatz = wnfAZEinsatzLief1.getBigDecimal();
		}

		BigDecimal dbPreis = bdVkpreis.subtract(bdWareneinsatz).subtract(
				bdAZEinsatz);

		wkvDBVkpreis.getWlaValue().setText(
				Helper.formatZahl(dbPreis, Defaults.getInstance()
						.getIUINachkommastellenPreiseVK(), LPMain.getInstance()
						.getTheClient().getLocUi()));
		BigDecimal dbPreisProzent = BigDecimal.ZERO;
		if (bdVkpreis.doubleValue() != 0) {
			dbPreisProzent = dbPreis.divide(bdVkpreis,
					Defaults.getInstance().getIUINachkommastellenPreiseVK(),
					BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
		}

		wkvDBVkpreisProzent.getWlaValue().setText(
				Helper.formatZahl(dbPreisProzent, 2, LPMain.getInstance()
						.getTheClient().getLocUi()));

		if (wnfVkPreisGewaehlt.getBigDecimal() != null) {
			BigDecimal dbPreisVKGewaehlt = wnfVkPreisGewaehlt.getBigDecimal()
					.subtract(bdWareneinsatz).subtract(bdAZEinsatz);

			wkvDBVkpreisGewaehlt.getWlaValue().setText(
					Helper.formatZahl(dbPreisVKGewaehlt, Defaults.getInstance()
							.getIUINachkommastellenPreiseVK(), LPMain
							.getInstance().getTheClient().getLocUi()));

			BigDecimal dbPreisProzentVKGewaehlt = BigDecimal.ZERO;
			if (bdVkpreis.doubleValue() != 0) {
				dbPreisProzentVKGewaehlt = dbPreisVKGewaehlt
						.divide(wnfVkPreisGewaehlt.getBigDecimal(),
								Defaults.getInstance()
										.getIUINachkommastellenPreiseVK(),
								BigDecimal.ROUND_HALF_UP).multiply(
								new BigDecimal(100));
			}

			wkvDBVkpreisGewaehltProzent.getWlaValue().setText(
					Helper.formatZahl(dbPreisProzentVKGewaehlt, 2, LPMain
							.getInstance().getTheClient().getLocUi()));

		} else {
			wkvDBVkpreisGewaehlt.getWlaValue().setText(null);
			wkvDBVkpreisGewaehltProzent.getWlaValue().setText(null);
		}
	}

	private void dto2Components() throws Throwable {
		wnfMenge.setBigDecimal(agstklmengenstaffelDto.getNMenge());
		wnfVkPreisGewaehlt.setBigDecimal(agstklmengenstaffelDto
				.getNVkpreisGewaehlt());

		BigDecimal bdWareneinsatz = agstklmengenstaffelDto
				.getNMaterialeinsatzLief1();

		if (bdWareneinsatz == null) {
			bdWareneinsatz = setWareneinsatz();
		}

		BigDecimal bdAZEinsatz = agstklmengenstaffelDto.getNAzeinsatzLief1();
		if (bdAZEinsatz == null) {
			bdAZEinsatz = setAZEinsatz();
		}

		BigDecimal bdVkpreis = agstklmengenstaffelDto.getNVkpreis();

		BigDecimal[] vkpreise = setVKPreis();

		if (bdVkpreis == null) {
			bdVkpreis = vkpreise[AngebotstklFac.VKPREIS_LT_AGTSKLPOSITIONSPREIS];
		}

		wnfMaterialeinsatzLief1.setBigDecimal(bdWareneinsatz);
		wnfAZEinsatzLief1.setBigDecimal(bdAZEinsatz);

		wnfSummeLief1.setBigDecimal(wnfAZEinsatzLief1.getBigDecimal().add(
				wnfMaterialeinsatzLief1.getBigDecimal()));

		wnfVkPreisAgstkl.setBigDecimal(bdVkpreis);
		wnfVkPreisKundenpreisfindung
				.setBigDecimal(vkpreise[AngebotstklFac.VKPREIS_LT_KUNDENPREISFINDUNG]);

		berechneDB();

	}

	public void eventActionNew(EventObject eventObject,
			boolean bChangeKeyLockMeI, boolean bNeedNoNewI) throws Throwable {
		super.eventActionNew(eventObject, bChangeKeyLockMeI, false);
		agstklmengenstaffelDto = new AgstklmengenstaffelDto();

		wkvDBVkpreis.getWlaValue().setText("");
		wkvDBVkpreisGewaehlt.getWlaValue().setText("");
		wkvDBVkpreisGewaehltProzent.getWlaValue().setText("");
		wkvDBVkpreisProzent.getWlaValue().setText("");
		this.leereAlleFelder(this);
	}

	/**
	 * Stornieren einer Rechnung bzw Gutschrift
	 * 
	 * @param e
	 *            ActionEvent
	 * @param bAdministrateLockKeyI
	 *            boolean
	 * @param bNeedNoDeleteI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {
		if (this.agstklmengenstaffelDto != null) {
			if (agstklmengenstaffelDto.getIId() != null) {
				if (!isLockedDlg()) {
					DelegateFactory
							.getInstance()
							.getAngebotstklDelegate()
							.removeAgstklmengenstaffel(
									agstklmengenstaffelDto.getIId());
					this.agstklmengenstaffelDto = null;
					this.leereAlleFelder(this);
					super.eventActionDelete(e, false, false);
				}
			}
		}
	}

	protected String getLockMeWer() {
		return HelperClient.LOCKME_AGSTKL;
	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {

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

		}
	}

	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {
		super.eventYouAreSelected(false);

		wlaMandantenwaehrung1.setText(getTabbedPaneAgstkl()
				.getInternalFrameAngebotstkl().getAgstklDto().getWaehrungCNr());
		wlaMandantenwaehrung2.setText(getTabbedPaneAgstkl()
				.getInternalFrameAngebotstkl().getAgstklDto().getWaehrungCNr());
		wlaMandantenwaehrung3.setText(getTabbedPaneAgstkl()
				.getInternalFrameAngebotstkl().getAgstklDto().getWaehrungCNr());
		wlaMandantenwaehrung4.setText(getTabbedPaneAgstkl()
				.getInternalFrameAngebotstkl().getAgstklDto().getWaehrungCNr());
		wlaMandantenwaehrung5.setText(getTabbedPaneAgstkl()
				.getInternalFrameAngebotstkl().getAgstklDto().getWaehrungCNr());
		wlaMandantenwaehrung6.setText(getTabbedPaneAgstkl()
				.getInternalFrameAngebotstkl().getAgstklDto().getWaehrungCNr());
		wlaMandantenwaehrung7.setText(getTabbedPaneAgstkl()
				.getInternalFrameAngebotstkl().getAgstklDto().getWaehrungCNr());
		wlaMandantenwaehrung8.setText(getTabbedPaneAgstkl()
				.getInternalFrameAngebotstkl().getAgstklDto().getWaehrungCNr());

		if (!bNeedNoYouAreSelectedI) {
			Object key = getKeyWhenDetailPanel();
			if (key == null
					|| (key != null && key.equals(LPMain.getLockMeForNew()))) {
				// einen neuen Eintrag anlegen oder die letzte Position wurde
				// geloescht.
				leereAlleFelder(this);
				clearStatusbar();
			} else {
				// einen alten Eintrag laden.
				agstklmengenstaffelDto = DelegateFactory.getInstance()
						.getAngebotstklDelegate()
						.agstklmengenstaffelFindByPrimaryKey((Integer) key);
				dto2Components();
			}
		}
		tabbedPaneAgstkl.refreshTitle();
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		super.eventActionUpdate(aE, bNeedNoUpdateI);
	}

	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		return false;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	void wnfMenge_focusLost(FocusEvent e) {
		try {
			if (wnfMenge.getBigDecimal() != null) {

				wnfMaterialeinsatzLief1.setBigDecimal(setWareneinsatz());
				wnfAZEinsatzLief1.setBigDecimal(setAZEinsatz());

				BigDecimal[] vkpreise = setVKPreis();

				wnfVkPreisAgstkl
						.setBigDecimal(vkpreise[AngebotstklFac.VKPREIS_LT_AGTSKLPOSITIONSPREIS]);
				wnfVkPreisKundenpreisfindung
						.setBigDecimal(vkpreise[AngebotstklFac.VKPREIS_LT_KUNDENPREISFINDUNG]);

				berechneDB();
			}
		} catch (Throwable e1) {
			handleException(e1, true);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	void wnfVkPReisGeweahlt_focusLost(FocusEvent e) {
		try {
			if (wnfMenge.getBigDecimal() != null) {

				wnfMaterialeinsatzLief1.setBigDecimal(setWareneinsatz());
				wnfAZEinsatzLief1.setBigDecimal(setAZEinsatz());

				BigDecimal[] vkpreise = setVKPreis();

				wnfVkPreisAgstkl
						.setBigDecimal(vkpreise[AngebotstklFac.VKPREIS_LT_AGTSKLPOSITIONSPREIS]);
				wnfVkPreisKundenpreisfindung
						.setBigDecimal(vkpreise[AngebotstklFac.VKPREIS_LT_KUNDENPREISFINDUNG]);

				berechneDB();
			}
		} catch (Throwable e1) {
			handleException(e1, true);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}

class PanelAgstklmengenstaffel_wnfMenge_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelAgstklmengenstaffel adaptee;

	PanelAgstklmengenstaffel_wnfMenge_focusAdapter(
			PanelAgstklmengenstaffel adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfMenge_focusLost(e);
	}
}

class PanelAgstklmengenstaffel_wnfVkPreisGewaehlt_focusAdapter extends
		java.awt.event.FocusAdapter {
	PanelAgstklmengenstaffel adaptee;

	PanelAgstklmengenstaffel_wnfVkPreisGewaehlt_focusAdapter(
			PanelAgstklmengenstaffel adaptee) {
		this.adaptee = adaptee;
	}

	public void focusLost(FocusEvent e) {
		adaptee.wnfVkPReisGeweahlt_focusLost(e);
	}
}
