package com.nandini.ledgersplit.service;

import com.nandini.ledgersplit.dto.UserRequestDTO;
import com.nandini.ledgersplit.dto.UserResponseDTO;
import com.nandini.ledgersplit.exception.UserNotFoundException;
import com.nandini.ledgersplit.model.User;
import com.nandini.ledgersplit.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        User savedUser =  userRepository.save(user);

        return mapToResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream().map(this::mapToResponseDTO).toList();
    }

    public UserResponseDTO getUserById(Long id){
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return mapToResponseDTO(existingUser);
    }

    public void deleteUserById(Long id){
        userRepository.delete(getUserEntityById(id));
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO updatedUser){
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());

        User savedUser = userRepository.save(existingUser);

        return mapToResponseDTO(savedUser);
    }

    private User getUserEntityById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private UserResponseDTO mapToResponseDTO(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

}
