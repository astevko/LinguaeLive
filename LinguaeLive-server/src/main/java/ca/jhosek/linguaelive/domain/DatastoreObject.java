package ca.jhosek.main.server.domain;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;

abstract public class DatastoreObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	private Integer version = 0;
	
	/**
	 * used to update the version in Objectify so that RequestFactory can handle the object
	 * as per http://nimbustecnologia.blogspot.com/2011/01/requestfactory-objectify.html
	 */
	final void onPersist()
	{
		this.version++;
	}

	final public Long getId()
	{
		return id;
	}

	final public void setId(Long id)
	{
		this.id = id;
	}

	final public Integer getVersion()
	{
		return version;
	}

	final public void setVersion(Integer version)
	{
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatastoreObject other = (DatastoreObject) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
//		if (version == null) {
//			if (other.version != null)
//				return false;
//		} else if (!version.equals(other.version))
//			return false;
		return true;
	}
}
