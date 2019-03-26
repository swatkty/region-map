package egovframework.example.util;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import egovframework.example.util.ParamMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("unchecked")
public class FileUtil {
	
	/**
	 * 파일 업로드
	 * @param formFile
	 * @param uploadPath 업로드 root 경로
	 * @return
	 */
	public static ParamMap upload(MultipartFile formFile, String uploadPath) {
		String orgFileName		= formFile.getOriginalFilename(); // 원본 파일명
		String fileExt 			= FilenameUtils.getExtension(orgFileName); // 파일 확장자
		InputStream input 		= null;
		OutputStream output		= null;
		ParamMap fileMap 		= null;
		String yyyymmdd 		= DateUtil.getToday();
		String uploadFileName 	= yyyymmdd + "_" + UniqueKeyGenerator.getUniqueKey() + "." + fileExt;
		
		// file upload directory
		uploadPath = uploadPath + File.separator + yyyymmdd + File.separator;
		
		try {
			// file upload directory make
			File uploadDir = new File(uploadPath);
			makeDir(uploadDir);

			input = formFile.getInputStream();

			output = new FileOutputStream(uploadPath + uploadFileName);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = input.read(buffer, 0, 8192)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
			
			// 이미지 파일 일 경우 가로 , 세로 사이즈를 구한다.
			int imgWidth = 0;
			int imgHeight = 0;
			if(StringUtils.isNotEmpty(formFile.getContentType()) && StringUtils.contains(formFile.getContentType().toLowerCase(), "image")) {
				Image srcImg = ImageIO.read(new File(uploadPath + uploadFileName));
				imgWidth = srcImg.getWidth(null);	// 원본 이미지의 가로
				imgHeight = srcImg.getHeight(null);	// 원본 이미지의 세로
			}
			
			fileMap = new ParamMap();
			fileMap.put("org_file_name", orgFileName);
			fileMap.put("upload_file_name", uploadFileName);
			fileMap.put("input_name", formFile.getName());
			fileMap.put("upload_path", uploadPath);
			fileMap.put("file_ext", fileExt);
			fileMap.put("file_size", formFile.getSize());
			fileMap.put("file_type", formFile.getContentType());
			fileMap.put("img_width", imgWidth);
			fileMap.put("img_height", imgHeight);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
				input.close();	
				output = null;
				input = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return fileMap;
	}
	
	/**
	 * 파일 다운로드
	 * @param request
	 * @param response
	 * @param realFileName		실제 파일명
	 * @param uploadFileName	업로드된 파일명
	 * @param type				다운로드 type (B : 기본, E : 에디터)
	 * @param thum				l : 목록 용 이미지, v : 상세 용 이미지, 나머지 : 원본 이미지
	 * @throws Exception
	 */
	public void download(HttpServletRequest request, HttpServletResponse response, String realFileName,	String uploadFileName, String type, String thum) throws Exception {
		String uploadRoot 	= PropertiesUtil.getValue("upload_root"); // upload root directory
		if("E".equals(type)) {
			uploadRoot = PropertiesUtil.getValue("upload_root_editor"); // upload root directory
		}
		
		String uploadPath 	= StringUtils.substring(uploadFileName, 0, 8); // upload middle path
		String filePath		= ""; // full file path
		
		uploadRoot = uploadRoot + File.separator + uploadPath + File.separator;
		if(StringUtils.isEmpty(thum)) { // 원본 이미지
			filePath = uploadRoot + uploadFileName;
		} else {
			String fileName = uploadFileName.substring(0, uploadFileName.lastIndexOf(".")); // 파일명
			String ext = uploadFileName.substring(uploadFileName.lastIndexOf(".")); // 확장자
			
			filePath = uploadRoot + fileName + "_" + thum + ext;
		}
		
		File file = new File(filePath);
		if (file == null || !file.exists() || file.length() <= 0 || file.isDirectory()) {
			throw new Exception("can not find file");
		}

		String mimetype = request.getSession().getServletContext().getMimeType(file.getName());
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			download(request, response, is, realFileName, file.length(), mimetype, type);
		} catch (Exception e) {
		} finally {
			try {
				is.close();
			} catch (Exception ex) {}
		}
	}
	
