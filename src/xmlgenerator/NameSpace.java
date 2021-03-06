/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgenerator;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author Anatoly
 */
public class NameSpace {
    public NameSpace() {}
    public NameSpace(String s){ name = s;}
    public NameSpace(String s, TreeSet<String> nameSet){ name = s; names = nameSet;}
    
    private String name = new String();
    private TreeSet<String> names = new TreeSet<>();
    private ArrayList<NameSpace> childNodes = new ArrayList<>();
    
    public String getSetName(){ return name;}
    public void setSetName(String s){ name = s;}
    public TreeSet<String> getNames() { 
        TreeSet<String> rtrn = (TreeSet<String>) names.clone();
        for (NameSpace child: childNodes)
            rtrn.addAll(child.getNames());
        return rtrn;
    }
    public TreeSet<String> getNameSet() { 
        return names;
    }
    public TreeSet<String> getNamesOfNS(String s){
        if (name.equals(s))
            return getNames();
        TreeSet<String> rtrn = new TreeSet<>();
        for (NameSpace ns: childNodes){
            rtrn.addAll(ns.getNamesOfNS(s));
        }
        return rtrn;
    }
    public boolean contains(String s){
        if (names.contains(s))
            return true;
        for (NameSpace ch:childNodes)
            if (ch.contains(s))
                return true;
        return false;
    }
    public boolean containsChildNS(String nsName){
        boolean rtrn = false;
        for (NameSpace child: childNodes)
            if (child.name.equals(nsName))
                return true;
            else if (child.containsChildNS(nsName))
                return true;
        return rtrn;
    }
    public NameSpace getChildByName(String nsName){
        NameSpace rtrn = null;
        for (NameSpace child: childNodes)
            if (child.name.equals(nsName))
                return child;
            else {
                rtrn = child.getChildByName(nsName);
                if (rtrn!=null)
                    return rtrn;
            }
        return rtrn;
    }
    public TreeSet<String> getPersonalNames(){ return (TreeSet<String>) names.clone();}
    public void addName(String s) { names.add(s);}
    public void addChildNameSpace(NameSpace child) { childNodes.add(child);}
    public ArrayList<NameSpace> getChildNameSpaces(){ return childNodes;}

    public NameSpace getCopy() {
        NameSpace rtrn = new NameSpace(name);
        rtrn.childNodes = (ArrayList<NameSpace>) childNodes.clone();
        rtrn.names = (TreeSet<String>) names.clone();
        return rtrn;
    }
}
