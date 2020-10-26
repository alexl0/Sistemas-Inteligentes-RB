/*
 * Copyright (c) CISIAD, UNED, Spain,  2019. Licensed under the GPLv3 licence
 * Unless required by applicable law or agreed to in writing,
 * this code is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OF ANY KIND.
 */

package org.openmarkov.gui.dialog.network;

import org.openmarkov.core.action.ChangeNetworkTypeEdit;
import org.openmarkov.core.action.NetworkCommentEdit;
import org.openmarkov.core.exception.ConstraintViolationException;
import org.openmarkov.core.exception.DoEditException;
import org.openmarkov.core.exception.NonProjectablePotentialException;
import org.openmarkov.core.exception.WrongCriterionException;
import org.openmarkov.core.model.network.ProbNet;
import org.openmarkov.core.model.network.type.NetworkType;
import org.openmarkov.core.model.network.type.plugin.NetworkTypeManager;
import org.openmarkov.core.oopn.OOPNet;
import org.openmarkov.gui.dialog.CommentListener;
import org.openmarkov.gui.dialog.common.CommentHTMLScrollPane;
import org.openmarkov.gui.localize.StringDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Panel to set the definition of a network.
 *
 * @author jlgozalo
 * @version 1.1 ibermejo
 */
public class NetworkDefinitionPanel extends JPanel implements CommentListener {
	/**
	 * internal serial id
	 */
	private static final long serialVersionUID = 1047978130482205148L;
	/**
	 * String database
	 */
	protected StringDatabase stringDatabase = StringDatabase.getUniqueInstance();
	/**
	 * The Network Type Label
	 */
	private JLabel jLabelNetworkTypes = null;
	/**
	 * The Network Types Combo Box Drop Down List
	 */
	private JComboBox<String> jComboBoxNetworkTypes = null;
	/**
	 * The Network Definition Comment Label
	 */
	private JTextArea jTextAreaLabelNetworkDefinitionComment;
	/**
	 * The Network Comment Scroll Panel box
	 */
	private CommentHTMLScrollPane commentHTMLScrollPaneNetworkDefinition = null;
	/**
	 * Checkbox to define Object Orientedness of Network
	 */
	private JCheckBox jcheckBoxIsObjectOriented = null;
	/**
	 * Checkbox to define Object Orientedness of Network
	 */
	private JCheckBox jcheckBoxShowCommentOnOpening = null;
	/**
	 * Specifies if the network whose additionalProperties are edited is new.
	 */
	private boolean newNetwork = false;
	private ProbNet probNet;
	private NetworkPropertiesDialog parent;
	private NetworkTypeManager networkTypeManager;

	/**
	 * Constructor.
	 *
	 * @param parent this panel's parent dialog
	 */
	public NetworkDefinitionPanel(NetworkPropertiesDialog parent) {
		this.parent = parent;
		this.newNetwork = true;
		initialize();
	}

	/**
	 * Constructor.
	 *
	 * @param probNet manage the network access
	 */
	public NetworkDefinitionPanel(NetworkPropertiesDialog parent, ProbNet probNet) {
		this.parent = parent;
		this.newNetwork = probNet == null;
		this.probNet = probNet;
		initialize();
		if (probNet != null) {
			setFieldsFromProperties(probNet);
		}
	}

