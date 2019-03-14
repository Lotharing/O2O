package top.lothar.o2o.service;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import top.lothar.o2o.BaseTest;
import top.lothar.o2o.entity.HeadLine;

public class HeadLineServiceTest extends BaseTest{
	
	@Autowired
	private HeadLineService headLineService;
	
	@Test
	public void testQueryHeadLine() throws IOException{
		List<HeadLine> headList = headLineService.getHeadLineList(new HeadLine());
		System.out.println(headList.size());
	}
}
