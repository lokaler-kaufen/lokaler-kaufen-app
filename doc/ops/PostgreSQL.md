# PostgreSQL

## Create a database and a user with limited rights

```sql
create user mercury with encrypted password '<password>';
create database mercury;
grant all privileges on database mercury to mercury;
```

## Connect to the real databases (test / staging / production)

The database has a firewall which only allows traffic from the VM where the application is running. 
You can connect to the database by tunneling over the VM.

```
ssh lokaler.kaufen -L 2345:[ip of the database]:5432
```

The IP of the database can be found in the Cloud console. Then connect to PostgreSQL at `localhost:2345`

## Use TLS

1. Download the PostgreSQL server certificate from the Cloud console
1. Adjust the connection string, append `?sslmode=verify-ca&sslrootcert=/path/to/certificate.pem` at the end
