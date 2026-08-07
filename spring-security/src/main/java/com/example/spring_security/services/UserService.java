package com.example.spring_security.services;

import com.example.spring_security.dto.SignUpDto;
import com.example.spring_security.dto.UserDto;
import com.example.spring_security.entities.User;
import com.example.spring_security.exceptions.ResourceNotFoundException;
import com.example.spring_security.repo.UserRepository;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + username + "  not found"));
    }

    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
        if (user.isPresent()) {
            throw new BadCredentialsException("User with email " + signUpDto.getEmail() + "  already exists");
        }

        User toCreate = modelMapper.map(signUpDto, User.class);
        toCreate.setPassword(passwordEncoder.encode(toCreate.getPassword()));

        User savedUser = userRepository.save(toCreate);
        return modelMapper.map(savedUser, UserDto.class);
    }
}
