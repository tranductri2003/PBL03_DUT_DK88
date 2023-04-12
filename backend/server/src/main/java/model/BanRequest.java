package model;

import java.util.List;

public class BanRequest extends Request {

	private String moreDetail;
	private List<String> imageProof;
	
	public BanRequest() {
		super();
	}

	public BanRequest(Integer requestID, String targetID, Integer requestCode, String moreDetail, List<String> imgs) {
		super(requestID, targetID, requestCode);
		this.moreDetail = moreDetail;
		this.imageProof = imgs;
	}

	public String getMoreDetail() {
		return moreDetail;
	}

	public void setMoreDetail(String moreDetail) {
		this.moreDetail = moreDetail;
	}

	public List<String> getImageProof() {
		return imageProof;
	}

	public void setImageProof(List<String> imageProof) {
		this.imageProof = imageProof;
	}
	
	
}