	/**
	 * initialises the panel
	 */
	private void initialize() {
		setName("NetworkDefinitionPanel");
		networkTypeManager = new NetworkTypeManager();
		final GroupLayout groupLayout = new GroupLayout((JComponent) this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addContainerGap().addGroup(
						groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
								groupLayout.createSequentialGroup()
										.addComponent(getJTextAreaLabelNetworkDefinitionComment(),
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(getCommentHTMLScrollPaneNetworkDefinition(),
												GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE).addContainerGap())
								.addGroup(groupLayout.createSequentialGroup().addGroup(
										groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
												.addComponent(getJLabelNetworkTypes(), GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
												groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
														groupLayout.createSequentialGroup()
																.addComponent(getJComboBoxNetworkTypes(),
																		GroupLayout.PREFERRED_SIZE, 182,
																		GroupLayout.PREFERRED_SIZE).addContainerGap())))
								.addGroup(groupLayout.createParallelGroup()
										.addComponent(getCheckBoxIsObjectOriented(), GroupLayout.PREFERRED_SIZE, 180,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(getCheckBoxShowCommentOnOpening(), GroupLayout.PREFERRED_SIZE,
												280, GroupLayout.PREFERRED_SIZE)))));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
				groupLayout.createSequentialGroup().addContainerGap().addGroup(
						groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(getJLabelNetworkTypes(), GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
								.addComponent(getJComboBoxNetworkTypes(), GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
						groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(getJTextAreaLabelNetworkDefinitionComment())
								.addComponent(getCommentHTMLScrollPaneNetworkDefinition(), GroupLayout.DEFAULT_SIZE,
										117, Short.MAX_VALUE)).addGroup(
						groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(getCheckBoxShowCommentOnOpening()))
						.addComponent(getCheckBoxIsObjectOriented()).addComponent(getCheckBoxShowCommentOnOpening())
						.addContainerGap(189, Short.MAX_VALUE)));
		setLayout(groupLayout);
	}

	/**
	 * initializes the getJLabelNetworkTypes
	 *
	 * @return jLabelNetworkTypes the label for the NetworkTypes field
	 */
	private JLabel getJLabelNetworkTypes() {
		if (jLabelNetworkTypes == null) {
			jLabelNetworkTypes = new JLabel();
			jLabelNetworkTypes.setText("a Label :");
			jLabelNetworkTypes.setText(stringDatabase.getString("NetworkDefinitionPanel.NetworkTypes.Text"));
			jLabelNetworkTypes.setMinimumSize(new Dimension(25, 0));
			jLabelNetworkTypes.setName("jLabelNetworkTypes");
			jLabelNetworkTypes.setDisplayedMnemonic(
					stringDatabase.getString("NetworkDefinitionPanel.NetworkTypes.Mnemonic").charAt(0));
			jLabelNetworkTypes.setLabelFor(getJComboBoxNetworkTypes());
		}
		return jLabelNetworkTypes;
	}

	/**
	 * initialises the jComboBoxNetworkTypes
	 *
	 * @return jComboBoxNetworkTypes the comboBox of the Network Types field
	 */
	private JComboBox<String> getJComboBoxNetworkTypes() {
		if (jComboBoxNetworkTypes == null) {
			Set<String> networkTypeNames = networkTypeManager.getNetworkTypeNames();
			List<String> networkTypes = new ArrayList<>(networkTypeNames.size());
			for (String networkType : networkTypeNames) {
				networkTypes.add(stringDatabase.getString("NetworkDefinitionPanel.NetworkTypes.Items." + networkType));
			}
			Collections.sort(networkTypes);
			String[] networkTypeArray = new String[networkTypes.size()];
			networkTypes.toArray(networkTypeArray);
			jComboBoxNetworkTypes = new JComboBox<String>(networkTypeArray);
			jComboBoxNetworkTypes.setName("jComboBoxNetworkTypes");
			jComboBoxNetworkTypes.setEditable(false);
			//
			if (newNetwork) {
				// Set Bayesian Network as default
				jComboBoxNetworkTypes.setSelectedItem(
						stringDatabase.getString("NetworkDefinitionPanel.NetworkTypes.Items.BayesianNetwork"));
			} else {
				jComboBoxNetworkTypes.setSelectedItem(stringDatabase.getString(
						"NetworkDefinitionPanel.NetworkTypes.Items." + networkTypeManager
								.getName(probNet.getNetworkType())));
				jComboBoxNetworkTypes.addActionListener(new ActionListener() {
					@Override public void actionPerformed(ActionEvent arg0) {
						networkTypeChanged();
					}
				});
			}
		}
		return jComboBoxNetworkTypes;
	}

