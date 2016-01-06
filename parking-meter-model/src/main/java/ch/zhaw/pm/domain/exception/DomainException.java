package ch.zhaw.pm.domain.exception;

public class DomainException extends Exception {

	private static final long serialVersionUID = 6416433716426674337L;
	private ErrCode errCode = ErrCode.ERROR;

	public DomainException(String exc) {
		super(exc);
	}

	public DomainException(ErrCode errCode) {
		super(errCode.getMsg());
		this.errCode = errCode;
	}

	public String getMessage() {
		return super.getMessage();
	}

	public ErrCode getErrCode() {
		return this.errCode;
	}

}
