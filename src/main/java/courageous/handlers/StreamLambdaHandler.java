package courageous.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spark.SparkLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import courageous.webservices.Router;
import spark.Spark;

public class StreamLambdaHandler implements RequestStreamHandler {
    private static final Logger log = LogManager.getLogger(StreamLambdaHandler.class);
    private static final SparkLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SparkLambdaContainerHandler.getAwsProxyHandler();
            new Router().registerResources();
            Spark.awaitInitialization();
        } catch (ContainerInitializationException e) {
            // if we fail here. We re-throw the exception to force another cold start
            log.error("Could not initialize Spark container", e);
            throw new RuntimeException("Could not initialize Spark container", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}