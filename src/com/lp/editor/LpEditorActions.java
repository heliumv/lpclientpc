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
package com.lp.editor;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TextAction;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.lp.client.frame.component.DialogQuery;
import com.lp.client.frame.component.PanelQueryFLR;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.delegate.PersonalDelegate;
import com.lp.client.pc.LPMain;
import com.lp.client.system.SystemFilterFactory;
import com.lp.client.util.logger.LpLogger;
import com.lp.editor.ui.LpFontChooser;
import com.lp.editor.util.LpEditorMessages;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.system.service.TheClientDto;

/**
 * 
 * <p>
 * <I>Actions fuer den LpEditor</I>.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2008
 * </p>
 * <p>
 * Erstellungsdatum <I>August 2003</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Kajetan Fuchsberger
 * @author Sascha Zelzer
 * @version $Revision: 1.4 $
 */

class ActionFileNew extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFileNew(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.New");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic("Action.New");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_FILE_NEW);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.openBlank();
		lpEditor.requestFocusInWindow();
	}
}

class ActionFileOpen extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFileOpen(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.Open_");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.Open_");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_FILE_OPEN);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.open();
		lpEditor.requestFocusInWindow();
	}
}

class ActionFileSave extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFileSave(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.Save");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Save");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_FILE_SAVE);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.save();
		lpEditor.requestFocusInWindow();
	}
}

class ActionFileSaveAs extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	// KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFileSaveAs(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.SaveAs_");
		sShortDescription = sName;
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.SaveAs_");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_FILE_SAVEAS);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.saveAs();
		lpEditor.requestFocusInWindow();
	}
}

/**
 * Action - Klasse fuer das Bearbeiten der Undo - Events. Haelt unter anderem
 * den Undo - Manager auf dem neuesten Stand und Stellt Methoden zum
 * entsprechenden Aktivieren / Deaktivieren der Action bereit.
 * 
 * @see com.lp.lpeditor.LpEditor.LpUndoableEditListener
 * @see RedoAction
 */
class UndoAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public UndoAction(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.Undo");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Undo");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_UNDO);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
		setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			lpEditor.undoManager.undo();
		} catch (CannotUndoException ex) {
			LpLogger.getInstance(UndoAction.class).warn(
					ex.getLocalizedMessage());
		}
		updateUndoState();
		lpEditor.actionEditRedo.updateRedoState();
		lpEditor.updateBufferStatus(null, -1, true);
		lpEditor.requestFocusInWindow();
	}

	protected void updateUndoState() {
		if (lpEditor.undoManager.canUndo()) {
			setEnabled(true);
			putValue(Action.NAME,
					lpEditor.undoManager.getUndoPresentationName());
		} else {
			setEnabled(false);
			putValue(Action.NAME, "Undo");
		}
	}
}

/**
 * Action - Klasse fuer das Bearbeiten der Redo - Events. Haelt unter anderem
 * den Undo - Manager auf dem neuesten Stand und Stellt Methoden zum
 * entsprechenden Aktivieren / Deaktivieren der Action bereit.
 * 
 * @see com.lp.lpeditor.LpEditor.LpUndoableEditListener
 * @see UndoAction
 */
class RedoAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public RedoAction(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString("Action.Redo");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_R,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Redo");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_REDO);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
		setEnabled(false);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			lpEditor.undoManager.redo();
		} catch (CannotRedoException ex) {
			LpLogger.getInstance(RedoAction.class).warn(
					ex.getLocalizedMessage());
		}
		updateRedoState();
		lpEditor.actionEditUndo.updateUndoState();
		lpEditor.updateBufferStatus(null, -1, true);
		lpEditor.requestFocusInWindow();
	}

	protected void updateRedoState() {
		if (lpEditor.undoManager.canRedo()) {
			setEnabled(true);
			putValue(Action.NAME,
					lpEditor.undoManager.getRedoPresentationName());
		} else {
			setEnabled(false);
			putValue(Action.NAME, "Redo");
		}
	}
}

class ActionEditCut extends StyledEditorKit.CutAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionEditCut(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Cut");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_X,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic("Action.Cut");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_CUT);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}

class ActionEditCopy extends StyledEditorKit.CopyAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionEditCopy(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Copy");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_C,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Copy");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_COPY);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}
}

/**
 * 
 * <p>
 * Action um das heutige Datum und Uhrzeit in client Locale und Kurzzeichen des
 * gerade eingeloggten Personal im Editor einzufuegen.
 * </p>
 * 
 * <p>
 * 
 * @author $Author: valentin $
 *         </p>
 * 
 * @version not attributable Date $Date: 2008/08/11 10:46:03 $
 */
