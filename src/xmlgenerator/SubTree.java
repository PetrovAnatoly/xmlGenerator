/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgenerator;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Anatoly
 */
public class SubTree {
    //constructors
    private static int counter = 0;
    private int minD, maxD, minB, maxB;
    public SubTree(int minDepth, int maxDepth, int minBranch, int maxBranch){
        minD = minDepth; maxD = maxDepth; minB = minBranch; maxB = maxBranch;
        boolean hasChilds = getRandInt(minDepth, maxDepth) != 0;
        if (hasChilds){
            int childCount = getRandInt(minBranch, maxBranch);
            for (int i=0; i < childCount; i++){
                SubTree child = new SubTree(minDepth>0 ? minDepth-1:0, maxDepth-1, minBranch, maxBranch);
                childNodes.add(child);
            }
        }
        tag = "tag" + String.valueOf(counter);
        counter++;
    }
    public SubTree() {
        tag = "root";
    }
    public SubTree(String rootTag){
        tag = rootTag;
    }
    
    public void setIncidentalAttributes(int minAttr, int maxAttr/*, String attrNameRegularExpr, String attrValueRegularExpr*/){
        int attrCount = getRandInt(minAttr, maxAttr);
        for (int i=0; i < attrCount; i++){
            //String attribute = getStringByRegularExpression(attrNameRegularExpr);
            //String value = getStringByRegularExpression(attrValueRegularExpr);
            attributes.put("attr"+String.valueOf(i), "val"+String.valueOf(i));
        }
        for (SubTree childNode : childNodes) {
            childNode.setIncidentalAttributes(minAttr, maxAttr/*, String attrNameRegularExpr, String attrValueRegularExpr*/);
        }
    }
    public void setImperativeAttributes(HashMap<Integer,HashMap<String, String>> attributesInLevel, HashMap<String, String> attributesInAllLevel){
        setAttributesAtAllLevels(attributesInAllLevel);
        setAttributesAtLevels(attributesInLevel);
    }
    public void setAttributesAtAllLevels(HashMap<String, String> attributesInAllLevel){
        attributes.putAll(attributesInAllLevel);
        for (SubTree child: childNodes)
            child.setAttributesAtAllLevels(attributesInAllLevel);
    }
    private static int levelCounter = 0;
    public void setAttributesAtLevels(HashMap<Integer,HashMap<String, String>> attributesInLevel){
        HashMap<String, String> attributesInThisLevel = attributesInLevel.get(levelCounter);
        if (attributesInThisLevel!= null)
            attributes.putAll(attributesInThisLevel);
        levelCounter++;
        for (SubTree child: childNodes)
            child.setAttributesAtLevels(attributesInLevel);
        levelCounter--;
    }
    public void setImperativeTags(HashMap<Integer,HashMap<String, String[]>> tagsInLevel, HashMap<String, String[]> tagsInAllLevel){
        setTagsAtLevels(tagsInLevel);
        setTagsAtAllLevels(tagsInAllLevel);
    }
    public void setTagsAtAllLevels(HashMap<String, String[]> tagsInAllLevel){
        if (childNodes.isEmpty()) return;
        ArrayList<SubTree> newTagsAtThisLevel = new ArrayList<>();
        for (String thisTag: tagsInAllLevel.keySet()){
            int numOfThisTag = XMLGenerator.getRandomInt(tagsInAllLevel.get(thisTag));
            newTagsAtThisLevel.addAll(makeNodesWithThisString(thisTag, numOfThisTag));
        }
        if (maxD>0)
            for (SubTree newTag: newTagsAtThisLevel){
                newTag.fill(minD>0 ? minD-1:0, maxD-1, minB, maxB);
                newTag.setTagsAtAllLevels(tagsInAllLevel);
            }
        for (SubTree child: childNodes)
            child.setTagsAtAllLevels(tagsInAllLevel);
        childNodes.addAll(newTagsAtThisLevel);
    }
    public void setTagsAtLevels(HashMap<Integer,HashMap<String, String[]>> tagsInLevel){
        levelCounter++;
        HashMap<String, String[]> tagsInThisLevel = tagsInLevel.get(levelCounter);
        ArrayList<SubTree> newTagsAtThisLevel = new ArrayList<>();
        if (!(tagsInThisLevel==null || tagsInThisLevel.isEmpty())){
            for (String thisTag: tagsInThisLevel.keySet()){
                int numOfThisTag = XMLGenerator.getRandomInt(tagsInThisLevel.get(thisTag));
                newTagsAtThisLevel.addAll(makeNodesWithThisString(thisTag, numOfThisTag));
            }
            for (SubTree newTag: newTagsAtThisLevel){
                if (maxD!=0)
                    newTag.fill(minD>0 ? minD-1:0, maxD-1, minB, maxB);
                newTag.setTagsAtLevels(tagsInLevel);
            }
        }
        for (SubTree child: childNodes)
            child.setTagsAtLevels(tagsInLevel);
        childNodes.addAll(newTagsAtThisLevel);
        levelCounter--;
    }
    private static ArrayList<SubTree> makeNodesWithThisString(String str, int num){
        ArrayList<SubTree> rtrn = new ArrayList<>();
        for (int i = 0; i < num; i++)
            rtrn.add(new SubTree(str));
        return rtrn;
    }
    //fields
    private String tag = new String();
    private String textContent = new String();
    private ArrayList<SubTree> childNodes = new ArrayList<>();
    private HashMap<String, String> attributes = new HashMap<>();
    
