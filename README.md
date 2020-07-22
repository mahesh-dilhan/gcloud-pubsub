# Spring Boot Docker image deploy to GCP 
I followed below steps in order to deploy spring-boot app to GCP
* Create GCP account - Free Account
* Create a project `($PROJECT_ID)`
* Create a service Account - type Owner
* Download the google credential detail as json file . it is important to setup GOOGLE_APPLICATION_CREDENTIALS - if ignored, then Docker container will throws excpetions
* application property file 
* Enable Kubernetes API service
* Enable Container Registry
* Create topic and subcription
`topic -gtopic` and `subcription -gsubcription`
* Build docker image , tag, push to private registry `gcr.io/${PROJECT_ID}/image:tag`
`docker tag gcloud-pubsub:1 gcr.io/${PROJECT_ID}/gcloud-pubsub:1`
* verify images before push. Make sure not to push to public registry as you expose your credential
`docker push gcr.io/${PROJECT_ID}/gcloud-pubsub:1`
* Verify the image in private registry 
* Use Deploy container option in Kubernetes API
* Provide the name as gcloud-pubsub for Application
* Once deployed, expose the deployment as External Load balancer type providing port 8080:8080
* POST message using `curl` to public ip and view the container log



# Google PubSub integration with Spring Boot
In this repository
* Send message to channel
* Delegate message to pubsub template to push to pub-sub topic
* Listen to topic-subcription and consume message

### Pre-requisite
* open JDK 13
* gcloud project, service account and enable pubsub to project


