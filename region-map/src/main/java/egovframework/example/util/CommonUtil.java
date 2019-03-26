package egovframework.example.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CommonUtil {
	/**
	 * 긴 주소 URL을 짧게 변경한다.
	 * @param longUrl
	 * @return
	 */
	public static String getShortURL(String longUrl) {
		String returnUrl = "";
		URL url = null;     
		String inputLine;
		BufferedReader in = null;
		   
		try {
		   String shortenURL = "http://api.bit.ly/v3/shorten";
		   String apiKey = PropertiesUtil.getValue("bit_ly_apikey");
		   String login = PropertiesUtil.getValue("bit_ly_login");
		   String format = "txt";

		   shortenURL += "?login=" + login;
		   shortenURL += "&apiKey=" + apiKey;
		   shortenURL += "&longUrl=" + URLEncoder.encode(longUrl, "UTF-8");
		   shortenURL += "&format=" + format;
		   
		   url = new URL(shortenURL);    
		   in = new BufferedReader(new InputStreamReader(url.openStream()));         
		   while ((inputLine = in.readLine()) != null) {             
			   returnUrl = inputLine;
		   }
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			try {in.close();}catch(Exception e){e.printStackTrace();};
		}
		
		return returnUrl;
	}
	
	/**
	 * 문자열을 정규화 표현식으로 변환
	 * @param str
	 * @return
	 */
	public static String strToCode(String str) {
		str = str.replaceAll("&", "&amp;")
		 		 .replaceAll("<", "&lt;")
		 		 .replaceAll(">", "&gt;")
		 		 .replaceAll("'", "&#039;")
		 		 .replaceAll("\"", "&#034;");
		
		return str;
	}
	
	/**
	 * 정규화 표현식을 문자열로 변환
	 * @param str
	 * @return
	 */
	public static String codeToStr(String str) {
		str = str.replaceAll("&lt;", "<")
		 		 .replaceAll("&gt;", ">")
		 		 .replaceAll("&#039;", "'")
		 		 .replaceAll("&#034;", "\"")
		 		 .replaceAll("&amp;", "&");
		
		return str;
	}

	/**
	 * 문자열을 HTML 표현으로 바꿔준다.
	 * @param str
	 * @return
	 */
	public static String strToHtml(String str) {
		str = str.replaceAll("<", "&lt;")
		 		 .replaceAll(">", "&gt;")
		 		 .replaceAll("\r\n", "<br/>")
		 		 .replaceAll("\n", "<br/>")
		 		 .replaceAll(" ", "&nbsp;");

		return str;
	}
	
	/**
	 * 주소 정보를 좌표로 변환한다.
	 * @param addr1 주소정보 : 시도 + 구군 + 동
	 * @param addr2 상세주소 : 상세번지
	 * @param coordIdx 출력 좌표 체계 (0 : [latlng]위도/경도 좌표, 1 : [tm128]평면 좌표계. 종/횡 좌표)
	 * @return
	 */
	public static String[] getCoordinate(String addr1, String addr2, int coordIdx) {
		String coordArr[] = null; // [0] : x 좌표, [1] : y 좌표
		String coordStr = "latlng"; // 출력 좌표 체계
		
		if(coordIdx == 1) {
			coordStr = "tm128";
		}
		
		/*
			step 1 : 주소정보 + 상세주소에 대한 첫번째 x, y 좌표를 구한다.
			step 2 : step 1 정보가 없을 경우 주소정보에 대한 첫번째 x, y 좌표를 구한다.
		*/
		try {
			if(StringUtils.isEmpty(addr1)) return null;
				
			String api = "http://openapi.map.naver.com/api/geocode.php";
			api += "?key=" + PropertiesUtil.getValue("naver_mapkey");
			api += "&encoding=utf-8&coord=" + coordStr + "&query=";
			
			String address1 = api + URLEncoder.encode(addr1 + " " + addr2, "UTF-8"); // 주소정보 + 상세주소
			String address2 = api + URLEncoder.encode(addr1, "UTF-8"); // 주소정보
			
			DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = bFactory.newDocumentBuilder();
			
			// step 1 - 주소정보 + 상세주소에 대한 첫번째 x, y 좌표를 구한다.
			Document document = docBuilder.parse(address1);
			Element rootEl = document.getDocumentElement();
			String total = rootEl.getElementsByTagName("total").item(0).getFirstChild().getNodeValue();
			
			if(Integer.parseInt(total) == 0) { // 검색된 좌표가 없을 경우
				// step 2 - 주소정보에 대한 첫번째 x, y 좌표를 구한다.
				document = docBuilder.parse(address2);
				rootEl = document.getDocumentElement();
			} 
			
			total = rootEl.getElementsByTagName("total").item(0).getFirstChild().getNodeValue();
			if(Integer.parseInt(total) > 0) { 
				coordArr = new String[2];
				coordArr[0] = rootEl.getElementsByTagName("x").item(0).getFirstChild().getNodeValue();
				coordArr[1] = rootEl.getElementsByTagName("y").item(0).getFirstChild().getNodeValue();
			}
		} catch (Exception e){
			e.printStackTrace();
			coordArr = null;
		}
		
		return coordArr;
	}
	
	/**
	 * 주소 정보에 대한 좌표 목록을 반환한다.
	 * @param addr1 주소정보 : 시도 + 구군 + 동
	 * @param addr2 상세주소 : 상세번지
	 * @param coordIdx 출력 좌표 체계 (0 : [latlng]위도/경도 좌표, 1 : [tm128]평면 좌표계. 종/횡 좌표)
	 * @return
	 */
	public static List getCoordList(String addr1, String addr2, int coordIdx) {
		List coordList = new ArrayList();
		Map coordMap = new HashMap(); // [address, x, y] node 정보를 저장
		String coordStr = "latlng"; // 출력 좌표 체계
		if(coordIdx == 1) {
			coordStr = "tm128";
		}
		
		/*
			step 1 : 주소정보 + 상세주소에 대한 좌표 목록을 구한다.
			step 2 : step 1 정보가 없을 경우 주소정보에 대한 좌표목록을 구한다.
		*/
		try {
			if(StringUtils.isEmpty(addr1)) return null;
				
			String api = "http://openapi.map.naver.com/api/geocode.php";
			api += "?key=" + PropertiesUtil.getValue("naver_mapkey");
			api += "&encoding=utf-8&coord=" + coordStr + "&query=";
			
			String address1 = api + URLEncoder.encode(addr1 + " " + addr2, "UTF-8"); // 주소정보 + 상세주소
			String address2 = api + URLEncoder.encode(addr1, "UTF-8"); // 주소정보
			
			DocumentBuilderFactory bFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = bFactory.newDocumentBuilder();
			
			// step 1 - 주소정보 + 상세주소 좌표 목록을 가져온다.
			Document document = docBuilder.parse(address1);
			Element rootEl = document.getDocumentElement();
			String total = rootEl.getElementsByTagName("total").item(0).getFirstChild().getNodeValue();
			
			if(Integer.parseInt(total) == 0) { // 검색된 좌표가 없을 경우
				// step 2 - 주소정보에 대한 좌표 목록을 가져온다.
				document = docBuilder.parse(address2);
				rootEl = document.getDocumentElement();
			}
			
			total = rootEl.getElementsByTagName("total").item(0).getFirstChild().getNodeValue();
			if(Integer.parseInt(total) > 0) {
				NodeList itemList = rootEl.getElementsByTagName("item"); // item list
				
				for(int i = 0; i < itemList.getLength(); i++) {
					NodeList itemChildList = itemList.item(i).getChildNodes(); // item node child list
					coordMap = new HashMap();
					
					for(int j = 0; j < itemChildList.getLength(); j++) {
						Node itemChildNode = itemChildList.item(j); // item 자식 노드
						
						if("#text".equals(itemChildNode.getNodeName())) { continue; }
						
						if("address".equals(itemChildNode.getNodeName())) { // address node 일 경우
							coordMap.put("address", itemChildNode.getFirstChild().getNodeValue());
						} else if("point".equals(itemChildNode.getNodeName())) { // point node 일 경우
							NodeList pointChildList = itemChildList.item(j).getChildNodes(); // point node child list
							for(int k = 0; k < pointChildList.getLength(); k++) {
								Node pointChildNode = pointChildList.item(k); // point 자식 노드명
								
								if("#text".equals(pointChildNode.getNodeName())) { continue; }
								
								if("x".equals(pointChildNode.getNodeName())) { // x node 일 경우
									coordMap.put("x", pointChildNode.getFirstChild().getNodeValue());
								} else if("y".equals(pointChildNode.getNodeName())) { // y node 일 경우
									coordMap.put("y", pointChildNode.getFirstChild().getNodeValue());
								}
							}
						}
					}
					coordList.add(coordMap);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			coordList = new ArrayList();
		}
		
		return coordList;
	}
	
	/**
	 * cookie 생성
	 * @param response
	 * @param name
	 * @param value
	 * @param iMinute
	 * @throws Exception
	 */
	public static void setCookie(HttpServletResponse response, String name,	String value, int iMinute) throws Exception {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60 * iMinute);
		cookie.setPath("/");
		cookie.setDomain(StringUtils.remove(PropertiesUtil.getValue("site_url"), "http://"));
		response.addCookie(cookie);
	}

	/**
	 * cookie 생성
	 * @param response
	 * @param name
	 * @param value
	 * @param maxAge
	 * @param path
	 * @throws Exception
	 */
	public static void setCookie(HttpServletResponse response, String name,	String value, int maxAge, String path) throws Exception {
		value = value == null ? null : java.net.URLEncoder.encode(value.toString(), "UTF-8");
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	/**
	 * cookie 생성
	 * @param response
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	public static void setCookie(javax.servlet.http.HttpServletResponse response, String name, String value) throws Exception {
		setCookie(response, name, value, 60 * 24 * 15);
	}

	/**
	 * cookie 값을 가져온다.
	 * @param request
	 * @param cookieName
	 * @return
	 * @throws Exception
	 */
	public static String getCookieValue(javax.servlet.http.HttpServletRequest request, String cookieName)throws Exception {
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			return "";
		}

		String value = "";
		for(int i = 0; i < cookies.length; i++) {
			if(cookieName.equals(cookies[i].getName())) {
				value = cookies[i].getValue();
				break;
			}
		}
		return value;
	}

	/**
	 * 전체 cookie 목록을 가져온다. 
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static List getCookies(javax.servlet.http.HttpServletRequest request)throws Exception {
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		List cookieList = new ArrayList();
		Map cookie = null;

		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
				cookie = new HashMap();
				cookie.put("NAME", URLDecoder.decode(cookies[i].getName(), "UTF-8"));
				cookie.put("VALUE",URLDecoder.decode(cookies[i].getValue(), "UTF-8"));
				cookieList.add(i, cookie);
			}
		}

		return cookieList;
	}
	
	/**
	 * md5 암호화
	 * @param value
	 * @return
	 */
	public static String makeMd5(String value) {
		try {
			StringBuilder sb = new StringBuilder();
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] passwordBytes = value.getBytes();
			byte[] encryptPassword;
			int length;

			digest.update(passwordBytes);
			encryptPassword = digest.digest();
			length = encryptPassword.length;

			for (int i = 0; i < length; i++) {
				sb.append(String.format("%02x", 0xff & (char)encryptPassword[i]));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * ibatis make Query
	 * @param value
	 * @return
	 */
	public static String makeSql(String prefix, String id) {
		return prefix + "." + id;
	}
}
