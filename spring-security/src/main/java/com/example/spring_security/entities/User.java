package com.example.spring_security.entities;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.spring_security.entities.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * @Entity: Marks this class as a JPA (Java Persistence API) Entity,
 *          mapping it to a database table (by default, named "user" or
 *          "users").
 */
@Entity
public class User implements UserDetails {

    /**
     * @Id: Specifies that this field is the primary key of the entity.
     * @GeneratedValue: Defines the strategy for primary key generation.
     *                  GenerationType.IDENTITY: The database will auto-increment
     *                  this ID column (e.g. auto_increment in MySQL).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column(unique = true): Instructs JPA/Hibernate to create a unique constraint
     *                on this column in the database table, preventing duplicate
     *                emails.
     */
    @Column(unique = true)
    private String email;
    private String name;
    private String password;

    /**
     * @ElementCollection: Used to declare a collection of basic types (like Enums,
     *                     Strings, Integers)
     *                     which will be stored in a separate join table managed
     *                     automatically by JPA.
     * 
     *                     fetch = FetchType.EAGER: Forces Hibernate to load the
     *                     collection immediately
     *                     when the parent User entity is loaded from the database.
     * 
     *                     @Enumerated(EnumType.STRING): Specifies that the Role
     *                     enum values should be stored
     *                     in the database as their String representation (e.g.,
     *                     "ADMIN", "USER") rather than their integer ordinal value.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public User() {
    }

    public User(Long id, String email, String name, String password, Set<Role> roles) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * getAuthorities(): Overridden from UserDetails interface.
     * Converts the user's enum Roles into SimpleGrantedAuthority objects (prefixed
     * with "ROLE_")
     * which Spring Security uses for role-based authorization checks.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    /**
     * getPassword(): Overridden from UserDetails.
     * Returns the hashed password used to authenticate the user.
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * getUsername(): Overridden from UserDetails.
     * Returns the unique identifier for the user (in our case, the email).
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    /**
     * isAccountNonExpired(): Indicates whether the user's account has expired.
     * Returning false makes the user unable to authenticate.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * isAccountNonLocked(): Indicates whether the user is locked or unlocked.
     * Returning false makes the user unable to authenticate.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * isCredentialsNonExpired(): Indicates whether the user's credentials
     * (password) has expired.
     * Returning false makes the user unable to authenticate.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * isEnabled(): Indicates whether the user is enabled or disabled.
     * Returning false makes the user unable to authenticate.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
