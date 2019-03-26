package egovframework.example.sample.web;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.RegionCodeVO;
import egovframework.example.sample.service.SampleVO;

import egovframework.example.util.MysqlLoadDataFile;
import egovframework.example.util.Ogr2Ogr;
import egovframework.example.util.ParamMap;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springmodules.validation.commons.DefaultBeanValidator;

@Controller
public class EgovSampleController {

	/** EgovSampleService */
	@Resource(name = "sampleService")
	private EgovSampleService sampleService;

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	static boolean bSkipFailures = false;
	static int nGroupTransactions = 200;
	static boolean bPreserveFID = false;
	static final int OGRNullFID = -1;
	static int nFIDToFetch = OGRNullFID;
	
	@RequestMapping(value = "/addSample.do", method = RequestMethod.GET)
	public String addSampleView(@ModelAttribute("searchVO") SampleVO sampleVO, ModelMap model) throws Exception {
		model.addAttribute("sampleVO", new SampleVO());
		System.out.println("=====================addSample=====================");
		//mysql Connection Test
		//String confirm = sampleService.selectConnectingConfirm();
		//System.out.println("====lsmd_cont_ldreg table total cnt====>>>" + confirm);
		
		model.addAttribute("result", "");
		return "sample/egovSampleRegister";
	}
	
