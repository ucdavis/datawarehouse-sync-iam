package edu.ucdavis.dss.aws;

import com.amazonaws.Request;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;

public class RequestSigner {
	public static void performSigningSteps(String serviceName, String region, Request<?> requestToSign) {
		AWS4Signer signer = new AWS4Signer();
		signer.setServiceName(serviceName);
		signer.setRegionName(region);

		// Get credentials
		AWSCredentialsProvider credsProvider = new EnvironmentVariableCredentialsProvider();

		AWSCredentials creds = credsProvider.getCredentials();

		// Sign request with supplied creds
		signer.sign(requestToSign, creds);
	}
}
