package com.tdgame;

import java.io.File;  
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class contains the code to create an XML file and load it into the 2D array for creating a new map
 * 
 * @author Team 2
 * @version $revision
 *
 */
public class SaveXML {  
	
	private Screen screen;
	private String newFileName;
	DocumentBuilderFactory documentFactory = DocumentBuilderFactory
			.newInstance();
	DocumentBuilder documentBuilder;
	Document document;
	Element logRootElement; 
	Attr attribute;
	String logFileName;

	public SaveXML(Screen screen, String newFileName) {
		this.screen = screen;
		this.newFileName = newFileName;
		
	}
	public SaveXML(String log)
	{
		try
		{
			logFileName=log;
			File logFile;
			logFile=new File("../log/", log);
			if(!logFile.exists())
			{
				documentBuilder = documentFactory.newDocumentBuilder();
				document = documentBuilder.newDocument();
				logRootElement = document.createElement("Log");
				document.appendChild(logRootElement);
				
				
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);
				StreamResult streamResult = new StreamResult(new File(
						"../log/"+log));
	
				transformer.transform(domSource, streamResult);
			}
			else
			{
				documentBuilder = documentFactory.newDocumentBuilder();
				document = documentFactory.newDocumentBuilder().parse(new File("../log/"+log));
				logRootElement = document.getDocumentElement();
			}

		}
		catch(Exception e){}
	}
	
	/**
	 * To create XML file from map array 
	 */
	public void createXML() {
		try {
			int count = 0;
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();

			// define root elements
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("Map");
			Attr attribute = document.createAttribute("userMoney");
			attribute.setValue(screen.user.player.money+"");
			rootElement.setAttributeNode(attribute);
			
			document.appendChild(rootElement);
						
			for(int k=0; k < this.screen.map[0].length; k++) {
				// define row elements
				Element row = document.createElement("Row");
				rootElement.appendChild(row);
				
				for(int i=0; i < this.screen.map.length; i++) {					
					
					for(int j=k; j < this.screen.map[0].length;) {
						
						count++;
						// tiles
						Element tile = document.createElement("Tile");
						row.appendChild(tile);
						
						// add attributes to tile											
						attribute = document.createAttribute("x");
						attribute.setValue(j+"");
						tile.setAttributeNode(attribute);
						
						attribute = document.createAttribute("y");
						attribute.setValue(i+"");
						tile.setAttributeNode(attribute);
						
						attribute = document.createAttribute("value");
						
						if(MouseHandler.saveGame = true) {
							attribute.setValue(this.screen.map[i][j]+"");
						}
						else {
							attribute.setValue(0+"");
						}
						
						tile.setAttributeNode(attribute);
												
						attribute = document.createAttribute("position");
						if(MouseHandler.hashMap_of_pathIndex_with_position.containsValue(i+"_"+j)) {

							for (Entry<Integer, String> entry : MouseHandler.hashMap_of_pathIndex_with_position.entrySet()) {
					            if (entry.getValue().equals(i+"_"+j)) {
					                attribute.setValue(entry.getKey()+"");
					                break;
					            }
					        }
						}
						else {
							attribute.setValue(0+"");
						}
						
						tile.setAttributeNode(attribute);
						
						// logic for saving state
						if(Screen.towerMap != null && null != Screen.towerMap[i][j]) {
							
							Tower towerOnMapBtn=Screen.towerMap[i][j];	
							
							attribute = document.createAttribute("tower");
							attribute.setValue(towerOnMapBtn.getType());
							tile.setAttributeNode(attribute);
							
							attribute = document.createAttribute("ammunition");
							attribute.setValue(towerOnMapBtn.getAmmunition()+"");
							tile.setAttributeNode(attribute);
							
							attribute = document.createAttribute("range");
							attribute.setValue(towerOnMapBtn.getRange()+"");
							tile.setAttributeNode(attribute);
							
							attribute = document.createAttribute("cost");
							attribute.setValue(towerOnMapBtn.getCost()+"");
							tile.setAttributeNode(attribute);
							
							attribute = document.createAttribute("rateOfFire");
							attribute.setValue(towerOnMapBtn.getRateOfFire()+"");
							tile.setAttributeNode(attribute);
							
							attribute = document.createAttribute("refundRate");
							attribute.setValue(towerOnMapBtn.getRefundRate()+"");
							tile.setAttributeNode(attribute);
							
							attribute = document.createAttribute("costToAddAmmunition");
							attribute.setValue(towerOnMapBtn.getCostToAddAmmunition()+"");
							tile.setAttributeNode(attribute);
							
							attribute = document.createAttribute("towerLevel");
							attribute.setValue(towerOnMapBtn.getTowerLevel()+"");
							tile.setAttributeNode(attribute);
							
							attribute = document.createAttribute("towerStrategy");
							attribute.setValue(towerOnMapBtn.getTowerStrategy()+"");
							tile.setAttributeNode(attribute);
							
							
						}
//						attribute = document.createAttribute("tower");
						
						
						
						break;
					}
				}
			}			
			
			if(newFileName.contains(".xml")) {
				newFileName = newFileName.split("\\.")[0];
			}
			
			// creating and writing to xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(
					"../level/"+newFileName+".xml"));
			if(MouseHandler.saveEditedMap){
				Screen.saveLogXML.writeLog("Map", "Edit Map", "Map file "+newFileName+" edited and saved successfully");
			}
			else{
				Screen.saveLogXML.writeLog("Map", "Create Map", "Map file "+newFileName+" successfully created");
				Screen.saveLogXML.writeLog("Map", "Create Map", "Map "+newFileName+" saved successfully");
			}
				
			transformer.transform(domSource, streamResult);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

	/**
	 * To write log file
	 */
	public void writeLog(String logType,String elementType,String msg){
		
		 Date date= new Date();
		 //getTime() returns current time in milliseconds
		 long time = date.getTime();
         //Passed the milliseconds to constructor of Timestamp class 
		 Timestamp ts = new Timestamp(time);
		 String timeStamp=ts.toString();
	 
		try{
			
			Element entry = document.createElement("Entry");
			logRootElement.appendChild(entry);
			
			// add attributes to tile											
			attribute = document.createAttribute("TimeStamp");
			attribute.setValue(timeStamp);
			entry.setAttributeNode(attribute);
			
			attribute = document.createAttribute("LogType");
			attribute.setValue(logType);
			entry.setAttributeNode(attribute);
			
			attribute = document.createAttribute("ElementType");
			attribute.setValue(elementType);
			entry.setAttributeNode(attribute);
			
			attribute = document.createAttribute("Msg");
			attribute.setValue(msg);
			entry.setAttributeNode(attribute);
			
			DOMSource source = new DOMSource(document);

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        StreamResult result = new StreamResult("../log/"+logFileName);
	        transformer.transform(source, result);
			
			
		}
		catch(Exception e){
			
		}
	}
	
}