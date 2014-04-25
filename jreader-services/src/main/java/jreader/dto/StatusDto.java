package jreader.dto;

public class StatusDto {
	
	private int errorCode;
	
	public StatusDto() {
		this(0);
	}
	
	public StatusDto(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
