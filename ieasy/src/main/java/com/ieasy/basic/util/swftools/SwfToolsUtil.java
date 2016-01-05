package com.ieasy.basic.util.swftools;

import javax.inject.Inject;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.ieasy.basic.util.file.FileUtils;
import com.ieasy.module.common.web.servlet.WebContextUtil;

/**
 * 文件转换调用接口
 * 
 * @author 张代浩
 * 
 */
@Component
public class SwfToolsUtil {
	
	@Inject
	private TaskExecutor taskExecutor;				//注入Spring提供的多线程接口，使邮件可异步发送
	
	public void convert2SWFAnyc(String inputFile) {
		String swfPath = WebContextUtil.getSc().getRealPath("/") + ConStant.SWFTOOLS_BASE_DIR + "/" ;
		System.out.println("input===" + swfPath);
		this.taskExecutor.execute(new convert2SWF(swfPath, inputFile)) ;
	}
	
	private class convert2SWF implements Runnable {
		private String swfPath;
		private String inputFile;
		public convert2SWF(String swfPath, String inputFile) {
			super();
			this.swfPath = swfPath;
			this.inputFile = inputFile;
		}
		@Override
		public void run() {
			convert2SWF(swfPath, inputFile);
		}
	}
	
	private static void convert2SWF(String swfPath, String inputFile) {
		String extend = FileUtils.getExtend(inputFile);
		PDFConverter pdfConverter = new OpenOfficePDFConverter();
		SWFConverter swfConverter = new SWFToolsSWFConverter();
		if (extend.equals("pdf")) {
			swfConverter.convert2SWF(inputFile, swfPath, extend);
		}
		if (extend.equals("doc") || extend.equals("docx") || extend.equals("xls") || extend.equals("pptx") || extend.equals("xlsx") || extend.equals("ppt") || extend.equals("txt") || extend.equals("odt")) {
			DocConverter converter = new DocConverter(pdfConverter, swfConverter);
			converter.convert(inputFile, swfPath, extend);
		}
	}

	public static void main(String[] args) {
		String swfPath = WebContextUtil.getSc().getRealPath("/") + ConStant.SWFTOOLS_BASE_DIR + "/" ;
		SwfToolsUtil.convert2SWF(swfPath, "D:\\work\\test\\dd.pdf");
		
		//request.getSession().getServletContext().getRealPath("/") + SWFTOOLS_BASE_DIR + "/"
	}

}
