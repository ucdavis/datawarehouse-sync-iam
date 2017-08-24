package edu.ucdavis.dss.iam.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.ucdavis.dss.iam.dtos.IamAssociation;
import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamDepartment;
import edu.ucdavis.dss.iam.dtos.IamPerson;
import edu.ucdavis.dss.iam.dtos.IamPrikerbacct;

public class IamClient {
	private final Logger logger = LoggerFactory.getLogger("IamLogger");
	private final int TIMEOUT = 30000; // milliseconds
	private final int MAX_RETRIES = 3;

	private CloseableHttpClient httpclient;
	private HttpHost targetHost;
	private HttpClientContext context;
	private String apiKey;

	public IamClient(String apiKey) {
		httpclient = HttpClientBuilder.create().setRetryHandler(retryHandler).build();

		targetHost = new HttpHost("iet-ws.ucdavis.edu", 443, "https");

		// Add AuthCache to the execution context
		context = HttpClientContext.create();

		this.apiKey = apiKey;
	}

	/**
	 * Returns the IAM ID for the given Mothra ID.
	 *
	 * Note: Mothra ID is called UcdPersonUUID in LDAP.
	 * 
	 * @param mothraId
	 * @return
	 */
	public Long getIamIdFromMothraId(String mothraId) {
		HttpGet request = new HttpGet("/api/iam/people/ids/search?mothraId=" + mothraId + "&v=1.0&key=" + apiKey);
		Long iamId = null;

		try {
			HashMap<Object, Object> results = null;

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();
			request.setConfig(requestConfig);

			CloseableHttpResponse response = httpclient.execute(
					targetHost, request, context);

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findPath("results").get(0);

			if ((arrNode != null) && (arrNode.isNull() == false)) {
				results = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructMapType(
								HashMap.class, Object.class, Object.class));

				iamId = Long.parseLong((String) results.get("iamId"));
			} else {
				// Mothra IDs may exist for individuals who are not active and don't show up in IAM.
				// This is not an error, nor warning.
				//log.error("getIamIdFromMothraId response from IAM not understood or was empty/null");
			}

			response.close();
		} catch (HttpHostConnectException e) {

		} catch (IOException e) {
			logger.error(exceptionStacktraceToString(e));
		}

