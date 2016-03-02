package com.qingruan.museum.admin.web.uploadImg;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/uploadify")
public class upload {
//
//	@RequestMapping(value = "/list")
//	public String list() {
//		return "/uploadImg/upload";
//	}
//
//	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
//	public void uploadFile(HttpServletResponse response,HttpServletRequest request,@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
//		System.out.println("进来了~~~~~~~");
//		byte[] bytes = file.getBytes();
//		System.out.println("文件名称：" + file.getOriginalFilename());
//		String uploadDir = request.getRealPath("/") + "upload";
//		System.out.println("路径==========================" + uploadDir);
//		File dirPath = new File(uploadDir);
//		if (!dirPath.exists()) {
//			dirPath.mkdirs();
//		}
//		String sep = System.getProperty("file.separator");
//		File uploadedFile = new File(uploadDir + sep
//				+ file.getOriginalFilename());
//		FileCopyUtils.copy(bytes, uploadedFile);
//	}
}
