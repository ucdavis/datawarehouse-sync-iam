package edu.ucdavis.dss.iam.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ucdavis.dss.datawarehouse.sync.iam.ExceptionUtils;
import edu.ucdavis.dss.iam.dtos.IamBou;
import edu.ucdavis.dss.iam.dtos.IamContactInfo;
import edu.ucdavis.dss.iam.dtos.IamPerson;
import edu.ucdavis.dss.iam.dtos.IamPersonIdResult;
import edu.ucdavis.dss.iam.dtos.IamPpsAssociation;
import edu.ucdavis.dss.iam.dtos.IamPpsDepartment;
import edu.ucdavis.dss.iam.dtos.IamPrikerbacct;
import edu.ucdavis.dss.iam.dtos.IamSisAssociation;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IamClient {
	private final Logger logger = LoggerFactory.getLogger("IamLogger");
	private final CloseableHttpClient httpClient;
	private final String BASE_URL = "https://iet-ws.ucdavis.edu/api/iam";
	private final String apiKey;

	public IamClient(String apiKey) {
		Timeout TIMEOUT = Timeout.ofSeconds(30);
		PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
			.setDefaultConnectionConfig(
				ConnectionConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).build()).build();

		int MAX_RETRIES = 3;
		TimeValue RETRY_INTERVAL = TimeValue.ofSeconds(1);
		HttpRequestRetryStrategy retryStrategy = new DefaultHttpRequestRetryStrategy(MAX_RETRIES, RETRY_INTERVAL);

		this.httpClient =
			HttpClients.custom().setConnectionManager(connectionManager).setRetryStrategy(retryStrategy).build();
		this.apiKey = apiKey;
	}

	/**
	 * Returns a list of all departments
	 *
	 * @return list of departments as IamDepartment DTOs
	 */
	public List<IamPpsDepartment> getAllPpsDepartments() {
		HttpGet request = new HttpGet(BASE_URL + "/orginfo/pps/depts?v=1.0&key=" + apiKey);
		List<IamPpsDepartment> departments = null;

		try {
			departments = httpClient.execute(request, response -> parseResponse(response, IamPpsDepartment.class));
		} catch (IOException e) {
			logger.error(ExceptionUtils.stacktraceToString(e));
		}

		return departments;
	}

	/**
	 * Returns a list of all PPS associations for the given IAM ID
	 *
	 * @return list of PPS associations as IamPpsAssociation DTOs
	 */
	public List<IamPpsAssociation> getAllPpsAssociationsForIamId(Long iamId) {
		HttpGet request = new HttpGet(BASE_URL + "/associations/pps/" + iamId + "?v=1.0&key=" + apiKey);
		List<IamPpsAssociation> associations = null;

		try {
			associations = httpClient.execute(request, response -> parseResponse(response, IamPpsAssociation.class));
		} catch (IOException e) {
			logger.error(ExceptionUtils.stacktraceToString(e));
		}

		return associations;
	}

	/**
	 * Returns a list of all SIS associations for the given IAM ID
	 *
	 * @return list of SIS associations as IamSisAssociation DTOs
	 */
	public List<IamSisAssociation> getAllSisAssociationsForIamId(Long iamId) {
		HttpGet request = new HttpGet(BASE_URL + "/associations/sis/" + iamId + "?v=1.0&key=" + apiKey);
		List<IamSisAssociation> associations = null;

		try {
			associations = httpClient.execute(request, response -> parseResponse(response, IamSisAssociation.class));
		} catch (IOException e) {
			logger.error(ExceptionUtils.stacktraceToString(e));
		}

		return associations;
	}

	/**
	 * Returns the contact info entry for a given IamID
	 *
	 * @return List<IamContactInfo> or null
	 */
	public List<IamContactInfo> getContactInfo(Long iamId) {
		// First, get all people in the department
		HttpGet request = new HttpGet(BASE_URL + "/people/contactinfo/" + iamId + "?v=1.0&key=" + apiKey);
		List<IamContactInfo> contactInfos = null;

		try {
			contactInfos = httpClient.execute(request, response -> parseResponse(response, IamContactInfo.class));
		} catch (IOException e) {
			logger.error(ExceptionUtils.stacktraceToString(e));
		}

//		if(contactInfos.size() > 1) {
//			logger.warn("IAM returned " + contactInfos.size() + " contactInfos for IAM ID: " + iamId + " (email: " + contactInfos.get(0).getEmail() + ")");
//		}

		return contactInfos;
	}

	/**
	 * Returns the person entry(ies) for a given IamID
	 *
	 * @return List<IamPerson> or null
	 */
	public List<IamPerson> getPersonInfo(Long iamId) {
		// First, get all people in the department
		HttpGet request = new HttpGet(BASE_URL + "/people/search?iamId=" + iamId + "&v=1.0&key=" + apiKey);
		List<IamPerson> people = null;

		try {
			people = httpClient.execute(request, response -> parseResponse(response, IamPerson.class));
		} catch (IOException e) {
			logger.error(ExceptionUtils.stacktraceToString(e));
		}

		// People with pending work state returns an iamId with empty values in other fields, we'll ignore them
		if (people.stream().anyMatch(person -> person.getoFirstName() == null && person.getoLastName() == null)) {
			logger.warn("IAM returned a pending person with IAM ID: " + iamId);
			people.clear();
		}

//		if(people.size() > 1) {
//			logger.warn("IAM returned " + people.size() + " 'people' for IAM ID: " + iamId + " (mothra ID: " + people.get(0).getMothraId() + ")");
//		}

		return people;
	}

	/**
	 * Returns the person entry(ies) for a given IamID
	 *
	 * @return List<IamPerson> or null
	 */
	public List<IamPrikerbacct> getPrikerbacct(Long iamId) {
		HttpGet request = new HttpGet(BASE_URL + "/people/prikerbacct/" + iamId + "?v=1.0&key=" + apiKey);
		List<IamPrikerbacct> prikerbaccts = null;

		try {
			prikerbaccts = httpClient.execute(request, response -> parseResponse(response, IamPrikerbacct.class));
		} catch (IOException e) {
			logger.warn("/api/iam/people/prikerbacct/" + iamId + " response from IAM not understood or was empty/null");
		}

//		if(prikerbaccts.size() > 1) {
//			logger.warn("IAM returned " + prikerbaccts.size() + " prikerbaccts for IAM ID: " + iamId + " (user ID: " + prikerbaccts.get(0).getUserId() + ")");
//		}

		return prikerbaccts;
	}

	public List<IamBou> getAllBous() {
		HttpGet request = new HttpGet(BASE_URL + "/orginfo/pps/divisions?v=1.0&key=" + apiKey);
		List<IamBou> bous = null;

		try {
			bous = httpClient.execute(request, response -> parseResponse(response, IamBou.class));
		} catch (IOException e) {
			logger.error(ExceptionUtils.stacktraceToString(e));
		}

		return bous;
	}

	public List<IamPersonIdResult> getAllIamIds() {
		HttpGet request = new HttpGet(BASE_URL + "/people/ids?v=1.0&key=" + apiKey);
		List<IamPersonIdResult> iamIds = null;

		try {
			iamIds = httpClient.execute(request, response -> parseResponse(response, IamPersonIdResult.class));
		} catch (IOException e) {
			logger.error(ExceptionUtils.stacktraceToString(e));
		}

		return iamIds;
	}

	/**
	 * Returns iamIds updated in the last X amount of days
	 *
	 * @return List<IamPersonIdResult>
	 */
	public List<IamPersonIdResult> getModifiedIamIds() {
		int DAYS_TO_SUBTRACT = 2;
		String modifyDate =
			LocalDate.now(ZoneOffset.UTC).minusDays(DAYS_TO_SUBTRACT).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		HttpGet request =
			new HttpGet(BASE_URL + "/people/search?modifyDateAfter=" + modifyDate + "&v=1.0&key=" + apiKey);
		List<IamPersonIdResult> iamIds = null;

		try {
			iamIds = httpClient.execute(request, response -> parseResponse(response, IamPersonIdResult.class));
		} catch (IOException e) {
			logger.error(ExceptionUtils.stacktraceToString(e));
		}

		return iamIds;
	}

	private <T> List<T> parseResponse(ClassicHttpResponse response, Class<T> dtoClass) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			JsonNode rootNode = mapper.readValue(EntityUtils.toString(response.getEntity()), JsonNode.class);
			JsonNode arrNode = rootNode.findPath("results");

			if ((arrNode != null) && !arrNode.isNull()) {
				return mapper.readValue(arrNode.toString(),
					mapper.getTypeFactory().constructCollectionType(List.class, dtoClass));
			}
		} catch (IOException | ParseException e) {
			logger.error(ExceptionUtils.stacktraceToString(e));
		}

		return null;
	}
}