package tool;



import java.util.UUID;

public class GUIDUtil
{
	/**
	 * To suppress default construction for noninstantiability
	 */
	private GUIDUtil()
	{
		
	}
	public static String getGUIDString()
	{
		UUID uuid = UUID.randomUUID();
		return "open-"+uuid.toString();
	}

}
