package egovframework.example.sample.service.impl;

import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.RegionCodeVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("sampleService")
public class EgovSampleServiceImpl extends EgovAbstractServiceImpl implements EgovSampleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);

	/** SampleDAO */
	// TODO ibatis 사용
	@Resource(name = "sampleDAO")
	private SampleDAO sampleDAO;
	// TODO mybatis 사용
	//  @Resource(name="sampleMapper")
	//	private SampleMapper sampleDAO;

	/** ID Generation */
	@Resource(name = "egovIdGnrService")
	private EgovIdGnrService egovIdGnrService;

	@Override
	public String selectConnectingConfirm() throws Exception {
		return sampleDAO.selectConnectingConfirm();
	}

	@Override
	public int updateCenterXY(RegionCodeVO vo) throws Exception {
		return sampleDAO.updateCenterXY(vo);
	}

	@Override
	public List<?> selectRegionCodeList() throws Exception {
		return sampleDAO.selectRegionCodeList();
	}

}
