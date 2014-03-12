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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Locale;

import javax.jcr.RepositoryException;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.io.FileUtils;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.frame.dialog.DialogFactory;
import com.lp.client.partner.PartnerFilterFactory;
import com.lp.client.pc.LPMain;
import com.lp.client.util.JCRFileUploader;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.jcr.service.DokumentbelegartDto;
import com.lp.server.system.jcr.service.DokumentgruppierungDto;
import com.lp.server.system.jcr.service.JCRDocDto;
import com.lp.server.system.jcr.service.JCRDocFac;
import com.lp.server.system.jcr.service.docnode.DocNodeBase;
import com.lp.server.system.jcr.service.docnode.DocNodeFile;
import com.lp.server.system.jcr.service.docnode.DocNodeJCR;
import com.lp.server.system.jcr.service.docnode.DocNodeLiteral;
import com.lp.server.system.jcr.service.docnode.DocNodeSymbolicLink;
import com.lp.server.system.jcr.service.docnode.DocNodeVersion;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.jcr.service.docnode.HeliumDocPath;
import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.MediaFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public class PanelDokumentenablage extends PanelDialog implements
		TreeSelectionListener, TreeModelListener, TreeExpansionListener,
		DropListener {

	private static final long serialVersionUID = 3513778783444609709L;

	private static final String MUST_LOAD_CHILDREN = "mustLoadChildren";
	
	private File lastSaveDir = null;

	private WrapperJTree tree;
	private DefaultTreeModel treeModel;
	private DocPath fullDocPath = null;
	private String sTable = null;
	private String sRow = null;
	private JCRDocDto jcrDocDto = null;
	private File file = null;
	private JScrollPane treeView = null;
	private boolean bShowExitButton = true;

	private boolean treeInProgress = false;

	private WrapperLabel wlaBelegart = new WrapperLabel(LPMain.getTextRespectUISPr("lp.belegart"));
	private WrapperComboBox wcbBelegart = new WrapperComboBox();
	private WrapperLabel wlaAnleger = new WrapperLabel(LPMain.getTextRespectUISPr("lp.anleger"));
	private WrapperTextField wtfAnleger = new WrapperTextField();
	private WrapperButton wbuPartner = null;
	private PanelQueryFLR panelQueryFLRPartner = null;
	private WrapperTextField wtfPartner = new WrapperTextField();
	private WrapperLabel wlaZeitpunkt = new WrapperLabel(LPMain.getTextRespectUISPr("lp.anlegezeitpunkt"));
	private WrapperTextField wdfZeitpunkt = new WrapperTextField();
	private WrapperLabel wlaBelegnummer = new WrapperLabel(LPMain.getTextRespectUISPr("jcr.label.datensatz"));
	private WrapperTextField wtfBelegnummer = new WrapperTextField();
	private WrapperLabel wlaSchlagworte = new WrapperLabel(LPMain.getTextRespectUISPr("lp.schlagworte"));
	private WrapperTextField wtfSchlagworte = new WrapperTextField();
	private WrapperLabel wlaName = new WrapperLabel(LPMain.getTextRespectUISPr("lp.name"));
	private WrapperTextField wtfName = new WrapperTextField();
	private WrapperLabel wlaFilename = new WrapperLabel(LPMain.getTextRespectUISPr("lp.dateiname"));
	private WrapperTextField wtfFilename = new WrapperTextField();
	private WrapperLabel wlaTable = new WrapperLabel(LPMain.getTextRespectUISPr("lp.tabelle"));
	private WrapperTextField wtfTable = new WrapperTextField();
	private WrapperLabel wlaRow = new WrapperLabel(LPMain.getTextRespectUISPr("lp.id"));
	private WrapperTextField wtfRow = new WrapperTextField();
	private WrapperLabel wlaMIME = new WrapperLabel(LPMain.getTextRespectUISPr("lp.dateityp"));
	private WrapperTextField wtfMIME = new WrapperTextField();
	private WrapperCheckBox wcbVersteckt = new WrapperCheckBox(LPMain.getTextRespectUISPr("lp.versteckt"));
	private WrapperLabel wlaSicherheitsstufe = new WrapperLabel(LPMain.getTextRespectUISPr("lp.sicherheitsstufe"));
	private WrapperComboBox wcbSicherheitsstufe = new WrapperComboBox();
	private WrapperLabel wlaGruppierung = new WrapperLabel(LPMain.getTextRespectUISPr("label.gruppierung"));
	private WrapperComboBox wcbGruppierung = new WrapperComboBox();
	private WrapperButton wbuChooseDoc = new WrapperButton(LPMain.getTextRespectUISPr("lp.dokumente.dokumentwaehlen"));
	private WrapperButton wbuShowDoc = new WrapperButton(LPMain.getTextRespectUISPr("lp.dokumente.anzeigen"));
	private WrapperButton wbuSaveDoc = new WrapperButton(LPMain.getTextRespectUISPr("lp.dokumente.speichern"));
	private WrapperLabel wlaVorschau = new WrapperLabel(LPMain.getTextRespectUISPr("lp.vorschau"));
	private WrapperMediaControl wmcMedia = null;
	private WrapperCheckBox wcbVersteckteAnzeigen = new WrapperCheckBox(LPMain.getTextRespectUISPr("lp.versteckte"));
	private WrapperButton wbuSuche = new WrapperButton(LPMain.getTextRespectUISPr("lp.suche"));
	private WrapperTextField wtfSuche = new WrapperTextField();
	private DragAndDropTarget dropArea = new DragAndDropTarget();

	private PartnerDto partnerDto = null;
	private PersonalDto personalDto = null;
	private DokumentgruppierungDto[] dokumentgruppierungDto = null;
	private DokumentbelegartDto[] dokumentbelegartDto = null;

	private boolean bHatStufe0 = false;
	private boolean bHatStufe1 = false;
	private boolean bHatStufe2 = false;
	private boolean bHatStufe3 = false;
	private boolean bHatStufe99 = false;

	private String defaultEinlesePfad = null;

	private boolean bNewNode = false;

	private boolean bVersteckteAnzeigen = false;

	private boolean bHatDokumentenablage = false;

	private static final String ACTION_SPECIAL_PARTNER = "action_special_partner";
	private static final String ACTION_SPECIAL_CHOOSE = "action_special_choose";
	private static final String ACTION_SPECIAL_SHOW = "action_special_show";
	private static final String ACTION_SPECIAL_SAVE = "action_special_save";

	private static final String ACTION_SPECIAL_SCAN = "action_special_scan";
	private final String BUTTON_SCAN = PanelBasis.ACTION_MY_OWN_NEW
			+ ACTION_SPECIAL_SCAN;

	// Konstruktor mit HeliumV Node als Einstiegspunkt
	public PanelDokumentenablage(InternalFrame internalFrameI,
			String addTitleI, Integer iPartnerIId) throws Throwable {
		this(internalFrameI, null, new HeliumDocPath(), addTitleI, null, false,
				iPartnerIId);
	}

	// Konstruktor mit Beleg als Einstiegspunkt
	public PanelDokumentenablage(InternalFrame internalFrameI,
			String addTitleI, DocPath docPath, String sTable, String sRow,
			boolean bShowExitButton, Integer iPartnerIId) throws Throwable {
		super(internalFrameI, addTitleI, bShowExitButton);
		if (addTitleI != null) {
			getInternalFrame().setTitle(
					getInternalFrame().getTitle() + " | "
							+ LPMain.getTextRespectUISPr("lp.dokumente"));
		}
		this.bShowExitButton = bShowExitButton;
		wmcMedia = new WrapperMediaControl(getInternalFrame(), "", false, true);
		this.fullDocPath = docPath;
		this.sTable = sTable;
		this.sRow = sRow;
		bHatStufe0 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_0_CU);
		bHatStufe1 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_1_CU);
		bHatStufe2 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_2_CU);
		bHatStufe3 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_3_CU);
		bHatStufe99 = DelegateFactory.getInstance().getTheJudgeDelegate()
				.hatRecht(RechteFac.RECHT_DOKUMENTE_SICHERHEITSSTUFE_99_CU);

		ArbeitsplatzparameterDto parameter = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.holeArbeitsplatzparameter(
						ParameterFac.ARBEITSPLATZPARAMETER_DOKUMENTE_EINLESE_PFAD);

		if (parameter != null && parameter.getCWert() != null) {
			defaultEinlesePfad = parameter.getCWert();
		}

		jbInit();
		initComponents();
		enableAllComponents(this, false);
		wcbVersteckteAnzeigen.setEnabled(true);
		wtfSuche.setEditable(true);
		wtfSuche.setColumnsMax(200);
		wbuSuche.setEnabled(true);
		if (iPartnerIId != null) {
			partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(iPartnerIId);
		} else {
			MandantDto mandantDto = DelegateFactory
					.getInstance()
					.getMandantDelegate()
					.mandantFindByPrimaryKey(
							LPMain.getTheClient().getMandant());
			iPartnerIId = mandantDto.getPartnerIId();
			partnerDto = DelegateFactory.getInstance().getPartnerDelegate()
					.partnerFindByPrimaryKey(iPartnerIId);
		}
	}

	private void setSearchedDocumentsOnTree(DefaultMutableTreeNode top,
			ArrayList<DocPath> docPaths) throws ExceptionLP,
			RepositoryException, Throwable {
		if (docPaths.size() == 0)
			treeModel.insertNodeInto(new DefaultMutableTreeNode("..."), top, 0);

		DefaultMutableTreeNode treeNode;
		for (DocPath path : docPaths) {
			treeNode = top;
			boolean firstNode = true;
			for (DocNodeBase node : path.asDocNodeList()) {
				if (firstNode) {
					firstNode = false;
				} else {
					int i = getDocNodeChildPos(node, treeNode);
					if (i == -1) {
						DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
								node);
						treeModel.insertNodeInto(newNode, treeNode,
								treeNode.getChildCount());
						treeNode = newNode;
					} else {
						treeNode = (DefaultMutableTreeNode) treeNode
								.getChildAt(i);
					}
				}
			}
		}
		tree.setModel(treeModel);
	}

	/**
	 * Gibt -1 zur&uuml;ck wenn kein docNode kein child von treeNode, sonst dessen
	 * Position (getChildAt(int i))
	 * 
	 * @param docNode
	 * @param treeNode
	 * @return
	 */
	private int getDocNodeChildPos(DocNodeBase docNode,
			DefaultMutableTreeNode treeNode) {
		for (int i = 0; i < treeNode.getChildCount(); i++) {
			if (docNode.toString().equals(
					((DefaultMutableTreeNode) treeNode.getChildAt(i))
							.getUserObject().toString()))
				return i;
		}
		return -1;
	}

	private void refresh() throws ExceptionLP, Throwable {
		List<DocNodeBase> parts = fullDocPath.getDeepCopy().asDocNodeList();
		boolean isSuche = wtfSuche.getText() != null
				&& !"".equals(wtfSuche.getText());

		DefaultMutableTreeNode dmtNode = null;
		Object[] toSelect = new Object[parts.size()];
		if (parts.size() > 0) {
			dmtNode = new DefaultMutableTreeNode(parts.get(0));
			toSelect[0] = dmtNode;
			treeModel = new DefaultTreeModel(dmtNode);
			treeModel.addTreeModelListener(this);
		}
		for (int i = 1; i < parts.size() && !isSuche; i++) {
			DefaultMutableTreeNode toAdd = new DefaultMutableTreeNode(
					parts.get(i));
			toSelect[i] = toAdd;
			treeModel.insertNodeInto(toAdd, dmtNode, dmtNode.getChildCount());
			dmtNode = toAdd;
		}
		TreePath treePath = new TreePath(toSelect);
		TreePath treePathOld = null;
		if (toSelect.length >= 2) {
			treePathOld = new TreePath(toSelect[toSelect.length - 2]);
		} else {
			treePathOld = new TreePath(toSelect);
		}
		// Value Changed damit die Buttons korrekt angezeigt werden
		valueChanged(new TreeSelectionEvent(tree, treePath, true, treePathOld,
				treePath));
		if (isSuche) {
			setSearchedDocumentsOnTree(dmtNode, DelegateFactory.getInstance()
					.getJCRDocDelegate().searchDocNodes(wtfSuche.getText()));
			for (int i = tree.getRowCount(); i > 0; i--) {
				tree.collapseRow(i);
			}
		} else if (dmtNode.getChildCount() == 0) {
			List<DocNodeBase> childDocNodes = DelegateFactory
					.getInstance().getJCRDocDelegate()
					.getDocNodeChildrenFromNode(fullDocPath);
			if (childDocNodes != null) {
				setDocNodesOnTree(dmtNode, childDocNodes, fullDocPath);
				tree.expandPath(treePath);
			}
		}
		tree.setSelectionPath(treePath);
	}

	private void jbInit() throws Throwable {
		if (LPMain
				.getInstance()
				.getDesktop()
				.darfAnwenderAufZusatzfunktionZugreifen(
						MandantFac.ZUSATZFUNKTION_DOKUMENTENABLAGE)) {
			if (!(new HeliumDocPath()).equals(fullDocPath)) {
				bHatDokumentenablage = true;
			}
		}
		if (bShowExitButton) {
			String[] aWhichButtonIUse = new String[] { PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_SAVE,
					PanelBasis.ACTION_DISCARD };
			enableToolsPanelButtons(aWhichButtonIUse);
			createAndSaveAndShowButton("/com/lp/client/res/scanner.png",
					"TWAIN-Import", BUTTON_SCAN, null);
		}
		dropArea.setCenterText(LPMain
				.getTextRespectUISPr("lp.datei.draganddrop.ablegen"));
		dropArea.setBackground(Color.LIGHT_GRAY);
		dropArea.setSupportFiles(true);
		dropArea.addDropListener(this);
		dropArea.setMinimumSize(new Dimension(200, 100));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		JPanel leftPane = new JPanel();
		JPanel rightPane = new JPanel();

		JLayeredPane rightLayered = new JLayeredPane();
		rightLayered.setLayout(new GridBagLayout());
		rightLayered.add(rightPane, new GridBagConstraints(1, 1, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));
		// rightLayered.setLayer(rightPane, 0, 1);
		if (bShowExitButton && bHatDokumentenablage)
			rightLayered.add(dropArea, new GridBagConstraints(1, 2, 1, 1, 1, 1,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
		// rightLayered.setLayer(dropArea, 1, 1);

		rightPane.setLayout(new GridBagLayout());

		tree = new WrapperJTree(treeModel);
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.setRowHeight(0);

		tree.setCellRenderer(new ToolTipCellRenderer());
		ToolTipManager.sharedInstance().registerComponent(tree);

		tree.addTreeSelectionListener(this);

		refresh();

		personalDto = DelegateFactory
				.getInstance()
				.getPersonalDelegate()
				.personalFindByPrimaryKey(
						LPMain.getTheClient().getIDPersonal());
		dokumentbelegartDto = DelegateFactory
				.getInstance()
				.getJCRDocDelegate()
				.dokumentbelegartfindbyMandant(
						LPMain.getTheClient().getMandant());
		for (int i = 0; i < dokumentbelegartDto.length; i++) {
			if (!JCRDocFac.DEFAULT_ARCHIV_BELEGART
					.equals(dokumentbelegartDto[i].getCNr()))
				wcbBelegart.addItem(dokumentbelegartDto[i].getCNr());
		}
		dokumentgruppierungDto = DelegateFactory
				.getInstance()
				.getJCRDocDelegate()
				.dokumentgruppierungfindbyMandant(
						LPMain.getTheClient().getMandant());
		for (int i = 0; i < dokumentgruppierungDto.length; i++) {
			if (!JCRDocFac.DEFAULT_ARCHIV_GRUPPE
					.equals(dokumentgruppierungDto[i].getCNr())
					|| !JCRDocFac.DEFAULT_KOPIE_GRUPPE
							.equals(dokumentgruppierungDto[i].getCNr())
					|| !JCRDocFac.DEFAULT_VERSANDAUFTRAG_GRUPPE
							.equals(dokumentgruppierungDto[i].getCNr())) {
				wcbGruppierung.addItem(dokumentgruppierungDto[i].getCNr());
			}
		}

		// Listen for when the selection changes.
		tree.addTreeExpansionListener(this);

		wcbVersteckteAnzeigen.setEnabled(true);
		wcbVersteckteAnzeigen.addActionListener(actionListener);

		wtfSuche.setEditable(true);
		wbuSuche.setEnabled(true);
		wbuSuche.addActionListener(actionListener);
		wbuPartner = new WrapperButton();

		wbuPartner.setText(LPMain.getTextRespectUISPr("button.partner"));
		wbuPartner.setToolTipText(LPMain
				.getTextRespectUISPr("button.partner.tooltip"));

		wbuPartner.setActionCommand(ACTION_SPECIAL_PARTNER);
		wbuPartner.addActionListener(this);
		wbuChooseDoc.setActionCommand(ACTION_SPECIAL_CHOOSE);
		wbuChooseDoc.addActionListener(this);
		wbuShowDoc.setActionCommand(ACTION_SPECIAL_SHOW);
		wbuShowDoc.addActionListener(this);
		wbuSaveDoc.setActionCommand(ACTION_SPECIAL_SAVE);
		wbuSaveDoc.addActionListener(this);

		wtfPartner = new WrapperTextField();
		wtfPartner.setColumnsMax(Facade.MAX_UNBESCHRAENKT);
		wtfPartner.setActivatable(false);
		wtfAnleger.setActivatable(false);
		wdfZeitpunkt.setActivatable(false);
		wtfBelegnummer.setActivatable(false);
		wtfTable.setActivatable(false);
		wtfRow.setActivatable(false);
		wtfFilename.setActivatable(false);
		wtfFilename.setColumnsMax(100);
		wtfMIME.setActivatable(false);
		wcbVersteckt.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				wtfSchlagworte.setMandatoryField(!wcbVersteckt.isSelected());
			}
		});

		if (bHatStufe0) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_NONE);
		}
		if (bHatStufe1) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_LOW);
		}
		if (bHatStufe2) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_MEDIUM);
		}
		if (bHatStufe3) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_HIGH);
		}
		if (bHatStufe99) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_ARCHIV);
		}

		wtfTable.setMandatoryField(true);
		wtfName.setMandatoryField(true);
		wtfName.setColumnsMax(200);
		wtfBelegnummer.setMandatoryField(true);
		wtfRow.setMandatoryField(true);
		wtfFilename.setMandatoryField(true);
		wtfMIME.setMandatoryField(true);
		wtfAnleger.setMandatoryField(true);
		wtfSchlagworte.setMandatoryField(true);
		wtfSchlagworte.setColumnsMax(300);
		wdfZeitpunkt.setMandatoryField(true);
		wtfPartner.setMandatoryField(true);

		treeView = new JScrollPane(tree);
		treeView.setMinimumSize(new Dimension(200, 10));
		treeView.setPreferredSize(new Dimension(200, 10));

		iZeile = 0;
		if (!bShowExitButton) {
			jpaWorkingOn.add(wtfSuche, new GridBagConstraints(0, iZeile, 1, 1,
					0.2, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0));
			jpaWorkingOn.add(wbuSuche, new GridBagConstraints(1, iZeile, 1, 1,
					0.1, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
					new Insets(2, 2, 2, 2), 0, 0));
			iZeile++;
		}
		jpaWorkingOn.add(wcbVersteckteAnzeigen, new GridBagConstraints(0,
				iZeile, 1, 1, 1, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;
		leftPane.add(treeView);

		jpaWorkingOn.add(splitPane, new GridBagConstraints(0, iZeile, 3, 1,
				1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile = 0;
		rightPane.add(wlaName, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		rightPane.add(wtfName, new GridBagConstraints(1, iZeile, 3, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		rightPane.add(wlaTable, new GridBagConstraints(4, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wtfTable, new GridBagConstraints(5, iZeile, 2, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		rightPane.add(wlaSchlagworte, new GridBagConstraints(0, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wtfSchlagworte, new GridBagConstraints(1, iZeile, 6, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		rightPane.add(wlaZeitpunkt, new GridBagConstraints(0, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wdfZeitpunkt, new GridBagConstraints(1, iZeile, 3, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wlaSicherheitsstufe, new GridBagConstraints(4, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wcbSicherheitsstufe, new GridBagConstraints(5, iZeile, 1,
				1, 1.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wcbVersteckt, new GridBagConstraints(6, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		rightPane.add(wlaBelegnummer, new GridBagConstraints(0, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wtfBelegnummer, new GridBagConstraints(1, iZeile, 3, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wlaRow, new GridBagConstraints(4, iZeile, 1, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		rightPane.add(wtfRow, new GridBagConstraints(5, iZeile, 2, 1, 1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						2, 2, 2, 2), 0, 0));
		iZeile++;
		rightPane.add(wlaFilename, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wtfFilename, new GridBagConstraints(1, iZeile, 3, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wlaMIME, new GridBagConstraints(4, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wtfMIME, new GridBagConstraints(5, iZeile, 2, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		rightPane.add(wlaBelegart, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wcbBelegart, new GridBagConstraints(1, iZeile, 3, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wlaGruppierung, new GridBagConstraints(4, iZeile, 1, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wcbGruppierung, new GridBagConstraints(5, iZeile, 2, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		rightPane.add(wbuPartner, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wtfPartner, new GridBagConstraints(1, iZeile, 3, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wlaAnleger, new GridBagConstraints(4, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wtfAnleger, new GridBagConstraints(5, iZeile, 2, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		iZeile++;

		rightPane.add(wbuChooseDoc, new GridBagConstraints(0, iZeile, 3, 1,
				1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		rightPane.add(wbuShowDoc, new GridBagConstraints(3, iZeile, 2, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		rightPane.add(wbuSaveDoc, new GridBagConstraints(5, iZeile, 2, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		rightPane.add(wlaVorschau, new GridBagConstraints(0, iZeile, 1, 1, 1.0,
				0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		iZeile++;
		rightPane.add(wmcMedia, new GridBagConstraints(0, iZeile, 7, 4, 1.0,
				0.5, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		splitPane.setLeftComponent(treeView);
		splitPane.setRightComponent(rightLayered);
	}

	private void setDocNodesOnTree(DefaultMutableTreeNode top,
			List<DocNodeBase> docNodes, DocPath docPath)
			throws ExceptionLP, RepositoryException, Throwable {
		treeModel.reload(top);
		DefaultMutableTreeNode treeNode = null;
		if (docNodes != null) {
			for (DocNodeBase docNode : docNodes) {
				treeNode = new DefaultMutableTreeNode(docNode);
				boolean bDarfDocSehen = true;
				JCRDocDto jcrDocDto = null;
				if (docNode.getNodeType() == DocNodeBase.FILE)
					jcrDocDto = ((DocNodeFile) docNode).getJcrDocDto();
				if (jcrDocDto != null) {
					int iSicherheitsstufe = (int) jcrDocDto
							.getlSicherheitsstufe();
					bDarfDocSehen = false;
					switch (iSicherheitsstufe) {
					case (int) JCRDocFac.SECURITY_NONE:
						if (bHatStufe0) {
							bDarfDocSehen = true;
						}
						break;
					case (int) JCRDocFac.SECURITY_LOW:
						if (bHatStufe1) {
							bDarfDocSehen = true;
						}
						break;
					case (int) JCRDocFac.SECURITY_MEDIUM:
						if (bHatStufe2) {
							bDarfDocSehen = true;
						}
						break;
					case (int) JCRDocFac.SECURITY_HIGH:
						if (bHatStufe3) {
							bDarfDocSehen = true;
						}
						break;
					case (int) JCRDocFac.SECURITY_ARCHIV:
						if (bHatStufe99) {
							bDarfDocSehen = true;
						}
						break;
					}
				}

				if (bDarfDocSehen) {
					if (jcrDocDto != null) {
						ArrayList<DocNodeVersion> versions = DelegateFactory
								.getInstance().getJCRDocDelegate()
								.getAllDocumentVersions(jcrDocDto);
						if (versions.size() > 0) {
							boolean alleVersteckt = true;
							for (DocNodeVersion version : versions) {
								if(!version.getJCRDocDto().getbVersteckt()) {
									alleVersteckt = false;
									break;
								}
							}
							if(bVersteckteAnzeigen || !alleVersteckt) {
								treeModel.insertNodeInto(treeNode, top,
										top.getChildCount());
							}
							for (DocNodeVersion version : versions) {
								if(bVersteckteAnzeigen || !version.getJCRDocDto().getbVersteckt()) {
									treeModel.insertNodeInto(
											new DefaultMutableTreeNode(version),
											treeNode, treeNode.getChildCount());
								}
							}
						} else if(bVersteckteAnzeigen || !jcrDocDto.getbVersteckt()){
							treeModel.insertNodeInto(treeNode, top,
									top.getChildCount());
						}
					}
					if (jcrDocDto == null) {
						if(docNode.getNodeType() == DocNodeBase.SYMBOLIC_LINK) {
							DocPath path = ((DocNodeSymbolicLink)docNode).getViewPath().getDeepCopy();
							path.asDocNodeList().remove(path.getLastDocNode());
							path.add(docNode);
							addNotExistingNodes(path, top);
						} else {
							treeModel.insertNodeInto(treeNode, top,
									top.getChildCount());
							treeModel.insertNodeInto(new DefaultMutableTreeNode(
									MUST_LOAD_CHILDREN), treeNode, 0);
						}
					}
				}
			}
		}
	}
	
	private void addNotExistingNodes(DocPath pathToAdd, DefaultMutableTreeNode top) {
		if(pathToAdd == null || pathToAdd.size() == 0) {
			treeModel.insertNodeInto(new DefaultMutableTreeNode(
					MUST_LOAD_CHILDREN), top, 0);
			return;
		}
		boolean exists = false;
		DefaultMutableTreeNode node = null;
		for(int i = 0; i < top.getChildCount(); i++) {
			node = (DefaultMutableTreeNode)top.getChildAt(i);
			if(pathToAdd.asDocNodeList().get(0).toString().equals(node.getUserObject().toString())) {
					exists = true;
					break;
			}
		}
		if(!exists) {
			node = new DefaultMutableTreeNode(pathToAdd.asDocNodeList().get(0));
			treeModel.insertNodeInto(node, top, top.getChildCount());
		}
		DocPath subPath = pathToAdd.getDeepCopy();
		subPath.asDocNodeList().remove(0);
		addNotExistingNodes(subPath, node);
	}

	public void valueChanged(TreeSelectionEvent arg0) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();

		if (tree.getPathBounds(arg0.getPath()) != null)
			tree.scrollRectToVisible(tree.getPathBounds(arg0.getPath()));

		jcrDocDto = null;
		file = null;
		clearComponents();
		enableAllComponents(this, false);
		wcbVersteckteAnzeigen.setEnabled(true);
		wtfSuche.setEditable(true);
		wbuSuche.setEnabled(true);
		wmcMedia.setMimeType(null);
		if (node != null) {
			enableToolsPanelButtons(false, PanelBasis.ACTION_SAVE,
					PanelBasis.ACTION_DISCARD);
			if (node.getUserObject() instanceof DocNodeBase) {
				Object[] objectPath = node.getUserObjectPath();
				DocPath selectionDocPath = new DocPath();
				for (Object object : objectPath) {
					if (object instanceof DocNodeBase) {
						selectionDocPath.add((DocNodeBase) object);
					} else if (object instanceof String) {
						selectionDocPath.add(new DocNodeLiteral(object
								.toString() + "_string"));
					}
				}
				DocNodeBase selectedDocNode = (DocNodeBase) node
						.getUserObject();

				if (selectedDocNode.getNodeType() == DocNodeBase.FILE
						&& node.isLeaf()) {
					jcrDocDto = ((DocNodeFile) selectedDocNode).getJcrDocDto();
				} else if (selectedDocNode.getNodeType() == DocNodeBase.VERSION) {
					jcrDocDto = ((DocNodeVersion) selectedDocNode)
							.getJCRDocDto();
				} else {
					jcrDocDto = null;
				}
				if (jcrDocDto != null) {
					if (!jcrDocDto.getbVersteckt() || bVersteckteAnzeigen) {
						dto2Components();
						dropArea.setVisible(false);
						enableToolsPanelButtons(false, PanelBasis.ACTION_NEW,
								BUTTON_SCAN);
						enableToolsPanelButtons(
								selectedDocNode.getNodeType() == DocNodeBase.FILE
										|| selectedDocNode.getNodeType() == DocNodeBase.VERSION
										&& bHatMindestensEineSicherheitsStufe()
										&& bHatDokumentenablage,
								PanelBasis.ACTION_UPDATE);

						wbuShowDoc.setEnabled(true);
						wbuSaveDoc.setEnabled(true);
					}
				} else {
					enableToolsPanelButtons(false, PanelBasis.ACTION_UPDATE);
					boolean b = fullDocPath.asDocNodeList().size() > 0
							&& selectedDocNode instanceof DocNodeJCR
							&& bHatMindestensEineSicherheitsStufe()
							&& bHatDokumentenablage;
					enableToolsPanelButtons(b, PanelBasis.ACTION_NEW,
							BUTTON_SCAN);
					dropArea.setVisible(b);
				}
			}
		} else {
			enableToolsPanelButtons(false, ACTION_UPDATE,
					PanelBasis.ACTION_NEW, BUTTON_SCAN);
			dropArea.setVisible(false);
		}
	}

	private void dto2Components() {
		try {
			JCRDocDto dataJCR = DelegateFactory.getInstance()
					.getJCRDocDelegate().getData(jcrDocDto);
			if (dataJCR != null) {
				jcrDocDto = dataJCR;
			}
		} catch (Throwable t) {
		}
		if (jcrDocDto.getbData() != null) {
			if (file == null) {
				try {
					file = File.createTempFile(jcrDocDto.getsFilename(),
							jcrDocDto.getsMIME());
					FileOutputStream fosFileGetter = new FileOutputStream(file);
					fosFileGetter.write(jcrDocDto.getbData());
					fosFileGetter.close();
				} catch (Exception e) {
				}
			}
		}
		PersonalDto anlegerDto = null;
		try {
			anlegerDto = DelegateFactory
					.getInstance()
					.getPersonalDelegate()
					.personalFindByPrimaryKey(
							new Integer((int) jcrDocDto.getlAnleger()));
		} catch (Throwable e1) {

		}
		if (anlegerDto == null) {
			wtfAnleger.setText("");
		} else {
			wtfAnleger.setText(anlegerDto.getCKurzzeichen());
		}
		try {
			if (jcrDocDto.getlPartner() != null) {
				partnerDto = DelegateFactory
						.getInstance()
						.getPartnerDelegate()
						.partnerFindByPrimaryKey(
								jcrDocDto.getlPartner().intValue());
			}
		} catch (ExceptionLP e) {
			// e.printStackTrace();
		}
		if (partnerDto != null) {
			wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
		} else {
			wtfPartner.setText("");
		}
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT, Locale.GERMANY);
		wdfZeitpunkt
				.setText(df.format(new Timestamp(jcrDocDto.getlZeitpunkt())));
		wtfBelegnummer.setText(jcrDocDto.getsBelegnummer());
		wtfSchlagworte.setText(jcrDocDto.getsSchlagworte());
		wtfName.setText(jcrDocDto.getsName());
		wtfFilename.setText(jcrDocDto.getsFilename());
		wtfTable.setText((jcrDocDto.getsTable() == null || jcrDocDto
				.getsTable().isEmpty()) ? " " : jcrDocDto.getsTable());
		wtfRow.setText(jcrDocDto.getsRow());
		wtfMIME.setText(jcrDocDto.getsMIME());
		wcbVersteckt.setSelected(jcrDocDto.getbVersteckt());
		if (jcrDocDto.getlSicherheitsstufe() == JCRDocFac.SECURITY_ARCHIV) {
			wcbSicherheitsstufe.addItem(JCRDocFac.SECURITY_ARCHIV);
		}
		wcbSicherheitsstufe.setSelectedItem(jcrDocDto.getlSicherheitsstufe());
		if (JCRDocFac.DEFAULT_ARCHIV_BELEGART.equals(jcrDocDto.getsBelegart())) {
			wcbBelegart.addItem(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		}
		wcbBelegart.setSelectedItem(jcrDocDto.getsBelegart());
		wcbGruppierung.setSelectedItem(jcrDocDto.getsGruppierung());
		try {
			if (".JPG".equals(jcrDocDto.getsMIME().toUpperCase())
					|| ".JPEG".equals(jcrDocDto.getsMIME().toUpperCase())) {
				wmcMedia.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_IMAGE_JPEG);
				wmcMedia.setOMedia(Helper.getBytesFromFile(file));
			} else if (".JRPRINT".equals(jcrDocDto.getsMIME().toUpperCase())) {
				wmcMedia.setMimeType(MediaFac.DATENFORMAT_MIMETYPE_APP_JASPER);
				wmcMedia.setOMedia(Helper.getBytesFromFile(file));
			} else {
				wmcMedia.setMimeType(null);
			}
		} catch (Throwable e) {
		}
	}

	private void components2Dto() throws ExceptionLP {
		jcrDocDto = new JCRDocDto();
		try {
			jcrDocDto.setbData(Helper.getBytesFromFile(file));
		} catch (Exception e) {
			throw new ExceptionLP(
					EJBExceptionLP.FEHLER_JCR_DATEI_KONNTE_NICHT_GELESEN_WERDEN,
					e);
		}
		jcrDocDto.setDocPath(fullDocPath.getDeepCopy().add(
				new DocNodeFile(wtfName.getText())));
		jcrDocDto.setsBelegart((String) wcbBelegart.getSelectedItem());
		jcrDocDto.setlAnleger(personalDto.getIId());
		jcrDocDto.setlPartner(partnerDto.getIId());
		jcrDocDto.setlZeitpunkt(System.currentTimeMillis());
		jcrDocDto.setsBelegnummer(wtfBelegnummer.getText());
		jcrDocDto.setsSchlagworte(wtfSchlagworte.getText());
		jcrDocDto.setsName(wtfName.getText());
		jcrDocDto.setsFilename(wtfFilename.getText());
		jcrDocDto.setsTable(wtfTable.getText());
		jcrDocDto.setsRow(wtfRow.getText());
		jcrDocDto.setsMIME(wtfMIME.getText());
		jcrDocDto.setlSicherheitsstufe((Long) wcbSicherheitsstufe
				.getSelectedItem());
		jcrDocDto.setsGruppierung((String) wcbGruppierung.getSelectedItem());
		jcrDocDto.setbVersteckt(wcbVersteckt.isSelected());
	}

	private void clearComponents() {
		wtfAnleger.setText(null);
		wtfPartner.setText(null);
		wdfZeitpunkt.setText(null);
		wtfBelegnummer.setText(null);
		wtfSchlagworte.setText(null);
		wtfName.setText(null);
		wtfFilename.setText(null);
		wtfTable.setText(null);
		wtfRow.setText("0");
		wtfMIME.setText(null);
		wcbVersteckt.setSelected(false);
		wcbSicherheitsstufe.setSelectedItem(JCRDocFac.SECURITY_NONE);
		wcbSicherheitsstufe.removeItem(JCRDocFac.SECURITY_ARCHIV);
		wcbBelegart.removeItem(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		try {
			wmcMedia.setOMedia(null);
		} catch (Throwable e) {
		}
	}

	protected void eventActionUpdate(ActionEvent aE, boolean bNeedNoUpdateI)
			throws Throwable {
		bNewNode = false;

		enableToolsPanelButtons(true, PanelBasis.ACTION_SAVE,
				PanelBasis.ACTION_DISCARD, BUTTON_SCAN);
		enableToolsPanelButtons(false, PanelBasis.ACTION_NEW,
				PanelBasis.ACTION_UPDATE);

		if (jcrDocDto.getlSicherheitsstufe() == JCRDocFac.SECURITY_ARCHIV) {
			wtfSchlagworte.setEditable(true);
			if (LPMain.getInstance().isLPAdmin()) {
				wcbSicherheitsstufe.setEnabled(true);
			}
		} else {
			enableAllComponents(this, true);
		}
		tree.setEnabled(false);
		wcbVersteckteAnzeigen.setEnabled(false);
		wtfName.setEditable(false);
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT, Locale.GERMANY);
		wdfZeitpunkt
				.setText(df.format(new Timestamp(System.currentTimeMillis())));
		wtfAnleger.setText(personalDto.getCKurzzeichen());
	}

	public void eventActionNew(EventObject eventObject, boolean bLockMeI,
			boolean bNeedNoNewI) throws Throwable {
		bNewNode = true;
		clearComponents();
		if (chooseFile()) {
			enableToolsPanelButtons(true, PanelBasis.ACTION_SAVE,
					PanelBasis.ACTION_DISCARD, BUTTON_SCAN);
			enableToolsPanelButtons(false, PanelBasis.ACTION_NEW,
					PanelBasis.ACTION_UPDATE);
			setDefaultsForNew();
			enableAllComponents(this, true);
			tree.setEnabled(false);
			wcbVersteckteAnzeigen.setEnabled(false);
		}

	}

	public void eventActionSave(ActionEvent e, boolean bNeedNoSaveI)
			throws Throwable {
		ParametermandantDto param = DelegateFactory
				.getInstance()
				.getParameterDelegate()
				.getMandantparameter(
						LPMain.getTheClient().getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_ALLGEMEIN_DOKUMENTE_MAXIMALE_GROESSE);
		Integer lFileSize = (Integer) param.getCWertAsObject();
		// Umrechnung in Byte
		Integer lFileSizeByte = lFileSize * 1024;
		boolean bSpeichern = true;
		if (file.length() > lFileSizeByte) {
			boolean bHatRechtImmerZuSpeichern = DelegateFactory
					.getInstance()
					.getTheJudgeDelegate()
					.hatRecht(
							RechteFac.RECHT_DOKUMENTE_DARF_DOKUMENTE_GROESSER_ALS_MAX_ZULAESSIG_SPEICHERN);
			if (bHatRechtImmerZuSpeichern) {
				boolean bYes = DialogFactory
						.showModalJaNeinDialog(
								getInternalFrame(),
								LPMain.getTextRespectUISPr(
												"lp.dokumente.datei.zugros.trotzdem.speichern"));
				if (!bYes) {
					bSpeichern = false;
				}
			} else {
				bSpeichern = false;
				DialogFactory.showModalDialog(
						LPMain.getTextRespectUISPr("lp.hinweis"),
						LPMain.getTextRespectUISPr(
								"lp.error.dateizugross")
								+ "\n "
								+ LPMain.getTextRespectUISPr(
										"lp.datei.maxgroesse")
								+ ": "
								+ lFileSize
								+ "Kb "
								+ LPMain.getTextRespectUISPr(
										"lp.datei.dateigroesse")
								+ ": "
								+ (file.length() / 1024) + "Kb");
			}
		}
		if (bSpeichern) {
			if (allMandatoryFieldsSetDlg()) {
				boolean bSaveAllowed = true;
				JCRDocDto temp = jcrDocDto;
				if (temp != null) {
					if (temp.getsSchlagworte() == "")
						temp.setsSchlagworte(null);
					String path = jcrDocDto.getsPath(); // der wirkliche Pfad der Version
					components2Dto();
					temp.setlZeitpunkt(jcrDocDto.getlZeitpunkt());
					if (jcrDocDto.equals(temp)) {
						bSaveAllowed = true; // keine Aenderungen, einfach speichern
					} else {
						temp.setbVersteckt(jcrDocDto.getbVersteckt());
						if (jcrDocDto.equals(temp)) {
							DelegateFactory
									.getInstance()
									.getJCRDocDelegate()
									.setVisibilityOfDocument(
											jcrDocDto.getsPath(), path,
											jcrDocDto.getbVersteckt());
							bSaveAllowed = false;
						}
					}
				}
				if (bSaveAllowed) {
					components2Dto();
					if (bNewNode) {
						// Neuer Knoten
						if (DelegateFactory.getInstance().getJCRDocDelegate()
								.checkIfNodeExists(jcrDocDto.getDocPath())) {
							throw new ExceptionLP(
									EJBExceptionLP.FEHLER_JCR_KNOTEN_EXISTIERT_BEREITS,
									null);
						}
					}
					enableToolsPanelButtons(false, PanelBasis.ACTION_SAVE,
							PanelBasis.ACTION_DISCARD,
							PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_NEW,
							BUTTON_SCAN);

					DelegateFactory.getInstance().getJCRDocDelegate()
							.addNewDocumentOrNewVersionOfDocument(jcrDocDto);

					if (defaultEinlesePfad != null) {
						try {
							FileUtils.forceDelete(file);
						} catch (java.io.IOException e1) {

							DialogFactory.showModalDialog(
									LPMain.getTextRespectUISPr(
											"lp.error"),
									LPMain.getTextRespectUISPr(
											"lp.error.dokument.loeschen")
											+ " " + e1.getMessage());
						}
					}
				} else {
				}
				enableAllComponents(this, false);
				tree.setEnabled(true);
				wcbVersteckteAnzeigen.setEnabled(true);
				wcbVersteckteAnzeigen.setEnabled(true);
				wtfSuche.setEditable(true);
				wbuSuche.setEnabled(true);
				refresh();
			}
		}
	}

	public void setDefaultsForNew() throws Throwable {
		wtfAnleger.setText(personalDto.getCKurzzeichen());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT, Locale.GERMANY);
		wdfZeitpunkt
				.setText(df.format(new Timestamp(System.currentTimeMillis())));
		wtfFilename.setText(file.getName());
		wtfMIME.setText(Helper.getMime(file.getName()));
		wcbVersteckt.setSelected(false);
		if (bHatStufe3) {
			wcbSicherheitsstufe.setSelectedItem(JCRDocFac.SECURITY_HIGH);
		} else if (bHatStufe2) {
			wcbSicherheitsstufe.setSelectedItem(JCRDocFac.SECURITY_MEDIUM);
		} else if (bHatStufe1) {
			wcbSicherheitsstufe.setSelectedItem(JCRDocFac.SECURITY_LOW);
		} else if (bHatStufe0) {
			wcbSicherheitsstufe.setSelectedItem(JCRDocFac.SECURITY_NONE);
		}
		wcbSicherheitsstufe.removeItem(JCRDocFac.SECURITY_ARCHIV);
		wcbBelegart.removeItem(JCRDocFac.DEFAULT_ARCHIV_BELEGART);
		wtfBelegnummer.setText(tree.getLastSelectedPathComponent().toString());
		wtfTable.setText(sTable);
		wtfRow.setText(sRow);
		wtfPartner.setText(partnerDto.formatFixTitelName1Name2());

	}

	protected void eventActionSpecial(ActionEvent e) throws Throwable {
		if (e.getActionCommand().equals(ACTION_SPECIAL_PARTNER)) {
			panelQueryFLRPartner = PartnerFilterFactory.getInstance()
					.createPanelFLRPartner(getInternalFrame(),
							partnerDto == null ? null : partnerDto.getIId(),
							false);
			new DialogQuery(panelQueryFLRPartner);
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_CHOOSE)) {
			chooseFile();
			wtfFilename.setText(file.getName());
			if (file.getName().lastIndexOf(".") != -1) {
				wtfMIME.setText(file.getName().substring(
						file.getName().lastIndexOf(".")));
			} else {
				wtfMIME.setText("");
			}
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SHOW)) {
			showFile();
		} else if (e.getActionCommand().equals(ACTION_SPECIAL_SAVE)) {
			saveFile();
		} else if (e.getActionCommand().equals(BUTTON_SCAN)) {
			try {
				if (file == null) {
					// Es wird ein Neues Dokument angelegt
					bNewNode = true;
					clearComponents();

					file = getInternalFrame().scanFile();

					if (file != null) {
						enableToolsPanelButtons(true, PanelBasis.ACTION_SAVE,
								PanelBasis.ACTION_DISCARD, BUTTON_SCAN);
						enableToolsPanelButtons(false,
								PanelBasis.ACTION_UPDATE, PanelBasis.ACTION_NEW);
						setDefaultsForNew();
						enableAllComponents(this, true);

						tree.setEnabled(false);
					}
				} else {
					// Es wird eine neue Version angelegt
					file = getInternalFrame().scanFile();
					wtfFilename.setText(file.getName());
					if (file.getName().lastIndexOf(".") != -1) {
						wtfMIME.setText(file.getName().substring(
								file.getName().lastIndexOf(".")));
					} else {
						wtfMIME.setText("");
					}
				}
			} catch (Throwable t) {
				DialogFactory.showModalDialog(LPMain.getTextRespectUISPr("lp.error"), LPMain.getTextRespectUISPr("lp.error.scannen"));

			}

		} else {
			super.eventActionSpecial(e);
		}
	}

	private boolean chooseFile() throws Throwable {
		final JFileChooser fc = new JFileChooser();

		if (defaultEinlesePfad != null) {
			fc.setCurrentDirectory(new File(defaultEinlesePfad));
		}

		int retVal = fc.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			try {
				wmcMedia.setOMedia(Helper.getBytesFromFile(file));
				return true;
			} catch (IOException e) {
				return false;
			} catch (Throwable e) {
				return false;
			}
		} else {
			return false;
		}

	}
	
	private void saveFile() {
		createTempFile();
		File newFile = lastSaveDir != null ? new File(lastSaveDir.getParentFile(), jcrDocDto.getsFilename()) :
			new File(jcrDocDto.getsFilename());
		newFile = HelperClient.showSaveFileDialog(file, newFile, this, null, jcrDocDto.getsMIME());
		if(newFile != null) lastSaveDir = newFile;
	}
	
	private void createTempFile() {
		try {
			if (file == null) {
				if (jcrDocDto.getbData() == null) {
					// TODO: Ordentliche Fehlermeldung
					return;
				}
				file = File.createTempFile(jcrDocDto.getsFilename(),
						jcrDocDto.getsMIME());
				FileOutputStream fosFileGetter = new FileOutputStream(file);
				fosFileGetter.write(jcrDocDto.getbData());
				fosFileGetter.close();
				file.deleteOnExit();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	private void showFile() throws IOException {
		createTempFile();
		HelperClient.desktopTryToOpenElseSave(file, getInternalFrame());
//		try {
//			HelperClient.desktopOpenEx(file);
//		} catch (IOException dex) {
//			// catch Exception Windows throws if no application is associated
//			if (dex.getMessage().startsWith("Failed to open file")) {
//				JFileChooser fc = new JFileChooser();
//				fc.setDialogType(JFileChooser.SAVE_DIALOG);
//				fc.setSelectedFile(file);
//				int returnVal = fc.showSaveDialog(getInternalFrame());
//				if (returnVal == JFileChooser.APPROVE_OPTION) {
//					File file = fc.getSelectedFile();
//					FileOutputStream foStream = new FileOutputStream(file);
//					foStream.write(Helper.getBytesFromFile(file));
//					foStream.close();
//				}
//			} else {
//				// catch exception thrown by mac if no application is associated
//				if (dex.getMessage()
//						.startsWith(
//								"Failed to launch the associated application with the specified file")) {
//					JFileChooser fc = new JFileChooser();
//					fc.setDialogType(JFileChooser.SAVE_DIALOG);
//					fc.setSelectedFile(file);
//					int returnVal = fc.showSaveDialog(getInternalFrame());
//					if (returnVal == JFileChooser.APPROVE_OPTION) {
//						File file = fc.getSelectedFile();
//						FileOutputStream foStream = new FileOutputStream(file);
//						foStream.write(Helper.getBytesFromFile(file));
//						foStream.close();
//					}
//				} else {
//					throw dex;
//				}
//			}
//		}
	}

	protected void eventItemchanged(EventObject eI) throws Throwable {
		ItemChangedEvent e = (ItemChangedEvent) eI;
		if (e.getID() == ItemChangedEvent.GOTO_DETAIL_PANEL) {
			if (e.getSource() == panelQueryFLRPartner) {
				Object key = ((ISourceEvent) e.getSource()).getIdSelected();
				if (key != null) {
					partnerDto = DelegateFactory.getInstance()
							.getPartnerDelegate()
							.partnerFindByPrimaryKey((Integer) key);
					wtfPartner.setText(partnerDto.formatFixTitelName1Name2());
				}
			}
		}
	}

	@Override
	protected void eventActionDiscard(ActionEvent e) throws Throwable {
		TreePath path = tree.getSelectionPath();
		tree.setSelectionPath(null);
		tree.setSelectionPath(path);
		tree.setEnabled(true);
		tree.requestFocus();
		wcbVersteckteAnzeigen.setEnabled(true);
	}

	private boolean bHatMindestensEineSicherheitsStufe() {
		return bHatStufe0 || bHatStufe1 || bHatStufe2 || bHatStufe3
				|| bHatStufe99;
	}

	public void treeNodesChanged(TreeModelEvent arg0) {
	}

	public void treeNodesInserted(TreeModelEvent arg0) {
		tree.setModel(treeModel);
	}

	public void treeNodesRemoved(TreeModelEvent arg0) {
	}

	public void treeStructureChanged(TreeModelEvent arg0) {
	}

	ActionListener actionListener = new ActionListener() {
		public void actionPerformed(ActionEvent actionEvent) {
			bVersteckteAnzeigen = wcbVersteckteAnzeigen.isSelected();
			try {
				refresh();
			} catch (Throwable e) {
			}
		}
	};

	public WrapperJTree getTree() {
		return tree;
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent arg0) {
	}

	private void setBusy(boolean b) {
		treeInProgress = b;
		if (b) {
			getInternalFrame().getFrameProgress().start(
					LPMain.getTextRespectUISPr("lp.working"));
		} else {
			getInternalFrame().getFrameProgress().pause();
		}
	}

	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		if (treeInProgress)
			return;
		setBusy(true);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath()
				.getLastPathComponent();
		DefaultMutableTreeNode firstChild = (DefaultMutableTreeNode) node
				.getFirstChild();
		TreePath selectionPath = tree.getSelectionPath();
		if (MUST_LOAD_CHILDREN.equals(firstChild.getUserObject())) {

			node.removeAllChildren();
			DocPath expandPath = new DocPath();

			for (Object obj : node.getUserObjectPath()) {
				try {
					expandPath.add((DocNodeBase) obj);
				} catch (ClassCastException ex) {
					expandPath.add(new DocNodeLiteral(obj.toString()));
				}
			}

			List<DocNodeBase> childList;
			try {
				childList = DelegateFactory.getInstance().getJCRDocDelegate()
						.getDocNodeChildrenFromNode(expandPath);
				setDocNodesOnTree(node, childList, expandPath);
			} catch (Throwable e) {
				treeModel
						.insertNodeInto(new DefaultMutableTreeNode(e), node, 0);
			}
			tree.expandPath(new TreePath(node.getPath()));
			tree.setSelectionPath(selectionPath);
		}
		setBusy(false);
	}

	private class ToolTipCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = 8159531411916759571L;

		private Icon fileAttachmentIcon = HelperClient
				.createImageIcon("document_attachment16x16.png");
		private Icon fileErrorIcon = HelperClient
				.createImageIcon("delete2.png");
		private Icon mailAttachmentIcon = HelperClient
				.createImageIcon("mail.png");

		private JLabel label;

		public ToolTipCellRenderer() {
			label = new JLabel();
			label.setOpaque(true);
		}

		private Icon getIconForNodeType(int nodeType) {
			switch (nodeType) {
			case DocNodeBase.FILE:
			case DocNodeBase.VERSION:
				return fileAttachmentIcon;
			case DocNodeBase.MAIL:
				return mailAttachmentIcon;
			default:
				return getDefaultClosedIcon();
			}
		}

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			if (value instanceof DefaultMutableTreeNode) {
				Object obj = ((DefaultMutableTreeNode) value).getUserObject();
				if (obj instanceof DocNodeBase) {
					DocNodeBase node = (DocNodeBase) obj;
					if (LPMain.getInstance().isLPAdmin())
						label.setToolTipText(node.asEncodedPath() + " ("
								+ obj.getClass().getSimpleName() + ")");

					label.setIcon(getIconForNodeType(node.getNodeType()));
				} else if (obj instanceof Throwable) {
					Throwable ex = (Throwable) obj;
					this.setToolTipText(ex.getMessage());

					label.setIcon(fileErrorIcon);
					value = LPMain
							.getTextRespectUISPr("system.jcr.dokumente.fehler.beim.laden");
				}
			}
			label.setText(value.toString());
			if (sel) {
				label.setBackground(Color.blue);
				label.setForeground(Color.white);
			} else {
				label.setBackground(Color.white);
				label.setForeground(null);
			}
			return label;
		}
	}

	@Override
	public void filesDropped(DragAndDropTarget source, List<File> files) {
		if (source == dropArea) {
			if (files == null || files.size() == 0)
				return;
			try {
				file = files.get(0);
				if(!file.exists()) {
					DialogFactory.showModalDialog(
							LPMain.getTextRespectUISPr("lp.error"),
							LPMain.getMessageTextRespectUISPr("lp.dokumente.typnichtunterstuetzt", Helper.getMime(file.getName())));
					return;
				}
				setDefaultsForNew();
				wtfName.setText(" ");
				components2Dto();
				new DialogJCRUploadSettings(LPMain.getInstance().getDesktop(), jcrDocDto);
				JCRFileUploader.uploadFiles(files, jcrDocDto);
				refresh();
			} catch (Throwable e) {
				ClassLoader classloader = org.apache.poi.poifs.filesystem.POIFSFileSystem.class
						.getClassLoader();
				URL res = classloader
						.getResource("org/apache/poi/poifs/filesystem/POIFSFileSystem.class");
				String path = res.getPath();
				System.out.println("Core POI came from " + path);
				e.printStackTrace();
			}

		}
	}
}
