package org.prgrms.kdt.customer.repository;

import org.prgrms.kdt.customer.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerJdbcRepository implements CustomerRepository {
    private static final Logger logger = LoggerFactory.getLogger(CustomerJdbcRepository.class);
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var customerName = resultSet.getString("name");
        var email = resultSet.getString("email");
        var lastLoginAt = resultSet.getTimestamp("last_login_at") != null ?
                resultSet.getTimestamp("last_login_at").toLocalDateTime() : null;
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

        return new Customer(customerId, customerName, email, lastLoginAt, createdAt);
    };

    public CustomerJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Customer insert(Customer customer) {
        var update = jdbcTemplate.update("INSERT INTO customers(customer_id, name, email, created_at) VALUES (UNHEX(REPLACE(?, '-', '')), ?, ?, ?)",
                customer.getCustomerId().toString(),
                customer.getName(),
                customer.getEmail(),
                Timestamp.valueOf(customer.getCreatedAt()));

        if(update != 1) {
            throw new RuntimeException("Nothing was inserted");
        }

        return customer;
    }

    @Override
    public Customer update(Customer customer) {
        var update = jdbcTemplate.update("UPDATE customers SET name = ?, email = ?, last_login_at = ? WHERE customer_id = UNHEX(REPLACE(?, '-', ''))",
                customer.getName(),
                customer.getEmail(),
                customer.getLastLoginAt() != null ? Timestamp.valueOf(customer.getLastLoginAt()) : null,
                customer.getCustomerId().toString()
        );

        if(update != 1) {
            throw new RuntimeException("Nothing was updated");
        }

        return customer;
    }

    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from customers", Integer.class);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query("select * from customers", customerRowMapper);
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE customer_id = UNHEX(REPLACE(?, '-', ''))",
                    customerRowMapper,
                    customerId.toString()));
        } catch(EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE name = ?",
                    customerRowMapper,
                    name));
        } catch(EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from customers WHERE email = ?",
                    customerRowMapper,
                    email));
        } catch(EmptyResultDataAccessException e) {
            logger.error("Got empty result", e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM customers");
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
