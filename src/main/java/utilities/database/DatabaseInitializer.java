package utilities.database;

import persistence.repository.IActeurRepository;
import persistence.repository.IFilmRepository;
import persistence.repository.IPersonneRepository;
import persistence.repository.IRoleRepository;
import utilities.csvextractors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializer {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private FilmExtractor filmExtractor;

    @Autowired
    private ActorExtractor actorExtractor;

    @Autowired
    private RoleExtractor roleExtractor;

    @Autowired
    private RealisateurExtractor realisateurExtractor;

    @Autowired
    private IFilmRepository iFilmRepository;

    @Autowired
    private IActeurRepository iActeurRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private IPersonneRepository iPersonneRepository;

    @Autowired
    private CastingPrincipalExtractor castingPrincipalExtractor;

    @Autowired
    private FilmRealisateurExtractor filmRealisateurExtractor;

    public void createDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "CREATE SCHEMA IF NOT EXISTS bq6m2mlrafs4wqh3gcgt";
            statement.executeUpdate(sql);
            System.out.println("Database created or already exists.");

            // Ensure tables are created
            createTablesIfNotExist(connection);

            populateDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExist(Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();

        // Create Film table if not exists
        String createFilmTable = "CREATE TABLE IF NOT EXISTS film (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "annee VARCHAR(10) NOT NULL, " +
                "langue VARCHAR(100) NOT NULL, " +
                "rating VARCHAR(4) NOT NULL, " +
                "resume TEXT NOT NULL, " +
                "url VARCHAR(500))";
        connection.createStatement().executeUpdate(createFilmTable);
        System.out.println("Film table created or already exists.");

        // Create Personne table if not exists
        String createPersonneTable = "CREATE TABLE IF NOT EXISTS personne (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "nom VARCHAR(255) NOT NULL, " +
                "prenom VARCHAR(255), " +
                "date_naissance DATETIME, " +
                "lieu_naissance VARCHAR(255))";
        connection.createStatement().executeUpdate(createPersonneTable);
        System.out.println("Personne table created or already exists.");

        // Create Acteur table if not exists
        String createActeurTable = "CREATE TABLE IF NOT EXISTS acteur (" +
                "id_acteur BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "personne_id BIGINT, " +
                "id_imdb VARCHAR(255), " +
                "taille DOUBLE, " +
                "FOREIGN KEY (personne_id) REFERENCES personne(id))";
        connection.createStatement().executeUpdate(createActeurTable);
        System.out.println("Acteur table created or already exists.");

        // Create Role table if not exists
        String createRoleTable = "CREATE TABLE IF NOT EXISTS role (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "role_name VARCHAR(255) NOT NULL, " +
                "film_id BIGINT NOT NULL, " +
                "actor_id BIGINT NOT NULL)";
        connection.createStatement().executeUpdate(createRoleTable);
        System.out.println("Role table created or already exists.");

        // Create Film_Realisateur table if not exists
        String createFilmRealisateurTable = "CREATE TABLE IF NOT EXISTS film_realisateur (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "realisateur_id_imdb VARCHAR(255), " +
                "film_imdb VARCHAR(255))";
        connection.createStatement().executeUpdate(createFilmRealisateurTable);
        System.out.println("Film_Realisateur table created or already exists.");

        // Create Film_Acteur table if not exists
        String createFilmActeurTable = "CREATE TABLE IF NOT EXISTS film_acteur (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "acteur_id_imdb VARCHAR(255), " +
                "film_imdb VARCHAR(255))";
        connection.createStatement().executeUpdate(createFilmActeurTable);
        System.out.println("Film_Acteur table created or already exists.");
    }

    private boolean tableExists(DatabaseMetaData metaData, String tableName) throws SQLException {
        try (var rs = metaData.getTables(null, null, tableName.toUpperCase(), null)) {
            return rs.next();
        }
    }

    private void populateDatabase() {
        // Populate Film table
        //filmExtractor.extractAndSaveFilmsFromCSV("src/main/resources/CSV/films.csv");

        // Populate Realisateur table
        //realisateurExtractor.extractRealisateursFromCSV("src/main/resources/CSV/realisateurs.csv");

        // Populate Film_Realisateur table
        //filmRealisateurExtractor.extractAndSaveFilmRealisateurFromCSV("src/main/resources/CSV/film_realisateurs.csv");

        // Populate Casting Principal table
        //castingPrincipalExtractor.extractCastingPrincipalsFromCSV("src/main/resources/CSV/castingPrincipal.csv");

        // Populate Personne and Acteur tables
        //actorExtractor.extractActorsFromCSV("src/main/resources/CSV/acteurs.csv");

        // Populate Role table
        roleExtractor.extractRolesFromCSV("src/main/resources/CSV/roles.csv");
    }
}