	/**
	 * 파일 다운로드 모듈
	 * @param request
	 * @param response
	 * @param is
	 * @param realFileName
	 * @param fileSize
	 * @param mimetype
	 * @param type
	 * @throws Exception
	 */
	private void download(HttpServletRequest request, HttpServletResponse response, InputStream is, String realFileName, long fileSize, String mimetype, String type) throws Exception {
		String mime = mimetype;
		String charSet = PropertiesUtil.getValue("character_set");
		if (mimetype == null || mimetype.length() == 0) {
			mime = "application/octet-stream;";
		}

		byte[] buffer = new byte[8192];
		response.setContentType(mime + "; charset=" + charSet);

		if(!"E".equals(type)) {
			String userAgent = request.getHeader("User-Agent");

			if (userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5 이하
				response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(realFileName, charSet) + ";");
			} else if (userAgent.indexOf("MSIE") > -1) { // MS IE (보통은 6.x 이상 가정)
				realFileName = realFileName.replaceAll(" ", "plmkijnhyrtfsdwerg578jh80jhrt56ghb");
				String realFileName2 = java.net.URLEncoder.encode(realFileName, charSet);
				realFileName2 = realFileName2.replaceAll("plmkijnhyrtfsdwerg578jh80jhrt56ghb", " ");
				response.setHeader("Content-Disposition", "attachment; filename=" + realFileName2 + ";");
			} else { // 모질라나 오페라
				response.setHeader("Content-Disposition", "attachment; filename=" + new String(realFileName.getBytes(charSet), "latin1") + ";");
			}
		}

		// 파일 사이즈가 정확하지 않을때는 아예 지정하지 않는다.
		if (fileSize > 0) {
			response.setHeader("Content-Length", "" + fileSize);
		}

		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(is);
			outs = new BufferedOutputStream(response.getOutputStream());
			int read = 0;

			while ((read = fin.read(buffer)) != -1) {
				outs.write(buffer, 0, read);
			}
		} finally {
			try {outs.close();} catch (Exception ex1) {}
			try {fin.close();} catch (Exception ex2) {}
		} // end of try/catch
	}

	/**
	 * 디렉토리 생성
	 * @param dir
	 */
	public static void makeDir(File dir) {
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	/**
	 * 파일 이동
	 * @param src 원본 파일 경로
	 * @param dest 대상 파일 경로
	 * @throws Exception
	 */
	public static void moveFiles(String src, String dest) throws Exception { 
		moveFiles(new File(src), new File(dest));	
	}
	
	/**
	 * 파일 이동
	 * @param src 원본 파일 객체
	 * @param dest 대상 파일 객체
	 * @throws Exception
	 */
	public static void moveFiles(File src, File dest) throws Exception	{
		copyFile(src, dest);
		src.delete();
	}
	
	/**
	 * 파일 복사
	 * @param src 원본 파일 경로
	 * @param dest 대상 파일 경로
	 * @throws Exception
	 */
	public static void copyFile(String src, String dest) throws Exception { 
		copyFile(new File(src), new File(dest));	
	}
	
	/**
	 * 파일 복사
	 * @param src 원본 파일 객체
	 * @param dest 대상 파일 객체
	 * @throws Exception
	 */
	public static void copyFile(File src, File dest) throws Exception {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		
		try {
			fin = new FileInputStream(src);
			fout = new FileOutputStream (dest);
			int c;
			while ((c = fin.read()) >= 0)
			fout.write(c);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fin.close();
			fout.close();
		}
	}
	
	/**
	 * 파일을 읽어 온다.
	 * @param request
	 * @param fileName
	 * @return
	 */
	public static String getFileRead(HttpServletRequest request, String fileName) {
		String content = "";
		InputStreamReader reader = null;
		BufferedReader br = null;
		String rootPath = PropertiesUtil.getValue("mail_form_root");

		try {
			reader = new InputStreamReader(new FileInputStream(request.getSession().getServletContext().getRealPath(rootPath + fileName)), "UTF8");
			br = new BufferedReader(reader);
			String line = br.readLine();

			while (line != null) {
				line.trim();
				content += line + "\n";
				line = br.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			content = "";
		} finally {
			try {
				br.close();
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}

		return content;
	}
}
