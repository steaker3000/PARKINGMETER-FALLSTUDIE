package ch.zhaw.pm.persistence.service.exception;

public class PersistenceException extends Exception {

	private static final long serialVersionUID = 6416438816426674727L;

	public PersistenceException(String exc) {
		super(exc);
	}

	public String getMessage() {
		return super.getMessage();
	}

}
