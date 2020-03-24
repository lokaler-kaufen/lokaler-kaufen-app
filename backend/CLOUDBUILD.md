# Google Cloud Build FAQs

## How can I encrypt environment variables?

```bash
echo -n $MY_SECRET | gcloud kms encrypt --plaintext-file=- --ciphertext-file=- --location=global --keyring=lokalerkaufen --key=CLOUD_BUILD_KEY | base64
```

## How can I run Cloud Build locally for debugging?

Install local builder

```bash
gcloud components install cloud-build-local
```

Run build on your local docker environment

```bash
cloud-build-local --config=cloudbuild.yaml --dryrun=false .
```

More details: https://cloud.google.com/cloud-build/docs/build-debug-locally
