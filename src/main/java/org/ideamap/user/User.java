package org.ideamap.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // 1. Tells Spring this class is a database entity
@Table(name = "users") // 2. Specifies the table name as "users" (a good practice)
public class User {

    @Id // 3. Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 4. Auto-generates the ID value
    private Long id;

    @Column(nullable = false, unique = true) // 5. Sets constraints: cannot be null, must be unique
    private String username;

    @Column(nullable = false) // 6. Cannot be null
    private String password;

    // --- Constructors, Getters, and Setters ---
    // A no-argument constructor is required by JPA
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters for all fields...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}