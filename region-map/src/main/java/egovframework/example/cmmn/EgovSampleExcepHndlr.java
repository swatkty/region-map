package egovframework.example.cmmn;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EgovSampleExcepHndlr implements ExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleExcepHndlr.class);

	@Override
	public void occur(Exception ex, String packageName) {
		LOGGER.debug(" EgovServiceExceptionHandler run...............");
	}
}
