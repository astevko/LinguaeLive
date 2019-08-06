package ca.jhosek.main.server.domain;


import com.google.web.bindery.requestfactory.shared.Locator;
import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Generic @Locator for objects that extend DatastoreObject
 * 
 * @author turbomanage
 */
public class ObjectifyLocator extends Locator<DatastoreObject	, Long>
{

	@Override
	public DatastoreObject create(Class<? extends DatastoreObject> clazz)
	{
		try
		{
			return clazz.newInstance();
		} catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DatastoreObject find(Class<? extends DatastoreObject> clazz, Long id)
	{
		return ofy().load().type(clazz).id(id).now();
	}

	@Override
	public Class<DatastoreObject> getDomainType()
	{
		// Never called
		return null;
	}

	@Override
	public Long getId(DatastoreObject domainObject)
	{
		return domainObject.getId();
	}

	@Override
	public Class<Long> getIdType()
	{
		return Long.class;
	}

	@Override
	public Object getVersion(DatastoreObject domainObject)
	{
		return domainObject.getVersion();
	}

}
