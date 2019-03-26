package egovframework.example.util;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <p>
 * Title: 패스워드, 주민번호, 금융정보등의 암호화 지원
 * </p>
 * <p>
 * Description: 패스워드, 주빈번호, 금융정보등의 암호화 지원
 * </p>
 * <p>
 * 패스워드의 경우, 복호화 할 수 없는 단뱡향 암호화인 SHA-256
 * <P>
 * 주민번호, 금융정보와 같은 복호화가 필요한 경우에는 AES[Advanced Encryption Standard]-128을 이용한다.
 */
public class EncryptUtil {
	
	/**
	 * 입력받은 text 에 대한 AES-128 암호화(Encrypt)가 이뤄진 결과를 반환한다.
	 * AES-128 의 결과값은 원문이 16자리 이하이면 32자이나, 
	 * Base64 인코딩을 진행하면 24자리로 바뀐다.
	 * 원문이 16 ~ 32자리까지는 암호문은 44자리, 
	 * 원문이 32 ~ 48자리까지는 암호문은 64자리로 바뀐다.
	 * 
	 * 원문의 길이에 따라 데이터베이스 컬럼의 길이를 조절할 필요가 있다.
	 * 주민번호는 16자리이하이므로, 24자리로
	 * 
	 * @param 원문 text
	 * @return 암호화 된 text
	 */
	public static String Encrypt(String text) throws Exception {

		// 비밀키 값 - 복호화시의 값과 동일해야 한다.
		//String key = Conf.getString("com.encript.key"); // properties 파일같은 별도의 파일에 반드시 가져온다.
		String key = PropertiesUtil.getValue("com.encript.key"); // properties 파일같은 별도의 파일에 반드시 가져온다.

		if (text == null || "".equals(text))
			return "";

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length)
			len = keyBytes.length;

		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(results);
	}

	/**
	 * 입력받은 text 에 대한 AES-128 복호화(Decrypt)가 이뤄진 결과를 반환한다.
	 * 
	 * @param 암호화 된 text
	 * @return 복호화 된 text
	 */
	public static String Decrypt(String text) throws Exception {

		// 비밀키 값 - 암호화시의 값과 동일해야 한다.
		//String key = Conf.getString("com.encript.key"); // properties 파일같은 별도의 파일에 반드시 가져온다.
		String key = PropertiesUtil.getValue("com.encript.key");

		if (text == null || "".equals(text))
			return "";

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length)
			len = keyBytes.length;

		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		BASE64Decoder decoder = new BASE64Decoder();
		byte[] results = cipher.doFinal(decoder.decodeBuffer(text));
		return new String(results, "UTF-8");
	}

	/**
	 * 패스워드 암호화 - 단방향. 복호화 안됨.
	 * 입력받은 data 에 대한  SHA-256 암호화(Encrypt)가 이뤄진 결과를 반환한다.
	 * 암호화 된 결과는 62자리에서 원문에 따라 약간 더 늘어날 수 있다.
	 * 데이터베이스에 컬럼길이를 정할때는 80byte 정도로 약간의 여유를 두는 것이 요구된다.
	 * 
	 * @param 원문 text
	 * @return 암호화 된 text
	 */
	public String createHash(String data) throws Exception {

		if (data == null) {
			throw new NullPointerException();
		}

		// JDK 1.4.2 버전
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] raw = md.digest(data.getBytes("EUC-KR")); // 해당 charset에 맞게 변경
		StringBuffer result = new StringBuffer();
		
		for (int i = 0; i < raw.length; i++) {
			result.append(Integer.toHexString(raw[i] & 0xff));
		}
		return result.toString();
	}
}
