# Turtle

##### A repository for messing around with ticker APIs.

## Local development setup
- From any cljs file, do cider-jack-in-clojurescript via Cider.
- For styling: `lein garden auto dev`.
- Navigate to `localhost:3449`.

## Deployment
- Ensure that the `api.tickerize.keigo.io` bucket exist in AWS S3.
- Start from a clean slate with `lein clean`.
- Build the backend with `lein uberjar`.
- Build the frontend with `lein cljsbuild once min`.
- Build the styling files: `lein garden once min`.
- Deploy the backend stack with `serverless deploy`.
- Deploy the frontend stack with `firebase deploy`.
- Tear the infrastructure down with `serverless remove`.
