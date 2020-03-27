# Mercury backend settings

Mercury ships sensible default settings, but you'll need additional settings for it to work.
Create a file named `application.yaml` beside the JAR file and put the additional config
in there.

## Production ready settings

### Configure the database

At the moment, Mercury only supports PostgreSQL databases.

```yaml
spring.datasource:
  url: "jdbc:postgresql://localhost:5432/postgres"
  username: "postgres"
  password: "password"
```

Adjust the URL and the credentials to your database.

### Regenerate the JWT secrets

Regenerate every key listed below anew with `openssl rand -base64 32`.

```yaml
mercury:
  tokens:
    shopJwtSecret: "u2WXgWEWhyop+qtpiH5jhQmDN+PVRbazKPNuseI0dmE=" # openssl rand -base64 32
    adminJwtSecret: "QXOm/a5//OFtmciYomnixCTDCzNUQFWNAiT3eGBOfhQ=" # openssl rand -base64 32
    shopCreationJwtSecret: "8aaM086u9cpaNp8fMKURLOdpTEJYPqFy/xrN3M5ynT4=" # openssl rand -base64 32
    passwordResetJwtSecret: "71GRYLeFT2QzI/nq+g4UMk1hQ7Q+X2SvCU3ge5Nkpd8=" # openssl rand -base64 32
    reservationCancellationJwtSecret: "aOAUO26KvMsqgb+lRl2o1dQFziRMBnQI9mw5rMxabcE" # openssl rand -base64 32
```

### Enable email sending

First, enable email sending (otherwise Mercury will just write emails to stdout):

```yaml
mercury:
  email:
    use-dummy: false # Enable email sending
```

then configure your email provider:

```yaml
spring.mail:
  host: "smtp.gmail.com"
  port: 587
  username: "[username]"
  password: "[password]"
  properties:
    mail.smtp.starttls.enable: true # Server has STARTTLS enabled
```

OR

```yaml
spring.mail:
  host: "smtp.mailbox.org"
  protocol: "smtps" # Server speaks SSL/TLS
  port: 465
  username: "[username]"
  password: "[username]"
```

### Configure your domain

Mercury needs to know on which domain it runs:

```yaml
variables:
  domain: "https://demo.lokaler.kaufen"
```
