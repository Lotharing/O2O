package top.lothar.o2o.util;

public class PageCalculator {
	/**
	 * Service->Dao时页码对应DAO中的检索范围
	 * 例如第二页，pageIndex=2  就是从0到2-1 * pagesize 这里的检索
	 * ？前边是执行条件，:前边是true执行 ， 后边是false执行
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static int calculatorRowIndex(int pageIndex , int pageSize) {
		return (pageIndex > 0)?(pageIndex-1)*pageSize:0;
	}
	
}
