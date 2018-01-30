package fantasymanager.exceptions;

public class FantasyManagerException extends Exception
{
	private static final long serialVersionUID = 7913816137850682813L;

	public FantasyManagerException(String mensaje)
	{
		super(mensaje);
	}
	
	public FantasyManagerException(Exception e)
	{
		super(e);
	}
	
	public FantasyManagerException(String mensaje, Throwable causa)
	{
		super(mensaje, causa);
	}
}
