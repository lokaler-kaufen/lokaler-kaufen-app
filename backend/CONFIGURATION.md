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

### Create a new master key

Regenerate the master key with `openssl rand -base64 32`:

```yaml
mercury:
  tokens:
    masterKey: "fZZP2re13vX7Q6xEWJDlAyYdzwtmZTmSEvjafucWlYg=" # Must be Base64, generated with openssl rand -base64 32
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
