package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.DataBase;
import guru.qa.niffler.data.entity.*;
import guru.qa.niffler.data.jdbc.DataSourceProvider;
import guru.qa.niffler.data.sjdbc.UserEntityRowMapper;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserRepositoryJdbc implements UserRepository {

    private static final DataSource authDataSource = DataSourceProvider.dataSource(DataBase.AUTH);
    private static final DataSource udDataSource = DataSourceProvider.dataSource(DataBase.USERDATA);

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createUserInAuth(UserAuthEntity user) {
        try (Connection conn = authDataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement userPs = conn.prepareStatement(
                    "INSERT INTO \"user\" (" +
                            "username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)" +
                            " VALUES (?, ?, ?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );
                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO \"authority\" (" +
                                 "user_id, authority)" +
                                 " VALUES (?, ?)"
                 )) {
                userPs.setString(1, user.getUsername());
                userPs.setString(2, pe.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());
                userPs.executeUpdate();

                UUID generatedUserId = null;
                try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        generatedUserId = UUID.fromString(resultSet.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t access to id");
                    }
                }
                user.setId(generatedUserId);

                for (Authority a : Authority.values()) {
                    authorityPs.setObject(1, generatedUserId);
                    authorityPs.setString(2, a.name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }
                authorityPs.executeBatch();
                conn.commit();
                return user;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity createUserInUserdata(UserEntity user) {
        try (Connection conn = udDataSource.getConnection();
             PreparedStatement userPs = conn.prepareStatement(
                     "INSERT INTO \"user\" (" +
                             "username, currency, firstname, surname, photo, photo_small)" +
                             " VALUES (?, ?, ?, ?, ?, ?)",
                     RETURN_GENERATED_KEYS
             )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setObject(5, user.getPhoto());
            userPs.setObject(6, user.getPhotoSmall());
            userPs.executeUpdate();

            UUID generatedUserId = null;
            try (ResultSet resultSet = userPs.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedUserId = UUID.fromString(resultSet.getString("id"));
                } else {
                    throw new IllegalStateException("Can`t access to id");
                }
            }
            user.setId(generatedUserId);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserAuthEntity updateUserInAuth(UserAuthEntity user) {
        try (Connection connection = authDataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(
                    """
                            UPDATE "user"
                            SET username = ?,
                            password = ?,
                            enabled = ?,
                            account_non_expired = ?,
                            account_non_locked = ?,
                            credentials_non_expired= ?
                            WHERE id = ?""",
                    RETURN_GENERATED_KEYS);

                 PreparedStatement deleteAuthorityPs = connection.prepareStatement(
                         "DELETE FROM \"authority\" WHERE user_id = ?"
                 );

                 PreparedStatement authorityPs = connection.prepareStatement(
                         "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)"
                 )) {
                ps.setString(1, user.getUsername());
                ps.setString(2, pe.encode(user.getPassword()));
                ps.setBoolean(3, user.getEnabled());
                ps.setBoolean(4, user.getAccountNonExpired());
                ps.setBoolean(5, user.getAccountNonLocked());
                ps.setBoolean(6, user.getCredentialsNonExpired());
                ps.setObject(7, user.getId());
                ps.executeUpdate();

                deleteAuthorityPs.setObject(1, user.getId());
                deleteAuthorityPs.executeUpdate();

                for (AuthorityEntity a : user.getAuthorities()) {
                    authorityPs.setObject(1, user.getId());
                    authorityPs.setString(2, a.getAuthority().name());
                    authorityPs.addBatch();
                }
                authorityPs.executeBatch();

                connection.commit();
                return user;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity updateUserInUserdata(UserEntity user) {
        try (Connection connection = udDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     """
                             UPDATE "user"
                             SET username = ?,
                             "currency = ?,
                             "firstname = ?,
                             "surname = ?,
                             "photo = ?,
                             "photo_small = ?
                             "WHERE id = ?""")) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setObject(4, user.getSurname());
            ps.setObject(5, user.getPhoto());
            ps.setObject(6, user.getPhotoSmall());
            ps.setObject(7, user.getId());
            ps.executeUpdate();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findUserInUserdataById(UUID id) {
        try (Connection connection = udDataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM \"user\" WHERE id = ?"
             )) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserEntityRowMapper rowMapper = UserEntityRowMapper.instance;
                    UserEntity userEntity = rowMapper.mapRow(rs, 1);
                    return Optional.of(userEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}