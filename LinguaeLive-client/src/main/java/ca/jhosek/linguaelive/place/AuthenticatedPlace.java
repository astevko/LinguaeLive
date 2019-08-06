/**
 * copyright (c) 2011 Andrew Stevko, all rights reserved
 */
package ca.jhosek.main.client.place;

import com.google.gwt.place.shared.Place;

/**
 * Places that inherit from this require the user to be logged in.
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
public abstract class AuthenticatedPlace extends Place {

    /**
     * is this Place class name  the same as that Place class name
     */
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		return (getClass() == obj.getClass());    	
    }	
    
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ this.getClass().getName().hashCode();
		return result;
	}

}
