package egovframework.example.sample.service.impl;

import java.util.HashMap;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sampleMapper")
public interface SampleMapper {

	String selectConnectingConfirm() throws Exception;
	
	String insertCenterXY(HashMap<String, String> hashMap) throws Exception;
}
