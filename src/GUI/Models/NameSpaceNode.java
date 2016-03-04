/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import javax.swing.tree.DefaultMutableTreeNode;
import xmlgenerator.NameSpace;

/**
 *
 * @author Anatoly
 */
public class NameSpaceNode extends DefaultMutableTreeNode{
    private NameSpace associatedNameSpace;

    public NameSpaceNode(String name) {
        super(name);
    }
    public NameSpaceNode(NameSpace ns){
        super(ns.getSetName());
        associatedNameSpace = ns;
    }
    public NameSpaceNode() {
        super();
    }
    public void associateWithNameSpace(NameSpace node) { associatedNameSpace = node;}
    public NameSpace getAssociatedNameSpace() { return associatedNameSpace;} 
}
