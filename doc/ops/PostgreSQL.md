# PostgreSQL

## Create a database and a user with limited rights

```sql
create user mercury with encrypted password '<password>';
create database mercury;
grant all privileges on database mercury to mercury;
```