	@RequestMapping(value = "/changeFile.do", method = RequestMethod.POST)
	public String updateSampleView(HttpServletRequest request, ModelMap model) throws Exception {

		ParamMap paramMap = new ParamMap();
		paramMap.parseRequest(request);
		
		System.out.println("paramMap.toString() ===>>>>>" + paramMap.toString());
		
		String filePath = propertiesService.getString("file.path");
		int underlineCnt = Integer.parseInt(propertiesService.getString("file.path.underline"));
		
		paramMap.put("url" , propertiesService.getString("db.url"));
		paramMap.put("user" , propertiesService.getString("db.user"));
		paramMap.put("password" , propertiesService.getString("db.password"));
		paramMap.put("tableName" , propertiesService.getString("db.tableName"));
		paramMap.put("underline" , propertiesService.getString("file.path.underline"));
		paramMap.put("filePath" , filePath);
		
		String localFilePath = filePath + paramMap.getString("attacheFile");
		System.out.println("localFilePath====>>>" + localFilePath);
		
		String[] arrFilePath = StringUtils.split(localFilePath, ".");
		System.out.println("arrFilePath====>>>" + arrFilePath[0]);
		
		String[] arrGubun = StringUtils.split(localFilePath, "_");
		System.out.println("arrGubun====>>>>" + arrGubun[underlineCnt]);
		
		String regionCode = StringUtils.defaultString(arrGubun[underlineCnt], "");
		System.out.println("regionCode=======>>>>" + regionCode);
		paramMap.put("regionCode", regionCode);
		
		String changeFilePath = arrFilePath[0] + ".csv";
		paramMap.put("changeFilePath", changeFilePath);
		
		System.out.println("changeFilePath====>>>" + changeFilePath);
		
		File file = new File(localFilePath);
		System.out.println("file localFilePath===>>>" + file.exists());
		System.out.println("java.library.path :::" + System.getProperty("java.library.path"));
		
		String result = "";
		
		try {
			String[] args = {"-f", "CSV", "-lco", "GEOMETRY=AS_WKT", "-s_srs", "EPSG:5174", "-t_srs", "EPSG:4326", "-simplify", "0.0001", changeFilePath, localFilePath};
			
			/*
			MysqlLoadDataFile mysqlLoadDataFile = new MysqlLoadDataFile();
			result = mysqlLoadDataFile.LoadDataInfileExcute(paramMap);
			System.out.println("Result => " + result);
			*/
			
			if("SUCCESS".equals(Ogr2Ogr.main(args))) {
				MysqlLoadDataFile mysqlLoadDataFile = new MysqlLoadDataFile();
				result = mysqlLoadDataFile.LoadDataInfileExcute(paramMap);
				System.out.println("Result => " + result);
			} else {
				System.out.println("FAIL : file.shp to file.csv");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("result", result);
		//return "redirect:/addSample.do";
		//return "forward:/addSample.do";
		return "sample/egovSampleRegister";
	}
	
	
	@RequestMapping(value = "/kakao.do", method = RequestMethod.GET)
	public String kakaoRegister(@ModelAttribute("searchVO") SampleVO sampleVO, ModelMap model) throws Exception {
		model.addAttribute("sampleVO", new SampleVO());
		System.out.println("==========================================");
		//mysql Connection Test
		//String confirm = sampleService.selectConnectingConfirm();
		//System.out.println("====lsmd_cont_ldreg table total cnt====>>>" + confirm);
		String kakaoRestapi = propertiesService.getString("kakao.restapi");
		String kakaojavascript = propertiesService.getString("kakao.javascript");
		System.out.println("kakao.restapi ===>>>" + kakaoRestapi);
		System.out.println("kakao.javascript ===>>>" + kakaojavascript);
		model.addAttribute("result", "");
		return "sample/kakaoRegister";
	}
	
	@RequestMapping(value = "/kakaoMap.do", method = RequestMethod.GET)
	public String kakaoMap(@ModelAttribute("searchVO") SampleVO sampleVO, ModelMap model) throws Exception {
		model.addAttribute("sampleVO", new SampleVO());
		System.out.println("=====================kakaoMap=====================");
		return "sample/kakaoMap";
	}
	
	@RequestMapping(value = "/naverMap.do", method = RequestMethod.GET)
	public String naverMap(@ModelAttribute("searchVO") SampleVO sampleVO, ModelMap model) throws Exception {
		model.addAttribute("sampleVO", new SampleVO());
		System.out.println("=====================naverMap=====================");
		return "sample/naverMap";
	}
	
	@RequestMapping(value = "/getCenterXY.do", method = RequestMethod.GET)
	public String getCenterXY(@ModelAttribute("searchVO") SampleVO sampleVO, ModelMap model) throws Exception {
        
        try {
        	List<?> list = sampleService.selectRegionCodeList();
        	System.out.println("region_code lat 0, lng 0 list size=======>>>>" + list.size());
        	
        	for(int i = 0; i < list.size(); i++) {
        		
        		EgovMap map =  (EgovMap) list.get(i);
        		System.out.println("region_code_name =======>>>>" + map.get("regionCodeName"));
        		
        		String query = URLEncoder.encode(map.get("regionCodeName").toString(), "UTF-8");
        		String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + query;
        		
        		getRegionAddress(getJSONData(url) , map);
        	}
        }catch(Exception e){
            e.printStackTrace();
        }

		model.addAttribute("sampleVO", new SampleVO());
		return "sample/kakaoMap";
	}
	
	private String getJSONData(String apiUrl) throws Exception {
		
		String kakaoRestapi = propertiesService.getString("kakao.restapi");
        String jsonString = new String();
        
        String buf;
        URL url = new URL(apiUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        
        String auth = "KakaoAK " + kakaoRestapi;
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-Requested-With", "curl");
        conn.setRequestProperty("Authorization", auth);
        
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        while ((buf = br.readLine()) != null) {
            jsonString += buf;
        }
        
        return jsonString;
    }
	
	private String getRegionAddress(String jsonString, EgovMap map) throws Exception {
		
        String address = "";
        String lat = "";//위도
        String lng = "";//경도
        //System.out.println("getRegionAddress===>>>>" + jsonString);
        JSONObject jObj = new JSONObject(jsonString);
        
        JSONObject meta = (JSONObject) jObj.get("meta");
        int totalCount = meta.getInt("total_count");
        
        if(totalCount > 0){
            JSONArray jArray = (JSONArray) jObj.get("documents");
            JSONObject subJobj = (JSONObject) jArray.get(0);
    		JSONObject subsubJobj = (JSONObject) subJobj.get("address");
    		address = (String) subsubJobj.get("address_name");
    		lng = subsubJobj.getString("x");
    		lat = subsubJobj.getString("y");
    		
    		if(address.equals("") || address == null){
	    		subJobj = (JSONObject) jArray.get(1);
	    		subJobj = (JSONObject) subJobj.get("address");
	    		address =(String) subJobj.get("address_name");
	    		lat = subJobj.getString("y");
	    		lng = subJobj.getString("x");
        	}
    		RegionCodeVO vo = new RegionCodeVO();
    		vo.setLat(lat);
    		vo.setLng(lng);
    		vo.setRegionCode(map.get("regionCode").toString());
    		int confirm = sampleService.updateCenterXY(vo);
    		
        }
        System.out.println("regionCode : " + map.get("regionCode").toString() + " address : " + address + " lng:" + lng + " lat:" + lat);
        return address;
    }


}
