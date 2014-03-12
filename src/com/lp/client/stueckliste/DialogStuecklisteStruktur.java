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
package com.lp.client.stueckliste;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.lp.client.frame.ExceptionLP;
import com.lp.client.frame.HelperClient;
import com.lp.client.frame.component.WrapperGotoButton;
import com.lp.client.frame.component.WrapperJTree;
import com.lp.client.frame.component.WrapperLabel;
import com.lp.client.frame.delegate.DelegateFactory;
import com.lp.client.pc.LPMain;
import com.lp.server.stueckliste.service.StuecklisteDto;

@SuppressWarnings("static-access")
public class DialogStuecklisteStruktur extends JDialog implements
		ActionListener, TreeExpansionListener, TreeSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BorderLayout borderLayout = new BorderLayout();
	private JTree tree = null;
	InternalFrameStueckliste internalFrame = null;

	WrapperGotoButton wbuStkl = new WrapperGotoButton(
			WrapperGotoButton.GOTO_STUECKLISTE_DETAIL);
	WrapperGotoButton wbuArtikel = new WrapperGotoButton(
			WrapperGotoButton.GOTO_ARTIKEL_AUSWAHL);

	public DialogStuecklisteStruktur(InternalFrameStueckliste internalFrame)
			throws Throwable {
		super(LPMain.getInstance().getDesktop(), LPMain.getInstance()
				.getTextRespectUISPr("stkl.baumansicht"), false);
		this.internalFrame = internalFrame;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		jbInit();
		HelperClient.memberVariablenEinerKlasseBenennen(this);

		this.setSize(800, 500);

	}

	public void actionPerformed(ActionEvent e) {

	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.dispose();
		}
	}

	private void jbInit() throws Throwable {

		this.getContentPane().setLayout(borderLayout);

		StuecklisteTreeNode root = new StuecklisteTreeNode("ROOT");

		TreeMap<String, Integer> tm = DelegateFactory.getInstance()
				.getStuecklisteDelegate().holeAlleWurzelstuecklisten();

		Iterator it = tm.keySet().iterator();
		while (it.hasNext()) {

			String s = (String) it.next();
			Integer stkl_i_id = tm.get(s);

			StuecklisteTreeNode stkl = new StuecklisteTreeNode(s, stkl_i_id,
					null);
			stkl.setUserObject(s);
			StuecklisteTreeNode next = new StuecklisteTreeNode("POS");
			stkl.add(next);

			root.add(stkl);
		}

		tree = new JTree(root);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		tree.addTreeExpansionListener(this);

		tree.addTreeSelectionListener(this);

		this.getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);

		// Goto Stkl ->

		JPanel p = new JPanel(new GridBagLayout());

		wbuStkl.setPreferredSize(new Dimension(25, 21));
		wbuStkl.setMinimumSize(new Dimension(25, 21));

		WrapperLabel wlaStkl = new WrapperLabel( LPMain.getInstance()
				.getTextRespectUISPr("stkl.baumansicht.gehezustkl"));
		p.add(wlaStkl, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 100, 0));
		p.add(wbuStkl.getWrapperButtonGoTo(), new GridBagConstraints(1, 1, 1,
				1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 10, 0));

		WrapperLabel wlaArtikel = new WrapperLabel(LPMain.getInstance()
				.getTextRespectUISPr("stkl.baumansicht.gehezuartikel"));
		p.add(wlaArtikel, new GridBagConstraints(2, 1, 1, 1, 1, 1,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,
						0, 0, 0), 100, 0));
		p.add(wbuArtikel.getWrapperButtonGoTo(), new GridBagConstraints(3, 1,
				1, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 0, 0, 0), 10, 0));

		this.getContentPane().add(p, BorderLayout.NORTH);

		wbuStkl.setOKey(null);
		wbuArtikel.setOKey(null);

	}

	@Override
	public void treeCollapsed(TreeExpansionEvent arg0) {
		// TODO Auto-generated method stub
		StuecklisteTreeNode node = (StuecklisteTreeNode) arg0.getPath()
				.getLastPathComponent();
		node.removeAllChildren();

		StuecklisteTreeNode next = new StuecklisteTreeNode("POS");
		node.add(next);

		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();

		model.nodeChanged(node);
		int z = 0;
	}

	@Override
	public void treeExpanded(TreeExpansionEvent arg0) {
		// TODO Auto-generated method stub

		StuecklisteTreeNode node = (StuecklisteTreeNode) arg0.getPath()
				.getLastPathComponent();

		node.removeAllChildren();

		DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
		try {
			Integer stuecklisteIId = node.getStueckliste_i_id();
			if (stuecklisteIId == null && node.getArtikel_i_id() != null) {
				StuecklisteDto stklDto = DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								node.getArtikel_i_id());
				if (stklDto != null) {
					stuecklisteIId = stklDto.getIId();
				}
			}

			if (stuecklisteIId != null) {

				TreeMap<String, Integer> tmPositionen = DelegateFactory
						.getInstance().getStuecklisteDelegate()
						.holeNaechsteEbene(stuecklisteIId);

				Iterator it = tmPositionen.keySet().iterator();

				while (it.hasNext()) {
					String s = (String) it.next();
					Integer artikelIId = tmPositionen.get(s);

					StuecklisteTreeNode next = new StuecklisteTreeNode(s, null,
							artikelIId);

					StuecklisteDto stklDto = DelegateFactory
							.getInstance()
							.getStuecklisteDelegate()
							.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
									artikelIId);
					if (stklDto != null) {
						next.add(new StuecklisteTreeNode("POS", stklDto
								.getIId(), null));
					}

					model.insertNodeInto(next, node, node.getChildCount());

				}

			}
		} catch (Throwable e) {
			internalFrame.handleException(e, true);
		}
		model.nodeChanged(node);
		model.reload(node);
		// Naechste Ebene Holen

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub

		StuecklisteTreeNode node = (StuecklisteTreeNode) e.getPath()
				.getLastPathComponent();

		Integer artikelIId = node.getArtikel_i_id();

		Integer stuecklisteIId = node.getStueckliste_i_id();
		try {
			if (stuecklisteIId == null && node.getArtikel_i_id() != null) {
				StuecklisteDto stklDto = DelegateFactory
						.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByMandantCNrArtikelIIdOhneExc(
								node.getArtikel_i_id());
				if (stklDto != null) {
					stuecklisteIId = stklDto.getIId();
				}
			}

			if (artikelIId == null && stuecklisteIId != null) {
				StuecklisteDto stklDto = DelegateFactory.getInstance()
						.getStuecklisteDelegate()
						.stuecklisteFindByPrimaryKey(stuecklisteIId);
				artikelIId = stklDto.getArtikelIId();

			}

		} catch (Throwable ex) {
			internalFrame.handleException(ex, true);
		}

		wbuStkl.setOKey(stuecklisteIId);
		wbuArtikel.setOKey(artikelIId);
	}
}
