package top.lothar.o2o.dto;
/**
 * 实体操作后的状态
 * @author Lothar
 *
 */
import java.util.List;

import top.lothar.o2o.entity.Shop;
import top.lothar.o2o.enums.ShopStateEnum;

public class ShopExecution {
	//结果状态
	private int state;
	//状态标识
	private String stateInfo;
	//店铺数量
	private int count;
	//操作的Shop（增删改时候用到）
	private Shop shop;
	//shop列表（查询店铺列表时候使用）
	private List<Shop> shopList;
	/**
	 * 无参构造
	 */
	public ShopExecution() {
		
	}
	/**
	 * 失败构造器，店铺操作失败时候所执行的对象
	 * 有参构造传入枚举类型,实现枚举信息传入本类私有属性
	 * @param stateEnum
	 */
	public ShopExecution(ShopStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}
	//成功构造器，店铺操作成功时候
	public ShopExecution(ShopStateEnum stateEnum,Shop shop) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shop=shop;
	}
	//成功构造器，店铺操作成功时候
	public ShopExecution(ShopStateEnum stateEnum,List<Shop> shopList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopList=shopList;
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	public List<Shop> getShopList() {
		return shopList;
	}
	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}
	
}
