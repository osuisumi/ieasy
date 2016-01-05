package com.ieasy.basic.util.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarOutputStream;

/**
 * 通过 Apache Tool 进行JAVA tar || tar.gz
 */
public class TarAndGzUtil {
	
	private static Log log = LogFactory.getLog(TarAndGzUtil.class) ;
	
	public static void main(String[] args) throws Exception {

		tarUtil("D:\\aa", "D:\\aa.tar") ;
		//tarUtil(new File("D:\\work\\test"), new File("D:\\work\\test.tar")) ;
		
		//UnTarUtil("D:\\work\\test.tar", "D:\\work\\UnTar") ;
		//UnTarUtil(new File("D:\\work\\test.tar"), "D:\\work\\UnTar") ;
		
		//tarGzUtil("D:\\work\\杂项.tar", "D:\\work\\test.tar.gz") ;
		//tarGzUtil(new File("D:\\work\\test.tar"), new File("D:\\work\\test.tar.gz")) ;
		
		//UnTarGzUtil("D:\\work\\test.tar.gz", "D:\\work\\UnTarGz") ;
		//UnTarGzUtil(new File("D:\\work\\test.tar.gz"), "D:\\work\\UnTarGz") ;
	}
	

	/**
	 * 归档tar文件
	 * @param srcStr 原文件或目录
	 * @param targetTarStr 输出目标归档文件
	 */
	public static File tarUtil(String srcStr, String targetTarStr) {
		// 要压缩的文件对象
		File srcFile = new File(srcStr);
		// 压缩后的文件名
		File targetTarFile = new File(targetTarStr);
		return tarUtil(srcFile, targetTarFile) ;
	}
	
