package HTMLParser.DBEngines;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
	private static final String DB_ENGINES_SYSTEMS_URL = "https://db-engines.com/de/systems";
	private static Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) throws Exception {
		Set<String> urls = getDBs();
		List<String[]> parseDBEngine = parseDBEngine(urls);
		export(parseDBEngine);

	}

	protected static List<String[]> parseDBEngine(Set<String> urls) throws IOException, Exception {
		List<String> columnName = new ArrayList<String>();
		columnName.add("URLName");
		List<String[]> table = new ArrayList<String[]>();
		int index = 0;
		logger.trace("Start Parsing");

		for (String url : urls) {
			String[] tableRow = new String[100];
			tableRow[0] = extractURLName(url);
			table.add(index++, tableRow);
			logger.trace("Fetching %s..." + url);
			System.out.println((tableRow[0]));
			Document doc = Jsoup.connect(url).get();
			for (Element element : doc.getElementsByTag("table")) {
				if (isTableWithData(element)) {
					extractTable(columnName, tableRow, element);
				}
			}
		}
		List<String[]> DBTable = new ArrayList<String[]>();

		DBTable.add(columnName.toArray(new String[columnName.size()]));
		for (String[] strings : table) {
			DBTable.add(Arrays.copyOfRange(strings, 0, columnName.size()));
		}

		return DBTable;
	}

	private static String extractURLName(String url) {
		String[] split = url.split("/");
		String res = "";
		
		try {
			res = URLDecoder.decode(split[split.length - 1],StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return res;
	}

	protected static void export(List<String[]> table) throws Exception {
		StringBuilder builder = new StringBuilder();
		for (String[] strings : table) {
			for (int i = 0; i < strings.length; i++) {
				String CSVSeperator = ";";
				String output = "";
				if (Objects.nonNull(strings[i]))
					output = strings[i].replace(CSVSeperator, ",");
				else
					output = strings[i];

				builder.append(output + CSVSeperator);
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append("\n");
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter("DBRanking.csv"));
		writer.write(builder.toString());
		writer.close();

	}

	protected static void extractTable(List<String> columnName, String[] tableRow, Element ele) {
		for (Element tableChilds : ele.children()) {
			for (Element row : tableChilds.getElementsByTag("tr")) {
				if (row.text().startsWith("Weitere Informationen bereitgestellt vom Systemhersteller")) {
					return;
				}
				
				String column = "";
				Elements elementsByClass = row.getElementsByClass("attribute");

				if (elementsByClass.size() > 0) {

					Element element = elementsByClass.get(0);

					if (element != null) {
						column = extractAttributeOwnText(element);
						if (column.startsWith("DB-Engines Ranking")) {
							extractRanking(row, columnName, tableRow);
							continue;
						}
						if(column.startsWith("Primäres Datenbankmodell")) {
							continue;
						}
						if (!columnName.contains(column)) {
							columnName.add(column);	
						}
					}
				}

				elementsByClass = row.getElementsByClass("value");
				elementsByClass.addAll(row.getElementsByClass("header"));
				if (elementsByClass.size() > 0) {
					Element element = elementsByClass.get(0);
					if (element != null) {
						if (columnName.indexOf(column) < 0) {
							return;
						}
						if(column.startsWith("Lizenz")) {
							String content = extractAttribute(element);
							
							if(content.startsWith("Open Source")) {
								tableRow[columnName.indexOf(column)] = "frei";
							}
							else {
								tableRow[columnName.indexOf(column)] = "kommerziell";
							}
						}
						else if(column.startsWith("Erscheinungsjahr")){
							String[] split = extractAttribute(element).split(" ");
							
							tableRow[columnName.indexOf(column)] = split[0];
						}
						else if(column.startsWith("Implementierungssprache")){
							String[] split = extractAttribute(element).replaceAll(" ", "").split("(\\s+)|(\\s*und\\s*)|(\\s*and\\s*)");
							
							String res = "";
							
							for(int i = 0; i < split.length; i++) {
								res+= split[i];
								if(i< split.length-1) res+=",";
							}
							
							tableRow[columnName.indexOf(column)] = res;
						}
						else {
							tableRow[columnName.indexOf(column)] = extractAttribute(element);
						}
					}
				}

			}
		}

	}
	
	private static void extractRanking(Element row, List<String> columnName, String[] tableRow) {
		Elements elementsByClass = row.getElementsByClass("value");
		
		String primaryDatabaseModel = "Primäres Datenbankmodell";
		if (!columnName.contains(primaryDatabaseModel)) {
			columnName.add(primaryDatabaseModel);
		}
		
		for(Element element : elementsByClass) {
			Elements elementsByTag = element.getElementsByTag("tr");
			for (Element elementTag : elementsByTag) {
				String text = elementTag.text();
				if (text.startsWith("Punkte") || text.startsWith("Rang")) {
					String[] split = text.split(" ");
					if (!columnName.contains(split[0])) {
						columnName.add(split[0]);
					}
						
					tableRow[columnName.indexOf(split[0])] = ("" + split[1].replaceAll("#", "")).replace(",", ".");
				} else if (text.startsWith("#")) {
					
//					String secRank = "Sekundärer Rang";
//					if (!columnName.contains(secRank)) {
//						columnName.add(secRank);
//					}
//					
//					String secRankCategory = "Sekundäre Kategorie";
//					if (!columnName.contains(secRankCategory)) {
//						columnName.add(secRankCategory);
//					}
//					
//					tableRow[columnName.indexOf(secRankCategory)] = text.substring(text.indexOf(" ")+1);
//					tableRow[columnName.indexOf(secRank)] = text.substring(1, text.indexOf(" "));
					
					String secCatogoryColumn = text.substring(text.indexOf(" ")+1);
					
					if(tableRow[columnName.indexOf(primaryDatabaseModel)] == null || tableRow[columnName.indexOf(primaryDatabaseModel)].isBlank()) {
						tableRow[columnName.indexOf(primaryDatabaseModel)] = secCatogoryColumn;
					}
					else {
						tableRow[columnName.indexOf(primaryDatabaseModel)] += ", " + secCatogoryColumn;
					}

					if (!columnName.contains(secCatogoryColumn)) {
						columnName.add(secCatogoryColumn);
					}
					
					tableRow[columnName.indexOf(secCatogoryColumn)] = text.substring(1, text.indexOf(" "));
				}

			}
		}

	}

	protected static String extractAttributeOwnText(Element elementByClass) {
		String ownText = elementByClass.ownText();
		if (ownText.isBlank()) {
			ownText = elementByClass.text();
		}
		return ownText;

	}

	protected static String extractAttribute(Element elementByClass) {
		return elementByClass.text();
	}

	protected static boolean isTableWithData(Element ele) {
		return ele.hasClass("tools");
	}

	protected static Set<String> getDBs() throws IOException {

		Set<String> dbUrls = new HashSet<String>();
		Document doc = Jsoup.connect(DB_ENGINES_SYSTEMS_URL).get();
		Elements links = doc.select("a[href]");

		for (Element element : links) {
			String attr = element.attr("abs:href");
			/*if (dbUrls.size() > 5)
				return dbUrls;*/
			if (attr.startsWith("https://db-engines.com/de/system/"))
				dbUrls.add(attr);

		}
		return dbUrls;
	}

	
}
