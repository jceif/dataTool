package tool.file;

import tool.GUIDUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * 
 * @author hugo
 * 
 */
public class FileOperateUtil {
	private static final String REAL_NAME = "realName";

	private static final String SIZE = "size";
	private static final String SUFFIX = "suffix";
	private static final String CONTENT_TYPE = "contentType";
	private static final String CREATE_TIME = "createTime";
	public static String DOWNLOAD_PATH = "download/";
	public static String UPLOAD_PATH = "D:/Genuitec/upload/";

	/**
	 * 压缩后的文件名
	 * 
	 * @author geloin
	 * @date 2012-3-29 下午6:21:32
	 * @param name
	 * @return
	 */
	private static String zipName(String name) {
		String prefix = "";
		if (name.indexOf(".") != -1) {
			prefix = name.substring(0, name.lastIndexOf("."));
		} else {
			prefix = name;
		}
		return prefix + ".zip";
	}

	/**
	 * 将上传的文件进行重命名 规则为：节点编号+年月日时分+guid
	 * 
	 * @author geloin
	 * @date 2012-3-29 下午3:39:53
	 * @param name
	 * @return
	 */
	public static String getStorageName(String name) {
		// 日期格式(到分钟)
		String strDate = new SimpleDateFormat("yyyyMMddHHmm")
				.format(new Date());
		// uuid 保证文件不重名
		String uuid =  GUIDUtil.getGUIDString();
		String strNodeNo = getNodeNo();

		String fileName = strNodeNo + "_" + strDate + "_" + uuid;
		if (name.indexOf(".") != -1) {
			fileName+=name.substring(name.lastIndexOf("."));
		}

		return fileName;
	}

	/**
	 * 随机产生节点，让文件均匀分布在各个节点上
	 * 
	 * @return
	 */
	public static String getNodeNo() {

		return "N01";
	}

	/**
	 * 复制文件到制定位置
	 * 
	 * @param src
	 *            源文件
	 * @param dst
	 *            目标文件
	 * @throws IOException
	 */
	public static void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

}