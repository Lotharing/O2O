package top.lothar.o2o.dto;

import java.util.List;

import top.lothar.o2o.entity.Product;
import top.lothar.o2o.enums.ProductStateEnum;


public class ProductExecution {
	
	private Product product;// 增删改用

	private int state;// 状态标识

	private String stateInfo;// 状态信息

	private List<Product> productList;// 查询列表用
	
    private int count;//分页查询用

	public ProductExecution() {

	}

	/*
	 * 失败时候使用的构造器
	 */
	public ProductExecution(ProductStateEnum productStateEnum) {
		this.state = productStateEnum.getState();
		this.stateInfo = productStateEnum.getStateInfo();
	}

	/*
	 * 成功时候使用的构造器
	 */
	public ProductExecution(ProductStateEnum productStateEnum, Product product) {
		this.state = productStateEnum.getState();
		this.stateInfo = productStateEnum.getStateInfo();
		this.product = product;
	}

	/*
	 * 成功时候使用的构造器
	 */
	public ProductExecution(ProductStateEnum productStateEnum, List<Product> productList) {
		this.state = productStateEnum.getState();
		this.stateInfo = productStateEnum.getStateInfo();
		this.productList = productList;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	

}
