package top.lothar.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import top.lothar.o2o.dto.ImageHolder;
/**
 * 图片处理类
 * @author Lothar
 *
 */
public class ImageUtil {
	
	//获取当前主线程执行陆行
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	//日期标准格式（用来生成文件名）
	private static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	//随机数（5位数配合文件名）
	private static final Random r = new Random();
	//启用日志
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	/**
	 * 将CommonsMultipartFile转换成File类
	 * 
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}
	/**
	 * 将上传的文件增加水印放入指定目录
	 * @param thumbnail		传入的文件实体（名字和流）
	 * @param targetAddr	存放目标路径
	 * @return
	 */
	public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) {
		//随机生成名
		String realFileName = getRandomFileName();
		//获取文件扩展名
		String extension = getFileExtension(thumbnail.getImageName());
		//建立目标文件夹
		makeDirPath(targetAddr);
		//最终图片存储路径
		String relativeAddr = targetAddr+realFileName+extension;
		logger.debug("current relativeAddr is :" + relativeAddr);
		//建立最终存储图片地址文件源(操作系统指定路径+图片存储路径)
		File dest = new File(PathUtil.getImgBasePath()+relativeAddr);
		logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("basePath is :" + basePath);
		try {
			Thumbnails.of(thumbnail.getImage()).size(200, 200)
			.watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"watermark.jpg")),0.25f)
			.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			// TODO: handle exception
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}
	/**
	 * 将上传的文件（商品详情图）增加水印放入指定目录
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
		// 获取不重复的随机名
		String realFileName = getRandomFileName();
		// 获取文件的扩展名如png,jpg等
		String extension = getFileExtension(thumbnail.getImageName());
		// 如果目标路径不存在，则自动创建
		makeDirPath(targetAddr);
		// 获取文件存储的相对路径(带文件名)
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is :" + relativeAddr);
		// 获取文件要保存到的目标路径
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("basePath is :" + basePath);
		// 调用Thumbnails生成带有水印的图片
		try {
			Thumbnails.of(thumbnail.getImage()).size(337, 640)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
					.outputQuality(0.9f).toFile(dest);
		} catch (IOException e) {
			logger.error(e.toString());
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		// 返回图片相对路径地址
		return relativeAddr;
	}
	/**
	 * 创建目标路径所涉及到的目录，即home/work/xiangze/xxx.jpg
	 * 那么home work xiangze这三个文件夹都需要创建出来
	 * @param targetAddr	所选文件的路径
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
		File dirFile = new File(realFileParentPath);
		if(!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
	}
	/**
	 * 获取输入文件流的扩展名
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}
	/**
	 * 生成图片的随机名规则
	 * 当前时间年月日时分秒+5位随机数
	 * @return
	 */
	private static String getRandomFileName() {
		//获取随机五位数
		int rannum = r.nextInt(89999)+10000;
		//按照固定格式返回时间
		String nowTimeStr = DateFormat.format(new Date());
		//字符串+整型自动转化为字符串
		return nowTimeStr+rannum;
	}
	/**
	 * 判断storePath是文件路径还是目录路径
	 * 如果是文件则删除文件
	 * 如果是目录则删除该目录下的所有文件
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath) {
		File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
		if(fileOrPath.exists()){
			if(fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for(int i = 0 ; i < files.length ; i++) {
					files[i].delete();
				}
			}
			fileOrPath.delete();
		}
	}
	
}
