<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Daum 지도 시작하기</title>
	<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery.js'/>" ></script>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=e67bcc3905abaf388d822fd806434b8b"></script>
	<script type="text/javascript">
	$(document).ready(function(){
		var container = document.getElementById("map");
		var options = {
			center: new daum.maps.LatLng(33.450701, 126.570667),
			level: 3
		};

		var map = new daum.maps.Map(container, options);

		//var url = encodeURI("query=" + $("#query").val() + "&KakaoAK=" + $("#KakaoAK").val());
		/* 
		$.ajax({
		    url: 'https://dapi.kakao.com/v2/local/search/address.json?query=경기 수원시 팔달구 인계동' ,
		    //headers: { 'Authorization': 'KakaoAK b9a77da028e3c665f20c3f95b7cb7aee' },
		    contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			dataType : 'text',
		    beforeSend : function(xhr) {
		    	xhr.setRequestHeader('Authorization', 'KakaoAK b9a77da028e3c665f20c3f95b7cb7aee')
		    },
		    type: 'GET'
		}).done(function(data) {
		    console.log(data);
		    alert(JSON.stringify(data));
		    $("#result").val(JSON.stringify(data));
		});
		*/
		
		$.ajax({
			url : "https://dapi.kakao.com/v2/local/search/address.json" ,
			headers : { "Authorization" : "KakaoAK b9a77da028e3c665f20c3f95b7cb7aee" },
			//beforeSend : function(xhr) {
		    //	xhr.setRequestHeader("Authorization", "KakaoAK b9a77da028e3c665f20c3f95b7cb7aee")
		    //},
			//contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			//dataType : "text",
			type : "GET",
			data : {"query" : "경기 수원시 팔달구 인계동"},
			success : function(data) {
				try {
					//alert("success");
					console.log(data);
				    alert(JSON.stringify(data));
				    $("#result").val(JSON.stringify(data));
				} catch(ie) {
					alert(ie.description);
				}
			},
			error : function(request, status, error) {
				try {
					alert("code : "+request.status+"\n" + "message : "+request.responseText+"\n" + "error : "+error);
				} catch(ie) {
					alert(ie.description);
				}
			}
			});
	});
	</script>
</head>
<body>
	<form id="frm" name="frm">
	</form>
	<textarea id="result" name="result" style="width:100%;"></textarea>
	<br/><p/>
	<div id="map" style="width:100%;height:800px;"></div>
</body>
</html>