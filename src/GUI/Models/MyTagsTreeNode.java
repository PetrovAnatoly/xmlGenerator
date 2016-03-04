/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import javax.swing.tree.DefaultMutableTreeNode;
import xmlgenerator.SubTree;

/**
 *
 * @author Anatoly
 */
public class MyTagsTreeNode extends DefaultMutableTreeNode{
    private SubTree associatedNode;

    public MyTagsTreeNode(String tag) {
        super(tag);
    }

    public MyTagsTreeNode() {
        super();
    }
    public void associateWithNode(SubTree node) { associatedNode = node;}
    public SubTree getAssociatedNode() { return associatedNode;}
}
