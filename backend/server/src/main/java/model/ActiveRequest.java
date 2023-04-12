package model;

public class ActiveRequest extends Request {

	private String imageFront;
	private String imageBack;
	
	
	public ActiveRequest() {
		super();
	}

	public ActiveRequest(Integer requestID, String targetID, Integer requestCode, String imageFront, String imageBack) {
		super(requestID, targetID, requestCode);
		this.imageFront = imageFront;
		this.imageBack = imageBack;
	}

	public String getImageFront() {
		return imageFront;
	}


	public void setImageFront(String imageFront) {
		this.imageFront = imageFront;
	}


	public String getImageBack() {
		return imageBack;
	}


	public void setImageBack(String imageBack) {
		this.imageBack = imageBack;
	}

	
	
}
