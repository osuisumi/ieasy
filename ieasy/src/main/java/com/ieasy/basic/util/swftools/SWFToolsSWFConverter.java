package com.ieasy.basic.util.swftools;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.ieasy.basic.util.PinyinUtil;
import com.ieasy.basic.util.file.FileUtils;

public class SWFToolsSWFConverter implements SWFConverter {

	private static Logger logger = Logger.getLogger(SWFToolsSWFConverter.class);

	/** SWFTools pdf2swf.exe路径 */
	// private static String PDF2SWF_PATH = ConStant.SWFTOOLS_PDF2SWF_PATH;

	public void convert2SWF(String inputFile, String swfFile, String swfPath, String extend) {
		File pdfFile = new File(inputFile);
		File outFile = new File(swfFile);

		if (!pdfFile.exists()) {
			logger.info("PDF文件不存在！");
			return;
		}

		if (outFile.exists()) {
			logger.info("SWF文件已存在！");
			return;
		}
		
		//pdf2swf.exe -t c:\test\OA办公系统概要设计.pdf -o c:\test\dd.swf -s flashversion=9
		//String command = ConStant.getSWFToolsPath(extend) + " \"" + inputFile + "\" -o " + " \"" + swfFile + " \"" + " -s languagedir=D:\\work\\tools\\xpdf\\xpdf-chinese-simplified -T 9 -f";
		// + "\" -o " + swfFile + " -s languagedir=D:\\xpdf-chinese-simplified -T 9 -f";
				
		String command = ConStant.getSWFToolsPath(swfPath, extend) + " \"" + inputFile + "\" -o " + " \"" + swfFile + " \"" + " -s languagedir=D:\\xpdf-chinese-simplified -T 9 -f";
		// + "\" -o " + swfFile + " -s languagedir=D:\\xpdf-chinese-simplified -T 9 -f";
		
		try {
			// 开始转换文档
			Process process = Runtime.getRuntime().exec(command);
			StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "Error");
			StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "Output");
			errorGobbler.start();
			outputGobbler.start();
			try {
				process.waitFor();
				logger.info("时间-------" + process.waitFor());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void convert2SWF(String inputFile, String swfPath, String extend) {
		String swfFile = PinyinUtil.getPinYinHeadChar(FileUtils.getFilePrefix2(inputFile)) + ".swf";
		convert2SWF(inputFile, swfFile, swfPath, extend);
	}
}