		return iamId;
	}

	/**
	 * Returns a list of all departments
	 * 
	 * @return list of departments as IamDepartment DTOs
	 */
	public List<IamDepartment> getAllDepartments() {
		HttpGet request = new HttpGet("/api/iam/orginfo/pps/depts?v=1.0&key=" + apiKey);
		List<IamDepartment> departments = null;

		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();
			request.setConfig(requestConfig);

			CloseableHttpResponse response = httpclient.execute(
					targetHost, request, context);

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findPath("results");

			if ((arrNode != null) && (arrNode.isNull() == false)) {
				departments = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamDepartment.class));
			} else {
				logger.error("getAllDepartments response from IAM not understood or was empty/null");
			}

			response.close();
		} catch (IOException e) {
			logger.error(exceptionStacktraceToString(e));
		}

		return departments;
	}

	/**
	 * Returns a list of all associations for the given IAM ID
	 * 
	 * @return list of associations as IamAssociation DTOs
	 */
	public List<IamAssociation> getAllAssociationsForIamId(Long iamId) {
		long startTime;

		String url = "/api/iam/associations/pps/" + iamId;
		HttpGet request = new HttpGet(url + "?v=1.0&key=" + apiKey);
		List<IamAssociation> associations = null;

		try {
			logger.debug("HTTP GET: " + url);
			startTime = new Date().getTime();

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();
			request.setConfig(requestConfig);

			CloseableHttpResponse response = httpclient.execute(
					targetHost, request, context);
			logger.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");

			if ((arrNode != null) && (arrNode.isNull() == false)) {
				associations = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamAssociation.class));
			} else {
				logger.warn("/api/iam/associations/pps/" + iamId + " response from IAM not understood or was empty/null");

				return null;
			}

			response.close();
		} catch (IOException e) {
			logger.error(exceptionStacktraceToString(e));
			return null;
		}

		return associations;
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
		HttpGet request = new HttpGet(url + "&v=1.0&key=" + apiKey);
		List<IamAssociation> associations = null;

		try {
			logger.debug("HTTP GET: " + url);
			startTime = new Date().getTime();

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();
			request.setConfig(requestConfig);

			CloseableHttpResponse response = httpclient.execute(
					targetHost, request, context);
			logger.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");

			if ((arrNode != null) && (arrNode.isNull() == false)) {
				associations = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamAssociation.class));
			} else {
				logger.warn("/api/iam/associations/pps/search?deptCode=" + deptCode + " response from IAM not understood or was empty/null");

				return null;
			}

			response.close();
		} catch (IOException e) {
			logger.error(exceptionStacktraceToString(e));
			return null;
		}

		return associations;
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
		HttpGet request = new HttpGet(url + "?v=1.0&key=" + apiKey);
		List<IamContactInfo> contactInfos = null;

		try {
			logger.debug("HTTP GET: " + url);
			startTime = new Date().getTime();

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();
			request.setConfig(requestConfig);

			CloseableHttpResponse response = httpclient.execute(
					targetHost, request, context);
			logger.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");

			if ((arrNode != null) && (arrNode.isNull() == false)) {
				contactInfos = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamContactInfo.class));
			} else {
				logger.warn("/api/iam/people/contactinfo/" + iamId + " response from IAM not understood or was empty/null");

				return null;
			}

			response.close();
		} catch (IOException e) {
			logger.error(exceptionStacktraceToString(e));
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
		HttpGet request = new HttpGet(url + "&v=1.0&key=" + apiKey);
		List<IamPerson> people = null;

		try {
			logger.debug("HTTP GET: " + url);
			startTime = new Date().getTime();

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();
			request.setConfig(requestConfig);

			CloseableHttpResponse response = httpclient.execute(
					targetHost, request, context);
			logger.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");

			if ((arrNode != null) && (arrNode.isNull() == false)) {
				people = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamPerson.class));
			} else {
				logger.warn("/api/iam/people/search?iamId=" + iamId + " response from IAM not understood or was empty/null");

				return null;
			}

			response.close();
		} catch (IOException e) {
			logger.error(exceptionStacktraceToString(e));
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
		HttpGet request = new HttpGet(url + "?v=1.0&key=" + apiKey);
		List<IamPrikerbacct> prikerbaccts = null;

		try {
			logger.debug("HTTP GET: " + url);
			startTime = new Date().getTime();

			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();
			request.setConfig(requestConfig);

			CloseableHttpResponse response = httpclient.execute(
					targetHost, request, context);
			logger.debug("HTTP GET took " + (new Date().getTime() - startTime) + "ms.");

			HttpEntity entity = response.getEntity();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			JsonNode rootNode = mapper.readValue(EntityUtils.toString(entity), JsonNode.class);
			JsonNode arrNode = rootNode.findParent("responseData");
			arrNode = rootNode.findPath("results");

			if ((arrNode != null) && (arrNode.isNull() == false)) {
				prikerbaccts = mapper.readValue(
						arrNode.toString(),
						mapper.getTypeFactory().constructCollectionType(
								List.class, IamPrikerbacct.class));
			} else {
				logger.warn("/api/iam/people/prikerbacct/" + iamId + " response from IAM not understood or was empty/null");

				return null;
			}

			response.close();
		} catch (IOException e) {
			logger.error(exceptionStacktraceToString(e));
			return null;
		}

		return prikerbaccts;
	}

	// Credit: http://hc.apache.org/httpcomponents-client-4.5.x/tutorial/html/fundamentals.html#d5e316
	private HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
		@Override
		public boolean retryRequest(
				IOException exception,
				int executionCount,
				HttpContext context) {
			if (executionCount >= MAX_RETRIES) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof InterruptedIOException) {
				// Timeout
				return false;
			}
			if (exception instanceof UnknownHostException) {
				// Unknown host
				return false;
			}
			if (exception instanceof ConnectTimeoutException) {
				// Connection refused
				return false;
			}
			if (exception instanceof SSLException) {
				// SSL handshake exception
				return false;
			}
			HttpClientContext clientContext = HttpClientContext.adapt(context);
			HttpRequest request = clientContext.getRequest();
			boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
			if (idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			
			return false;
		}
	};

	// Credit: http://stackoverflow.com/questions/10120709/difference-between-printstacktrace-and-tostring
	private static String exceptionStacktraceToString(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		ps.close();
		return baos.toString();
	}
}
