package Exceptions;

import java.io.IOException;

public class CriticalStatusException extends RuntimeException {

	public CriticalStatusException() {
		super("Un robot è in condizioni critiche");
	}
	
	public CriticalStatusException(String msg) {
		super(msg);
	}
}
