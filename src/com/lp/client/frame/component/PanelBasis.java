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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;

import javax.persistence.EntityExistsException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.hibernate.exception.ConstraintViolationException;

import com.lp.client.frame.Defaults;
import com.lp.client.frame.DialogError;
import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.LockStateValue;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.frame.report.PanelReportIfJRDS;
import com.lp.client.pc.LPButtonAction;
import com.lp.client.pc.LPMain;
import com.lp.client.util.logger.LpLogger;
import com.lp.editor.LpEditor;
import com.lp.server.artikel.service.ArtgruDto;
import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.LockMeDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.IModificationData;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <b>frame</b><BR>
 * Dies ist die Basisiklasse aller Panels.<BR>
 *
 * <p>
 * Erstellungsdatum 10.10.04
 * </p>
 *
 * @version $Revision: 1.45 $
 * @author Josef Ornetsmueller
 */
public abstract class PanelBasis extends JPanel implements KeyListener,
		ActionListener, MouseListener, ItemChangedListener, FocusListener,
		IPanelBasis {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private ToolBar toolBar = null;
	private PanelStatusbar panelStatusbar = null;
	private InternalFrame internalFrame = null;

	protected final LpLogger myLogger = (LpLogger) LpLogger.getInstance(this
			.getClass());

	// Zeilennummer fuer's Gridbaglayout
	protected int iZeile = 0;

	static final public int LOCK_IS_NOT_LOCKED = -1;
	static final public int LOCK_FOR_EMPTY = 0;
	static final public int LOCK_FOR_NEW = 1;
	static final public int LOCK_IS_LOCKED_BY_ME = 2;
	static final public int LOCK_IS_LOCKED_BY_OTHER_USER = 3;
	static final public int LOCK_NO_LOCKING = 4;
	static final public int LOCK_ENABLE_REFRESHANDPRINT_ONLY = 5;
	static final public int LOCK_ENABLE_REFRESHANDUPDATE_ONLY = 6;
	static final public int LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY = 7;
	static final public int LOCK_DISABLE_ALL = 8;

	static final public String ACTION_NEW = "action_new";
	static final public String ACTION_LOCK = "action_lock";
	static final public String ACTION_DISCARD = "action_discard";
	static final public String ACTION_SAVE = "action_save";
	static final public String ACTION_DELETE = "action_delete";
	static final public String ACTION_FILTER = "action_filter";
	static final public String ACTION_REFRESH = "action_refresh";
	static final public String ACTION_PRINT = "action_print";
	static final public String ACTION_PRINT_FLR = "action_print_flr";
	static final public String ACTION_UPDATE = "action_update";
	static final public String ACTION_LEEREN = "action_leeren";
	static final public String ACTION_TEXT = "action_text";

	// pqaddbutton: 0 Das Action Command jedes eigenen Button, der wie Neu
	// geschalten werden soll, muss so beginnen
	static final public String ACTION_MY_OWN_NEW = "action_my_own_new_";

	static final public String ACTION_PREVIOUS = ACTION_MY_OWN_NEW
			+ "action_previous";
	static final public String ACTION_NEXT = ACTION_MY_OWN_NEW + "action_next";

	// btnownupdate: 0 Das Action Command jedes eigenen Button, der enabled sein
	// soll, wenn der Save Button enabled ist, muss so beginnen
	static final public String ACTION_MY_OWN_BUTTON_SIMILAR_TO_SAVE = "action_my_own_button_similar_to_save";

	// Der Button der vom Framework nicht behandelt wird, muss
	// so beginnen
	static final public String LEAVEALONE = "leavealone_";

	// Das Action Command jedes eigenen Button, der immer Enabled sein soll,
	// muss
	// so beginnen
	static final public String ALWAYSENABLED = "alwaysenabled_";

	// posreihung: 0 diese Buttons koennen optional auf einem PanelQuery sitzen
	static final public String ACTION_POSITION_VONNNACHNMINUS1 = "action_position_vonnnachnminus1";
	static final public String ACTION_POSITION_VONNNACHNPLUS1 = "action_position_vonnnachnplus1";
	static final public String ACTION_POSITION_VORPOSITIONEINFUEGEN = ACTION_MY_OWN_NEW
			+ "position_vorpositioneinfuegen";

	// mehrfachselekt: 2 neue actions
	static final public String ACTION_KOPIEREN = "action_kopieren";
	static final public String ACTION_EINFUEGEN_LIKE_NEW = ACTION_MY_OWN_NEW
			+ "action_einfuegen_like_new";

	private boolean isFilterPushed = false;
	private String add2Title = "please set me";

	// wenn ich ein PanelDetail bin dann steht hier der Key drin.
	private Object keyWhenDetailPanel = null;

	public static final String ESC = "ESC";
	public static final String ALT1 = "ALT1";
	public static final String ALT2 = "ALT2";
	public static final String ALT3 = "ALT3";
	public static final String ALT4 = "ALT4";
	public static final String ALT5 = "ALT5";
	public static final String ALT6 = "ALT6";
	public static final String ALT7 = "ALT7";
	public static final String ALT8 = "ALT8";
	public static final String ALT9 = "ALT9";
	public static final String ALTF = "ALTF";
	public static final String ALTR = "ALTR";
	public static final String ALTB = "ALTB";

	private static final Integer IISPEICHERN = new Integer(0);
	private static final Integer IIVERWERFEN = new Integer(1);

	private boolean bHatVersandRecht = false;
	private RequestFocusLater requestFocusLater = new RequestFocusLater();

	final static public JComponent NO_VALUE_THATS_OK_JCOMPONENT = new JLabel();

	/**
	 * PanelBasis
	 *
	 * @param internalFrameI
	 *            InternalFrame
	 * @param addTitleI
	 *            String
	 * @throws Throwable
	 */
	public PanelBasis(InternalFrame internalFrameI, String addTitleI)
			throws Throwable {
		this(internalFrameI, addTitleI, null);
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
	public final boolean handleException(Throwable t, boolean bHandleHardI) {
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

			if (efc.getICode() == EJBExceptionLP.FEHLER_BEIM_LOESCHEN) {
				if (efc.getCause() instanceof EntityExistsException) {

					EntityExistsException ee = (EntityExistsException) efc
							.getCause();

					if (ee.getCause() instanceof ConstraintViolationException) {

						String fk = "";

						ConstraintViolationException ce = (ConstraintViolationException) ee
								.getCause();

						if (ce.getSQLException().getNextException() != null) {

							String s = ce.getSQLException().getNextException()
									.getMessage();
							String[] teile = s.split("\u00BB");
							if (teile.length > 2) {
								ArrayList<String> alInfo = new ArrayList<String>();
								String fkZeile = teile[2];
								fk = fkZeile.substring(0,
										fkZeile.indexOf("\u00AB"))
										.toUpperCase();
								alInfo.add(fk);
								if (teile.length > 3) {
									String table = teile[3].substring(0,
											teile[3].indexOf("\u00AB"));
									alInfo.add(table);
									String iId = teile[3].substring(teile[3]
											.indexOf("(i_id)=(") + 8);
									iId = iId.substring(0, iId.indexOf(")"));
									alInfo.add(iId);
								}

								efc.setAlInfoForTheClient(alInfo);
							} else {
								try {
									teile = s.split("\u201E");

									if (teile.length > 2) {
										ArrayList<String> alInfo = new ArrayList<String>();
										String fkZeile = teile[2];
										fk = fkZeile.substring(0,
												fkZeile.indexOf("\u201C")).toUpperCase();
										alInfo.add(fk);
										efc.setAlInfoForTheClient(alInfo);
									}
								} catch (Throwable e) {
									//Wenn hier was passiert, dann ignorieren
								}
							}

						} else {

							java.sql.SQLException sql = ce.getSQLException();
							String s = sql.getMessage();
							String[] teile = s.split("'");
							if (teile.length > 1) {
								fk = teile[1];
							}
							ArrayList<String> alInfo = new ArrayList<String>();
							alInfo.add(fk);
							efc.setAlInfoForTheClient(alInfo);
						}
					}
				}
			}

			sMsg = LPMain.getInstance().getMsg(efc);
			bErrorBekannt = (sMsg != null);
			if (!bErrorBekannt) {
				// exhc4: Fehlercode wird noch nicht abgefangen
				sMsg = "ExceptionLP, Fehlercode unbekannt: " + efc.getICode();
			}
			if (efc.getICode() == EJBExceptionLP.FEHLER_BEIM_UPDATE) {
				getInternalFrame().fireItemChanged(this,
						ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP);
			}

		}

		if (bErrorBekannt) {
			new DialogError(LPMain.getInstance().getDesktop(), efc,
					DialogError.TYPE_INFORMATION);
			// JOptionPane pane =
			// getInternalFrame().getNarrowOptionPane(Desktop.
			// MAX_CHARACTERS_UNTIL_WORDWRAP);
			// pane.setMessage(sMsg);
			// pane.setMessageType(JOptionPane.ERROR_MESSAGE);
			// JDialog dialog = pane.createDialog(this, "");
			// dialog.show();
		}

		if (!bErrorBekannt && bHandleHardI) {
			// exhc5: Internalframe schliessen.
			LPMain.getInstance().exitFrame(getInternalFrame(), t);
		}

		return bErrorBekannt;
	}

	/**
	 * PanelBasis
	 *
	 * @param internalFrameI
	 *            InternalFrame
	 * @param addTitleI
	 *            String
	 * @param keyWhenDetailPanelI
	 *            Object
	 * @throws Throwable
	 */
	public PanelBasis(InternalFrame internalFrameI, String addTitleI,
			Object keyWhenDetailPanelI) throws Throwable {
		internalFrame = internalFrameI;
		add2Title = addTitleI;
		keyWhenDetailPanel = keyWhenDetailPanelI;
		try {
			setMyDefaults();
			addFocusListener(this);
		} catch (Throwable ex) {
			handleException(ex, true);
		}
	}

	public PanelBasis() {
	}

	/**
	 * Setze alle Defaultwerte; hier koennen jetzt "schwerer" Methoden
	 * aufgerufen werden.
	 *
	 * @todo ppp abstract PJ 4828
	 * @throws Throwable
	 */
	private void setMyDefaults() throws Throwable {

		createAllButtons();

		// dies button sind fix!
		String[] aWhichButtonIUse = {
		// ACTION_CHANGE,
		// ACTION_SAVE,
		// ACTION_DELETE,
		// ACTION_DISCARD,
		ACTION_REFRESH,
		// ACTION_PRINT,
		};

		enableButtonAction(aWhichButtonIUse);
	}

	/**
	 * Basisimplementierung fuer eventYouAreSelected. <br>
	 * Titelleiste des InternalFrame aktualisieren und Status aller Komponenten
	 * schalten. <br>
	 * Subklassen muessen diese Implementierung aufrufen!
	 *
	 * @param bNeedNoYouAreSelectedI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventYouAreSelected(boolean bNeedNoYouAreSelectedI)
			throws Throwable {

		try {
			updateLpTitle();
			LockStateValue lockstateValue = getLockedstateDetailMainKey();
			int iLockState = lockstateValue.getIState();
			if (iLockState == LOCK_IS_LOCKED_BY_ME
					|| iLockState == LOCK_IS_LOCKED_BY_OTHER_USER) {
				// war gelockt; zB. von mir (alter Lock) oder anderen: sperren.
				updateButtons(lockstateValue);
			} else {
				updateButtons(lockstateValue);
			}
			/**
			 * @todo MB->JO hier nicht!!! nur bei new und update PJ 4829
			 */
			setFirstFocusableComponent();
		} catch (Throwable t) {
			handleException(t, true);
		}
	}

	public void updateLpTitle() {
		getInternalFrame().setLpTitle(InternalFrame.TITLE_IDX_OHRWASCHLOBEN,
				add2Title);
	}

	/**
	 * Hole den "Lock" fuer diese Panel.
	 *
	 * @return LockMeDto Was soll gelockt werden.
	 * @throws Exception
	 *             Ausnahme.
	 */
	private LockMeDto getLockMe() throws Exception {

		// QP D Q
		// 12 12 222
		// D
		//
		// getKeyWhenDetailPanel() getInternalFrame().getKeyWasForLockMe()
		// wenn D: hauptkey zb kd=12 hauptkey zb kd=12
		// wenn QD: nebenkey zb kd(=12)|sachberater=222 hauptkey zb kd=12
		// wenn QD: nebenkey zb kd(=12)|sachberater=null hauptkey zb kd=12

		String lockMe = null;
		// erst das nebenpanel, wenn hauptpanel dann hauptpanel==nebenpanel
		if (getKeyWhenDetailPanel() == null) {
			// ist nebenpanel und hat keinen key->leere tabelle
			lockMe = null;
		} else {
			// ist haupt oder nebenpanel
			lockMe = getInternalFrame().getKeyWasForLockMe();
		}

		// lockMeDtoU.getCWas()
		// ==null hauptpanel nebenpanel gesperrt
		// key!=null ==null nein
		// key!=null ==null ja
		boolean bHauptKeyOKNebenKeyNull = (getInternalFrame()
				.getKeyWasForLockMe() != null)
				&& (getKeyWhenDetailPanel() == null);
		if (bHauptKeyOKNebenKeyNull) {
			// hauptkey!=null && nebenkey==null->hauptkey pruefen
			lockMe = getInternalFrame().getKeyWasForLockMe();
		}

		return new LockMeDto(getLockMeWer(), lockMe, LPMain.getInstance()
				.getCNrUser());
	}

	/**
	 * F&uuml;r Welchen Belegtyp ist die Sperre?
	 *
	 * @return null wenn kein Lock, sonst der "Locktyp" HelperClient.LOCKME_*
	 * @throws Exception
	 */
	protected String getLockMeWer() throws Exception {
		throw new Exception("not implemented yet: " + this.getClass().getName()
				+ ": getLockMeWer()");
	}

	/**
	 * Mache einen Button und merke ihn dir.
	 *
	 * @param sIconPathI
	 *            String
	 * @param sTooltipI
	 *            String
	 * @param sActionCommandI
	 *            String
	 * @param rechtCNrI
	 *            String: erforderliches Benutzerrecht. (null = kein
	 *            zusaetzliches Recht erforderlich)
	 * @return JButton
	 */
	private JButton createAndSaveButton(String sIconPathI, String sTooltipI,
			String sActionCommandI, String rechtCNrI) {
		return getToolBar().createAndSaveButton(sIconPathI, sTooltipI,
				sActionCommandI, null, rechtCNrI);
	}

	/**
	 * Mache einen Button und merke ihn dir.
	 *
	 * @param sIconPathI
	 *            String
	 * @param sTooltipI
	 *            String
	 * @param sActionCommandI
	 *            String
	 * @param accelKey
	 *            char
	 * @param rechtCNrI
	 *            String: erforderliches Benutzerrecht. (null = kein
	 *            zusaetzliches Recht erforderlich)
	 * @return JButton
	 */
	protected final JButton createAndSaveButton(String sIconPathI,
			String sTooltipI, String sActionCommandI, KeyStroke accelKey,
			String rechtCNrI) {
		return getToolBar().createAndSaveButton(sIconPathI, sTooltipI,
				sActionCommandI, accelKey, rechtCNrI);
	}

	/**
	 * Einen Button aufgrund seines ActionCommand vom Panel entfernen. <br>
	 * Der Button muss existieren!
	 *
	 * @param ac
	 *            ActionCommand UW->JO
	 * @throws Exception
	 */
	public void removeButton(String ac) throws Exception {
		getToolBar().removeButton(ac);
	}

	/**
	 * Mache einen Button und merke ihn dir.
	 *
	 * @deprecated MB: use createAndSaveAndShowButton(String iconPath, String
	 *             tooltip, String ac, String rechtCNrI)
	 *
	 * @param iconPath
	 *            String
	 * @param tooltip
	 *            String
	 * @param ac
	 *            String
	 * @throws Exception
	 */
	public void createAndSaveAndShowButton(String iconPath, String tooltip,
			String ac) throws Exception {
		createAndSaveAndShowButton(iconPath, tooltip, ac, null, null);
	}

	/**
	 * Mache einen Button und f&uuml;ge ihn links im ToolsPanel hinzu.
	 *
	 * @param rechtCNrI
	 *            String: erforderliches Benutzerrecht. (null = kein
	 *            zusaetzliches Recht erforderlich)
	 * @throws Exception
	 */
	public void createAndSaveAndShowButton(String iconPath, String tooltip,
			String ac, String rechtCNrI) throws Exception {
		createAndSaveAndShowButton(iconPath, tooltip, ac, null, rechtCNrI);
	}

	/**
	 * Mache einen Button und f&uuml;ge ihn links im ToolsPanel hinzu.
	 *
	 * @param rechtCNrI
	 *            String: erforderliches Benutzerrecht. (null = kein
	 *            zusaetzliches Recht erforderlich)
	 * @throws Exception
	 */
	public void createAndSaveAndShowButton(String iconPath, String tooltip,
			String ac, KeyStroke accelKey, String rechtCNrI) throws Exception {
		getToolBar().addButtonLeft(iconPath, tooltip, ac, accelKey, rechtCNrI);
	}

	/**
	 * btnstate: 5 Enable, disable diese Panelbuttons je nach Lockstate dieses
	 * (this) Panels.
	 *
	 * @throws Throwable
	 */
	public void updateButtons() throws Throwable {
		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		updateButtons(lockstateValue);
	}

	public void updateButtons(boolean bHatVersandRecht) throws Throwable {
		this.bHatVersandRecht = bHatVersandRecht;
		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		updateButtons(lockstateValue);
	}

	/**
	 * btnstate: 6 Enable, disable diese Panelbuttons je nach lockstateValueI.
	 *
	 * @param lockstateValueI
	 *            String, wer lockt?
	 * @throws Throwable
	 */
	public void updateButtons(LockStateValue lockstateValueI) throws Throwable {

		// precondition
		if (getInternalFrame().getRechtModulweit() == null) {
			myLogger.error("getInternalFrame().getRechtModulweit() == null");
		}

		int iLockstate = lockstateValueI.getIState();

		// Rechte
		if (getInternalFrame().getRechtModulweit().equals(
				RechteFac.RECHT_MODULWEIT_READ)) {
			if (!bHatVersandRecht)
				iLockstate = LOCK_ENABLE_REFRESHANDPRINT_ONLY;
			setStatusbarSpalte5(LPMain
					.getTextRespectUISPr("system.nurleserecht"));
		}

		Collection<?> buttons = getHmOfButtons().values();

		if (iLockstate == LOCK_FOR_EMPTY) {
			// emptytable: 5 an dieser Stelle steht im Detail eines PanelSplit
			// fest,
			// dass es keinen Eintrag gibt; alle Elemente des Panels disabled
			enableAllComponents(this, false);
			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();
				if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
					// es ist kein leave me alone button
					item.getButton().setEnabled(false);
				}
			}
		}

		else if (iLockstate == LOCK_FOR_NEW
				|| iLockstate == LOCK_IS_LOCKED_BY_ME) {
			// lock und disable "alles", user will was aenderen.
			setStatusbar(lockstateValueI, null);
			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();
				if (item.getButton().getActionCommand().equals(ACTION_SAVE)
						|| item.getButton().getActionCommand()
								.equals(ACTION_DISCARD)
						|| item.getButton().getActionCommand()
								.equals(ACTION_TEXT)
						|| item.getButton().getActionCommand()
								.indexOf(ACTION_MY_OWN_BUTTON_SIMILAR_TO_SAVE) != -1) {
					item.getButton().setEnabled(true);
				} else {
					if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button
						item.getButton().setEnabled(false);
					}
				}
			}
			enableAllComponents(this, true);
			getInternalFrame().enableAllPanelsExcept(false);
		}

		else if (iLockstate == LOCK_IS_LOCKED_BY_OTHER_USER) {
			// user darf nur schauen.
			setStatusbar(lockstateValueI, null);

			enableAllComponents(this, false);
			getInternalFrame().enableAllPanelsExcept(true);

			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();
				if (item.getButton().getActionCommand().equals(ACTION_REFRESH)
						|| item.getButton().getActionCommand()
								.equals(ACTION_PRINT)
						|| item.getButton().getActionCommand()
								.equals(ACTION_TEXT)
						|| item.getButton().getActionCommand()
								.equals(ACTION_NEXT)
						|| item.getButton().getActionCommand()
								.equals(ACTION_PREVIOUS)) {
					item.getButton().setEnabled(true);
				} else {
					if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button
						item.getButton().setEnabled(false);
					}
				}
			}
			// Hier war die Originalposition
			/*
			 * enableAllComponents(this, false);
			 * getInternalFrame().enableAllPanelsExcept(true);
			 */
		}

		else if (iLockstate == LOCK_IS_NOT_LOCKED) {
			// user schaut nur.

			// eventuell gesperrt text leeren.
			getPanelStatusbar().setLockField(null);

			enableAllComponents(this, false);
			getInternalFrame().enableAllPanelsExcept(true);

			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();
				if (item.getButton().getActionCommand().equals(ACTION_UPDATE)
						|| item.getButton().getActionCommand()
								.equals(ACTION_REFRESH)
						|| item.getButton().getActionCommand()
								.equals(ACTION_PRINT)
						|| item.getButton().getActionCommand()
								.startsWith(ACTION_MY_OWN_NEW)
						|| item.getButton().getActionCommand()
								.equals(ACTION_TEXT)
						|| item.getButton().getActionCommand()
								.equals(ACTION_NEXT)
						|| item.getButton().getActionCommand()
								.equals(ACTION_PREVIOUS)) {
					item.getButton().setEnabled(true);
				}
				// @uw alle special buttons freigeben WORKAROUND
				else if (item.getButton().getActionCommand()
						.indexOf("_special_") != -1) {
					item.getButton().setEnabled(true);
				} else {
					if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
						// es ist kein leave me alone button
						item.getButton().setEnabled(false);
					}
				}
				if (item.getButton().getActionCommand().equals(ACTION_DELETE)) {
					item.getButton().setEnabled(true);
				}
			}
			/*
			 * enableAllComponents(this, false);
			 * getInternalFrame().enableAllPanelsExcept(true);
			 */
		}

		else if (iLockstate == LOCK_NO_LOCKING) {
			// kein Locking
			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();
				if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
					// es ist kein leave me alone button
					item.getButton().setEnabled(true);
				}
			}
			enableAllComponents(this, true);

			// UW->JO->MB frage Hotfix fuer Deploy darf bei PanelDialog nicht
			// aufgerufen werden
			if (this instanceof PanelDialog) {
				// do nothing
			} else {
				getInternalFrame().enableAllPanelsExcept(false);
			}
		}

		else if (iLockstate == LOCK_ENABLE_REFRESHANDPRINT_ONLY) {
			enableAllComponents(this, false);
			getInternalFrame().enableAllPanelsExcept(true);

			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();
				if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
					// es ist kein leave me alone button

					if (item.getButton().getActionCommand()
							.indexOf(ACTION_REFRESH) > -1
							|| item.getButton().getActionCommand()
									.indexOf(ACTION_PRINT) > -1
							|| item.getButton().getActionCommand()
									.indexOf(ACTION_NEXT) > -1
							|| item.getButton().getActionCommand()
									.indexOf(ACTION_PREVIOUS) > -1) {
						// es ist ein Refresh button
						item.getButton().setEnabled(true);
					} else {
						// es ist kein Refresh button
						item.getButton().setEnabled(false);
					}
				}
			}
		}

		else if (iLockstate == LOCK_ENABLE_REFRESHANDUPDATE_ONLY) {
			enableAllComponents(this, false);
			getInternalFrame().enableAllPanelsExcept(true);

			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();
				if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
					// es ist kein leave me alone button

					if (item.getButton().getActionCommand()
							.indexOf(ACTION_REFRESH) > -1
							|| item.getButton().getActionCommand()
									.indexOf(ACTION_UPDATE) > -1) {
						// es ist ein Refresh button
						item.getButton().setEnabled(true);
					} else {
						// es ist kein Refresh button
						item.getButton().setEnabled(false);
					}
				}
			}
		} else if (iLockstate == LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY) {
			enableAllComponents(this, false);
			getInternalFrame().enableAllPanelsExcept(true);

			for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
				LPButtonAction item = (LPButtonAction) iter.next();
				if (item.getButton().getActionCommand().indexOf(LEAVEALONE) == -1) {
					// es ist kein leave me alone button

					if (item.getButton().getActionCommand()
							.indexOf(ACTION_REFRESH) > -1
							|| item.getButton().getActionCommand()
									.indexOf(ACTION_UPDATE) > -1
							|| item.getButton().getActionCommand()
									.indexOf(ACTION_PRINT) > -1) {
						// es ist ein Refresh button
						item.getButton().setEnabled(true);
					} else {
						// es ist kein Refresh button
						item.getButton().setEnabled(false);
					}
				}
			}
		} else {
			throw new Throwable("wrong lockstate: " + iLockstate);
		}

		// alwaysenabled: 2 hier werden sie immer auf sichtbar gesetzt
		updateButtonsAlwaysEnabled(buttons);
	}

	protected void updateButtonsAlwaysEnabled(Collection<?> buttons) {
		for (Iterator<?> iter = buttons.iterator(); iter.hasNext();) {
			LPButtonAction item = (LPButtonAction) iter.next();
			String actionCommand = item.getButton().getActionCommand();
			if (actionCommand.indexOf(ALWAYSENABLED) != -1) {
				item.getButton().setEnabled(true);
			}
		}
	}

	private void setStatusbar(LockStateValue lockstate, Integer idPersonal)
			throws Exception, Throwable {

		if ((getLockMe().getCWas().equals(LPMain.getLockMeForNew()))) {
			idPersonal = LPMain.getTheClient().getIDPersonal();
		} else if (lockstate != null && (lockstate.getALockMeDto() != null)
				&& (lockstate.getALockMeDto().length > 0)) {
			idPersonal = lockstate.getALockMeDto()[0].getPersonalIIdLocker();
		}

		if (idPersonal != null) {
			PersonalDto personalDto = DelegateFactory.getInstance()
					.getPersonalDelegate().personalFindByPrimaryKey(idPersonal);

			String sLock = ((personalDto.getCKurzzeichen() == null) ? ""
					: personalDto.getCKurzzeichen())
					+ " ("
					+ personalDto.getPartnerDto()
							.getCName1nachnamefirmazeile1() + ")";
			getPanelStatusbar().setLockField(sLock);
		}
	}

	public boolean isLockedDlg() throws Throwable {
		boolean isLocked = false;

		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		int iLockstate = lockstateValue.getIState();

		if (iLockstate == LOCK_IS_LOCKED_BY_ME
				|| iLockstate == LOCK_IS_LOCKED_BY_OTHER_USER) {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("lp.system.locked.text"));
			isLocked = true;
			updateButtons(lockstateValue);
		}

		return isLocked;
	}

	protected boolean isLocked() throws Throwable {
		boolean isLocked = false;

		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		int iLockstate = lockstateValue.getIState();

		if (iLockstate == LOCK_IS_LOCKED_BY_ME
				|| iLockstate == LOCK_IS_LOCKED_BY_OTHER_USER) {
			isLocked = true;
		}

		return isLocked;
	}

	protected boolean isNotLocked() throws Throwable {
		boolean isNotLocked = true;

		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		int iLockstate = lockstateValue.getIState();

		if (iLockstate == LOCK_IS_LOCKED_BY_OTHER_USER
		/* || iLockstate == LOCK_IS_LOCKED_BY_ME */) {
			isNotLocked = false;
		}

		return isNotLocked;
	}

	public LockStateValue getLockedstateDetailMainKey() throws Throwable {

		LockMeDto lockMeDtoU = getLockMe();
		LockMeDto[] aLockMeDto = null;
		if (lockMeDtoU != null) {
			aLockMeDto = DelegateFactory.getInstance().getTheJudgeDelegate()
					.theJudgeFindByWerWas(lockMeDtoU);

			boolean bHauptKeyOKNebenKeyNull = (getInternalFrame()
					.getKeyWasForLockMe() != null)
					&& (getKeyWhenDetailPanel() == null);
			if (bHauptKeyOKNebenKeyNull) {
				// hauptkey!=null && nebenkey==null->hauptkey pruefen
				if (aLockMeDto.length == 0) {
					// im panel ist der aktuelle key null
					lockMeDtoU = null;
				}
			}

		}
		int iState = 0;

		iState = LOCK_IS_NOT_LOCKED;
		boolean bLockOK = (lockMeDtoU != null && lockMeDtoU.getCWas() != null);

		if (bLockOK) {
			if (bLockOK
					&& (lockMeDtoU.getCWas().equals(LPMain.getLockMeForNew()))) {
				iState = LOCK_FOR_NEW;
			} else {
				if (aLockMeDto != null && aLockMeDto.length > 0) {
					if (aLockMeDto.length > 1) {
						iState = LOCK_IS_LOCKED_BY_OTHER_USER;
					}
					if (aLockMeDto[0].getPersonalIIdLocker().equals(
							LPMain.getTheClient().getIDPersonal())
							&& aLockMeDto[0].getCUsernr().startsWith(
									LPMain.getInstance().getCNrUser())) {
						iState = LOCK_IS_LOCKED_BY_ME;
					} else {
						iState = LOCK_IS_LOCKED_BY_OTHER_USER;
					}
				}
			}
		} else {
			// im panel ist der aktuelle key null
			iState = LOCK_FOR_EMPTY;
		}
		return new LockStateValue(lockMeDtoU, aLockMeDto, iState);
	}

	/**
	 * Aktiviere aWhichButtons Buttons.
	 *
	 * @param aWhichButtons
	 *            String[]; zB. ACTION_SAVE, ACTION_LOCK
	 * @throws Throwable
	 */
	protected void enableButtonAction(String[] aWhichButtons) throws Throwable {
		getToolBar().enableButtonAction(aWhichButtons);
	}

	/**
	 * Aktiviere aWhichButtons Buttons.
	 *
	 * @param aWhichButtons
	 *            String[]; zB. ACTION_SAVE, ACTION_LOCK
	 * @throws ExceptionForLPClients
	 * @throws Exception
	 */
	protected void enableToolsPanelButtons(String[] aWhichButtons)
			throws Exception {
		getToolBar().enableToolsPanelButtons(aWhichButtons);
	}

	/**
	 * Enable oder disable Buttons, auch solche, die nicht LEAVEALONE sind!
	 *
	 * @param enable
	 * @param which
	 *            die ActionCommands der Buttons
	 */
	public void enableToolsPanelButtons(boolean enable, String... which) {
		getToolBar().enableToolsPanelButtons(enable, which);
	}

	/**
	 * Enable oder disable der LeaveAlone-Buttons.
	 *
	 * @param aButtonStringI
	 *            die Bezeichnungen der Buttons
	 * @param bEnableI
	 *            boolean enable oder disble
	 * @throws Exception
	 */
	public void enableToolsPanelLeaveAloneButtons(String[] aButtonStringI,
			boolean bEnableI) throws Exception {
		getToolBar()
				.enableToolsPanelLeaveAloneButtons(aButtonStringI, bEnableI);
	}

	/**
	 * Mache einen Button.
	 *
	 * @param iconPath
	 *            String
	 * @param tooltip
	 *            String
	 * @param ac
	 *            String
	 * @return JButton
	 */
	public JButton createButtonActionListenerThis(String iconPath,
			String tooltip, String ac) {
		ImageIcon ii = new ImageIcon(getClass().getResource(iconPath));

		JButton button = HelperClient.createButton(ii, tooltip, ac);

		button.addActionListener(this);
		return button;
	}

	public WrapperButton createWrapperButtonActionListenerThis(String iconPath,
			String tooltip, String ac) {
		ImageIcon ii = new ImageIcon(getClass().getResource(iconPath));

		WrapperButton button = HelperClient
				.createWrapperButton(ii, tooltip, ac);

		button.addActionListener(this);
		return button;
	}

	private void setIconFilterPushed(boolean locked) {
		String pfadName = "/com/lp/client/res/funnel.png";
		if (isFilterPushed) {
			pfadName = "/com/lp/client/res/funnel_add.png";
		}
		ImageIcon ii = new ImageIcon(getClass().getResource(pfadName));
		((LPButtonAction) getHmOfButtons().get(ACTION_FILTER)).getButton()
				.setIcon(ii);
	}

	protected void eventActionUnlock(ActionEvent e) throws Throwable {
		if (getLockMe() != null) {
			if (getLockMe().getCWas().equals(LPMain.getLockMeForNew())) {
				getInternalFrame().setKeyWasForLockMe(
						getInternalFrame().getKeyWasForLockMeOld());
			} else {
				unlock(getLockMe());
			}
			getPanelStatusbar().setLockField(null);
		}
	}

	protected void eventActionLock(ActionEvent e) throws Throwable {
		if (getLockMe() != null) {
			if (getLockMe().getCWas() != null) {
				if (!getLockMe().getCWas().equals(LPMain.getLockMeForNew())) {
					lock(getLockMe());
				}
			}
		}
	}

	protected void lock(LockMeDto lockMeDtoI) throws Throwable {
		myLogger.debug(lockMeDtoI);
		DelegateFactory.getInstance().getTheJudgeDelegate()
				.addLockedObject(lockMeDtoI);
		getInternalFrame().lock() ;
	}

	protected void unlock(LockMeDto lockMeDtoI) throws Throwable {
		myLogger.debug(lockMeDtoI);
		DelegateFactory.getInstance().getTheJudgeDelegate()
				.removeLockedObject(lockMeDtoI);
		getInternalFrame().unlock() ;
	}

	protected int getLockedByWerWas(LockMeDto lockMeDtoI) throws Throwable {
		int iLockstate = LOCK_IS_NOT_LOCKED;
		LockMeDto[] aLockMes = ((DelegateFactory.getInstance()
				.getTheJudgeDelegate().theJudgeFindByWerWas(lockMeDtoI)));
		if (aLockMes.length > 0) {
			if (aLockMes[0].getCUsernr().startsWith(
					LPMain.getInstance().getCNrUser())) {
				iLockstate = LOCK_IS_LOCKED_BY_ME;
			} else {
				iLockstate = LOCK_IS_LOCKED_BY_OTHER_USER;
			}
		}

		return iLockstate;
	}

	/**
	 * eventActionSpecial
	 *
	 * @param e
	 *            ActionEvent
	 * @throws Throwable
	 */
	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		LPMain.getInstance().exitFrame(getInternalFrame());
	}

	protected void eventActionDelete(ActionEvent e,
			boolean bAdministrateLockKeyI, boolean bNeedNoDeleteI)
			throws Throwable {

		if (bAdministrateLockKeyI) {
			internalFrame.setKeyWasForLockMe(null);
		}

		getInternalFrame().fireItemChanged(this,
				ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP);
	}

	/**
	 * Refreshen des Panels, falls noetig.
	 *
	 * @param e
	 *            ActionEvent
	 * @param bNeedNoRefreshI
	 *            boolean
	 * @throws Throwable
	 */
	protected void eventActionRefresh(ActionEvent e, boolean bNeedNoRefreshI)
			throws Throwable {

		// evtref: 1 gegebenenfalls updaten
		if (!bNeedNoRefreshI) {
			eventYouAreSelected(bNeedNoRefreshI);
		}

		// evtref: 2 alle components updaten
		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		if (lockstateValue.getIState() == LOCK_IS_LOCKED_BY_ME) {
			/** @todo JO->JO obsolet? PJ 4830 */
			lockstateValue.setIState(LOCK_IS_LOCKED_BY_OTHER_USER);
			updateButtons(lockstateValue);
		} else {
			updateButtons(lockstateValue);
		}
	}

	/**
	 * Verwerfen der aktuelle Usereingabe und zurueckgehen auf den bestehenden
	 * Datensatz, wenn einer existiert.
	 *
	 * @param e
	 *            Ereignis
	 * @throws Throwable
	 */
	protected void eventActionDiscard(ActionEvent e) throws Throwable {

		Object[] options = { LPMain.getTextRespectUISPr("lp.ja"),
				LPMain.getTextRespectUISPr("lp.nein") };
		if ((JOptionPane.showOptionDialog(this,
				LPMain.getTextRespectUISPr("lp.verwerfen.text"), "",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0])) == JOptionPane.YES_OPTION) {
			discard();
		}
	}

	protected void doActionDiscard() throws Throwable {
		discard();
	}

	protected void eventActionText(ActionEvent e) throws Throwable {

	}

	protected void eventActionNext(boolean next) throws Throwable {

		if (getInternalFrame().tabbedPaneRoot.getSelectedComponent() instanceof TabbedPane) {
			TabbedPane tp = (TabbedPane) getInternalFrame().tabbedPaneRoot
					.getSelectedComponent();
			if (tp.getComponentAt(0) instanceof PanelQuery) {
				PanelQuery pq = (PanelQuery) tp.getComponentAt(0);
				Object o = null;

				if (next == true) {
					o = pq.holeKeyNaechsteZeile();
				} else {
					o = pq.holeKeyVorherigeZeile();
				}

				if (o != null) {
					pq.setSelectedId(o);
					tp.lPEventItemChanged(new ItemChangedEvent(pq,
							ItemChangedEvent.ITEM_CHANGED));
					this.eventYouAreSelected(false);

					if (tp.getSelectedComponent() instanceof PanelSplit) {
						tp.lPEventObjectChanged(null);
					}

				}
			}

		}

	}

	private void discard() throws Throwable {
		// Lock entfernen
		eventActionUnlock(null);
		// Wieder alle Panels aktivieren
		getInternalFrame().enableAllPanelsExcept(true);
		// Lockstatus
		updateButtons(getLockedstateDetailMainKey());
		if (getInternalFrame().getKeyWasForLockMeOld() != null
				&& getInternalFrame().getKeyWasForLockMeOld().equals(
						LPMain.getLockMeForNew())) {
			// Discard nach Neu ohne Lock.
			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP);
		} else {
			// Es war ein Eintrag gesperrt.
			getInternalFrame().fireItemChanged(this,
					ItemChangedEvent.ACTION_DISCARD);
		}
		// refresh
		eventYouAreSelected(false);
	}

	/**
	 * Behandle Ereignis New.
	 *
	 * @param eventObject
	 *            Ereignis.
	 * @param bAdministrateLockKeyI
	 *            true ... habe den "dicken/haupt" key; false ... zb.
	 *            "nebenpanel"
	 * @param bNeedNoNewI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionNew(EventObject eventObject,
			boolean bAdministrateLockKeyI, boolean bNeedNoNewI)
			throws Throwable {

		if (bAdministrateLockKeyI) {
			internalFrame.setKeyWasForLockMe(LPMain.getLockMeForNew());
		}
		setKeyWhenDetailPanel(LPMain.getLockMeForNew());
		eventActionLock(null);
	}

	public boolean allMandatoryFieldsSetDlg() throws Throwable {
		boolean bOKSave = true;
		// if (!allMandatorySet(this)) {
		Component c = allMandatoryComponentSet(this);
		if (c != null) {
			showDialogPflichtfelderAusfuellen();
			bOKSave = false;
		}
		return bOKSave;
	}

	public void showDialogPflichtfelderAusfuellen() {
		DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"),
				LPMain.getTextRespectUISPr("lp.pflichtfelder.ausfuellen"));
	}

	protected void checkLockedDlg(LockMeDto lmI) throws Throwable {
		if (!lmI.getCWas().equals(LPMain.getLockMeForNew())) {
			// kein Neuer.
			if (getLockedByWerWas(lmI) == LOCK_IS_NOT_LOCKED) {
				// nicht mehr gelockt!
				LockStateValue lockstateValue = getLockedstateDetailMainKey();
				lockstateValue.setIState(LOCK_IS_LOCKED_BY_OTHER_USER);
				updateButtons(lockstateValue);

				// @uw man muss unterscheiden zwischen
				// discard nach einem neu button und discard nach einem aendern
				// button
				if (getInternalFrame().getKeyWasForLockMe() == null) {
					getInternalFrame().enableAllPanelsExcept(true);

					// wenn die tabelle leer ist oder zuvor der neu button
					// gedrueckt wurde
					// ev. auf das default panel springen
					getInternalFrame().fireItemChanged(this,
							ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP);
				} else {
					getInternalFrame().fireItemChanged(this,
							ItemChangedEvent.ACTION_DISCARD);
				}

				throw new ExceptionLP(EJBExceptionLP.FEHLER_LOCK_NOTFOUND, null);
			}
		}
	}

	protected void checkLockedDlg() throws Throwable {
		checkLockedDlg(getLockMe());
	}

	/**
	 * btnsave: 0 behandle das ereignis save.
	 *
	 * @param e
	 *            ActionEvent der Event.
	 * @param bNeedNoSaveI
	 *            boolean
	 * @throws Throwable
	 */
	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {

		eventActionUnlock(e);

		// andere zb. oberes QP informieren fuer refresh.
		getInternalFrame().fireItemChanged(this, ItemChangedEvent.ACTION_SAVE);

		// buttons updaten.
		LockStateValue lockstateValue = getLockedstateDetailMainKey();
		lockstateValue.setIState(LOCK_IS_NOT_LOCKED);
		updateButtons(lockstateValue);

		if (getInternalFrame().getKeyWasForLockMe() == null) {
			// der erste eintrag wurde angelegt
			getInternalFrame().setKeyWasForLockMe(getKeyWhenDetailPanel() + "");
		}
		/**
		 * @todo MB->JO nach dem speichern muss der focus weg (sonst ists vorbei
		 *       mit der tastaturbedienung) PJ 4838 am besten ins panelSplit
		 *       oder auf die buttonleiste
		 */
	}

	protected void eventActionPrint(ActionEvent e) throws Throwable {
		LPMain.getInstance().exitFrame(getInternalFrame());
	}

	protected void eventActionPrint(HvActionEvent e) throws Throwable {
		// fuehrt einen cast von HvActionEvent auf ActionEvent durch und
		// ruft default eventActionPrint(ActionEvent e) auf,
		// wenn im aufgerufenen Modul eventActionPrint(HvActionEvent e)
		// nicht implementiert ist
		eventActionPrint((ActionEvent) e) ;

//		LPMain.getInstance().exitFrame(getInternalFrame());
	}

	protected void eventActionFilter(ActionEvent e) throws Throwable {
		isFilterPushed = (!isFilterPushed);
		setIconFilterPushed(isFilterPushed);
	}

	protected void eventActionEscape(ActionEvent e) throws Throwable {
		getInternalFrame()
				.fireItemChanged(this, ItemChangedEvent.ACTION_ESCAPE);
	}

	protected void eventActionAlt(ActionEvent e) throws Throwable {
		// e.getModifiers()
		getInternalFrame().fireItemChanged(this, 24);
	}

	public void update() throws Throwable {
		eventActionUpdate(null, false);
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		if (bNeedNoUpdateI) {
			return;
		}
		int iLockstate = getLockedstateDetailMainKey().getIState();
		if (iLockstate == LOCK_IS_NOT_LOCKED
				||
				// in diesen Faellen ein echtes update zulassen
				iLockstate == LOCK_ENABLE_REFRESHANDUPDATEANDPRINT_ONLY
				|| iLockstate == LOCK_ENABLE_REFRESHANDUPDATE_ONLY) {
			// MB: nocheinmal ein refresh. (der datensatz muss bereits gelockt
			// sein)
			// damit werden die aktuellen Daten angezeigt. Solange der Lock
			// besteht,
			// kann kein anderer User die Daten veraendern.
			eventYouAreSelected(false);
			/**
			 * @todo MB->MB eigentlich sollte erst nach dem Locken refresht
			 *       werden aber, dann funktionieren die PanelSplit-FLR's nicht
			 *       mehr richtig ... keine ahnung wieso
			 */

			eventActionLock(null);
			// Lockstate setzen und Buttons schalten.
			LockStateValue lockstateValue = getLockedstateDetailMainKey();
			lockstateValue.setIState(LOCK_IS_LOCKED_BY_ME);
			updateButtons(lockstateValue);
			setFirstFocusableComponent();
		} else {
			DialogFactory.showModalDialog(
					LPMain.getTextRespectUISPr("lp.hint"),
					LPMain.getTextRespectUISPr("lp.system.locked.text"));

			LockStateValue lockstateValue = getLockedstateDetailMainKey();
			lockstateValue
					.setIState((iLockstate == LOCK_IS_LOCKED_BY_ME) ? LOCK_IS_LOCKED_BY_OTHER_USER
							: iLockstate);
			updateButtons(lockstateValue);
		}

		// btnupd: andere informieren.
		getInternalFrame()
				.fireItemChanged(this, ItemChangedEvent.ACTION_UPDATE);
	}

	protected void eventActionLeeren(ActionEvent e) throws Throwable {
		LPMain.getInstance().exitFrame(getInternalFrame());
	}

	/**
	 * Fange alle ActionEvents; Nur diese Klasse faengt Events, diese werden
	 * dann umgeleitet ...
	 *
	 * @param e
	 *            ActionEvent
	 */
	final public void actionPerformed(ActionEvent e) {
		myLogger.warn("actionPerformed (start): " + e.paramString());
		actionPerformedLog(e);
		this.isEnabled();
		myLogger.warn("actionPerformed (stop): " + e.paramString());
	}

	/**
	 * Hole erste Feld, welches den Focus uebernimmt.
	 *
	 * @return Component
	 * @throws Exception
	 */
	// protected abstract JComponent getFirstFocusableComponent() throws
	// Exception;
	protected JComponent getFirstFocusableComponent() throws Exception {
		return null;
	}

	/**
	 * Setze den Eingabefocus auf die erste Component.
	 *
	 * @throws Exception
	 */
	public final void setFirstFocusableComponent() throws Exception {
		Component component = getFirstFocusableComponent();
		if (component == NO_VALUE_THATS_OK_JCOMPONENT) {
			// alles OK keine aktion noetig
		} else if (component == null) {
			myLogger.error("setFirstFocusableComponent(): component != null");
		} else {
			// VF
			// if(!component.requestFocusInWindow()) {
			// // MB: Workaround fuer java bug 5089436
			// // see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5089436
			// SwingUtilities.invokeLater(new RequestFocusLater(component));
			// }
			// SwingUtilities.invokeLater(new RequestFocusLater(component));
			SwingUtilities.invokeLater(getRequestFocusLater(component));
		}
	}

	private RequestFocusLater getRequestFocusLater(Component focusComponent) {
		// if(requestFocusLater == null) {
		// requestFocusLater = new RequestFocusLater() ;
		// }
		requestFocusLater.setComponent(focusComponent);
		return requestFocusLater;
	}

	private class RequestFocusLater implements Runnable {
		private Component comp;

		// public RequestFocusLater() {
		// }
		//
		// public RequestFocusLater(Component src) {
		// setComponent(src);
		// }

		public void setComponent(Component src) {
			comp = src;
		}

		public void run() {
			comp.requestFocusInWindow();
		}
	}

	/**
	 * Verteile <b>alle</b> eingehenden Events; ist die einzige Stelle!
	 *
	 * @param e
	 *            ActionEvent
	 */
	private void actionPerformedLog(EventObject e) {
		// long tStart = System.currentTimeMillis();
		try {
			// housekeeping
			// myLogger.info("action start: " + Helper.cutString(e.toString(),
			// Defaults.LOG_LENGTH));

			getInternalFrame().getFrameProgress().start(
					LPMain.getTextRespectUISPr("lp.working"));
			if (Defaults.getInstance().isUseWaitCursor()) {
				getInternalFrame()
						.setCursor(
								java.awt.Cursor
										.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
			}
			// setEnabled(false);

			if (e instanceof ActionEvent) {
				myLogger.warn("actionPerformedLog (ActionEvent) "
						+ ((ActionEvent) e).paramString());
				performActionEvents(new HvActionEvent((ActionEvent) e));
			} else if (e instanceof MouseEvent) {
				myLogger.warn("actionPerformedLog (MouseEvent) "
						+ ((MouseEvent) e).paramString());
				performMouseEvents((MouseEvent) e);
			} else if (e instanceof ItemChangedEvent) {
				ItemChangedEvent ie = (ItemChangedEvent) e;
				myLogger.warn("actionPerformedLog (ItemChangedEvent) "
						+ ie.paramString() + " " + ie.getSource());
				// itemevt: 8 Jeder Itemevent kommt hier rueber, wegen logging,
				// locking, ...
				performItemChangedEvents((ItemChangedEvent) e);
			} else if (e instanceof KeyEvent) {
				myLogger.warn("actionPerformedLog (KeyEvent) "
						+ ((KeyEvent) e).paramString());
				performKeyEvents((KeyEvent) e);
			} else if (e == null) {
				myLogger.warn("actionPerformedLog (eventYourAreSelected) ");
				eventYouAreSelected(false);
			} else {
				LPMain.getInstance().exitFrame(getInternalFrame());
			}
		} catch (Throwable t) {
			// housekeeping
			getInternalFrame().getFrameProgress().stop();
			// getInternalFrame().setCursor(Cursor.getPredefinedCursor(Cursor.
			// DEFAULT_CURSOR));
			// setEnabled(true);
			// long tEnd = System.currentTimeMillis();
			// myLogger.info("action end: " + (tEnd - tStart) + " [ms]");
			handleException(t, true);
		} finally {
			// housekeeping
			getInternalFrame().getFrameProgress().pause();
			if (Defaults.getInstance().isUseWaitCursor()) {
				getInternalFrame().setCursor(
						Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			// setEnabled(true);
			// long tEnd = System.currentTimeMillis();
			// myLogger.info("action end: " + (tEnd - tStart) + " [ms]");
		}
	}

	/**
	 * performKeyEvents
	 *
	 * @param e
	 *            KeyEvent
	 * @throws Throwable
	 */
	private void performKeyEvents(KeyEvent e) throws Throwable {

		if (e.getID() == KeyEvent.KEY_PRESSED) {
			eventKeyPressed(e);
		} else if (e.getID() == KeyEvent.KEY_RELEASED) {
			eventKeyReleased(e);
		} else if (e.getID() == KeyEvent.KEY_TYPED) {
			eventKeyTyped(e);
		}
	}

	/**
	 * eventKeyTyped
	 *
	 * @param e
	 *            KeyEvent
	 * @throws Throwable
	 */
	protected void eventKeyTyped(KeyEvent e) throws Throwable {
		LPMain.getInstance().exitFrame(getInternalFrame());
	}

	/**
	 * eventKeyReleased
	 *
	 * @param e
	 *            KeyEvent
	 * @throws Throwable
	 */
	protected void eventKeyReleased(KeyEvent e) throws Throwable {
		LPMain.getInstance().exitFrame(getInternalFrame());
	}

	/**
	 * eventKeyPressed
	 *
	 * @param e
	 *            KeyEvent
	 * @throws Throwable
	 */
	protected void eventKeyPressed(KeyEvent e) throws Throwable {
		LPMain.getInstance().exitFrame(getInternalFrame());
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			setVisible(false);
			System.exit(0);
		}
	}

	/**
	 * itemevt: 9 Fuehre den Itemevent aus.
	 *
	 * @param e
	 *            ItemChangedEvent
	 * @throws Throwable
	 */
	protected void performItemChangedEvents(ItemChangedEvent e)
			throws Throwable {
		try {
			eventItemchanged(e);
		} catch (Throwable t) {
			handleException(t, true);
		}
	}

	/**
	 * itemevt: 10 Informiere ueber Itemevent; ableiten.
	 *
	 * @param eI
	 *            EventObject
	 * @throws Throwable
	 */
	protected void eventItemchanged(EventObject eI) throws Throwable {
		// myLogger.error("**E: eventItemchanged ableiten!");
		// LPMain.getInstance().exitFrame(getInternalFrame());

		// -->Kommt raus lt. Besptrechung vom 16.4.2012
	}

	/**
	 * eventMouseEntered.
	 *
	 * @param e
	 *            MouseEvent
	 * @throws Throwable
	 */
	private void eventMouseEntered(MouseEvent e) throws Throwable {
		LPMain.getInstance().exitFrame(getInternalFrame());
	}

	/**
	 * eventMouseClicked.
	 *
	 * @param e
	 *            MouseEvent
	 * @throws Throwable
	 */
	protected void eventMouseClicked(MouseEvent me) throws Throwable {
		if(me.getSource() instanceof JButton) {
			performActionEvents(new HvActionEvent(me));
			return ;
		}
		LPMain.getInstance().exitFrame(getInternalFrame());
	}

	/**
	 * performMouseEvents
	 *
	 * @param me
	 *            MouseEvent
	 * @throws Throwable
	 */
	private void performMouseEvents(MouseEvent me) throws Throwable {
		// hier werden wir noch erweitern.
		if (me.getID() == MouseEvent.MOUSE_ENTERED) {
			eventMouseEntered(me);
		} else if (me.getID() == MouseEvent.MOUSE_CLICKED) {
			eventMouseClicked(me);
		} else if (me.getID() == MouseEvent.MOUSE_EXITED) {
			eventMouseExited(me);
		} else if (me.getID() == MouseEvent.MOUSE_PRESSED) {
			eventMousePressed(me);
		} else if (me.getID() == MouseEvent.MOUSE_RELEASED) {
			eventMouseReleased(me);
		}
	}

	private void eventMouseReleased(MouseEvent me) {
		// bei Bedarf implementieren.
	}

	private void eventMousePressed(MouseEvent me) throws Throwable {
		// bei Bedarf implementieren.
	}

	private void eventMouseExited(MouseEvent me) {
		// bei Bedarf implementieren.
	}

	protected boolean pruefeObZertifiziert(Integer artikelIId, LieferantDto lDto)
			throws Throwable {
		boolean bZertifiziert = true;
		if (artikelIId != null) {

			ArtikelDto aDto = DelegateFactory.getInstance()
					.getArtikelDelegate().artikelFindByPrimaryKey(artikelIId);

			if (!aDto.getArtikelartCNr().equals(
					ArtikelFac.ARTIKELART_HANDARTIKEL)) {

				if (aDto.getArtgruIId() != null) {

					ArtgruDto agruDto = DelegateFactory.getInstance()
							.getArtikelDelegate()
							.artgruFindByPrimaryKey(aDto.getArtgruIId());

					if (Helper.short2boolean(agruDto.getBZertifizierung())) {

						if (lDto.getTFreigabe() == null) {
							bZertifiziert = false;
						}
					}
				} else {
					// Fehlermeldung

					ParametermandantDto parameter = (ParametermandantDto) DelegateFactory
							.getInstance()
							.getParameterDelegate()
							.getParametermandant(
									ParameterFac.PARAMETER_ARTIKELGRUPPE_IST_PFLICHTFELD,
									ParameterFac.KATEGORIE_ARTIKEL,
									LPMain.getTheClient().getMandant());
					boolean bPflichtfeld = (Boolean) parameter
							.getCWertAsObject();
					if (bPflichtfeld == true) {
						DialogFactory
								.showModalDialog(
										LPMain.getTextRespectUISPr("lp.error"),
										LPMain.getTextRespectUISPr("part.lieferant.zertifizierung.keineartikelgruppe"));
						bZertifiziert = false;
					}

				}
			}

		}
		return bZertifiziert;
	}

	private void performActionEvents(HvActionEvent aE) throws Throwable {
		if (aE.getActionCommand().equals(ACTION_DELETE)) {
			eventActionDelete(aE, true, false);
		} else if (aE.getActionCommand().equals(ACTION_UPDATE)) {
			eventActionUpdate(aE, false);
		} else if (aE.getActionCommand().equals(ACTION_LEEREN)) {
			eventActionLeeren(aE);
		} else if (aE.getActionCommand().equals(ACTION_REFRESH)) {
			try {
				eventActionRefresh(aE, false);
			} catch (ExceptionLP efc) {
				ItemChangedEvent ice = new ItemChangedEvent(this,
						ItemChangedEvent.ACTION_GOTO_MY_DEFAULT_QP);

				switch (efc.getICode()) {

				case EJBExceptionLP.FEHLER_BEI_FINDBYPRIMARYKEY:
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.hint"),
							LPMain.getTextRespectUISPr("lp.hint.geloescht"));
					getInternalFrame().changed(ice);
					break;

				// checknumberformat: 6
				case EJBExceptionLP.FEHLER_FORMAT_NUMBER:
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getTextRespectUISPr("lp.error.belegwerte"));
					break;

				default:
					LPMain.getInstance().exitFrame(getInternalFrame(), efc);
					break;
				}
			}
		} else if (aE.getActionCommand().equals(ACTION_PRINT)) {
			eventActionPrint(aE);
		} else if (aE.getActionCommand().equals(ACTION_NEW)) {

			eventActionNew(null, true, false);
		} else if (aE.getActionCommand().equals(ACTION_DISCARD)) {
			eventActionDiscard(aE);
		} else if (aE.getActionCommand().equals(ACTION_SAVE)) {
			eventActionSave(aE, false);
		} else if (aE.getActionCommand().equals(ACTION_FILTER)) {
			eventActionFilter(aE);
		} else if (aE.getActionCommand().equals(ACTION_TEXT)) {
			eventActionText(aE);
		} else if (aE.getActionCommand().equals(ACTION_NEXT)) {
			eventActionNext(true);
		} else if (aE.getActionCommand().equals(ACTION_PREVIOUS)) {
			eventActionNext(false);
		} else if (aE.getActionCommand().equals(ESC)) {
			if (this instanceof WrapperEditorField) {
			} else {
				if (isNotLocked())
					eventActionEscape(aE);
			}
		} else if (aE.getActionCommand().equals(ALT1)
				|| aE.getActionCommand().equals(ALT2)
				|| aE.getActionCommand().equals(ALT3)
				|| aE.getActionCommand().equals(ALT4)
				|| aE.getActionCommand().equals(ALT5)
				|| aE.getActionCommand().equals(ALT6)
				|| aE.getActionCommand().equals(ALT7)
				|| aE.getActionCommand().equals(ALT8)
				|| aE.getActionCommand().equals(ALT9)
				|| aE.getActionCommand().equals(ALTR)
				|| aE.getActionCommand().equals(ALTF)
				|| aE.getActionCommand().equals(ALTB)) {
			eventActionAlt(aE);
		} else {
			eventActionSpecial(aE);
		}
	}

	private void createAllButtons() {
		// accel: ESC
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ESC);
		getActionMap().put(ESC, new ButtonAbstractAction(this, ESC));

		createAndSaveButton(
				"/com/lp/client/res/document.png",
				LPMain.getTextRespectUISPr("lp.new"),
				ACTION_NEW,
				KeyStroke
						.getKeyStroke('N', java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton(
				"/com/lp/client/res/disk_blue.png",
				LPMain.getTextRespectUISPr("lp.save"),
				ACTION_SAVE,
				KeyStroke
						.getKeyStroke('S', java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton(
				"/com/lp/client/res/edit.png",
				LPMain.getTextRespectUISPr("lp.edit"),
				ACTION_UPDATE,
				KeyStroke
						.getKeyStroke('U', java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton("/com/lp/client/res/leeren.png",
				LPMain.getTextRespectUISPr("lp.leeren"), ACTION_LEEREN,
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton(
				"/com/lp/client/res/undo.png",
				LPMain.getTextRespectUISPr("lp.undo"),
				ACTION_DISCARD,
				KeyStroke
						.getKeyStroke('Z', java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton(
				"/com/lp/client/res/delete2.png",
				LPMain.getTextRespectUISPr("lp.delete"),
				ACTION_DELETE,
				KeyStroke
						.getKeyStroke('D', java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton("/com/lp/client/res/refresh.png",
				LPMain.getTextRespectUISPr("lp.refresh"), ACTION_REFRESH,
				KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), null);

		createAndSaveButton(
				"/com/lp/client/res/funnel.png",
				LPMain.getTextRespectUISPr("lp.filter"),
				ACTION_FILTER,
				KeyStroke
						.getKeyStroke('F', java.awt.event.InputEvent.CTRL_MASK),
				null);

		createAndSaveButton(
				"/com/lp/client/res/printer.png",
				LPMain.getTextRespectUISPr("lp.printer"),
				ACTION_PRINT,
				KeyStroke
				.getKeyStroke('P', java.awt.event.InputEvent.CTRL_MASK),
				null).addMouseListener(this);

		createAndSaveButton(
				"/com/lp/client/res/table_sql_view.png",
				LPMain.getTextRespectUISPr("lp.flrprinter"),
				ACTION_PRINT_FLR,
				KeyStroke
						.getKeyStroke('O', java.awt.event.InputEvent.CTRL_MASK),
				null);

		createAndSaveButton(
				"/com/lp/client/res/arrow_up_blue.png",
				LPMain.getTextRespectUISPr("lp.tooltip.positionvonnnachnminus1verschieben"),
				ACTION_POSITION_VONNNACHNMINUS1,
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton(
				"/com/lp/client/res/arrow_down_blue.png",
				LPMain.getTextRespectUISPr("lp.tooltip.positionvonnnachnplus1verschieben"),
				ACTION_POSITION_VONNNACHNPLUS1,
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton(
				"/com/lp/client/res/row_add_before.png",
				LPMain.getTextRespectUISPr("lp.tooltip.positionvoraktuellerpositioneinfuegen"),
				ACTION_POSITION_VORPOSITIONEINFUEGEN,
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton("/com/lp/client/res/copy.png",
				LPMain.getTextRespectUISPr("lp.inzwischenablagekopieren"),
				ACTION_KOPIEREN, null);
		createAndSaveButton("/com/lp/client/res/paste.png",
				LPMain.getTextRespectUISPr("lp.auszwischenablageeinfuegen"),
				ACTION_EINFUEGEN_LIKE_NEW, RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton("/com/lp/client/res/notebook.png",
				LPMain.getTextRespectUISPr("lp.texteingabezuartikel"),
				ACTION_TEXT, KeyStroke.getKeyStroke('T',
						java.awt.event.InputEvent.CTRL_MASK),
				RechteFac.RECHT_MODULWEIT_UPDATE);

		createAndSaveButton("/com/lp/client/res/navigate_left.png",
				LPMain.getTextRespectUISPr("lp.vorherigerdatensatz"),
				ACTION_PREVIOUS, KeyStroke.getKeyStroke('K',
						java.awt.event.InputEvent.CTRL_MASK), null);
		createAndSaveButton("/com/lp/client/res/navigate_right.png",
				LPMain.getTextRespectUISPr("lp.naechsterdatensatz"),
				ACTION_NEXT, KeyStroke.getKeyStroke('L',
						java.awt.event.InputEvent.CTRL_MASK), null);

	}

	/*
	 * Setzt alle Felder auf Enabled/Disabled, wenn Eigenschaft allowEnable ==
	 * true
	 */
	protected void leereAlleFelder(java.awt.Container container)
			throws Throwable {
		java.awt.Component[] components = container.getComponents();
		for (int i = 0; i < components.length; ++i) {
			if (components[i] instanceof WrapperEditorField) {
				((WrapperEditorField) components[i]).removeContent();
			} else if (components[i] instanceof WrapperBildField) {
				((WrapperBildField) components[i]).setImage(null);
			} else if (components[i] instanceof WrapperTimestampField) {
				((WrapperTimestampField) components[i]).removeContent();
			} else if ((components[i] instanceof java.awt.Container)
					&& !(components[i] instanceof IControl)) {
				leereAlleFelder((java.awt.Container) components[i]);
			} else {
				java.awt.Component component = null;
				if (components[i] instanceof javax.swing.JScrollPane) {
					// Komponenten fuer TextAreas und Tabellen auf Scrollpanes
					// ermitteln
					javax.swing.JScrollPane jScrollPane = (javax.swing.JScrollPane) components[i];
					component = jScrollPane.getViewport().getView();
				} else {
					component = components[i];
				}
				if (component instanceof IControl) {
					IControl iControl = (IControl) component;
					iControl.removeContent();

					if (component instanceof WrapperTextField) {
						WrapperTextField wtf = (WrapperTextField) component;
						if (wtf.getZugehoerigesSelectField() != null) {
							wtf.getZugehoerigesSelectField().removeContent();
						}
					}

				}
			}
		}
	}

	/**
	 * Alle Pflichtfelder dieser Component gefuellt?
	 *
	 * @param component
	 *            Component zu pruefen.
	 * @return boolean true ... ja; false ... sonst
	 * @throws Throwable
	 */
	private boolean allMandatorySet(Component component) throws Throwable {

		if (component instanceof IControl) {
			IControl c = (IControl) component;
			if (c.isMandatoryField() && !c.hasContent())
				return false;

		} else if (component instanceof java.awt.Container
				&& component.isVisible()) {
			Container c = (Container) component;
			for (Component child : c.getComponents()) {
				if (!allMandatorySet(child))
					return false;
			}
		} else if (component instanceof javax.swing.JScrollPane) {
			// Komponenten fuer TextAreas und Tabellen auf Scrollpanes
			// ermitteln
			JScrollPane jScrollPane = (JScrollPane) component;
			if (!allMandatorySet(jScrollPane.getViewport().getView())) {
				return false;
			}
		}
		// alle Pflichtfelder sind gefuellt
		return true;
	}

	private Component allMandatoryComponentSet(Component component)
			throws Throwable {
		if (component instanceof IControl) {
			IControl c = (IControl) component;
			if (c.isMandatoryField() && !c.hasContent())
				return component;

		} else if (component instanceof java.awt.Container
				&& component.isVisible()) {
			Container c = (Container) component;
			for (Component child : c.getComponents()) {
				Component com = allMandatoryComponentSet(child);
				if (com != null)
					return com;
			}
		} else if (component instanceof javax.swing.JScrollPane) {
			// Komponenten fuer TextAreas und Tabellen auf Scrollpanes
			// ermitteln
			JScrollPane jScrollPane = (JScrollPane) component;
			if (!allMandatorySet(jScrollPane.getViewport().getView())) {
				return jScrollPane;
			}
		}
		// alle Pflichtfelder sind gefuellt
		return null;
	}

	/*
	 * Setzt alle Felder auf Enabled/Disabled, wenn Eigenschaft allowEnable ==
	 * true
	 *
	 * @todo ppp eigentlich private PJ 4849
	 */
	public static void enableAllComponents(java.awt.Container container,
			boolean enable) {
		java.awt.Component[] components = container.getComponents();
		for (int i = 0; i < components.length; ++i) {
			/** @todo JO->MB machst den du? PJ 4850 */
			if (components[i] instanceof PanelReportIfJRDS) {
				// MB: die kriterienpanel duerfen nicht beeinflusst werden
				return;
			}
			if (components[i] instanceof LpEditor) {
				((LpEditor) components[i]).setEditable(enable);
				((LpEditor) components[i]).showToolBar(enable);
				/** @todo AD Test Table PJ 4858 */
				((LpEditor) components[i]).showTableItems(false);
				// (LpEditor) components[i]).showTableItems(true);

				((LpEditor) components[i]).validate();
			} else if (components[i] instanceof WrapperEditorField) {
				((WrapperEditorField) components[i]).setEditable(enable);
			} else if (components[i] instanceof WrapperFixableNumberField) {
				((WrapperFixableNumberField) components[i]).setEditable(enable);
				((WrapperFixableNumberField) components[i]).getWrbFixNumber()
						.setEnabled(enable);
			} else if (components[i] instanceof WrapperTimestampField) {
				if (((WrapperTimestampField) components[i]).isActivatable()) {
					((WrapperTimestampField) components[i]).setEditable(enable);
				} else {
					((WrapperTimestampField) components[i]).setEditable(false);
				}
			} else if (components[i] instanceof WrapperSnrChnrField) {
				if (((WrapperSnrChnrField) components[i]).isActivatable()) {
					((WrapperSnrChnrField) components[i]).setEditable(enable);
				} else {
					((WrapperSnrChnrField) components[i]).setEditable(false);
				}
			} else if ((components[i] instanceof java.awt.Container)
					&& !(components[i] instanceof IControl)) {
				enableAllComponents((java.awt.Container) components[i], enable);
			} else {
				java.awt.Component component = null;
				if (components[i] instanceof javax.swing.JScrollPane) {
					// Komponenten fuer TextAreas und Tabellen auf Scrollpanes
					// ermitteln
					javax.swing.JScrollPane jScrollPane = (javax.swing.JScrollPane) components[i];
					component = jScrollPane.getViewport().getView();
					jScrollPane.getViewport().revalidate();
					components[i].repaint();
				} else {
					component = components[i];
				}
				if (component instanceof IControl) {
					IControl iControl = (IControl) component;
					if (iControl instanceof JTextComponent) {
						if (iControl.isActivatable()) {
							((JTextComponent) component).setEditable(enable);
						} else {
							((JTextComponent) component).setEditable(false);
						}
					} else if (iControl instanceof WrapperTextFieldWithIconButton) {
						if (iControl.isActivatable()) {
							((WrapperTextFieldWithIconButton) component)
									.setEditable(enable);
						} else {
							((WrapperTextFieldWithIconButton) component)
									.setEditable(false);
						}
					} else {
						if (iControl.isActivatable()) {
							((Component) component).setEnabled(enable);
						} else {
							((Component) component).setEnabled(false);
						}
					}
					if (component instanceof WrapperNumberField) {
						WrapperNumberField wnf = (WrapperNumberField) component;
						if (wnf.isDependenceField()) {
							if (enable) {
								wnf.setBackground(HelperClient
										.getDependenceFieldBackgroundColor());
							} else {
								wnf.setBackground(HelperClient
										.getDependenceFieldBackgroundColorDisabled());
							}
						}
					}
				}
			}
		}
	}

	@Override
	public HashMap<String, LPButtonAction> getHmOfButtons() {
		return getToolBar().getHmOfButtons();
	}

	public JPanel getToolsPanel() throws Exception {
		return getToolBar().getToolsPanel();
	}

	public ToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new ToolBar(this);

			initComponents();
		}
		return toolBar;
	}

	public void setToolBar(ToolBar toolBar) {
		this.toolBar = toolBar;
	}

	protected void resetToolsPanel() throws Exception {
		getToolBar().resetToolsPanel();
	}

	public InternalFrame getInternalFrame() {
		return internalFrame;
	}

	/**
	 * @deprecated MB: das ist aber gar nicht schoen!
	 *
	 *             Setzen des IF's. nur in diesem Package sichtbar.
	 *
	 * @param internalFrame
	 *            InternalFrame
	 */
	void setInternalFrame(InternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}

	public boolean isFilterPushed() {
		return isFilterPushed;
	}

	/**
	 * mouseClicked.
	 *
	 * @param e
	 *            MouseEvent
	 */
	final public void mouseClicked(MouseEvent e) {
		actionPerformedLog(e);
	}

	/**
	 * mouseEntered.
	 *
	 * @param e
	 *            MouseEvent
	 */
	final public void mouseEntered(MouseEvent e) {
		// wen du's brauchst aufmachen; macht sonst zuviele events.
		// actionPerformedLog(e);
	}

	/**
	 * mouseExited.
	 *
	 * @param e
	 *            MouseEvent
	 */
	final public void mouseExited(MouseEvent e) {
		// wen du's brauchst aufmachen; macht sonst zuviele events.
		// actionPerformedLog(e);
	}

	/**
	 * mousePressed.
	 *
	 * @param e
	 *            MouseEvent
	 */
	final public void mousePressed(MouseEvent e) {
		// wen du's brauchst aufmachen; macht sonst zuviele events.
		// actionPerformedLog(e);
	}

	/**
	 * mouseReleased.
	 *
	 * @param e
	 *            MouseEvent
	 */
	public void mouseReleased(MouseEvent e) {
		// wen du's brauchst aufmachen; macht sonst zuviele events.
		// actionPerformedLog(e);
	}

	/**
	 * keyPressed.
	 *
	 * @param e
	 *            KeyEvent
	 */
	final public void keyPressed(KeyEvent e) {
		actionPerformedLog(e);
	}

	/**
	 * keyReleased.
	 *
	 * @param e
	 *            KeyEvent
	 */
	final public void keyReleased(KeyEvent e) {
		actionPerformedLog(e);
	}

	/**
	 * keyTyped.
	 *
	 * @param e
	 *            KeyEvent
	 */
	final public void keyTyped(KeyEvent e) {
		actionPerformedLog(e);
	}

	/**
	 * itemevt: 7 Informiere Panel ueber einen Itemevent.
	 *
	 * @param e
	 *            ItemChangedEvent
	 */
	final public void changed(EventObject e) {
		actionPerformedLog(e);
	}

	public String getAdd2Title() {
		return add2Title;
	}

	public void setAdd2Title(String sAdd2Title) {
		add2Title = sAdd2Title;
	}

	public Object getKeyWhenDetailPanel() {
		return keyWhenDetailPanel;
	}

	public void setKeyWhenDetailPanel(Object keyWhenDetailPanel) {
		myLogger.info(this.getClass() + "|key:" + keyWhenDetailPanel);
		this.keyWhenDetailPanel = keyWhenDetailPanel;
	}

	/**
	 * Die Spalten fuer die Datensatzmodifikationen setzen PersonalIId fuer
	 * Aendern und Anlegen Timestamps fuer Aendern und Anlegen
	 *
	 * @param theData
	 * @throws Throwable
	 */
	protected final void setStatusbarModification(IModificationData theData)
			throws Throwable {
		getPanelStatusbar().setPersonalIIdAnlegen(
				theData.getPersonalIIdAnlegen());
		getPanelStatusbar().setPersonalIIdAendern(
				theData.getPersonalIIdAendern());
		getPanelStatusbar().setTAnlegen(theData.getTAnlegen());
		getPanelStatusbar().setTAendern(theData.getTAendern());
	}

	protected final void setStatusbarPersonalIIdAnlegen(
			Integer personalIIdAnlegen) throws Throwable {
		getPanelStatusbar().setPersonalIIdAnlegen(personalIIdAnlegen);
	}

	protected final void setStatusbarPersonalIIdAendern(
			Integer personalIIdAendern) throws Throwable {
		getPanelStatusbar().setPersonalIIdAendern(personalIIdAendern);
	}

	protected final void setStatusbarTAnlegen(Timestamp tAnlegen)
			throws Throwable {
		getPanelStatusbar().setTAnlegen(tAnlegen);
	}

	protected final void setStatusbarTAendern(Timestamp tAendern)
			throws Throwable {
		getPanelStatusbar().setTAendern(tAendern);
	}

	protected final void setStatusbarStatusCNr(String statusCNr)
			throws Throwable {
		getPanelStatusbar().setStatusCNr(statusCNr);
	}

	protected final void setStatusbarSpalte5(Object o) throws Throwable {
		setStatusbarSpalte5Color(Color.black);
		getPanelStatusbar().setSpalte5(o);
	}

	protected final void setStatusbarSpalte5Color(Color color) throws Throwable {
		getPanelStatusbar().setTextColorSpalte5(color);
	}

	/**
	 * Statusleiste leeren. statusbarneu: 3 hier kannst du die statusbarfelder
	 * leeren.
	 *
	 * @throws Throwable
	 */
	protected final void clearStatusbar() throws Throwable {
		getPanelStatusbar().clearStatusbar();
	}

	/**
	 * panelstatusbar holen mit lazy loading. statusbarneu: 2 hier wird sie
	 * gebaut
	 *
	 * @return JPanel
	 * @throws Throwable
	 */
	protected final PanelStatusbar getPanelStatusbar() throws Throwable {
		if (panelStatusbar == null) {
			panelStatusbar = new PanelStatusbar();
			initComponents();
		}
		return panelStatusbar;
	}

	/**
	 * Eigene ExceptionLP's verarbeiten. myexception: 1
	 *
	 * @param exfc
	 *            EJBExceptionLP
	 * @return boolean
	 * @throws Throwable
	 */
	public boolean handleOwnException(ExceptionLP exfc) throws Throwable {
		return false;
	}

	/**
	 * evtvet: Gib die PropertyVetoException zurueck, wenn null ohne Meldung aus
	 * dem Modul ausgestiegen. Achtung: Eigentlich muss jeder Event ueber
	 * actionPerformedLog laufen, geht hier nicht wegen Rueckgabewert.
	 *
	 * @return PropertyVetoException
	 * @throws Throwable
	 */
	final public PropertyVetoException vetoableChangeLP() throws Throwable {

		PropertyVetoException pve = eventActionVetoableChangeLP();
		return pve;
	}

	/**
	 * evtvet: Event "Vetoable Window close"; wird null zurueckgegeben, so wird
	 * das Modul via dicard beendet, wird ein PropertyVetoException
	 * zurueckgegeben, bleibt das Modul "erhalten".
	 *
	 * @return PropertyVetoException
	 * @throws Throwable
	 */
	protected PropertyVetoException eventActionVetoableChangeLP()
			throws Throwable {

		PropertyVetoException pve = null;

		int iLockstate = getLockedstateDetailMainKey().getIState();
		if (iLockstate == LOCK_IS_LOCKED_BY_ME || iLockstate == LOCK_FOR_NEW) {
			Object am[] = { LPMain.getTextRespectUISPr("lp.speichern"),
					LPMain.getTextRespectUISPr("lp.verwerfen_ohne_frage"), };
			int iOption = DialogFactory.showModalDialogDesktopMitte(
					LPMain.getTextRespectUISPr("lp.warning.speichern"), "", am,
					IISPEICHERN);

			if (iOption == IISPEICHERN.intValue()) {
				if (allMandatoryFieldsSetDlg()) {
					eventActionSave(null, false);
				} else {
					pve = new PropertyVetoException("", null);
				}
			} else if (iOption == IIVERWERFEN.intValue()) {
				discard();
			} else {
				pve = new PropertyVetoException("", null);
			}
		}

		return pve;

	}

	public void finalize() throws Throwable {
		getHmOfButtons().clear();
		alleLPEditorAufNullSetzen(this.getComponents());
		super.finalize();
	}

	private void alleLPEditorAufNullSetzen(java.awt.Component[] components) {
		for (int i = 0; i < components.length; ++i) {
			if (components[i] instanceof WrapperEditorField) {

				WrapperEditorField wef = ((WrapperEditorField) components[i]);
				wef.lpEditor.cleanup();
				wef.lpEditor = null;
				wef.jspScrollPane.setViewport(null);
				wef.setToolBar(null);
				components[i] = null;
			} else if (components[i] instanceof WrapperBildField) {
				WrapperBildField wef = ((WrapperBildField) components[i]);
				wef.setToolBar(null);
				components[i] = null;
			} else if (components[i] instanceof WrapperDateField) {
				WrapperDateField wef = ((WrapperDateField) components[i]);
				wef.cleanup();
				components[i] = null;
			} else if (components[i] instanceof WrapperMediaControl) {
				WrapperMediaControl wef = ((WrapperMediaControl) components[i]);
				wef.cleanup();
				wef.setToolBar(null);
				components[i] = null;
			} else if (components[i] instanceof WrapperMediaControlTexteingabe) {
				WrapperMediaControlTexteingabe wef = ((WrapperMediaControlTexteingabe) components[i]);
				wef.setToolBar(null);
				components[i] = null;
			} else {
				if (components[i] instanceof Container) {
					alleLPEditorAufNullSetzen(((Container) components[i])
							.getComponents());
				}
			}
		}
	}

	public void focusGained(FocusEvent e) {
		try {
			setFirstFocusableComponent();
		} catch (Exception ex) {
			myLogger.error("focusGained(): ", ex);
		}
	}

	public void focusLost(FocusEvent e) {
		// JO 15.02.06 wegen shortcuts da.
	}

	/**
	 * Initialierungen fuer die Komponenten. Muss zu einem Zeitpunkt aufgerufen
	 * werden, wenn die Komponenten nicht mehr null sind.
	 */
	protected final void initComponents() {
		// fuer neue Namensgebung
		HelperClient.setComponentNames(this);

		// alte Namensgebung
		// -> weg, sobald alle alten Qftests migriert sind!
		try {
			setComponentNamesForAbbot(false);
		} catch (Throwable ex) {
			myLogger.warn("Fehler in setComponentNamesForAbbot!");
		}
	}

	/**
	 * Alle Member-Variablen der Panels vom Typ java.awt.Component erhalten als
	 * Name den Variablennamen (damit Abbot auch bei Veraenderungen am Layout
	 * noch richtig funktioniert)
	 *
	 * Wird noch solange gebraucht, bis alle alten qftests migriert sind.
	 *
	 * @throws Throwable
	 * @param bGenerateUniqueNames
	 *            boolean
	 * @deprecated
	 */
	protected final void setComponentNamesForAbbot(boolean bGenerateUniqueNames)
			throws Throwable {
		// nur dann, wenn der Abbot auch laeuft
		if (Defaults.getInstance().isOldComponentNamingEnabled()) {
			long tStart = System.currentTimeMillis();
			Field[] fields = this.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				String sName = fields[i].getName();
				try {
					// Alle final bzw. static konstanten ignorieren
					if (!(Modifier.isFinal(fields[i].getModifiers()) && Modifier
							.isStatic(fields[i].getModifiers()))) {
						// Methode setName(String name) laden
						Method method = fields[i].getDeclaringClass()
								.getMethod("setName",
										new Class[] { String.class });
						boolean bIsAccessible = fields[i].isAccessible();
						if (!bIsAccessible) {
							// Zugriff sichern
							fields[i].setAccessible(true);
						}
						// Objekt laden
						Object oComponent = fields[i].get(this);
						if (oComponent instanceof java.awt.Component) {
							// Warnung fuer Felder, die nicht private oder
							// protected sind
							if (!Modifier.isPrivate(fields[i].getModifiers())
									&& !Modifier.isProtected(fields[i]
											.getModifiers())) {
								myLogger.warn(sName + " in "
										+ this.getClass().getName()
										+ " is not private or protected");
							}
							// Den Klassennamen anhaengen, damit eindeutige
							// Namen vergeben werden.
							if (bGenerateUniqueNames) {
								sName = this.getClass().getSimpleName() + "."
										+ sName;
							}
							// setName(ausfuehren)
							method.invoke(oComponent, new Object[] { sName });
						} else if (oComponent instanceof WrapperIdentField) {
							((WrapperIdentField) oComponent)
									.setComponentNames(fields[i].getName());
						}
						// alte Verfuegbarkeit wiederherstellen
						fields[i].setAccessible(bIsAccessible);
					}
				} catch (NoSuchMethodException ex) {
					throw new ExceptionLP(EJBExceptionLP.FEHLER, ex);
				}
			}
			// Dauer loggen
			long tEnd = System.currentTimeMillis();
			myLogger.debug("setComponentNamesForAbbot in "
					+ this.getClass().getName() + " dauerte " + (tEnd - tStart)
					+ " ms.");
		}
	}
}
