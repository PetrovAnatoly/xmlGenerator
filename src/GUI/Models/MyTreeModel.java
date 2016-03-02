/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Anatoly
 */
public class MyTreeModel extends DefaultTreeModel{
    public MyTreeModel(TreeNode root) {
        super(root);
    }
}
