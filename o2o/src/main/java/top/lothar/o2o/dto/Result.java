package top.lothar.o2o.dto;
/*
 * 封装json对象，所有的返回结果使用它
 */
public class Result<T> {
	private boolean success;//是否成功标志
	private T data;//成功是返回的数据
	private String errorMsg;//错误信息
	private int errorCode; //状态码
	
	public Result(){
		
	}
	
	/*
	 * 成功时候使用的构造器
	 */
	public Result(boolean success,T data){
		this.data=data;
		this.success=success;
	}
	
	/*
	 * 错误时候使用的构造器
	 */
	public Result(boolean success,int errorCode,String errorMsg){
		this.errorCode=errorCode;
		this.errorMsg=errorMsg;
		this.success=success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getdata() {
		return data;
	}

	public void setdata(T data) {
		this.data = data;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
}
