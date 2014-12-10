package pl.edu.agh.analizer.youtube.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

public class SortingUtilTest {

	@Test
	public void descnedingSortForUnsortedMapTest(){
		Map<String,Integer> testMap = new HashMap<String,Integer>();
		testMap.put("Analysis 1", new Integer(200));
		testMap.put("Analysis 2", new Integer(400));
		
		List<Entry<String,Integer>> list = SortingUtil.entriesSortedByValues(testMap);
		
		assertEquals(400,list.get(0).getValue().intValue());
	}
	
	@Test
	public void descnedingSortForSortedMapTest(){
		Map<String,Integer> testMap = new HashMap<String,Integer>();
		testMap.put("Analysis 1", new Integer(400));
		testMap.put("Analysis 2", new Integer(200));
		
		List<Entry<String,Integer>> list = SortingUtil.entriesSortedByValues(testMap);
		
		assertEquals(400,list.get(0).getValue().intValue());
	}
	
}
