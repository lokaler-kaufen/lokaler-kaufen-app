# Google Cloud Build FAQs

## How can I encrypt environment variables?

```bash
echo -n $MY_SECRET | gcloud kms encrypt --plaintext-file=- --ciphertext-file=- --location=global --keyring=lokalerkaufen --key=CLOUD_BUILD_KEY | base64
```

This command encrypts the value of the environment variable `$MY_SECRET` using the CryptoKey. The encrypted value is a base64-encoded string so you can easily include the value in your build request.

More details: https://cloud.google.com/cloud-build/docs/securing-builds/use-encrypted-secrets-credentials

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