class ActionInsertDateTimeUserShortcut extends TextAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionInsertDateTimeUserShortcut(LpEditor lpEditor) {
		super("InsertDateTimeUserShortcut");
		sName = LpEditorMessages.getInstance().getString(
				"Action.InsertDateTimeUserShortcut");
		sShortDescription = sName;
		// MR todo: Shortcut funktioniert nicht.
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_I,
		// KeyEvent.CTRL_MASK);
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_INSERT_DATE);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e) {
		JTextComponent target = getTextComponent(e);
		if ((target != null) && (e != null)) {
			if ((!target.isEditable()) || (!target.isEnabled())) {
				UIManager.getLookAndFeel().provideErrorFeedback(target);
				return;
			}
			String content = e.getActionCommand();

			// Heutiges Datum und Zeit in richtigem Locale, und Bearbeiter
			// Kurzzeichen einfuegen
			Locale editorLocale = LPMain.getInstance().getUISprLocale();
			GregorianCalendar cal = new GregorianCalendar(editorLocale);
			// Datum
			SimpleDateFormat sdDateFormat = new SimpleDateFormat("dd.MM.yyyy",
					editorLocale);
			content = sdDateFormat.format(cal.getTime());
			// Uhrzeit
			SimpleDateFormat sdTimeFormat = new SimpleDateFormat("HH:mm",
					editorLocale);
			content += " " + sdTimeFormat.format(cal.getTime());
			// Personal Kurzzeichen des Bearbeiters
			try {
				TheClientDto clientDto = LPMain.getInstance().getTheClient();
				Integer iPersonalID = clientDto.getIDPersonal();
				PersonalDelegate persDelegate = DelegateFactory.getInstance()
						.getPersonalDelegate();
				PersonalDto persDto = persDelegate
						.personalFindByPrimaryKey(iPersonalID);
				String sPersKurzzeichen = persDto.getCKurzzeichen();
				content += " " + sPersKurzzeichen;
			} catch (Throwable ex) {
			}

			if (content != null) {
				target.replaceSelection(content);
			} else {
				UIManager.getLookAndFeel().provideErrorFeedback(target);
			}
		}

		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}

