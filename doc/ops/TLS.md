# TLS

## Certbot

We manage the TLS certificates on the server via certbot. If you want a new subdomain,
prepare the `server` block for it in the `/etc/nginx/nginx.conf`, but without the

```
ssl_certificate /etc/letsencrypt/live/.../fullchain.pem;
ssl_certificate_key /etc/letsencrypt/live/.../privkey.pem;
```

lines. Then just run `certbot --nginx` and select all listed domains (e.g. by inputting `1,2,3,4,5`).
Certbot then notices that it could expand the existing certificate to the new subdomain.

If certbot asks if you want to redirect, answer `NO`, as we manage the redirects by ourself.
