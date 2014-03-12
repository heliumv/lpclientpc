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
package com.lp.client.frame.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import com.lp.client.frame.Defaults;
import com.lp.client.pc.LPMain;
import com.lp.editor.LpEditor;
import com.lp.editor.util.TextBlockOverflowException;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.SystemFac;

@SuppressWarnings("static-access")
/**
 * <p>Panel zur Eingabe eines Textes per LP-Editor<br/></p>
 *
 * <p>Copyright Logistik Pur Software GmbH (c) 2004-2008</p>
 * @author  Martin Bluehweis
 * @version $Revision: 1.8 $
 */
public class WrapperEditorField extends PanelBasis implements IControl {
	
	private static final long serialVersionUID = 1L;
	
	private boolean isMandatoryField = false;
	private boolean isMandatoryFieldDB = false;
	private boolean isActivatable = true;

	private static final String ACTION_SPECIAL_EDITORFIELD_EDIT = "action_special_editorfield_edit";
	private static final String ACTION_SPECIAL_EDITORFIELD_DEFAULT = "action_special_editorfield_default";

	private String nameFuerEigenschaftsdefinition = null;
	
	public String getNameFuerEigenschaftsdefinition() {
		return nameFuerEigenschaftsdefinition;
	}

	public void setNameFuerEigenschaftsdefinition(
			String nameFuerEigenschaftsdefinition) {
		this.nameFuerEigenschaftsdefinition = nameFuerEigenschaftsdefinition;
	}

	private String defaultText = null;
	private ImageIcon imageIconEdit = null;
	private ImageIcon imageIconReset = null;

	private GridBagLayout gridBagLayout1 = new GridBagLayout();
	private JPanel jpaWorkingOn = new JPanel();
	private WrapperButton wbuEdit = new WrapperButton();
	private GridBagLayout gridBagLayout2 = new GridBagLayout();
	private WrapperButton wbuDefault = new WrapperButton();
	private JScrollPane jspScrollPane = new JScrollPane();
	private LpEditor lpEditor;
	private boolean bWithoutButtons = false;

	public WrapperEditorField(InternalFrame internalFrame, String addTitleI)
			throws Throwable {
		this(internalFrame, addTitleI, false);
	}

	public WrapperEditorField(InternalFrame internalFrame, String addTitleI,
			boolean bWithoutButtons) throws Throwable {
		super(internalFrame, addTitleI);
		this.bWithoutButtons = bWithoutButtons;
		jbInit();
		initComponents();

	}
	
	protected void setDocumentWidth(int width) {
		lpEditor.setPageWidth(width);
	}