    //methods
    public void addChild(SubTree chld) {
        childNodes.add(chld);
    }
    public void removeChild(SubTree chld) { childNodes.remove(chld);}
    public void fill(int minDepth, int maxDepth, int minBranch, int maxBranch){
        minD = minDepth; maxD = maxDepth; minB = minBranch; maxB = maxBranch;
        boolean hasChilds = getRandInt(minDepth, maxDepth) != 0;
        if (hasChilds){
            int childCount = getRandInt(minBranch, maxBranch);
            for (int i=0; i < childCount; i++){
                SubTree child = new SubTree("tag" + String.valueOf(counter));
                counter++;
                child.fill(minDepth>0 ? minDepth-1:0, maxDepth-1, minBranch, maxBranch);
                childNodes.add(child);
            }
        }
    }
    private int getRandInt(int from, int to){
        if (to == 0)
            return 0;
        return from + (new Random()).nextInt(to-from+1);
    }
    private String getStringByRegularExpression(String regularExpression){
        //will be soon
        return regularExpression;
    }
    private static int shift = 0;
    private void showAsTree()
    {
        for (int i = 0; i < shift; i++) System.out.print("\t");
        System.out.print(tag);
        System.out.println(attributes);
        shift++;
        for (SubTree ch: childNodes) 
            ch.showAsTree();
        shift--;
    }
    public static void main(String[] argv){
        SubTree test = new SubTree(1,4,1,4);
        test.setIncidentalAttributes(1, 3);
        test.showAsTree();
    }
    public Document toDocument() throws ParserConfigurationException{
        Document document = XMLGenerator.getDocument("root");
        Element root = document.getDocumentElement();
        insertIntoElement(document, root);
        return document;
    }
    private void insertIntoElement(Document document, Element element){
        Element newElem = document.createElement(tag);
        for (String key: attributes.keySet()){
            newElem.setAttribute(key, attributes.get(key));
        }
        newElem.setTextContent(textContent);
        element.appendChild(newElem);
        for (SubTree childNode : childNodes) {
            childNode.insertIntoElement(document, newElem);
        }
    }
    public ArrayList<SubTree> getChildNodes() { return childNodes;}
    public String getTag() { return tag;}
    public String getTextContent(){ return textContent;}

    public String getAttributes() {
        return attributes.toString();
    }
}
