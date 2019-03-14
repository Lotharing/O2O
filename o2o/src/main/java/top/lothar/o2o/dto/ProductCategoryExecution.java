package top.lothar.o2o.dto;

import java.util.List;

import top.lothar.o2o.entity.ProductCategory;
import top.lothar.o2o.enums.ProductCategoryStateEnum;

public class ProductCategoryExecution {
	//结果状态
	private int state;
	//状态标识
	private String stateInfo;
	//存储的商品列表
	private List<ProductCategory> productCategoryList;
	
	public ProductCategoryExecution() {
		
	}
	/**
	 *  失败构造器
	 * @param StateEnum
	 */
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum) {
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
	}
	/**
	 * 成功构造器
	 * @param stateEnum
	 * @param productCategoriyList
	 */
	public ProductCategoryExecution(ProductCategoryStateEnum stateEnum,List<ProductCategory> productCategoriyList) {
		this.state=stateEnum.getState();
		this.stateInfo=stateEnum.getStateInfo();
		this.productCategoryList=productCategoriyList;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}
	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}
	
}
