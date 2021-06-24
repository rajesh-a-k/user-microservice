package com.tm.user.response;

public class ErrorResponse {
	
	private String errorStatus;
	private String errorMessage;
	
	
	/**
	 * @param errorStatus
	 * @param errorMessage
	 */
	public ErrorResponse(String errorStatus, String errorMessage) {
		this.errorStatus = errorStatus;
		this.errorMessage = errorMessage;
	}
	
	/**
	 * @return the errorStatus
	 */
	public String getErrorStatus() {
		return errorStatus;
	}
	/**
	 * @param errorStatus the errorStatus to set
	 */
	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
