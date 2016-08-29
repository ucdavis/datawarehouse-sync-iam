package edu.ucdavis.dss.iam.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ucdavis.dss.iam.dtos.IamAssociation;
import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamDepartment;
import edu.ucdavis.dss.iam.dtos.IamPerson;
import edu.ucdavis.dss.iam.dtos.IamPrikerbacct;

public class IamClient {
	private final Logger log = LoggerFactory.getLogger("IamLogger");

	private CloseableHttpClient httpclient;
	private HttpHost targetHost;
	private HttpClientContext context;
	private String apiKey;

	public IamClient(String apiKey) {
		httpclient = HttpClientBuilder.create().build();
		targetHost = new HttpHost("iet-ws.ucdavis.edu", 443, "https");

		// Add AuthCache to the execution context
		context = HttpClientContext.create();
		
		this.apiKey = apiKey;
	}

	/**
	 * Returns a list of all departments
	 * 
	 * @return list of departments as IamDepartment DTOs
	 */
	public List<IamDepartment> getAllDepartments() {
		HttpGet httpget = new HttpGet("/api/iam/orginfo/pps/depts?v=1.0&key=" + apiKey);
		List<IamDepartment> departments = null;

		try {
			CloseableHttpResponse response = httpclient.execute(
					targetHost, httpget, context);

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findPath("results");
			
			if ((arrNode != null) && (arrNode.isNull() == false)) {
				departments = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamDepartment.class));
			} else {
				log.error("getAllDepartments response from IAM not understood or was empty/null");
			}

			response.close();
		} catch (IOException e) {
			log.error(exceptionStacktraceToString(e));
		}
		
		return departments;
	}
	
	/**
	 * Returns a list of all associations for the department indicated by 'deptCode'
	 * 
	 * @return list of associations as IamAssociation DTOs
	 */
	public List<IamAssociation> getAllAssociationsForDepartment(String deptCode) {
		long startTime;
		
		// First, get all people in the department
		String url = "/api/iam/associations/pps/search?deptCode=" + deptCode;
		HttpGet httpget = new HttpGet(url + "&v=1.0&key=" + apiKey);
		List<IamAssociation> associations = null;

		try {
			log.debug("HTTP GET: " + url);
			startTime = new Date().getTime();
			CloseableHttpResponse response = httpclient.execute(
					targetHost, httpget, context);
			log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");
			
			if ((arrNode != null) && (arrNode.isNull() == false)) {
				associations = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamAssociation.class));
			} else {
				log.warn("/api/iam/associations/pps/search?deptCode=" + deptCode + " response from IAM not understood or was empty/null");
				
				return null;
			}

			response.close();
		} catch (IOException e) {
			log.error(exceptionStacktraceToString(e));
			return null;
		}
		
		return associations;
		
