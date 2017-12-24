package com.raxit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SpecificCategory {

	public static void main(String a[]) throws ArrayIndexOutOfBoundsException, IOException {

		// declaring webclient object
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);

		// declaring i/o objects for writing in a text file
		File output = new File("items.txt");
		FileWriter fw = new FileWriter(output);
		BufferedWriter bw = new BufferedWriter(fw);

		int counter = 1;

		try {

			String base = "https://www.macys.com";
			String searchUrl = base + "/shop/womens-clothing/womens-activewear?id=29891";

			HtmlPage page = client.getPage(searchUrl);

			// fetching the next page in pagination baar.
			List<HtmlElement> pagination = (List<HtmlElement>) page.getByXPath("//li[@class='nextPage ']");

			// conditional while loop, to fetch all the pages which are in pagination
			while (pagination.size() != 0) {
				// fetching our targated product detail
				List<HtmlElement> macystopnav = (List<HtmlElement>) page.getByXPath("//div[@class='productDetail']");
				if (macystopnav.size() == 0) {
					System.out.println("no data to scrape.... !");
				} else {
					for (HtmlElement htmlItem : macystopnav) {

						HtmlElement price = ((HtmlElement) htmlItem.getFirstByXPath(".//span['regular']"));

						HtmlAnchor li = (HtmlAnchor) htmlItem.getFirstByXPath(".//a");
						String content = li.getTextContent().toString();
						
						//removing unwanted spaces from the fetched content
						content = content.trim();

						bw.write(counter + " -> " + content);
						bw.newLine();
						counter = counter + 1;
						System.out.println(counter + " -> " + content);
				
					}
					
					//once we fetch all items from one page (in pagination), then we have to fetch data from second page in pagination,
					//for that we are declaring new string variable, in which we will stroe the URL of next page, and pass it in htmlunit page variable
					String newpage = "";

					//condition for passing new page in the existing loop (to fetch all the pages in pagination)
					for (HtmlElement anchor : pagination) {
						
						HtmlAnchor link = (HtmlAnchor) anchor.getFirstByXPath(".//a");
						newpage = link.getHrefAttribute().toString();
					}
					
					//updating page and pagination variables to accomodate pagination scraping.
					page = client.getPage(base + newpage);
					pagination = (List<HtmlElement>) page.getByXPath("//li[@class='nextPage ']");
					
				}
			}
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
