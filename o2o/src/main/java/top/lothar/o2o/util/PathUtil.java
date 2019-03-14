package top.lothar.o2o.util;
/**
 * 项目图片路径类
 * @author Lothar
 *
 */
public class PathUtil {
	// 获取文件分隔符
	private static String separator = System.getProperty("file.separator");

	//返回项目图片根路径
	public static String getImgBasePath() {

		// 获取当前操作系统
		String os = System.getProperty("os.name");
		String basePath = "";

		if (os.toLowerCase().startsWith("win")) {
			basePath = "D:/projectdev/image/";
		} else {
			basePath = "home/xiangze/image/";
		}
		basePath = basePath.replace("/", separator);
		
		return basePath;
	}

	// 返回项目图片子路径
	public static String getShopImagePath(long shopId) {
		String imagePath = "/upload/images/item/shop/" + shopId + "/";
		return imagePath.replace("/", separator);
	}
}
