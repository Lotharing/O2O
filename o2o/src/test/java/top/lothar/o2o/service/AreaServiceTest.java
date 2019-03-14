package top.lothar.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.entity.Area;

public class AreaServiceTest extends BaseTest{
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private CacheService cacheService;
	
	@Test
	public void testGetAreaList() {
		List<Area> areaList = areaService.getAreaList();
		assertEquals("枣园", areaList.get(0).getAreaName());
		
		cacheService.removeFromCache(areaService.AREALISTKEY);
		
		areaList = areaService.getAreaList();
		System.out.println(areaList.size());
	}
}
