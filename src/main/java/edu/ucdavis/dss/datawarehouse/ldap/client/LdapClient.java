package edu.ucdavis.dss.datawarehouse.ldap.client;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultTlsDirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;

public class LdapClient {
	static private Logger logger = LoggerFactory.getLogger("LdapClient");
	private LdapTemplate ldapTemplate = null;
	private String serverUrl, serverBase, serverUser, serverPassword;
	
	public LdapClient(String serverUrl, String serverBase, String serverUser, String serverPassword) {
		this.serverUrl = serverUrl;
		this.serverBase = serverBase;
		this.serverUser = serverUser;
		this.serverPassword = serverPassword;
	}

	public List<String> fetchAllUcdPersonUUIDs() {
		String[] attrsToReturn = {"ucdPersonUUID"};
		
		bind();
		
		int i = 0;
		int inc = 2000; // limit set at ldap.ucdavis.edu
		
		List<String> allUcdPersonUUIDs = new ArrayList<String>();

		// Max known ucdPersonUUID as of 1/5/2017 = 01364314
		// Found using LDAP search ucdPersonUUID >= x
		int maxUuid = 1500000;
		for(i = 0; i < maxUuid; i += inc) {
			allUcdPersonUUIDs.addAll(ldapTemplate.search("",
					"(&(ucdPersonUUID<=" + (i + inc) + ")(ucdPersonUUID>=" + i + "))",
					SearchControls.SUBTREE_SCOPE,
					attrsToReturn,
					new AttributeMapper()));
			
			if(i % 10000 == 0) {
				logger.debug("i = " + i + ", pct complete = " + String.format("%.5f", ((float)i / (float)maxUuid) * 100.0) + ", allUcdPersonUUIDs.size() = " + allUcdPersonUUIDs.size());
			}
		}
		
		return allUcdPersonUUIDs;
	}

	private boolean bind() {
		if(ldapTemplate != null) { return true; }
		
		LdapContextSource ctxSrc = new LdapContextSource();
		
		ctxSrc.setUrl(serverUrl);
		ctxSrc.setBase(serverBase);
		ctxSrc.setUserDn(serverUser);
		ctxSrc.setPassword(serverPassword);
		
		ctxSrc.setAuthenticationStrategy(new DefaultTlsDirContextAuthenticationStrategy());
		
		ctxSrc.afterPropertiesSet();

		ldapTemplate = new LdapTemplate(ctxSrc);
		
		return true;
	}
	
	private class AttributeMapper implements AttributesMapper<String> {

		@Override
		public String mapFromAttributes(Attributes attributes) throws NamingException {
			Attribute personId = attributes.get("ucdPersonUUID");
			
			return (String)personId.get();
		}
		
	}
}