class ActionInsertSignatur extends TextAction {
	/**
 * 
 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionInsertSignatur(LpEditor lpEditor) {
		super("InsertSignatur");
		sName = LpEditorMessages.getInstance().getString(
				"Action.InsertSignatur");
		sShortDescription = sName;

		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_INSERT_SIGNATUR);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);

		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e) {
		JTextComponent target = getTextComponent(e);
		if ((target != null) && (e != null)) {
			if ((!target.isEditable()) || (!target.isEnabled())) {
				UIManager.getLookAndFeel().provideErrorFeedback(target);
				return;
			}
			String content = "";

			// Personal Kurzzeichen des Bearbeiters
			try {
				TheClientDto clientDto = LPMain.getInstance().getTheClient();
				Integer iPersonalID = clientDto.getIDPersonal();
				PersonalDelegate persDelegate = DelegateFactory.getInstance()
						.getPersonalDelegate();

				String locale = LPMain.getInstance().getTheClient()
						.getLocUiAsString();

				if (lpEditor.localeFuerSignatur != null) {
					locale = lpEditor.localeFuerSignatur;
				}

				String signatur = persDelegate.getSignatur(iPersonalID, locale);
				if (signatur != null) {
					content = signatur;
				}
			} catch (Throwable ex) {
			}

			if (content != null) {

				String text = target.getText();

				text = text + content;

				target.setText(text);
			}
		}

		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}

class ActionInsertTextbaustein extends TextAction {
	/**
 * 
 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionInsertTextbaustein(LpEditor lpEditor) {
		super("InsertTextbaustein");
		sName = LpEditorMessages.getInstance().getString(
				"Action.InsertTextbaustein");
		sShortDescription = sName;

		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_INSERT_TEXTBAUSTEIN);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);

		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent e) {
		JTextComponent target = getTextComponent(e);
		if ((target != null) && (e != null)) {
			if ((!target.isEditable()) || (!target.isEnabled())) {
				UIManager.getLookAndFeel().provideErrorFeedback(target);
				return;
			}

			String content = "";

			// Personal Kurzzeichen des Bearbeiters
			try {
				// Alle Texbtausteine holen

				DialogTextbausteine d = new DialogTextbausteine();
				LPMain.getInstance().getDesktop()
				.platziereDialogInDerMitteDesFensters(d);
				d.setVisible(true);

				if (d.mediastandardIId != null) {
					MediastandardDto mDto = DelegateFactory.getInstance()
							.getMediaDelegate()
							.mediastandardFindByPrimaryKey(d.mediastandardIId);

					// In Dialog anzeigen

					String signatur = "";
					if (mDto.getOMediaText() != null) {
						content = mDto.getOMediaText();
					}

				}
				d.dispose();

			} catch (Throwable ex) {
			}

			if (content != null) {

				String text = target.getText();

				text = text + content;

				target.setText(text);
			}
		}

		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}

class ActionEditPaste extends StyledEditorKit.PasteAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionEditPaste(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Paste");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_V,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.Paste");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_EDIT_PASTE);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		boolean bCellEditing = false;
		if (lpEditor.currentTable != null && lpEditor.currentTable.isEditing()) {
			lpEditor.currentTable.doRowHeightUpdate(false);
			bCellEditing = true;
		}

		super.actionPerformed(e);

		if (bCellEditing) {
			lpEditor.currentTable.doRowHeightUpdate(true);
			lpEditor.currentTable.updateRowHeight(lpEditor.currentTable
					.getEditingRow());
		}

		lpEditor.requestFocusInWindow();
	}

	public KeyStroke getAcceleratorKey() {
		return kAcceleratorKey;
	}

}

class ActionInsertTable extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionInsertTable(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.TableInsert");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_T,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.TableInsert");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_INSERT_TABLE);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.insertTable(null);
		lpEditor.requestFocusInWindow();
	}
}

class ActionDeleteTable extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionDeleteTable(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.TableDelete");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_T,
				KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.TableDelete");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_DELETE_TABLE);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.deleteTable();
		lpEditor.requestFocusInWindow();
	}
}

class ActionInsertRowAbove extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionInsertRowAbove(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString(
				"Action.RowInsertAbove");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.SHIFT_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.RowInsertAbove");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_INSERT_ROW_BEFORE);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.insertTableRow(true);
	}
}

class ActionInsertRowBelow extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionInsertRowBelow(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString(
				"Action.RowInsertBelow");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.RowInsertBelow");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_INSERT_ROW_AFTER);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.insertTableRow(false);
	}
}

class ActionDeleteRow extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionDeleteRow(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.RowDelete");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_D,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.RowDelete");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_DELETE_ROW);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		lpEditor.deleteTableRow();
	}
}

class ActionFormatAlignment extends StyledEditorKit.AlignmentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sShortDescription; // Tooltip
	// KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFormatAlignment(LpEditor lpEditor, String resKey, int align) {
		super(LpEditorMessages.getInstance().getString(resKey), align);

		sShortDescription = LpEditorMessages.getInstance().getString(resKey);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(resKey);
		switch (align) {
		case StyleConstants.ALIGN_LEFT:
			imgIcon = lpEditor.iconManager
					.getIcon(LpEditorIconManager.ICON_STYLE_ALIGN_LEFT);
			break;
		case StyleConstants.ALIGN_RIGHT:
			imgIcon = lpEditor.iconManager
					.getIcon(LpEditorIconManager.ICON_STYLE_ALIGN_RIGHT);
			break;
		case StyleConstants.ALIGN_CENTER:
			imgIcon = lpEditor.iconManager
					.getIcon(LpEditorIconManager.ICON_STYLE_ALIGN_CENTER);
			break;
		case StyleConstants.ALIGN_JUSTIFIED:
			imgIcon = lpEditor.iconManager
					.getIcon(LpEditorIconManager.ICON_STYLE_ALIGN_JUSTIFIED);
			break;
		}

		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		lpEditor.requestFocusInWindow();
	}
}

class ActionFormatStyleBold extends StyledEditorKit.BoldAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFormatStyleBold(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Bold");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_F,
				KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
		iMnemonicKey = LpEditorMessages.getInstance()
				.getMnemonic("Action.Bold");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_BOLD);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		lpEditor.requestFocusInWindow();
	}
}

class ActionFormatStyleItalic extends StyledEditorKit.ItalicAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFormatStyleItalic(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Italic");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_K,
				KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.Italic");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_ITALIC);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		lpEditor.requestFocusInWindow();
	}
}

class ActionFormatStyleUnderline extends StyledEditorKit.UnderlineAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFormatStyleUnderline(LpEditor lpEditor) {
		super();
		sName = LpEditorMessages.getInstance().getString("Action.Underline");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_U,
				KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.Underline");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_UNDERLINE);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		lpEditor.requestFocusInWindow();
	}
}

class ActionFormatStyleStrikethrough extends StyledEditorKit.StyledTextAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFormatStyleStrikethrough(LpEditor lpEditor) {
		super("font-strikethrough");
		sName = LpEditorMessages.getInstance()
				.getString("Action.Strikethrough");
		sShortDescription = sName;
		kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.Strikethrough");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_STRIKETHROUGH);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		JEditorPane editor = getEditor(e);
		if (editor != null) {
			StyledEditorKit kit = getStyledEditorKit(editor);
			MutableAttributeSet attr = kit.getInputAttributes();
			boolean strikethrough = (StyleConstants.isStrikeThrough(attr)) ? false
					: true;
			SimpleAttributeSet sas = new SimpleAttributeSet();
			StyleConstants.setStrikeThrough(sas, strikethrough);
			setCharacterAttributes(editor, sas, false);
		}

		lpEditor.requestFocusInWindow();
	}
}

class ActionFormatFont extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	// KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFormatFont(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString(
				"Action.TextProperties");
		sShortDescription = sName;
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.TextProperties");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_FONT);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		int pos = lpEditor.getCaretPosition();
		if (lpEditor.getCaret().getMark() <= pos) {
			pos--;
		}
		AttributeSet attr = lpEditor.getStyledDocument()
				.getCharacterElement(pos).getAttributes();

		LpFontChooser fontChooser = new LpFontChooser(lpEditor.getOwnerFrame(),
				lpEditor.getEditorMode(), null,
				lpEditor.getAsAvailableFontSizes());

		fontChooser.setAttributes(attr, lpEditor.jTextPane.getSelectedText());
		fontChooser.setVisible(true);
		if (fontChooser.getOption() == JOptionPane.OK_OPTION) {
			lpEditor.setCharacterAttributes(fontChooser.getAttributes(), false); // fontChooser.getSelectedFont()
		}
		fontChooser.dispose();
		fontChooser = null;
		lpEditor.requestFocusInWindow();
	}
}

@SuppressWarnings("static-access")
class ActionFormatColorForeground extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	// KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFormatColorForeground(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString(
				"Action.ColorForeground");
		sShortDescription = sName;
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.ColorForeground");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_COLOR_FOREGROUND);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		Color colorOld, colorNew;
		AttributeSet attributeSetOld = lpEditor.getCharacterAttributes();
		colorOld = lpEditor.getStyledDocument().getForeground(attributeSetOld);
		colorNew = JColorChooser.showDialog(lpEditor, sName, colorOld);
		if (colorNew != null) {
			SimpleAttributeSet attributeSetNew = new SimpleAttributeSet();
			StyleConstants.setForeground(attributeSetNew, colorNew);
			lpEditor.setCharacterAttributes(attributeSetNew, false);
		}
		lpEditor.requestFocusInWindow();
	}
}

class ActionFormatColorBackground extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sName;
	String sShortDescription; // Tooltip
	// KeyStroke kAcceleratorKey;
	Integer iMnemonicKey;
	ImageIcon imgIcon;
	LpEditor lpEditor;

	public ActionFormatColorBackground(LpEditor lpEditor) {
		sName = LpEditorMessages.getInstance().getString(
				"Action.ColorBackground");
		sShortDescription = sName;
		// kAcceleratorKey = KeyStroke.getKeyStroke(KeyEvent.VK_N,
		// KeyEvent.CTRL_MASK);
		iMnemonicKey = LpEditorMessages.getInstance().getMnemonic(
				"Action.ColorBackground");
		imgIcon = lpEditor.iconManager
				.getIcon(LpEditorIconManager.ICON_STYLE_COLOR_BACKGROUNG);

		putValue(NAME, sName);
		putValue(SHORT_DESCRIPTION, sShortDescription);
		// putValue(ACCELERATOR_KEY, kAcceleratorKey);
		putValue(MNEMONIC_KEY, iMnemonicKey);
		putValue(SMALL_ICON, imgIcon);

		this.lpEditor = lpEditor;
	}

	public void actionPerformed(ActionEvent e) {
		Color colorOld, colorNew;
		AttributeSet attributeSetOld = lpEditor.getCharacterAttributes();
		colorOld = lpEditor.getStyledDocument().getBackground(attributeSetOld);

		colorNew = JColorChooser.showDialog(lpEditor, sName, colorOld);
		if (colorNew != null) {
			SimpleAttributeSet attributeSetNew = new SimpleAttributeSet();
			StyleConstants.setBackground(attributeSetNew, colorNew);
			lpEditor.setCharacterAttributes(attributeSetNew, false);
		}
		lpEditor.requestFocusInWindow();
	}
}

class MenuActionListener implements ActionListener {
	public void actionPerformed(ActionEvent ae) {
		String sCommand = ae.getActionCommand();

		if (sCommand.equals("exit")) {

		}
	}
}
