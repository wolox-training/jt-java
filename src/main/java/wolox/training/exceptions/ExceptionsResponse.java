package wolox.training.exceptions;

import java.sql.Timestamp;

public class ExceptionsResponse {

	private String origin;
	private Timestamp timestamp;
	private String error;

	public ExceptionsResponse(String error) {
		this.error = error;
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

	public ExceptionsResponse(String origin, String error) {
		this.origin = origin;
		this.error = error;
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
}
