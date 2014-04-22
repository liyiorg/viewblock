package viewblock.exception;

public class ViewBlockRequiredParameter extends ViewBlockException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4227388327681092715L;
	
	private String paramName;

	public ViewBlockRequiredParameter(String message) {
		super(message);
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	
}