	/**
	 * 归档tar文件
	 * @param srcFile 原文件或目录
	 * @param targetTarFile 输出目标归档文件
	 */
	public static File tarUtil(File srcFile, File targetTarFile) {
		log.debug("开始归档文件，目标文件："+srcFile+"  输出文件："+targetTarFile) ;
		TarOutputStream out = null;
		boolean boo = false;// 是否压缩成功
		try {
			out = new TarOutputStream(new BufferedOutputStream(new FileOutputStream(targetTarFile)));
			tar(srcFile, out, "", true);
			boo = true;
			log.debug("归档文件结束，目标文件："+srcFile+"  输出文件："+targetTarFile) ;
			return targetTarFile ;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException ex) {
				ex.printStackTrace() ;
				throw new RuntimeException("关闭Tar输出流出现异常，可能是空文件夹！", ex);
			} finally {
				// 清理操作
				if (!boo && targetTarFile.exists())// 归档不成功,
					targetTarFile.delete();
			}
		}
	}
	
	
	/**
	 * 归档tar文件
	 * @param srcStr 原文件或目录
	 * @param targetGzFile 输出目标归档文件
	 */
	public static File tarGzUtil(String srcTarFile, String targetGzFile) {
		// 要压缩的文件对象
		File srcFile = new File(srcTarFile);
		// 压缩后的文件名
		File targetTarFile = new File(targetGzFile);
		return tarGzUtil(srcFile, targetTarFile) ;
	}
	
	/**
	 * 压缩归档tar.gz文件
	 * @param srcTarFile tar归档文件
	 * @param targetGzFile 数据出目标压缩文件
	 */
	public static File tarGzUtil(File srcTarFile, File targetGzFile) {
		log.debug("开始压缩归档，目标文件："+srcTarFile+"  输出文件："+targetGzFile) ;
		
		GZIPOutputStream out = null;
		InputStream in = null;
		boolean boo = false;// 是否成功
		try {
			in = new FileInputStream(srcTarFile);
			out = new GZIPOutputStream(new FileOutputStream(targetGzFile), 1024 * 2);
			byte b[] = new byte[1024 * 2];
			int length = 0;
			while ((length = in.read(b)) != -1) {
				out.write(b, 0, length);
			}
			boo = true;
			log.debug("归档压缩结束，目标文件："+srcTarFile+"  输出文件："+targetGzFile) ;
			return targetGzFile ;
		} catch (Exception ex) {
			throw new RuntimeException("压缩归档文件失败", ex);
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException ex) {
				throw new RuntimeException("关闭流出现异常", ex);
			} finally {
				if (!boo) {// 清理操作
					srcTarFile.delete();
					if (targetGzFile.exists())
						targetGzFile.delete();
				}
			}
		}
	}
	
	/**
	 * 解压归档tar文件
	 * @param srcTarStr 要解压的归档文件
	 * @param destDir 解压输出的目录
	 */
	public static void UnTarUtil(String srcTarStr, String destDir) {
		File srcTarFile = new File(srcTarStr);// 要压缩的文件对象
		UnTarUtil(srcTarFile, destDir) ;
	}
	
	/**
	 * 解压归档tar文件
	 * @param srcTarFile 要解压的归档文件
	 * @param destDir 解压输出的目录
	 */
	public static void UnTarUtil(File srcTarFile, String destDir) {
		boolean boo = false;// 是否压缩成功
		try {
			unTar(srcTarFile, destDir);
			boo = true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// 清理操作
			if (!boo)
				deleteDirectory(new File(destDir));// 目标文件夹 。清理
		}
	}
	
	/**
	 * 解压归档tar文件
	 * @param srcTarGzStr 要解压的gz压缩文件
	 * @param destDir 解压输出的目录
	 */
	public static void UnTarGzUtil(String srcTarGzStr, String destDir) {
		File srcTarGzFile = new File(srcTarGzStr);
		UnTarGzUtil(srcTarGzFile, destDir) ;
	}
	
	/**
	 * 解压归档tar文件
	 * @param srcTarGzFile 要解压的gz压缩文件
	 * @param destDir 解压输出的目录
	 */
	public static void UnTarGzUtil(File srcTarGzFile, String destDir) {
		boolean boo = false;// 是否压缩成功
		try {
			unTarGz(srcTarGzFile, destDir);
			boo = true;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// 清理操作
			if (!boo)
				deleteDirectory(new File(destDir));// 目标文件夹 。清理
		}
	}
	
	

	/**
	 * 归档tar文件
	 * @param file 归档的文件对象
	 * @param out 输出tar流
	 * @param dir 相对父目录名称
	 * @param boo 是否把空目录归档进去
	 */
	private static void tar(File file, TarOutputStream out, String dir, boolean boo) throws IOException {
		if (file.isDirectory()) {// 是目录
			File[] listFile = file.listFiles();// 得出目录下所有的文件对象
			if (listFile.length == 0 && boo) {// 空目录归档
				out.putNextEntry(new TarEntry(dir + file.getName() + "/"));// 将实体放入输出Tar流中
				log.debug("归档目录："+file.getAbsolutePath()) ;
				return;
			} else {
				for (File cfile : listFile) {
					tar(cfile, out, dir + file.getName() + "/", boo);// 递归归档
				}
			}
		} else if (file.isFile()) {// 是文件
			log.debug("归档文件："+file.getAbsolutePath()) ;
			byte[] bt = new byte[2048 * 2];
			TarEntry ze = new TarEntry(dir + file.getName());// 构建tar实体
			// 设置压缩前的文件大小
			ze.setSize(file.length());
			// ze.setName(file.getName());//设置实体名称.使用默认名称
			out.putNextEntry(ze);// //将实体放入输出Tar流中
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				int i = 0;
				while ((i = fis.read(bt)) != -1) {// 循环读出并写入输出Tar流中
					out.write(bt, 0, i);
				}
			} catch (IOException ex) {
				throw new IOException("写入归档文件出现异常", ex);
			} finally {
				try {
					if (fis != null)
						fis.close();// 关闭输入流
					out.closeEntry();
				} catch (IOException ex) {
					throw new IOException("关闭输入流出现异常");
				}
			}
		}
	}
	

	/**
	 * 解压tar File
	 * @param file 要解压的tar文件对象
	 * @param outputDir 要解压到某个指定的目录下
	 * @throws IOException
	 */
	private static void unTar(File file, String outputDir) throws IOException {
		TarInputStream tarIn = null;
		try {
			tarIn = new TarInputStream(new FileInputStream(file), 1024 * 2);
			createDirectory(outputDir, null);// 创建输出目录
			TarEntry entry = null;
			while ((entry = tarIn.getNextEntry()) != null) {
				if (entry.isDirectory()) {// 是目录
					createDirectory(outputDir, entry.getName());// 创建空目录
				} else {// 是文件
					File tmpFile = new File(outputDir + "/" + entry.getName());
					createDirectory(tmpFile.getParent() + "/", null);// 创建输出目录
					OutputStream out = null;
					try {
						out = new FileOutputStream(tmpFile);
						int length = 0;
						byte[] b = new byte[2048];
						while ((length = tarIn.read(b)) != -1) {
							out.write(b, 0, length);
						}
					} catch (IOException ex) {
						throw ex;
					} finally {
						if (out != null)
							out.close();
					}
				}
			}
		} catch (IOException ex) {
			throw new IOException("解压归档文件出现异常", ex);
		} finally {
			try {
				if (tarIn != null) {
					tarIn.close();
				}
			} catch (IOException ex) {
				throw new IOException("关闭tarFile出现异常", ex);
			}
		}
	}

	
	/**
	 * 解压tar.gz 文件
	 * @param file 要解压的tar.gz文件对象
	 * @param outputDir 要解压到某个指定的目录下
	 * @throws IOException
	 */
	private static void unTarGz(File file, String outputDir) throws IOException {
		TarInputStream tarIn = null;
		try {
			tarIn = new TarInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file))), 1024 * 2);
			createDirectory(outputDir, null);// 创建输出目录
			TarEntry entry = null;
			while ((entry = tarIn.getNextEntry()) != null) {
				if (entry.isDirectory()) {// 是目录
					createDirectory(outputDir, entry.getName());// 创建空目录
				} else {// 是文件
					File tmpFile = new File(outputDir + "/" + entry.getName());
					createDirectory(tmpFile.getParent() + "/", null);// 创建输出目录
					OutputStream out = null;
					try {
						out = new FileOutputStream(tmpFile);
						int length = 0;
						byte[] b = new byte[2048];
						while ((length = tarIn.read(b)) != -1) {
							out.write(b, 0, length);
						}
					} catch (IOException ex) {
						throw ex;
					} finally {
						if (out != null)
							out.close();
					}
				}
			}
		} catch (IOException ex) {
			throw new IOException("解压归档文件出现异常", ex);
		} finally {
			try {
				if (tarIn != null) {
					tarIn.close();
				}
			} catch (IOException ex) {
				throw new IOException("关闭tarFile出现异常", ex);
			}
		}
	}

	
	/**
	 * 构建目录
	 * 
	 * @param outputDir
	 * @param subDir
	 */
	private static void createDirectory(String outputDir, String subDir) {
		File file = new File(outputDir);
		if (!(subDir == null || subDir.trim().equals(""))) {// 子目录不为空
			file = new File(outputDir + "/" + subDir);
		}
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 清理文件(目录或文件)
	 * 
	 * @param file
	 */
	private static void deleteDirectory(File file) {
		if (file.isFile()) {
			file.delete();// 清理文件
		} else {
			File list[] = file.listFiles();
			if (list != null) {
				for (File f : list) {
					deleteDirectory(f);
				}
				file.delete();// 清理目录
			}
		}
	}
}
