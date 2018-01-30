package fantasymanager.exceptions;

public class FantasyManagerParserException extends FantasyManagerException
{
	private static final long serialVersionUID = -6992216142699851061L;

	public FantasyManagerParserException(String mensaje)
	{
		super(mensaje);
	}
	
	public FantasyManagerParserException(Exception e)
	{
		super(e);
	}
	
	public FantasyManagerParserException(String mensaje, Throwable causa)
	{
		super(mensaje, causa);
	}
}
