package com.parsing;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Parse {
			
	public ArrayList<String> currencyData (String choice){
		
		ArrayList <String> cData = new ArrayList<>();
		cData.add("Wybierz walutê...");
		cData.add("z³oty polski");					
	try {		
		URL url = new URL("http://www.nbp.pl/kursy/xml/LastA.xml");
					
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(url.openStream());
			
		if (choice == "date"){
		NodeList datePublication = doc.getElementsByTagName("data_publikacji");
		Element dateActual = (Element) datePublication.item(0);
		NodeList dateText = dateActual.getChildNodes();	
		cData.add(dateText.item(0).getNodeValue().trim().toString());
		}	
		NodeList positionAll = doc.getElementsByTagName("pozycja");
			
		for (int i=0; i<positionAll.getLength(); i++){
				
			Element position = (Element) positionAll.item(i);	
				
			if (choice == "name"){
			NodeList currencyName = position.getElementsByTagName("nazwa_waluty");
			Element currency = (Element) currencyName.item(0);	
			NodeList currencyNameText = currency.getChildNodes();
			cData.add(currencyNameText.item(0).getNodeValue().trim().toString());
			}
			if (choice == "converter"){
			NodeList calculateList = position.getElementsByTagName("przelicznik");
           		 Element calculateElement = (Element)calculateList.item(0);
           		 NodeList textCalculateList = calculateElement.getChildNodes();
           		 cData.add(textCalculateList.item(0).getNodeValue().trim());
			} 
			if (choice == "course"){
           		 NodeList courseList = position.getElementsByTagName("kurs_sredni");
            		Element courseElement = (Element)courseList.item(0);
           		 NodeList textKursList = courseElement.getChildNodes();
           		 cData.add(textKursList.item(0).getNodeValue().trim());
			}         
		}							
	}catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	return cData;
	}	
}
