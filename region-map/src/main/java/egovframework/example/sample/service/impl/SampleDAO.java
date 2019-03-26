package egovframework.example.sample.service.impl;

import egovframework.example.sample.service.RegionCodeVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository("sampleDAO")
public class SampleDAO extends EgovAbstractDAO {

	
	public String selectConnectingConfirm() {
		return (String) select("sampleDAO.selectConnectingConfirm");
	}

	public int updateCenterXY(RegionCodeVO vo) {
		return update("sampleDAO.updateCenterXY", vo);
	}

	public List<?> selectRegionCodeList() {
		return list("sampleDAO.selectRegionCodeList");
	}

}
