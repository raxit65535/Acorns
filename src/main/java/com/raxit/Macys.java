package com.raxit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class Macys {

	public static void main(String a[]) throws ArrayIndexOutOfBoundsException, IOException {

		//declaration of WebClient object
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);

		//declaration of output file object
		File output = new File("Generalized-Categories.txt");
		
		//initiating file writer and buffer writer object to write in the output file.
		FileWriter fw = new FileWriter(output);
		BufferedWriter bw = new BufferedWriter(fw);
		
		//counter to keep track of number of records fetched.
		int counter = 1;
		
		try {
			
			String searchUrl = "https://www.macys.com/";
			HtmlPage page = client.getPage(searchUrl);

			//fetching the div tag of top nav sub categories
			List<HtmlElement> macystopnav = (List<HtmlElement>) page
					.getByXPath("//div[@class='small-4 columns flexbox']");
			
			//condition if the fetched HtmlElement's size is not zero
			if (macystopnav.size() == 0) {
				System.out.println("no data to scrape.... !");
			} else {
				//since the size of our fetched list is not zero, we will extract our tragated data from it.
				for (HtmlElement htmlItem : macystopnav) {
					
					HtmlElement li = ((HtmlElement) htmlItem.getFirstByXPath(".//ul['flexLabelLinksContainer']//li"));
					String content = li.getTextContent().toString();

					bw.write(counter + " -> " + content);
					bw.newLine();
					counter = counter + 1;
					System.out.println(content);
				}
				
				bw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