```
/Library/Java/JavaVirtualMachines/adoptopenjdk-13.jdk/Contents/Home/bin/java -javaagent:/Applications/IntelliJ IDEA CE.app/Contents/lib/idea_rt.jar=57482:/Applications/IntelliJ IDEA CE.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Users/mahesh/play/gcloud/gcloud-pubsub/target/classes:/Users/mahesh/.m2/repository/org/springframework/cloud/spring-cloud-gcp-starter/1.2.3.RELEASE/spring-cloud-gcp-starter-1.2.3.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/cloud/spring-cloud-gcp-core/1.2.3.RELEASE/spring-cloud-gcp-core-1.2.3.RELEASE.jar:/Users/mahesh/.m2/repository/com/google/cloud/google-cloud-core/1.93.4/google-cloud-core-1.93.4.jar:/Users/mahesh/.m2/repository/com/google/guava/guava/29.0-android/guava-29.0-android.jar:/Users/mahesh/.m2/repository/com/google/guava/failureaccess/1.0.1/failureaccess-1.0.1.jar:/Users/mahesh/.m2/repository/com/google/guava/listenablefuture/9999.0-empty-to-avoid-conflict-with-guava/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar:/Users/mahesh/.m2/repository/org/checkerframework/checker-compat-qual/2.5.5/checker-compat-qual-2.5.5.jar:/Users/mahesh/.m2/repository/com/google/j2objc/j2objc-annotations/1.3/j2objc-annotations-1.3.jar:/Users/mahesh/.m2/repository/com/google/api/gax/1.56.0/gax-1.56.0.jar:/Users/mahesh/.m2/repository/com/google/auto/value/auto-value-annotations/1.7/auto-value-annotations-1.7.jar:/Users/mahesh/.m2/repository/com/google/protobuf/protobuf-java-util/3.11.4/protobuf-java-util-3.11.4.jar:/Users/mahesh/.m2/repository/com/google/code/gson/gson/2.8.6/gson-2.8.6.jar:/Users/mahesh/.m2/repository/com/google/api/grpc/proto-google-common-protos/1.17.0/proto-google-common-protos-1.17.0.jar:/Users/mahesh/.m2/repository/com/google/api/grpc/proto-google-iam-v1/0.13.0/proto-google-iam-v1-0.13.0.jar:/Users/mahesh/.m2/repository/org/threeten/threetenbp/1.4.3/threetenbp-1.4.3.jar:/Users/mahesh/.m2/repository/com/google/api/api-common/1.9.0/api-common-1.9.0.jar:/Users/mahesh/.m2/repository/com/google/auth/google-auth-library-credentials/0.20.0/google-auth-library-credentials-0.20.0.jar:/Users/mahesh/.m2/repository/com/google/auth/google-auth-library-oauth2-http/0.20.0/google-auth-library-oauth2-http-0.20.0.jar:/Users/mahesh/.m2/repository/com/google/http-client/google-http-client/1.35.0/google-http-client-1.35.0.jar:/Users/mahesh/.m2/repository/org/apache/httpcomponents/httpclient/4.5.12/httpclient-4.5.12.jar:/Users/mahesh/.m2/repository/commons-codec/commons-codec/1.14/commons-codec-1.14.jar:/Users/mahesh/.m2/repository/org/apache/httpcomponents/httpcore/4.4.13/httpcore-4.4.13.jar:/Users/mahesh/.m2/repository/io/opencensus/opencensus-contrib-http-util/0.24.0/opencensus-contrib-http-util-0.24.0.jar:/Users/mahesh/.m2/repository/com/google/http-client/google-http-client-jackson2/1.35.0/google-http-client-jackson2-1.35.0.jar:/Users/mahesh/.m2/repository/com/google/protobuf/protobuf-java/3.11.4/protobuf-java-3.11.4.jar:/Users/mahesh/.m2/repository/com/google/code/findbugs/jsr305/3.0.2/jsr305-3.0.2.jar:/Users/mahesh/.m2/repository/org/springframework/cloud/spring-cloud-gcp-autoconfigure/1.2.3.RELEASE/spring-cloud-gcp-autoconfigure-1.2.3.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/2.3.1.RELEASE/spring-boot-autoconfigure-2.3.1.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/boot/spring-boot-starter/2.3.1.RELEASE/spring-boot-starter-2.3.1.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/boot/spring-boot/2.3.1.RELEASE/spring-boot-2.3.1.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/boot/spring-boot-starter-logging/2.3.1.RELEASE/spring-boot-starter-logging-2.3.1.RELEASE.jar:/Users/mahesh/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar:/Users/mahesh/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar:/Users/mahesh/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.13.3/log4j-to-slf4j-2.13.3.jar:/Users/mahesh/.m2/repository/org/apache/logging/log4j/log4j-api/2.13.3/log4j-api-2.13.3.jar:/Users/mahesh/.m2/repository/org/slf4j/jul-to-slf4j/1.7.30/jul-to-slf4j-1.7.30.jar:/Users/mahesh/.m2/repository/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar:/Users/mahesh/.m2/repository/org/yaml/snakeyaml/1.26/snakeyaml-1.26.jar:/Users/mahesh/.m2/repository/org/slf4j/slf4j-api/1.7.30/slf4j-api-1.7.30.jar:/Users/mahesh/.m2/repository/org/springframework/cloud/spring-cloud-gcp-starter-pubsub/1.2.3.RELEASE/spring-cloud-gcp-starter-pubsub-1.2.3.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/cloud/spring-cloud-gcp-pubsub/1.2.3.RELEASE/spring-cloud-gcp-pubsub-1.2.3.RELEASE.jar:/Users/mahesh/.m2/repository/com/google/cloud/google-cloud-pubsub/1.105.0/google-cloud-pubsub-1.105.0.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-api/1.29.0/grpc-api-1.29.0.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-context/1.29.0/grpc-context-1.29.0.jar:/Users/mahesh/.m2/repository/org/codehaus/mojo/animal-sniffer-annotations/1.18/animal-sniffer-annotations-1.18.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-stub/1.29.0/grpc-stub-1.29.0.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-protobuf/1.29.0/grpc-protobuf-1.29.0.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-protobuf-lite/1.29.0/grpc-protobuf-lite-1.29.0.jar:/Users/mahesh/.m2/repository/com/google/api/grpc/proto-google-cloud-pubsub-v1/1.87.0/proto-google-cloud-pubsub-v1-1.87.0.jar:/Users/mahesh/.m2/repository/com/google/api/gax-grpc/1.56.0/gax-grpc-1.56.0.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-auth/1.29.0/grpc-auth-1.29.0.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-netty-shaded/1.29.0/grpc-netty-shaded-1.29.0.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-alts/1.29.0/grpc-alts-1.29.0.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-grpclb/1.29.0/grpc-grpclb-1.29.0.jar:/Users/mahesh/.m2/repository/org/apache/commons/commons-lang3/3.10/commons-lang3-3.10.jar:/Users/mahesh/.m2/repository/org/conscrypt/conscrypt-openjdk-uber/2.2.1/conscrypt-openjdk-uber-2.2.1.jar:/Users/mahesh/.m2/repository/io/opencensus/opencensus-api/0.26.0/opencensus-api-0.26.0.jar:/Users/mahesh/.m2/repository/io/grpc/grpc-core/1.29.0/grpc-core-1.29.0.jar:/Users/mahesh/.m2/repository/com/google/android/annotations/4.1.1.4/annotations-4.1.1.4.jar:/Users/mahesh/.m2/repository/io/perfmark/perfmark-api/0.19.0/perfmark-api-0.19.0.jar:/Users/mahesh/.m2/repository/com/google/errorprone/error_prone_annotations/2.3.4/error_prone_annotations-2.3.4.jar:/Users/mahesh/.m2/repository/javax/annotation/javax.annotation-api/1.3.2/javax.annotation-api-1.3.2.jar:/Users/mahesh/.m2/repository/org/springframework/integration/spring-integration-core/5.3.1.RELEASE/spring-integration-core-5.3.1.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/spring-aop/5.2.7.RELEASE/spring-aop-5.2.7.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/spring-beans/5.2.7.RELEASE/spring-beans-5.2.7.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/spring-context/5.2.7.RELEASE/spring-context-5.2.7.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/spring-expression/5.2.7.RELEASE/spring-expression-5.2.7.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/spring-messaging/5.2.7.RELEASE/spring-messaging-5.2.7.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/spring-tx/5.2.7.RELEASE/spring-tx-5.2.7.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/retry/spring-retry/1.2.5.RELEASE/spring-retry-1.2.5.RELEASE.jar:/Users/mahesh/.m2/repository/io/projectreactor/reactor-core/3.3.6.RELEASE/reactor-core-3.3.6.RELEASE.jar:/Users/mahesh/.m2/repository/org/reactivestreams/reactive-streams/1.0.3/reactive-streams-1.0.3.jar:/Users/mahesh/.m2/repository/org/springframework/boot/spring-boot-starter-web/2.3.1.RELEASE/spring-boot-starter-web-2.3.1.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/boot/spring-boot-starter-json/2.3.1.RELEASE/spring-boot-starter-json-2.3.1.RELEASE.jar:/Users/mahesh/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.11.0/jackson-databind-2.11.0.jar:/Users/mahesh/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.11.0/jackson-annotations-2.11.0.jar:/Users/mahesh/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.11.0/jackson-core-2.11.0.jar:/Users/mahesh/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jdk8/2.11.0/jackson-datatype-jdk8-2.11.0.jar:/Users/mahesh/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.11.0/jackson-datatype-jsr310-2.11.0.jar:/Users/mahesh/.m2/repository/com/fasterxml/jackson/module/jackson-module-parameter-names/2.11.0/jackson-module-parameter-names-2.11.0.jar:/Users/mahesh/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat/2.3.1.RELEASE/spring-boot-starter-tomcat-2.3.1.RELEASE.jar:/Users/mahesh/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/9.0.36/tomcat-embed-core-9.0.36.jar:/Users/mahesh/.m2/repository/org/glassfish/jakarta.el/3.0.3/jakarta.el-3.0.3.jar:/Users/mahesh/.m2/repository/org/apache/tomcat/embed/tomcat-embed-websocket/9.0.36/tomcat-embed-websocket-9.0.36.jar:/Users/mahesh/.m2/repository/org/springframework/spring-web/5.2.7.RELEASE/spring-web-5.2.7.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/spring-webmvc/5.2.7.RELEASE/spring-webmvc-5.2.7.RELEASE.jar:/Users/mahesh/.m2/repository/org/projectlombok/lombok/1.18.12/lombok-1.18.12.jar:/Users/mahesh/.m2/repository/org/springframework/spring-core/5.2.7.RELEASE/spring-core-5.2.7.RELEASE.jar:/Users/mahesh/.m2/repository/org/springframework/spring-jcl/5.2.7.RELEASE/spring-jcl-5.2.7.RELEASE.jar com.gcloud.gcloudpubsub.GcloudPubsubApplication

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.3.1.RELEASE)

2020-07-21 18:57:16.791  INFO 11034 --- [           main] c.g.g.GcloudPubsubApplication            : Starting GcloudPubsubApplication on Maheshs-MBP with PID 11034 (/Users/mahesh/play/gcloud/gcloud-pubsub/target/classes started by mahesh in /Users/mahesh/play/gcloud/gcloud-pubsub)
2020-07-21 18:57:16.793  INFO 11034 --- [           main] c.g.g.GcloudPubsubApplication            : No active profile set, falling back to default profiles: default
2020-07-21 18:57:17.500  INFO 11034 --- [           main] faultConfiguringBeanFactoryPostProcessor : No bean named 'errorChannel' has been explicitly defined. Therefore, a default PublishSubscribeChannel will be created.
2020-07-21 18:57:17.504  INFO 11034 --- [           main] faultConfiguringBeanFactoryPostProcessor : No bean named 'taskScheduler' has been explicitly defined. Therefore, a default ThreadPoolTaskScheduler will be created.
2020-07-21 18:57:17.506  INFO 11034 --- [           main] faultConfiguringBeanFactoryPostProcessor : No bean named 'integrationHeaderChannelRegistry' has been explicitly defined. Therefore, a default DefaultHeaderChannelRegistry will be created.
2020-07-21 18:57:17.541  INFO 11034 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.integration.config.IntegrationManagementConfiguration' of type [org.springframework.integration.config.IntegrationManagementConfiguration] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2020-07-21 18:57:17.547  INFO 11034 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'integrationChannelResolver' of type [org.springframework.integration.support.channel.BeanFactoryChannelResolver] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2020-07-21 18:57:17.548  INFO 11034 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'integrationDisposableAutoCreatedBeans' of type [org.springframework.integration.config.annotation.Disposables] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2020-07-21 18:57:17.792  INFO 11034 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2020-07-21 18:57:17.803  INFO 11034 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2020-07-21 18:57:17.804  INFO 11034 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.36]
2020-07-21 18:57:17.885  INFO 11034 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2020-07-21 18:57:17.886  INFO 11034 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1048 ms
2020-07-21 18:57:18.008  INFO 11034 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService 'taskScheduler'
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.springframework.util.ReflectionUtils (file:/Users/mahesh/.m2/repository/org/springframework/spring-core/5.2.7.RELEASE/spring-core-5.2.7.RELEASE.jar) to constructor java.lang.invoke.MethodHandles$Lookup(java.lang.Class)
WARNING: Please consider reporting this to the maintainers of org.springframework.util.ReflectionUtils
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
2020-07-21 18:57:18.093  INFO 11034 --- [           main] o.s.c.g.a.c.GcpContextAutoConfiguration  : The default project ID is gcloud-spring-pubsub
2020-07-21 18:57:18.142  WARN 11034 --- [           main] c.g.a.oauth2.DefaultCredentialsProvider  : Your application has authenticated using end user credentials from Google Cloud SDK. We recommend that most server applications use service accounts instead. If your application continues to use end user credentials from Cloud SDK, you might receive a "quota exceeded" or "API not enabled" error. For more information about service accounts, see https://cloud.google.com/docs/authentication/.
2020-07-21 18:57:18.142  INFO 11034 --- [           main] o.s.c.g.core.DefaultCredentialsProvider  : Default credentials provider for user 764086051850-6qr4p6gpi6hn506pt8ejuq83di341hur.apps.googleusercontent.com
2020-07-21 18:57:18.143  INFO 11034 --- [           main] o.s.c.g.core.DefaultCredentialsProvider  : Scopes in use by default credentials: [https://www.googleapis.com/auth/pubsub, https://www.googleapis.com/auth/spanner.admin, https://www.googleapis.com/auth/spanner.data, https://www.googleapis.com/auth/datastore, https://www.googleapis.com/auth/sqlservice.admin, https://www.googleapis.com/auth/devstorage.read_only, https://www.googleapis.com/auth/devstorage.read_write, https://www.googleapis.com/auth/cloudruntimeconfig, https://www.googleapis.com/auth/trace.append, https://www.googleapis.com/auth/cloud-platform, https://www.googleapis.com/auth/cloud-vision, https://www.googleapis.com/auth/bigquery]
2020-07-21 18:57:18.158  INFO 11034 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService 'pubsubPublisherThreadPool'
2020-07-21 18:57:18.195  INFO 11034 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService 'pubsubSubscriberThreadPool'
2020-07-21 18:57:18.204  INFO 11034 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'pubSubAcknowledgementExecutor'
2020-07-21 18:57:19.356  INFO 11034 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2020-07-21 18:57:19.590  INFO 11034 --- [           main] o.s.i.endpoint.EventDrivenConsumer       : Adding {logging-channel-adapter:_org.springframework.integration.errorLogger} as a subscriber to the 'errorChannel' channel
2020-07-21 18:57:19.590  INFO 11034 --- [           main] o.s.i.channel.PublishSubscribeChannel    : Channel 'application.errorChannel' has 1 subscriber(s).
2020-07-21 18:57:19.590  INFO 11034 --- [           main] o.s.i.endpoint.EventDrivenConsumer       : started bean '_org.springframework.integration.errorLogger'
2020-07-21 18:57:19.590  INFO 11034 --- [           main] o.s.i.endpoint.EventDrivenConsumer       : Adding {message-handler:inboundHandler.messageReceiver.serviceActivator} as a subscriber to the 'pubsubInputChannel' channel
2020-07-21 18:57:19.590  INFO 11034 --- [           main] o.s.integration.channel.DirectChannel    : Channel 'application.pubsubInputChannel' has 1 subscriber(s).
2020-07-21 18:57:19.590  INFO 11034 --- [           main] o.s.i.endpoint.EventDrivenConsumer       : started bean 'inboundHandler.messageReceiver.serviceActivator'
2020-07-21 18:57:19.590  INFO 11034 --- [           main] o.s.i.endpoint.EventDrivenConsumer       : Adding {message-handler:outboundHandler.messageDeligator.serviceActivator} as a subscriber to the 'OutboundChannel' channel
2020-07-21 18:57:19.590  INFO 11034 --- [           main] o.s.integration.channel.DirectChannel    : Channel 'application.OutboundChannel' has 1 subscriber(s).
2020-07-21 18:57:19.590  INFO 11034 --- [           main] o.s.i.endpoint.EventDrivenConsumer       : started bean 'outboundHandler.messageDeligator.serviceActivator'
2020-07-21 18:57:19.590  INFO 11034 --- [           main] ProxyFactoryBean$MethodInvocationGateway : started bean 'outboundHandler$ExternalMessageDeligator'
2020-07-21 18:57:19.591  INFO 11034 --- [           main] o.s.i.gateway.GatewayProxyFactoryBean    : started bean 'outboundHandler$ExternalMessageDeligator'
2020-07-21 18:57:19.611  INFO 11034 --- [           main] .s.c.g.p.i.i.PubSubInboundChannelAdapter : started bean 'messageChannelAdapter'; defined in: 'class path resource [com/gcloud/gcloudpubsub/InboundHandler.class]'; from source: 'org.springframework.core.type.classreading.SimpleMethodMetadata@7393222f'
2020-07-21 18:57:19.630  INFO 11034 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2020-07-21 18:57:19.642  INFO 11034 --- [           main] c.g.g.GcloudPubsubApplication            : Started GcloudPubsubApplication in 3.129 seconds (JVM running for 3.527)

```

### Send message to channel
```
Maheshs-MBP:gcloud-pubsub mahesh$  curl -XPOST localhost:8080/publishMessage/shawn
```

### output
```
020-07-21 19:17:01.631  INFO 11358 --- [nio-8080-exec-2] c.gcloud.gcloudpubsub.PubSubController   : message send to pubsub {}shawn
2020-07-21 19:17:02.981  INFO 11358 --- [sub-subscriber1] com.gcloud.gcloudpubsub.InboundHandler   : Received Message ..! Payload: GenericMessage [payload=byte[5], headers={gcp_pubsub_acknowledgement=org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter$1@6c83b901, id=ccd42ec0-5c38-0507-6e65-31123e028636, gcp_pubsub_original_message=PushedAcknowledgeablePubsubMessage{projectId='gcloud-spring-pubsub', subscriptionName='gtopic', message=data: "shawn"
message_id: "1398788025672693"
publish_time {
  seconds: 1595330221
  nanos: 982000000
}
}, timestamp=1595330222981}]
```