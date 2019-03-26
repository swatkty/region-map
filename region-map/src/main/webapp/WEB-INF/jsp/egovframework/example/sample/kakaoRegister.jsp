<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width" />
<title>Login Demo - Kakao JavaScript SDK</title>
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery-1.4.2.min.js'/>" ></script>

</head>
<body>
	<a id="kakao-login-btn"></a>
	<a href="http://developers.kakao.com/logout"></a>
	<script type='text/javascript'>
		//<![CDATA[
		// 사용할 앱의 JavaScript 키를 설정해 주세요.
		Kakao.init('e67bcc3905abaf388d822fd806434b8b');
		// 카카오 로그인 버튼을 생성합니다.
		Kakao.Auth.createLoginButton({
			container : '#kakao-login-btn',
			success : function(authObj) {
				alert(JSON.stringify(authObj));
				$("#result").val(JSON.stringify(authObj));
				
			},
			fail : function(err) {
				alert(JSON.stringify(err));
			}
		});
		//]]>
	</script>
	<textarea id="result" style="width:500px;height:50px;"></textarea>
</body>
</html>
