package phase1;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLtoJSON {
	static String inputFolderPath = "YahooAnswers";
	static String outputFolderPath = "output/phase1";
	static String xmlFolderName;
	
	public static void main(String []args){
		inputFolderPath = "/home/adeel/course/sem3/web mining/project";
		outputFolderPath = "output/phase1";
		xmlFolderName = inputFolderPath + File.separator + "xml";
		
		File xmlFolder = new File(xmlFolderName);
		
		File []answersXML = xmlFolder.listFiles();
		
		for(File f : answersXML){
			if(f.isFile()){
				JSONObject obj = parseXMLtoJSON(f);
				
				if(obj !=  null){
					String fname = f.getName();
					fname = fname.substring(0, fname.length() - 4);
					String outputPath = outputFolderPath + File.separator + fname;
					
					File op = new File(outputPath);
					
					if(!op.getParentFile().exists()){
						op.getParentFile().mkdirs();
					}
					
					FileWriter fr = null;
					try {
						fr = new FileWriter(op);
						fr.write(obj.toString());
					} catch (IOException e) {
						e.printStackTrace();
					} finally{
						if(fr != null){
							try {
								fr.close();
							} catch (IOException e) {
								e.printStackTrace();
							}			
						}
					}
				}
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private static JSONObject parseXMLtoJSON(File f){
		JSONObject object = null;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(f);
			
			doc.getDocumentElement().normalize();
			
			Node question  = doc.getDocumentElement().getElementsByTagName("Question").item(0);
			
			NamedNodeMap quesAttrs= question.getAttributes();
			String qid = quesAttrs.getNamedItem("id").getTextContent();
			String typ = quesAttrs.getNamedItem("type").getTextContent();
			
			String subject = "";
			String content = "";
			String date = "";
			String timestamp = "";
			String userId = "";
			String userNick = "";
			String chosenAnwserTimestamp = "";
			
			
			NodeList questionChildren= question.getChildNodes();
			
			for(int i = 0 ; i < questionChildren.getLength(); i++){
				Node n = questionChildren.item(i);
				
				XMLNode x = XMLNode.getNode(n.getNodeName());
				
				if(x == null)
					continue;
				
				switch(x){
					case SUBJECT: subject = n.getTextContent(); break;
					case CONTENT: content = n.getTextContent(); break;
					case DATE: date = n.getTextContent(); break;
					case TIMESTAMP: timestamp = n.getTextContent(); break;
					case USERID: userId = n.getTextContent(); break;
					case USERNICK: userNick = n.getTextContent(); break;
					case ANSWER_TIMESTAMP: chosenAnwserTimestamp = n.getTextContent(); break;
				}
			}
			
			object = new JSONObject();
			object.put("id", qid);
			object.put("type", typ);
			object.put("subject", subject);
			object.put("content", content);
			object.put("date", date);
			object.put("timestamp", timestamp);
			object.put("userid", userId);
			object.put("usernick", userNick);
			object.put("answer", chosenAnwserTimestamp);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return object;
	}
}
