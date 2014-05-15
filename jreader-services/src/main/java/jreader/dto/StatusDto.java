package jreader.dto;

public class StatusDto {
	
	private int errorCode;
	private String errorMessage;
	
	public StatusDto() {
		this(0);
	}

	public StatusDto(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public StatusDto(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

}