	/**
	 * initialises the getJTextAreaLabelNetworkDefinitionComment
	 *
	 * @return jTextAreaLabelNetworkDefinitionComment the extended label for the
	 * comment field of Network Definition
	 */
	protected JTextArea getJTextAreaLabelNetworkDefinitionComment() {
		if (jTextAreaLabelNetworkDefinitionComment == null) {
			jTextAreaLabelNetworkDefinitionComment = new JTextArea();
			jTextAreaLabelNetworkDefinitionComment.setLineWrap(true);
			jTextAreaLabelNetworkDefinitionComment.setOpaque(false);
			jTextAreaLabelNetworkDefinitionComment.setName("jTextAreaLabelNetworkDefinitionComment");
			jTextAreaLabelNetworkDefinitionComment.setFocusable(false);
			jTextAreaLabelNetworkDefinitionComment.setEditable(false);
			jTextAreaLabelNetworkDefinitionComment.setFont(getJLabelNetworkTypes().getFont());
			jTextAreaLabelNetworkDefinitionComment.setText("an Extended Label");
			jTextAreaLabelNetworkDefinitionComment
					.setText(stringDatabase.getString("NetworkDefinitionPanel.NetworkDefinitionComment.Text"));
		}
		return jTextAreaLabelNetworkDefinitionComment;
	}

	/**
	 * initialises the getCommentHTMLScrollPaneForNetworkDefinition
	 *
	 * @return commentHTMLScrollPaneNetworkDefinition the comment for the Node
	 * definition
	 */
	private CommentHTMLScrollPane getCommentHTMLScrollPaneNetworkDefinition() {
		if (commentHTMLScrollPaneNetworkDefinition == null) {
			commentHTMLScrollPaneNetworkDefinition = new CommentHTMLScrollPane();
			commentHTMLScrollPaneNetworkDefinition.setName("commentHTMLScrollPaneNetworkDefinition");
			if (!newNetwork) {
				commentHTMLScrollPaneNetworkDefinition.addCommentListener(this);
			}
		}
		return commentHTMLScrollPaneNetworkDefinition;
	}

	private JCheckBox getCheckBoxIsObjectOriented() {
		if (jcheckBoxIsObjectOriented == null) {
			jcheckBoxIsObjectOriented = new JCheckBox(
					stringDatabase.getString("NetworkDefinitionPanel.IsObjectOriented.Text"), false);
			jcheckBoxIsObjectOriented.setSelected(probNet != null && probNet instanceof OOPNet);
			jcheckBoxIsObjectOriented.setEnabled(newNetwork);
		}
		return jcheckBoxIsObjectOriented;
	}

	private JCheckBox getCheckBoxShowCommentOnOpening() {
		if (jcheckBoxShowCommentOnOpening == null) {
			jcheckBoxShowCommentOnOpening = new JCheckBox(
					stringDatabase.getString("NetworkDefinitionPanel.ShowComment.Text"), false);
			jcheckBoxShowCommentOnOpening.setEnabled(true);
			jcheckBoxShowCommentOnOpening.setSelected(probNet != null && probNet.getShowCommentWhenOpening());
		}
		return jcheckBoxShowCommentOnOpening;
	}

	/**
	 * This method fills the content of the fields from a NetworkProperties
	 * object.
	 *
	 * @param probNet network from where load the information.
	 */
	private void setFieldsFromProperties(ProbNet probNet) {
		getJComboBoxNetworkTypes().setSelectedItem(stringDatabase.getString(
				"NetworkDefinitionPanel.NetworkTypes.Items." + networkTypeManager.getName(probNet.getNetworkType())));
		// set the title for comment
		MessageFormat messageForm = new MessageFormat(
				stringDatabase.getString("NetworkDefinitionPanel." + "CommentHTMLScrollPaneNetworkDefinition.Text"));
		// String shortNetworkName = (String)network.properties.
		// get(netPropertyNames.NAME.toString());
		String shortNetworkName = probNet.getName();
		int lastIndexOfSlashPath = shortNetworkName.lastIndexOf("\\");
		shortNetworkName = shortNetworkName.substring(lastIndexOfSlashPath + 1);
		Object[] labelArgs = new Object[] { shortNetworkName };
		getCommentHTMLScrollPaneNetworkDefinition().setTitle(messageForm.format(labelArgs));
		// String comment = (String)network.properties.get(
		// netPropertyNames.COMMENT.toString());
		getCommentHTMLScrollPaneNetworkDefinition().setCommentHTMLTextPaneText(probNet.getComment());
	}

