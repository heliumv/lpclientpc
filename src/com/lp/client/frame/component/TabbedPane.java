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
package com.lp.client.frame.component;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.EventObject;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.lp.client.frame.Command;
import com.lp.client.frame.Defaults;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.ICommand;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.Desktop;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/*
 * <p>Diese Klasse ist die "Mutter" aller TabbedPane(s).</p>
 * 
 * <p>Beshreibung: </p>
 * 
 * <p>Copyright: Copyright (c) 2004</p>
 * 
 * <p>Organisation: </p>
 * 
 * @author unbekannt
 * 
 * @version $Revision: 1.10 $
 */
abstract public class TabbedPane extends JTabbedPane implements
		ItemChangedListener, // itemevt: 3 jede Tabbedpane kann vom
		// Internalframe informiert werden.
		ChangeListener, ActionListener, ITabbedPane, ICommand {
	private static final long serialVersionUID = 1L;
	private final InternalFrame internalFrame;
	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());
	private String sAddTitle = null;
	protected boolean bDarfPreiseSehen = true;

	int[] keys = { KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4,
			KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8,
			KeyEvent.VK_9, KeyEvent.VK_0, };

	public TabbedPane(InternalFrame internalFrameI, String addTitleI) {
		internalFrame = internalFrameI;
		sAddTitle = addTitleI;
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				sAddTitle);
		// darf Preise sehen.
		try {
			bDarfPreiseSehen = DelegateFactory.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF);
		} catch (Throwable e) {
		}
	}

	/**
	 * Initialierungen fuer die Komponenten. Muss zu einem Zeitpunkt aufgerufen
	 * werden, wenn die Komponenten nicht mehr null sind.
	 * 
	 */
	protected final void initComponents() {
		HelperClient.setComponentNames(this);
	}

	/**
	 * getInternalFrame
	 * 
	 * @return InternalFrame
	 */
	public InternalFrame getInternalFrame() {
		return internalFrame;
	}

	// private void setBusy() {
	//
	// getInternalFrame().getFrameProgress().start(
	// LPMain.getTextRespectUISPr("lp.working"));
	// getInternalFrame().setCursor(
	// java.awt.Cursor
	// .getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
	// setEnabled(false);
	// }

	final public void changed(EventObject e) {
		long tStart = System.currentTimeMillis();

		try {
			// housekeeping
			myLogger.info("action start: "
					+ Helper.cutString(e.toString(), Defaults.LOG_LENGTH)
					+ " in " + this.getClass().getName());
			getInternalFrame().getFrameProgress().start(
					LPMain.getTextRespectUISPr("lp.working"));
			if (Defaults.getInstance().isUseWaitCursor()) {
				getInternalFrame().setCursor(
						Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			}
			setEnabled(false);

			if (e instanceof ItemChangedEvent) {
				lPEventItemChanged((ItemChangedEvent) e);
			} else if (e instanceof ActionEvent) {
				lPActionEvent((ActionEvent) e);
			} else if (e instanceof EventObject) {
				lPEventObjectChanged((ChangeEvent) e);
			}
		} catch (Throwable t) {
			// housekeeping
			getInternalFrame().getFrameProgress().stop();
			handleException(t, true);
		} finally {
			// housekeeping
			getInternalFrame().getFrameProgress().pause();
			if (Defaults.getInstance().isUseWaitCursor()) {
				getInternalFrame().setCursor(
						Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			setEnabled(true);
			long tEnd = System.currentTimeMillis();
			myLogger.info("action end: " + (tEnd - tStart) + " [ms]");
		}
	}

	/**
	 * stateChanged
	 * 
	 * @param e
	 *            ChangeEvent
	 */
	final public void stateChanged(ChangeEvent e) {
		// Umleiten, wegen logging und so.
		changed(e);
	}

	/**
	 * Behandle ItemChangedEvent; zB in Querypanel auf anderen gewechselt.
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventItemChanged(ItemChangedEvent e) throws Throwable {
	}

	protected abstract JMenuBar getJMenuBar() throws Throwable;

	public void insertTab(String title, Icon icon, Component component,
			String tip, int index) {
		super.insertTab(title, icon, component, tip, index);

		if (index < 9) {

			setTitleAt(index, (index + 1) + " " + getTitleAt(index));
			setMnemonicAt(index, keys[index]);

		}

	}

	/**
	 * Liefert den aktuell ausgew?hlten Reiter, wenn dieser ein PanelBasis ist,
	 * sonst null
	 * 
	 * @return das Aktuelle Panel
	 */
	protected PanelBasis getAktuellesPanel() {
		if (getSelectedComponent() instanceof PanelBasis)
			return (PanelBasis) getSelectedComponent();
		return null;
	}

	public void refreshAktuellesPanel() throws Throwable {
		initDtos();
		PanelBasis tab = getAktuellesPanel();
		if (tab != null) {
			tab.eventYouAreSelected(false);
		}
	}

	protected void initDtos() throws Throwable {
	}

	/**
	 * Behandle ActionEvent; zB Menue-Klick oben.
	 * 
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	abstract protected void lPActionEvent(ActionEvent e) throws Throwable;

	/**
	 * Behandle ChangeEvent; zB Tabwechsel oben.
	 * 
	 * @param e
	 *            ChangeEvent
	 * @throws Throwable
	 */
	public void lPEventObjectChanged(ChangeEvent e) throws Throwable {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_AS_I_LIKE, "");
		// TP-bezogene Menubar einsetzen
		JMenuBar jMenuBar = getJMenuBar();

		/*
		 * Alle Menuepunkte im System.out anzeigen if (jMenuBar instanceof
		 * WrapperMenuBar) { WrapperMenuBar mb = (WrapperMenuBar) jMenuBar; for
		 * (int i = 0; i < mb.getComponents().length; i++) { if
		 * (mb.getComponents()[i] instanceof JMenu) { JMenu menu = (JMenu)
		 * mb.getComponents()[i]; // System.out.println(menu.getText());
		 * 
		 * for (int j = 0; j < menu.getPopupMenu().getComponents().length; j++)
		 * {
		 * 
		 * if (menu.getPopupMenu().getComponents()[j] instanceof JMenuItem) {
		 * JMenuItem item = (JMenuItem) menu.getPopupMenu() .getComponents()[j];
		 * 
		 * System.out.println("Modul: " + getInternalFrame().getBelegartCNr() +
		 * " TabbedPane: " + getSAddTitle() + " Men\u00FC: " + menu.getText() +
		 * " Item: " + item.getText());
		 * 
		 * } }
		 * 
		 * } }
		 * 
		 * }
		 */

		Component[] jMenus = jMenuBar.getComponents();
		for (int i = 0; i < jMenus.length; i++) {
			if (jMenus[i] instanceof JMenu) {
				// Die einzelnen Menues werden nur dann angezeigt, wenn auch was
				// drin ist
				JMenu jMenu = (JMenu) jMenus[i];
				jMenu.setVisible(jMenu.getItemCount() > 0);
			}
		}
		HelperClient.setComponentNamesMenuBar(jMenuBar);
		getInternalFrame().setJMenuBar(jMenuBar);
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLUNTEN,
				sAddTitle);
	}

	public String getSAddTitle() {
		return sAddTitle;
	}

	final public void actionPerformed(ActionEvent e) {
		changed(e);
	}

	/**
	 * handleex: Behandle Expection t; Meldung fuer den Benutzer; evtl. close
	 * Frame. Dies ist die zentrale Methode um allgemeine (frameweite)
	 * Exceptions abzuhandeln.
	 * 
	 * @param t
	 *            Throwable
	 * @param bHandleHardI
	 * <br/>
	 *            true ... Wird die Exception nicht gefunden kommt eine allg.
	 *            Errormeldung und der Internalframe wird geschlossen.<br/>
	 *            false ... Es wird versucht die Exception abzuhandeln, wenn
	 *            nicht moeglich, wird false retourniert; es wird keine Meldung
	 *            angezeigt
	 * @return boolean
	 */
	public boolean handleException(Throwable t, boolean bHandleHardI) {

		boolean bErrorBekannt = false;

		// Alles wird geloggt.
		if (t != null) {
			String sLog = t.getClass().getName() + ": "
					+ t.getLocalizedMessage();
			StackTraceElement[] ste = t.getStackTrace();
			if (ste.length > 0) {
				sLog = sLog + "\n" + ste[0].toString();
			}
			myLogger.error(sLog, t);
		}

		ExceptionLP efc = null;
		if (t instanceof ExceptionLP) {
			efc = (ExceptionLP) t;
		}

		if (efc != null) {
			try {
				if (handleOwnException(efc)) {
					return true;
				}
			} catch (Throwable ex) {
				// nothing here
			}
		}

		// Alle ExceptionForLPClients behandeln.
		String sMsg = null;
		if (efc != null) {
			sMsg = LPMain.getInstance().getMsg(efc);
			bErrorBekannt = (sMsg != null);
			if (!bErrorBekannt) {
				// exhc4: Fehlercode wird noch nicht abgefangen
				sMsg = "ExceptionForLPClients, Fehlercode unbekannt: "
						+ efc.getICode();
			}
			if (efc.getICode() == EJBExceptionLP.FEHLER_BEIM_UPDATE) {
				getInternalFrame().fireItemChanged(this,
						ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP);
			}
		}

		if (bErrorBekannt) {
			JOptionPane pane = InternalFrame
					.getNarrowOptionPane(com.lp.client.pc.Desktop.MAX_CHARACTERS_UNTIL_WORDWRAP);
			pane.setMessage(sMsg);
			pane.setMessageType(JOptionPane.ERROR_MESSAGE);

			JDialog dialog = pane.createDialog(LPMain.getInstance()
					.getDesktop(), LPMain.getTextRespectUISPr("lp.error"));

			dialog.setVisible(true);
		}

		if (!bErrorBekannt && bHandleHardI) {
			// exhc5: Internalframe schliessen.
			LPMain.getInstance().exitFrame(getInternalFrame(), t);
		}

		return bErrorBekannt;
	}

	/**
	 * Eigene ExceptionForLPClients's verarbeiten.
	 * 
	 * myexception: 1
	 * 
	 * @param exfc
	 *            ExceptionForLPClients
	 * @return boolean
	 */
	protected boolean handleOwnException(ExceptionLP exfc) {
		return false;
	}

	protected Boolean positionAmEndeEinfuegen() {
		ButtonGroup group = new javax.swing.ButtonGroup();
		String msgString1 = LPMain.getTextRespectUISPr("lp.einfuegen.titel");
		JRadioButton opt1 = new javax.swing.JRadioButton(
				LPMain.getTextRespectUISPr("lp.einfuegen.voraktuellercursor"));
		JRadioButton opt2 = new javax.swing.JRadioButton(
				LPMain.getTextRespectUISPr("lp.einfuegen.amende"));

		if (Desktop.einfuegenAusZwischenablage == 0) {
			opt1.setSelected(true);
		} else {
			opt2.setSelected(true);
		}

		group.add(opt1);
		group.add(opt2);

		Object[] array = { msgString1, opt1, opt2 };
		int sel = javax.swing.JOptionPane.showConfirmDialog(null, array, "",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (sel == 0) {
			if (opt2.isSelected()) {
				Desktop.einfuegenAusZwischenablage = 1;
				return true;
			} else {
				Desktop.einfuegenAusZwischenablage = 0;
				return false;
			}
		} else if (sel == -1) {
			return null;
		}

		return null;
	}

	/**
	 * evtvet: Event "Vetoable Window close"; wird null zurueckgegeben, so wird
	 * das Modul via dicard beendet, wird ein PropertyVetoException
	 * zurueckgegeben, bleibt das Modul "erhalten".
	 * 
	 * @return PropertyVetoException
	 * @throws Throwable
	 */
	public PropertyVetoException vetoableChangeLP() throws Throwable {

		if (this.getSelectedComponent() instanceof PanelBasis) {
			PanelBasis pb = (PanelBasis) this.getSelectedComponent();
			if (null == pb)
				return null; // scheint so, als ob es Situationen gibt bei denen
								// noch nichts selektiert ist? (ghp)
			return pb.vetoableChangeLP();
		} else {
			return null;
		}

	}

	/**
	 * Setze den Einbgabefocus auf die erste Component.
	 * 
	 * @throws Exception
	 */
	public void setFirstFocusableComponent() throws Exception {
		PanelBasis pb = (PanelBasis) this.getSelectedComponent();
		pb.setFirstFocusableComponent();
	}

	/**
	 * Setze den Einbgabefocus auf die erste Component.
	 * 
	 * @throws Exception
	 * @return JComponent
	 */
	public JComponent getFirstFocusableComponent() throws Exception {
		PanelBasis pb = (PanelBasis) this.getSelectedComponent();
		return pb.getFirstFocusableComponent();
	}

	/**
	 * Gebe das DTO des TabbedPane zur&uuml;ck
	 * 
	 * @return das Dto
	 */
	public Object getDto() {
		return null;
	}

	public int execute(Command command) {
		return ICommand.COMMAND_NOT_DONE;
	}

	public void finalize() throws Throwable {
		// this.removeAll();
		super.finalize();
	}
}
