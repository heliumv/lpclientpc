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
package com.lp.client.pc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import com.lp.client.artikel.DialogFehlmengen;
import com.lp.client.fertigung.FertigungFilterFactory;
import com.lp.client.fertigung.InternalFrameFertigung;
import com.lp.client.fertigung.TabbedPaneLos;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.InternalFrame;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.system.InternalFrameSystem;
import com.lp.client.system.TabbedPaneNachrichtarchiv;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.system.jms.service.LPMessage;
import com.lp.server.system.service.LocaleFac;
import com.lp.util.Helper;

@SuppressWarnings("static-access")
/*
 * 
 * <p> Diese Klasse kuemmert sich um die Desktopstatusbar.</p>
 * 
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * 
 * <p>Erstellung: Josef Ornetsmueller; 28.06.06</p>
 * 
 * <p>@author $Author: christian $</p>
 * 
 * @version not attributable Date $Date: 2011/03/03 13:35:15 $
 */
public class PanelDesktopStatusbar extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel jpaWorkingOn = new JPanel();

	private WrapperLabel wlaSpalte0 = null;
	private WrapperLabel wlaSpalte1 = null;
	private WrapperLabel wlaSpalte4 = null;
	private WrapperLabel wlaSpalte3 = null;
	private WrapperLabel wlaSpalte5 = null;
	private WrapperLabel wlaSpalte6 = null;
	private WrapperLabel wlaSpalte2 = null;

	private GridBagLayout gridBagLayoutWorkingOn = new GridBagLayout();
	private GridBagLayout gridBagLayoutStatusbar = new GridBagLayout();
	private final static int iHeight = 18;

	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());

	private static final int I_QUEUE = 2;
	private static final int I_TOPIC = 3;
	private static final int I_INFO_TOPIC = 4;
	private static final int I_BILD_MANDANT = 6;

	private static final int I_MAX = 6;

	public PanelDesktopStatusbar() throws Throwable {
		jbInit();
	}

	private void jbInit() throws Throwable {

		Border borderStatusbarFeld = BorderFactory.createBevelBorder(
				BevelBorder.LOWERED, Color.white, Color.white, new Color(115,
						114, 105), /** @todo JO 18.03.06 ->lp.properties PJ 5380 */
				new Color(165, 163, 151));

		jpaWorkingOn = new JPanel();
		wlaSpalte0 = new WrapperLabel();
		wlaSpalte1 = new WrapperLabel();
		wlaSpalte2 = new WrapperLabel();
		wlaSpalte3 = new WrapperLabel();
		wlaSpalte4 = new WrapperLabel();
		wlaSpalte5 = new WrapperLabel();
		wlaSpalte6 = new WrapperLabel();

		gridBagLayoutWorkingOn = new GridBagLayout();
		jpaWorkingOn.setLayout(gridBagLayoutWorkingOn);
		jpaWorkingOn.setBorder(BorderFactory.createEmptyBorder());

		this.setLayout(gridBagLayoutStatusbar);

		wlaSpalte0.setMaximumSize(new Dimension(Helper.getBreiteInPixel(4),
				iHeight));
		wlaSpalte0.setMinimumSize(new Dimension(Helper.getBreiteInPixel(4),
				iHeight));
		wlaSpalte0.setPreferredSize(new Dimension(Helper.getBreiteInPixel(4),
				iHeight));

		wlaSpalte1.setMaximumSize(new Dimension(Helper.getBreiteInPixel(10),
				iHeight));
		wlaSpalte1.setMinimumSize(new Dimension(Helper.getBreiteInPixel(10),
				iHeight));
		wlaSpalte1.setPreferredSize(new Dimension(Helper.getBreiteInPixel(10),
				iHeight));

		wlaSpalte2.setMaximumSize(new Dimension(Helper.getBreiteInPixel(4),
				iHeight));
		wlaSpalte2.setMinimumSize(new Dimension(Helper.getBreiteInPixel(4),
				iHeight));
		wlaSpalte2.setPreferredSize(new Dimension(Helper.getBreiteInPixel(4),
				iHeight));
		wlaSpalte2
				.addMouseListener(new PanelDesktopStatusbar_wlaSpalte2_mouseAdapter(
						this));

		wlaSpalte3.setMaximumSize(new Dimension(Helper.getBreiteInPixel(10),
				iHeight));
		wlaSpalte3.setMinimumSize(new Dimension(Helper.getBreiteInPixel(10),
				iHeight));
		wlaSpalte3.setPreferredSize(new Dimension(Helper.getBreiteInPixel(10),
				iHeight));
		wlaSpalte3
				.addMouseListener(new PanelDesktopStatusbar_wlaSpalte3_mouseAdapter(
						this));

		wlaSpalte4.setMaximumSize(new Dimension(10000, iHeight));
		wlaSpalte4.setMinimumSize(new Dimension(10, iHeight));
		wlaSpalte4.setPreferredSize(new Dimension(10, iHeight));
		wlaSpalte4
				.addMouseListener(new PanelDesktopStatusbar_wlaSpalte4_mouseAdapter(
						this));

		wlaSpalte5.setMaximumSize(new Dimension(10000, iHeight));
		wlaSpalte5.setMinimumSize(new Dimension(10, iHeight));
		wlaSpalte5.setPreferredSize(new Dimension(10, iHeight));

		wlaSpalte6.setMaximumSize(new Dimension(10000, iHeight));
		wlaSpalte6.setMinimumSize(new Dimension(10, iHeight));
		wlaSpalte6.setPreferredSize(new Dimension(10, iHeight));

		wlaSpalte2.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"lp.queue"));

		wlaSpalte0.setBorder(borderStatusbarFeld);
		wlaSpalte1.setBorder(borderStatusbarFeld);
		wlaSpalte2.setBorder(borderStatusbarFeld);
		wlaSpalte3.setBorder(borderStatusbarFeld);
		wlaSpalte4.setBorder(borderStatusbarFeld);
		wlaSpalte5.setBorder(borderStatusbarFeld);
		wlaSpalte6.setBorder(borderStatusbarFeld);

		Font defaultFont = HelperClient.getDefaultFont();
		// ca. 90% der standardgroesse
		Font statusbarFont = new Font(defaultFont.getFontName(),
				defaultFont.getStyle(), (defaultFont.getSize() * 9) / 10);
		wlaSpalte0.setFont(statusbarFont);
		wlaSpalte1.setFont(statusbarFont);
		wlaSpalte2.setFont(statusbarFont);
		wlaSpalte3.setFont(statusbarFont);
		wlaSpalte4.setFont(statusbarFont);
		wlaSpalte5.setFont(statusbarFont);
		wlaSpalte6.setFont(statusbarFont);

		wlaSpalte0.setHorizontalAlignment(SwingConstants.CENTER);
		wlaSpalte1.setHorizontalAlignment(SwingConstants.CENTER);
		wlaSpalte2.setHorizontalAlignment(SwingConstants.CENTER);
		wlaSpalte3.setHorizontalAlignment(SwingConstants.CENTER);
		wlaSpalte4.setHorizontalAlignment(SwingConstants.LEFT);
		wlaSpalte5.setHorizontalAlignment(SwingConstants.LEFT);
		wlaSpalte6.setHorizontalAlignment(SwingConstants.LEFT);

		jpaWorkingOn.add(wlaSpalte0, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte1, new GridBagConstraints(1, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte2, new GridBagConstraints(2, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte3, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte4, new GridBagConstraints(4, 0, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte5, new GridBagConstraints(5, 0, 1, 1, 0.3,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 2), 0, 0));
		jpaWorkingOn.add(wlaSpalte6, new GridBagConstraints(6, 0, 1, 1, 0.4,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 2), 0, 0));

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
	}

	/**
	 * Einen Wert in ein Label setzen.
	 * 
	 * @param iWhichI
	 *            int
	 * @param valueLblI
	 *            Object
	 */
	private void setValueAt(int iWhichI, Object valueLblI) {
		String sText = null;
		if (valueLblI == null) {
			sText = "";
		} else if (valueLblI instanceof String) {
			sText = (String) valueLblI;
		} else if (valueLblI instanceof java.util.Date) {
			java.util.Date dDatum = (java.util.Date) valueLblI;
			sText = Helper.formatDatum(dDatum, Defaults.getInstance()
					.getLocUI());
		} else if (valueLblI instanceof java.sql.Timestamp) {
			java.util.Date dDatum = new java.sql.Timestamp(
					((java.util.Date) valueLblI).getTime());
			sText = Helper.formatDatum(dDatum, Defaults.getInstance()
					.getLocUI());
		} else {
			sText = valueLblI.toString();
		}

		switch (iWhichI) {
		case 0:
			wlaSpalte0.setText(sText);
			break;
		case 1:
			wlaSpalte1.setText(sText);
			break;
		case 2:
			wlaSpalte2.setText(sText);
			break;
		case 3:
			wlaSpalte3.setText(sText);
			break;
		case 4:
			wlaSpalte4.setText(sText);
			break;
		case 5:
			wlaSpalte5.setText(sText);
			break;
		case 6:
			wlaSpalte6.setText(sText);
			break;
		default:
			wlaSpalte0.setText("0 >" + iWhichI + "> 5 sorry");
		}
	}

	/**
	 * Leere die StatusBar.
	 */
	public void clearStatusbar() {
		for (int i = 0; i <= I_MAX; i++) {
			this.setValueAt(i, null);
		}
	}

	public void setLPQueue(Object o) {
		this.setValueAt(I_QUEUE, o);
	}

	public void setLPTopic(Object o) {
		this.setValueAt(I_TOPIC, o);
	}

	public void setLPInfoTopic(Object o) {
		this.setValueAt(I_INFO_TOPIC, o);
	}

	public void setBildMandant(Icon o) {
		if (o == null) {
			wlaSpalte6.setIcon(null);
		} else {
			wlaSpalte6.setIcon(o);
		}

	}

	public void wlaSpalte2_mouseClicked(MouseEvent e) {
		JOptionPane pane = InternalFrame.getNarrowOptionPane(80);
		pane.setMessageType(JOptionPane.INFORMATION_MESSAGE);

		ArrayList<?> al = null;
		String msg = null;
		try {
			al = DelegateFactory.getInstance().getLPAsynchSubscriber().browse();

			MessageFormat mf = new MessageFormat(LPMain.getInstance()
					.getTextRespectUISPr("lp.queue.jobs"));
			mf.setLocale(LPMain.getInstance().getTheClient().getLocUi());
			Object pattern[] = { al.size() + "" };
			msg = mf.format(pattern) + Helper.LINE_SEPARATOR;

			if (DelegateFactory.getInstance().getLPAsynchSubscriber().getMsg() != null) {
				msg += DelegateFactory.getInstance().getLPAsynchSubscriber()
						.getLpmLast().getBbJobPositivProcessed()
						+ ": "
						+ new Time(DelegateFactory.getInstance()
								.getLPAsynchSubscriber().getMsg()
								.getJMSTimestamp())
						+ " | "
						+ DelegateFactory.getInstance().getLPAsynchSubscriber()
								.getLpmLast()
								.getSSender2stelligesModulKuerzel()
						+ " | "
						+ DelegateFactory.getInstance().getLPAsynchSubscriber()
								.getLpmLast().getSUser()
						+ " | "
						+ DelegateFactory.getInstance().getLPAsynchSubscriber()
								.getLpmLast().getIiWhatIWant()
						+ Helper.LINE_SEPARATOR;
			}

			for (int i = 0; i < al.size(); i++) {
				ObjectMessage mess = (ObjectMessage) al.get(i);
				LPMessage lpm = (LPMessage) mess.getObject();

				msg += i
						+ 1
						+ ". | "
						+ lpm.getSSender2stelligesModulKuerzel()
						+ " | "
						+ lpm.getSUser()
						+ " | "
						+ lpm.getIiWhatIWant()
						+ " | "
						+ mess.getJMSPriority()
						+ " | "
						+ ((System.currentTimeMillis() - mess.getJMSTimestamp()) / 1000)
						+ " [s] | " + Helper.LINE_SEPARATOR;
			}
			pane.setMessage(msg);
		} catch (Throwable ex) {
			// Wir brechen hier nicht ab.
			myLogger.error(ex.getMessage());
		}
		JDialog dialog = pane.createDialog(this, LPMain.getInstance()
				.getTextRespectUISPr("lp.queue.title"));
		dialog.setVisible(true);
	}

	public void wlaSpalte3_mouseClicked(MouseEvent e) {

		try {
			if (LPMain.getInstance().getDesktop()
					.darfAnwenderAufModulZugreifen(LocaleFac.BELEGART_SYSTEM)) {
				InternalFrameSystem ifSystem = (InternalFrameSystem) LPMain
						.getInstance().getDesktop()
						.holeModul(LocaleFac.BELEGART_SYSTEM);
				ifSystem.geheZu(InternalFrameSystem.IDX_PANE_NACHRICHTENARCHIV,
						TabbedPaneNachrichtarchiv.IDX_PANEL_NACHRICHTARCHIV,
						null, null, null);
			}
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void wlaSpalte4_mouseClicked(MouseEvent e) {
		try {
			String meldung = JOptionPane.showInputDialog("Meldung eingeben:");
			if (meldung.length() > 0)
				LPMain.getInstance().getInfoTopic().send2AllUser(meldung);
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

class PanelDesktopStatusbar_wlaSpalte2_mouseAdapter extends MouseAdapter {
	private PanelDesktopStatusbar adaptee;

	PanelDesktopStatusbar_wlaSpalte2_mouseAdapter(PanelDesktopStatusbar adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.wlaSpalte2_mouseClicked(e);
	}
}

class PanelDesktopStatusbar_wlaSpalte3_mouseAdapter extends MouseAdapter {
	private PanelDesktopStatusbar adaptee;

	PanelDesktopStatusbar_wlaSpalte3_mouseAdapter(PanelDesktopStatusbar adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.wlaSpalte3_mouseClicked(e);
	}
}

class PanelDesktopStatusbar_wlaSpalte4_mouseAdapter extends MouseAdapter {
	private PanelDesktopStatusbar adaptee;

	PanelDesktopStatusbar_wlaSpalte4_mouseAdapter(PanelDesktopStatusbar adaptee) {
		this.adaptee = adaptee;
	}

	public void mouseClicked(MouseEvent e) {
		adaptee.wlaSpalte4_mouseClicked(e);
	}
}
