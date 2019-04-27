build:
	mvn clean package

package: build
	aws cloudformation package --template-file cloudformation.yaml --output-template-file output-cloudformation.yaml --s3-bucket courageous-deploy

deploy: package
	aws cloudformation deploy --template-file output-cloudformation.yaml --stack-name ServerlessCourageousApp  --capabilities CAPABILITY_IAM

generate-services:
	docker run --rm \
		-v ${PWD}:/local openapitools/openapi-generator-cli generate \
		-i /local/openapi/courageous-tracking.yaml \
		-g typescript-angular \
		-o /local/website/api-services \
   		--additional-properties npmName=@angular-courageous/api-services,npmVersion=0.0.1,ngVersion=7.2.13

generate-services-swift:
	docker run --rm \
		-v ${PWD}:/local openapitools/openapi-generator-cli generate \
		-i /local/openapi/courageous-tracking.yaml \
		-g swift4 \
		-o /local/api-service-swift
   		--additional-properties projectName=courageous-api-services