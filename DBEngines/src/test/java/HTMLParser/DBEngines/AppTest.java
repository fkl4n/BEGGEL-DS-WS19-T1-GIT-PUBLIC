package HTMLParser.DBEngines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class AppTest {

	@Test
	void parseDBEngineSystemsOracleTest() throws IOException, Exception {
		Set<String> urls = new HashSet<String>();
		urls.add("https://db-engines.com/de/system/Oracle");
		List<String[]> parseDBEngine = App.parseDBEngine(urls);
		assertEquals(parseDBEngine.get(0).length ,37);
	}
	
	@Test
	void parseDBEngineSystemsPostGreTest() throws IOException, Exception {
		Set<String> urls = new HashSet<String>();
		urls.add("https://db-engines.com/de/system/PostgreSQL");
		List<String[]> parseDBEngine = App.parseDBEngine(urls);
		assertEquals(parseDBEngine.get(0).length ,37);
	}
	
	@Test
	void parseDBEngineSystemsCouchbaseTest() throws IOException, Exception {
		Set<String> urls = new HashSet<String>();
		urls.add("https://db-engines.com/de/system/Couchbase");
		List<String[]> parseDBEngine = App.parseDBEngine(urls);
		assertEquals(parseDBEngine.get(0).length ,36);
	}
	
	@Test
	void parseDBEngineTest() throws IOException, Exception {		
		Set<String> parseDBEngine = App.getDBs();		
		assertEquals(parseDBEngine.size(), 393);
	}
	
	@Test
	void parseDBEngineSystemsJackrabbitTest() throws IOException, Exception {
		Set<String> urls = new HashSet<String>();
		urls.add("https://db-engines.com/de/system/Jackrabbit");
		List<String[]> parseDBEngine = App.parseDBEngine(urls);
		assertEquals(parseDBEngine.get(0).length ,12);
	}
	

}
