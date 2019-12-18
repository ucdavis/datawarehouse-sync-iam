# Requirements
MySQL
Java

# Installation
1. Create ~/.data-warehouse/settings.properties file
2. Set the following JVM system properties (environment variables seem to not work with HakariCP):
 * dw_sync_iam_jdbc_url
 * dw_sync_iam_jdbc_user
 * dw_sync_iam_jdbc_password
3. Create your local schema using schema/schema.sql
