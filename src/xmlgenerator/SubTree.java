/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlgenerator;

import GUI.MainFrame;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
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
    private int globalMaxDepth;
    public void setGlobalMaximunDepth(int i){ globalMaxDepth = i;}
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
    private static HashMap<String, ArrayList<String>> attrInAllLvls = new HashMap<>();
    private static HashMap<Integer, HashMap<String, ArrayList<String>>> attrInLvls = new HashMap<>();
    private static HashMap<String, HashMap<String, ArrayList<String>>> attrInTags = new HashMap<>();
    private static HashMap<ArrayList<String>, NameSpace> assoc = new HashMap<>();
    public void setImperativeAttributes(HashMap<Integer,HashMap<String, String>> attributesInLevel, 
            HashMap<String, String> attributesInAllLevel, 
            HashMap<String, HashMap<String, String>> attributesOfTags){
        attrInAllLvls = new HashMap<>();
        attrInLvls = new HashMap<>();
        attrInTags = new HashMap<>();
        assoc = new HashMap<>();
        for (String attr: attributesInAllLevel.keySet()){
            String s = attributesInAllLevel.get(attr);
            ArrayList<String> valuesOfThisAttr = getNamesToUseAndCreateNewNSIfNeeded(s);
            attrInAllLvls.put(attr, valuesOfThisAttr);
        }
        for (Integer level: attributesInLevel.keySet()){
            HashMap<String, ArrayList<String>> attrInThisLvl;
            if (attrInLvls.containsKey(level))
                attrInThisLvl = attrInLvls.get(level);
            else {
                attrInThisLvl = new HashMap<>();
                attrInLvls.put(level, attrInThisLvl);
            }
            for (String attr: attributesInLevel.get(level).keySet()){
                String s = attributesInLevel.get(level).get(attr);
                ArrayList<String> valuesOfThisAttr = getNamesToUseAndCreateNewNSIfNeeded(s);
                attrInThisLvl.put(attr, valuesOfThisAttr);
            }
        }
        for (String tag: attributesOfTags.keySet()){
            HashMap<String, ArrayList<String>> attrInThisTag;
            if (attrInTags.containsKey(tag))
                attrInThisTag = attrInTags.get(tag);
            else {
                attrInThisTag = new HashMap<>();
                attrInTags.put(tag, attrInThisTag);
            }
            for (String attr: attributesOfTags.get(tag).keySet()){
                String s = attributesOfTags.get(tag).get(attr);
                ArrayList<String> valuesOfThisAttr = getNamesToUseAndCreateNewNSIfNeeded(s);
                attrInThisTag.put(attr, valuesOfThisAttr);
            }
        }
        SetAttributesOfTags(attrInTags);
        setAttributesAtLevels(attrInLvls);
        setAttributesAtAllLevels(attrInAllLvls);
    }
    private static ArrayList<String> getNamesToUseAndCreateNewNSIfNeeded(String s){
        ArrayListForNames<String> rtrn = new ArrayListForNames<>();
        String nsName = "";
        String countStr = "";
        int count = -1;
        String storageNameSpaceName = "";
        if (s.startsWith("//")){
            s = String.copyValueOf(s.toCharArray(), 2, s.length()-2);
            nsName = copyUpToDoubleSlash(s);
            s = s.replaceFirst(nsName, "");
            if (s.startsWith("//")){
                s = String.copyValueOf(s.toCharArray(), 2, s.length()-2);
                countStr = copyUpToDoubleSlash(s);
                s = s.replaceFirst(countStr, "");
                if (s.startsWith("//")){
                    s = String.copyValueOf(s.toCharArray(), 2, s.length()-2);
                    storageNameSpaceName = copyUpToDoubleSlash(s);
                }
            }
            TreeSet<String> nmes = MainFrame.nsRoot.getNamesOfNS(nsName);
            try{count = Integer.valueOf(countStr.trim());}
            catch(NumberFormatException exc){}
            if (count>0 && count<nmes.size()){
                int len, index;
                for (int i = 0; i < count; i++){
                    len = nmes.toArray().length;
                    index = getRandInt(0, len-1);
                    s = (String) nmes.toArray()[index];
                    rtrn.add(s);
                    nmes.remove(s);
                }
            }
            else
                rtrn = new ArrayListForNames(nmes);
            //создаем новый NameSpace
            if (!storageNameSpaceName.isEmpty()){
                NameSpace newNS;
                if (MainFrame.nsRoot.containsChildNS(storageNameSpaceName))
                    newNS = MainFrame.nsRoot.getChildByName(storageNameSpaceName);
                else {
                    newNS = new NameSpace(storageNameSpaceName);
                    MainFrame.nsRoot.addChildNameSpace(newNS);
                }
                System.out.println(assoc.containsKey(rtrn));
                assoc.put(rtrn, newNS);
            }
        }
        else 
            rtrn.add(s);
        return rtrn;
    }
    private static String copyUpToDoubleSlash(String s){
        String rtrn = "";
        boolean firstSlashIsReached = false;
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == '/')
                if (firstSlashIsReached)
                    break;
                else
                    if (i == s.length()-1)
                        rtrn+=s.charAt(i);
                    else
                        firstSlashIsReached = true;
            else{
                rtrn+=s.charAt(i);
                firstSlashIsReached = false;
            }
        }
        return rtrn;
    }
    public void SetAttributesOfTags(HashMap<String, HashMap<String, ArrayList<String>>> attributesOfTags){
        HashMap<String, ArrayList<String>> attributesOfThisTag = attributesOfTags.get(tag);
        if (attributesOfThisTag!=null)
            for (String attribute: attributesOfThisTag.keySet())
                if (!attributes.containsKey(attribute)){
                    ArrayList<String> names = attributesOfThisTag.get(attribute);
                    int position = getRandInt(0, names.size()-1);
                    String s = names.get(position);
                    attributes.put(attribute, s);
                    if (assoc.containsKey(names))
                        assoc.get(names).addName(s);
                }
        for (SubTree child: childNodes)
            child.SetAttributesOfTags(attributesOfTags);
    }
    private static int levelCounter = 0;
    public void setAttributesAtLevels(HashMap<Integer,HashMap<String, ArrayList<String>>> attributesInLevel){
        HashMap<String, ArrayList<String>> attributesInThisLevel = attributesInLevel.get(levelCounter);
        if (attributesInThisLevel!= null)
            for (String attribute: attributesInThisLevel.keySet())
                if (!attributes.containsKey(attribute)){
                    ArrayList<String> names = attributesInThisLevel.get(attribute);
                    int position = getRandInt(0, names.size()-1);
                    String s = names.get(position);
                    attributes.put(attribute, s);
                    if (assoc.containsKey(names))
                        assoc.get(names).addName(s);
                }
        levelCounter++;
        for (SubTree child: childNodes)
            child.setAttributesAtLevels(attributesInLevel);
        levelCounter--;
    }
    public void setAttributesAtAllLevels(HashMap<String, ArrayList<String>> attributesInAllLevel){
        if (attributesInAllLevel!=null)
            for (String attribute: attributesInAllLevel.keySet())
                if (!attributes.containsKey(attribute)){
                    if (!attributes.containsKey(attribute)){
                        ArrayList<String> names = attributesInAllLevel.get(attribute);
                        int position = getRandInt(0, names.size()-1);
                        String s = names.get(position);
                        attributes.put(attribute, s);
                        if (assoc.containsKey(names))
                            assoc.get(names).addName(s);
                    }
                }
        for (SubTree child: childNodes)
            child.setAttributesAtAllLevels(attributesInAllLevel);
    }
    public void setImperativeTags(HashMap<Integer,HashMap<String, String[]>> tagsInLevel, 
            HashMap<String, String[]> tagsInAllLevel,
            HashMap<String, HashMap<String, String[]>> tagsofParentTags){
        int maxImperativeLevel = 0;
        for (int i : tagsInLevel.keySet()) { 
            if (i>maxImperativeLevel)
                maxImperativeLevel=i;
        }
        setTagsAtLevels(tagsInLevel, tagsInAllLevel, tagsofParentTags,maxImperativeLevel);
        //setTagsAtAllLevels(tagsInAllLevel);
    }
    public void setTagsAtLevels(HashMap<Integer,HashMap<String, String[]>> tagsInLevel,
            HashMap<String, String[]> tagsInAllLevel, 
            HashMap<String, HashMap<String, String[]>> tagsofParentTags,
            int maxImperativeLevel){
        levelCounter++;
        if (globalMaxDepth == 0){
            levelCounter--;
            return;
        }
        HashMap<String, String[]> tagsInThisLevel = tagsInLevel.get(levelCounter);
        HashMap<String, String[]> tagsInThisParentTag = tagsofParentTags.get(tag);
        ArrayList<SubTree> newTagsAtThisParentTag = new ArrayList<>();
        if (!(tagsInThisLevel==null || tagsInThisLevel.isEmpty())){
            for (String thisTag: tagsInThisLevel.keySet()){
                int numOfThisTag = XMLGenerator.getRandomInt(tagsInThisLevel.get(thisTag));
                newTagsAtThisParentTag.addAll(makeNodesWithThisString(thisTag, numOfThisTag));
            }
        }
        if (!(tagsInThisParentTag == null || tagsInThisParentTag.isEmpty())){
            for (String thisTag: tagsInThisParentTag.keySet()){
                int numOfThisTag = XMLGenerator.getRandomInt(tagsInThisParentTag.get(thisTag));
                newTagsAtThisParentTag.addAll(makeNodesWithThisString(thisTag, numOfThisTag));
            }
        }
        if (!(newTagsAtThisParentTag.isEmpty() && childNodes.isEmpty())){
            for (String thisTag: tagsInAllLevel.keySet()){
                int numOfThisTag = XMLGenerator.getRandomInt(tagsInAllLevel.get(thisTag));
                newTagsAtThisParentTag.addAll(makeNodesWithThisString(thisTag, numOfThisTag));
            }
        }
        else{
            if (maxImperativeLevel>=levelCounter)
                for (String thisTag: tagsInAllLevel.keySet()){
                    int numOfThisTag = XMLGenerator.getRandomInt(tagsInAllLevel.get(thisTag));
                    newTagsAtThisParentTag.addAll(makeNodesWithThisString(thisTag, numOfThisTag));
                }
        }
        for (SubTree newTag: newTagsAtThisParentTag){
            newTag.globalMaxDepth = globalMaxDepth-1;
            if (maxD!=0)
                newTag.fill(minD>0 ? minD-1:0, maxD-1, minB, maxB);
            newTag.setTagsAtLevels(tagsInLevel, tagsInAllLevel, tagsofParentTags, maxImperativeLevel);
        }
        for (SubTree child: childNodes)
            child.setTagsAtLevels(tagsInLevel, tagsInAllLevel, tagsofParentTags, maxImperativeLevel);
        childNodes.addAll(newTagsAtThisParentTag);
        levelCounter--;
    }
    
    //последовательность добавления: 
    // 1.для тегов (в порядке строк таблицы)
    // 2. для уровней (-//-)
    // 3. содержимое для всех вершин (-//-)
    public void setTextContent(HashMap<Integer, ArrayList<String>> textContentInLevels, 
            ArrayList<String> textContentInAllLevels, 
            HashMap<String, ArrayList<String>> textContentInTags) {
        ArrayList<String> contentOfThisNode = new ArrayList<>();
        ArrayList<String> contentOfThisNodeTag = textContentInTags.get(tag);
        ArrayList<String> contentOfThisNodeLevel = textContentInLevels.get(levelCounter);
        if (contentOfThisNodeTag != null)
            contentOfThisNode = copyWithoutRepeat(contentOfThisNode, contentOfThisNodeTag);
        if (contentOfThisNodeLevel != null)
            contentOfThisNode = copyWithoutRepeat(contentOfThisNode, contentOfThisNodeLevel);
        if (textContentInAllLevels != null)
            contentOfThisNode = copyWithoutRepeat(contentOfThisNode, textContentInAllLevels);
        for (String atomContent: contentOfThisNode){
            if (atomContent.startsWith("//")){
                atomContent = atomContent.replace("//", "");
                TreeSet<String> nmes = MainFrame.nsRoot.getNamesOfNS(atomContent);
                int len = nmes.toArray().length;
                int index = getRandInt(0, len-1);
                atomContent = (String) nmes.toArray()[index];
            }
            textContent+=atomContent;
        }
        levelCounter++;
        for (SubTree child: childNodes)
            child.setTextContent(textContentInLevels, textContentInAllLevels, textContentInTags);
        levelCounter--;
    }
    private static ArrayList<String> copyWithoutRepeat(ArrayList<String> to, ArrayList<String> from){
        for (String s: from)
            if (!to.contains(s))
                to.add(s);
        return to;
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
                if (globalMaxDepth == 0)
                    break;
                child.globalMaxDepth = globalMaxDepth - 1;
                counter++;
                child.fill(minDepth>0 ? minDepth-1:0, maxDepth-1, minBranch, maxBranch);
                childNodes.add(child);
            }
        }
    }
    // до to включительно
    private static int getRandInt(int from, int to){
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
    
    public SubTree getCopy() {
        SubTree rtrn = new SubTree(tag);
        rtrn.attributes = (HashMap<String, String>) attributes.clone();
        rtrn.textContent = this.textContent;
        for (SubTree child: childNodes)
            rtrn.addChild(child.getCopy());
        return rtrn;
    }

    
}
