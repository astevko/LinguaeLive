/**
 * com.ninuku.memories.server JettyHitLogger
 *
 *  copyright (c) 2010 Andrew Stevko
 */
package ca.jhosek.main.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;

import com.google.inject.Inject;


/**
 * hit logger for jetty
 * 
 * @author copyright (C) 2011 Andrew Stevko
 *
 */
@SuppressWarnings("deprecation")
public class HitLoggerFilter implements Filter {

	private Logger logger;

	/**
	 *
	 */
	@Inject
	public HitLoggerFilter( Logger logger ) {
		//
		this.logger = logger;
	}

	 private FilterConfig filterConfig = null;

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		//
		this.filterConfig  = null;
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		if( filterConfig == null )
			return;


		if( req instanceof HttpServletRequest ) {
			logger.info( "HttpServletRequest URL= " + HttpUtils.getRequestURL( (HttpServletRequest) req ) );

/*			HttpServletRequest hreq = (HttpServletRequest) req;
			Enumeration<String> headers = hreq.getHeaderNames();
			for(; headers.hasMoreElements(); ) {
				String header = headers.nextElement();
				Log.finer( " header: " + header + " = " + hreq.getHeader(header));
			}
*/		}

//		Log.info(  + " " + req.getProtocol() );
//		Log.info(  + " " + req.getServerName() );
//		Log.info(  + " " + req.getServerPort() );
//		Log.info(  + " " + req.getRealPath() );

/*		Enumeration<String> attrs = req.getAttributeNames();
		for(; attrs.hasMoreElements(); ) {
			String param = attrs.nextElement();
			Log.finer( " attribute: " + param + " = " + req.getAttribute(param));
		}
*/
/*		Enumeration<String> params = req.getParameterNames();
		for(; params.hasMoreElements(); ) {
			String param = params.nextElement();
			Log.finer( " parameter: " + param + " = " + req.getParameter(param));
		}
*/

		// pass on the hit..
		chain.doFilter(req, resp);
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		//
		this.filterConfig = filterConfig;
	}

}
