package egovframework.example.cmmn;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EgovSampleOthersExcepHndlr implements ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleOthersExcepHndlr.class);

	@Override
	public void occur(Exception exception, String packageName) {
		LOGGER.debug(" EgovServiceExceptionHandler run...............");
	}

}