	/**
	 * This method checks the name field.
	 *
	 * @return true, if the name field isn't empty; otherwise, false.
	 */
	protected boolean checkName() {
		// String name = getJTextFieldNetworkName().getText();
		return true;
	}

	public void commentHasChanged() {
		// check if the comment is empty
		String comment = getCommentHTMLScrollPaneNetworkDefinition().isEmpty() ?
				"" :
				getCommentHTMLScrollPaneNetworkDefinition().getCommentText();
		NetworkCommentEdit networkCommentEdit = new NetworkCommentEdit(probNet, comment, getShowComment());
		try {
			probNet.doEdit(networkCommentEdit);
		} catch (ConstraintViolationException | DoEditException | NonProjectablePotentialException | WrongCriterionException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, StringDatabase.getUniqueInstance().getString(e.getMessage()),
					StringDatabase.getUniqueInstance().getString(e.getMessage()), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void networkTypeChanged() {
		String itemSelected = (String) jComboBoxNetworkTypes.getSelectedItem();
		if (!(itemSelected == null)) {
			org.openmarkov.core.model.network.type.NetworkType selectedNetworkType = null;
			for (String networkTypeName : networkTypeManager.getNetworkTypeNames()) {
				if (itemSelected.equals(stringDatabase
						.getString("NetworkDefinitionPanel.NetworkTypes.Items." + networkTypeName))) {
					selectedNetworkType = networkTypeManager.getNetworkType(networkTypeName);
				}
			}
			if (selectedNetworkType != null) {
                /*
                28/10/2014
                Fixing issue 169
                https://bitbucket.org/cisiad/org.openmarkov.issues/issue/169/opening-the-network-properties-dialog
                The ChangeNetworkTypeEdit should only be invoked if the network type has actually changed
                 */
				if (probNet.getNetworkType().toString().compareTo(selectedNetworkType.toString()) != 0) {
					ChangeNetworkTypeEdit changeNetworkType = new ChangeNetworkTypeEdit(probNet, selectedNetworkType);
					try {
						probNet.doEdit(changeNetworkType);
						parent.getNetworkAdvancedPanel().update(probNet);
					} catch (ConstraintViolationException | NonProjectablePotentialException | WrongCriterionException e) {
						e.printStackTrace();
						JOptionPane
								.showMessageDialog(this, StringDatabase.getUniqueInstance().getString(e.getMessage()),
										StringDatabase.getUniqueInstance().getString(e.getMessage()),
										JOptionPane.ERROR_MESSAGE);
					} catch (DoEditException e) {
						// TODO maintain comboBox with the current probNet
						e.printStackTrace();
						// if (!newNetwork){
						JOptionPane.showMessageDialog(this, e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
						// It cannot be done the change selected so combobox
						// selection must be same
						jComboBoxNetworkTypes.setSelectedItem(stringDatabase.getString(
								"NetworkDefinitionPanel.NetworkTypes.Items." + probNet.getNetworkType().toString()
										.toString()));
						// }
					}
				}
			}
		}
	}

	public NetworkType getNetworkType() {
		org.openmarkov.core.model.network.type.NetworkType selectedNetworkType = null;
		for (String networkTypeName : networkTypeManager.getNetworkTypeNames()) {
			if (jComboBoxNetworkTypes.getSelectedItem()
					.equals(stringDatabase.getString("NetworkDefinitionPanel.NetworkTypes.Items." + networkTypeName))) {
				selectedNetworkType = networkTypeManager.getNetworkType(networkTypeName);
			}
		}
		return selectedNetworkType;
	}

	public String getNetworkComment() {
		return getCommentHTMLScrollPaneNetworkDefinition().getCommentText();
	}

	public boolean isObjectOriented() {
		return jcheckBoxIsObjectOriented.isSelected();
	}

	public boolean getShowComment() {
		return jcheckBoxShowCommentOnOpening.isSelected();
	}
}