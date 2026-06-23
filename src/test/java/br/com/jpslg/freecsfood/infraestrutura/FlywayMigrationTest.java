package br.com.jpslg.freecsfood.infraestrutura;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
class FlywayMigrationTest {
    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");

    @Test
    void deveAplicarTodasAsMigrationsEmBancoLimpo() throws Exception {
        Flyway flyway = Flyway.configure()
                .dataSource(MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword())
                .locations("classpath:db/migration")
                .load();

        assertThat(flyway.migrate().migrationsExecuted).isEqualTo(1);
        assertThat(flyway.validateWithResult().validationSuccessful).isTrue();
        assertThat(contarRegistros("permissao")).isEqualTo(4);
        assertThat(contarRegistros("estado")).isEqualTo(3);
        assertThat(contarRegistros("cidade")).isEqualTo(4);
        assertThat(contarRegistros("cozinha")).isEqualTo(4);
        assertThat(contarRegistros("forma_pagamento")).isEqualTo(3);
        assertThat(contarRegistros("restaurante")).isEqualTo(4);
        assertThat(contarRegistros("produto")).isEqualTo(5);
        assertThat(contarRegistros("restaurante_forma_pagamento")).isEqualTo(11);
    }

    private long contarRegistros(String tabela) throws Exception {
        try (Connection connection = DriverManager.getConnection(
                MYSQL.getJdbcUrl(), MYSQL.getUsername(), MYSQL.getPassword());
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM " + tabela)) {
            result.next();
            return result.getLong(1);
        }
    }
}
