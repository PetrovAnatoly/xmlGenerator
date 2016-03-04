/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Models;

import javax.swing.table.DefaultTableModel;
import xmlgenerator.NameSpace;

/**
 *
 * @author Anatoly
 */
public class NameSpaceTableModel extends DefaultTableModel{
    public NameSpaceTableModel(){ setColumnIdentifiers(headers);}
    public NameSpaceTableModel(NameSpace ns){
        setColumnIdentifiers(headers); 
        for (String name: ns.getNames()){
            Object[] rw = {name};
            this.addRow(rw);
        }
    }
    private static final Object[] headers = {"name"};
    private static final Object[] rowData = {""};
    public void addRow(){ this.addRow(rowData);}
    
}
