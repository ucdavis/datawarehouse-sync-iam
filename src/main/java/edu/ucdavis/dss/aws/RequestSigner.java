package edu.ucdavis.dss.aws;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.signer.Aws4Signer;
import software.amazon.awssdk.auth.signer.params.Aws4SignerParams;
import software.amazon.awssdk.http.SdkHttpFullRequest;
import software.amazon.awssdk.regions.Region;

public class RequestSigner {
	public static SdkHttpFullRequest sign(String serviceName, String region, SdkHttpFullRequest request) {
		Aws4SignerParams signerParams = Aws4SignerParams.builder()
			.awsCredentials(EnvironmentVariableCredentialsProvider.create().resolveCredentials())
			.signingName(serviceName)
			.signingRegion(Region.of(region)).build();

		Aws4Signer signer = Aws4Signer.create();
		return signer.sign(request, signerParams);
	}
}
