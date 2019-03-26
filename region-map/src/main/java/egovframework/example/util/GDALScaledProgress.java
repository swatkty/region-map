package egovframework.example.util;

import org.gdal.gdal.ProgressCallback;

public class GDALScaledProgress extends ProgressCallback {
	private double pctMin;
	private double pctMax;
	private ProgressCallback mainCbk;

	public GDALScaledProgress(double pctMin, double pctMax, ProgressCallback mainCbk) {
		this.pctMin = pctMin;
		this.pctMax = pctMax;
		this.mainCbk = mainCbk;
	}

	public int run(double dfComplete, String message) {
		return this.mainCbk.run(this.pctMin + dfComplete * (this.pctMax - this.pctMin), message);
	}
}