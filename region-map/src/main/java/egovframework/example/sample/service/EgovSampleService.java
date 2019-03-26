package egovframework.example.sample.service;

import java.util.List;

public interface EgovSampleService {

	String selectConnectingConfirm() throws Exception;

	int updateCenterXY(RegionCodeVO vo) throws Exception;

	List<?> selectRegionCodeList() throws Exception;

}