//		// Perform four additional queries to obtain the rest of this person's information
//		for(IamPerson person : people) {
//			// Augment IamPerson with information from /people/associations/pps ...
//			try {
//				url = "/api/iam/associations/pps/" + person.getIamId();
//				log.debug("HTTP GET: " + url);
//				startTime = new Date().getTime();
//				httpget = new HttpGet(url + "?key=" + apiKey + "&v=1.0");
//				log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");
//				
//				CloseableHttpResponse response = httpclient.execute(
//						targetHost, httpget, context);
//
//				HttpEntity entity = response.getEntity();
//
//				ObjectMapper mapper = new ObjectMapper();
//				
//				JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
//				JsonNode arrNode = rootNode.findParent("responseData");
//				arrNode = rootNode.findPath("results");
//				
//				if ((arrNode != null) && (arrNode.isNull() == false)) {
//					Set<IamAssociation> associations = null;
//					
//					associations = mapper.readValue(
//							arrNode.toString(),
//							mapper.getTypeFactory().constructCollectionType(
//									List.class, IamAssociation.class));
//					
//					// We'll set up a blank list just in case the client wants to call .size(), etc.
//					if(associations == null) associations = new HashSet<IamAssociation>();
//					
//					person.setAssociations(associations);
//					
//					for(IamAssociation association : associations) {
//						log.debug("Position type: " + association.getPositionType() + "(code: " + association.getPositionTypeCode() + ")");
//					}
//				} else {
//					log.warn("getAllPeopleByDepartmentCode /api/iam/associations/pps/" + person.getIamId() + " response from IAM not understood or was empty/null");
//					
//					continue;
//				}
//
//				response.close();
//			} catch (IOException e) {
//				log.error(exceptionStacktraceToString(e));
//				continue;
//			}
		//}
		
		//return people;
	}
	
	/**
	 * Returns the contact info entry for a given IamID
	 * 
	 * @return List<IamContactInfo> or null
	 */
	public List<IamContactInfo> getContactInfo(Long iamId) {
		long startTime;
		
		// First, get all people in the department
		String url = "/api/iam/people/contactinfo/" + iamId;
		HttpGet httpget = new HttpGet(url + "?v=1.0&key=" + apiKey);
		List<IamContactInfo> contactInfos = null;

		try {
			log.debug("HTTP GET: " + url);
			startTime = new Date().getTime();
			CloseableHttpResponse response = httpclient.execute(
					targetHost, httpget, context);
			log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");
			
			if ((arrNode != null) && (arrNode.isNull() == false)) {
				contactInfos = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamContactInfo.class));
			} else {
				log.warn("/api/iam/people/contactinfo/" + iamId + " response from IAM not understood or was empty/null");
				
				return null;
			}

			response.close();
		} catch (IOException e) {
			log.error(exceptionStacktraceToString(e));
			return null;
		}
		
		return contactInfos;
	}

	/**
	 * Returns the person entry(ies) for a given IamID
	 * 
	 * @return List<IamPerson> or null
	 */
	public List<IamPerson> getPersonInfo(Long iamId) {
		long startTime;
		
		// First, get all people in the department
		String url = "/api/iam/people/search?iamId=" + iamId;
		HttpGet httpget = new HttpGet(url + "&v=1.0&key=" + apiKey);
		List<IamPerson> people = null;

		try {
			log.debug("HTTP GET: " + url);
			startTime = new Date().getTime();
			CloseableHttpResponse response = httpclient.execute(
					targetHost, httpget, context);
			log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");
			
			if ((arrNode != null) && (arrNode.isNull() == false)) {
				people = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamPerson.class));
			} else {
				log.warn("/api/iam/people/search?iamId=" + iamId + " response from IAM not understood or was empty/null");
				
				return null;
			}

			response.close();
		} catch (IOException e) {
			log.error(exceptionStacktraceToString(e));
			return null;
		}
		
		return people;
	}

	/**
	 * Returns the person entry(ies) for a given IamID
	 * 
	 * @return List<IamPerson> or null
	 */
	public List<IamPrikerbacct> getPrikerbacct(Long iamId) {
		long startTime;
		
		// First, get all people in the department
		String url = "/api/iam/people/prikerbacct/" + iamId;
		HttpGet httpget = new HttpGet(url + "?v=1.0&key=" + apiKey);
		List<IamPrikerbacct> prikerbaccts = null;

		try {
			log.debug("HTTP GET: " + url);
			startTime = new Date().getTime();
			CloseableHttpResponse response = httpclient.execute(
					targetHost, httpget, context);
			log.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");
			
			if ((arrNode != null) && (arrNode.isNull() == false)) {
				prikerbaccts = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamPrikerbacct.class));
			} else {
				log.warn("/api/iam/people/prikerbacct/" + iamId + " response from IAM not understood or was empty/null");
				
				return null;
			}

			response.close();
		} catch (IOException e) {
			log.error(exceptionStacktraceToString(e));
			return null;
		}
		
		return prikerbaccts;
	}
	
	// Credit: http://stackoverflow.com/questions/10120709/difference-between-printstacktrace-and-tostring
	private static String exceptionStacktraceToString(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}
}
