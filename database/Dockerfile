FROM mysql:8.0.26

COPY mock_mysql.cnf /etc/mysql/conf.d/
RUN chmod 775 /etc/mysql/conf.d/mysql.cnf

COPY tables/*.sql /docker-entrypoint-initdb.d/
COPY mock_data /docker-entrypoint-initdb.d/

CMD ["--character-set-server=utf8mb4", "--collation-server=utf8mb4_general_ci"]