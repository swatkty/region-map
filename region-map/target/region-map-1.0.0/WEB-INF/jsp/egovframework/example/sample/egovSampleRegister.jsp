<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <c:set var="registerFlag" value="${empty sampleVO.id ? 'create' : 'modify'}"/>
    <title>Sample <c:if test="${registerFlag == 'create'}"><spring:message code="button.create" /></c:if>
                  <c:if test="${registerFlag == 'modify'}"><spring:message code="button.modify" /></c:if>
    </title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/sample.css'/>"/>
    
    <!--For Commons Validator Client Side-->
    <%-- <script type="text/javascript" src="<c:url value='/cmmn/validator.do'/>"></script> --%>
    <script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/fms/EgovMultiFile.js'/>" ></script>
    <script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery.js'/>" ></script>
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=e67bcc3905abaf388d822fd806434b8b"></script>
    <validator:javascript formName="sampleVO" staticJavascript="false" xhtml="true" cdata="false"/>
	<script type="text/javaScript" language="javascript" defer="defer">
	<c:if test="${result ne ''}">
		alert("<c:out value="${result}" />");
	</c:if>
	
	function fn_egov_save() {
		frm = document.detailForm;
		frm.action = "/changeFile.do";
		frm.submit();
	};
	
	$(document).ready(function(){
		//start map
		var container = document.getElementById('map');
		var options = {
			center: new daum.maps.LatLng(33.450701, 126.570667),
			level: 3
		};

		var map = new daum.maps.Map(container, options);
		//end map
	});
	</script>
</head>
<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;">
<p id="result"></p>
<form:form commandName="sampleVO" id="detailForm" name="detailForm" method="post">
    <div id="content_pop">
    	<!-- 타이틀 -->
    	<div id="title">
    		<ul>
    			<li><img src="<c:url value='/images/egovframework/example/title_dot.gif'/>" alt=""/>
                    <c:if test="${registerFlag == 'create'}"><spring:message code="button.create" /></c:if>
                    <c:if test="${registerFlag == 'modify'}"><spring:message code="button.modify" /></c:if>
                </li>
    		</ul>
    	</div>
    	<!-- // 타이틀 -->
    	<div id="table">
    	<table width="100%" border="1" cellpadding="0" cellspacing="0" style="bordercolor:#D3E2EC; bordercolordark:#FFFFFF; BORDER-TOP:#C2D0DB 2px solid; BORDER-LEFT:#ffffff 1px solid; BORDER-RIGHT:#ffffff 1px solid; BORDER-BOTTOM:#C2D0DB 1px solid; border-collapse: collapse;">
    		<colgroup>
    			<col width="150"/>
    			<col width="?"/>
    		</colgroup>
    		<c:if test="${registerFlag == 'modify'}">
        		<tr>
        			<td class="tbtd_caption"><label for="id"><spring:message code="title.sample.id" /></label></td>
        			<td class="tbtd_content">
        				<form:input path="id" cssClass="essentiality" maxlength="10" readonly="true" />
        			</td>
        		</tr>
    		</c:if>
    		<tr>
    			<td class="tbtd_caption"><label for="name"><spring:message code="title.attachefile" /></label></td>
    			<td class="tbtd_content">
    				<%-- <form:input path="file" maxlength="30" cssClass="txt"/> --%>
    				<input name="attacheFile" id="egovComFileUploader" type="file" cssClass="txt" />
    				&nbsp;<form:errors path="name" />
    			</td>
    		</tr>
    	</table>
      </div>
		<div id="sysbtn">
			<ul>
				<li>
	                <span class="btn_blue_l">
	                    <a href="javascript:fn_egov_save();">
	                        <c:if test="${registerFlag == 'create'}"><spring:message code="button.create" /></c:if>
	                    </a>
	                    <img src="<c:url value='/images/egovframework/example/btn_bg_r.gif'/>" style="margin-left:6px;" alt=""/>
	                </span>
	            </li>
	        </ul>
		</div>
    </div>
    <div id="map" style="width:70%;height:750px;"></div>
</form:form>
</body>
</html>