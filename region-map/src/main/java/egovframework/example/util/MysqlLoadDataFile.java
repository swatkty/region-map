package egovframework.example.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;

public class MysqlLoadDataFile {

	protected Connection conn;

	public String LoadDataInfileExcute(ParamMap paramMap) throws Exception {

		System.out.println("LoadDataInfileExcute paramMap ===>>>" + paramMap.toString());
		
		String url = paramMap.getString("url");
		String user = paramMap.getString("user");
		String password = paramMap.getString("password");
		String tableName = paramMap.getString("tableName");
		String regionCode = paramMap.getString("regionCode");
		String changeFilePath = paramMap.getString("changeFilePath");
		
		tableName = tableName + "_" + regionCode + "_" + DateUtil.getToday("yyyy_MM_dd");
		System.out.println("LoadDataInfileExcute tableName===>>>" + tableName);
		
		String driver = "com.mysql.jdbc.Driver";
		Connection conn = null;
		Statement stmt = null;
		int result = 0;

		try {
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			
			File file = new File(paramMap.getString("changeFilePath"));
			System.out.println("file.exists()=========>>>" + file.exists());
			
			if (file.exists()) {
				
				System.out.println("==============sql execute============");
				String sql = "";
				sql += "load data infile ";
				sql += "'" + StringUtils.replace(changeFilePath, "\\", "/") +"' ";
				sql += "into table " + tableName + " ";
				sql += "character set 'euckr' ";
				sql += "fields terminated by ',' ";
				sql += "enclosed by '\"' ";
				sql += "lines terminated by '\\n' ";//\r\n
				sql += "ignore 1 lines (wkt, pnu, jibun, bchk, sgg_oid, col_adm_se) SET sido = left(pnu, 2) ";
				stmt = conn.createStatement();
				
				System.out.println("sql==>>>" + sql);
				result = stmt.executeUpdate(sql);
				
			} else {
				return "FAIL";
			}

		} catch (Exception e) {
			System.out.println("e: " + e);
			return "FAIL";

		} finally {
			conn.close();
			stmt.close();
		}
		return "SUCCESS : " + result;
	}

}