	private void jbInit() throws Throwable {
		this.setLayout(gridBagLayout1);
		lpEditor = new LpEditor(null);
//		lpEditor.setJasperReport(DelegateFactory.getInstance()
//				.getSystemDelegate().getDreispalter(), false);
		lpEditor.setPageWidth(ParameterCache.getPageWidth(ParameterFac.PARAMETER_EDITOR_BREITE_SONSTIGE));
		lpEditor.getTextBlockAttributes(-1).capacity = SystemFac.MAX_LAENGE_EDITORTEXT;
		lpEditor.showAlignmentItems(false);
		lpEditor.showFileItems(false);
		lpEditor.showFontStyleItems(false);
		lpEditor.showMenu(false);
		lpEditor.showStatusBar(false);
		lpEditor.showTableItems(false);
		lpEditor.showTabRuler(false);
		lpEditor.showToolBar(false);

		wbuEdit.setMinimumSize(new Dimension(23, 23));
		wbuEdit.setPreferredSize(new Dimension(23, 23));
		wbuEdit.setActionCommand(ACTION_SPECIAL_EDITORFIELD_EDIT);
		wbuEdit.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"text.bearbeiten"));
		wbuEdit.setIcon(getImageIconEdit());
		wbuEdit.addActionListener(this);
		wbuEdit.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke('T',
						java.awt.event.InputEvent.CTRL_MASK),
						ACTION_SPECIAL_EDITORFIELD_EDIT);
		wbuEdit.getActionMap().put(ACTION_SPECIAL_EDITORFIELD_EDIT,
				new ButtonAbstractAction(this, ACTION_SPECIAL_EDITORFIELD_EDIT));

		jpaWorkingOn.setLayout(gridBagLayout2);
		wbuDefault.setMinimumSize(new Dimension(23, 23));
		wbuDefault.setPreferredSize(new Dimension(23, 23));
		wbuDefault.setActionCommand(ACTION_SPECIAL_EDITORFIELD_DEFAULT);
		wbuDefault.setToolTipText(LPMain.getInstance().getTextRespectUISPr(
				"text.default"));
		wbuDefault.setIcon(getImageIconReset());
		wbuDefault.addActionListener(this);
		lpEditor.setEditable(false);

		getInternalFrame().addItemChangedListener(this);

		this.add(jpaWorkingOn, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		if (!bWithoutButtons) {
			jpaWorkingOn.add(wbuEdit,
					new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0,
									0), 0, 0));
			jpaWorkingOn.add(wbuDefault,
					new GridBagConstraints(1, 1, 1, 1, 0.0, 0.1,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0,
									0), 0, 0));
		}
		jpaWorkingOn.add(jspScrollPane, new GridBagConstraints(0, 0, 1, 2, 1.0,
				1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 2), 0, 0));
		jspScrollPane.getViewport().add(lpEditor, null);

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_EDITORFIELD_DEFAULT)) {
			lpEditor.setText(defaultText);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_EDITORFIELD_EDIT)) {

			getInternalFrame().showPanelEditor(this, this.getAdd2Title(), 
					lpEditor.getText());
			// getInternalFrame().showPanelEditor(this,
			// this.getAdd2Title(),
			// lpEditor.getReportData().getTextForDatabase());
		}
	}

	protected void eventItemchanged(EventObject eI) {
	}

	public String getDefaultText() {
		return defaultText;
	}

	/**
	 * Den Default-Text fuer dieses Editorfeld setzen. wreditf: 3 optionaler
	 * Default-Text
	 * 
	 * @param defaultText
	 *            String
	 */
	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public String getText() throws TextBlockOverflowException {
		return lpEditor.getText();
	}

	/**
	 * Auf Basis des Editor-Styles einen Plaintext zur&uuml;ckliefern
	 * 
	 * @return Text ohne HTML Encoding sofern im Editor isStyledText == false
	 * @throws TextBlockOverflowException
	 */
	public String getPlainText() throws TextBlockOverflowException {
		return lpEditor.getPlainText();
	}

	public void setText(String text) {
		lpEditor.setText(text);
	}

	private ImageIcon getImageIconEdit() {
		if (imageIconEdit == null) {
			imageIconEdit = new ImageIcon(getClass().getResource(
					"/com/lp/client/res/notebook.png"));
		}
		return imageIconEdit;
	}

	private ImageIcon getImageIconReset() {
		if (imageIconReset == null) {
			imageIconReset = new ImageIcon(getClass().getResource(
					"/com/lp/client/res/undo.png"));
		}
		return imageIconReset;
	}

	public void setEditable(boolean bEnabled) {
		wbuDefault.setEnabled(bEnabled);
		wbuEdit.setEnabled(bEnabled);
	}

	public boolean isMandatoryField() {
		return isMandatoryField || isMandatoryFieldDB;
	}

	public void setMandatoryField(boolean isMandatoryField) {
		if (isMandatoryFieldDB == false || isMandatoryField == true) {
			this.isMandatoryField = isMandatoryField;
			if (this.isMandatoryField == true) {
				setBorder(Defaults.getInstance().getMandatoryFieldBorder());
			} else {
				setBorder(new WrapperDateField().getBorder());
			}
		}
	}

	public boolean isMandatoryFieldDB() {
		return isMandatoryFieldDB;
	}

	public void setMandatoryFieldDB(boolean isMandatoryFieldDB) {
		this.isMandatoryFieldDB = isMandatoryFieldDB;
		if (isMandatoryFieldDB == true) {
			setMandatoryField(true);
		}
	}

	public boolean isActivatable() {
		return isActivatable;
	}

	public void setActivatable(boolean isActivatable) {
		this.isActivatable = isActivatable;
	}

	public void removeContent() {
		lpEditor.setText("");
	}

	public LpEditor getLpEditor() {
		return lpEditor;
	}

	protected JComponent getFirstFocusableComponent() throws Exception {
		return wbuEdit;
	}

	public void setEditButtonMnemonic(int toSet) {
		wbuEdit.setMnemonic(toSet);
	}

	public void setEditButtonMnemonic(char toSet) {
		wbuEdit.setMnemonic(toSet);
	}

	public void setDefaultButtonMnemonic(int toSet) {
		wbuDefault.setMnemonic(toSet);
	}

	public void setDefaultButtonMnemonic(char toSet) {
		wbuDefault.setMnemonic(toSet);
	}
	
	@Override
	public boolean hasContent() throws Throwable {
		return getText() != null && !getText().trim().isEmpty();
	}
}
